package sted.datastructures;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sted.datastructures.parseinfo.TVShowInfo;
import sted.datastructures.sources.SourceInfo;

public class StedXMLParser
{
    public static void main(String[] args) throws IOException {
        write(parse(new File("shows.xml")),new File("out.xml"), 1);
    }

    private StedXMLParser() {}

    public static final void write(List<TVShowInfo> l, File f, int version)
            throws IOException {
        FileWriter fw = new FileWriter(f);
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        fw.write("<sted>\n");
        fw.write("\t<version>" + version + "</version>\n");
        fw.write("\t<shows>\n");
        for (TVShowInfo i : l) {
            fw.write("\t\t<show id=\"" + i.getShowID() + "\">\n");
            fw.write("\t\t\t<name>" + escapeXML(i.getName()) + "</name>\n");
            fw.write("\t\t\t<minimumsize>" + i.getMinSize() + "</minimumsize>\n");
            fw.write("\t\t\t<maximumsize>" + i.getMaxSize() + "</maximumsize>\n");
            fw.write("\t\t\t<seeders>" + i.getMinSeeders() + "</seeders>\n");
            fw.write("\t\t\t<sources>\n");
            for (SourceInfo si : i.getSources()) {
                fw.write("\t\t\t\t<class>" + si.getClassName() + "</class>\n");
                if (si.getSearchName() != null)
                    fw.write("\t\t\t\t<searchname>" + si.getSearchName() + "</searchname>\n");
                if (si.getShowID() != null)
                    fw.write("\t\t\t\t<showid>" + si.getShowID() + "</showid>\n");

            }
            fw.write("\t\t\t</sources>\n");
            fw.write("\t\t</show>\n");
        }
        fw.write("\t</shows>\n");
        fw.flush();
        fw.close();
    }

    private static final List<TVShowInfo> parse(Element nodeList) {
        if (nodeList == null) return null;

        List<TVShowInfo> list = new LinkedList<TVShowInfo>();

        NodeList shows = nodeList.getElementsByTagName("show");

        // if there are shows
        if (shows != null && shows.getLength() > 0) {
            for (int i = 0; i < shows.getLength(); i++) {
                Element show = (Element) shows.item(i);
                String elName =
                        getTextValue(show, "name", "");

                TVShowInfo serie = null;
                // if we've found the show
                //if(name.equals(elName))
                //{
                // first check to see if it is a daily show
                // before creating the serie object
                String showID = null;
                String att = show.getAttribute("id");
                String tv_com = getTextValue(show, "tv_com", null);
                if (att != null && !att.isEmpty()) showID = att;
                else showID = tv_com;

                String daily = getTextValue(show, "daily", "");

                int min = getIntValue(show, "minimumsize", -1);

                int max = getIntValue(show, "maximumsize", -1);

                int seeders = getIntValue(show, "seeders", -1);

                String keywords = getTextValue(show, "keywords", "");

                if (daily.equals("True")) {
                    serie = new TVShowInfo(elName,
                            showID, min, max, seeders, true);
                } else {
                    serie = new TVShowInfo(elName,
                            showID, min, max, seeders, false);
                }

                String searchName1 = getTextValue(show, "searchname", null);
                String epguides = getTextValue(show, "epguides", null);
                if (searchName1 != null) {
                    serie.addSourceInfo(new SourceInfo("sted.datastructures.sources.TVRageSource",
                            searchName1, null));
                }
                if (epguides != null) {
                    serie.addSourceInfo(new SourceInfo("sted.datastructures.sources.EpguidesSource",
                            null, epguides));
                }


                NodeList sources = show.getElementsByTagName("sources");

                if (sources != null && sources.getLength() > 0) {
                    ///System.out.println(sources.getLength());
                    NodeList n = sources.item(0).getChildNodes();
                    //System.out.println(n.getLength());

                    if (n != null) {
                        for (int j = 0; j < n.getLength(); j++) {
                            //System.out.println(n.item(j));
                            Node theN = n.item(j);
                            if (theN.getNodeType() == Node.ELEMENT_NODE) {
                                Element source = (Element) n.item(j);
                                String className = getTextValue(source, "class", "");
                                String searchName = getTextValue(source, "searchname", null);
                                String showid = getTextValue(source, "showid", "");
                                serie.addSourceInfo(new SourceInfo(className, searchName, showid));
                            }
                        }
                    }
                }




                // set the feeds
                //Vector tempF = getVectorValue(show, "feeds", "feed");
                //Vector feeds = new Vector();
                //for(int j=0; j<tempF.size(); j++)
                //{
                //TedSerieFeed f = new TedSerieFeed((String)tempF.elementAt(j), 0);
                //feeds.addElement(f);
                //}
                //serie.setFeeds(feeds);

                // check to see if the from break option has to be used
                //String searchName = getTextValue(show, "searchname");
                //serie.setSearchName(searchName);

                //return serie;
                if (serie != null) {
                    list.add(serie);
                }
            }
        }

        return list;
    }
    
    public static final List<TVShowInfo> parse(File f) {
        Element nodeList = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = (Document) db.parse(f);
            nodeList = dom.getDocumentElement();
        } catch (SAXException ex) {
            Logger.getLogger(StedXMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StedXMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(StedXMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return parse(nodeList);
    }

    public static final List<TVShowInfo> parse(URL url) {
        Element nodeList = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Document dom = (Document) db.parse(conn.getInputStream());
            nodeList = dom.getDocumentElement();
        } catch (SAXException ex) {
            Logger.getLogger(StedXMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StedXMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(StedXMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return parse(nodeList);
    }

    public static final Element parseElement(URL url) {
        Element nodeList = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Document dom = (Document) db.parse(conn.getInputStream());
            nodeList = dom.getDocumentElement();
        } catch (SAXException ex) {
            Logger.getLogger(StedXMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StedXMLParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(StedXMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nodeList;
    }

    public static int getIntValue(Element elem, String object, int def) {
        try {
            return Integer.parseInt(getTextValue(elem, object, def + ""));
	} catch(NumberFormatException e) {
            return def;
	}
    }

    public static String getTextValue(Element elem, String object, String def) {
        String result = def == null ? null : new String(def);
        try {
            NodeList nl = elem.getElementsByTagName(object);
            if( nl != null && nl.getLength() > 0 ) {
                Element el1 = (Element)nl.item(0);
                if(el1.getFirstChild() != null)
                    result = el1.getFirstChild().getNodeValue();
            }
            return result;
        } catch(Exception e) {
            return result;
        }
    }

    private static String escapeXML(String s) {
        StringBuffer str = new StringBuffer();
        int len = (s != null) ? s.length() : 0;

        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '<':
                    str.append("&lt;");
                    break;
                case '>':
                    str.append("&gt;");
                    break;
                case '&':
                    str.append("&amp;");
                    break;
                case '"':
                    str.append("&quot;");
                    break;
                case '\'':
                    str.append("&apos;");
                    break;

                default:
                    str.append(ch);
            }
        }
        return str.toString();
    }
}

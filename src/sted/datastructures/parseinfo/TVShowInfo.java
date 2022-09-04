package sted.datastructures.parseinfo;

import sted.datastructures.sources.SourceException;
import sted.datastructures.tvshow.TVShow;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import sted.datastructures.sources.SourceInfo;
import sted.datastructures.sources.Source;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.StartTag;
import org.christianschenk.simplecache.SimpleCache;
import sted.datastructures.sources.SourceFactory;
import sted.datastructures.sources.TVRageSource;

public class TVShowInfo {
    
    ///////////////// ALL INSTANCES VARIABLES //////////////////
    
    private static final SimpleCache<String> CACHE =
            new SimpleCache<String>(12*60*60);

    public static String getHTML(String showID, String name) {
        return CACHE.get(showID + name + "$html");
    }

    public static String getStatus(String showID, String name) {
        return CACHE.get(showID + name + "$status");
    }

    private static void putHTML(String showID, String name,
            String html) {
        CACHE.put(showID + name + "$html", html);
    }

    private static void putStatus(String showID, String name,
            String status) {
        CACHE.put(showID + name + "$status", status);
    }

    ////////////////////////////////////////////////////////////

    private String name;
    private String showID;

    // imdb, tvrage, epguides, tv.com, tv.yahoo.com, etc.
    private Set<SourceInfo> sources;
    private SourceInfo tvRage = null;
    
    private int minSize;
    private int maxSize;
    private int minSeeders;

    // TODO not used
    private boolean isDaily;
    
    public TVShowInfo(String name, String showID,
            int minSize, int maxSize,
            int minSeeders, boolean isDaily) {
        this.showID = showID;
        this.name = name;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.minSeeders = minSeeders;
        this.isDaily = isDaily;
        this.sources = new HashSet<SourceInfo>();
    }

    private SourceInfo getTVRageSource() {
        if (tvRage == null)
            try {
            tvRage = new SourceInfo("sted.datastructures.sources.TVRageSource",
                    name, TVRageSource.getTVRageID(name));
        } catch (SourceException ex) {
            Logger.getLogger(TVShowInfo.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return tvRage;
    }

    public String getName() {
        return name;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getMinSeeders() {
        return minSeeders;
    }

    public int getMinSize() {
        return minSize;
    }

    public void addSourceInfo(SourceInfo sourceInfo) {
        sources.add(sourceInfo);
    }

    public Set<SourceInfo> getSources() {
        return sources;
    }

    public String getShowID() {
        return showID;
    }

    public boolean isDaily() {
        return isDaily;
    }

    @Override
    public String toString() {
        return name;
    }

    public synchronized TVShow grabInfo() {
        List<TVShow> shows = new LinkedList<TVShow>();
        Set<SourceInfo> ss = new HashSet<SourceInfo>();
        ss.add(getTVRageSource());
        ss.addAll(sources);
        for (SourceInfo s : ss) {
            try {
                Source source = SourceFactory.getSource(s.getClassName());
                TVShow tvShow = new TVShow(name);
                if (s.getShowID() != null) {
                    tvShow = source.grabInfoByID(name, s.getShowID());
                }
                if (tvShow == null && s.getSearchName() != null) {
                    tvShow = source.grabInfoByName(s.getSearchName());
                }
                if (tvShow == null) {
                    // try show name
                    tvShow = source.grabInfoByName(name);
                }
                if (tvShow != null) {
                    shows.add(tvShow);
                }
            } catch (SourceException ex) {
                Logger.getLogger(TVShowInfo.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
        if (!shows.isEmpty()) {
            Iterator<TVShow> it = shows.iterator();
            TVShow curr = it.next();
            while(it.hasNext())
                curr = curr.merge(it.next());
            return curr;
        } else return null;
    }

    public synchronized String getStatus() {
        // force getHTML
        getHTML();
        return getStatus(showID, name);
    }

    /**
     * Returns the html information of this show
     * from tv.com. This is a blocking method,
     * that waits for the data from tv.com.
     * If some exception ocurrs then the html response
     * of the method will contain the error. That is,
     * this method always return non null string.
     * @return the html info
     */
    public synchronized String getHTML() {
        if (showID == null)
            return "<htm>" +
                    "<h1>Invalid Show Identifier</h1>" +
                    "<p>Show identifier can't be null</p>" +
                   "</html>";
        String ret = getHTML(showID, name);
        if (ret != null) return ret;
        try {
            String summary = null;
            String tv_com = showID + ".jpg";
            String score = null;
            String description = null;
            String status = null;
            String premiered = null;
            String genre = null;
            URL url = new URL("http://www.tv.com/show/" +
                    showID + "/summary.html/");
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; " + 
                    "Windows NT 6.0; en-GB; rv:1.9.1.2) Gecko/20090729 " +
                    "Firefox/3.5.2 (.NET CLR 3.5.30729)");
            net.htmlparser.jericho.Source source =
                    new net.htmlparser.jericho.Source(conn.getInputStream());
            source.setLogger(null);
            List<StartTag> list = source.getAllStartTags();
            Element summaryElement = null,
                    ratingElement = null,
                    infoElement = null;
            for (StartTag t : list) {
                if (t.toString().contains(
                        "div class=\"MODULE\" id=\"show_buzz_info\""))
                    infoElement = t.getElement();
                if (t.toString().contains(
                        "div class=\"MODULE\" id=\"show_summary\""))
                    summaryElement = t.getElement();
                if (t.toString().contains(
                        "div class=\"global_rating score"))
                    ratingElement = t.getElement();
                if (summaryElement != null &&
                        ratingElement != null &&
                            infoElement != null) break;
            }

            if (infoElement != null) {
                Iterator<Segment> s = infoElement.getNodeIterator();
                boolean nextFetch = false;
                String res = "";
                while(s.hasNext()) {
                    Segment seg = s.next();
                    if (seg == null) continue;
                    String str = seg.toString();
                    if (str == null) continue;
                    str = str.trim();
                    if (str.isEmpty()) continue;
                    if (str.equals("<h4>") || str.equals("</div>"))
                        nextFetch = false;
                    else if (str.equals("</h4>")) nextFetch = true;
                    else if (nextFetch)
                        if (res.isEmpty()) res += str;
                        else res += "$$##" + str;
                }
                String [] arr = res.split("\\$\\$\\#\\#");
                if (arr.length >= 2) {
                    status = arr[0].trim();
                    premiered = arr[1].trim();
                    if (arr.length > 2) {
                        genre = "";
                        for (int i = 3 ; i < arr.length; i++)
                            genre += " " + arr[i];
                        if (genre.isEmpty()) genre = null;
                        genre = genre.trim();
                    }
                }
            }
            
            if (summaryElement != null) {
                StartTag summaryTag = summaryElement.
                        getFirstStartTag("p id=\"whole_summ\"");
                if (summaryTag != null) {
                    Element e = summaryTag.getElement();
                    if (e != null) {
                        Segment seg = e.getContent();
                        if (seg != null) {
                            String cc = seg.toString();
                            if (cc != null) cc = cc.trim();
                            summary = cc;
                        }
                    }
                }
            }
            if (ratingElement != null) {
                StartTag numberTag = ratingElement.
                        getFirstStartTag("span class=\"number\"");
                if (numberTag != null) {
                    Element e = numberTag.getElement();
                    if (e != null) {
                        Segment seg = e.getContent();
                        if (seg != null) {
                            String cc = seg.toString();
                            if (cc != null) cc = cc.trim();
                            score = cc;
                        }
                    }
                }
                StartTag descriptionTag = ratingElement.
                        getFirstStartTag("span class=\"description\"");
                if (descriptionTag != null) {
                    Element e = descriptionTag.getElement();
                    if (e != null) {
                        Segment seg = e.getContent();
                        if (seg != null) {
                            String cc = seg.toString();
                            if (cc != null) cc = cc.trim();
                            description = cc;
                        }
                    }
                }
            }

            String fullScore = score + " " + description;
            String res = "<html>" +
                    "<table width=\"200\" border=\"1\">" +
                        "<tr>" +
                            "<td colspan=\"4\">" +
                                "<img src=\"http://image.com.com/tv/images" +
                                    "/content_headers/program_new/" +
                                    tv_com + "\" width=\"480\" " +
                                    "height=\"200\" />" +
                            "</td>" +
                        "</tr>" +
                        "<tr>" +
                            "<td>" +
                                "<h3>Show Score</h3>" +
                                "<p>" + fullScore + "</p>" +
                            "</td>" +
                            "<td>" +
                                "<h3>Status</h3>" +
                                "<p>" + status + "</p>" +
                            "</td>" +
                            "<td>" +
                                "<h3>Premiered</h3>" +
                                "<p>" + premiered + "</p>" +
                            "</td>" +
                            "<td>" +
                                "<h3>Genre</h3>" +
                                "<p>" + genre + "</p>" +
                            "</td>" +
                        "</tr>" +
                        "<tr>" +
                            "<td colspan=\"4\">" +
                                "<h3>Summary</h3>" +
                                "<p>" + summary + "</p>" +
                            "</td>" +
                        "</tr>" +
                    "</table>" +
                "</html>";
            putHTML(showID, name, res);
            putStatus(showID, name, status);
            return res;
        } catch (MalformedURLException ex) {
            Logger.getLogger(TVShowInfo.class.getName())
                    .log(Level.SEVERE, null, ex);
            return "<htm><p>" + ex + "</p></html>";
        } catch (IOException ex) {
            Logger.getLogger(TVShowInfo.class.getName())
                    .log(Level.SEVERE, null, ex);
            return "<htm><p>" + ex + "</p></html>";
        }
    }
}
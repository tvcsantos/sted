/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sted.datastructures.sources;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import sted.datastructures.StedXMLParser;
import sted.datastructures.tvshow.Episode;
import sted.datastructures.tvshow.Season;
import sted.datastructures.tvshow.TVShow;

/**
 *
 * @author tvcsantos
 */
public class TVRageSource implements Source {

    private static final String HALFURL =
            "http://www.tvrage.com/feeds/episode_list.php?sid=";

    public TVRageSource() { }

    public void browse() throws IOException {
        //java.awt.Desktop.getDesktop().browse(uri);
    }

    public static final String getTVRageID(String showName)
            throws SourceException {
        String urlShowName = showName.replace(" ", "%20");

        // First we want to detect the id of this show on tvrage. For this we need
        // to parse the search results on the name of the show.
        String url = "http://www.tvrage.com/feeds/search.php?show=" +
                urlShowName;
        org.w3c.dom.Element foundShowsElement;
        try {
            foundShowsElement = StedXMLParser.parseElement(new URL(url));
        } catch (MalformedURLException ex) {
            SourceException se = new SourceException();
            se.initCause(ex);
            throw se;
        }

        // If there is no internet connection the xml file cannot be retrieved.
        if (foundShowsElement == null) {
            return null;
        }

        NodeList foundShowsList =
                foundShowsElement.getElementsByTagName("show");

        // Only continue if we've found at least one show with this name.
        String showId = "";
        if (foundShowsList != null && foundShowsList.getLength() > 0) {
            // For every show...
            for (int i = 0; i < foundShowsList.getLength(); i++) {
                // ... see if it's the exact same name as the show we're looking for.
                org.w3c.dom.Element show =
                        (org.w3c.dom.Element) foundShowsList.item(i);

                String foundName = StedXMLParser.getTextValue(show, "name", "");

                //System.out.println(foundName);

                // If not then try the next show.
                if (!foundName.toLowerCase().replace(":", "").
                        equals(showName.replace(":", "").toLowerCase())) {
                    continue;
                }

                // Otherwise we've found the show id
                showId = StedXMLParser.getTextValue(show, "showid", "");

                //System.out.println(showId);

                // No need to search further
                break;
            }
        }

        return showId;
    }

    public TVShow grabInfoByName(String tvShowName)
            throws SourceException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TVShow grabInfoByID(String tvShowName, String tvShowID)
            throws SourceException
    {
        try {
            if (tvShowID == null) return null;
            String showID = new String(tvShowID).trim();
            if (showID.isEmpty()) return null;

            Date parsedAirDate = null;
            String DATE_FORMAT = "yy-MM-dd";

	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);

            Element elem = StedXMLParser.parseElement(new URL(HALFURL + showID));

            if (elem == null) return null;

            NodeList foundSeasonsList = elem.getElementsByTagName("Episodelist");

            if (foundSeasonsList != null && foundSeasonsList.getLength() > 0)
            {
                // There is only on episode list
		Element episodeList = (Element)foundSeasonsList.item(0);

                // Which has multiple seasons
		NodeList seasonEpisodes = episodeList.getElementsByTagName("Season");

                TVShow result = new TVShow(new String(tvShowName));

		if (seasonEpisodes != null && seasonEpisodes.getLength() > 0)
		{
                    for (int i = 0; i < seasonEpisodes.getLength(); ++i)
                    {
			// For every season
			Element currentSeason = (Element)seasonEpisodes.item(i);

                        // For this season retrieve its season number. This is stored
			// as an attribute of this tag.
			int seasonNumber = Integer.parseInt(currentSeason.getAttribute("no"));

                        Season season = new Season(result, seasonNumber);

                        // Get all the episodes of that season
			NodeList episodesOfSeason = currentSeason.getElementsByTagName("episode");

			// Retrieve all the needed info
			if (episodesOfSeason != null && episodesOfSeason.getLength() > 0)
                        {
                            for (int z = 0; z < episodesOfSeason.getLength(); z++)
                            {
				Element episodeInfo = (Element)episodesOfSeason.item(z);
				int episodeNumber = StedXMLParser.getIntValue(episodeInfo, "seasonnum", -1);
				String title = StedXMLParser.getTextValue(episodeInfo, "title", "");
				Date airdate = null;
				try {
                                    String date = StedXMLParser.getTextValue(episodeInfo, "airdate", null);
                                    if (date == null) continue;
                                    // Sanity check on invalid months or days.
                                    if (!date.contains("-00"))
                                    {
                                        parsedAirDate = sdf.parse(date);
		                        //if (parsedAirDate.after(from) && parsedAirDate.before(to))
		                        //{
		                            airdate = parsedAirDate;
		                        //}

                                    }
                                } catch (ParseException pe) {
                                    continue;
                                }

                                Episode episode = new Episode(season, title, episodeNumber, airdate);

                                season.addEpisode(episode);
                            }
                        }

                        result.addSeason(season);
                    }
                }

                return result;
            }


        } catch (MalformedURLException ex) {
            SourceException se = new SourceException();
            se.initCause(ex);
            throw se;
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        TVShow show = new TVRageSource().grabInfoByID("Heroes","8172");
        System.out.println(show.toFormatedString());
    }
}
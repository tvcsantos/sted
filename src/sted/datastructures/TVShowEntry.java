/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sted.datastructures;

import pt.unl.fct.di.tsantos.util.exceptions.UnsupportedFormatException;
import sted.datastructures.parseinfo.TVShowInfo;
import sted.datastructures.tvshow.TVShow;
import sted.datastructures.tvshow.Episode;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.SortedSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.torrentsniffer.torrent.Torrent;
import net.sf.torrentsniffer.torrent.TorrentImpl;
import net.sf.torrentsniffer.torrent.TorrentInfo;
import org.tiling.scheduling.ScheduleIterator;
import org.tiling.scheduling.Scheduler;
import org.tiling.scheduling.SchedulerTask;
import pt.unl.fct.di.tsantos.util.Pair;
import pt.unl.fct.di.tsantos.util.net.RSSFeed;
import pt.unl.fct.di.tsantos.util.download.subtitile.SubtitleAttributes;
import pt.unl.fct.di.tsantos.util.exceptions.IncorrectSizeException;
import pt.unl.fct.di.tsantos.util.download.torrent.TorrentDownloader;

/**
 *
 * @author tvcsantos
 */
public class TVShowEntry extends Observable {
    private final TVShowInfo theShowInfo;
    private TVShow theShow;

    public class State {
        protected String message;

        public State() {

        }

        public State(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public class Enabled extends State {
        public Enabled() {
            this.message = "enabled";
        }

        @Override
        public void setMessage(String message) {
        }
    }

    public class Disabled extends State {
        public Disabled() {
            this.message = "disabled";
        }

        @Override
        public void setMessage(String message) {
        }
    }

    public class Play extends State {
        public Play() {
            this.message = "no torrents found";
        }

        @Override
        public void setMessage(String message) {
        }
    }

    public class Iddle extends State {
        public Iddle() {
        }

        public Iddle(String message) {
            super(message);
        }
    }

    public class Searching extends State {
        public Searching() {

        }

        public Searching(String message) {
            super(message);
        }

        protected float progress;

        public float getProgress() {
            return progress;
        }

        public void setProgress(float progress) {
            this.progress = progress;
        }
    }

    public class Downloading extends State {
        
        public Downloading() {

        }

        public Downloading(String message) {
            super(message);
        }
    }

    public class GettingStatus extends State {
        public GettingStatus() {
            this.message = "getting status info";
        }

        @Override
        public void setMessage(String message) {
        }
    }

    private State state = new Enabled();

    private void changeState(State state) {
        this.state = state;
        setChanged();
        notifyObservers(this.state);
    }

    public State getState() {
        return state;
    }

    private String title;
    private boolean HD;
    private int minSize;
    private int maxSize;
    private int minSeeders;
    private Keywords keywords;
    private List<RSSFeed> rssFeedList;
    private int progress;
    private String progressMessage;

    private Timer timer;
    private Scheduler scheduler;
    private TimerTask refresher;

    private int season;
    private int episode;

    private SchedulerTask downloader;

    private Episode scheduled;

    private final ModifiableScheduleIterator msi;
    private boolean scheduling = false;

    private File saveDir;

    private class ModifiableScheduleIterator implements ScheduleIterator {
        private long period;
        private Date next;

        public ModifiableScheduleIterator(long delay, long period) {
            long start = System.currentTimeMillis();
            this.period = period;
            this.next = new Date(start + delay);
        }

        public void setNext(Date next) {
            this.next = next;
        }
        
        public Date next() {
            Date res = next;
            System.out.println("RESULT: " + res);
            next = new Date(res.getTime() + period);
            System.out.println("NEXT: " + next);
            return res;
        }
    }

    public TVShowEntry(String title, TVShowInfo showInfo,
            TVShow tvShow, int season, int episode, File saveDir) {
        this.title = title;
        theShowInfo = showInfo;
        theShow = tvShow;
        HD = false;
        keywords = null;
        rssFeedList = new LinkedList<RSSFeed>();
        this.season = season;
        this.episode = episode;
        this.saveDir = saveDir;

        timer = new Timer();

        msi = new ModifiableScheduleIterator(0, 1*60*1000);

        downloader = newTask();

        scheduler = new Scheduler();

        refresher = new TimerTask() {
            @Override
            public void run() {
                // refresh TVShow info
                theShow = theShowInfo.grabInfo();
                update();
            }
        };
    }

    private SchedulerTask newTask() {
        return new SchedulerTask() {
            @Override
            public void run() {
                // check for torrents
                // download best torrent
                //changeStatusCode(TVShowEntry.PLAY);
                changeState(new Play());
                if (theShow == null) refresher.run();
                if (scheduled != null) {
                    Date airD = scheduled.getAirDate();
                    System.out.println(airD);
                    Date now = new Date();
                    if (airD != null && !airD.before(now)) {
                        msi.setNext(airD);
                    }
                }
                boolean found = parseFeeds();
                if (found) {
                    System.out.println("IUUUUUPPPPPPPPPPPIIII!");
                    int season = TVShowEntry.this.season;
                    int episode = TVShowEntry.this.episode;
                    Episode ep = theShow.getSeasonEpisode(
                            season, episode + 1);
                    if (ep == null) {
                        // perhaps end of season so try next season
                        ep = theShow.getSeasonEpisode(
                                season + 1, 1);
                        if (ep != null) {
                            season++;
                            episode = 1;
                        }
                    } else episode++;
                    if (ep == null)
                        // still null no luck force info refresh
                        //refresher.run();
                        return;
                    // schedule for next execution at next air date
                    // if no air date force refresh info
                    Date air = ep.getAirDate();
                    System.out.println(air);
                    if (air != null) msi.setNext(air);
                    setSeasonAndEpisode(season, episode);
                    changeState(new Iddle(Utils.getFormatedAirDate(
                        scheduled.getAirDate())));
                } else {
                    changeState(new Play());
                }
                //changeStatusCode(TVShowEntry.ACTIVE);
                
                //updateStatus(Utils.getFormatedAirDate(scheduled.getAirDate()));
            }
        };
    }

    public TVShowEntry(String title, TVShowInfo showInfo,
            TVShow tvShow, File saveDir) {
        this(title, showInfo, tvShow, 1, 1, saveDir);
    }

    public TVShowEntry(String title, TVShowInfo showInfo, File saveDir) {
        this(title, showInfo, null, saveDir);
    }

    public final TVShowInfo getInfo() {
        return theShowInfo;
    }

    public void startScheduling() {
        if (!scheduling)
            scheduler.schedule(downloader, msi);
        timer.schedule(refresher, 0, 20*60*1000);
        scheduling = true;
    }

    public void stopScheduling() {
        scheduler.cancel();
        timer.cancel();
    }

    private void setScheduled(Episode ep) {
        this.scheduled = ep;
        if (scheduled != null)
            changeState(
                    new Iddle(Utils.getFormatedAirDate(scheduled.getAirDate())));
            //updateStatus(Utils.getFormatedAirDate(scheduled.getAirDate()));
        else {
            changeState(new GettingStatus());
            //updateStatus("Getting Status Info");
            new Thread() {
                @Override
                public void run() {
                    changeState(new Iddle(theShowInfo.getStatus()));
                    //updateStatus(theShowInfo.getStatus());
                }
            }.start();
        }
        msi.setNext(new Date());
        downloader.cancel();
        downloader = newTask();
        scheduler.schedule(downloader, msi);
        scheduling = true;
    }

    public Keywords getKeywords() {
        return this.keywords;
    }

    public void setKeywords(Keywords kw) {
        this.keywords = kw;
    }

    private boolean parseFeeds() {
        URL bestURL = null;
        int bestSeeders = 0;
        TorrentInfo bestTorrentInfo = null;
        Searching state = new Searching();
        for (RSSFeed rssfeed : rssFeedList) {
            state.setMessage("loading " + rssfeed);
            state.setProgress(0f);
            changeState(state);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {}
            SyndFeed parseFeed = null;
            try {
                parseFeed = rssfeed.parseFeed();
            } catch(Exception e) {
                
            }
            if (parseFeed == null) continue;
            List<SyndEntry> entries = parseFeed.getEntries();
            if (entries == null) continue;
            int i = 0;
            int j = 0;
            float progress = 0f;
            float step = 1f/entries.size();
            for (Iterator<SyndEntry> it = entries.iterator(); it.hasNext() ;
                    progress += step) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {}
                state.setProgress(progress);
                changeState(state);
                SyndEntry entry = it.next();
                String stitle = entry.getTitle();
                String link = entry.getLink();
                if (link == null) continue;
                System.out.println(link.toString());
                System.out.println(stitle);
                if (keywords == null ||
                        keywords.match(stitle)) {
                    // ver se é a season e episodio que se quer
                    Pair<Integer, SortedSet<Integer>> seasonAndEpisodes = null;
                    try {
                        seasonAndEpisodes =
                                SubtitleAttributes.getSeasonAndEpisodes(stitle);
                    } catch (UnsupportedFormatException ex) {
                        System.err.println(ex);
                        continue;
                    }
                    if (seasonAndEpisodes.getFst().intValue() == season &&
                            seasonAndEpisodes.getSnd().contains(episode)) {
                        try {
                            URL url = new URL(entry.getLink());
                            Torrent torrent = new TorrentImpl(url);
                            TorrentInfo info = torrent.getInfo();
                            int seedersFromRSS =
                                    RSSFeed.getSeedersFromRSS(entry);
                            int seedersFromTorrent =
                                    TorrentDownloader.getSeedersFromTorrent(torrent);
                            int seeders = seedersFromRSS <= 0 ?
                                seedersFromTorrent : seedersFromRSS;
                            // ver o numero de seeders
                            // ver min/max size
                            // tudo ok ver se é melhor que o torrent actual
                            // ou seja em termos de seeders
                            // se é um proper ou repack ou whateva
                            boolean hasCorrectSize = false;
                            try {
                                hasCorrectSize = TorrentDownloader.
                               hasCorrectSize(torrent, entry, minSize, maxSize);
                            } catch (IncorrectSizeException ex) {
                                Logger.getLogger(TVShowEntry.class.getName()).
                                        log(Level.SEVERE, null, ex);
                                hasCorrectSize = false;
                            }

                            if (hasCorrectSize
                                    && seeders >= minSeeders) {
                                if (bestURL == null || bestSeeders < seeders) {
                                    bestURL = url;
                                    bestSeeders = seeders;
                                    bestTorrentInfo = info;
                                }
                            }

                        } catch (MalformedURLException ex) {
                            Logger.getLogger(TVShowEntry.class.getName())
                                    .log(Level.SEVERE, null, ex);
                            continue;
                        }
                    }
                }

                // if title match keywords
                //System.out.println(entry);
            }
        }

        // download do melhor torrent se houver!
        if (bestURL != null) {
            try {
                changeState(new Downloading("Downloading " 
                        + bestTorrentInfo.getName()));
                List<File> files = TorrentDownloader.download(bestURL,
                        bestTorrentInfo.getName(), saveDir);
                if (files != null) return true;
                return true;
            } catch (IOException ex) {
                return false;
            }
        }
        
        return false;
    }

    public void addRSSFeed(RSSFeed rssFeed) {
        rssFeedList.add(rssFeed);
    }

    public void setRSSFeeds(List<RSSFeed> rssFeedList) {
        this.rssFeedList = rssFeedList;
        new Thread() {
            @Override
            public void run() {
                downloader.run();
            }
        }.start();
    }

    public List<RSSFeed> getRSSFeeds() {
        return rssFeedList;
    }

    public int getSeason() {
        return season;
    }

    public int getEpisode() {
        return episode;
    }

    public boolean isHD() {
        return HD;
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

    public String getTitle() {
        return title;
    }

    public int getProgress() {
        return progress;
    }

    public String getSearch() {
        String ret = "Unknown Season/Episode.";
        if (scheduled == null && theShow != null) {
            Episode prev = theShow.getPreviousAiredEpisode();
            if (prev != null)
                ret = "Season: " + prev.getSeason().getNumber() +
                            ", Episode: " + (prev.getNumber() + 1) +
                            " or Season " + (prev.getSeason().getNumber() + 1) +
                            ". Unknown air date.";
        } else if (scheduled != null) {
            ret = "Season: " + scheduled.getSeason().getNumber() +
                ", Episode: " + scheduled.getNumber() +
                ": \"" + scheduled.getTitle() + "\"";
        }
        return ret;
    }

    public String getProgressMessage() {
        return progressMessage;
    }

    public String getStatus() {
        return null;
    }

    public void setHD(boolean HD) {
        this.HD = HD;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
        update();
    }

    public void setSeason(int season) {
        this.season = season;
        update();
    }

    public void setSeasonAndEpisode(int season, int episode) {
        this.season = season;
        this.episode = episode;
        update();   
    }

    private void update() {
        if (theShow != null) {
            setScheduled(theShow.getSeasonEpisode(this.season, this.episode));
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setMinSeeders(int minSeeders) {
        this.minSeeders = minSeeders;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }    
}

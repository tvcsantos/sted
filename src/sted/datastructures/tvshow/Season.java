package sted.datastructures.tvshow;

import sted.datastructures.*;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class Season {
    public static final Comparator<Season> COMPARATOR =
        new Comparator<Season>() {
            public int compare(Season o1, Season o2) {
                return o1.number < o2.number ? -1 :
                    o1.number == o2.number ? 0 : 1;
            }
        };

    protected TVShow tvShow;
    protected int number;
    protected SortedMap<Integer, Episode> map;

    public Season(TVShow tvShow, int number) {
        this.tvShow = tvShow;
        this.number = number;
        this.map = new TreeMap<Integer, Episode>();
    }

    public Episode getEpisode(int number) {
        return map.get(number);
    }

    public void addEpisode(Episode episode) {
        map.put(episode.getNumber(), episode);
    }

    public int getNumber() {
        return number;
    }

    public SortedSet<Episode> getEpisodes() {
        SortedSet<Episode> eps =
                new TreeSet<Episode>(Episode.COMPARATOR);
        eps.addAll(map.values());
        return eps;
    }

    public TVShow getTVShow() {
        return tvShow;
    }
}

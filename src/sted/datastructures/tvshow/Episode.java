package sted.datastructures.tvshow;

import java.util.Comparator;
import java.util.Date;

public class Episode {
    public static final Comparator<Episode> COMPARATOR =
        new Comparator<Episode>() {
            public int compare(Episode o1, Episode o2) {
                return o1.number < o2.number ? -1 :
                    o1.number == o2.number ? 0 : 1;
            }
        };

    protected Season season;
    protected String title;
    protected int number;
    protected Date airDate;

    public Episode(Season season, String title, int number, Date airDate) {
        this.season = season;
        this.title = title;
        this.number = number;
        this.airDate = airDate;
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public Date getAirDate() {
        return airDate;
    }

    public Season getSeason() {
        return season;
    }
}

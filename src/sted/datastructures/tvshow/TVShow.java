package sted.datastructures.tvshow;

import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

public class TVShow implements Comparable<TVShow> {
    protected String name;
    protected SortedMap<Integer, Season> map;

    public TVShow(String name) {
        this.name = name;

        this.map = new TreeMap<Integer, Season>();
    }

    public String getName() {
        return name;
    }

    public void addSeason(Season season) {
        map.put(season.getNumber(), season);
    }

    public Season getSeason(int number) {
        return map.get(number);
    }

    public Episode getSeasonEpisode(int season, int episode) {
        Season s = map.get(season);
        if (s == null) return null;
        Episode e = s.getEpisode(episode);
        return e;
    }

    public Episode getNextAirEpisode() {
        Date today = new Date();
        for (Entry<Integer,Season> s : map.entrySet()) {
            SortedSet<Episode> eps = s.getValue().getEpisodes();
            if (eps.isEmpty()) continue;
            Episode last = eps.last();
            Date airDate = last.getAirDate();
            if (airDate != null && airDate.before(today)) continue;
            for (Episode e : eps) {
                Date d = e.getAirDate();
                if (d != null && !d.before(today)) return e;
            }
        }
        return null;
    }

    public Episode getPreviousAiredEpisode() {
        Date today = new Date();
        Episode prev = null;
        for (Entry<Integer,Season> s : map.entrySet()) {
            SortedSet<Episode> eps = s.getValue().getEpisodes();
            for (Episode e : eps) {
                Date d = e.getAirDate();
                if (d != null) {
                    if (!d.before(today)) break;
                    else prev = e;
                }
            }
        }
        return prev;
    }

    public int compareTo(TVShow o) {
        return this.name.compareTo(o.name);
    }

    public String toFormatedString() {
        String res = "";
        res += "TVShow: " + name + "\n";
        for (Season s : map.values()) {
            res += "  Season " + s.getNumber() + "\n";
            for (Episode e : s.getEpisodes()) {
                res += "    Episode " + e.getNumber() + "\n";
                res += "      Title: " + e.getTitle() + "\n";
                res += "      Air Date: " + e.getAirDate() + "\n";
            }
        }
        return res;
    }

    public TVShow merge(TVShow o) {
        Iterator<Entry<Integer,Season>> thisSeasons =
                this.map.entrySet().iterator();
        Iterator<Entry<Integer,Season>> thatSeasons =
                o.map.entrySet().iterator();
        TVShow result = new TVShow(new String(name));
        Season thisSeason = null;
        Season thatSeason = null;
        if (thisSeasons.hasNext()) thisSeason = thisSeasons.next().getValue();
        if (thatSeasons.hasNext()) thatSeason = thatSeasons.next().getValue();
        while (thisSeason != null || thatSeason != null) {
            if (thisSeason != null && thatSeason == null) {
                result.addSeason(newSeason(result, thisSeason));
                thisSeason = null;
            } else if (thisSeason == null && thatSeason != null) {
                result.addSeason(newSeason(result, thatSeason));
                thatSeason = null;
            } else if (thisSeason != null && thatSeason != null) {
                int thisNumber = thisSeason.getNumber();
                int thatNumber = thatSeason.getNumber();
                if (thisNumber < thatNumber) {
                    result.addSeason(newSeason(result, thisSeason));
                    thisSeason = null;
                } else if (thisNumber > thatNumber) {
                    result.addSeason(newSeason(result, thatSeason));
                    thatSeason = null;
                } else {
                    // merge the season aka episodes in depth
                    Iterator<Episode> thisEpisodes =
                            thisSeason.getEpisodes().iterator();
                    Iterator<Episode> thatEpisodes =
                            thatSeason.getEpisodes().iterator();
                    Season sresult = new Season(result, thisNumber);
                    Episode thisEpisode = null;
                    Episode thatEpisode = null;
                    if (thisEpisodes.hasNext())
                        thisEpisode = thisEpisodes.next();
                    if (thatEpisodes.hasNext())
                        thatEpisode = thatEpisodes.next();
                    while (thisEpisode != null || thatEpisode != null) {
                    if (thisEpisode != null && thatEpisode == null) {
                        sresult.addEpisode(newEpisode(sresult, thisEpisode));
                        thisEpisode = null;
                    } else if (thisEpisode == null && thatEpisode != null) {
                        sresult.addEpisode(newEpisode(sresult, thatEpisode));
                        thatEpisode = null;
                    } else if (thisEpisode != null && thatEpisode != null) {
                        int thisEpNumber = thisEpisode.getNumber();
                        int thatEpNumber = thatEpisode.getNumber();
                        if (thisEpNumber < thatEpNumber) {
                            sresult.addEpisode(
                                    newEpisode(sresult, thisEpisode));
                            thisEpisode = null;
                        } else if (thisEpNumber > thatEpNumber) {
                            sresult.addEpisode(
                                    newEpisode(sresult, thatEpisode));
                            thatEpisode = null;
                        } else {
                            if (thisEpisode.getAirDate() != null
                                    && thatEpisode.getAirDate() == null)
                                sresult.addEpisode(
                                        newEpisode(sresult, thisEpisode));
                            else if (thisEpisode.getAirDate() == null
                                    && thatEpisode.getAirDate() != null)
                                sresult.addEpisode(
                                        newEpisode(sresult, thatEpisode));
                            else sresult.addEpisode(
                                    newEpisode(sresult, thisEpisode));
                            thisEpisode = null;
                            thatEpisode = null;
                        }
                    }
                    if (thisEpisode == null && thisEpisodes.hasNext())
                        thisEpisode = thisEpisodes.next();
                    if (thatEpisode == null && thatEpisodes.hasNext())
                        thatEpisode = thatEpisodes.next();
                    }
                    result.addSeason(sresult);
                    thisSeason = null;
                    thatSeason = null;
                }
            }
            if (thisSeason == null && thisSeasons.hasNext())
                thisSeason = thisSeasons.next().getValue();
            if (thatSeason == null && thatSeasons.hasNext())
                thatSeason = thatSeasons.next().getValue();
        }
        return result;
    }

    private static Season newSeason(TVShow show, Season s) {
        Season res = new Season(show, s.getNumber());
        for (Episode ep : s.getEpisodes()) {
            res.addEpisode(newEpisode(res, ep));
        }
        return res;
    }

    private static Episode newEpisode(Season season, Episode ep) {
        Date date = ep.getAirDate();
        return new Episode(season,
                    new String(ep.getTitle()), ep.getNumber(),
                    date == null ? null : (Date) date.clone());
    }
}

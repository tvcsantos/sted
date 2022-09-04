package sted.datastructures.sources;

import sted.datastructures.tvshow.TVShow;
import java.io.IOException;

public interface Source {
    public TVShow grabInfoByName(String tvShowName) throws SourceException;
    public TVShow grabInfoByID(String tvShowName, String tvShowID)
            throws SourceException;
    public void browse() throws IOException;
}


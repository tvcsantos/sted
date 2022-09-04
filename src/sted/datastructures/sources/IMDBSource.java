/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sted.datastructures.sources;

import java.io.IOException;
import sted.datastructures.tvshow.TVShow;

/**
 *
 * @author tvcsantos
 */
public class IMDBSource implements Source {

    public IMDBSource() { }

    public TVShow grabInfoByName(String tvShowName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TVShow grabInfoByID(String tvShowName, String tvShowID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void browse() throws IOException {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}

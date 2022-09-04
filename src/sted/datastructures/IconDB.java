/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sted.datastructures;

import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author tvcsantos
 */
public class IconDB {

    private IconDB() {}

    private static final String FOLDER = "icons";

    /*public static Icon getIconForShow(TVShowEntry entry) {
        switch(entry.getStatusCode()) {
            case TVShowEntry.ACTIVE:
                return new ImageIcon(FOLDER + File.separator + "tv.png");
            case TVShowEntry.PLAY:
                return new ImageIcon(FOLDER + File.separator + "play.png");
            case TVShowEntry.STOP:
                return new ImageIcon(FOLDER + File.separator + "stop.png");
            default:
                return new ImageIcon(FOLDER + File.separator + "tv.png");
        }
    }*/

    public static Icon getHDIcon() {
        return new ImageIcon(FOLDER + File.separator + "hd.png");
    }

    public static Icon getAddShowIcon() {
        return new ImageIcon(FOLDER + File.separator + "addShow.png");
    }

    public static Icon getDeleteShowIcon() {
        return new ImageIcon(FOLDER + File.separator + "deleteShow.png");
    }

    public static Icon getEditShowIcon() {
        return new ImageIcon(FOLDER + File.separator + "editShow.png");
    }

    public static Icon getRefreshShowIcon() {
        return new ImageIcon(FOLDER + File.separator + "refreshShow.png");
    }

    public static Icon getRefreshSubsIcon() {
        return new ImageIcon(FOLDER + File.separator + "refreshSubs.png");
    }

    public static Icon getGeneralIcon() {
        return new ImageIcon(FOLDER + File.separator + "general.png");
    }

    public static Icon getRSSIcon() {
        return new ImageIcon(FOLDER + File.separator + "rss.png");
    }

    public static Icon getFiltersIcon() {
        return new ImageIcon(FOLDER + File.separator + "filters.png");
    }

    public static Icon getScheduleIcon() {
        return new ImageIcon(FOLDER + File.separator + "schedule.png");
    }

    public static Icon getHelpIcon() {
        return new ImageIcon(FOLDER + File.separator + "help.png");
    }

    public static Icon getSearchIcon() {
        return new ImageIcon(FOLDER + File.separator + "search.png");
    }

    public static Icon getAddIcon() {
        return new ImageIcon(FOLDER + File.separator + "add.png");
    }

    public static Icon getRemoveIcon() {
        return new ImageIcon(FOLDER + File.separator + "remove.png");
    }

    public static Icon getUpIcon() {
        return new ImageIcon(FOLDER + File.separator + "up.png");
    }

    public static Icon getDownIcon() {
        return new ImageIcon(FOLDER + File.separator + "down.png");
    }

    public static Icon getOpenIcon() {
        return new ImageIcon(FOLDER + File.separator + "open.png");
    }
}

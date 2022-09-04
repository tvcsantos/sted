/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sted.datastructures;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author tvcsantos
 */
public class Utils {
    private Utils() {}

    public static final double difference(Date a, Date b) {
        return (a.getTime() - b.getTime())/86400000.0;
    }

    public static final String getFormatedAirDate(Date d) {
        if (d == null) return "Unknown air date";
        int diff = (int) Math.ceil(difference(d, new Date()));
        String result = "";
        switch (diff) {
            case 0:
                result = "Will air today";
                break;
            case 1:
                result = "Will air tomorow";
                break;
            case 2:
                result = "Will air on the day after tomorow";
                break;
            // if its within the next week just display the day of the week
            case 3:
            case 4:
            case 5:
            case 6: {
                System.out.println(TimeZone.getDefault().getOffset(new Date().getTime()));
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEEE", Locale.US);
                System.out.println(d);
                result = "Will air on " + dayFormat.format(d);
                break;
            }
            // if its more than a week away display the day and date
            default: {
                DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, Locale.US);
                if (diff < 0) result = "Aired on ";
                else result = "Will air on ";
                result += df.format(d);
                break;
            }
        }
        return result;
    }
}

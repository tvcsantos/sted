/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sted.datastructures;

import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

/**
 *
 * @author tvcsantos
 */
public class Language {

    private Locale locale;
    private TimeZone timeZone;
    private Properties lang;

    public Language(Locale locale, TimeZone timeZone) {
        this.lang = new Properties();
        // load file from this locale: "language_" + locale.toString() + ".properties"
        // if not found load : "language.properties"
        //lang.load(new FileInputStream(f));
        this.locale = locale;
        this.timeZone = timeZone;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }
}

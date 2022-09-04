/*
 * StedApp.java
 */

package sted;

import sted.gui.StedView;
import java.util.logging.LogRecord;
import org.jdesktop.application.Application;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Logger;
import pt.unl.fct.di.tsantos.util.FileUtils;
import pt.unl.fct.di.tsantos.util.app.DefaultSingleFrameApplication;
import sted.datastructures.StedXMLParser;
import sted.datastructures.TVShowEntry;
import sted.datastructures.parseinfo.TVShowInfo;

/**
 * The main class of the application.
 */
public class StedApp extends DefaultSingleFrameApplication {

    private List<TVShowInfo> showsInfo =
            new LinkedList<TVShowInfo>();

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        super.startup();
        showsInfo = StedXMLParser.parse(new File(getSettingsDirectory(),
                "shows.xml"));
    }

    @Override
    protected void showView() {
        show(new StedView(this));
    }

    /**
     * At startup create and show the main frame of the application.
     */
    /*@Override protected void startup() {
        showsInfo = StedXMLParser.parse(new File("shows.xml"));
        System.out.println(Locale.getDefault());
        initLoggers();
        show(new StedView(this));
    }*/


    private void initLoggers() {
        final Logger thisl = Logger.getLogger(StedApp.class.getName());
        Logger l1 = Logger.getLogger(TVShowEntry.class.getName());
        l1.addHandler(new Handler(){

            @Override
            public void publish(LogRecord record) {
                thisl.log(record);
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }

         });
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    public List<TVShowInfo> getShowsInfo() {
        return showsInfo;
    }

    public List<TVShowInfo> getShowsInfo(String regex) {
        List<TVShowInfo> res = new LinkedList<TVShowInfo>();
        for (TVShowInfo info : showsInfo)
            if (info.getName().toLowerCase()
                    .matches(regex.toLowerCase()))
                res.add(info);
        return res;
    }

    protected void createSettingsDirectory() {}

    @Override
    protected void populateSettingsDirectory() {
        File xmlFile = new File(getSettingsDirectory(), "shows.xml");
        if (!xmlFile.exists()) {
            try {
                InputStream is = StedApp.class.getResourceAsStream(
                        "resources/shows.xml");
                if (is == null) return;
                FileOutputStream fos = new FileOutputStream(xmlFile);
                FileUtils.copy(is, fos);
                is.close();
                fos.close();
            } catch (IOException ex) {
                // Should not happen
            }
        }
    }

    @Override
    protected void update() throws Exception {
        //updateShowList();
    }

    @Override
    protected String initSettingsDirectory() {
        return ".sted";
    }

    @Override
    protected URL initWebLocation() {
        try {
            String webLocation = getContext().getResourceMap()
                    .getString("Application.webLocation");
            return new URL(webLocation);
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    @Override
    protected String initName() {
        return "Super Torrent Episode Downloader";
    }

    @Override
    protected Long initUpdateInterval() {
        return new Long(12*60*60*1000);
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of StedApp
     */
    public static StedApp getApplication() {
        return Application.getInstance(StedApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(
                org.jdesktop.application.SessionStorage.class.getName());
        logger.setLevel(java.util.logging.Level.OFF);
        launch(StedApp.class, args);
    }
}

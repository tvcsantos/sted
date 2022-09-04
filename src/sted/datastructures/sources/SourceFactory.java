/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sted.datastructures.sources;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tvcsantos
 */
public class SourceFactory {
    private static Map<Class<?>, Source> sourcesCache = loadSources();

    private SourceFactory() {}

    private static final Map<Class<?>, Source> loadSources() {
        Map<Class<?>, Source> res = new HashMap<Class<?>, Source>();
        res.put(TVRageSource.class, new TVRageSource());
        res.put(IMDBSource.class, new IMDBSource());
        return res;
    }

    public static final Source getSource(Class<Source> sourceClass) {
        try {
            Source s = sourcesCache.get(sourceClass);
            if (s != null) {
                s = sourceClass.newInstance();
                sourcesCache.put(sourceClass, s);
            }
            return s;
        } catch (InstantiationException ex) {
            return null;
        } catch (IllegalAccessException ex) {
            return null;
        }
    }

    public static final Source getSource(String className) {
        try {
            Class<Source> theClass = (Class<Source>) Class.forName(className);
            return getSource(theClass);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    public static void main(String[] args) {
        Source source = SourceFactory.getSource("sted.datastructures.sources.IMDBSource");
        System.out.println(source);
        if (source instanceof IMDBSource) System.out.println("yes!!!");
    }
}



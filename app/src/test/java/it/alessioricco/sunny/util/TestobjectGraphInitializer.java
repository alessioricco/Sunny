package it.alessioricco.sunny.util;

import dagger.ObjectGraph;
import it.alessioricco.sunny.injection.ObjectGraphSingleton;

/**
 * Created by alessioricco on 22/10/2016.
 */

public class TestObjectGraphInitializer {

    static public void init() {

        TestAppModules[] modules = {
                new TestAppModules()
        };
        ObjectGraph objectGraph = ObjectGraph.create((Object[]) modules);
        ObjectGraphSingleton.reset();
        ObjectGraphSingleton.setInstance(objectGraph);
    }

    static public void close() {
        ObjectGraphSingleton.reset();
    }
}

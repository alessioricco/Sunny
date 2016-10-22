package it.alessioricco.sunny.injection;

import dagger.ObjectGraph;

/**
 * Created by alessioricco on 22/10/2016.
 *
 * singleton used for manage dagger objec graphs
 *
 */

public class ObjectGraphSingleton {
    private static ObjectGraph instance = null;

    public static ObjectGraph getInstance() {
        return ObjectGraphSingleton.instance;
    }

    public static void reset() {
        ObjectGraphSingleton.instance = null;
    }

    public static void setInstance(ObjectGraph instance) {
        if (ObjectGraphSingleton.instance == null) {
            ObjectGraphSingleton.instance = instance;
        } else {
            throw new RuntimeException("Invalid assignment: the instance has been signed already");
        }

    }
}

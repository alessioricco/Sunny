package it.alessioricco.sunny.util;

import dagger.Module;
import it.alessioricco.sunny.injection.AppModule;

/**
 * Created by alessioricco on 22/10/2016.
 */


@Module(
        includes = {
                AppModule.class
        },
        injects = {

        },
        library = true)
public class TestAppModules {
}


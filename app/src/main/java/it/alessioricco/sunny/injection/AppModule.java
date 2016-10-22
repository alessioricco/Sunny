package it.alessioricco.sunny.injection;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.alessioricco.sunny.MainActivity;
import it.alessioricco.sunny.MainApp;

/**
 * Created by alessioricco on 22/10/2016.
 */

@Module(
        injects = {
                MainApp.class,
                MainActivity.class
        },
        library = true)

public class AppModule {

    private MainApp app; // App: constructor
    private MainApp testApp; // Test: constructor and environment

    /**
     * constructor for the main android app
     * @param app the application itself
     */
    public AppModule(MainApp app) {
        this.app = app;
    }

    /**
     * constructor for unit test
     */
    public AppModule() {
        try {
            if (testApp == null) {
                testApp = new MainApp();
            }
            app = testApp;
        } catch (Exception e)
        {/* do nothing */}
    }


}

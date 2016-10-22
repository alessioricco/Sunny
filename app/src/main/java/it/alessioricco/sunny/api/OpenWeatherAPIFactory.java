package it.alessioricco.sunny.api;

import retrofit2.Retrofit;

/**
 * Created by alessioricco on 22/10/2016.
 *
 * factory for the openweather api
 */

public class OpenWeatherAPIFactory {
    public static OpenWeatherAPI createWeatherAPI(Retrofit retrofit) {
        return retrofit.create(OpenWeatherAPI.class);
    }
}

package it.alessioricco.sunny.api;

import it.alessioricco.sunny.models.Forecast;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by alessioricco on 22/10/2016.
 *
 * retrofit interfaces for API call
 */

public interface OpenWeatherAPI {

    @GET("/data/2.5/forecast")
    Observable<Forecast> getForecast(@Query("id") long id, @Query("units") String unit, @Query("APPID") String appid);

    @GET("/data/2.5/forecast")
    Observable<Forecast> getForecastByCity(@Query("q") String q, @Query("units") String unit, @Query("APPID") String appid);

    @GET("/data/2.5/forecast")
    Observable<Forecast> getForecastByCoord(@Query("q") String q, @Query("units") String unit, @Query("APPID") String appid);
}

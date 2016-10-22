package it.alessioricco.sunny.services;

import javax.inject.Inject;

import it.alessioricco.sunny.api.OpenWeatherAPI;
import it.alessioricco.sunny.api.OpenWeatherAPIFactory;
import it.alessioricco.sunny.api.RestAdapterFactory;
import it.alessioricco.sunny.injection.ObjectGraphSingleton;
import it.alessioricco.sunny.models.Forecast;
import rx.Observable;
import util.Environment;

/**
 * Created by alessioricco on 22/10/2016.
 *
 * service for retrieve forecasts
 */

public class OpenWeatherService {

    @Inject
    RestAdapterFactory restAdapterFactory;

    public OpenWeatherService() {

        ObjectGraphSingleton.getInstance().inject(this);
    }

    /**
     * Returns all bookings for the current user
     *
     * @return user's bookings
     */
    public Observable<Forecast> getForecast(final long id, final String units) {

        final OpenWeatherAPI api = OpenWeatherAPIFactory.createWeatherAPI(RestAdapterFactory.getJSONRestAdapter());

        return api.getForecast(id, units, Environment.OPENWEATHER_API_APPID);
    }

}

package it.alessioricco.sunny.models;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by alessioricco on 22/10/2016.
 *
 * model for the single forecast item
 * {
 "dt":1406106000,
 "main":{
 "temp":298.77,
 "temp_min":298.77,
 "temp_max":298.774,
 "pressure":1005.93,
 "sea_level":1018.18,
 "grnd_level":1005.93,
 "humidity":87
 "temp_kf":0.26},
 "weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04d"}],
 "clouds":{"all":88},
 "wind":{"speed":5.71,"deg":229.501},
 "sys":{"pod":"d"},
 "dt_txt":"2014-07-23 09:00:00"}
 */

public class ForecastItem implements Serializable {
    @Getter
    @Setter
    long dt;
    @Getter @Setter
    Main main;
    @Getter @Setter
    List<Weather> weather;
    @Getter @Setter Clouds clouds;
    @Getter @Setter Wind wind;
    @Getter @Setter String dt_txt;
}

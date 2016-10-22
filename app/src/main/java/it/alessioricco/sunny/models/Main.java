package it.alessioricco.sunny.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by alessioricco on 22/10/2016.
 *
 * model for the "main" object
 * {
 "temp":298.77,
 "temp_min":298.77,
 "temp_max":298.774,
 "pressure":1005.93,
 "sea_level":1018.18,
 "grnd_level":1005.93,
 "humidity":87
 "temp_kf":0.26}
 */

public class Main implements Serializable {
    @Getter
    @Setter
    float temp;
    @Getter @Setter float temp_min;
    @Getter @Setter float temp_max;
    @Getter @Setter float pressure;
    @Getter @Setter float sea_level;
    @Getter @Setter float grnd_level;
    @Getter @Setter float humidity;
    @Getter @Setter float temp_kf;
}

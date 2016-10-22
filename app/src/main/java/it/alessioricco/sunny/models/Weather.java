package it.alessioricco.sunny.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by alessioricco on 22/10/2016.
 *
 * weather object
 * {"id":804,"main":"Clouds","description":"overcast clouds","icon":"04d"}
 * //todo: icons maybe can be retrieved from the service
 */

public class Weather implements Serializable {
    @Getter
    @Setter
    long id;
    @Getter @Setter String main;
    @Getter @Setter String description;
    @Getter @Setter String icon;
}

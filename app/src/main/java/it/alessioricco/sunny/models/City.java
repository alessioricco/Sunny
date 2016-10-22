package it.alessioricco.sunny.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by alessioricco on 22/10/2016.
 *
 * city object
 * the structure is quite different from the one given
 * because there is a typo in the website
 *
 * {"id":1851632,
 * "name":"Shuzenji",
 * "coord":{"lon":138.933334,"lat":34.966671},
 * "country":"JP"}
 *
 */

public class City implements Serializable {

    @Getter @Setter long _id;
    @Getter @Setter String name;
    @Getter @Setter String country;
    @Getter @Setter Coord coord;
}

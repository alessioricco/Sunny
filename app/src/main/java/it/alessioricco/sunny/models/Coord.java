package it.alessioricco.sunny.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by alessioricco on 22/10/2016.
 *
 * coordinates
 */

public class Coord implements Serializable {
    @Getter @Setter double lon;
    @Getter @Setter double lat;
}

package it.alessioricco.sunny.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by alessioricco on 22/10/2016.
 *
 * wind object
 * {"speed":5.71,"deg":229.501}
 */

public class Wind implements Serializable {
    @Getter @Setter float speed;
    @Getter @Setter float deg;
}

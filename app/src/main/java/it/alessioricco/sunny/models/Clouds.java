package it.alessioricco.sunny.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by alessioricco on 22/10/2016.
 *
 * clouds object
 * {"all":88}
 * todo: could we ignore it?
 */

class Clouds implements Serializable {
    @Getter
    @Setter
    int all;
}

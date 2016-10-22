package it.alessioricco.sunny.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import it.alessioricco.sunny.R;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by alessioricco on 22/10/2016.
 *
 * settings storage
 */

public class Settings {

    //todo: we can use a resource id
    @Getter @Setter String unit;

    public static String getPreferredLocation(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_location_key),
                context.getString(R.string.pref_location_default));
    }

    public static boolean isMetric(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_units_key),
                context.getString(R.string.pref_units_metric))
                .equals(context.getString(R.string.pref_units_metric));
    }
}

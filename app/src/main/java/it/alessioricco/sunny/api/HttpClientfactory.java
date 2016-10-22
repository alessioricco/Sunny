package it.alessioricco.sunny.api;

import com.squareup.okhttp.OkHttpClient;

/**
 * Created by alessioricco on 22/10/2016.
 *
 * Factory for http clients
 */

public class HttpClientFactory {
    final public OkHttpClient getHttpClient() {
        return new OkHttpClient();
    }

}

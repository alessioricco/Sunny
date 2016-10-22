package it.alessioricco.sunny.api;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Inject;

import it.alessioricco.sunny.injection.ObjectGraphSingleton;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;
import util.Environment;

/**
 * Created by alessioricco on 22/10/2016.
 */

public class RestAdapterFactory {

    @Inject
    OkHttpClient httpClient;

    static final String url = Environment.OPENWEATHER_API_URL;

    public RestAdapterFactory() {
        ObjectGraphSingleton.getInstance().inject(this);
    }

    public static Retrofit getJSONRestAdapter() {

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .build();

    }


}

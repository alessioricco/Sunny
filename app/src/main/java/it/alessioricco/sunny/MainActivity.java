package it.alessioricco.sunny;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.ObjectGraph;
import it.alessioricco.sunny.injection.ObjectGraphSingleton;
import it.alessioricco.sunny.models.Forecast;
import it.alessioricco.sunny.models.Settings;
import it.alessioricco.sunny.services.OpenWeatherService;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import util.Environment;

public class MainActivity extends AppCompatActivity {

    // dagger
    @Inject
    OpenWeatherService weatherService;

    protected CompositeSubscription compositeSubscription = new CompositeSubscription();

    //todo: in future they should be taken from internal storage
    private Settings settings = new Settings();

    @InjectView(R.id.fab)
    android.support.design.widget.FloatingActionButton fab;

    @InjectView(R.id.toolbar_layout)
    android.support.design.widget.CollapsingToolbarLayout toolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                toggleUnit();
                compositeSubscription.add(asyncUpdateForecast());

            }
        });

        // dagger init
        ObjectGraph objectGraph = ObjectGraphSingleton.getInstance();
        objectGraph.inject(this);
        ButterKnife.inject(this);

        //begin of the custom code
        settings.setUnit(getString(R.string.unit_imperial));
        toggleUnit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void SnackBar(final View view, final String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    /**
     * https://github.com/ReactiveX/RxJava/wiki/The-RxJava-Android-Module
     */
    @Override
    public void onResume() {
        super.onResume();

        compositeSubscription.add(asyncUpdateForecast());

    }

    private void toggleUnit() {

        int temperatureId;
        int unitId;

        // TODO: we can optimize this if
        if (getCurrentUnits() == getString(R.string.unit_metric)){
            unitId = R.string.unit_imperial;
            temperatureId = R.drawable.temperature_fahrenheit;
        } else {
            unitId = R.string.unit_metric;
            temperatureId = R.drawable.temperature_celsius;
        }
        settings.setUnit(getString(unitId));
        if (fab!= null) {
            fab.setImageResource(temperatureId);
        }

    }

    /**
     * get the current unit from setting
     * @return
     */
    private String getCurrentUnits() {
        return settings.getUnit();
    }

    /**
     * get the current selected city
     * @return
     */
    private long getCurrentCityID() {
        //todo: for now is hardcoded but must be configurable
        return Environment.CORK_CITYID;
    }

    /**
     * format the temperature using the current selected unit
     * @param temp
     * @return
     */
    private String formatTemperature(long temp) {
        return String.format("%d%s", temp, getCurrentUnitsToDisplay());
    }

    /**
     * show degrees with C or F (depend by the settings)
     * @return
     */
    private String getCurrentUnitsToDisplay(){
        return (getCurrentUnits() == getString(R.string.unit_metric)) ? getString(R.string.degree_centigrads) : getString(R.string.degree_farenheit);
    }

    /**
     * display forecasts
     * @param forecast
     */
    private void updateForecast(final Forecast forecast) {
        if (forecast == null) return;
        if (forecast.getList() == null) return;

        // this value should be retrieved by another api call (current weather)
        final long temp = (long)forecast.getList().get(0).getMain().getTemp();

        // show the forecasts on screen
        final String title = String.format("%s %s", forecast.getCity().getName(), formatTemperature(temp));
        toolbarLayout.setTitle(title);
    }


    /**
     * show the progress bar
     */
    private void startProgress() {
        //todo: to be implemented
    }

    /**
     * hide the progress bar
     */
    private void endProgress() {
        //todo: to be implemented
    }

    /**
     * call the service, retrieve the results and draw the results
     * @return
     */
    private Subscription asyncUpdateForecast(){
        //todo the progress must me show only the first time, then we should use another thing
        startProgress();

        //http://api.openweathermap.org/data/2.5/forecast?id=2965140&appid=f32f1f5e85b0f12bdaeefcf83e1fbd7d
        //todo: this should be taken by the current gps location
        final String units = getCurrentUnits();
        final Observable<Forecast> observable = weatherService.getForecast(getCurrentCityID(), units);

        return observable

                .subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Forecast>() {
                    @Override
                    public void onCompleted() {
                        // todo hide the spinner
                        endProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        // cast to retrofit.HttpException to get the response code
                        if (e instanceof HttpException) {
                            HttpException response = (HttpException) e;
                            int code = response.code();
                            //TODO: add a toast
                            System.err.println(response.message());
                            //TODO: retry if it doesn't works
                        }
                    }

                    @Override
                    public void onNext(Forecast forecast) {
                        updateForecast(forecast);
                    }
                });
    }
}

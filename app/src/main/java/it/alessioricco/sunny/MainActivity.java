package it.alessioricco.sunny;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.ObjectGraph;
import it.alessioricco.sunny.injection.ObjectGraphSingleton;
import it.alessioricco.sunny.models.Forecast;
import it.alessioricco.sunny.models.ForecastItem;
import it.alessioricco.sunny.models.Settings;
import it.alessioricco.sunny.models.Wind;
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

    @InjectView(R.id.vertical_layout_temperatures)
    LinearLayout linearLayoutTemperatures;

    @InjectView(R.id.ivBigImage)
    ImageView toolbarImage;

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

        // the fab in not visible because is ugly.
        // was used to test the forecast update with another unit
        // when visible is a toggle button between imperial and metric units
        fab.setVisibility(View.GONE);

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
            startActivity(new Intent(this, SettingsActivity.class));
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
        //return settings.getUnit();
        return Settings.isMetric(getApplicationContext()) ? getString(R.string.unit_metric) : getString(R.string.unit_imperial);
    }

    /**
     * get the current selected city
     * @return
     */
//    private long getCurrentCityID() {
//        //todo: for now is hardcoded but must be configurable
//        //return Environment.CORK_CITYID;
//        Long cityId = Long.parseLong( Settings.getPreferredLocation(getApplicationContext()));
//        return cityId == null ? Environment.CORK_CITYID : cityId;
//    }

    /**
     * get the current selected city name
     * @return
     */
    private String getCurrentCityName() {
        //todo: for now is hardcoded but must be configurable
        //return Environment.CORK_CITYID;
        String cityName = Settings.getPreferredLocation(getApplicationContext());
        return cityName == null ? "Rome,it" : cityName;
    }

    /**
     * format the temperature using the current selected unit
     * @param temp
     * @return
     */
    private String formatTemperature(float temp) {
        return String.format("%.1f%s", temp, getCurrentUnitsToDisplay());
    }

    /**
     * show degrees with C or F (depend by the settings)
     * @return
     */
    private String getCurrentUnitsToDisplay(){
        return (getCurrentUnits() == getString(R.string.unit_metric)) ? getString(R.string.degree_centigrads) : getString(R.string.degree_farenheit);
    }

    /**
     * based on the current city id (or name+country)
     * it should find a good background image to display
     * todo: an API for that should be find
     * @param forecast
     * @return
     */
//    private String getCurrentCityBackground(final Forecast forecast) {
//        //https://api.teleport.org/api/urban_areas/slug:london/images/
//        //return "http://d2f0ora2gkri0g.cloudfront.net/bkpam2216982_cork-city.jpg";
//        return "https://d13k13wj6adfdf.cloudfront.net/urban_areas/london-12fdfd9fcf.jpg";
//    }


    /**
     * display forecasts
     * @param forecast
     */
    private void updateForecast(final Forecast forecast) {
        if (forecast == null) return;
        if (forecast.getList() == null) return;

        // this value should be retrieved by another api call (current weather)
        final float temp = forecast.getList().get(0).getMain().getTemp();
        final String tempWeather = forecast.getList().get(0).getWeather().get(0).getDescription();

        // show the forecasts on screen
        final String title = String.format("%s %s", forecast.getCity().getName(), formatTemperature(temp));
        toolbarLayout.setTitle(title);


        this.linearLayoutTemperatures.removeAllViews();

        // ADD HEADER
        {
            final LinearLayout container = (LinearLayout) getLayoutInflater().inflate(R.layout.temperature, null);

            final TextView temperature = (TextView) container.findViewById(R.id.temperature);
            if (temperature != null) {
                temperature.setText("Temp");
            }
            final TextView windSpeed = (TextView) container.findViewById(R.id.wind);
            if (windSpeed != null) {
                windSpeed.setText("Wind");
            }
            final TextView pressure = (TextView) container.findViewById(R.id.pressure);
            if (pressure != null) {
                pressure.setText("Pressure");
            }
            final TextView temperatureDate = (TextView) container.findViewById(R.id.temperature_date);
            if (temperatureDate != null) {
                temperatureDate.setText("Time");
            }
            final TextView temperatureDayMonth = (TextView) container.findViewById(R.id.temperature_day);
            if (temperatureDayMonth != null) {
                temperatureDayMonth.setText("Date");
            }
            this.linearLayoutTemperatures.addView(container);
        }

        // ADD ROWS
        boolean justOneTime = true;
        String currentDay = "";
        for(ForecastItem item: forecast.getList()) {
            final LinearLayout container = (LinearLayout)getLayoutInflater().inflate(R.layout.temperature, null);

            // TEMPERATURE
            final TextView temperature = (TextView) container.findViewById(R.id.temperature);
            if (temperature != null) {
                temperature.setText(String.format("%s", formatTemperature(item.getMain().getTemp())));
            }

            // WIND
            final TextView windSpeed = (TextView) container.findViewById(R.id.wind);
            if (windSpeed != null) {
                windSpeed.setText(String.format("%.1f m/s", item.getWind().getSpeed() ));
            }

            // Pressure
            final TextView pressure = (TextView) container.findViewById(R.id.pressure);
            if (pressure != null) {
                pressure.setText(String.format("%.1f", item.getMain().getPressure() ));
            }

            // HOUR
            final TextView temperatureDate = (TextView) container.findViewById(R.id.temperature_date);
            if (temperatureDate != null) {
                String tempDate = "";

                //SimpleDateFormat dt = new SimpleDateFormat("dd MMM hh:00");

                //HOUR
                SimpleDateFormat dt = new SimpleDateFormat("HH:00");
                Date date = new Date(item.getDt()*1000);
                tempDate = dt.format(date);
                temperatureDate.setText(String.format("%s",tempDate));

            }

            //DATE
            final TextView temperatureDayMonth = (TextView) container.findViewById(R.id.temperature_day);
            if (temperatureDayMonth != null) {
                String tempDate = "";

                //SimpleDateFormat dt = new SimpleDateFormat("dd MMM hh:00");

                SimpleDateFormat dt = new SimpleDateFormat("dd MMM");
                Date date = new Date(item.getDt()*1000);
                tempDate = dt.format(date);
                if (currentDay.equalsIgnoreCase(tempDate)) {
                    temperatureDayMonth.setVisibility(View.INVISIBLE);
                } else {
                    currentDay = tempDate;
                }
                temperatureDayMonth.setText(String.format("%s",tempDate));

            }

            // ICON
            final ImageView temperatureIcon = (ImageView) container.findViewById(R.id.temperature_icon);
            String url = String.format("%s%s.png", Environment.OPENWEATHER_IMG_URL, item.getWeather().get(0).getIcon());
            //TODO: use placeholders
            Picasso.with(getApplicationContext())
                    .load(url)
                    .into(temperatureIcon);

            if (justOneTime) {
                justOneTime = false;
                // It's cached
                Picasso.with(getApplicationContext())
                        .load(url)
                        .into(toolbarImage);
            }

            this.linearLayoutTemperatures.addView(container);
        }


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
        //final long city = getCurrentCityID();
        //final Observable<Forecast> observable = weatherService.getForecast(city, units);
        final String city = getCurrentCityName();
        final Observable<Forecast> observable = weatherService.getForecastByCity(city, units);
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

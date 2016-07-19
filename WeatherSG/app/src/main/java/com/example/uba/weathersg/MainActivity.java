package com.example.uba.weathersg;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.uba.weathersg.views.HeaderView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import connection.Callback;
import connection.DailyApi;
import connection.HourlyApi;
import connection.RequestTask;
import connection.Response;
import connection.Response.*;
import model.DailyForecast;
import model.HourlyForecast;
import model.WeatherForecast;
import model.WeatherConstants;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private HeaderView headerView;
    private Button morningButton;
    private Button afternoonButton;
    private Button nightButton;
    private TextView northTextView;
    private TextView westTextView;
    private TextView centralTextView;
    private TextView eastTextView;
    private TextView southTextView;
    private Button detailsButton;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        headerView = (HeaderView) findViewById(R.id.v_header_view_main);
        morningButton = (Button) findViewById(R.id.btn_morning_main);
        afternoonButton = (Button) findViewById(R.id.btn_afternoon_main);
        nightButton = (Button) findViewById(R.id.btn_night_main);
        northTextView = (TextView) findViewById(R.id.tv_north_main);
        westTextView = (TextView) findViewById(R.id.tv_west_main);
        centralTextView = (TextView) findViewById(R.id.tv_central_main);
        eastTextView = (TextView) findViewById(R.id.tv_east_main);
        southTextView = (TextView) findViewById(R.id.tv_south_main);
        detailsButton = (Button) findViewById(R.id.btn_details_main);

        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TwoHoursActivity.class);
                startActivity(intent);
            }
        });

        setTabButtonSelected(morningButton);
        morningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateZoneWeatherByTime(WeatherConstants.morn);
                setTabButtonSelected(morningButton);
            }
        });

        afternoonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateZoneWeatherByTime(WeatherConstants.afternoon);
                setTabButtonSelected(afternoonButton);
            }
        });

        nightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateZoneWeatherByTime(WeatherConstants.night);
                setTabButtonSelected(nightButton);
            }
        });

        if (isNetworkConnected()) {

            /**
             * TODO::Add Cache system to avoid extra network calls and check Validity Time of forecast to invalidate current forecast
             *
             */
            new HourlyApi(new Callback() {
                @Override
                public void onComplete() {
                    onHourlyAPIDone();
                }
            }).execute("http://www.nea.gov.sg/api/WebAPI/?dataset=2hr_nowcast&keyref=781CF461BB6606AD907750DFD1D076677BDD20C8534A2197");
            new DailyApi(new Callback() {
                @Override
                public void onComplete() {
                    onDailyAPIDone();
                }
            }).execute("http://www.nea.gov.sg/api/WebAPI/?dataset=24hrs_forecast&keyref=781CF461BB6606AD907750DFD1D076677BDD20C8534A2197");

            initializeGAPI();
            //currentLatitude = 1.317421f;
            //currentLongitude = 103.863158f;

        } else {
            Toast.makeText(this, WeatherConstants.internetMessage, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        //Toast.makeText(this, currentLatitude + currentLongitude + "", Toast.LENGTH_LONG).show();

    }

    public void initializeGAPI() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }





    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            setCurrentLocation();

            //Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    public void setCurrentLocation() {
        float shortestDistance = 0f;
        float results[] = new float[1];
        List<WeatherForecast> items = HourlyForecast.getInstance().getWeatherItems();
        WeatherForecast nearByForecast = null;
        if (items == null) {
            return;
        }
        for (WeatherForecast obj : items
                ) {

            Location.distanceBetween(currentLatitude, currentLongitude, obj.getLat(), obj.getlan(), results);
            if (shortestDistance == 0) {
                shortestDistance = results[0];
            }

            if (results[0] <= shortestDistance) {
                shortestDistance = results[0];
                nearByForecast = obj;
            }
            obj.setNearBy(false);
        }
        if (nearByForecast != null) {
            nearByForecast.setNearBy(true);
        }
    }

    boolean dailyResponse = false;
    boolean hourlyResponse = false;

    public void onDailyAPIDone() {
        dailyResponse = true;
        if (hourlyResponse) {
            updateUI();
        }
    }

    public void onHourlyAPIDone() {
        hourlyResponse = true;
        if (dailyResponse) {
            updateUI();
        }

    }

    public void updateUI() {
        setCurrentLocation();

        StringBuilder sb = new StringBuilder();
        sb.append(DailyForecast.getInstance().getValue(WeatherConstants.temperature_low));
        sb.append(" - ");
        sb.append(DailyForecast.getInstance().getValue(WeatherConstants.temperature_high));
        sb.append(" ");
        sb.append(DailyForecast.getInstance().getValue(WeatherConstants.temperature_unit));

        StringBuilder hm = new StringBuilder();
        hm.append(DailyForecast.getInstance().getValue(WeatherConstants.relativeHumidity_low));
        hm.append(" - ");
        hm.append(DailyForecast.getInstance().getValue(WeatherConstants.relativeHumidity_high));
        hm.append(" ");
        hm.append(DailyForecast.getInstance().getValue(WeatherConstants.relativeHumidity_unit));

        StringBuilder wb = new StringBuilder();
        wb.append("Wind Direction->");
        wb.append(DailyForecast.getInstance().getValue(WeatherConstants.wind_direction));
        wb.append(" ");
        wb.append(DailyForecast.getInstance().getValue(WeatherConstants.wind_speed));
        wb.append(" ");
        wb.append("km/h");

        headerView.setValues(sb.toString(), hm.toString(), wb.toString(), DailyForecast.getInstance().getValue(WeatherConstants.forecastIssue), DailyForecast.getInstance().getValue(WeatherConstants.validTime));
        headerView.setImage(this);
        updateZoneWeatherByTime(WeatherConstants.morn);


    }

    public void updateZoneWeatherByTime(String time) {

        HashMap<String, String> forecast = DailyForecast.getInstance().getMap(time);


        northTextView.setCompoundDrawablesWithIntrinsicBounds(null, getResource(forecast.get(WeatherConstants.wxnorth)), null, null);


        westTextView.setCompoundDrawablesWithIntrinsicBounds(null, getResource(forecast.get(WeatherConstants.wxwest)), null, null);


        centralTextView.setCompoundDrawablesWithIntrinsicBounds(null, getResource(forecast.get(WeatherConstants.wxcentral)), null, null);

        eastTextView.setCompoundDrawablesWithIntrinsicBounds(null, getResource(forecast.get(WeatherConstants.wxeast)), null, null);

        southTextView.setCompoundDrawablesWithIntrinsicBounds(null, getResource(forecast.get(WeatherConstants.wxsouth)), null, null);

    }

    private Drawable getResource(String img) {
        Resources resources = getApplicationContext().getResources();
        int id = resources.getIdentifier(img.toLowerCase(), "mipmap",
                getApplicationContext().getPackageName());
        return ContextCompat.getDrawable(this, id);

    }

    private void setTabButtonSelected(Button selected) {
        morningButton.setSelected(false);
        afternoonButton.setSelected(false);
        nightButton.setSelected(false);
        selected.setSelected(true);
    }

    /**
     * TODO:: should be moved in a Utility class
     * @return
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isNetworkConnected = cm.getActiveNetworkInfo() != null;
        return isNetworkConnected;
    }


}

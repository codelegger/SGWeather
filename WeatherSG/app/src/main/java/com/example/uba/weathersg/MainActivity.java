package com.example.uba.weathersg;

import android.content.IntentSender;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import connection.RequestTask;
import connection.Response;
import connection.Response.*;
import model.DailyForecast;
import model.HourlyForecast;
import model.WeatherForecast;
import model.Weather_const;

import static connection.Response.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    HourlyForecast hourlyForecast = new HourlyForecast();
    DailyForecast dailyForecast = new DailyForecast();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    public String currentLocation = "" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new HourlyApi().execute("http://www.nea.gov.sg/api/WebAPI/?dataset=2hr_nowcast&keyref=781CF461BB6606AD907750DFD1D076677BDD20C8534A2197");
        //new DailyApi().execute("http://www.nea.gov.sg/api/WebAPI/?dataset=24hrs_forecast&keyref=781CF461BB6606AD907750DFD1D076677BDD20C8534A2197");

        //initializeGAPI();
        currentLatitude =1.317421f;
        currentLongitude =  103.863158f;

        setCurrentLocation();
        System.out.print("curent location    " +currentLocation );
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();

    }

    public class HourlyApi extends RequestTask {
        int loading ;
        protected void onPreExecute() {
            loading = 0;
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Response output) {
            super.onPostExecute(output);
            // Update widgets
//        AbstractWidgetProvider.updateWidgets(MainActivity.this);
//        DashClockWeatherExtension.updateDashClock(MainActivity.this);
        }



        @Override
        protected String getAPIName() {
            return "2Hour - weather";
        }

        @Override
        protected void updateMainUI() {
            //updateTodayWeatherUI();
            //updateLastUpdateTime();
        }

        @Override
        protected Response.ParseResult parseResponse(String response) {
            return parseHourlyJson(response);
        }
    }

    public void initializeGAPI()
    {
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


    public class DailyApi extends RequestTask {
        int loading ;
        @Override
        protected void onPreExecute() {
            loading = 0;
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Response output) {
            super.onPostExecute(output);
            // Update widgets
//        AbstractWidgetProvider.updateWidgets(MainActivity.this);
//        DashClockWeatherExtension.updateDashClock(MainActivity.this);
        }



        @Override
        protected String getAPIName() {
            return "2Hour - weather";
        }

        @Override
        protected void updateMainUI() {
            //updateTodayWeatherUI();
            //updateLastUpdateTime();
        }

        @Override
        protected Response.ParseResult parseResponse(String response) {
            return parseDailyJson(response);
        }
    }



    private ParseResult parseHourlyJson(String result) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(result));
            int eventType = xpp.getEventType();
            boolean validTime = false;
            boolean item;
            boolean startDocument = true;
            String text = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = xpp.getName();

                switch (eventType)
                {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(tagname.equals("validTime"))
                        {
                            hourlyForecast.setValidTime(text);
                        }
                        else if(tagname.equals("area"))
                        {
                            String forecast = xpp.getAttributeValue(null, "forecast");
                            String lat = xpp.getAttributeValue(null, "lat");
                            String lon = xpp.getAttributeValue(null, "lon");
                            String name = xpp.getAttributeValue(null, "name");

                            System.out.println("Forecast  " + name);
                            WeatherForecast obj = new WeatherForecast(name, forecast, lat, lon);
                            hourlyForecast.addWeatherItem(obj);
                        }
                        break;
                }

                eventType = xpp.next();
            }
            System.out.println("End document");



        } catch (XmlPullParserException e) {
            e.printStackTrace();
           return ParseResult.DATA_EXCEPTION;
        } catch (IOException e) {
            e.printStackTrace();
            return ParseResult.DATA_EXCEPTION;

        }

        return ParseResult.OK;
    }




    private ParseResult parseDailyJson(String result) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(result));
            int eventType = xpp.getEventType();
            boolean validTime = false;
            boolean item;
            boolean startDocument = true;
            String text = "";
            HashMap<String,String> forecast = new HashMap<String,String>();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = xpp.getName();
                switch (eventType)
                {
                    case XmlPullParser.START_TAG:
                        if(tagname.equals(Weather_const.morn) || tagname.equals(Weather_const.afternoon)
                                || tagname.equals(Weather_const.night))
                        {
                            forecast = new HashMap<String,String>();
                        }

                        break;
                    case XmlPullParser.TEXT:
                        text = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(tagname.equals(Weather_const.validTime))
                        {
                            dailyForecast.set(Weather_const.validTime,text);
                        }
                        else if(tagname.equals(Weather_const.forecastIssue))
                        {
                            dailyForecast.set(Weather_const.forecastIssue,text);
                        }
                        else if(tagname.equals(Weather_const.temperature))
                        {
                            String high = xpp.getAttributeValue(null, "high");
                            String low = xpp.getAttributeValue(null, "low");
                            String unit = xpp.getAttributeValue(null, "unit");
                            dailyForecast.set(Weather_const.temperature_high,high);
                            dailyForecast.set(Weather_const.temperature_low,low);
                            dailyForecast.set(Weather_const.temperature_unit,unit);
                        }
                        else if(tagname.equals(Weather_const.relativeHumidity))
                        {
                            String high = xpp.getAttributeValue(null, "high");
                            String low = xpp.getAttributeValue(null, "low");
                            String unit = xpp.getAttributeValue(null, "unit");
                            dailyForecast.set(Weather_const.relativeHumidity_high,high);
                            dailyForecast.set(Weather_const.relativeHumidity_low,low);
                            dailyForecast.set(Weather_const.relativeHumidity_unit,unit);
                        }
                        else if(tagname.equals(Weather_const.wind))
                        {
                            String direction = xpp.getAttributeValue(null, "direction");
                            String speed = xpp.getAttributeValue(null, "speed");
                            dailyForecast.set(Weather_const.wind_direction,direction);
                            dailyForecast.set(Weather_const.wind_speed,speed);
                        }
                        else if(tagname.equals(Weather_const.timePeriod))
                        {
                            forecast.put(Weather_const.timePeriod , text);
                        }
                        else if(tagname.equals(Weather_const.wxeast))
                        {
                            forecast.put(Weather_const.wxeast , text);
                        }
                        else if(tagname.equals(Weather_const.wxcentral))
                        {
                            forecast.put(Weather_const.wxcentral , text);
                        }
                        else if(tagname.equals(Weather_const.wxwest))
                        {
                            forecast.put(Weather_const.wxwest , text);
                        }
                        else if(tagname.equals(Weather_const.wxnorth))
                        {
                            forecast.put(Weather_const.wxnorth , text);
                        }
                        else if(tagname.equals(Weather_const.wxsouth))
                        {
                            forecast.put(Weather_const.wxsouth , text);
                        }
                        if(tagname.equals(Weather_const.morn) || tagname.equals(Weather_const.afternoon)
                                || tagname.equals(Weather_const.night))
                        {
                            dailyForecast.set(tagname,forecast);
                        }
                        break;
                }

                eventType = xpp.next();
            }
            System.out.println("End document");



        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return ParseResult.DATA_EXCEPTION;
        } catch (IOException e) {
            e.printStackTrace();
            return ParseResult.DATA_EXCEPTION;

        }

        return ParseResult.OK;
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
            LocationServices.FusedLocationApi.removeLocationUpdates (mGoogleApiClient,this);

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

            Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
        }
    }




    @Override
    public void onConnectionSuspended(int i) {}

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

    public void setCurrentLocation()
    {
        if(hourlyForecast!=null)
        {
            float shortestDistance = 0f ;
            float results[] = new float[1];
            List<WeatherForecast> items  = hourlyForecast.getWeatherItems();
            for (WeatherForecast obj:items
                 ) {

                Location.distanceBetween(currentLatitude,currentLongitude, obj.getLat(),obj.getlan(),results);
                if(results[0] >= shortestDistance)
                {
                    shortestDistance = results[0];
                    currentLocation = obj.getName();
                }
            }
        }
    }


}

package com.example.uba.weathersg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.example.uba.weathersg.views.HeaderView;

import java.util.ArrayList;

import model.DailyForecast;
import model.HourlyForecast;
import model.WeatherConstants;
import model.WeatherForecast;

public class TwoHoursActivity extends AppCompatActivity {

    private HeaderView headerView;
    private RecyclerView recyclerView;
    private TwoHoursAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_hours);

        headerView = (HeaderView) findViewById(R.id.v_header_view_two_hours);
        recyclerView = (RecyclerView) findViewById(R.id.rv_forecast_two_hours);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        updateUI();
//        ArrayList<WeatherForecast> weatherForecasts = new ArrayList<>();
//        WeatherForecast wf = new WeatherForecast("Ang Mo Kio", "bR", "2.0", "2.0");
//        WeatherForecast wf2 = new WeatherForecast("Boon Keng", "CL", "2.0", "2.0");
//        WeatherForecast wf3 = new WeatherForecast("Ang Mo Kio", "br", "2.0", "2.0");
//        weatherForecasts.add(wf);
//        weatherForecasts.add(wf2);
//        weatherForecasts.add(wf3);

        adapter = new TwoHoursAdapter(this, HourlyForecast.getInstance().getWeatherItems());//TODO: pass the list
        recyclerView.setAdapter(adapter);
    }
    public void updateUI()
    {
       // setCurrentLocation();

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

        headerView.setValues(sb.toString(),hm.toString() ,wb.toString(),DailyForecast.getInstance().getValue(WeatherConstants.forecastIssue) ,HourlyForecast.getInstance().getValidTime());
        headerView.setImage(this);


    }

}

package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uba on 15/7/16.
 */

public class HourlyForecast {
    private String validTime;




    private List<WeatherForecast> weatherItems ;

    public HourlyForecast()
    {
        weatherItems = new ArrayList<WeatherForecast>();
    }

    public List<WeatherForecast> getWeatherItems()
    {
        return weatherItems;
    }

    public void addWeatherItem(WeatherForecast w)
    {
        weatherItems.add(w);
    }

    public void setValidTime(String validTime)
    {
        this.validTime = validTime;
    }


}

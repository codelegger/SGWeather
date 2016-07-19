package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uba on 15/7/16.
 */

public class HourlyForecast {
    private String validTime;

    private static HourlyForecast _instance = null ;

    public static HourlyForecast getInstance()
    {
        if(_instance == null)
        {
            _instance = new HourlyForecast();
        }
        return _instance;
    }

    private List<WeatherForecast> weatherItems;

   private HourlyForecast() {
        weatherItems = new ArrayList<WeatherForecast>();
    }

    public List<WeatherForecast> getWeatherItems() {
        return weatherItems;
    }

    public void addWeatherItem(WeatherForecast w) {
        weatherItems.add(w);
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }
    public String getValidTime()
    {
        return validTime;
    }


}

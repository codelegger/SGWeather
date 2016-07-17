package model;

/**
 * Created by uba on 15/7/16.
 */
public class WeatherForecast {
    private String name;
    private String fr_sym;
    private float lat;
    private float lan;

    public WeatherForecast(String name , String w_symbol ,  String lat , String lan )
    {
        this.name = name ;
        this.fr_sym = w_symbol;
        this.lan = Float.parseFloat(lan);
        this.lat = Float.parseFloat(lat);
    }
    public float getLat(){
        return  lat;
    }
    public float getlan(){
        return lan;
    }
    public String getName() {
        return name;
    }



}

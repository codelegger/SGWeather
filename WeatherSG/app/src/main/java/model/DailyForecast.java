package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by uba on 16/7/16.
 */
public class DailyForecast {


    private static DailyForecast _instance = null;

    public static DailyForecast getInstance() {
        if (_instance == null) {
            _instance = new DailyForecast();
        }
        return _instance;
    }

    HashMap<String, Object> weatherDetail;

    private DailyForecast() {
        weatherDetail = new HashMap<String, Object>();
    }

    public void set(String k, Object v) {
        weatherDetail.put(k, v);
    }

    public String getValue(String key) {
        String s = (String) weatherDetail.get(key);
        if (s != null) {
            return s;
        }
        return "Not Available";
    }


    public HashMap<String, String> getMap(String key) {

        HashMap<String, String> valuesMap = (HashMap<String, String>) weatherDetail.get(key);
        return valuesMap;
    }
}


/**
 * <title>24 Hour Forecast</title>
 * <forecastIssue date="16-07-2016" time="05:35 PM"/>
 * <validTime>6 PM 16 Jul - 6 PM 17 Jul</validTime>
 * <temperature high="33" low="25" unit="Degrees Celsius"/>
 * <relativeHumidity high="95" low="55" unit="Percentage"/>
 * <wind direction="SW" speed="10 - 20"/>
 * <wxmain>TL</wxmain>
 * <forecast>
 * Partly cloudy overnight. For tomorrow, thundery showers mainly over northern, eastern and western Singapore in the late morning and early afternoon.
 * </forecast>
 * <pastweather>Nil</pastweather>
 * </main>
 * <night>
 * <timePeriod>6 pm 16 Jul to 6 am 17 Jul</timePeriod>
 * <wxeast>PN</wxeast>
 * <wxwest>PN</wxwest>
 * <wxnorth>PN</wxnorth>
 * <wxsouth>PN</wxsouth>
 * <wxcentral>PN</wxcentral>
 */

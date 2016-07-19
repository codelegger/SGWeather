package connection;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

import model.DailyForecast;
import model.WeatherConstants;

/**
 * Created by uba on 18/7/16.
 */
public class DailyApi extends RequestTask {
    int loading;
    Callback callback ;

    public DailyApi(Callback callback)
    {
        this.callback = callback ;
    }

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
        this.callback.onComplete();
        //updateTodayWeatherUI();
        //updateLastUpdateTime();
    }

    @Override
    protected Response.ParseResult parseResponse(String response) {
        return parseDailyJson(response);
    }
    private Response.ParseResult parseDailyJson(String result) {
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
            HashMap<String, String> forecast = new HashMap<String, String>();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equals(WeatherConstants.morn) || tagname.equals(WeatherConstants.afternoon)
                                || tagname.equals(WeatherConstants.night)) {
                            forecast = new HashMap<String, String>();
                        }

                        break;
                    case XmlPullParser.TEXT:
                        text = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagname.equals(WeatherConstants.validTime)) {
                            DailyForecast.getInstance().set(WeatherConstants.validTime, text);
                        } else if (tagname.equals(WeatherConstants.forecastIssue)) {
                            DailyForecast.getInstance().set(WeatherConstants.forecastIssue, text);
                        } else if (tagname.equals(WeatherConstants.temperature)) {
                            String high = xpp.getAttributeValue(null, "high");
                            String low = xpp.getAttributeValue(null, "low");
                            String unit = xpp.getAttributeValue(null, "unit");
                            DailyForecast.getInstance().set(WeatherConstants.temperature_high, high);
                            DailyForecast.getInstance().set(WeatherConstants.temperature_low, low);
                            DailyForecast.getInstance().set(WeatherConstants.temperature_unit, unit);
                        } else if (tagname.equals(WeatherConstants.relativeHumidity)) {
                            String high = xpp.getAttributeValue(null, "high");
                            String low = xpp.getAttributeValue(null, "low");
                            String unit = xpp.getAttributeValue(null, "unit");
                            DailyForecast.getInstance().set(WeatherConstants.relativeHumidity_high, high);
                            DailyForecast.getInstance().set(WeatherConstants.relativeHumidity_low, low);
                            DailyForecast.getInstance().set(WeatherConstants.relativeHumidity_unit, unit);
                        } else if (tagname.equals(WeatherConstants.wind)) {
                            String direction = xpp.getAttributeValue(null, "direction");
                            String speed = xpp.getAttributeValue(null, "speed");
                            DailyForecast.getInstance().set(WeatherConstants.wind_direction, direction);
                            DailyForecast.getInstance().set(WeatherConstants.wind_speed, speed);
                        } else if (tagname.equals(WeatherConstants.timePeriod)) {
                            forecast.put(WeatherConstants.timePeriod, text);
                        } else if (tagname.equals(WeatherConstants.wxeast)) {
                            forecast.put(WeatherConstants.wxeast, text);
                        } else if (tagname.equals(WeatherConstants.wxcentral)) {
                            forecast.put(WeatherConstants.wxcentral, text);
                        } else if (tagname.equals(WeatherConstants.wxwest)) {
                            forecast.put(WeatherConstants.wxwest, text);
                        } else if (tagname.equals(WeatherConstants.wxnorth)) {
                            forecast.put(WeatherConstants.wxnorth, text);
                        } else if (tagname.equals(WeatherConstants.wxsouth)) {
                            forecast.put(WeatherConstants.wxsouth, text);
                        }
                        else if (tagname.equals(WeatherConstants.wxmain)) {
                            DailyForecast.getInstance().set(WeatherConstants.wxmain, text);
                        }
                        if (tagname.equals(WeatherConstants.morn) || tagname.equals(WeatherConstants.afternoon)
                                || tagname.equals(WeatherConstants.night)) {
                            DailyForecast.getInstance().set(tagname, forecast);
                        }
                        break;
                }

                eventType = xpp.next();
            }
            System.out.println("End document");


        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return Response.ParseResult.DATA_EXCEPTION;
        } catch (IOException e) {
            e.printStackTrace();
            return Response.ParseResult.DATA_EXCEPTION;

        }

        return Response.ParseResult.OK;
    }
}

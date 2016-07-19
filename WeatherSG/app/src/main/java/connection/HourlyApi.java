package connection;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

import model.HourlyForecast;
import model.WeatherForecast;

/**
 * Created by uba on 18/7/16.
 */
public class HourlyApi extends RequestTask {
    int loading;
    Callback callback = null ;
    protected void onPreExecute() {
        loading = 0;
        super.onPreExecute();
    }

    public HourlyApi(Callback callback)
    {
        this.callback = callback ;
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
        return parseHourlyJson(response);
    }
    private Response.ParseResult parseHourlyJson(String result) {
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

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagname.equals("validTime")) {
                            HourlyForecast.getInstance().setValidTime(text);
                        } else if (tagname.equals("area")) {
                            String forecast = xpp.getAttributeValue(null, "forecast");
                            String lat = xpp.getAttributeValue(null, "lat");
                            String lon = xpp.getAttributeValue(null, "lon");
                            String name = xpp.getAttributeValue(null, "name");

                            System.out.println("Forecast  " + name);
                            WeatherForecast obj = new WeatherForecast(name, forecast, lat, lon);
                            HourlyForecast.getInstance().addWeatherItem(obj);
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

package com.example.uba.weathersg.views;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uba.weathersg.R;

import org.w3c.dom.Text;

import model.DailyForecast;
import model.WeatherConstants;

/**
 * Created by uba on 18/7/16.
 */
public class HeaderView extends LinearLayout {

    private TextView titleTextView;
    private TextView tempTextView;
    private TextView humidityTextView;
    private TextView windTextView;
    private TextView forecastTextView;
    private ImageView weatherIcon;
    private TextView validTextView;

    public HeaderView(Context context) {
        super(context);
        init();
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_header, this, true);

        titleTextView = (TextView) findViewById(R.id.tv_title_header);
        tempTextView = (TextView) findViewById(R.id.tv_temp_header);
        humidityTextView = (TextView) findViewById(R.id.tv_humidity_header);
        windTextView = (TextView) findViewById(R.id.tv_wind_header);
        forecastTextView = (TextView) findViewById(R.id.tv_forcast_header);
        weatherIcon = (ImageView) findViewById(R.id.img_header);
        validTextView = (TextView)findViewById(R.id.tv_validTimeheader);

    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public void setTitleTextView(TextView titleTextView) {
        this.titleTextView = titleTextView;
    }

    public TextView getTempTextView() {
        return tempTextView;
    }

    public void setTempTextView(TextView tempTextView) {
        this.tempTextView = tempTextView;
    }

    public TextView getHumidityTextView() {
        return humidityTextView;
    }

    public void setHumidityTextView(TextView humidityTextView) {
        this.humidityTextView = humidityTextView;
    }

    public TextView getWindTextView() {
        return windTextView;
    }

    public void setWindTextView(TextView windTextView) {
        this.windTextView = windTextView;
    }

    public TextView getForecastTextView() {
        return forecastTextView;
    }

    public void setForecastTextView(TextView forecastTextView) {
        this.forecastTextView = forecastTextView;
    }

    public void setValues(String temp, String humidity, String wind, String forecast,String validTime) {
        tempTextView.setText(temp);
        humidity = WeatherConstants.humidityLabel +  humidity;
        humidityTextView.setText(humidity);
        windTextView.setText(wind);
        forecastTextView.setText(forecast);
        validTextView.setText(validTime);
    }


    public void setImage(Context ctx){
        Resources resources = ctx.getResources();
        int wxmainId = resources.getIdentifier(DailyForecast.getInstance().getValue(WeatherConstants.wxmain).toLowerCase(), "mipmap",
                ctx.getPackageName());
        weatherIcon.setImageResource(wxmainId);
    }
}

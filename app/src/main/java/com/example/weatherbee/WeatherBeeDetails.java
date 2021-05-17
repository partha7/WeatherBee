package com.example.weatherbee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class WeatherBeeDetails extends AppCompatActivity {
    TextView tv_weather_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_bee_details);

        tv_weather_data = (TextView) findViewById(R.id.tv_weather_details);

        Intent startOfThisActivity = getIntent();
        if(startOfThisActivity != null){
            if(startOfThisActivity.hasExtra(Intent.EXTRA_TEXT)){
                String forecast = startOfThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                tv_weather_data.setText(forecast);
            }
        }
    }
}
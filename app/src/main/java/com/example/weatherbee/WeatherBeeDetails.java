package com.example.weatherbee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class WeatherBeeDetails extends AppCompatActivity {
    TextView tv_weather_data;
    String forecast;
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_bee_details);

        tv_weather_data = (TextView) findViewById(R.id.tv_weather_details);

        Intent startOfThisActivity = getIntent();
        if(startOfThisActivity != null){
            if(startOfThisActivity.hasExtra(Intent.EXTRA_TEXT)){
                forecast = startOfThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                tv_weather_data.setText(forecast);
            }
        }
    }
    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(forecast + FORECAST_SHARE_HASHTAG)
                .getIntent();
        return shareIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather_details, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareForecastIntent());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // COMPLETED (7) Launch SettingsActivity when the Settings option is clicked
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

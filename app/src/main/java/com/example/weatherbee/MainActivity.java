package com.example.weatherbee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherbee.utils.GetResponseToDisplay;
import com.example.weatherbee.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements WeatherBeeAdapter.WeatherBeeOnClickListener, LoaderManager.LoaderCallbacks<String[]> {

    TextView tv_error_message;
    RecyclerView rv_weather;
    ProgressBar pb_loading_indicator;
    String location = "94043,USA";
    WeatherBeeAdapter weatherBeeAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherBeeAdapter = new WeatherBeeAdapter(this);

        rv_weather = (RecyclerView) findViewById(R.id.rv_weather_data);
        tv_error_message = (TextView) findViewById(R.id.tv_error_message);
        pb_loading_indicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_weather.setLayoutManager(linearLayoutManager);
        rv_weather.setHasFixedSize(true);
        rv_weather.setAdapter(weatherBeeAdapter);
        getWeatherData(location);

    }
    public void showWeatherData(){
        rv_weather.setVisibility(View.VISIBLE);
        tv_error_message.setVisibility(View.INVISIBLE);
    }

    public void showErrorMessage(){
        rv_weather.setVisibility(View.INVISIBLE);
        tv_error_message.setVisibility(View.VISIBLE);
    }

    public void getWeatherData(String location) {
     new DownloadWeatherData().execute(location);
    }

    @Override
    public void onClick(String weatherForTheDay) {
        Context context = this;
        Intent intentToStartDetailsActivity = new Intent(context, WeatherBeeDetails.class);
        intentToStartDetailsActivity.putExtra(Intent.EXTRA_TEXT, weatherForTheDay);

//        Toast.makeText(context, weatherForTheDay, Toast.LENGTH_SHORT).show();
        startActivity(intentToStartDetailsActivity);
    }

    @NonNull
    @Override
    public Loader<String[]> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<String[]>(this) {


            @Nullable
            @Override
            public String[] loadInBackground() {
                return new String[0];
            }
        };

    }

    @Override
    public void onLoadFinished(@NonNull Loader<String[]> loader, String[] data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String[]> loader) {

    }

    public class DownloadWeatherData extends AsyncTask<String, Void, String[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb_loading_indicator.setVisibility(View.VISIBLE);
        }


        @Override
        protected String[] doInBackground(String... strings) {
            URL url = NetworkUtils.buildUrl(strings[0]);
            String response = null;
            try {
                response = NetworkUtils.getAPIResponse(url);
//                System.out.println("Response "+ response);
                String[] simpleWeatherStringsFromJson = GetResponseToDisplay.getWeatherDetailsToDisplay(MainActivity.this, response);
                return simpleWeatherStringsFromJson;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String weatherdata[]) {
            pb_loading_indicator.setVisibility(View.INVISIBLE);
            if (weatherdata != null) {

                showWeatherData();

                weatherBeeAdapter.setWeatherData(weatherdata);

//                for (String weatherString : s) {
//                    tv_weather.append((weatherString) + "\n\n\n");\

//            }
            }
            else{
                showErrorMessage();
            }

        }
    }

    private void openLocationInMap() {
        String addressString = "1600 Ampitheatre Parkway, CA";
        Uri geoLocation = Uri.parse("geo:0,0?q=" + addressString);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            System.out.println( "Couldn't call " + geoLocation.toString()
                    + ", no receiving apps installed!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.weathermenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        return super.onOptionsItemSelected(item);
        int id = item.getItemId();

        if(id == R.id.action_refresh){
//            tv_weather.setText("");
            weatherBeeAdapter.setWeatherData(null);
            getWeatherData(location);
            return true;
        }
        else if(id == R.id.action_map){
            openLocationInMap();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


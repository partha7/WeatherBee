package com.example.weatherbee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherbee.utils.GetResponseToDisplay;
import com.example.weatherbee.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements WeatherBeeAdapter.WeatherBeeOnClickListener, LoaderManager.LoaderCallbacks<String[]>, SharedPreferences.OnSharedPreferenceChangeListener {

    TextView tv_error_message;
    RecyclerView rv_weather;
    ProgressBar pb_loading_indicator;
    String location;
    WeatherBeeAdapter weatherBeeAdapter;
    private static final int FORECAST_LOADER_ID = 0;
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;


    @Override
    protected void onStart() {
        super.onStart();
        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            System.out.println("onStart: preferences were updated");
            getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, null, this);
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

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
//        getWeatherData(location);
        int loaderId = FORECAST_LOADER_ID;

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        LoaderManager.LoaderCallbacks<String[]> callback = MainActivity.this;

        Bundle bundleForLoader = null;

        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);

    }
    public void showWeatherData(){
        rv_weather.setVisibility(View.VISIBLE);
        tv_error_message.setVisibility(View.INVISIBLE);
    }

    public void showErrorMessage(){
        rv_weather.setVisibility(View.INVISIBLE);
        tv_error_message.setVisibility(View.VISIBLE);
    }
//
//    public void getWeatherData(String location) {
//     new DownloadWeatherData().execute(location);
//    }

    @Override
    public void onClick(String weatherForTheDay) {
        Context context = this;
        Intent intentToStartDetailsActivity = new Intent(context, WeatherBeeDetails.class);
        intentToStartDetailsActivity.putExtra(Intent.EXTRA_TEXT, weatherForTheDay);

//        Toast.makeText(context, weatherForTheDay, Toast.LENGTH_SHORT).show();
        startActivity(intentToStartDetailsActivity);
    }

    public static String getPreferredWeatherLocation(Context context) {
        // COMPLETED (1) Return the user's preferred location
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForLocation = context.getString(R.string.pref_location_key);
        String defaultLocation = context.getString(R.string.pref_location_default);
        return prefs.getString(keyForLocation, defaultLocation);
    }

    public static boolean isMetric(Context context) {
        // COMPLETED (2) Return true if the user's preference for units is metric, false otherwise
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForUnits = context.getString(R.string.pref_units_key);
        String defaultUnits = context.getString(R.string.pref_units_metric);
        String preferredUnits = prefs.getString(keyForUnits, defaultUnits);
        String metric = context.getString(R.string.pref_units_metric);
        boolean userPrefersMetric;
        if (metric.equals(preferredUnits)) {
            userPrefersMetric = true;
        } else {
            userPrefersMetric = false;
        }
        return userPrefersMetric;
    }

    @NonNull
    @Override
    public Loader<String[]> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<String[]>(this) {
            String[] mWeatherData = null;


            @Override
            protected void onStartLoading() {
                if (mWeatherData != null) {
                    deliverResult(mWeatherData);
                } else {
                    pb_loading_indicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }


            @Nullable
            @Override
            public String[] loadInBackground() {
                location = getPreferredWeatherLocation(getApplicationContext());

                URL url = NetworkUtils.buildUrl(location);
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
        };
    }


            @Override
            public void onLoadFinished(@NonNull Loader<String[]> loader, String[] data) {
                pb_loading_indicator.setVisibility(View.INVISIBLE);
                weatherBeeAdapter.setWeatherData(data);
                if (null == data) {
                    showErrorMessage();
                } else {
                    showWeatherData();
                }

            }

            @Override
            public void onLoaderReset(@NonNull Loader<String[]> loader) {

            }
    private void invalidateData() {
        weatherBeeAdapter.setWeatherData(null);
    }


//    public class DownloadWeatherData extends AsyncTask<String, Void, String[]> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pb_loading_indicator.setVisibility(View.VISIBLE);
//        }
//
//
//        @Override
//        protected String[] doInBackground(String... strings) {
//            URL url = NetworkUtils.buildUrl(strings[0]);
//            String response = null;
//            try {
//                response = NetworkUtils.getAPIResponse(url);
////                System.out.println("Response "+ response);
//                String[] simpleWeatherStringsFromJson = GetResponseToDisplay.getWeatherDetailsToDisplay(MainActivity.this, response);
//                return simpleWeatherStringsFromJson;
//
//            } catch (IOException | JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String weatherdata[]) {
//            pb_loading_indicator.setVisibility(View.INVISIBLE);
//            if (weatherdata != null) {
//
//                showWeatherData();
//
//                weatherBeeAdapter.setWeatherData(weatherdata);
//
////                for (String weatherString : s) {
////                    tv_weather.append((weatherString) + "\n\n\n");\
//
////            }
//            }
//            else{
//                showErrorMessage();
//            }
//
//        }
//    }

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
            invalidateData();
            getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, null, this);
            return true;
        }
        else if(id == R.id.action_map){
            openLocationInMap();
            return true;
        }
        else if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;
    }
}


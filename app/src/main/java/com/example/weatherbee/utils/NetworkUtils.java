package com.example.weatherbee.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {

    private static final String TAG = "NetworkUtils";
    private static final String format = "json";
    private static final String units = "metric";
    private static final int numDays = 14;


    public static URL buildUrl(String locationQuery) {

        Uri builtUri = Uri.parse(Constants.getBaseUrl()).buildUpon()
                .appendQueryParameter(Constants.getQueryParam(), locationQuery)
                .appendQueryParameter(Constants.getFormatParam(), format)
                .appendQueryParameter(Constants.getUnitsParam(), units)
                .appendQueryParameter(Constants.getDaysParam(), Integer.toString(numDays))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getAPIResponse(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        System.out.println("URL+"+ url);

        try{
            InputStream in = urlConnection.getInputStream();

            Scanner sc = new Scanner(in);
            sc.useDelimiter("\\A");

            boolean hasInput = sc.hasNext();

            if(hasInput){
                return sc.next();

            }
            else{
                return null;

            }

        }
        finally {
            urlConnection.disconnect();
        }


    }

}

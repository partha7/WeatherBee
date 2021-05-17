package com.example.weatherbee.utils;

import android.content.Context;

import com.example.weatherbee.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetResponseToDisplay {

    public static String[] getWeatherDetailsToDisplay(Context context, String weatherJsonStr)
            throws JSONException {

        final String LIST = "list";
        final String TEMPERATURE = "temp";
        final String MAX_TEMP = "max";
        final String MIN_TEMP = "min";
        final String WEATHER = "weather";
        final String DESCRIPTION = "main";
//
//        final String OWM_MESSAGE_CODE = "cod";

        String[] parsedWeatherData = null;

        JSONObject weatherJson = new JSONObject(weatherJsonStr);

        JSONArray weatherArray = weatherJson.getJSONArray(LIST);

        parsedWeatherData = new String[weatherArray.length()];

        long startDay = DateUtilities.getStartDate();

        for (int i = 0; i < weatherArray.length(); i++) {
            String date;
            String highAndLow;
            long dateTimeMillis;
            double high;
            double low;
            String description;

            JSONObject weatherPerDay = weatherArray.getJSONObject(i);

            /*
             * We ignore all the datetime values embedded in the JSON and assume that
             * the values are returned in-order by day (which is not guaranteed to be correct).
             */
            dateTimeMillis = startDay + DateUtilities.DAY_IN_MILLIS * i;
            date = DateUtilities.getDateString(context, dateTimeMillis, false);

            /*
             * Description is in a child array called "weather", which is 1 element long.
             * That element also contains a weather code.
             */
            JSONObject weatherObject =
                    weatherPerDay.getJSONArray(WEATHER).getJSONObject(0);
            description = weatherObject.getString(DESCRIPTION);

            /*
             * Temperatures are sent by Open Weather Map in a child object called "temp".
             *
             * Editor's Note: Try not to name variables "temp" when working with temperature.
             * It confuses everybody. Temp could easily mean any number of things, including
             * temperature, temporary and is just a bad variable name.
             */
            JSONObject temperatureObject = weatherPerDay.getJSONObject(TEMPERATURE);
            high = temperatureObject.getDouble(MAX_TEMP);
            low = temperatureObject.getDouble(MIN_TEMP);
            highAndLow = formatHighLows(context, high, low);

            parsedWeatherData[i] = date + " - " + description + " - " + highAndLow;
        }

        return parsedWeatherData;
    }

    public static String formatHighLows(Context context, double high, double low) {
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String formattedHigh = formatTemperature(context, roundedHigh);
        String formattedLow = formatTemperature(context, roundedLow);

        String highLowStr = formattedHigh + " / " + formattedLow;
        return highLowStr;
    }

    public static String formatTemperature(Context context, double temperature) {
        int temperatureFormatResourceId = R.string.format_temperature_celsius;

        if (!isMetric(context)) {
            temperature = celsiusToFahrenheit(temperature);
            temperatureFormatResourceId = R.string.format_temperature_fahrenheit;

        }

        /* For presentation, assume the user doesn't care about tenths of a degree. */
        return String.format(context.getString(temperatureFormatResourceId), temperature);
    }
    public static boolean isMetric(Context context) {
        /** This will be implemented in a future lesson **/
        return true;
    }

    private static double celsiusToFahrenheit(double temperatureInCelsius) {
        double temperatureInFahrenheit = (temperatureInCelsius * 1.8) + 32;
        return temperatureInFahrenheit;
    }


}

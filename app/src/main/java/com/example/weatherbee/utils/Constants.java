package com.example.weatherbee.utils;

public class Constants {
    private static final String WEATHER_BASE_URL = "https://andfun-weather.udacity.com/staticweather";
    private static final String QUERY_PARAM = "q";
    private final static String FORMAT_PARAM = "mode";
    private final static String UNITS_PARAM = "units";
    private  final static String DAYS_PARAM = "cnt";



    public static String getBaseUrl(){
        return WEATHER_BASE_URL;
    }
    public static String getQueryParam(){
        return QUERY_PARAM;
    }
    public static String getFormatParam(){
        return FORMAT_PARAM;
    }
    public static String getUnitsParam(){
        return UNITS_PARAM;
    }
    public static String getDaysParam(){
        return DAYS_PARAM;
    }
}

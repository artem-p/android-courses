package com.example.android.sunshine.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class SunshineSyncTask {
    synchronized public static void syncWeather(Context context) {
        try {
            String jsonWeather = fetchWeather(context);
            updateData(context, jsonWeather);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetch weather from weather provider
     * */
    private static String fetchWeather(Context context) throws IOException {
        URL weatherRequestUrl = NetworkUtils.getUrl(context);
        return NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
    }


    /**
     * Update weather data
     * */
    private static void updateData(Context context, String jsonWeather) throws JSONException {
        ContentValues[] weatherValues = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context, jsonWeather);

        if (weatherValues != null && weatherValues.length > 0) {
            ContentResolver sunshineContentResolver = context.getContentResolver();

            // delete old weather data
            sunshineContentResolver.delete(WeatherContract.WeatherEntry.CONTENT_URI,
                    null,
                    null);

            sunshineContentResolver.bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, weatherValues);
        }
    }
}

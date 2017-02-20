/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherTextView;
    private TextView mErrorMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        /*
         * Using findViewById, we get a reference to our TextView from xml. This allows us to
         * do things like set the text of the TextView.
         */
        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);
        mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message);

        loadWeatherData();
    }


    private void loadWeatherData() {
        String prefferedLocation = SunshinePreferences.getPreferredWeatherLocation(MainActivity.this);

        URL owmQueryUrl = NetworkUtils.buildUrl(prefferedLocation);

        GetOwmDataTask getOwmDataTask = new GetOwmDataTask();
        getOwmDataTask.execute(owmQueryUrl);
    }
    class GetOwmDataTask extends AsyncTask<URL, Void, String[]> {

        @Override
        protected String[] doInBackground(URL... params) {
            if (params.length == 0) {
                return null;
            }

            URL owmUrl = params[0];

            String[] weatherData = null;
            try {
                String jsonResult = NetworkUtils.getResponseFromHttpUrl(owmUrl);
                weatherData = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this,
                        jsonResult);

                return weatherData;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return weatherData;
        }


        @Override
        protected void onPostExecute(String[] owmQueryResult) {
            if (owmQueryResult != null) {
                showWeatherData(owmQueryResult);
            } else {
                showErrorMessage();
            }
        }
    }

    private void showWeatherData(String[] weatherData) {
        for (String weatherStr : weatherData) {
            mWeatherTextView.append(weatherStr + "\n\n\n");
        }

        mWeatherTextView.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        mWeatherTextView.setText("");
        mWeatherTextView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }
}
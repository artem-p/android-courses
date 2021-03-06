package com.example.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    private String mForecast;
    private TextView mWeatherDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mWeatherDisplay = (TextView) findViewById(R.id.tv_display_weather);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mForecast = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                mWeatherDisplay.setText(mForecast);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItemId = item.getItemId();

        if (selectedItemId == R.id.action_share) {
            shareForecast();
        }

        return super.onOptionsItemSelected(item);
    }

    public void shareForecast() {
        if (mForecast != null) {
            String mediaType = "text/plain";
            String title = "Share forecast";

            Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                    .setChooserTitle(title)
                    .setType(mediaType)
                    .setText(mForecast)
                    .getIntent();

            if (shareIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(shareIntent);
            }
        }
    }
}
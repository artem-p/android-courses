package com.example.android.sunshine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;


public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        PreferenceScreen preferenceScreen = getPreferenceScreen();
        SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();

        int prefNumber = preferenceScreen.getPreferenceCount();

        for (int i = 0; i < prefNumber; i++) {
            Preference preference = preferenceScreen.getPreference(i);

            if (!(preference instanceof CheckBoxPreference)) {
                String val = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, val);
            }
        }
    }

    private void setPreferenceSummary(Preference preference, Object value) {
        String sVal = value.toString();
        String key = preference.getKey();

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(sVal);

            if (prefIndex >= 0) {
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            preference.setSummary(sVal);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference p = findPreference(key);
        if (null != p) {
            if (!(p instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}

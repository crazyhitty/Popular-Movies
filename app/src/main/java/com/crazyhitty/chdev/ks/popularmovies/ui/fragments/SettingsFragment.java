package com.crazyhitty.chdev.ks.popularmovies.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.crazyhitty.chdev.ks.popularmovies.R;

/**
 * Created by Kartik_ch on 2/15/2016.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onPause() {
        Preference prefResetApi =
                findPreference(getResources().getString(R.string.reset_api_key_pref_key));
        if (prefResetApi.getSharedPreferences().
                getBoolean(prefResetApi.getKey(), false)) {
            // apply reset, and then set the pref-value back to false
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.modify_api_key_pref_key), null);
            editor.commit();

            //reset the value
            SharedPreferences.Editor prefsEditor = prefResetApi.getSharedPreferences().edit();
            prefsEditor.putBoolean(getString(R.string.reset_api_key_pref_key), false);
            prefsEditor.commit();
        }
        super.onPause();
    }
}

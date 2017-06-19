package com.example.wifi.myapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.wifi.myapp.R;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }


}

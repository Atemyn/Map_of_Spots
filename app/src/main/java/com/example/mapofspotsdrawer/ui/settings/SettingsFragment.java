package com.example.mapofspotsdrawer.ui.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.mapofspotsdrawer.R;
import com.example.mapofspotsdrawer.ui.utils.UIUtils;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        UIUtils.hideListMapImageButton(requireActivity().findViewById(R.id.ib_list_map));
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}
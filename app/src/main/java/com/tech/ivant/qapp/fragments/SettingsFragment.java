package com.tech.ivant.qapp.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tech.ivant.qapp.R;

/**
 * Created by matthew on 7/3/15.
 */
public class SettingsFragment extends Fragment {

    public SettingsFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.preferences_frame, new SettingsPreferences()).commit();
        fm.beginTransaction()
                .replace(R.id.services_edit_frame, new ServicesEditFragment()).commit();

        return rootView;
    }



    public static class SettingsPreferences extends PreferenceFragment{
        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_preference);
        }
    }
}

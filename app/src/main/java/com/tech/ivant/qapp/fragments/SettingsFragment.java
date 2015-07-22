package com.tech.ivant.qapp.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tech.ivant.qapp.MainActivity;
import com.tech.ivant.qapp.R;
import com.tech.ivant.qapp.constants.StaticMethods;
import com.tech.ivant.qapp.preferences.TimePickerPreference;
import com.tech.ivant.qapp.util.DayTime;

/**
 * Created by matthew on 7/3/15.
 */
public class SettingsFragment extends Fragment {

    public SettingsFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        FragmentManager fm = getFragmentManager();

        SettingsPreferences settingsPreferences = new SettingsPreferences();

        fm.beginTransaction()
                .replace(R.id.preferences_frame, settingsPreferences).commit();
        fm.beginTransaction()
                .replace(R.id.services_edit_frame, new ServicesEditFragment()).commit();

        FrameLayout preferenceFrame = (FrameLayout) rootView.findViewById(R.id.preferences_frame);
        FrameLayout servicesEditFrame = (FrameLayout) rootView.findViewById(R.id.services_edit_frame);


        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
        //Preference company_name = settingsPreferences.findPreference(SettingsPreferences.KEY_COMPANY_NAME);
        //company_name.setSummary(sharedPreferences.getString(SettingsPreferences.KEY_COMPANY_NAME, "Company ABC"));


        return rootView;
    }



    public static class SettingsPreferences extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

        public String KEY_COMPANY_NAME;
        public String KEY_LANGUAGE;
        public String KEY_TIME_FORMAT;
        public String KEY_DATE_FORMAT;
        public String KEY_AUTOMATIC_CLEAN;
        public String KEY_AUTOMATIC_CLEAN_TIME;

        @Override
        public void onResume(){
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause(){
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);


            KEY_COMPANY_NAME = getResources().getString(R.string.KEY_PREFERENCE_COMPANY_NAME);
            KEY_LANGUAGE = getResources().getString(R.string.KEY_PREFERENCE_LANGUAGE);
            KEY_TIME_FORMAT = getResources().getString(R.string.KEY_PREFERENCE_HOUR_FORMAT);
            KEY_DATE_FORMAT = getResources().getString(R.string.KEY_PREFERENCE_DATE_FORMAT);
            KEY_AUTOMATIC_CLEAN = getResources().getString(R.string.KEY_PREFERENCE_AUTOMATIC_CLEAN);
            KEY_AUTOMATIC_CLEAN_TIME = getResources().getString(R.string.KEY_PREFERENCE_AUTOMATIC_CLEAN_TIME);


            Log.d("KEYCOMPANYNAME", KEY_COMPANY_NAME);

            addPreferencesFromResource(R.xml.settings_preference);
            MainActivity parentActivity = (MainActivity) getActivity();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(parentActivity);

            Preference company_name = findPreference(KEY_COMPANY_NAME);
            String company_name_string = sharedPreferences.getString(KEY_COMPANY_NAME, "Company ABC");
            company_name.setSummary(company_name_string);

            Preference language = findPreference(KEY_LANGUAGE);
            String language_string = sharedPreferences.getString(KEY_LANGUAGE, "English");
            language.setSummary(language_string);

            Preference date_format = findPreference(KEY_DATE_FORMAT);
            String date_format_string = sharedPreferences.getString(KEY_DATE_FORMAT, "mm/dd/yy");
            date_format.setSummary(date_format_string);

            Preference automatic_clean = findPreference(KEY_AUTOMATIC_CLEAN_TIME);
            long clean_time = sharedPreferences.getLong(KEY_AUTOMATIC_CLEAN_TIME, TimePickerPreference.DEFAULT_VALUE);
            if(sharedPreferences.getBoolean(KEY_TIME_FORMAT, false)){
                automatic_clean.setTitle(DayTime.format24hr(clean_time));
            }else{
                automatic_clean.setTitle(DayTime.format12hr(clean_time));
            }
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            String default_value = null;
            if(key.equals(KEY_COMPANY_NAME)){
                default_value = "Company ABC";
            }
            else if(key.equals(KEY_LANGUAGE)){
                default_value = "English";
            }
            else if(key.equals(KEY_DATE_FORMAT)){
                default_value = "mm/dd/yy";
            }
            else if(key.equals(KEY_AUTOMATIC_CLEAN)){
                if(sharedPreferences.getBoolean(key, false)){
                    StaticMethods.setUpAutomaticCleanAlarm(getActivity());
                }
            }
            else if(key.equals(KEY_AUTOMATIC_CLEAN_TIME)){
                Preference preference = findPreference(key);
                long val = sharedPreferences.getLong(key, TimePickerPreference.DEFAULT_VALUE);
                if(sharedPreferences.getBoolean(KEY_TIME_FORMAT, false)){
                    preference.setTitle(DayTime.format24hr(val));
                } else {
                    preference.setTitle(DayTime.format12hr(val));
                }
                StaticMethods.setUpAutomaticCleanAlarm(getActivity());
            }
            if(default_value != null) {
                Preference preference = findPreference(key);
                String value = sharedPreferences.getString(key, default_value);
                preference.setSummary(value);
                if (key.equals(KEY_COMPANY_NAME)) {
                    getActivity().setTitle(value);
                }
            }

        }
    }

}

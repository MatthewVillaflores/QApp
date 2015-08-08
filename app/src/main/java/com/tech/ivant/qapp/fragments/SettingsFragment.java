package com.tech.ivant.qapp.fragments;

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
import android.widget.ListView;

import com.tech.ivant.qapp.MainActivity;
import com.tech.ivant.qapp.R;
import com.tech.ivant.qapp.constants.Constants;
import com.tech.ivant.qapp.constants.StaticMethods;
import com.tech.ivant.qapp.preferences.TimePickerPreference;
import com.tech.ivant.qapp.util.TimeHandler;

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

/*
        fm.beginTransaction()
                .replace(R.id.services_edit_frame, new ServicesEditFragment()).commit();
*/

        FrameLayout preferenceFrame = (FrameLayout) rootView.findViewById(R.id.preferences_frame);
        //FrameLayout servicesEditFrame = (FrameLayout) rootView.findViewById(R.id.services_edit_frame);

        preferenceFrame.setScrollContainer(false);
        //servicesEditFrame.setScrollContainer(false);
        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //preferenceFrame.setLayoutParams(layoutParams);
        //preferenceFrame.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;

        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
        //Preference company_name = settingsPreferences.findPreference(SettingsPreferences.KEY_COMPANY_NAME);
        //company_name.setSummary(sharedPreferences.getString(SettingsPreferences.KEY_COMPANY_NAME, "Company ABC"));

        return rootView;
    }

    public void updateServicesList(){

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

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

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
                automatic_clean.setTitle(TimeHandler.format24hr(clean_time));
            }else{
                automatic_clean.setTitle(TimeHandler.format12hr(clean_time));
            }

            /*
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            ListView listView = new ListView(this.getActivity());
            listView.setAdapter(preferenceScreen.getRootAdapter());
            listView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            listView.setScrollContainer(false);
            preferenceScreen.bind(listView);
            StaticMethods.setListViewHeightBasedOnChildren(listView);
            */
        }

        private ServicesEditFragment mServicesEditFragment;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.preference_settings_layout, null);

            ListView listView = (ListView) rootView.findViewById(android.R.id.list);

            mServicesEditFragment = new ServicesEditFragment();

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.preference_services_edit_frame, mServicesEditFragment).commit();

            //listView.setMinimumHeight(600);

            Log.d(Constants.LOG_TAG, "SENPAIIIi!");
            return rootView;
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
                    StaticMethods.setUpAutomaticCleanAlarm();
                }
            }
            else if(key.equals(KEY_AUTOMATIC_CLEAN_TIME)){
                Preference preference = findPreference(key);
                long val = sharedPreferences.getLong(key, TimePickerPreference.DEFAULT_VALUE);
                if(sharedPreferences.getBoolean(KEY_TIME_FORMAT, false)){
                    preference.setTitle(TimeHandler.format24hr(val));
                } else {
                    preference.setTitle(TimeHandler.format12hr(val));
                }
                StaticMethods.setUpAutomaticCleanAlarm();
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

        public void updateServicesList(){
            mServicesEditFragment.updateList();
        }
    }

}

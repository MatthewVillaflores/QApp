package com.tech.ivant.qapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.ivant.qapp.R;

/**
 * Created by matthew on 7/3/15.
 */
public class SettingsFragment extends android.app.Fragment {
    public SettingsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
}

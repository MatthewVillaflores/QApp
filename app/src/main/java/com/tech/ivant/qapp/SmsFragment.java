package com.tech.ivant.qapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by matthew on 7/2/15.
 */
public class SmsFragment extends Fragment{
    public SmsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_sms, container, false);
        return rootView;
    }
}

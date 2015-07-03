package com.tech.ivant.qapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.ivant.qapp.R;

/**
 * Created by matthew on 7/3/15.
 */
public class SmsFragment extends android.app.Fragment {
    public SmsFragment(){

    }

    public View onCreateViewGroup(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_sms, container, false);
    }
}

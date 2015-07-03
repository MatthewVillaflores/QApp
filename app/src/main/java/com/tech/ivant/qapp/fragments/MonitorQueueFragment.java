package com.tech.ivant.qapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.ivant.qapp.R;

/**
 * Created by matthew on 7/3/15.
 */
public class MonitorQueueFragment extends Fragment {
    public MonitorQueueFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_monitor_queue, container, false);
        return rootView;
    }
}
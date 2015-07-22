package com.tech.ivant.qapp.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tech.ivant.qapp.MainActivity;
import com.tech.ivant.qapp.Queue;
import com.tech.ivant.qapp.R;
import com.tech.ivant.qapp.Service;
import com.tech.ivant.qapp.ViewService;

/**
 * Created by matthew on 7/3/15.
 */
public class MonitorQueueFragment extends Fragment {

    private Service mService = null;
    public static String KEY_SERVICE_ID = "ivant_monitor_queue_service_id";
    public static String KEY_SERVICE_COUNT = "ivant_monitor_queue_service_count";
    public static String KEY_SERVICE_AVE_WAITING = "ivant_monitor_queue_service_ave_waiting";

    public static final String EXTRA_NEW_QUEUE = "com.tech.ivant.qapp.new_queue";

    private View mRootView;

    public MonitorQueueFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.fragment_monitor_queue, container, false);
        //updateList(rootView);

        Service services[] = Service.all(getActivity());

        if(services != null) {
            mService = services[0];

            HorizontalScrollView hScroll = (HorizontalScrollView) rootView.findViewById(R.id.serviceListHScroll);
            LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.serviceListLinearLayout);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            //layoutParams.gravity = Gravity.CENTER;
            for (final Service service : services) {
                Button button = new Button(getActivity());
                button.setText(service.name);
                button.setPadding(50, 50, 50, 50);

                linearLayout.addView(button, layoutParams);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mService = service;
                        updateList(rootView);
                    }
                });
            }


            updateList(rootView);
        }
        mRootView = rootView;
        return rootView;
    }

    @Override
    public void onResume() {
        updateList(mRootView);
        super.onResume();
    }

    public void updateList(View view){

        Fragment fragmentTop = new ViewServiceTopFragment();
        FragmentManager fragmentManager = getFragmentManager();
        Bundle arguments = new Bundle();
        arguments.putLong(KEY_SERVICE_ID, mService.id);

        fragmentTop.setArguments(arguments);
        fragmentManager.beginTransaction().replace(R.id.monitorQueueTopBar, fragmentTop).commit();

        Fragment fragmentBottom = new ViewServiceFragment();
        fragmentBottom.setArguments(arguments);
        fragmentManager.beginTransaction().replace(R.id.viewServiceFragmentFrame, fragmentBottom).commit();

    }

}
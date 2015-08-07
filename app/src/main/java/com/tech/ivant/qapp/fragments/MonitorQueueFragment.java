package com.tech.ivant.qapp.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.tech.ivant.qapp.entities.Queue;
import com.tech.ivant.qapp.R;
import com.tech.ivant.qapp.entities.Service;
import com.tech.ivant.qapp.dao.ServiceDao;

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
    public ViewServiceFragment fragmentBottom;

    public ViewServiceTopFragment getFragmentTop() {
        return fragmentTop;
    }

    public ViewServiceTopFragment fragmentTop;

    public ViewServiceFragment getFragmentBottom() {
        return fragmentBottom;
    }

    public MonitorQueueFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.fragment_monitor_queue, container, false);
        //updateList(rootView);

        Service services[] = ServiceDao.all();

        //put NoShow service to last
        for(int i=0;i<services.length;i++){
            if(services[i].id == Service.noShowServiceId){
                Service temp = services[services.length-1];
                services[services.length-1] = services[i];
                services[i] = temp;

            }
        }

        if(services.length > 0) {
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

        if(mService == null){
            return;
        }

        fragmentTop = new ViewServiceTopFragment();
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
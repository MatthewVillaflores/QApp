package com.tech.ivant.qapp.fragments;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import com.tech.ivant.qapp.MainActivity;
import com.tech.ivant.qapp.R;
import com.tech.ivant.qapp.Service;
import com.tech.ivant.qapp.ViewService;

/**
 * Created by matthew on 7/3/15.
 */
public class MonitorQueueFragment extends Fragment {

    private static final String log_tag = "MONITOR_QUEUE_FRAGMENT";
    public MonitorQueueFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_monitor_queue, container, false);
        //updateList(rootView);

        Service[] services = Service.all(getActivity());
        Log.d(log_tag, "Listing services " + services.length + " services");

        LocalActivityManager lam = new LocalActivityManager(getActivity(), false);
        TabHost mTabHost = (TabHost) rootView.findViewById(R.id.tabHost);

        lam.dispatchCreate(savedInstanceState);
        mTabHost.setup(lam);
        if(services.length>0) {
            for (Service service : services) {
                Intent intent = new Intent().setClass(getActivity(), ViewService.class);
                intent.putExtra(MainActivity.SERVICE_ID_EXTRA, service.id + "");
                TabHost.TabSpec mTabSpec = mTabHost
                        .newTabSpec(service.name)
                        .setIndicator(service.name.toUpperCase())
                        .setContent(intent);

                if (mTabHost == null) {
                    Log.d(log_tag, "mTabHost is null");
                }
                if (mTabSpec == null) {
                    Log.d(log_tag, "mTabSpec is null");
                }
                mTabHost.addTab(mTabSpec);
            }
            mTabHost.setCurrentTab(0);
        }


        return rootView;
    }

/*
    public void updateList(View view){

        final Service[] services = Service.all(this.getActivity());

        final ListView servicesList = (ListView) view.findViewById(R.id.serviceListView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1);
        servicesList.setAdapter(adapter);

        if( services != null) {
            for (Service service : services) {
                adapter.add(service.name);
            }
        }

        final Activity context = this.getActivity();

        servicesList.setClickable(true);
        servicesList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent open_service = new Intent(context, ViewService.class);
                open_service.putExtra(MainActivity.SERVICE_ID_EXTRA, services[position].id+"");
                startActivity(open_service);
            }
        });

    }
    */
}
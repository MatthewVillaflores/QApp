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

    private Service mService;

    public MonitorQueueFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_monitor_queue, container, false);
        //updateList(rootView);

        Service services[] = Service.all(getActivity());

        mService = services[0];

        HorizontalScrollView hScroll = (HorizontalScrollView) rootView.findViewById(R.id.serviceListHScroll);
        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.serviceListLinearLayout);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.gravity = Gravity.CENTER;
        for(Service service : services){
            Button button = new Button(getActivity());
            button.setText(service.name);

            linearLayout.addView(button, layoutParams);
        }

        updateList(rootView);

        return rootView;
    }


    public void updateList(View view){

        final Queue[] queues = Queue.where(getActivity(), Queue.QueueEntry.COLUMN_NAME_SERVICE_ID, mService.id+"");

        final ListView servicesList = (ListView) view.findViewById(R.id.monitorQueueQueueList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1);
        servicesList.setAdapter(adapter);

        if( queues != null) {
            for (Queue queue : queues) {
                adapter.add(queue.customerName);
            }
        }
/*
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
*/
    }

}
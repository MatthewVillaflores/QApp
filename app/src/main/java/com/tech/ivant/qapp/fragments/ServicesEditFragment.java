package com.tech.ivant.qapp.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.tech.ivant.qapp.R;
import com.tech.ivant.qapp.constants.StaticMethods;
import com.tech.ivant.qapp.entities.Service;
import com.tech.ivant.qapp.dao.ServiceDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by matthew on 7/3/15.
 */
public class ServicesEditFragment extends Fragment{
    public ServicesEditFragment(){}
    private EditServiceAdapter mServiceAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.fragment_services_edit, container, false);

        ListView services_edit_list = (ListView) rootView.findViewById(R.id.edit_services_list);
        final Service[] services = populateList(rootView);

        services_edit_list.setScrollContainer(false);
        StaticMethods.setListViewHeightBasedOnChildren(services_edit_list);
        return rootView;
    }

    public Service[] populateList(final View rootView){
        final Service[] services = ServiceDao.all();
        ListView services_edit_list = (ListView) rootView.findViewById(R.id.edit_services_list);


        try {
             mServiceAdapter = new EditServiceAdapter
                    (getActivity(), new ArrayList<Service>(Arrays.asList(services)), rootView.getResources());
        } catch(NullPointerException er){
             mServiceAdapter = new EditServiceAdapter
                     (getActivity(), new ArrayList<Service>(), rootView.getResources());
        }

        services_edit_list.setAdapter(mServiceAdapter);

        /*
        ArrayAdapter<String> services_list_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        services_edit_list.setAdapter(services_list_adapter);
        if(services != null) {
            for (Service service : services) {
                services_list_adapter.add(service.name);
            }
        }

        services_edit_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Service service_clicked = services[position];
                AlertDialog dialog;
                dialog = new AlertDialog.Builder(rootView.getContext())
                        .setMessage("Are you sure you want to delete " + service_clicked.name + "?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Do nothing
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                service_clicked.delete(rootView.getContext());
                                populateList(rootView);
                            }
                        })
                        .show();
            }
        });
        */

        return services;
    }



}

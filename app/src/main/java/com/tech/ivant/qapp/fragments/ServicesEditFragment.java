package com.tech.ivant.qapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tech.ivant.qapp.R;
import com.tech.ivant.qapp.Service;

/**
 * Created by matthew on 7/3/15.
 */
public class ServicesEditFragment extends Fragment{
    public ServicesEditFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_services_edit, container, false);

        Service[] services = Service.all(getActivity());
        ListView services_edit_list = (ListView) rootView.findViewById(R.id.edit_services_list);
        ArrayAdapter<String> services_list_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        services_edit_list.setAdapter(services_list_adapter);
        if(services != null) {
            for (Service service : services) {
                services_list_adapter.add(service.name);
            }
        }

        return rootView;
    }
}

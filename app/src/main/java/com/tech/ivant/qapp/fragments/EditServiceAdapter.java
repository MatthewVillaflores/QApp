package com.tech.ivant.qapp.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tech.ivant.qapp.R;
import com.tech.ivant.qapp.entities.Service;
import com.tech.ivant.qapp.dao.ServiceDao;

import java.util.ArrayList;

/**
 * Created by matthew on 7/9/15.
 *
 * Adapter for the Service List in the Settings
 *
 */
public class EditServiceAdapter extends BaseAdapter{

    private ArrayList<Service> data;
    private Activity activity;
    private static LayoutInflater inflater = null;
    public Resources res;
    Service tempValues = null;

    public EditServiceAdapter(Activity a, ArrayList<Service> d, Resources resLocal){
        this.activity = a;
        this.data = d;
        this.res = resLocal;

        inflater = ( LayoutInflater ) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        //if( data.size() <= 0 ) return 0;
        return data.size();
    }

    @Override
    public Service getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{

        public TextView list_service_name;
        public Button list_edit_button;
        public Button list_delete_button;

    }


    /**
     * Inflate layout
     */
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){
            vi = inflater.inflate(R.layout.adapter_settings_service, null);

            holder = new ViewHolder();
            holder.list_service_name = (TextView)vi.findViewById(R.id.list_service_name);
            holder.list_edit_button = (Button)vi.findViewById(R.id.list_edit_button);
            holder.list_delete_button = (Button)vi.findViewById(R.id.list_delete_button);

            vi.setTag(holder);
        }
        else{
            holder = (ViewHolder)vi.getTag();
        }

        final BaseAdapter adapter = this;

        if(data.size()>=position){
            //tempValues = null;
            tempValues = data.get(position);

            holder.list_service_name.setText(tempValues.name);
            holder.list_delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setMessage("Are you sure you want to delete?")
                            .setPositiveButton("Yes", new DeleteButtonListener
                                    (data.get(position), v, adapter))
                            .setNegativeButton("Cancel", null).show();
                }
            });
            holder.list_edit_button.setOnClickListener(new EditButtonListener(data.get(position), vi, adapter));
        }

        notifyDataSetChanged();
        return vi;

    }

    private class DeleteButtonListener implements DialogInterface.OnClickListener{

        Service rService;
        View refView;
        BaseAdapter adapter;

        public DeleteButtonListener(Service service, View v, BaseAdapter adapt){
            rService = service;
            refView = v;
            adapter = adapt;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            data.remove(rService);
            ServiceDao.delete(rService);
            adapter.notifyDataSetChanged();
        }
    }

    private class EditButtonListener implements View.OnClickListener{
        Service service;
        View rootview;
        BaseAdapter adapter;
        public EditButtonListener(Service service, View v, BaseAdapter adapt){
            this.service = service;
            this.rootview = v;
            adapter = adapt;
        }

        @Override
        public void onClick(View v) {
            final Dialog edit_Service = new Dialog(v.getContext());
            edit_Service.setTitle("Edit Service");
            edit_Service.setContentView(R.layout.dialog_edit_service);

            final EditText service_name = (EditText) edit_Service.findViewById(R.id.dialog_edit_service_name);
            service_name.setText(service.name);

            edit_Service.show();

            Button cancel = (Button)edit_Service.findViewById(R.id.dialog_edit_service_cancel_button);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edit_Service.dismiss();
                }
            });

            Button save = (Button)edit_Service.findViewById(R.id.dialog_edit_service_save_button);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    service.name = service_name.getText().toString();
                    ServiceDao.update(service);
                    edit_Service.dismiss();
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

}

package com.tech.ivant.qapp.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.tech.ivant.qapp.R;
import com.tech.ivant.qapp.Service;

import java.util.ArrayList;

/**
 * Created by matthew on 7/9/15.
 */
public class EditServiceAdapter extends BaseAdapter implements View.OnClickListener {

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


    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){
            vi = inflater.inflate(R.layout.list_service_item, null);

            holder = new ViewHolder();
            holder.list_service_name = (TextView)vi.findViewById(R.id.list_service_name);
            holder.list_edit_button = (Button)vi.findViewById(R.id.list_edit_button);
            holder.list_delete_button = (Button)vi.findViewById(R.id.list_delete_button);

            vi.setTag(holder);


        }
        else{
            holder = (ViewHolder)vi.getTag();

            //if(data.size()>0){
            //}
        }

        if(data.size()>=position){
            //tempValues = null;
            tempValues = data.get(position);

            holder.list_service_name.setText(tempValues.name);
            holder.list_delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setMessage("Are you sure you want to delete?")
                            .setPositiveButton("Yes", new ServicesEditFragment.DeleteButtonListener
                                    (data.remove(position)))
                            .setNegativeButton("Cancel", null).show();
                }
            });

            vi.setOnClickListener(new ServiceListOnItemClickListener(position));

        }

        notifyDataSetChanged();
        return vi;

    }

    @Override
    public void onClick(View v) {

    }

    private class ServiceListOnItemClickListener implements View.OnClickListener {
        private int mPosition;

        public ServiceListOnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View v) {

        }
    }
}

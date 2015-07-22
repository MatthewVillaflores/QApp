package com.tech.ivant.qapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tech.ivant.qapp.Queue;
import com.tech.ivant.qapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by matthew on 7/18/15.
 */
public class QueueAdapter extends BaseAdapter{

    private ArrayList<Queue> data;
    private Activity activity;
    private static LayoutInflater inflater = null;
    public Resources res;
    Queue tempValue = null;
    private Handler mHandler;

    public QueueAdapter(Activity a, ArrayList<Queue> d, Resources resLocal){
        this.activity = a;
        this.data = d;
        this.res = resLocal;
        mHandler = new Handler();

        inflater = ( LayoutInflater ) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{
        public TextView queueId;
        public TextView queueName;
        public TextView queueNote;
        public TextView queueArrival;
        public TextView queueWaitTime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if(convertView == null){
            vi = inflater.inflate(R.layout.adapter_view_service_queue, null);

            holder = new ViewHolder();
            holder.queueId = (TextView) vi.findViewById(R.id.adapter_queue_id);
            holder.queueName = (TextView) vi.findViewById(R.id.adapter_queue_name);
            holder.queueNote = (TextView) vi.findViewById(R.id.adapter_queue_note);
            holder.queueArrival = (TextView) vi.findViewById(R.id.adapter_queue_arrival_time);
            holder.queueWaitTime = (TextView) vi.findViewById(R.id.adapter_queue_wait_time);

            vi.setTag(holder);
        }else{
            holder = (ViewHolder) vi.getTag();
        }

        if(data.size()>=position){
            tempValue = data.get(position);

            holder.queueId.setText((position+1)+"");
            holder.queueName.setText(tempValue.customerName);
            holder.queueNote.setText(tempValue.notes);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(vi.getContext());
            boolean is24Hour = sharedPreferences.getBoolean(vi.getResources().getString(R.string.KEY_PREFERENCE_HOUR_FORMAT), false);

            SimpleDateFormat dateFormat;
            if(is24Hour){
                dateFormat = new SimpleDateFormat("HH:mm");
            }else{
                dateFormat = new SimpleDateFormat("hh:mm aa");
            }
            holder.queueArrival.setText(dateFormat.format(tempValue.queueDate));

            WaitTimer mWaitTimer = new WaitTimer(holder, tempValue);
            mHandler.postDelayed(mWaitTimer, 0);
        }


        return vi;

    }

    private class WaitTimer implements Runnable{
        ViewHolder vHolder;
        Queue value;

        final long secondsInMilli = 1000;
        final long minutesInMilli = secondsInMilli * 60;
        final long hoursInMilli = minutesInMilli * 60;
        final long daysInMilli = hoursInMilli * 24;

        public WaitTimer(ViewHolder vh, Queue q){
            this.vHolder = vh;
            this.value = q;
        }

        @Override
        public void run() {
            long elapsedTime = System.currentTimeMillis() - value.queueDate;

            long elapsedMinute = elapsedTime / minutesInMilli;
            elapsedTime = elapsedTime % minutesInMilli;

            long elapsedSecond = elapsedTime / secondsInMilli;

            vHolder.queueWaitTime.setText(elapsedMinute+"m "+elapsedSecond+"s");

            mHandler.postDelayed(this, 0);

        }
    }
}

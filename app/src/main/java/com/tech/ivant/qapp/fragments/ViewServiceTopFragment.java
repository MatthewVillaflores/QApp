package com.tech.ivant.qapp.fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tech.ivant.qapp.dao.QueueDao;
import com.tech.ivant.qapp.entities.Queue;
import com.tech.ivant.qapp.R;

/**
 * Created by matthew on 7/18/15.
 */
public class ViewServiceTopFragment extends Fragment {

    final long secondsInMilli = 1000;
    final long minutesInMilli = secondsInMilli * 60;
    final long hoursInMilli = minutesInMilli * 60;
    final long daysInMilli = hoursInMilli * 24;

    private long mServiceId;
    private int mCount = 0;
    private long mAveWaitMinutes = 0;
    private long mAveWaitSeconds = 0;
    private BroadcastReceiver mBroadcastReceiver;

    public ViewServiceTopFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_view_service_top_layout, container, false);

        Bundle args = getArguments();

        mServiceId = args.getLong(MonitorQueueFragment.KEY_SERVICE_ID);
        getInfo(rootView);
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("LOCAL_BROADCAST", "Received new broadcast");
                if (intent.getBooleanExtra(MonitorQueueFragment.EXTRA_NEW_QUEUE, false)) {
                    getInfo(rootView);
                }
            }
        };

        LocalBroadcastManager.getInstance(rootView.getContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter("db_new_queue"));

        return rootView;
    }

    public void getInfo(View rootView){

        Queue[] queueList = QueueDao.where(QueueDao.QueueEntry.COLUMN_NAME_SERVICE_ID, mServiceId + "");
        if(queueList.length <= 0){
            mCount = 0;
            mAveWaitMinutes = 0;
            mAveWaitSeconds = 0;
        }else{
            mCount = queueList.length;

            long totalWait = 0;
            for(Queue queue : queueList){
                totalWait += System.currentTimeMillis() - queue.queueDate;
            }
            totalWait = totalWait/queueList.length;

            long mAveWait = totalWait;

            mAveWaitMinutes = mAveWait / minutesInMilli;
            mAveWait = mAveWait % minutesInMilli;

            mAveWaitSeconds = mAveWait / secondsInMilli;
        }

        TextView count = (TextView) rootView.findViewById(R.id.view_service_total_count);
        count.setText(mCount+"");

        TextView waiting = (TextView) rootView.findViewById(R.id.view_service_ave_waiting_time);
        waiting.setText(mAveWaitMinutes+"m "+mAveWaitSeconds+"s");

    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }
}

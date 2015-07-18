package com.tech.ivant.qapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tech.ivant.qapp.R;

import org.w3c.dom.Text;

/**
 * Created by matthew on 7/18/15.
 */
public class ViewServiceTopFragment extends Fragment {

    final long secondsInMilli = 1000;
    final long minutesInMilli = secondsInMilli * 60;
    final long hoursInMilli = minutesInMilli * 60;
    final long daysInMilli = hoursInMilli * 24;

    private int mCount = 0;
    private long mAveWaitMinutes = 0;
    private long mAveWaitSeconds = 0;

    public ViewServiceTopFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_service_top_layout, container, false);

        Bundle args = getArguments();
        mCount = args.getInt(MonitorQueueFragment.KEY_SERVICE_COUNT);
        long mAveWait = args.getLong(MonitorQueueFragment.KEY_SERVICE_AVE_WAITING);

        long mAveWaitMinutes = mAveWait / minutesInMilli;
        mAveWait = mAveWait % minutesInMilli;

        long mAveWaitSeconds = mAveWait / secondsInMilli;

        TextView count = (TextView) rootView.findViewById(R.id.view_service_top_total_count);
        count.setText(mCount+"");

        TextView waiting = (TextView) rootView.findViewById(R.id.view_service_top_average);
        waiting.setText(mAveWaitMinutes+"m "+mAveWaitSeconds+"s");

        return rootView;
    }
}

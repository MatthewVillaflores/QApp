package com.tech.ivant.qapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.tech.ivant.qapp.R;
import com.tech.ivant.qapp.dao.ServiceDao;
import com.tech.ivant.qapp.dao.records.TotalQueueDao;
import com.tech.ivant.qapp.entities.Service;
import com.tech.ivant.qapp.entities.records.TotalQueue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by matthew on 7/3/15.
 */
public class ReportFragment extends android.app.Fragment {
    public ReportFragment(){}

    private ArrayList<TotalQueue[]> mTotalQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView =  inflater.inflate(R.layout.fragment_report, container, false);

        retrieveData();

        GraphView graphViewTotalQueueTime = (GraphView) rootView.findViewById(R.id.graphViewTotalQueueCount);
        Viewport viewport = graphViewTotalQueueTime.getViewport();
        viewport.setMinX(0);
        viewport.setMaxX(7);
        viewport.setScalable(true);
        viewport.setScrollable(true);

        //ArrayList<BarGraphSeries<DataPoint>> barSeries = new ArrayList<>(5);
        DataPoint[][] dataPoints = new DataPoint[mTotalQueue.size()][mTotalQueue.get(0).length];

        for(int i=0;i<mTotalQueue.size();i++){
            for(int j=0;j<mTotalQueue.get(0).length;j++){
            }
        }

        for(int i=1;i<=6;i++){
            for(int j=1;j<=6;j++){
                dataPoints[i-1][j-1] = new DataPoint(j, i*j);
            }
            BarGraphSeries barGraphSeries = new BarGraphSeries<DataPoint>(dataPoints[i-1]);
            //barSeries.add(barGraphSeries);
            barGraphSeries.setSpacing(20);
            graphViewTotalQueueTime.addSeries(barGraphSeries);
        }
//        graphViewTotalQueueTime.addSeries(holder);


        GraphView graphViewAverageWaitingTime = (GraphView) rootView.findViewById(R.id.graphViewAverageWaitingTime);

        Viewport viewport1 = graphViewAverageWaitingTime.getViewport();
        viewport1.setScrollable(true);
        viewport1.setScalable(true);
        viewport1.setMinX(0);
        viewport1.setMaxX(7);

        for(DataPoint[] dataPoint : dataPoints){
            graphViewAverageWaitingTime.addSeries(new LineGraphSeries(dataPoint));
        }

        return rootView;
    }

    private void retrieveData(){
        Service[] services = ServiceDao.all();

        mTotalQueue = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        TotalQueue[] temp;

        for(int i=0;i<5;i++) {
            calendar.add(Calendar.DATE, i*-1);

            temp = TotalQueue.getByDates(calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

            mTotalQueue.add(temp);
        }



    }
}

package com.tech.ivant.qapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.tech.ivant.qapp.R;
import com.tech.ivant.qapp.constants.Constants;
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

    private ArrayList<BarGraphData> mTotalQueueData;

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
        DataPoint[][] dataPoints = new DataPoint[6][6];
        for(int i=1;i<=6;i++) {
            for (int j = 1; j <= 6; j++) {
                dataPoints[i - 1][j - 1] = new DataPoint(j, 0);
            }
        }

        for(int i=0;i<mTotalQueueData.size();i++){
            BarGraphData barGraphData = mTotalQueueData.get(i);
            for(int j=0;j<barGraphData.data.size();j++){
                dataPoints[i][j] = new DataPoint(j+1, barGraphData.data.get(j).total);
            }
        }

        for(DataPoint[] dataPoint : dataPoints){
            BarGraphSeries<DataPoint> barGraphSeries = new BarGraphSeries<>(dataPoint);
            barGraphSeries.setSpacing(20);
            graphViewTotalQueueTime.addSeries(barGraphSeries);
        }
        dataPoints = new DataPoint[6][6];

        for(int i=1;i<=6;i++){
            for(int j=1;j<=6;j++){
                dataPoints[i-1][j-1] = new DataPoint(j, i*j);
            }
        }

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
        ArrayList<TotalQueue[]> tQueue = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        TotalQueue[] temp;

        for(int i=0;i<5;i++) {

            temp = TotalQueue.getByDates(calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

            tQueue.add(temp);


            Log.d(Constants.LOG_TAG, "DB Query for "
                    + calendar.get(Calendar.DAY_OF_MONTH) + "/"
                    + calendar.get(Calendar.MONTH) + "/"
                    + calendar.get(Calendar.YEAR)+ ": " + temp.length);

            calendar.add(Calendar.DATE, -1);
        }

        mTotalQueueData = new ArrayList<>();


        for(TotalQueue[] tq : tQueue){
            for(TotalQueue totQ : tq){
                BarGraphData barGraphData = null;
                for (BarGraphData graphData : mTotalQueueData){
                    if(totQ.serviceId == graphData.serviceId){
                        graphData.data.add(totQ);
                        barGraphData = graphData;
                        break;
                    }
                }
                if(barGraphData == null){
                    barGraphData = new BarGraphData(totQ.serviceId);
                    barGraphData.data.add(totQ);
                    int i = 0;
                    for(BarGraphData bgd : mTotalQueueData){
                        if(barGraphData.serviceId > bgd.serviceId) break;
                        i++;
                    }
                    mTotalQueueData.add(i, barGraphData);
                }
            }
        }


    }

    private class BarGraphData{
        long serviceId;
        ArrayList<TotalQueue> data;

        public BarGraphData(){
            data = new ArrayList<>();
            serviceId = -1;
        }

        public BarGraphData(long service){
            data = new ArrayList<>();
            serviceId = service;
        }
    }
}

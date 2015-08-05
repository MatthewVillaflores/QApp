package com.tech.ivant.qapp.entities;

import android.util.Log;

import com.tech.ivant.qapp.constants.Constants;
import com.tech.ivant.qapp.dao.ReportDao;

import java.util.Calendar;

/**
 * Created by matthew on 7/21/15.
 */
public class Report {
    public long id;
    public int day;
    public int month;
    public int year;
    public int total;
    public long sumAverageWait;
    public long serviceId;

    //empty constructor: instantiate with current Time, and 0 total
    public Report(){
        this.id = -1;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = calendar.get(Calendar.MONTH);
        this.year = calendar.get(Calendar.YEAR);
        this.total = 0;
        this.sumAverageWait = 0;
    }

    public Report(int day, int month, int year, int total, long sumAverageWait) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.total = total;
        this.sumAverageWait = sumAverageWait;
        this.id = -1;
    }

    public Report(int day, int month, int year, int total, long sumAverageWait, long serviceId) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.total = total;
        this.sumAverageWait = sumAverageWait;
        this.serviceId = serviceId;
        this.id = -1;
    }

    public Report(long id, int day, int month, int year, int total, long sumAverageWait, long serviceId) {
        this.id = id;
        this.day = day;
        this.month = month;
        this.year = year;
        this.total = total;
        this.sumAverageWait = sumAverageWait;
        this.serviceId = serviceId;
    }

    //Abstraction of where clause with multiple conditions with day, month, and year as the conditions
    public static Report[] getByDates(int day, int month, int year){
        String fields[] = {
                ReportDao.TotalQueueEntry.COLUMN_NAME_DAY,
                ReportDao.TotalQueueEntry.COLUMN_NAME_MONTH,
                ReportDao.TotalQueueEntry.COLUMN_NAME_YEAR
        };
        String values[] = {
                Integer.toString(day),
                Integer.toString(month),
                Integer.toString(year)
        };

        return ReportDao.where(fields, values);
    }

    //Add a Queue to the Reports
    //Creates a New Report entry if it is the first Queue for the day,
    // else it iterates the total count
    public static void addQueue(Queue queue){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        //Get all the Reports for today
        Report[] today = Report.getByDates(calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.YEAR));

        //Find the Report corresponding the queue's service id
        Report tQueue = null;
        if (today.length > 0) {
            for (Report tq : today) {
                if (tq.serviceId == queue.service_id) {
                    tQueue = tq;
                    break;
                }
            }
        }

        //If there is no entry, create a new entry
        if(tQueue == null) {
            tQueue = new Report();
            tQueue.serviceId = queue.service_id;
            ReportDao.save(tQueue);
            Log.d(Constants.LOG_TAG, "Created new entry with id: " + tQueue.id);
        }

        //iterate
        tQueue.total+=1;

        ReportDao.update(tQueue);
    }

    public static void addAveWait(Queue queue) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Report[] today = Report.getByDates(calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.YEAR));

        Report tQueue = null;
        if (today.length > 0) {
            for (Report tq : today) {
                if (tq.serviceId == queue.service_id) {
                    tQueue = tq;
                    break;
                }
            }
        }
        if(tQueue == null) {
            tQueue = new Report();
            tQueue.serviceId = queue.service_id;
            ReportDao.save(tQueue);
            Log.d(Constants.LOG_TAG, "Created new entry with id: " + tQueue.id);
        }
        tQueue.sumAverageWait+= (System.currentTimeMillis() - queue.queueDate);

        ReportDao.update(tQueue);
    }
}


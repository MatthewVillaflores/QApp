package com.tech.ivant.qapp.entities.records;

import android.util.Log;

import com.tech.ivant.qapp.constants.Constants;
import com.tech.ivant.qapp.dao.ServiceDao;
import com.tech.ivant.qapp.dao.records.TotalQueueDao;
import com.tech.ivant.qapp.entities.Queue;
import com.tech.ivant.qapp.entities.Service;

import java.util.Calendar;

/**
 * Created by matthew on 7/21/15.
 */
public class TotalQueue {
    public long id;
    public int day;
    public int month;
    public int year;
    public int total;
    public long serviceId;

    //empty constructor: instantiate with current Time, and 0 total
    public TotalQueue(){
        this.id = -1;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = calendar.get(Calendar.MONTH);
        this.year = calendar.get(Calendar.YEAR);
        this.total = 0;
    }

    public TotalQueue(int day, int month, int year, int total) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.total = total;
        this.id = -1;
    }

    public TotalQueue(int day, int month, int year, int total, long serviceId) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.total = total;
        this.serviceId = serviceId;
        this.id = -1;
    }

    public TotalQueue(long id, int day, int month, int year, int total, long serviceId) {
        this.id = id;
        this.day = day;
        this.month = month;
        this.year = year;
        this.total = total;
        this.serviceId = serviceId;
    }

    public static TotalQueue[] getByDates(int day, int month, int year){
        String fields[] = {
                TotalQueueDao.TotalQueueEntry.COLUMN_NAME_DAY,
                TotalQueueDao.TotalQueueEntry.COLUMN_NAME_MONTH,
                TotalQueueDao.TotalQueueEntry.COLUMN_NAME_YEAR
        };
        String values[] = {
                Integer.toString(day),
                Integer.toString(month),
                Integer.toString(year)
        };

        return TotalQueueDao.where(fields, values);
    }

    public static void addQueue(Queue queue){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        TotalQueue[] today = TotalQueue.getByDates(calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.YEAR));

        TotalQueue tQueue = null;
        if (today.length > 0) {
            for (TotalQueue tq : today) {
                if (tq.serviceId == queue.service_id) {
                    tQueue = tq;
                    break;
                }
            }
        }
        if(tQueue == null) {
            tQueue = new TotalQueue(calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.YEAR),
                    0, queue.service_id);
            TotalQueueDao.save(tQueue);
            Log.d(Constants.LOG_TAG, "Created new entry with id: " + tQueue.id);
        }
        tQueue.total+=1;

        TotalQueueDao.update(tQueue);
    }
}


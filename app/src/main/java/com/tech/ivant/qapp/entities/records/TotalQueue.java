package com.tech.ivant.qapp.entities.records;

import com.tech.ivant.qapp.dao.records.TotalQueueDao;

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

    public TotalQueue(){

    }

    public TotalQueue(int day, int month, int year, int total) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.total = total;
    }

    public TotalQueue(int day, int month, int year, int total, long serviceId) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.total = total;
        this.serviceId = serviceId;
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
}


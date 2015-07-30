package com.tech.ivant.qapp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by matthew on 7/21/15.
 *
 * This class is meant to abstract the underlying computations
 * for setting the time given only an hour value and a minute value.
 *
 */
public class TimeHandler {

    public final long secondsInMilli = 1000;
    public final long minutesInMilli = secondsInMilli * 60;
    public final long hoursInMilli = minutesInMilli * 60;
    public final long daysInMilli = hoursInMilli * 24;
    public final long yearsInMilli = daysInMilli * 365;

    private int HOUR;
    private int MINUTE;
    private long longValue;
    private Calendar mCalendar;

    public TimeHandler(){
        this.HOUR = 0;
        this.MINUTE = 0;
        mCalendar = Calendar.getInstance();
        getMillis();
    }

    public TimeHandler(int hh, int mm){
        this.HOUR = hh;
        this.MINUTE = mm;
        mCalendar = Calendar.getInstance();
        getMillis();
    }

    public TimeHandler(long val){
        mCalendar = Calendar.getInstance();
        setLongValue(val);
    }

    public long getMillis(){
        mCalendar.set(Calendar.HOUR_OF_DAY, HOUR);
        mCalendar.set(Calendar.MINUTE, MINUTE);
        longValue = mCalendar.getTimeInMillis();
        return longValue;
    }

    public int getHOUR() {
        return HOUR;
    }

    public void setHOUR(int HOUR) {
        this.HOUR = HOUR;
        getMillis();
    }

    public int getMINUTE() {
        return MINUTE;
    }

    public void setMINUTE(int MINUTE) {
        this.MINUTE = MINUTE;
        getMillis();
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
        mCalendar.setTimeInMillis(longValue);
        this.HOUR = mCalendar.get(Calendar.HOUR_OF_DAY);
        this.MINUTE = mCalendar.get(Calendar.MINUTE);
    }

    public static String format12hr(long val){
        return (new SimpleDateFormat("hh:mm aa").format(val));
    }

    public static String format24hr(long val){
        return (new SimpleDateFormat("HH:mm").format(val));
    }
}

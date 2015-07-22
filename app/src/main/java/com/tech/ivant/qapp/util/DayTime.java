package com.tech.ivant.qapp.util;

import java.text.SimpleDateFormat;

/**
 * Created by matthew on 7/21/15.
 *
 * This class is meant to abstract the underlying computations
 * for setting the time given only an hour value and a minute value.
 *
 */
public class DayTime {

    public final long secondsInMilli = 1000;
    public final long minutesInMilli = secondsInMilli * 60;
    public final long hoursInMilli = minutesInMilli * 60;
    public final long daysInMilli = hoursInMilli * 24;
    public final long yearsInMilli = daysInMilli * 365;

    private int HOUR;
    private int MINUTE;
    private final long BASE_VALUE = 57600000;
    private long longValue;

    public DayTime(){
        this.HOUR = 0;
        this.MINUTE = 0;
        this.longValue = BASE_VALUE;
    }

    public DayTime(int hh, int mm){
        this.HOUR = hh;
        this.MINUTE = mm;
        getMillis();
    }

    public DayTime(long val){
        this.longValue = val;
        long temp = val - BASE_VALUE;
        this.HOUR = (int) (temp / hoursInMilli);
        this.MINUTE = (int) ((temp % hoursInMilli) / minutesInMilli);
    }

    public long getMillis(){
        longValue = BASE_VALUE + (HOUR*hoursInMilli) + (MINUTE*minutesInMilli);
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
        long temp = longValue - BASE_VALUE;
        this.HOUR = (int) (temp / hoursInMilli);
        this.MINUTE = (int) ((temp % hoursInMilli) / minutesInMilli);
    }

    public static String format12hr(long val){
        return (new SimpleDateFormat("hh:mm aa").format(val));
    }

    public static String format24hr(long val){
        return (new SimpleDateFormat("HH:mm").format(val));
    }
}

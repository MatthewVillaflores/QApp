package com.tech.ivant.qapp;

import android.provider.BaseColumns;

/**
 * Created by matthew on 6/27/15.
 */
public class Service {
    /**
     * defaultWaitingTime
     * endNumber
     * logo
     * name
     * notes
     * online
     * priorityNumber
     * startNumber
     */


    public static class ServiceEntry implements BaseColumns{
        public static final String TABLE_NAME = "service";
        public static final String COLUMN_NAME_ID = "id";

    }

}

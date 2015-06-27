package com.tech.ivant.qapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

    public long id;
    public String name;
    public String notes;




    /*
    TODO:

    CONSTRUCTOR
    SAVE
    UPDATE
    DELETE


    STATIC QUERIES:
        FIND
        WHERE
        ALL
     */

    public static class ServiceDBHelper extends SQLiteOpenHelper{

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + ServiceEntry.TABLE_NAME +" ("
                + ServiceEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + ServiceEntry.COLUMN_NAME_SNAME + " STRING, "
                + ServiceEntry.COLUMN_NAME_NOTES + " TEXT);";
        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + ServiceEntry.TABLE_NAME + ";";
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "queue_app.db";

        public ServiceDBHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_TABLE);
            onCreate(db);
        }
    }


    public static class ServiceEntry implements BaseColumns{
        public static final String TABLE_NAME = "service";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_SNAME = "name";
        public static final String COLUMN_NAME_NOTES = "notes";
    }

}

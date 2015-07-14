package com.tech.ivant.qapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    public int startNumber;
    public int endNumber;


    public Service() {

    }

    public Service(String name, String notes) {
        this.name = name;
        this.notes = notes;
        this.id = -1;
        this.startNumber = 1;
        this.endNumber = 0;
    }

    public Service(long id, String name, String notes) {
        this.id = id;
        this.name = name;
        this.notes = notes;
        this.startNumber = 1;
        this.endNumber = 0;
    }

    public Service(long id, String name, String notes, int startNumber, int endNumber) {
        this.id = id;
        this.name = name;
        this.notes = notes;
        this.startNumber = startNumber;
        this.endNumber = endNumber;
    }

    public static void initialize(Context context){
        SQLiteDatabase db = getWriteSQLiteDB(context);
    }

    public static SQLiteDatabase getWriteSQLiteDB(Context context) {
        DBManager dbhelper = new DBManager(context);
        return dbhelper.getWritableDatabase();
    }

    public static SQLiteDatabase getReadSQLiteDB(Context context) {
        DBManager dbhelper = new DBManager(context);
        return dbhelper.getReadableDatabase();
    }

    public void save(Context context) {
        if(id < 0){
            SQLiteDatabase db = getWriteSQLiteDB(context);
            ContentValues values = new ContentValues();
            values.put(ServiceEntry.COLUMN_NAME_SNAME, this.name);
            values.put(ServiceEntry.COLUMN_NAME_NOTES, this.notes);
            values.put(ServiceEntry.COLUMN_NAME_START_NUMBER, this.startNumber);
            values.put(ServiceEntry.COLUMN_NAME_END_NUMBER, this.endNumber);
            this.id = db.insert(ServiceEntry.TABLE_NAME, null, values);
        } else {
            update(context);
        }
    }

    public void update(Context context) {
        if(id < 0){
            save(context);
        } else {
            SQLiteDatabase db = getReadSQLiteDB(context);
            ContentValues values = new ContentValues();
            values.put(ServiceEntry.COLUMN_NAME_SNAME, this.name);
            values.put(ServiceEntry.COLUMN_NAME_NOTES, this.notes);
            values.put(ServiceEntry.COLUMN_NAME_START_NUMBER, this.startNumber);
            values.put(ServiceEntry.COLUMN_NAME_END_NUMBER, this.endNumber);
            db.update(ServiceEntry.TABLE_NAME, values, ServiceEntry.COLUMN_NAME_ID + " = " + this.id, null);
        }
    }

    public void delete(Context context) {
        SQLiteDatabase db = getReadSQLiteDB(context);
        db.delete(ServiceEntry.TABLE_NAME, ServiceEntry.COLUMN_NAME_ID + " = " + this.id, null);
    }

    public static Service find(Context context, long id){
        SQLiteDatabase db = getReadSQLiteDB(context);
        Cursor c = db.rawQuery("SELECT * FROM " + ServiceEntry.TABLE_NAME
                + " WHERE " + ServiceEntry.COLUMN_NAME_ID + " = " + id + ";", null);
        if(c.getCount() > 0){
            c.moveToFirst();
            Service service = new Service(c.getLong(0), c.getString(1), c.getString(2), c.getInt(3), c.getInt(4));
            c.close();
            db.close();
            return service;
        }
        c.close();
        db.close();
        return null;
    }

    public static Service[] where(Context context, String column, String query){
        SQLiteDatabase db = getReadSQLiteDB(context);
        Cursor c = db.rawQuery("SELECT * FROM " + ServiceEntry.TABLE_NAME
                + " WHERE " + column + " = " + query + ";", null);
        if(c.getCount() > 0){
            c.moveToFirst();
            Service[] services = new Service[c.getCount()];
            for(int i=0;i<c.getCount();i++){
                services[i] = new Service(c.getLong(0), c.getString(1), c.getString(2), c.getInt(3), c.getInt(4));
                c.moveToNext();
            }
            db.close();
            c.close();
            return services;
        }

        db.close();
        c.close();
        return null;
    }

    public static Service[] all(Context context){
        SQLiteDatabase db = getReadSQLiteDB(context);
        Cursor c = db.rawQuery("SELECT * FROM " + ServiceEntry.TABLE_NAME + ";", null);
        if(c.getCount() > 0){
            c.moveToFirst();
            Service[] services = new Service[c.getCount()];
            for(int i=0;i<c.getCount();i++){
                services[i] = new Service(c.getLong(0), c.getString(1), c.getString(2), c.getInt(3), c.getInt(4));
                c.moveToNext();
            }
            c.close();
            db.close();
            return services;
        }
        c.close();
        db.close();
        return null;
    }

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + ServiceEntry.TABLE_NAME +" ("
            + ServiceEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + ServiceEntry.COLUMN_NAME_SNAME + " STRING, "
            + ServiceEntry.COLUMN_NAME_NOTES + " TEXT, "
            + ServiceEntry.COLUMN_NAME_START_NUMBER + " INTEGER, "
            + ServiceEntry.COLUMN_NAME_END_NUMBER + " INTEGER "
                    + ");";
    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + ServiceEntry.TABLE_NAME + ";";



    public static class ServiceEntry implements BaseColumns{
        public static final String TABLE_NAME = "service";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_SNAME = "name";
        public static final String COLUMN_NAME_NOTES = "notes";
        public static final String COLUMN_NAME_START_NUMBER = "start_number";
        public static final String COLUMN_NAME_END_NUMBER = "end_number";
    }

}

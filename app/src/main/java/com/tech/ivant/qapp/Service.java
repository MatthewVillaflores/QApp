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

    public Service() {
    }

    public Service(String name, String notes) {
        this.name = name;
        this.notes = notes;
        this.id = -1;
    }

    public Service(String name, String notes, long id) {
        this.id = id;
        this.name = name;
        this.notes = notes;
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
            return new Service(c.getString(1), c.getString(2), c.getLong(0));
        }
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
                services[i] = new Service(c.getString(1), c.getString(2), c.getLong(0));
                c.moveToNext();
            }
            return services;
        }

        return null;
    }

    public static Service[] all(Context context){
        SQLiteDatabase db = getReadSQLiteDB(context);
        Cursor c = db.rawQuery("SELECT * FROM " + ServiceEntry.TABLE_NAME + ";", null);
        if(c.getCount() > 0){
            c.moveToFirst();
            Service[] services = new Service[c.getCount()];
            for(int i=0;i<c.getCount();i++){
                services[i] = new Service(c.getString(1), c.getString(2), c.getLong(0));
                c.moveToNext();
            }
            return services;
        }

        return null;
    }

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + ServiceEntry.TABLE_NAME +" ("
            + ServiceEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + ServiceEntry.COLUMN_NAME_SNAME + " STRING, "
            + ServiceEntry.COLUMN_NAME_NOTES + " TEXT);";
    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + ServiceEntry.TABLE_NAME + ";";



    public static class ServiceEntry implements BaseColumns{
        public static final String TABLE_NAME = "service";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_SNAME = "name";
        public static final String COLUMN_NAME_NOTES = "notes";
    }

}

package com.tech.ivant.qapp.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.tech.ivant.qapp.DBManager;
import com.tech.ivant.qapp.entities.Service;

/**
 * Created by matthew on 7/22/15.
 */
public class ServiceDao {

    //Insert service into the database
    public static long save(Service service){
        if(find(service.id) == null){       //Check if current service is in the database if no, save; else, update.
            SQLiteDatabase db = DBManager.getWriteDatabase();
            ContentValues values = new ContentValues();
            values.put(ServiceEntry.COLUMN_NAME_SNAME, service.name);
            values.put(ServiceEntry.COLUMN_NAME_NOTES, service.notes);
            values.put(ServiceEntry.COLUMN_NAME_START_NUMBER, service.startNumber);
            values.put(ServiceEntry.COLUMN_NAME_END_NUMBER, service.endNumber);
            service.id = db.insert(ServiceEntry.TABLE_NAME, null, values);
        } else {
            update(service);
        }
        return service.id;
    }

    //Update entry on database
    public static void update(Service service){
        if(find(service.id) != null){       //Check if current service is in the database, if yes, update; else, save.
            SQLiteDatabase db = DBManager.getWriteDatabase();
            ContentValues values = new ContentValues();
            values.put(ServiceEntry.COLUMN_NAME_SNAME, service.name);
            values.put(ServiceEntry.COLUMN_NAME_NOTES, service.notes);
            values.put(ServiceEntry.COLUMN_NAME_START_NUMBER, service.startNumber);
            values.put(ServiceEntry.COLUMN_NAME_END_NUMBER, service.endNumber);
            db.update(ServiceEntry.TABLE_NAME, values, ServiceEntry.COLUMN_NAME_ID + " = " + service.id, null);
        } else {
            save(service);
        }
    }

    //delete entry on database
    public static void delete(Service service){
        SQLiteDatabase db = DBManager.getReadDatabase();
        db.delete(ServiceEntry.TABLE_NAME, ServiceEntry.COLUMN_NAME_ID + " = " + service.id, null);
    }

    public static Service find(long id){
        SQLiteDatabase db = DBManager.getReadDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + ServiceEntry.TABLE_NAME
                + " WHERE " + ServiceEntry.COLUMN_NAME_ID + " = " + id + ";", null);
        if(c.getCount() > 0){
            c.moveToFirst();
            return new Service(c.getLong(0), c.getString(1), c.getString(2), c.getInt(3), c.getInt(4));
        }
        return null;
    }

    public static Service[] where(String column, long value){
        return where(column, Long.toString(value));
    }

    public static Service[] where(String column, String query){
        SQLiteDatabase db = DBManager.getReadDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + ServiceEntry.TABLE_NAME
                + " WHERE " + column + " = " + query+ ";", null);
        if(c.getCount() > 0){
            c.moveToFirst();
            Service[] services = new Service[c.getCount()];
            for(int i=0;i<c.getCount();i++){
                services[i] = new Service(c.getLong(0), c.getString(1), c.getString(2), c.getInt(3), c.getInt(4));
                c.moveToNext();
            }
            return services;
        }

        return new Service[0];
    }

    public static Service[] all(){
        SQLiteDatabase db = DBManager.getReadDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + ServiceEntry.TABLE_NAME + ";", null);
        if(c.getCount() > 0){
            c.moveToFirst();
            Service[] services = new Service[c.getCount()];
            for(int i=0;i<c.getCount();i++){
                services[i] = new Service(c.getLong(0), c.getString(1), c.getString(2), c.getInt(3), c.getInt(4));
                c.moveToNext();
            }
            return services;
        }

        return new Service[0];
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

    public static class ServiceEntry implements BaseColumns {
        public static final String TABLE_NAME = "service";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_SNAME = "name";
        public static final String COLUMN_NAME_NOTES = "notes";
        public static final String COLUMN_NAME_START_NUMBER = "start_number";
        public static final String COLUMN_NAME_END_NUMBER = "end_number";
    }

}

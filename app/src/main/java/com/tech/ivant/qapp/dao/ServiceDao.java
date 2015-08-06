package com.tech.ivant.qapp.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.tech.ivant.qapp.DBManager;
import com.tech.ivant.qapp.entities.Service;

import java.util.ArrayList;

/**
 * Created by matthew on 7/22/15.
 *
 * Database Access Object for Service Entity
 *
 * Table name: service
 * Fields (Columns):
 *  -id
 *  -name
 *  -notes
 *  -start_number
 *  -end_number
 *
 */
public class ServiceDao {

    /**
     * Given a Service object. Insert an entry of that object into the database
     * If there is already an entry corresponding to that Service object, it will update instead
     */
    public static long save(Service service){
        //Limit table rows to 6, 5 services plus no show service
        Service[] services = all();
        if(services.length>5){
            return -1;
        }

        //Check if current service is in the database if no, save; else, update.
        if(find(service.id) == null){
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

    /**
     * Given a Service object, this method updates its entry on the database.
     * If there is no entry corresponding to that Service, it inserts a new one.
     */
    public static void update(Service service){
        //Check if current service is in the database, if yes, update; else, save.
        if(find(service.id) != null){
            SQLiteDatabase db = DBManager.getWriteDatabase();
            ContentValues values = new ContentValues();
            values.put(ServiceEntry.COLUMN_NAME_SNAME, service.name);
            values.put(ServiceEntry.COLUMN_NAME_NOTES, service.notes);
            values.put(ServiceEntry.COLUMN_NAME_START_NUMBER, service.startNumber);
            values.put(ServiceEntry.COLUMN_NAME_END_NUMBER, service.endNumber);
            db.update(ServiceEntry.TABLE_NAME,
                    values,
                    ServiceEntry.COLUMN_NAME_ID + " = " + service.id,
                    null);
        } else {
            save(service);
        }
    }


    /**
     * Given a Service object, deletes that object from the database
     */
    public static void delete(Service service){
        SQLiteDatabase db = DBManager.getReadDatabase();
        db.delete(ServiceEntry.TABLE_NAME, ServiceEntry.COLUMN_NAME_ID + " = " + service.id, null);
    }

    /**
     * Find an entry in the database using a key (id)
     * Returns a Service object, or null if nothing is found
     *
     * SQL command:
     *  SELECT * FROM service WHERE id = (long value)id;
     */
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

    /**
     * Get entries from the database satisfying a given condition (Query using WHERE clause)
     * Returns an empty list if nothing is found, returns a Service array otherwise
     *
     * SQL command:
     *  SELECT * FROM service WHERE columnname = value;
     */
    public static Service[] where(String column, String query){
        SQLiteDatabase db = DBManager.getReadDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + ServiceEntry.TABLE_NAME
                + " WHERE " + column + " = \"" + query+ "\";", null);
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

    /**1574
     * Overloaded method: implicit conversion of long to String
     */
    public static Service[] where(String column, long value){
        return where(column, Long.toString(value));
    }

    /**
     * Get all entries in the database
     * returns a list containing all entries in the database
     * returns an empty list for an empty database
     *
     * SQL command:
     *  SELECT * FROM service;
     */
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

    /**
     * This method will remove the noShow service from the given array of services.
     * Returns an array of services with the noShow ommitted.
     */
    public static Service[] removeNoShow(Service[] services){
        if(services.length==0){
            return services;
        }

        Service[] servicesWithoutNoShow = new Service[services.length-1];

        int iter = 0;
        try {
            for (Service service : services) {
                if (!service.name.equals(Service.noShowServiceName)) {
                    servicesWithoutNoShow[iter] = service;
                    iter++;
                }
            }
        } catch(ArrayIndexOutOfBoundsException err){
            return services;
        }
        return servicesWithoutNoShow;

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

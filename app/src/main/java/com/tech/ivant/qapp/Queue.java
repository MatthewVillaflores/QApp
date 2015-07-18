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
public class Queue {

    /*
     * calldate
     * customerName
     * formattedQueueNumber
     * mobileNumber
     * notes
     * queueCall
     * queueDate
     * queueNumber
     * status
     * serviceId
     */

    //public long callDate;
    public String customerName;
    public String notes;
    public String mobileNumber;
    public int queueNumber;
    public long queueDate;
    public long service_id;
    public long id;


    public Queue(){
        this.id = -1;
    }

    public Queue(String customerName, String notes, long service_id) {
        this.customerName = customerName;
        this.notes = notes;
        this.mobileNumber = "";
        this.service_id = service_id;
        this.id = -1;
        this.queueDate = System.currentTimeMillis();
    }

    public Queue(String customerName, String notes, String mobileNumber, long service_id){
        this.customerName = customerName;
        this.notes = notes;
        this.mobileNumber = mobileNumber;
        this.service_id = service_id;
        this.id = -1;
        this.queueDate = System.currentTimeMillis();
    }

    public Queue(String customerName, String notes, long service_id, long id) {
        this.customerName = customerName;
        this.notes = notes;
        this.service_id = service_id;
        this.id = id;
        this.queueDate = System.currentTimeMillis();
    }

    public Queue(String customerName, String notes, String mobileNumber, long service_id, long id){
        this.customerName = customerName;
        this.notes = notes;
        this.mobileNumber = mobileNumber;
        this.service_id = service_id;
        this.id = id;
    }

    public Queue(String customerName, String notes, long service_id, long id, int queueNumber, long queueDate) {
        this.customerName = customerName;
        this.notes = notes;
        this.service_id = service_id;
        this.id = id;
        this.queueNumber = queueNumber;
        this.queueDate = queueDate;
    }

    public Queue(String customerName, String notes, String mobileNumber, int queueNumber, long queueDate, long service_id, long id) {
        this.customerName = customerName;
        this.notes = notes;
        this.mobileNumber = mobileNumber;
        this.queueNumber = queueNumber;
        this.queueDate = queueDate;
        this.service_id = service_id;
        this.id = id;
    }

    public static void initialize(Context context){
        SQLiteDatabase db = getWriteSQLiteDB(context);
    }

    public void save(Context context){
        if(id<0) {
            SQLiteDatabase db = getWriteSQLiteDB(context);
            ContentValues values = new ContentValues();
            values.put(QueueEntry.COLUMN_NAME_CNAME, this.customerName);
            values.put(QueueEntry.COLUMN_NAME_NOTES, this.notes);
            values.put(QueueEntry.COLUMN_NAME_MOBILE_NUMBER, this.mobileNumber);
            values.put(QueueEntry.COLUMN_NAME_SERVICE_ID, this.service_id);
            values.put(QueueEntry.COLUMN_NAME_QUEUE_DATE, this.queueDate);
            values.put(QueueEntry.COLUMN_NAME_QUEUE_NUMBER, this.queueNumber);
            this.id = db.insert(QueueEntry.TABLE_NAME, null, values);
        } else {
            update(context);
        }
    }

    public void update(Context context){
        if(id<0){      //Not yet in the database... save
            save(context);
        } else {
            SQLiteDatabase db = getReadSQLiteDB(context);
            ContentValues values = new ContentValues();
            values.put(QueueEntry.COLUMN_NAME_CNAME, this.customerName);
            values.put(QueueEntry.COLUMN_NAME_NOTES, this.notes);
            values.put(QueueEntry.COLUMN_NAME_MOBILE_NUMBER, this.mobileNumber);
            values.put(QueueEntry.COLUMN_NAME_SERVICE_ID, this.service_id);
            values.put(QueueEntry.COLUMN_NAME_QUEUE_DATE, this.queueDate);
            values.put(QueueEntry.COLUMN_NAME_QUEUE_NUMBER, this.queueNumber);
            db.update(QueueEntry.TABLE_NAME, values, QueueEntry.COLUMN_NAME_ID + " = " + this.id, null);
        }
    }

    public void delete(Context context){
        SQLiteDatabase db = getReadSQLiteDB(context);
        db.delete(QueueEntry.TABLE_NAME, QueueEntry.COLUMN_NAME_ID + " = " + this.id, null);
    }

    public static SQLiteDatabase getReadSQLiteDB(Context context){
        DBManager dbhelper = new DBManager(context);
        return dbhelper.getReadableDatabase();
    }

    public static SQLiteDatabase getWriteSQLiteDB(Context context){
        DBManager dbhelper = new DBManager(context);
        return dbhelper.getWritableDatabase();
    }

    public static Queue find(Context context, int id){
        //Query database SELECT * FROM queue WHERE id = 'id;
        SQLiteDatabase db = getReadSQLiteDB(context);
        Cursor c = db.rawQuery("SELECT * FROM " + QueueEntry.TABLE_NAME
                + " WHERE " + QueueEntry.COLUMN_NAME_ID + " = "
                + id + ";", null);
        if(c.getCount()>0) {
            c.moveToFirst();
            return translateCursorToQueue(c);
        } else {
            return null;
        }
    }

    public static Queue[] where(Context context, String columnname, String value){
        //Query database SELECT * FROM queue WHERE columnname = value;
        SQLiteDatabase db = getReadSQLiteDB(context);
        Cursor c = db.rawQuery("SELECT * FROM " + QueueEntry.TABLE_NAME
                + " WHERE " + columnname + " = " + value + ";", null);

        if(c.getCount()>0) {
            c.moveToFirst();
            Queue[] query = new Queue[c.getCount()];
            for (int i = 0; i < c.getCount(); i++) {
                query[i] = translateCursorToQueue(c);
                c.moveToNext();
            }
            return query;
        } else {
            return null;
        }
    }

    public static Queue[] all(Context context){
        //Query database SELECT * FROM queue;
        SQLiteDatabase db = getReadSQLiteDB(context);
        Cursor c = db.rawQuery("SELECT * FROM " + QueueEntry.TABLE_NAME + ";", null);
        if(c.getCount()>0) {
            c.moveToFirst();
            Queue[] query = new Queue[c.getCount()];
            for (int i = 0; i < c.getCount(); i++) {
                query[i] = translateCursorToQueue(c);
                c.moveToNext();
            }
            return query;
        } else {
            return null;
        }
    }

    // ID, NAME, NOTES, MOBILE, QUEUE_NUMBER, QUEUE DATE, SERVICE ID
    // NAME, NOTES, MOBILE, QUEUE_NUMBER, QUEUE_DATE, SERVICE_ID, ID

    private static Queue translateCursorToQueue(Cursor c){
        return new Queue(c.getString(1), c.getString(2), c.getString(3), c.getInt(4), c.getLong(5), c.getLong(6), c.getLong(0));
    }

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + QueueEntry.TABLE_NAME + " ("
            + QueueEntry.COLUMN_NAME_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + QueueEntry.COLUMN_NAME_CNAME + " STRING, "
            + QueueEntry.COLUMN_NAME_NOTES + " TEXT, "
            + QueueEntry.COLUMN_NAME_MOBILE_NUMBER + " STRING, "
            + QueueEntry.COLUMN_NAME_QUEUE_NUMBER + " INTEGER, "
            + QueueEntry.COLUMN_NAME_QUEUE_DATE + " INTEGER, "
            + QueueEntry.COLUMN_NAME_SERVICE_ID + " INTEGER, "
            + "FOREIGN KEY (" + QueueEntry.COLUMN_NAME_SERVICE_ID + ") REFERENCES "
            + Service.ServiceEntry.TABLE_NAME + "("
            + Service.ServiceEntry.COLUMN_NAME_ID + "));";
    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + QueueEntry.TABLE_NAME +";";

    public static abstract class QueueEntry implements BaseColumns{
        public static final String TABLE_NAME = "queue_table";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_CNAME = "customer_name";
        public static final String COLUMN_NAME_NOTES = "notes";
        public static final String COLUMN_NAME_MOBILE_NUMBER = "mobile_number";
        public static final String COLUMN_NAME_QUEUE_NUMBER = "queue_number";
        public static final String COLUMN_NAME_QUEUE_DATE = "queue_date";
        public static final String COLUMN_NAME_SERVICE_ID = "service_id";
    }
}

package com.tech.ivant.qapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.tech.ivant.qapp.DBManager;
import com.tech.ivant.qapp.entities.Queue;

/**
 * Created by matthew on 7/22/15.
 */
public class QueueDao {

    public static long save(Queue queue){
        if(find(queue.id) == null) {
            SQLiteDatabase db = DBManager.getWriteDatabase();
            ContentValues values = new ContentValues();
            values.put(QueueEntry.COLUMN_NAME_CNAME, queue.customerName);
            values.put(QueueEntry.COLUMN_NAME_NOTES, queue.notes);
            values.put(QueueEntry.COLUMN_NAME_MOBILE_NUMBER, queue.mobileNumber);
            values.put(QueueEntry.COLUMN_NAME_SERVICE_ID, queue.service_id);
            values.put(QueueEntry.COLUMN_NAME_QUEUE_DATE, queue.queueDate);
            values.put(QueueEntry.COLUMN_NAME_QUEUE_NUMBER, queue.queueNumber);
            queue.id = db.insert(QueueEntry.TABLE_NAME, null, values);
        } else {
            update(queue);
        }
        return queue.id;
    }

    public static void update(Queue queue){
        if(find(queue.id) == null){      //Not yet in the database... save
            save(queue);
        } else {
            SQLiteDatabase db = DBManager.getReadDatabase();
            ContentValues values = new ContentValues();
            values.put(QueueEntry.COLUMN_NAME_CNAME, queue.customerName);
            values.put(QueueEntry.COLUMN_NAME_NOTES, queue.notes);
            values.put(QueueEntry.COLUMN_NAME_MOBILE_NUMBER, queue.mobileNumber);
            values.put(QueueEntry.COLUMN_NAME_SERVICE_ID, queue.service_id);
            values.put(QueueEntry.COLUMN_NAME_QUEUE_DATE, queue.queueDate);
            values.put(QueueEntry.COLUMN_NAME_QUEUE_NUMBER, queue.queueNumber);
            db.update(QueueEntry.TABLE_NAME, values, QueueEntry.COLUMN_NAME_ID + " = " + queue.id, null);
        }
    }

    public static void delete(Queue queue){
        SQLiteDatabase db = DBManager.getReadDatabase();
        db.delete(QueueEntry.TABLE_NAME, QueueEntry.COLUMN_NAME_ID + " = " + queue.id, null);
    }

    public static Queue find(long id){
        //Query database SELECT * FROM queue WHERE id = 'id;
        SQLiteDatabase db = DBManager.getReadDatabase();
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

    public static Queue[] where(String columnname, String value){
        //Query database SELECT * FROM queue WHERE columnname = value;
        SQLiteDatabase db = DBManager.getReadDatabase();
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
            return new Queue[0];
        }
    }

    public static Queue[] where(String columnname, long value){
        return where(columnname, Long.toString(value));
    }

    public static Queue[] all(){
        //Query database SELECT * FROM queue;
        SQLiteDatabase db = DBManager.getReadDatabase();
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
            return new Queue[0];
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
                    + ServiceDao.ServiceEntry.TABLE_NAME + "("
                    + ServiceDao.ServiceEntry.COLUMN_NAME_ID + "));";
    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + QueueEntry.TABLE_NAME +";";

    public static abstract class QueueEntry implements BaseColumns {
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

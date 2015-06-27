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
    public long id;

    public Queue(){
    }

    public Queue(String customerName, String notes) {
        this.customerName = customerName;
        this.notes = notes;
        this.id = -1;
    }

    public Queue(String customerName, String notes, long id) {
        this.customerName = customerName;
        this.notes = notes;
        this.id = id;
    }

    public void save(Context context){
        //Check for identical entry, then save if unique

        if(id>0) {
            SQLiteDatabase db = getWriteSQLiteDB(context);
            ContentValues values = new ContentValues();
            values.put(QueueEntry.COLUMN_NAME_CNAME, this.customerName);
            values.put(QueueEntry.COLUMN_NAME_NOTES, this.notes);
            this.id = db.insert(QueueEntry.TABLE_NAME, null, values);
        }
        else{
            update(context);
        }
    }

    public void update(Context context){
        if(id<=0){
            save(context);
        }else{
            SQLiteDatabase db = getReadSQLiteDB(context);
            ContentValues values = new ContentValues();
            values.put(QueueEntry.COLUMN_NAME_CNAME, this.customerName);
            values.put(QueueEntry.COLUMN_NAME_NOTES, this.notes);
            db.update(QueueEntry.TABLE_NAME, values, QueueEntry.COLUMN_NAME_ID + " = " + this.id, null);
        }
    }

    public void delete(Context context){
        SQLiteDatabase db = getReadSQLiteDB(context);
        db.delete(QueueEntry.TABLE_NAME, QueueEntry.COLUMN_NAME_ID + " = " + this.id, null);
    }

    public static SQLiteDatabase getReadSQLiteDB(Context context){
        QueueDBHelper qdbhelper = new QueueDBHelper(context);
        return qdbhelper.getReadableDatabase();
    }

    public static SQLiteDatabase getWriteSQLiteDB(Context context){
        QueueDBHelper qdbhelper = new QueueDBHelper(context);
        return qdbhelper.getWritableDatabase();
    }

    public static Queue find(Context context, int id){
        //Query database SELECT * FROM queue WHERE id = 'id;
        SQLiteDatabase db = getReadSQLiteDB(context);
        Cursor c = db.rawQuery("SELECT * FROM " + QueueEntry.TABLE_NAME
                + " WHERE " + QueueEntry.COLUMN_NAME_ID + " = "
                + id + ";", null);
        if(c.getCount()>0) {
            c.moveToFirst();
            return new Queue(c.getString(1), c.getString(2));
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
                query[i] = new Queue(c.getString(1), c.getString(2), c.getLong(0));
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
                query[i] = new Queue(c.getString(1), c.getString(2), c.getLong(0));
                c.moveToNext();
            }
            return query;
        } else {
            return null;
        }
    }

    public static class QueueDBHelper extends SQLiteOpenHelper{

        //QUEUE TABLE
        // ID | NAME | NOTES | SERVICENAME

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + QueueEntry.TABLE_NAME + " ("
                + QueueEntry.COLUMN_NAME_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + QueueEntry.COLUMN_NAME_CNAME + " STRING, "
                + QueueEntry.COLUMN_NAME_NOTES + " TEXT, "
                + QueueEntry.COLUMN_NAME_SERVICE_ID + " INTEGER, "
                + "FOREIGN KEY(" + QueueEntry.COLUMN_NAME_SERVICE_ID + ") REFERENCES "
                + Service.ServiceEntry.TABLE_NAME + "("
                + Service.ServiceEntry.COLUMN_NAME_ID + "));";
        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + QueueEntry.TABLE_NAME +";";

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "queue_app.db";

        public QueueDBHelper(Context context){
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

    public static abstract class QueueEntry implements BaseColumns{
        public static final String TABLE_NAME = "queue";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_CNAME = "customer_name";
        public static final String COLUMN_NAME_NOTES = "notes";
        public static final String COLUMN_NAME_SERVICE_ID = "service_id";
    }
}

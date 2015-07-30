package com.tech.ivant.qapp.dao.records;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.tech.ivant.qapp.DBManager;
import com.tech.ivant.qapp.dao.ServiceDao;
import com.tech.ivant.qapp.entities.records.TotalQueue;

/**
 * Created by matthew on 7/21/15.
 */
public class TotalQueueDao {

    public static long save(TotalQueue totalQueue){
        if(find(totalQueue.id)==null){
            SQLiteDatabase db = DBManager.getWriteDatabase();
            ContentValues values = new ContentValues();
            values.put(TotalQueueEntry.COLUMN_NAME_ID, totalQueue.id);
            values.put(TotalQueueEntry.COLUMN_NAME_DAY, totalQueue.day);
            values.put(TotalQueueEntry.COLUMN_NAME_MONTH, totalQueue.month);
            values.put(TotalQueueEntry.COLUMN_NAME_YEAR, totalQueue.year);
            values.put(TotalQueueEntry.COLUMN_NAME_TOTAL, totalQueue.total);
            totalQueue.id = db.insert(TotalQueueEntry.TABLE_NAME, null, values);
            db.close();
        } else {
            update(totalQueue);
        }
        return totalQueue.id;
    }

    public static void update(TotalQueue totalQueue){
        if(find(totalQueue.id)==null){
            save(totalQueue);
        } else {
            SQLiteDatabase db = DBManager.getReadDatabase();
            ContentValues values = new ContentValues();
            values.put(TotalQueueEntry.COLUMN_NAME_ID, totalQueue.id);
            values.put(TotalQueueEntry.COLUMN_NAME_DAY, totalQueue.day);
            values.put(TotalQueueEntry.COLUMN_NAME_MONTH, totalQueue.month);
            values.put(TotalQueueEntry.COLUMN_NAME_YEAR, totalQueue.year);
            values.put(TotalQueueEntry.COLUMN_NAME_TOTAL, totalQueue.total);
            values.put(TotalQueueEntry.COLUMN_NAME_SERVICE_ID, totalQueue.serviceId);
            db.update(TotalQueueEntry.TABLE_NAME, values, TotalQueueEntry.COLUMN_NAME_ID + " = " + totalQueue.id, null);
            db.close();
        }
    }

    public static void delete(TotalQueue totalQueue){
        SQLiteDatabase db = DBManager.getReadDatabase();
        db.delete(TotalQueueEntry.TABLE_NAME, TotalQueueEntry.COLUMN_NAME_ID + " = " + totalQueue.id, null);
        db.close();
    }

    public static TotalQueue find(long id){
        SQLiteDatabase db = DBManager.getReadDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TotalQueueEntry.TABLE_NAME
                + " WHERE " + TotalQueueEntry.COLUMN_NAME_ID + " = " + id + ";", null);
        TotalQueue retval = null;
        if(c.getCount()>0){
            c.moveToFirst();
            retval = translateCursor(c);
        }

        c.close();
        db.close();
        return retval;
    }

    public static TotalQueue[] where(String column, String value){
        SQLiteDatabase db = DBManager.getReadDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TotalQueueEntry.TABLE_NAME
                + " WHERE " + column +" = " + value + ";", null);
        TotalQueue[] retVal = translateCursorList(c);
        c.close();
        db.close();
        return retVal;
    }

    public static TotalQueue[] where(String[] columns, String[] values){
        if( columns.length != values.length ) return new TotalQueue[0];

        SQLiteDatabase db = DBManager.getReadDatabase();
        StringBuilder query =  new StringBuilder();
        query.append("SELECT * FROM ").append(TotalQueueEntry.TABLE_NAME).append(" WHERE ");

        for(int i=0;i<columns.length;i++){
            query.append(columns[i]).append(" = ").append(values[i]);
            if(i!=columns.length-1){
                query.append(" AND ");
            } else {
                query.append(";");
            }
        }

        Cursor c = db.rawQuery(query.toString(), null);
        TotalQueue[] retVal = translateCursorList(c);
        c.close();
        db.close();
        return retVal;
    }

    public static TotalQueue[] all(){
        SQLiteDatabase db = DBManager.getReadDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TotalQueueEntry.TABLE_NAME + ";", null);
        TotalQueue[] retVal = translateCursorList(c);
        c.close();
        db.close();
        return retVal;
    }

    private static TotalQueue[] translateCursorList(Cursor c){
        if(c.getCount()>0){
            c.moveToFirst();
            TotalQueue[] query = new TotalQueue[c.getCount()];
            for(int i=0; i<c.getCount() ; i++){
                query[i] = translateCursor(c);
                c.moveToNext();
            }
            return query;
        } else {
            return new TotalQueue[0];
        }
    }

    private static TotalQueue translateCursor(Cursor c){
        return new TotalQueue(c.getLong(0), c.getInt(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getLong(5));
    }

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TotalQueueEntry.TABLE_NAME + " ("
                    + TotalQueueEntry.COLUMN_NAME_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + TotalQueueEntry.COLUMN_NAME_DAY + " INTEGER, "
                    + TotalQueueEntry.COLUMN_NAME_MONTH + " INTEGER, "
                    + TotalQueueEntry.COLUMN_NAME_YEAR + " INTEGER, "
                    + TotalQueueEntry.COLUMN_NAME_TOTAL + " INTEGER, "
                    + TotalQueueEntry.COLUMN_NAME_SERVICE_ID + " LONG, "
                    + "FOREIGN KEY (" + TotalQueueEntry.COLUMN_NAME_SERVICE_ID + ") REFERENCES "
                    + ServiceDao.ServiceEntry.TABLE_NAME + "("
                    + ServiceDao.ServiceEntry.COLUMN_NAME_ID + "));";
    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TotalQueueEntry.TABLE_NAME +";";

    public abstract class TotalQueueEntry implements BaseColumns{
        public static final String TABLE_NAME = "total_queue_table";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_DAY = "day";
        public static final String COLUMN_NAME_MONTH = "month";
        public static final String COLUMN_NAME_YEAR = "year";
        public static final String COLUMN_NAME_TOTAL = "total";
        public static final String COLUMN_NAME_SERVICE_ID = "service_id";
    }

}

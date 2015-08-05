package com.tech.ivant.qapp.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.tech.ivant.qapp.DBManager;
import com.tech.ivant.qapp.entities.Report;

/**
 * Created by matthew on 7/21/15.
 *
 * Database Access Object for Reports
 *
 * Table name: records_table
 * Fields (Columns):
 *  -id
 *  -day
 *  -month
 *  -year
 *  -total
 *  -ave_waiting
 *  -service_id (foreign key)
 *
 */
public class ReportDao {

    //Insert an entry to the database
    public static long save(Report report){
        if(find(report.id)==null){  //Check if entry is not in the database
            SQLiteDatabase db = DBManager.getWriteDatabase();

            ContentValues values = new ContentValues();
            values.put(TotalQueueEntry.COLUMN_NAME_DAY, report.day);
            values.put(TotalQueueEntry.COLUMN_NAME_MONTH, report.month);
            values.put(TotalQueueEntry.COLUMN_NAME_YEAR, report.year);
            values.put(TotalQueueEntry.COLUMN_NAME_TOTAL, report.total);
            values.put(TotalQueueEntry.COLUMN_NAME_SUM_AVE_WAIT, report.sumAverageWait);

            //insert entry
            report.id = db.insert(TotalQueueEntry.TABLE_NAME, null, values);
            db.close();
        } else {        //update instead if there is an entry in the database already
            update(report);
        }
        return report.id;
    }

    //Update an entry in the database
    public static void update(Report report){
        if(find(report.id)==null){      //check if entry is already on the base
            //save instead if there is no entry
            save(report);
        } else {
            SQLiteDatabase db = DBManager.getReadDatabase();

            ContentValues values = new ContentValues();
            values.put(TotalQueueEntry.COLUMN_NAME_ID, report.id);
            values.put(TotalQueueEntry.COLUMN_NAME_DAY, report.day);
            values.put(TotalQueueEntry.COLUMN_NAME_MONTH, report.month);
            values.put(TotalQueueEntry.COLUMN_NAME_YEAR, report.year);
            values.put(TotalQueueEntry.COLUMN_NAME_TOTAL, report.total);
            values.put(TotalQueueEntry.COLUMN_NAME_SUM_AVE_WAIT, report.sumAverageWait);
            values.put(TotalQueueEntry.COLUMN_NAME_SERVICE_ID, report.serviceId);

            //update entry in database
            db.update(TotalQueueEntry.TABLE_NAME, values, TotalQueueEntry.COLUMN_NAME_ID + " = " + report.id, null);
            db.close();
        }
    }

    //Delete an entry in the database
    public static int delete(Report report){
        SQLiteDatabase db = DBManager.getReadDatabase();
        int returnValue = db.delete(TotalQueueEntry.TABLE_NAME, TotalQueueEntry.COLUMN_NAME_ID + " = " + report.id, null);
        db.close();
        return returnValue;
    }

    //Query using an id. Returns a null value if nothing is found
    public static Report find(long id){
        SQLiteDatabase db = DBManager.getReadDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TotalQueueEntry.TABLE_NAME
                + " WHERE " + TotalQueueEntry.COLUMN_NAME_ID + " = " + id + ";", null);
        Report retval = null;
        if(c.getCount()>0){
            c.moveToFirst();
            retval = translateCursor(c);
        }

        c.close();
        db.close();
        return retval;
    }

    //Query using a WHERE clause. Returns an empty Report array if nothing is found.
    public static Report[] where(String column, String value){
        SQLiteDatabase db = DBManager.getReadDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TotalQueueEntry.TABLE_NAME
                + " WHERE " + column +" = " + value + ";", null);
        Report[] retVal = translateCursorList(c);
        c.close();
        db.close();
        return retVal;
    }

    //Query using a WHERE clause with multiple condition. Returns an empty array if nothing is found
    public static Report[] where(String[] columns, String[] values){
        if( columns.length != values.length ) return new Report[0];

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
        Report[] retVal = translateCursorList(c);
        c.close();
        db.close();
        return retVal;
    }

    //Query all entry in the database
    public static Report[] all(){
        SQLiteDatabase db = DBManager.getReadDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TotalQueueEntry.TABLE_NAME + ";", null);
        Report[] retVal = translateCursorList(c);
        c.close();
        db.close();
        return retVal;
    }

    //Translation of Cursor object to an array of Report object
    private static Report[] translateCursorList(Cursor c){
        if(c.getCount()>0){
            c.moveToFirst();
            Report[] query = new Report[c.getCount()];
            for(int i=0; i<c.getCount() ; i++){
                query[i] = translateCursor(c);
                c.moveToNext();
            }
            return query;
        } else {
            return new Report[0];
        }
    }

    //Translation of a Cursor object to Report object
    private static Report translateCursor(Cursor c){
        return new Report(c.getLong(0), c.getInt(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getLong(5), c.getLong(6));
    }

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TotalQueueEntry.TABLE_NAME + " ("
                    + TotalQueueEntry.COLUMN_NAME_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + TotalQueueEntry.COLUMN_NAME_DAY + " INTEGER, "
                    + TotalQueueEntry.COLUMN_NAME_MONTH + " INTEGER, "
                    + TotalQueueEntry.COLUMN_NAME_YEAR + " INTEGER, "
                    + TotalQueueEntry.COLUMN_NAME_TOTAL + " INTEGER, "
                    + TotalQueueEntry.COLUMN_NAME_SUM_AVE_WAIT + " LONG, "
                    + TotalQueueEntry.COLUMN_NAME_SERVICE_ID + " LONG, "
                    + "FOREIGN KEY (" + TotalQueueEntry.COLUMN_NAME_SERVICE_ID + ") REFERENCES "
                    + ServiceDao.ServiceEntry.TABLE_NAME + "("
                    + ServiceDao.ServiceEntry.COLUMN_NAME_ID + "));";
    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TotalQueueEntry.TABLE_NAME +";";

    public abstract class TotalQueueEntry implements BaseColumns{
        public static final String TABLE_NAME = "records_table";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_DAY = "day";
        public static final String COLUMN_NAME_MONTH = "month";
        public static final String COLUMN_NAME_YEAR = "year";
        public static final String COLUMN_NAME_TOTAL = "total";
        public static final String COLUMN_NAME_SUM_AVE_WAIT = "ave_waiting";
        public static final String COLUMN_NAME_SERVICE_ID = "service_id";
    }

}

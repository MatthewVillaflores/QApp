package com.tech.ivant.qapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by matthew on 6/29/15.
 */
public class DBManager extends SQLiteOpenHelper{

    public final static String DATABASE_NAME = "queue_app.db";
    public final static int DATABASE_VERSION = 1;

    public static DBManager dbManager;

    public static void initializeDB(Context context){
        dbManager = new DBManager(context);
    }

    public DBManager(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Service.SQL_CREATE_TABLE);
        db.execSQL(Queue.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Service.SQL_DELETE_TABLE);
        db.execSQL(Queue.SQL_DELETE_TABLE);
        onCreate(db);
    }

    public static SQLiteDatabase getReadDatabase(){
        return dbManager.getReadableDatabase();
    }

    public static SQLiteDatabase getWriteDatabase(){
        return dbManager.getWritableDatabase();
    }

}

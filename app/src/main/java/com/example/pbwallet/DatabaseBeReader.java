package com.example.pbwallet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseBeReader {
    SQLiteDatabase mDb;
    DatabaseHelper mDbHelper;
    Context mContext;
    private static final String DB_NAME = "bereader_db1";
    private static final int DB_VERSION = 1;

    public DatabaseBeReader(Context ctx) {
        mContext = ctx;
        mDbHelper = new DatabaseHelper(ctx, DB_NAME, null, DB_VERSION);
    }

    public void open()
    {
        mDb = mDbHelper.getWritableDatabase();
    }
    public void close()
    {
        mDb.close();
    }
    //inserimento values in table da modificare per ogni tabella
    public void inserisci(String name){
        ContentValues cv = new ContentValues();
        cv.put("name",name);
        mDb.insert("user",null,cv);
    }
    //query da modificare per ogni caso
    public Cursor query(String str){
        return mDb.query("user",null,null/*"name"+"='"+str+"'"*/,null,null,null,null);
    }


}
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
    public void insertUser(String name, String surname, String birthday, String username, String password, String hobby, int idcard, int idbudget){
        ContentValues cv = new ContentValues();
        cv.put("name",name);
        cv.put("surname",surname);
        cv.put("birthday", birthday);
        cv.put("username",username);
        cv.put("password",password);
        cv.put("hobby",hobby);
        cv.put("idcard",idcard);
        cv.put("idbudget",idbudget);
        mDb.insert("user",null,cv);
    }

    public void insertCard(int idcard, int numcard, String uscard, double money, int idtrans){
        ContentValues cv = new ContentValues();
        cv.put("idcard",idcard);
        cv.put("numcard",numcard);
        cv.put("uscard",uscard);
        cv.put("money",money);
        cv.put("idtrans",idtrans);
        mDb.insert("card",null,cv);
    }

    public void insertTrans(int idtrans,int idsubtype,double money,String date){
        ContentValues cv = new ContentValues();
        cv.put("idtrans",idtrans);
        cv.put("idsubtype",idsubtype);
        cv.put("money",money);
        cv.put("date",date);
        mDb.insert("trans",null,cv);
    }

    public void insertBudget(int idbudget, double bound, int idsubtype){
        ContentValues cv = new ContentValues();
        cv.put("idbudget",idbudget);
        cv.put("bound",bound);
        cv.put("idsubtype",idsubtype);
        mDb.insert("budget",null,cv);
    }

    public void insertSubtype(int idsubtype, int idtype, String name){
        ContentValues cv = new ContentValues();
        cv.put("idsubtype",idsubtype);
        cv.put("idtype",idtype);
        cv.put("name",name);
        mDb.insert("subtype",null,cv);
    }

    public void insertType(int idtype, String name, String image){
        ContentValues cv = new ContentValues();
        cv.put("idtype",idtype);
        cv.put("name",name);
        cv.put("image",image);
        mDb.insert("type",null,cv);
    }

    //query da modificare per ogni caso
    public Cursor query(String str){
        return mDb.query("user",null,null/*"name"+"='"+str+"'"*/,null,null,null,null);
    }


}
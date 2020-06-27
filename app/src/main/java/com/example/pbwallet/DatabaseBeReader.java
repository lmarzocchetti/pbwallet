package com.example.pbwallet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

public class DatabaseBeReader {
    SQLiteDatabase mDb;
    DatabaseHelper mDbHelper;
    Context mContext;
    private static final String DB_NAME = "bereader_db";
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
    public void insertUser(String name, String surname, String username, String password, String hobby, Integer idcard, Integer idbudget){
        ContentValues cv = new ContentValues();
        cv.put("name",name);
        cv.put("surname",surname);
        cv.put("username",username);
        cv.put("password",password);
        cv.put("hobby",hobby);
        cv.put("idcard",idcard);
        cv.put("idbudget",idbudget);
        mDb.insert("user",null,cv);
    }

    public void insertCard(Integer idcard, Integer numcard, String uscard, double money){
        ContentValues cv = new ContentValues();
        cv.put("idcard",idcard);
        cv.put("numcard",numcard);
        cv.put("uscard",uscard);
        cv.put("money",money);
        mDb.insert("card",null,cv);
    }

    public void insertTrans(Integer idtrans, Integer idcard, Integer idsubtype, double money, String date){
        ContentValues cv = new ContentValues();
        cv.put("idtrans",idtrans);
        cv.put("idcard",idcard);
        cv.put("idsubtype",idsubtype);
        cv.put("money",money);
        cv.put("date", date);
        mDb.insert("trans",null,cv);
    }

    public void insertBudget(Integer idbudget, double bound, Integer idsubtype){
        ContentValues cv = new ContentValues();
        cv.put("idbudget",idbudget);
        cv.put("bound",bound);
        cv.put("idsubtype",idsubtype);
    }

    public void insertSubtype(Integer idsubtype, Integer idtype, String name){
        ContentValues cv = new ContentValues();
        cv.put("idsubtype",idsubtype);
        cv.put("idtype",idtype);
        cv.put("name",name);
        mDb.insert("subtype",null,cv);
    }

    public void insertType(Integer idtype, String name){
        ContentValues cv = new ContentValues();
        cv.put("idtype",idtype);
        cv.put("name",name);
        mDb.insert("type",null,cv);
    }

    //query da modificare per ogni caso
    public Cursor queryUser(String ricerca, String values){
        return mDb.query("user",null,ricerca+"='"+values+"'",null,null,null,null);
    }

    public Cursor queryUserFull(){
        return mDb.query("user",null,null,null,null,null,null);
    }

    public Cursor queryCard(String ricerca, String values){
        return mDb.query("card",null,ricerca+"='"+values+"'",null,null,null,null);
    }

    public Cursor queryCardFull(){
        return mDb.query("card",null,null,null,null,null,null);
    }

    public Cursor queryTrans(String ricerca, String values){
        return mDb.query("trans",null,ricerca+"='"+values+"'",null,null,null,null);
    }

    public Cursor queryTransDate(String ricerca, String values){
        return mDb.query("trans",null,ricerca+" like '"+values+"%'",null,null,null,null);
    }

    public Cursor queryTransFull(){
        return mDb.query("trans",null,null,null,null,null,null);
    }

    public Cursor queryBudget(String ricerca, String values){
        return mDb.query("budget",null,ricerca+"='"+values+"'",null,null,null,null);
    }

    public Cursor querySubtype(String ricerca, String values){
        return mDb.query("subtype",null,ricerca+"='"+values+"'",null,null,null,null);
    }

    public Cursor queryType(String ricerca, String values){
        return mDb.query("type",null,ricerca+"='"+values+"'",null,null,null,null);
    }

    public Cursor queryLastTrans(){
        return mDb.query("trans",null,null,null,null,null,"datetime(date) DESC");
    }

    public Cursor queryLastTransCard(int card) {
        return mDb.query("card c, trans t", null, "c.idcard = t.idcard and t.idcard ="+card, null, null, null, "datetime(t.date) DESC");
    }

    public Cursor querySubtype(int idtrans) {
        return mDb.query("subtype s, trans t", null, "s.idsubtype = t.idsubtype and t.idtrans ="+idtrans, null, null, null, null);
    }
  
    public Cursor queryUsCard(){
        return mDb.query("card c, trans t", null,"c.idcard = t.idcard", null,null,null,"datetime(date) DESC");
    }

    public Cursor querySubtypeFull(){
        return mDb.query("trans t, subtype s", null, "s.idsubtype = t.idsubtype",null,null,null,"datetime(date) DESC");
    }

    public Cursor queryTransDist(){
        return mDb.query(true,"trans",null,null,null,null,null,"datetime(date) DESC", null);
    }
}
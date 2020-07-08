package com.example.pbwallet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * This class manages all the queries and inserts and updates of the database
 */
public class DatabaseBeReader {
    SQLiteDatabase mDb;
    DatabaseHelper mDbHelper;
    Context mContext;
    static final String DB_NAME = "bereader_db";
    private static final int DB_VERSION = 1;

    /**
     * Constructor that initialized the database by calling DatabaseHelper
     * @param ctx context
     */
    public DatabaseBeReader(Context ctx) {
        mContext = ctx;
        try {
            mDbHelper = new DatabaseHelper(ctx, DB_NAME, null, DB_VERSION);
        }
        catch (SQLException e) {
            Log.d("pbwallet", "Exception: " + Log.getStackTraceString(e));
        }
    }

    /**
     * This method establishes a connection whit the database
     */
    public void open()
    {
        try {
            mDb = mDbHelper.getWritableDatabase();
        }
        catch (SQLiteException e) {
            Log.d("pbwallet", "Exception: " + Log.getStackTraceString(e));
            throw new SQLiteException();
        }
    }

    /**
     * This method ends a connection whit the database
     */
    public void close()
    {
        mDb.close();
    }

    /**
     *  This method inserts a tuple
     * @param name String indicating the name of the user
     * @param surname String indicating the surname of the user
     * @param username String indicating the username of the user
     * @param password String indicating the password of the user
     * @param currency String indicating the currency chosen by the user
     */
    public void insertUser(String name, String surname, String username, String password, String currency) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("surname", surname);
        cv.put("username", username);
        cv.put("password", password);
        cv.put("currency", currency);
      
        try {
            mDb.insertOrThrow("user", null, cv);
        } catch (SQLiteException e) {
            Log.d("pbwallet", "Exception: " + Log.getStackTraceString(e));
            throw new SQLiteException();
        }
    }

    /**
     *  This method inserts a tuple
     * @param idcard Integer indicating the id of the card
     * @param uscard String indicating the name of the card
     * @param money double indicating the money contained in the card
     */
    public void insertCard(Integer idcard, String uscard, double money){
        ContentValues cv = new ContentValues();
        cv.put("idcard",idcard);
        cv.put("uscard",uscard);
        cv.put("money",money);
      
        try {
            mDb.insertOrThrow("card", null, cv);
        } catch (SQLiteException e) {
            Log.d("pbwallet", "Exception: " + Log.getStackTraceString(e));
            throw new SQLiteException();
        }
    }

       /**
       *  This method inserts a tuple
       *  @param idtrans Integer indicating the id of the transaction
       *  @param idcard Integer indicating the id of the card where the transaction took place
       *  @param idsubtype Integer indicating the id of the subtype to which the transaction is connected
       * @param money double indicating the money of the transaction
       * @param date String indicating the date on which the transaction occurred
        */
    public void insertTrans(Integer idtrans, Integer idcard, Integer idsubtype, double money, String date){
        ContentValues cv = new ContentValues();
        cv.put("idtrans",idtrans);
        cv.put("idcard",idcard);
        cv.put("idsubtype",idsubtype);
        cv.put("money",money);
        cv.put("date", date);

        try {
            mDb.insertOrThrow("trans", null, cv);
        } catch (SQLiteException e) {
            Log.d("pbwallet", "Exception: " + Log.getStackTraceString(e));
            throw new SQLiteException();
        }
    }

    /**
     *  This method inserts a tuple
     *  @param idbudget Integer indicating the id of the budget
     *  @param bound double indicating the limit of the budget
     *  @param idsubtype Integer indicating the id of the subtype to which the budget is connected
     *  @param money double indicating the money of the budget
     *  @param date String indicating the budget end date
     */
    public void insertBudget(Integer idbudget, double money ,double bound, String date ,Integer idsubtype){
        ContentValues cv = new ContentValues();
        cv.put("idbudget",idbudget);
        cv.put("money", money);
        cv.put("bound",bound);
        cv.put("date", date);
        cv.put("idsubtype",idsubtype);
        try {
            mDb.insertOrThrow("budget", null, cv);
        } catch (SQLiteException e) {
            Log.d("pbwallet", "Exception: " + Log.getStackTraceString(e));
            throw new SQLiteException();
        }
    }

    /**
     *  This method inserts a tuple
     *  @param idsubtype Integer indicating the id of the subtype
     *  @param idtype Integer indicating the id of the type to which the subtype is connected
     *  @param name String indicating the name of the subtype
     */
    public void insertSubtype(Integer idsubtype, Integer idtype, String name){
        ContentValues cv = new ContentValues();
        cv.put("idsubtype",idsubtype);
        cv.put("idtype",idtype);
        cv.put("name",name);
      
        try {
            mDb.insertOrThrow("subtype", null, cv);
        } catch (SQLiteException e) {
            Log.d("pbwallet", "Exception: " + Log.getStackTraceString(e));
            throw new SQLiteException();
        }
    }

    /**
     * This method inserts a tuple
     * @param idtype Integer indicating the id of the type
     * @param name String indicating the name of the type
     */
    public void insertType(Integer idtype, String name) {
        ContentValues cv = new ContentValues();
        cv.put("idtype",idtype);
        cv.put("name",name);

        try {
            mDb.insertOrThrow("type", null, cv);
        } catch (SQLiteException e) {
            Log.d("pbwallet", "Exception: " + Log.getStackTraceString(e));
            throw new SQLiteException();
        }
    }

    /**
     * This method deletes a tuple of a budget
     * @param idbudget int indicating the id of budget
     */
    public void deleteBudget(int idbudget) {
        mDb.delete("budget", "idbudget="+idbudget, null);
    }

    /**
     * This method updates the money of the cards
     * @param newmoney double indicating the money to be replaced
     * @param modidcard int indicating the id of the card
     */
    public void updateMoneyCard(double newmoney, int modidcard) {
        ContentValues newValues = new ContentValues();
        newValues.put("money", newmoney);
        mDb.update("card", newValues, "idcard="+modidcard, null);
    }

    /**
     * This method updates the money of the budget
     * @param newmoney double indicating the money to be replaced
     * @param idbudget int indicating the id of the budget
     */
    public void updateBudget (double newmoney, int idbudget) {
        ContentValues newValues = new ContentValues();
        newValues.put("money", newmoney);
        mDb.update("budget", newValues, "idbudget="+idbudget, null);
    }

    /**
     * This method does the general query of user
     * @param ricerca String indicating the column to search
     * @param values String indicating which value the search should match
     */
    public Cursor queryUser(String ricerca, String values){
        return mDb.query("user",null,ricerca+"='"+values+"'",null,null,null,null);
    }

    /**
     * This method does the full query of user
     */
    public Cursor queryUserFull(){
        return mDb.query("user",null,null,null,null,null,null);
    }

    /**
     * This method does the general query of card
     * @param ricerca String indicating the column to search
     * @param values String indicating which value the search should match
     */
    public Cursor queryCard(String ricerca, String values){
        return mDb.query("card",null,ricerca+"='"+values+"'",null,null,null,null);
    }

    /**
     * This method does the full query of card
     */
    public Cursor queryCardFull(){
        return mDb.query("card",null,null,null,null,null,null);
    }

    /**
     * This method does the full query of budget
     */
    public Cursor queryBudgetFull() {
        return mDb.query("budget", null, null, null, null, null, null);
    }

    /**
     * This method does the general query of trans by date
     * @param ricerca String indicating the column to search
     * @param values String indicating which value the search should match
     */
    public Cursor queryTransDate(String ricerca, String values){
        return mDb.query("trans",null,ricerca+" like '"+values+"%'",null,null,null,null);
    }

    /**
     * This method does the general query of budget by subtype
     * @param idsubtype int indicating the id of the subtype
     */
    public Cursor queryBudgetbySubtype(int idsubtype){
        return mDb.query("budget", null, "idsubtype="+idsubtype, null, null, null, null);
    }

    /**
     * This method does the general query of budget by idbudget
     * @param idbudget int indicating the id of the budget
     */
    public Cursor queryBudgetbyIdbudget(int idbudget) {
        return mDb.query("budget", null, "idbudget="+idbudget, null, null, null, null);
    }

    /**
     * This method does the general query of subtype
     * @param ricerca String indicating the column to search
     * @param values String indicating which value the search should match
     */
    public Cursor querySubtype(String ricerca, String values){
        return mDb.query("subtype",null,ricerca+"='"+values+"'",null,null,null,null);
    }

    /**
     * This method does the general query of type
     * @param ricerca String indicating the column to search
     * @param values String indicating which value the search should match
     */
    public Cursor queryType(String ricerca, String values){
        return mDb.query("type",null,ricerca+"='"+values+"'",null,null,null,null);
    }

    /**
     * This method does the full query of type
     */
    public Cursor queryTypeFull() {
        return mDb.query("type", null, null, null, null, null, null);
    }

    /**
     * This method does the query of trans by last date
     */
    public Cursor queryLastTrans(){
        return mDb.query("trans",null,null,null,null,null,"datetime(date) DESC");
    }

    /**
     * This method does the query of card by last date
     * @param idcard int indicating the id of the card
     */
    public Cursor queryLastTransCard(int idcard) {
        return mDb.query("card c, trans t", null, "c.idcard = t.idcard and t.idcard ="+idcard, null, null, null, "datetime(t.date) DESC");
    }

    /**
     * This method does the query of subtype by idtrans
     * @param idtrans int indicating the id of the trans
     */
    public Cursor querySubtype(int idtrans) {
        return mDb.query("subtype s, trans t", null, "s.idsubtype = t.idsubtype and t.idtrans ="+idtrans, null, null, null, null);
    }

    /**
     * This method does the query of name of the card by last date
     */
    public Cursor queryUsCard(){
        return mDb.query("card c, trans t", null,"c.idcard = t.idcard", null,null,null,"datetime(date) DESC");
    }

    /**
     * This method does the full query of subtype
     */
    public Cursor queryOnlySubType() {
        return mDb.query("subtype", null, null, null, null, null, null, null);
    }

    /**
     * This method does the query of subtype by last date
     */
    public Cursor querySubtypeFull(){
        return mDb.query("trans t, subtype s", null, "s.idsubtype = t.idsubtype",null,null,null,"datetime(date) DESC");
    }

    /**
     * This method does the query of distinct trans by last date
     */
    public Cursor queryTransDist(){
        return mDb.query(true,"trans",null,null,null,null,null,"datetime(date) DESC", null);
    }

    /**
     * This method does the query of trans by last id
     */
    public Cursor queryTransbyID() {
        return mDb.query("trans", null, null, null, null, null, "idtrans DESC");
    }

    /**
     * This method does the query of subtype by last id
     */
    public Cursor querySubTypebyID() {
        return mDb.query("subtype", null, null, null, null, null, "idsubtype DESC");
    }

    /**
     * This method does the query of card by last id
     */
    public Cursor queryCardbyID() {
        return mDb.query("card", null, null, null, null, null, "idcard DESC");
    }

    /**
     * This method does the query of budget by last id
     */
    public Cursor queryBudgetbyID() {
        return mDb.query("budget", null, null, null, null, null, "idbudget DESC");
    }

    /**
     * This method does the query of type by idtype
     * @param idtype int indicating the id of the type
     */
    public Cursor queryTypeBySubtype(int idtype){
        return mDb.query("type", null, "idtype ="+idtype, null, null, null, null);
    }

    /**
     * This method does the query of budget by new id
     */
    public Cursor queryBudgetAsc(){
        return mDb.query("budget b", null, null, null, null, null, "idbudget ASC");
    }

    /**
     * This method does the query of subtype by last idbudget
     */
    public Cursor querySubtypeByBudget() {
        return mDb.query("subtype s, budget b", null, "s.idsubtype=b.idsubtype", null, null, null, "idbudget ASC");
    }

    /**
     * This method does the query of subtype by idsubtype
     * @param idsubtype int indicating the id of the subtype
     */
    public Cursor querySubtypeBySubtype(int idsubtype) {
        return mDb.query("subtype", null, "idsubtype="+idsubtype, null, null, null, null);
    }
}
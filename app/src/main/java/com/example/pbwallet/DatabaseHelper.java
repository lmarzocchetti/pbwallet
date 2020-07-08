package com.example.pbwallet;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * This class initializes the database the first time it is created
 * and updates every time the application is started
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * Constructor for DatabaseHelper
     * @param context context
     * @param name name of the database
     * @param factory standard SQLiteCursor
     * @param version version of the database
     */
    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * This method executes SQL code for creating database tables
     */
    @Override
    public void onCreate(SQLiteDatabase db) throws SQLException {
        String create_table_user = "create table user("
                + "name varchar(50) not null,"
                + "surname varchar(30) not null,"
                + "username varchar(30),"
                + "password varchar(30),"
                + "currency varchar(2) not null,"
                + "primary key(username,password));";
        db.execSQL(create_table_user);

        String create_table_card = "create table card("
                + "idcard int,"
                + "uscard varchar(30) not null,"
                + "money double not null,"
                + "primary key(idcard, uscard));";
        db.execSQL(create_table_card);

        String create_table_trans = "create table trans("
                + "idtrans int primary key,"
                + "idcard int not null,"
                + "idsubtype int not null,"
                + "money double not null,"
                + "date datetime not null,"
                + "foreign key(idcard) references card(idcard),"
                + "foreign key(idsubtype) references subtype(idsubtype));";
        db.execSQL(create_table_trans);

        String create_table_budget = "create table budget("
                + "idbudget int primary key,"
                + "money double not null,"
                + "bound double not null,"
                + "date date not null,"
                + "idsubtype int not null,"
                + "foreign key(idsubtype) references subtype(idsubtype));";
        db.execSQL(create_table_budget);

        String create_table_subtype = "create table subtype("
                + "idsubtype int,"
                + "idtype int not null,"
                + "name varchar(30),"
                + "primary key(idsubtype, name),"
                + "foreign key(idtype) references type(idtype));";
        db.execSQL(create_table_subtype);

        String create_table_type = "create table type("
                + "idtype int,"
                + "name varchar(30),"
                + "primary key(idtype, name));";
        db.execSQL(create_table_type);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}

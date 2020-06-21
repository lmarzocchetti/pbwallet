package com.example.pbwallet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //creare tutte le tabelle
    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table_user = "create table user("
                + "name varchar(30) not null,"
                + "surname varchar(30),"
                + "birthday date,"
                + "username varchar(30),"
                + "password varchar(30),"
                + "idcard int);";
        db.execSQL(create_table_user);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }
}

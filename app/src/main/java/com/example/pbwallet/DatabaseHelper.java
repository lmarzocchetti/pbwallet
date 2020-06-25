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
                + "name varchar(50) not null,"
                + "surname varchar(30) not null,"
                + "username varchar(30),"
                + "password varchar(30),"
                + "hobby varchar(30) not null,"
                + "idcard int,"
                + "idbudget int,"
                + "primary key(username,password),"
                + "foreign key(idcard) references card(idcard),"
                + "foreign key(idbudget) references budget(idbudget));";
        db.execSQL(create_table_user);

        String create_table_card = "create table card("
                + "idcard int,"
                + "numcard int,"
                + "uscard varchar(30),"
                + "money double not null,"
                + "primary key(idcard,numcard,uscard));";
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
                + "bound double not null,"
                + "idsubtype int not null,"
                + "foreign key(idsubtype) references subtype(idsubtype));";
        db.execSQL(create_table_budget);

        String create_table_subtype = "create table subtype("
                + "idsubtype int,"
                + "idtype int not null,"
                + "name varchar(30) not null,"
                + "primary key(idsubtype),"
                + "foreign key(idtype) references type(idtype));";
        db.execSQL(create_table_subtype);

        String create_table_type = "create table type("
                + "idtype int,"
                + "name varchar(30) not null,"
                + "primary key(idtype));";
        db.execSQL(create_table_type);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }
}

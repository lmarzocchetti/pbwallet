package com.example.pbwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseBeReader db = new DatabaseBeReader(this);

        try {
            db.open();
            Cursor cur = db.queryUserFull();
            if (cur.moveToFirst()) {
                if (cur.getString(cur.getColumnIndex("password")) == null) {
                    Intent homepage = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(homepage);
                } else {
                    Intent loginpage = new Intent(MainActivity.this, LoginPanel.class);
                    startActivity(loginpage);
                }
            } else {
                Intent signinpage = new Intent(MainActivity.this, SigninPanel.class);
                db.insertType(0, "Lavoro");
                db.insertType(1, "Casa");
                db.insertType(2, "Spesa");
                db.insertType(3, "Hobby");
                db.insertType(4, "Altro");
                startActivity(signinpage);
            }
        }
        catch (SQLiteException e){
            e.printStackTrace();
        }finally {
            db.close();
            finish();
        }
    }
}
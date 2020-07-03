package com.example.pbwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
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
                    controlBudget();
                    startActivity(homepage);
                } else {
                    Intent loginpage = new Intent(MainActivity.this, LoginPanel.class);
                    startActivity(loginpage);
                }
            } else {
                Intent signinpage = new Intent(MainActivity.this, SigninPanel.class);
                db.insertType(0, "Work");
                db.insertType(1, "Home");
                db.insertType(2, "Shopping");
                db.insertType(3, "Hobby");
                db.insertType(4, "Other");
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

    private void controlBudget() {
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();

        Cursor cur = db.queryBudgetFull();
        String tmp = "";

        if(cur.moveToFirst()) {
            do {
                tmp = cur.getString(cur.getColumnIndex("date"));
                if(tmp.equals(java.time.LocalDate.now().toString())) {
                    db.deleteBudget(cur.getInt(cur.getColumnIndex("idbudget")));
                }

            } while(cur.moveToNext());
        }

        db.close();
    }
}
package com.example.pbwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();
        Cursor cur = db.queryUserFull();
        //db.insertSubtype(0, 2, "Caffe");
        //db.insertSubtype(1, 4, "Videogiochi");
        //db.insertSubtype(2, 0, "Computer");
        //db.insertCard(0, 0, "Postepay", 2000);
        //db.insertCard(1, 0, "MasterCard", 1000);
        //db.insertCard(2, 0, "DioCard", 666);
        //db.insertTrans(0, 0, 0, 3, "2020-06-25 13:20:20");
        //db.insertTrans(1, 0, 0, 3, "2020-06-24 13:20:20");
        //db.insertTrans(2, 0, 0, 3, "2020-06-26 13:20:20");
        //db.insertTrans(3, 0, 0, 3, "2020-06-28 13:20:20");
        //db.insertTrans(4, 0, 0, 3, "2020-06-29 13:20:20");
        if(cur.moveToFirst()){
            if(cur.getString(cur.getColumnIndex("password")) == null){
                Intent homepage = new Intent(MainActivity.this, HomeActivity.class);
                db.close();
                startActivity(homepage);
                finish();
            }
            else{
                Intent loginpage = new Intent(MainActivity.this, LoginPanel.class);
                db.close();
                startActivity(loginpage);
                finish();
            }
        }
        else{
            Intent signinpage = new Intent(MainActivity.this, SigninPanel.class);
            db.insertType(0, "Lavoro");
            db.insertType(1, "Casa");
            db.insertType(2, "Spesa");
            db.insertType(3, "Hobby");
            db.insertType(4, "Altro");
            db.close();
            startActivity(signinpage);
            finish();
        }
    }
}
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
        //db.insertCard(0, 0, "Intesa", 0);
        db.insertCard(0, 0, "MasterCard", 0);
        db.insertCard(1, 1, "Visa", 0);
        //db.insertTrans(21, 0, 0, 127, "2020-06-25 13:20:20");
        //db.insertTrans(8, 0, 0, 5, "2020-07-24 13:20:20");
        //db.insertTrans(9, 0, 0, 36, "2020-08-26 13:20:20");
        //db.insertTrans(10, 0, 0, 7, "2020-09-28 13:20:20");
        //db.insertTrans(11, 0, 0, 1237, "2020-10-29 13:20:20");
        /*db.insertCard(1,1,"Visa",3700);
        db.insertCard(2,2,"Mastercard", 7888.8);
        db.insertCard(3,3, "PostePay", 400);*/
        //db.insertTrans(12,1,1,-126,"2019-11-12 12:12:12");
        //db.insertTrans(13,2,1,123,"2019-10-12 12:11:12");
        //db.insertTrans(14,3,1,300,"2019-01-12 12:13:12");
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
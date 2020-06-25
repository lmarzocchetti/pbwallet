package com.example.pbwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();
        Cursor cur = db.queryUserFull();
        /*db.insertCard(1,1,"Visa",3700);
        db.insertCard(2,2,"Mastercard", 7888.8);
        db.insertCard(3,3, "PostePay", 400);
        db.insertTrans(1,1,1,-126,"2019-12-12 12:12:12");
        db.insertTrans(2,2,1,123,"2019-12-12 12:11:12");
        db.insertTrans(3,3,1,300,"2019-12-12 12:13:12");*/
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
            db.close();
            startActivity(signinpage);
            finish();
        }
    }
}
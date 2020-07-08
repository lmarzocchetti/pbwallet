package com.example.pbwallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.media.tv.TvContract;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

/**
 * The MainActivity of this App.
 */
public class MainActivity extends AppCompatActivity {
    static boolean checkbudget = true;

    /**
     * Open the database and control if is the first time opening this app; if this
     * is true then insert 5 Type Tuple and Start the SigninActivity.
     * If the user is already registered then control if he/she setup a password, and respectively
     * start the LoginPanel or the HomeActivity directly(in this case there is also a Control to elapsed budget).
     * @param savedInstanceState saved state for create this activity, in this application is NULL.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();
        Cursor cur = db.queryUserFull();
        if (cur.moveToFirst()) {
            if (cur.getString(cur.getColumnIndex("password")) == null) {
                Intent homepage = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homepage);
                db.close();
                finish();
            } else {
                Intent loginpage = new Intent(MainActivity.this, LoginPanel.class);
                startActivity(loginpage);
                db.close();
                finish();
            }
        } else {
            Intent signinpage = new Intent(MainActivity.this, SigninPanel.class);
            db.insertType(0, "Work");
            db.insertType(1, "Home");
            db.insertType(2, "Shopping");
            db.insertType(3, "Hobby");
            db.insertType(4, "Other");
            startActivity(signinpage);
            db.close();
            finish();
        }
    }

    /**
     * If this method is called, there is a serious problem with the database,
     * so create a toast to notificate the user, delete all database and restart the app.
     */
    private void databaseError() {
        Toast t = Toast.makeText(this, "Database error\nDeleting database...", Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
        getApplicationContext().deleteDatabase(DatabaseBeReader.DB_NAME);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }, 3000);
    }
}
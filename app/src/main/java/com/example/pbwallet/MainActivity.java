package com.example.pbwallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.media.tv.TvContract;
import android.os.Build;
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
        } finally {
            db.close();
            finish();
        }
    }

    private void controlBudget() {
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();

        Cursor cur = db.queryBudgetFull();
        String tmp, subtype;
        double money, bound;

        if(cur.moveToFirst()) {
            do {
                tmp = cur.getString(cur.getColumnIndex("date"));
                money = cur.getDouble(cur.getColumnIndex("money"));
                bound = cur.getDouble(cur.getColumnIndex("bound"));
                subtype = retrieveSubtypeName(cur.getInt(cur.getColumnIndex("idsubtype")));
                if(tmp.equals(java.time.LocalDate.now().toString())) {
                    //showNotification(subtype, money, bound);
                    db.deleteBudget(cur.getInt(cur.getColumnIndex("idbudget")));
                }

            } while(cur.moveToNext());
        }
        db.close();
    }

    private void showNotification(String subtype, double money, double bound) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationChannel.DEFAULT_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_baseline_notifications_24);
        builder.setContentTitle("Your budget is over");
        builder.setContentText(subtype+" "+money+" / "+bound);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationChannel channel = new NotificationChannel("budget", "budget", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("budget");
        NotificationManager notman = getSystemService(NotificationManager.class);
        notman.createNotificationChannel(channel);
        NotificationManagerCompat notificationmanager = NotificationManagerCompat.from(this);
        notificationmanager.notify(0, builder.build());
    }

    private String retrieveSubtypeName(int idsubtype) {
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();

        String rit = "";

        Cursor cur = db.querySubtypeBySubtype(idsubtype);
        if(cur.moveToFirst()) {
            rit = cur.getString(cur.getColumnIndex("name"));
        }

        db.close();
        return rit;
    }
}
package com.example.pbwallet;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        createNotification();
    }

    private void createNotification(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("budget", "budget", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("budget");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}

package com.example.pbwallet;

import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ShowNotification implements Runnable{
    String subtype;
    double money, bound;
    Context ctx;

    public ShowNotification(String subtype, double money, double bound, Context ctx){
        this.subtype = subtype;
        this.money = money;
        this.bound = bound;
        this.ctx = ctx;
    }
    @Override
    public void run() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, "budget");
        builder.setSmallIcon(R.drawable.ic_baseline_notifications_24);
        builder.setContentTitle("Your budget is over");
        builder.setContentText(subtype+" "+money+" / "+bound);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
        builder.setAutoCancel(true);
        NotificationManager notificationmanager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationmanager.notify(1, builder.build());
    }
}

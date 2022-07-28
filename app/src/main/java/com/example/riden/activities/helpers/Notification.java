package com.example.riden.activities.helpers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.riden.R;


public class Notification extends BroadcastReceiver {

    public int notificationID = 1;
    public String channelID = "channel1";
    public String titleExtra = "titleExtra";
    public String messageExtra = "messageExtra";
    @Override
    public void onReceive(Context context, Intent intent) {
       android.app.Notification notification = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.ic_baseline_local_phone_24)
                .setContentTitle(intent.getStringExtra(titleExtra))
                .setContentText(intent.getStringExtra(messageExtra))
                .build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationID, notification);
    }
}

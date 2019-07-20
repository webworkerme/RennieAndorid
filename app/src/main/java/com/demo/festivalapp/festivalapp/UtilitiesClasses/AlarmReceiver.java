package com.demo.festivalapp.festivalapp.UtilitiesClasses;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.demo.festivalapp.festivalapp.Activities.NotificationActvity;
import com.demo.festivalapp.festivalapp.Models.DatabaseHelper;
import com.demo.festivalapp.festivalapp.R;

public class AlarmReceiver extends BroadcastReceiver {
    String CHANNEL_ID = "my_channel_01";
    DatabaseHelper myDb;
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String event_id = null;
        myDb = new DatabaseHelper(context);
        Log.e("ALARM","WORKING");
        if(intent.getExtras()!=null)
        {
            event_id=intent.getStringExtra("EVENT_ID");
            Log.d("MESSAGE","IS :"+event_id);
           if( myDb.CheckALreadyAdded(event_id))
            {
                CreateNotification(context, "Event Reminder", "saved festival", event_id);
            }
            else
            {

            }
        }
    else
        {
//            CreateNotification(context,"Event Reminder","You have schedule reminders");
        }

    }
    public void CreateNotification(Context context, String title, String message,String id) {
        long when = System.currentTimeMillis();
        Intent intent = new Intent(context, NotificationActvity.class);
        intent.putExtra("EVENT_ID",id);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(context, CHANNEL_ID);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.splash_logo)
                .setTicker("Hearty365")
                .setContentTitle(title)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentInfo("Info");
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            b.setChannelId("com.demo.festivalapp.festivalapp");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "com.demo.festivalapp.festivalapp",
                    "Rennie",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(2, b.build());
//            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)

        } else {


            mNotificationManager.notify(1, b.build());

        }
    }
}

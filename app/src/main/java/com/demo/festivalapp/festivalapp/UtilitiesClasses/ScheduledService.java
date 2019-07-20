package com.demo.festivalapp.festivalapp.UtilitiesClasses;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.demo.festivalapp.festivalapp.Models.DatabaseHelper;

import java.util.Timer;

/**
 * Created by Abbasi on 10/16/2018.
 */

public class ScheduledService extends Service {
    private Timer timer = new Timer();
    String CHANNEL_ID = "my_channel_01";
    DatabaseHelper mydb;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//       if (Build.VERSION.SDK_INT >= 26) {
//            String CHANNEL_ID = "my_channel_01";
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//
//            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
//
//            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                    .setContentTitle("")
//                    .setContentText("").build();
//
//            startForeground(1, notification);
//        }
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                //Your code here
//
//                mydb = new DatabaseHelper(getApplicationContext());
//                Cursor cursor1 = mydb.ReadFestivals();
//                if (cursor1.getCount() > 0) {
//                    if (cursor1.moveToFirst()) {
//                        do {
//                            Log.d("DATE_START", "IS : " + cursor1.getString(cursor1.getColumnIndex("STARTDATE")));
//                            Log.d("DATE_END", "IS : " + cursor1.getString(cursor1.getColumnIndex("ENDDATE")));
//                        }
//                        while (cursor1.moveToNext());
//                    }
//                }
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-mm");
//                String currentDate = sdf.format(new Date());
//                SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
//                String currentTime = sdf1.format(new Date());
//                Log.d("CURRENTDATE", "IS" + currentDate);
//                Log.d("CURRENTTIME", "IS" + currentTime);
//                Log.d("CURRENT_WEEK", "IS : " + GetCurerentWeekDay());
//                Log.d("LONGVALUE", "IS : " + DateToTimeStamp(currentDate));
//                Log.d("INTEGER_VALUE", "IS : " + DateToTimeStamp(currentDate));
//                Cursor cursor = mydb.MacthDate(DateToTimeStamp(currentDate));
//                Log.d("CURSOR_SIZE", "IS :" + cursor.getCount());
//                if (cursor.getCount() > 0) {
//                    if (cursor.moveToFirst()) {
//                        do {
//                            int noti_id = cursor.getInt(cursor.getColumnIndex("ID"));
//                            String noti_weekdays = cursor.getString(cursor.getColumnIndex("WEEKDAYS"));
//                            String noti_holidays = cursor.getString(cursor.getColumnIndex("HOLIDAYS"));
//                            String noti_repeat = cursor.getString(cursor.getColumnIndex("REPEATWEEKDAYS"));
//                            String noti_done = cursor.getString(cursor.getColumnIndex("DONE"));
//                            if (String.valueOf(DateToTimeStamp(currentDate)).equals(noti_holidays)) {
//                                Cursor cursor2=mydb.MacthTime(currentTime);
//                                if(cursor2.getCount()>0) {
//                                    CreateNotification(getApplicationContext(), "Test", "Event is Scheduled");
//                                }
//                            } else {
//                                if (noti_weekdays.contains(","))
//                                {
//                                    String[] separated = noti_weekdays.split(",");
//                                    if (separated.length == 3) {
//                                        String day1 = separated[0];
//                                        String day2 = separated[1];
//                                        String day3 = separated[2];
//                                        if (day1.equals(GetCurerentWeekDay())) {
//
//                                            Cursor cursor2=mydb.MacthTime(currentTime);
//                                            if(cursor2.getCount()>0) {
//                                                if(noti_done.equals("1"))
//                                                {
//
//                                                }
//                                                else
//                                                {
//                                                    CreateNotification(getApplicationContext(), "Test", "Event is Scheduled");
//                                                if(noti_repeat.equals("0"))
//                                                {
//                                                    mydb.DeleteClassItems(noti_id);
//                                                }
//                                                }
//                                            }
//                                        } else if (day2.equals(GetCurerentWeekDay())) {
//                                            Cursor cursor2=mydb.MacthTime(currentTime);
//                                            if(cursor2.getCount()>0) {
//                                                if(noti_done.equals("1"))
//                                                {
//
//                                                }
//                                                else
//                                                {
//                                                    CreateNotification(getApplicationContext(), "Test", "Event is Scheduled");
//                                                    if(noti_repeat.equals("0"))
//                                                    {
//                                                        mydb.DeleteClassItems(noti_id);
//                                                    }
//                                                }
//                                            }
//                                        } else {
//                                            Cursor cursor2=mydb.MacthTime(currentTime);
//                                            if(cursor2.getCount()>0)
//                                            {
//                                                if(noti_done.equals("1"))
//                                                {
//
//                                                }
//                                                else
//                                                {
//                                                    CreateNotification(getApplicationContext(), "Test", "Event is Scheduled");
//                                                    if(noti_repeat.equals("0"))
//                                                    {
//                                                        mydb.DeleteClassItems(noti_id);
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    } else {
//                                        String day1 = separated[0];
//                                        String day2 = separated[1];
//                                        if (day1.equals(GetCurerentWeekDay())) {
//                                            Cursor cursor2=mydb.MacthTime(currentTime);
//                                            if(cursor2.getCount()>0) {
//                                                if(noti_done.equals("1"))
//                                                {
//
//                                                }
//                                                else
//                                                {
//                                                    CreateNotification(getApplicationContext(), "Test", "Event is Scheduled");
//                                                    if(noti_repeat.equals("0"))
//                                                    {
//                                                        mydb.DeleteClassItems(noti_id);
//                                                    }
//                                                }
//                                            }
//                                        } else {
//                                            Cursor cursor2=mydb.MacthTime(currentTime);
//                                            if(cursor2.getCount()>0) {
//                                                if(noti_done.equals("1"))
//                                                {
//
//                                                }
//                                                else
//                                                {
//                                                    CreateNotification(getApplicationContext(), "Test", "Event is Scheduled");
//                                                    if(noti_repeat.equals("0"))
//                                                    {
//                                                        mydb.DeleteClassItems(noti_id);
//                                                    }
//                                                }
//                                            }
//                                        }
//
//                                    }
//
//                                }
//                                else
//                                {
//                                    Cursor cursor2=mydb.MacthTime(currentTime);
//                                    if(cursor2.getCount()>0) {
//                                        if(noti_done.equals("1"))
//                                        {
//
//                                        }
//                                        else
//                                        {
//                                            CreateNotification(getApplicationContext(), "Test", "Event is Scheduled");
//                                            if(noti_repeat.equals("0"))
//                                            {
//                                                mydb.DeleteClassItems(noti_id);
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//
//                        }
//                        while (cursor.moveToNext());
//                    }
//                }
//
//            }
//        }, 0, 1 * 60 * 1000);//1 Minutes
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    public void CreateNotification(Context context, String title, String message) {
//        long when = System.currentTimeMillis();
//        Intent intent = new Intent(context, HomeScreenActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder b = new NotificationCompat.Builder(context, CHANNEL_ID);
//
//        b.setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.drawable.splash_logo)
//                .setTicker("Hearty365")
//                .setContentTitle(title)
//                .setContentText(message)
//                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
//                .setContentIntent(contentIntent)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentInfo("Info");
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            b.setChannelId("com.nlapps.grocerry.groceryapp");
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    "com.nlapps.grocerry.groceryapp",
//                    "Grocery Planner",
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//            if (notificationManager != null) {
//                notificationManager.createNotificationChannel(channel);
//            }
//            notificationManager.notify(2, b.build());
////            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
//
//        } else {
//
//
//            mNotificationManager.notify(1, b.build());
//
//        }
//    }
//
//    public String GetCurerentWeekDay() {
//        String week_day;
//        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
//        Date d = new Date();
//        String dayOfTheWeek = sdf.format(d);
//        week_day = dayOfTheWeek;
//        Log.d("WEEK_DAY", "IS : " + dayOfTheWeek);
//        return week_day;
//    }
//
//    public long DateToTimeStamp(String date1) {
//        long timer_Stamp = 0;
//        try {
//            DateFormat formatter = new SimpleDateFormat("yyyy-dd-mm");
//            Date date = (Date) formatter.parse(date1);
//            System.out.println("Today is " + date.getTime());
//            timer_Stamp = date.getTime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return timer_Stamp;
//    }
    }
}

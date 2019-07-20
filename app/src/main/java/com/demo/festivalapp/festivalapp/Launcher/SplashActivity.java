package com.demo.festivalapp.festivalapp.Launcher;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.demo.festivalapp.festivalapp.Activities.HomeScreenActivity;
import com.demo.festivalapp.festivalapp.R;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.ScheduledService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        context=this;
        SeparaterString("Friday,Saturday,Sunday");
        StartService();

//        GetDateFormate();
        RunSplashScreen();
    }
    public void RunSplashScreen()
    {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent homeIntent = new Intent(SplashActivity.this,HomeScreenActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
public String  GetDateFormate(String formatted_date)
{
    String strDate=null ;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = null;
    try {
        date = dateFormat.parse(formatted_date);
        strDate=dateFormat.format(date);
        Log.d("FORMATEED","DATE : "+strDate);
    } catch (ParseException e) {
        e.printStackTrace();
    }
  return strDate;
}
    public void StartService()
    {
        Intent i= new Intent(context, ScheduledService.class);
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(i);
//        } else {
            context.startService(i);
//        }

    }
    public void SeparaterString(String sample)
    {
        String[] separated = sample.split(",");
        if (separated.length==3)
        {
            String str1=separated[0];
            String str2=separated[1];
            String str3=separated[2];
            Log.d("FIRST","STRING"+str1);
            Log.d("SECOND","STRING"+str2);
            Log.d("SECOND","STRING"+str3);
        }
        else
        {

        }


    }
}

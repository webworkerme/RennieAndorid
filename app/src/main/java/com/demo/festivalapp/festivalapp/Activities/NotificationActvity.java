package com.demo.festivalapp.festivalapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.demo.festivalapp.festivalapp.Models.DatabaseHelper;
import com.demo.festivalapp.festivalapp.Models.Festivals;
import com.demo.festivalapp.festivalapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.festival_Name;
import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.festival_id;

public class NotificationActvity extends AppCompatActivity {
String id;
DatabaseHelper myDb;
TextView event_time,event_palce,event_name,tv_festname;
    LinearLayout btn_festivel_info, btn_event_list, btn_wish_list, btn_showmapview;
    SharedPreferences prefs;
    Festivals festivals;
    ImageView home_page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_actvity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myDb = new DatabaseHelper(this);
        prefs = getSharedPreferences("PURCHASE_DETAIS", MODE_PRIVATE);
        SetCustomTitle();
        setUiWidgets();
        GetData();

    }
public void GetData()
{
    if(getIntent().getExtras()!=null)
    {
        id=getIntent().getStringExtra("EVENT_ID");
    if(id!=null)
    {
        GetEventDetail(Integer.parseInt(id));
    }
    }
}
public void GetEventDetail(int id)
{
    Cursor cursor1=myDb.getEventDetail(id);
    if (cursor1.getCount() > 0) {
                    if (cursor1.moveToFirst()) {
                        do {
                            Log.d("EVENT_TIME", "IS : " + cursor1.getString(cursor1.getColumnIndex("TIME")));
                            Log.d("EVENT__NAME", "IS : " + cursor1.getString(cursor1.getColumnIndex("NAME")));
                            Log.d("EVENT__PLACE", "IS : " + cursor1.getString(cursor1.getColumnIndex("PLACE")));
                            event_time.setText(""+cursor1.getString(cursor1.getColumnIndex("TIME")));
                            event_name.setText(""+cursor1.getString(cursor1.getColumnIndex("NAME")));
                            event_palce.setText(""+cursor1.getString(cursor1.getColumnIndex("PLACE")));
                       String festivalDetail=cursor1.getString(cursor1.getColumnIndex("FESTIVAL_DETAIL"));
                            readJson(festivalDetail);
                        }
                        while (cursor1.moveToNext());
                    }
                }
}
public void setUiWidgets()
{
    event_time=findViewById(R.id.event_time);
    event_name=findViewById(R.id.event_name);
    event_palce=findViewById(R.id.event_place);
    tv_festname=findViewById(R.id.tv_festname);
    home_page = findViewById(R.id.home_page);
   //Bottom buttons
    btn_showmapview = findViewById(R.id.btn_showmapview);
    btn_showmapview.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (prefs.getBoolean("map_upgrade", false)) {
                Intent real_timemap = new Intent(NotificationActvity.this, MapsActivity.class);
                real_timemap.putExtra("festival", new Festivals(festivals.getId(), festivals.getTitle(), festivals.getGenre(),
                        festivals.getStartDate(), festivals.getEnd_date(), festivals.getFiles_home(), festivals.getFile_map(),
 festivals.getFiles_profile(), festivals.getFiles_info(),
                        festivals.getFestival_lat(),
                        festivals.getFestival_lng(), festivals.getWeekdays(), festivals.getRepeatWeekday(), festivals.getHoliday()));
                startActivity(real_timemap);
            } else {
                Intent map = new Intent(NotificationActvity.this, FreeUserMapActivty.class);
                map.putExtra("festival", new Festivals(festivals.getId(), festivals.getTitle(), festivals.getGenre(),
                        festivals.getStartDate(), festivals.getEnd_date(), festivals.getFiles_home(), festivals.getFile_map(), festivals.getFiles_profile(), festivals.getFiles_info(),
                        festivals.getFestival_lat(), festivals.getFestival_lng(), festivals.getWeekdays(), festivals.getRepeatWeekday(), festivals.getHoliday()));
                startActivity(map);
            }
        }
    });
    btn_wish_list = findViewById(R.id.btn_wish_list);
    btn_wish_list.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            Intent intent_festivalevent_list = new Intent(NotificationActvity.this, AllWishlist.class);
            intent_festivalevent_list.putExtra("festival", new Festivals(festivals.getId(), festivals.getTitle(), festivals.getGenre(),
                    festivals.getStartDate(), festivals.getEnd_date(), festivals.getFiles_home(), festivals.getFile_map(), festivals.getFiles_profile(), festivals.getFiles_info(),
                    festivals.getFestival_lat(), festivals.getFestival_lng(), festivals.getWeekdays(), festivals.getRepeatWeekday(), festivals.getHoliday()));
            startActivity(intent_festivalevent_list);

        }
    });
    btn_festivel_info = findViewById(R.id.btn_festivel_info);
    btn_festivel_info.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent_festival_info = new Intent(NotificationActvity.this, FestivalDetailActivty.class);
            intent_festival_info.putExtra("festival", new Festivals(festivals.getId(), festivals.getTitle(), festivals.getGenre(),
                    festivals.getStartDate(), festivals.getEnd_date(), festivals.getFiles_home(), festivals.getFile_map(), festivals.getFiles_profile(), festivals.getFiles_info(),
                    festivals.getFestival_lat(), festivals.getFestival_lng()
                    , festivals.getWeekdays(), festivals.getRepeatWeekday(), festivals.getHoliday()));
            startActivity(intent_festival_info);
        }
    });
    btn_event_list = findViewById(R.id.btn_event_list);
    btn_event_list.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent_festivalevent_list = new Intent(NotificationActvity.this, FestivalEventListActivity.class);
            intent_festivalevent_list.putExtra("festival", new Festivals(festivals.getId(), festivals.getTitle(), festivals.getGenre(),
                    festivals.getStartDate(), festivals.getEnd_date(), festivals.getFiles_home(), festivals.getFile_map(), festivals.getFiles_profile(), festivals.getFiles_info(),
                    festivals.getFestival_lat(), festivals.getFestival_lng(), festivals.getWeekdays(), festivals.getRepeatWeekday(), festivals.getHoliday()));
            startActivity(intent_festivalevent_list);

        }
    });
}
    public void SetCustomTitle() {
        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            Typeface tf = Typeface.createFromAsset(getAssets(), "Roycroft-Regular.otf");
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.layout_actionbar);
            getSupportActionBar().hide();
            TextView title = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
            title.setText("");
            title.setTypeface(tf);
        } catch (Exception ex) {

        }
    }
    public void readJson(String json_str)
    {
        try
        {
            JSONObject mainObject = new JSONObject(json_str);
            JSONArray festivalObject = mainObject.getJSONArray("FESTIVAL");
            for (int i=0;i<festivalObject.length();i++)
            {
                JSONObject c = festivalObject.getJSONObject(i);
                Log.d("FESTIVAL_ID", "IS :" + c.getString("id"));
                Log.d("FESTIVAL_TITLE", "IS :" + c.getString("title"));

               festivals=new Festivals(c.getString("id"),
                c.getString("title"),
                c.getString("genre"),
                c.getString("startDate"),
                c.getString("endDate"),
                c.getString("imgHome"),
                c.getString("imgFile"),
                c.getString("imgProfile"),
                c.getString("imginfo"),
                c.getString("lat"),
                c.getString("lng"),
                c.getString("weekDays"),
                c.getString("repeaWeekDays"),
                c.getString("holiDays"));
                String   image_url = "http://renfestapp.com/" +  c.getString("imgHome");
                festival_id=festivals.getId();
                festival_Name=festivals.getTitle();
                Log.d("HOME_PAGE", "URL" + image_url);
                LoadImageUsingGlide(image_url);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void LoadImageUsingGlide(String img_url) {
        Glide.with(this)
                .asBitmap()
                .load(img_url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        home_page.setImageBitmap(resource);
//                        festival_homeprogress.setVisibility(View.GONE);
                        Log.d("IMAGE_FETCHED", "TRUE");
                    }

                });
    }
}

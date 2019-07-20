package com.demo.festivalapp.festivalapp.Adapters;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.festivalapp.festivalapp.Models.DatabaseHelper;
import com.demo.festivalapp.festivalapp.Models.EventModel;
import com.demo.festivalapp.festivalapp.Models.Festivals;
import com.demo.festivalapp.festivalapp.R;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.AlarmReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Abbasi on 9/3/2018.
 */

public class FestivalEventListAdapter extends RecyclerView.Adapter<FestivalEventListAdapter.MyViewHolder> {
    private List<EventModel> festivalsList;
    Context mcontext;
    DatabaseHelper myDb;
    SharedPreferences prefs;
    Festivals festivals;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView event_time, title_event, event_place;
        LinearLayout layout_setitmer;
        ImageView image_setitmer;

        public LinearLayout layout_festival_detail;


        public MyViewHolder(View view) {
            super(view);
            event_time = (TextView) view.findViewById(R.id.event_time);
            title_event = (TextView) view.findViewById(R.id.title_event);
            event_place = (TextView) view.findViewById(R.id.event_place);
            layout_setitmer = view.findViewById(R.id.layout_setitmer);
            image_setitmer = view.findViewById(R.id.image_setitmer);
        }
    }


    public FestivalEventListAdapter(List<EventModel> moviesList, Context context,Festivals data_festivals) {
        this.festivalsList = moviesList;
        mcontext = context;
        myDb = new DatabaseHelper(mcontext);
        prefs = context.getSharedPreferences("PURCHASE_DETAIS", MODE_PRIVATE);
    this.festivals=data_festivals;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_adapter_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        holder.setIsRecyclable(false);
        if(prefs.getBoolean("event_upgrade",false))
        {
            holder.layout_setitmer.setEnabled(true);
        }
        else
        {
            holder.layout_setitmer.setEnabled(false);
        }
        String formatted_string;
        final EventModel festival = festivalsList.get(position);
        Log.d("FESTIVAL_TIME" + this.festivalsList.get(position).getStartTime(), "TYPE" + this.festivalsList.get(position).getType());
        ;
        formatted_string = festival.getStartTime();
//       }
        Log.d("Type", "is" + festival.getType());
        if (festival.getType().equals("copy")) {
            holder.event_time.setVisibility(View.GONE);
        } else {

            holder.event_time.setVisibility(View.VISIBLE);
            if (festival.getFilter_type().equals("theatre")) {
                holder.event_time.setText(festival.getEventplace());
            } else if (festival.getFilter_type().equals("artist")) {
                holder.event_time.setText(festival.getArtistsName());
            } else {
                holder.event_time.setText(formatted_string);
            }
        }
        if (festival.getFilter_type().equals("artist")) {
            holder.event_time.setText(festival.getEventplace());
        } else {
            holder.title_event.setText(festival.getArtistsName() + festival.getEventname());
        }
        Log.d("FIlTER", "TYPE" + festival.getFilter_type());
        if (festival.getFilter_type().equals("theatre")) {
            holder.event_place.setText("" + formatted_string);
        } else if (festival.getFilter_type().equals("artist")) {
            holder.event_place.setText("" + formatted_string);
        } else {
            holder.event_place.setText("" + festival.getEventplace());
        }
        //set all other value here
        holder.layout_setitmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Festival_START","DATE"+festival.getFestival_startdate());
                if(myDb.CheckALreadyAdded(festival.getFest_id()))
                {
                ShowDialogueDeleteReminder(festival.getFest_id(),holder.image_setitmer);
                }
                else
                {

                    ShowDialogueAddClass(position, festival.getFestival_startdate(), festival.getFestival_enddate(),
                            festival.getFest_weekdays(), festival.getFest_repeatweekdays(), festival.getFest_holiday(),
                            festival.getFest_id(), festival.getStartTime(),festival.getArtistsName() + festival.getEventname(),
                            festival.getEventplace(), holder.image_setitmer);
                }
            }
        });
        if(myDb.CheckALreadyAdded(festival.getFest_id()))
        {
            holder.image_setitmer.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.icon_checkmark));
            Log.d("ADDED","YES");
//             holder.layout_setitmer.setEnabled(false);
        }
        else
        {
        Log.d("NOT_ADDED","YES");
            holder.image_setitmer.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.ic_clock));
        }
    }

    @Override
    public int getItemCount() {
        return festivalsList.size();
    }

    public void SetRemider(Context context, String start_date, String time) {

        boolean alarm = (PendingIntent.getBroadcast(context, 0, new Intent("ALARM"), PendingIntent.FLAG_NO_CREATE) == null);
        if (alarm) {
            Intent itAlarm = new Intent("ALARM");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, itAlarm, 0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND, 3);
            AlarmManager alarme = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarme.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60000, pendingIntent);
        }
    }

    public void ShowDialogueAddClass(final int position, final String fest_start_date, final String fest_end_date,
                                     final String week_days, final String repeat_weekday, final String holidays,
                                     final String event_id, final String event_time, final String event_name,
                                     final String event_place, final ImageView image_setitmer) {
        final Dialog dialog = new Dialog(mcontext);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.layout_dialogue_addreminder);
        LinearLayout bedLcl = dialog.findViewById(R.id.bedLcl);
        Button btn_no = (Button) dialog.findViewById(R.id.btn_no);
        Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
        TextView tv_startdate = dialog.findViewById(R.id.tv_startdate);
        tv_startdate.setText("" + GetDateFormate(fest_start_date));
        TextView tv_enddate = dialog.findViewById(R.id.tv_enddate);
        tv_enddate.setText("" + GetDateFormate(fest_end_date));
        TextView tv_starttime = dialog.findViewById(R.id.tv_starttime);
        tv_starttime.setText("" + event_time);
        TextView tv_weekdays=dialog.findViewById(R.id.tv_weekdays);
        tv_weekdays.setText(""+week_days);
        TextView tv_holidays=dialog.findViewById(R.id.tv_holidays);
        tv_holidays.setText(""+holidays);
//        dialogButton.setText("Save");
        // if button is clicked, close the custom dialog
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String festival_str=MakeJsonObject(festivals);
                Log.d("FESTIVAL_DETAIL","IS :"+festival_str);
            long int_Startdate= DateToTimeStamp(GetDateFormate(fest_start_date));
            long int_Enddate= DateToTimeStamp(GetDateFormate(fest_end_date));
                Log.d("TIME_STAMP","IS : "+DateToTimeStamp(GetDateFormate(fest_start_date)));
                long result=AddReminder(int_Startdate,int_Enddate, event_time, week_days, repeat_weekday, holidays, event_id,
                        event_name, event_place,festival_str);
                if (result == -1) {
                    Toast.makeText(mcontext, "Unable to add Reminder", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Log.d("Holiday","event"+holidays);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = format.parse(holidays);
                       Log.d("Holiday_Year","IS :"+date.getYear());
                        Log.d("MONTH","IS :"+date.getMonth());
                        Log.d("day","IS :"+date.getDate());
                        Date date1=new Date("2019/1/16"+" "+event_time);
                        System.out.println("Time in 24Hours ="+new SimpleDateFormat("HH:mm").format(date1));
                        String[] separated = new SimpleDateFormat("HH:mm").format(date1).split(":");
                        int hour= Integer.parseInt(separated[0]);
                        int minute=Integer.parseInt(separated[1]);
                        String complete_year=String.valueOf(date.getYear()).substring(1);
                        String updated_year="20"+complete_year;
                        Log.d("HOLIDAYYEAR","IS :"+updated_year);
                        Date current_date=new Date();
                        if(date.after(current_date)) {
                            SetAlarm(event_id, Integer.parseInt(updated_year), date.getMonth(), date.getDate(),
                                    hour, minute, event_name, Integer.parseInt(event_id));
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(mcontext, "Reminder Added Succesfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    image_setitmer.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.icon_checkmark));
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-MM");
                    int count = 0;
                    try {
                        Date d1 = formatter.parse(GetDateFormate(fest_start_date));
                        Date d2 = formatter.parse(GetDateFormate(fest_end_date));
                        String fest_time=event_time;
                        count = saturdaysundaycount(event_id,d1,d2,event_time,event_name,Integer.parseInt(event_id));
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Log.d("Count of weekends = ","IS :"+count);
                }


            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mcontext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthLcl = (int) (displayMetrics.widthPixels * 0.9f);
        int heightLcl = (int) (displayMetrics.heightPixels * 0.5f);
        FrameLayout.LayoutParams paramsLcl = (FrameLayout.LayoutParams)
                bedLcl.getLayoutParams();
        paramsLcl.width = widthLcl;
        paramsLcl.height = heightLcl;
        paramsLcl.gravity = Gravity.CENTER;
        dialog.show();

        Window window = dialog.getWindow();
        bedLcl.setLayoutParams(paramsLcl);
    }

    public String GetDateFormate(String date) {
        String formatted_date = null;
        String year,month,day;

        try
        {
            year=date.substring(0,4);
            month=date.substring(4,6);
            if (date.length()<8)
            {
                day=date.substring(6,7);
            }
            else
            {
                day=date.substring(6,8);
            }

            formatted_date="20"+year.substring(2,4)+"-"+day+"-"+month;
        }
        catch (Exception ex)
        {

        }
        return  formatted_date;
    }

    public long AddReminder(long start_date, long end_date, String event_time, String weekdays, String repeat_weekDays,
                               String holidays, String event_id, String event_name, String event_place,String festival_data) {
        boolean reminder_added = false;
        Log.d("SAVEING_STARTDATE","is : "+start_date);
        long result = myDb.insertFestivalData(start_date, end_date, event_time, weekdays,
                repeat_weekDays, holidays, event_id, event_name, event_place,festival_data);

        return result;
    }
    public long DateToTimeStamp(String date1)
    {
        long timer_Stamp = 0;
        try
        {
            DateFormat formatter = new SimpleDateFormat("yyyy-dd-mm");
            Date date = (Date)formatter.parse(date1);
            System.out.println("Today is " +date.getTime());
            timer_Stamp=date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    return timer_Stamp;
    }
    public void ShowDialogueDeleteReminder(final String event_id, final ImageView imge_reminder)
    {
        final Dialog dialog = new Dialog(mcontext);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialogue_delete_reminder);
        LinearLayout bedLcl = dialog.findViewById(R.id.bedLcl);
        Button btn_no = (Button) dialog.findViewById(R.id.btn_no);
        Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
//        dialogButton.setText("Save");
        // if button is clicked, close the custom dialog
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                DeleteAlarms(Integer.parseInt(event_id));
                if(myDb.deleteTitle(event_id))
                {

                Toast.makeText(mcontext,"Reminder is succesfully deleted",Toast.LENGTH_SHORT).show();
                    imge_reminder.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.ic_clock));
                }
                else
                {
                    Toast.makeText(mcontext,"unable to delete reminder",Toast.LENGTH_SHORT).show();
                }


            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mcontext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthLcl = (int) (displayMetrics.widthPixels * 0.9f);
        int heightLcl = (int) (displayMetrics.heightPixels * 0.36f);
        FrameLayout.LayoutParams paramsLcl = (FrameLayout.LayoutParams)
                bedLcl.getLayoutParams();
        paramsLcl.width = widthLcl;
        paramsLcl.height = heightLcl;
        paramsLcl.gravity = Gravity.CENTER;
        dialog.show();

        Window window = dialog.getWindow();
        bedLcl.setLayoutParams(paramsLcl);
    }
    public int saturdaysundaycount(String event_id,Date d1, Date d2, String evetn_time,String event_name,int id) {
        Log.d("EVETN_TIME","IS"+evetn_time);
        Log.d("EVETN_STARTDATE","IS"+d1);
        Log.d("EVETN_ENDDATE","IS"+d2);
        Date date=new Date("2019/1/16"+" "+evetn_time);
        System.out.println("Time in 24Hours ="+new SimpleDateFormat("HH:mm").format(date));
        String[] separated = new SimpleDateFormat("HH:mm").format(date).split(":");
        int hour= Integer.parseInt(separated[0]);
        int minute=Integer.parseInt(separated[1]);
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);

        int sundays = 0;
        int saturday = 0;

        while (! c1.after(c2)) {

            if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ){
                Date date_str=c1.getTime();
                Date current_date=new Date();
                if(date_str.after(current_date)) {
                    Log.d("SATURDAY", "IS" + date_str);
                    Log.d("SATURDAY_DATE", "IS" + c1.getTime().getDate());
                    Log.d("SATURDAY_MONTH", "IS" + c1.getTime().getMonth());
                    Log.d("SATURDAY_YEAR", "IS" + c1.getTime().getYear());
                    Log.d("SATURDAY", "HOUR" + hour);
                    Log.d("SATURDAY", "MINUTE" + minute);
                    String complete_year=String.valueOf(c1.getTime().getYear()).substring(1);
                    String updated_year="20"+complete_year;
                    Log.d("UPDATED","YEAR"+updated_year);
                    SetAlarm(event_id,Integer.parseInt(updated_year), c1.getTime().getMonth(), c1.getTime().getDate(),
                            hour, minute, event_name, id);
                }
                saturday++;
            }
            if(c1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                Log.d("Sundays","IS"+c1.getTime());
                Date date_str=c1.getTime();
                Date current_date=new Date();
                if(date_str.after(current_date)) {
                    Log.d("Sundays", "IS" + date_str);
                    Log.d("Sundays_DATE", "IS" + c1.getTime().getDate());
                    Log.d("Sundays_MONTH", "IS" + c1.getTime().getMonth());
                    Log.d("Sundays_YEAR", "IS" + c1.getTime().getYear());
                    Log.d("Sundays", "HOUR" + hour);
                    Log.d("Sundays", "MINUTE" + minute);
                    String complete_year=String.valueOf(c1.getTime().getYear()).substring(1);
                    String updated_year="20"+complete_year;
                    Log.d("UPDATED","YEAR"+updated_year);
                    SetAlarm(event_id,Integer.parseInt(updated_year), c1.getTime().getMonth(), c1.getTime().getDate(),
                            hour, minute, event_name, id);
                }
                sundays++;
            }

            c1.add(Calendar.DATE, 1);
        }

        Log.d("Saturday Count = ","is :"+saturday);
        Log.d("Sunday Count = ","is"+sundays);
        return saturday + sundays;
    }
    public void SetAlarm(String event_id,int year,int month,int day_month,int hour_day,int hour_min,String event_name,int id)
    {

        AlarmManager alarmManager= (AlarmManager)mcontext.getSystemService(ALARM_SERVICE);
        Calendar calendar=new GregorianCalendar();
        calendar = GregorianCalendar.getInstance();
        //  calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.clear();
        calendar.set(year,month,day_month,hour_day,hour_min);
        Intent intent=new Intent(mcontext,AlarmReceiver.class);
        intent.putExtra("EVENT_ID",Long.toString(id));
        final int _id = (int) System.currentTimeMillis();
        PendingIntent broadcast=PendingIntent.getBroadcast(mcontext,_id,intent,0);

        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo( calendar.getTimeInMillis(),broadcast), broadcast);
        }
        else
        {
            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),broadcast);
        }
//        if(UpdateTimerValue(event_id,String.valueOf(_id)))
//        {
//        Log.d("Timer_Added ","Yes :");
//        }
//        else
//        {
//            Log.d("Timer_Added ","No :");
//        }
    }
   public boolean UpdateTimerValue(String event_id,String timer_id)
   {
       boolean is_updated=myDb.updateTimer(event_id,timer_id);
      return is_updated;
   }
   public void DeleteAlarms(int event_id)
   {
       Cursor cursor1=myDb.getAllSaveTimers(event_id);
       if (cursor1.getCount() > 0) {
           if (cursor1.moveToFirst()) {
               do {
                   Log.d("EVENT_TIME", "IS : " + cursor1.getInt(cursor1.getColumnIndex("TIMER_ID")));
               int timer_id=cursor1.getInt(cursor1.getColumnIndex("TIMER_ID"));
                   AlarmManager am = (AlarmManager) mcontext .getSystemService(mcontext.ALARM_SERVICE);
                   Intent i = new Intent(mcontext, AlarmReceiver.class);
                   PendingIntent p = PendingIntent.getBroadcast(mcontext, timer_id, i, 0);
                   am.cancel(p);

               }
               while (cursor1.moveToNext());
           }
       }
   }
   public String MakeJsonObject(Festivals festivals)
   {
       String obj_festival=null;
       JSONObject festival_obj = new JSONObject();
       try {
           festival_obj.put("id", festivals.getId());
           festival_obj.put("title", festivals.getTitle());
           festival_obj.put("genre", festivals.getGenre());
           festival_obj.put("startDate", festivals.getStartDate());
           festival_obj.put("endDate", festivals.getEnd_date());
           festival_obj.put("imgHome", festivals.getFiles_home());
           festival_obj.put("imgFile", festivals.getFile_map());
           festival_obj.put("imgProfile", festivals.getFiles_profile());
           festival_obj.put("imginfo", festivals.getFiles_info());
           festival_obj.put("lat",  festivals.getFestival_lat());
           festival_obj.put("lng", festivals.getFestival_lng());
           festival_obj.put("weekDays", festivals.getWeekdays());
           festival_obj.put("repeaWeekDays", festivals.getRepeatWeekday());
           festival_obj.put("holiDays", festivals.getHoliday());
           JSONArray jsonArray = new JSONArray();
           jsonArray.put(festival_obj);
           JSONObject festivalObj = new JSONObject();
           festivalObj.put("FESTIVAL", jsonArray);
           obj_festival=festivalObj.toString();
       } catch (JSONException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
    return obj_festival;
   }

}

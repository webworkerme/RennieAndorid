package com.demo.festivalapp.festivalapp.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Abbasi on 9/4/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Student.db";
    public static final String TABLE_NAME = "student_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "SURNAME";
    public static final String COL_5 = "IMAGE";
    public static final String COL_4 = "MARKS";
    public static final String COL_6 = "NOTES";
    public static final String COL_7 = "LATLNG";


 //Second Table
    public static final String SECOND_TABLE_NAME = "festival_table";
    public static final String SECOND_COL_1 = "ID";
    public static final String SECOND_COL_2 = "TIME";
    public static final String SECOND_COL_3 = "NAME";
    public static final String SECOND_COL_4 = "PLACE";
    //newly added coloumn
    public static final String SECOND_COL_5 = "EVENTID";
    public static final String SECOND_COL_7 = "WEEKDAYS";
    public static final String SECOND_COL_8 = "REPEATWEEKDAYS";
    public static final String SECOND_COL_9 = "HOLIDAYS";
    public static final String SECOND_COL_10 = "STARTDATE";
    public static final String SECOND_COL_11 = "ENDDATE";
    public static final String SECOND_COL_12 = "DONE";
    public static final String SECOND_COL_13 = "TIMER_ID";
    public static final String SECOND_COL_14 = "FESTIVAL_DETAIL";


  //Third Table
  public static final String THIRD_TABLE_NAME = "usermarkers_table";
    public static final String THIRD_COL_1 = "ID";
    public static final String THIRD_COL_2 = "LATITUDE";
    public static final String THIRD_COL_3 = "LONGITUDE";
    public static final String THIRD_COL_4 = "TITLE";

    //Fourth Table
    public static final String FOURTH_TABLE_NAME = "festivalist_table";
    public static final String FOURTH_COL_1 = "ID";
    public static final String FOURTH_COL_2 = "FESTIVALID";
    public static final String FOURTH_COL_3 = "FESTIVALNAME";
    public static final String FOURTH_COL_4 = "FESTIVALDATE";
    public static final String FOURTH_COL_5 = "FESTIVALADDRESS";
    public static final String FOURTH_COL_6 = "FESTIVALIMAGE";
    public static final String FOURTH_COL_7 = "FESTIVALSTATE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,SURNAME TEXT,NOTES TEXT,LATLNG TEXT,IMAGE BLOB,MARKS INTEGER)");
        db.execSQL("create table " + SECOND_TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,TIME TEXT,SURNAME TEXT,NAME TEXT,EVENTID TEXT,WEEKDAYS TEXT,REPEATWEEKDAYS TEXT,HOLIDAYS TEXT,STARTDATE INTEGER,ENDDATE INTEGER,DONE INTEGER,TIMER_ID INTEGER,FESTIVAL_DETAIL TEXT,PLACE TEXT)");
        db.execSQL("create table " + THIRD_TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,LATITUDE TEXT,LONGITUDE TEXT,TITLE TEXT)");
        db.execSQL("create table " + FOURTH_TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,FESTIVALID TEXT,FESTIVALNAME TEXT,FESTIVALDATE TEXT,FESTIVALADDRESS TEXT,FESTIVALSTATE TEXT,FESTIVALIMAGE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+SECOND_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+THIRD_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+FOURTH_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name,String category_name,String festival_id,byte[] image,String notes,String fetival_latlng) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,category_name);
        contentValues.put(COL_4,festival_id);
        contentValues.put(COL_5,image);
        contentValues.put(COL_6,notes);
        contentValues.put(COL_7,fetival_latlng);

        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public long insertFestivalData(long start_date,long end_date,String event_time,String weekdays,String repeat_weekDays,
                                   String holidays,String event_id, String event_name,String event_place,String festival_detail)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SECOND_COL_2,event_time);
        contentValues.put(SECOND_COL_3,event_name);
        contentValues.put(SECOND_COL_4,event_place);
        contentValues.put(SECOND_COL_5,event_id);
        contentValues.put(SECOND_COL_7,weekdays);
        contentValues.put(SECOND_COL_8,repeat_weekDays);
        contentValues.put(SECOND_COL_9,holidays);
        contentValues.put(SECOND_COL_10,start_date);
        contentValues.put(SECOND_COL_11,end_date);
        contentValues.put(SECOND_COL_12,0);
        contentValues.put(SECOND_COL_14,festival_detail);
        long result = db.insert(SECOND_TABLE_NAME,null ,contentValues);
        Log.d("INSERTED","ID"+result);
        return result;
    }
    public long insertMarkerData(String latitude,String longitude,String title_marker) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(THIRD_COL_2,latitude);
        contentValues.put(THIRD_COL_3,longitude);
        contentValues.put(THIRD_COL_4,title_marker);
        long result = db.insert(THIRD_TABLE_NAME,null ,contentValues);
        Log.d("INSERTED","ID"+result);
        return result;
    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        Log.d("SAVED","DATA"+res);
        return res;
    }
    public Cursor getAllFestivaData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+SECOND_TABLE_NAME,null);
        Log.d("SAVED","DATA"+res);
        return res;
    }
    public Cursor getAllMarkersData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+THIRD_TABLE_NAME,null);
        Log.d("SAVED","DATA"+res);
        return res;
    }
    public boolean updateData(String id,String name,String surname,String marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,surname);
        contentValues.put(COL_4,marks);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
    public boolean insertFestivalWishData(String festival_id,String festival_name,String festival_date,
                                          String festival_address,String festival_img_path,String festival_state) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FOURTH_COL_2,festival_id);
        contentValues.put(FOURTH_COL_3,festival_name);
        contentValues.put(FOURTH_COL_4,festival_date);
        contentValues.put(FOURTH_COL_5,festival_address);
        contentValues.put(FOURTH_COL_6,festival_img_path);
        contentValues.put(FOURTH_COL_7,festival_state);
        long result = db.insert(FOURTH_TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public Cursor getAllSavedFestivalList(String state_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query="select * from "+FOURTH_TABLE_NAME+" WHERE "+FOURTH_COL_7+"="+"'"+state_name+"'";
        Cursor res = db.rawQuery("select * from "+FOURTH_TABLE_NAME,null);
        Log.d("SAVED","DATA"+res);
        Log.d("QUERY","IS"+query);
        return res;
    }
    public boolean EmptyDatabase()
    {
        boolean result;
        SQLiteDatabase db = this.getWritableDatabase();
        int delete_able=db.delete(THIRD_TABLE_NAME, null, null);
        Log.d("TABLE1","Is"+delete_able);
        if(delete_able>0)
        {
            result=true;
        }
        else
        {
            result=false;
        }
        return result;
    }
    public boolean CheckALreadyAdded(String event_id) {
    Log.d("MATCH_EVENT","ID : "+event_id);
        boolean is_allreadyadde = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res_total = db.rawQuery("SELECT * FROM " + SECOND_TABLE_NAME + " WHERE " + "EVENTID" + "=" + event_id, null);
        Log.d("TOTAL", "RESULT" + res_total.getCount());
        if (res_total.getCount() > 0) {
            is_allreadyadde=true;
        }
        return is_allreadyadde;
    }
    public  Cursor MacthDate(long current_date) {
        SQLiteDatabase db = this.getWritableDatabase();
        String quey="SELECT * FROM " + SECOND_TABLE_NAME + " WHERE ENDDATE <= "+current_date+" AND STARTDATE >= "+current_date;
        Log.d("COMPARE","QUERY : "+quey);
        Cursor res = db.rawQuery("SELECT * FROM " + SECOND_TABLE_NAME + " WHERE ENDDATE >= "+current_date+" AND STARTDATE <= "+current_date,null);
        return res;
    }
    public  Cursor MacthTime(String current_time) {
        SQLiteDatabase db = this.getWritableDatabase();
        String quey="SELECT * FROM " + SECOND_TABLE_NAME + " WHERE TIME= "+current_time;
        Log.d("COMPARE","QUERY : "+quey);
        Cursor res = db.rawQuery("SELECT * FROM " + SECOND_TABLE_NAME + " WHERE TIME='"+current_time+"'",null);
        return res;
    }
    public Cursor ReadFestivals()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + SECOND_TABLE_NAME ,null);
        return res;
    }
    public boolean DeleteClassItems(int fest_id)
    {
        boolean is_updated=false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SECOND_COL_12,1);
        int value=db.update(SECOND_TABLE_NAME, cv, "ID=" + fest_id, null);
        Log.d("UPDATE","is"+value);
        if(value==0)
        {
            is_updated=false;
        }
        else
        {
            is_updated=true;
        }
        return  is_updated;
    }
    public boolean deleteTitle(String event_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SECOND_TABLE_NAME, SECOND_COL_5 + "=?", new String[]{event_id}) > 0;
    }
    public Cursor getEventDetail(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query="SELECT * FROM " + SECOND_TABLE_NAME + " WHERE EVENTID= "+id;
        Log.d("COMPARE","QUERY : "+query);
        Cursor res = db.rawQuery(query,null);
        return res;
    }
    public boolean updateTimer(String id,String timer_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SECOND_COL_1,id);
        contentValues.put(SECOND_COL_13,timer_id);
        db.update(SECOND_TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }
    public Cursor getAllSaveTimers(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query="SELECT * FROM " + SECOND_TABLE_NAME + " WHERE TIMER_ID= "+id;
        Log.d("COMPARE","QUERY : "+query);
        Cursor res = db.rawQuery(query,null);
        return res;
    }
}

package com.demo.festivalapp.festivalapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Abbasi on 9/3/2018.
 */

public class Festivals implements Parcelable {
 private String id,title, genre, start_date,end_date,type,files_home,file_map,files_profile,files_info;
  private String festival_lat,festival_lng;
private  String PolicyName,PolicyText;
    private  String weekdays;
    private  String repeatWeekday;
    private  String holiday;
    public Festivals() {
    }

    public Festivals(String fest_id,String title, String genre, String strat_date,String end_date,String img_home,String img_map,String img_profile,String img_info,String festival_lat,
                     String festival_lng,String weekdays,String repeatWeekday,String holiday)
    {
        this.id=fest_id;
        this.title = title;
        this.genre = genre;
        this.start_date = strat_date;
        this.files_home=img_home;
        this.file_map=img_map;
        this.files_profile=img_profile;
        this.files_info=img_info;
        this.end_date=end_date;
        this.PolicyName=PolicyName;
        this.PolicyText=PolicyText;
        this.festival_lat=festival_lat;
        this.festival_lng=festival_lng;
        this.weekdays=weekdays;
        this.repeatWeekday=repeatWeekday;
        this.holiday=holiday;
    }

    public static final Creator<Festivals> CREATOR = new Creator<Festivals>() {
        @Override
        public Festivals createFromParcel(Parcel in) {
            return new Festivals(in);
        }

        @Override
        public Festivals[] newArray(int size) {
            return new Festivals[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getStartDate() {
        return start_date;
    }

    public void setStartDate(String start_date) {
        this.start_date = start_date;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFiles_home() {
        return files_home;
    }

    public void setFiles_home(String files_home) {
        this.files_home = files_home;
    }

    public String getFile_map() {
        return file_map;
    }

    public void setFile_map(String file_map) {
        this.file_map = file_map;
    }

    public String getFiles_profile() {
        return files_profile;
    }

    public void setFiles_profile(String files_profile) {
        this.files_profile = files_profile;
    }

    public String getFiles_info() {
        return files_info;
    }

    public void setFiles_info(String files_info) {
        this.files_info = files_info;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getPolicyName() {
        return PolicyName;
    }

    public void setPolicyName(String policyName) {
        PolicyName = policyName;
    }

    public String getPolicyText() {
        return PolicyText;
    }

    public String getFestival_lat() {
        return festival_lat;
    }

    public void setFestival_lat(String festival_lat) {
        this.festival_lat = festival_lat;
    }

    public String getFestival_lng() {
        return festival_lng;
    }

    public void setFestival_lng(String festival_lng) {
        this.festival_lng = festival_lng;
    }

    public void setPolicyText(String policyText) {
        PolicyText = policyText;
    }

    public String getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(String weekdays) {
        this.weekdays = weekdays;
    }

    public String getRepeatWeekday() {
        return repeatWeekday;
    }

    public void setRepeatWeekday(String repeatWeekday) {
        this.repeatWeekday = repeatWeekday;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.genre);
        parcel.writeString(this.start_date);
        parcel.writeString(this.end_date);
        parcel.writeString(this.files_home);
        parcel.writeString(this.file_map);
        parcel.writeString(this.files_profile);
        parcel.writeString(this.files_info);
        parcel.writeString(this.PolicyName);
        parcel.writeString(this.PolicyText);
        parcel.writeString(this.festival_lat);
        parcel.writeString(this.festival_lng);

        parcel.writeString(this.weekdays);
        parcel.writeString(this.repeatWeekday);
        parcel.writeString(this.holiday);
    }
    public Festivals(Parcel in){
        this.id=in.readString();
        this.title=in.readString();
        this.genre= in.readString();
        this.start_date=in.readString();
        this.end_date=in.readString();
        this.files_home=in.readString();
        this.file_map=in.readString();
        this.files_profile=in.readString();
        this.files_info=in.readString();
        this.PolicyName=in.readString();
        this.PolicyText=in.readString();
        this.festival_lat=in.readString();
        this.festival_lng=in.readString();

        this.weekdays=in.readString();
        this.repeatWeekday=in.readString();
        this.holiday=in.readString();
    }

}

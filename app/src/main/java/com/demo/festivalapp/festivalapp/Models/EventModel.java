package com.demo.festivalapp.festivalapp.Models;

/**
 * Created by Abbasi on 9/4/2018.
 */

public class EventModel {
    private String StartTime, eventname, eventplace,type,start_date;
    private  String ArtistsName;
    private  String filter_type;
    private  String fest_weekdays;
    private  String fest_repeatweekdays;
    private  String fest_holiday;
    private String end_date;
    private String fest_id;
    private String festival_startdate;
    private String festival_enddate;
    public EventModel() {
    }

    public EventModel(String title, String genre, String year,String type,String ArtistsName,String start_date,
                      String filter_type,String week_days,String repeat_days,String holidays,String end_dat,
                      String fest_id,String fest_startdate,String fest_enddate ) {
        this.StartTime = title;
        this.eventname = genre;
        this.eventplace = year;
        this.type=type;
        this.ArtistsName=ArtistsName;
        this.start_date=start_date;
        this.filter_type=filter_type;
        this.fest_weekdays=week_days;
        this.fest_repeatweekdays=repeat_days;
        this.fest_holiday=holidays;
        this.end_date=end_dat;
        this.fest_id=fest_id;
        this.festival_startdate=fest_startdate;
        this.festival_enddate=fest_enddate;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String name) {
        this.StartTime = name;
    }

    public String getEventname() {
        return eventname;
    }

    public void setYear(String year) {
        this.eventname = year;
    }

    public String getEventplace() {
        return eventplace;
    }

    public void setEventplace(String genre) {
        this.eventplace = genre;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArtistsName() {
        return ArtistsName;
    }

    public void setArtistsName(String artistsName) {
        ArtistsName = artistsName;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getFilter_type() {
        return filter_type;
    }

    public void setFilter_type(String filter_type) {
        this.filter_type = filter_type;
    }

    public String getFest_weekdays() {
        return fest_weekdays;
    }

    public void setFest_weekdays(String fest_weekdays) {
        this.fest_weekdays = fest_weekdays;
    }

    public String getFest_repeatweekdays() {
        return fest_repeatweekdays;
    }

    public void setFest_repeatweekdays(String fest_repeatweekdays) {
        this.fest_repeatweekdays = fest_repeatweekdays;
    }

    public String getFest_holiday() {
        return fest_holiday;
    }

    public void setFest_holiday(String fest_holiday) {
        this.fest_holiday = fest_holiday;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getFest_id() {
        return fest_id;
    }

    public void setFest_id(String fest_id) {
        this.fest_id = fest_id;
    }

    public String getFestival_startdate() {
        return festival_startdate;
    }

    public void setFestival_startdate(String festival_startdate) {
        this.festival_startdate = festival_startdate;
    }

    public String getFestival_enddate() {
        return festival_enddate;
    }

    public void setFestival_enddate(String festival_enddate) {
        this.festival_enddate = festival_enddate;
    }
}

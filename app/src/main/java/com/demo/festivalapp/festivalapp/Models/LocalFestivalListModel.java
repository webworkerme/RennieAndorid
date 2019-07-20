package com.demo.festivalapp.festivalapp.Models;

/**
 * Created by Abbasi on 11/6/2018.
 */

public class LocalFestivalListModel {
  String id;
  String festival_id;
  String festival_name;
  String festival_address;
  String festival_date;
  String festival_image;

    public LocalFestivalListModel(String id, String festival_id, String festival_name,
                                  String festival_address, String festival_date, String festival_image) {
        this.id = id;
        this.festival_id = festival_id;
        this.festival_name = festival_name;
        this.festival_address = festival_address;
        this.festival_date = festival_date;
        this.festival_image = festival_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFestival_id() {
        return festival_id;
    }

    public void setFestival_id(String festival_id) {
        this.festival_id = festival_id;
    }

    public String getFestival_name() {
        return festival_name;
    }

    public void setFestival_name(String festival_name) {
        this.festival_name = festival_name;
    }

    public String getFestival_address() {
        return festival_address;
    }

    public void setFestival_address(String festival_address) {
        this.festival_address = festival_address;
    }

    public String getFestival_date() {
        return festival_date;
    }

    public void setFestival_date(String festival_date) {
        this.festival_date = festival_date;
    }

    public String getFestival_image() {
        return festival_image;
    }

    public void setFestival_image(String festival_image) {
        this.festival_image = festival_image;
    }
}

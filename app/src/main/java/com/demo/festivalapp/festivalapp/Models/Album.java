package com.demo.festivalapp.festivalapp.Models;

import android.graphics.Bitmap;

/**
 * Created by Lincoln on 18/05/16.
 */
public class Album {
    private String name;
    private String category_name;
    private Bitmap thumbnail;
    private String category_notes;
    private String category_latitude;

    public Album() {
    }

    public Album(String name, String numOfSongs, Bitmap thumbnail,String notes,String latlng) {
        this.name = name;
        this.category_name = numOfSongs;
        this.thumbnail = thumbnail;
        this.category_notes=notes;
        this.category_latitude=latlng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCategory_notes() {
        return category_notes;
    }

    public void setCategory_notes(String category_notes) {
        this.category_notes = category_notes;
    }

    public String getCategory_latitude() {
        return category_latitude;
    }

    public void setCategory_latitude(String category_latitude) {
        this.category_latitude = category_latitude;
    }
}

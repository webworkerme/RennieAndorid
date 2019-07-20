package com.demo.festivalapp.festivalapp.Models;

/**
 * Created by Abbasi on 11/6/2018.
 */

public class SaveStatesModel {
   public String id;
   public String state_name;

    public SaveStatesModel(String state_name) {
        this.id = id;
        this.state_name = state_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }
}

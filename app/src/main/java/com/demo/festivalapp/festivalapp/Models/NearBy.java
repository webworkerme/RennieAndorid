package com.demo.festivalapp.festivalapp.Models;

/**
 * Created by Abbasi on 9/3/2018.
 */

public class NearBy {
    private String state_name;

    public NearBy() {
    }

    public NearBy(String statename) {
        this.state_name = statename;

    }

    public String getStateName() {
        return state_name;
    }

    public void setStateName(String statename) {
        this.state_name = statename;
    }


}

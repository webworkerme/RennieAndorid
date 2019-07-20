package com.demo.festivalapp.festivalapp.Models;

/**
 * Created by Abbasi on 11/20/2018.
 */

public class PoliciesModel {
    private String address;
    private String datesAndHours;
    private String tickets;
    private String shuttle;
    private String charity;
    private String weapons;
    private String outsideFoodDrink;
    private String pets;
    private String wheelchairsStrollers;
    private String costumes;
    private String smoking;
    private String alcohol;
    private String aTM;
    private String other;

    public PoliciesModel(String address, String datesAndHours, String tickets, String shuttle, String charity, String weapons, String outsideFoodDrink, String pets, String wheelchairsStrollers, String costumes, String smoking, String alcohol, String aTM, String other)
    {
        this.address = address;
        this.datesAndHours = datesAndHours;
        this.tickets = tickets;
        this.shuttle = shuttle;
        this.charity = charity;
        this.weapons = weapons;
        this.outsideFoodDrink = outsideFoodDrink;
        this.pets = pets;
        this.wheelchairsStrollers = wheelchairsStrollers;
        this.costumes = costumes;
        this.smoking = smoking;
        this.alcohol = alcohol;
        this.aTM = aTM;
        this.other = other;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDatesAndHours() {
        return datesAndHours;
    }

    public void setDatesAndHours(String datesAndHours) {
        this.datesAndHours = datesAndHours;
    }

    public String getTickets() {
        return tickets;
    }

    public void setTickets(String tickets) {
        this.tickets = tickets;
    }

    public String getShuttle() {
        return shuttle;
    }

    public void setShuttle(String shuttle) {
        this.shuttle = shuttle;
    }

    public String getCharity() {
        return charity;
    }

    public void setCharity(String charity) {
        this.charity = charity;
    }

    public String getWeapons() {
        return weapons;
    }

    public void setWeapons(String weapons) {
        this.weapons = weapons;
    }

    public String getOutsideFoodDrink() {
        return outsideFoodDrink;
    }

    public void setOutsideFoodDrink(String outsideFoodDrink) {
        this.outsideFoodDrink = outsideFoodDrink;
    }

    public String getPets() {
        return pets;
    }

    public void setPets(String pets) {
        this.pets = pets;
    }

    public String getWheelchairsStrollers() {
        return wheelchairsStrollers;
    }

    public void setWheelchairsStrollers(String wheelchairsStrollers) {
        this.wheelchairsStrollers = wheelchairsStrollers;
    }

    public String getCostumes() {
        return costumes;
    }

    public void setCostumes(String costumes) {
        this.costumes = costumes;
    }

    public String getSmoking() {
        return smoking;
    }

    public void setSmoking(String smoking) {
        this.smoking = smoking;
    }

    public String getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(String alcohol) {
        this.alcohol = alcohol;
    }

    public String getaTM() {
        return aTM;
    }

    public void setaTM(String aTM) {
        this.aTM = aTM;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}

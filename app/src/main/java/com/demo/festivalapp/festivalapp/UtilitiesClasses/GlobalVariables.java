package com.demo.festivalapp.festivalapp.UtilitiesClasses;

/**
 * Created by Abbasi on 9/3/2018.
 */

public class GlobalVariables {
    public  static  String  BASE_URL="http://renfestapp.com/rennie/Api/get_events/d8263a0a1045391cbe5923cc1ba5a589e8f843770a6b5088d3bbf066ef5b4a64?";
    public  static  String URL_GET_FESTIVAL ="http://renfestapp.com/rennie/Api/get_festivals/d8263a0a1045391cbe5923cc1ba5a589e8f843770a6b5088d3bbf066ef5b4a64?";
    public  static  String URL_GET_FESTIVAL_POLICIES ="http://renfestapp.com/rennie/Api/get_polices/d8263a0a1045391cbe5923cc1ba5a589e8f843770a6b5088d3bbf066ef5b4a64?";

    //Get Event Urls on Map
    public  static  String  BASE_URL_MARKER="http://renfestapp.com/rennie/Api/get_table/d8263a0a1045391cbe5923cc1ba5a589e8f843770a6b5088d3bbf066ef5b4a64?";

    public static  String festival_id;
    public static  String festival_Name;

//    public static  String festival_Location;
//    public static  String festival_StartDate;
//    public static  String festival_FestivalEmail;
//    public static  String IMAGE_HOME;
//    public static  String IMAGE_INFO;
    public  static  boolean is_app_purchased=false;

    public static  String STATE_NAME="TEXAS";
    public static  String LOCAL_STATE_NAME="Texas";
    public static boolean is_wishlist=false;

    public static String ChecksSend(String pass_value)
{
    String value=pass_value;
return value;
}
public static String CheckReceive(String reeived_value)
{
    String value=reeived_value;
    return value;
}
}

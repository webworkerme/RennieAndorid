package com.demo.festivalapp.festivalapp.Activities;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.festivalapp.festivalapp.Models.DatabaseHelper;
import com.demo.festivalapp.festivalapp.Models.Festivals;
import com.demo.festivalapp.festivalapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.BASE_URL;
import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.BASE_URL_MARKER;
import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.ChecksSend;
import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.festival_id;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Festivals festivals;
    ProgressBar progress_events;
    ImageView icon_food,icon_stage,icon_carriage,icon_privy,icon_hourse;
    DatabaseHelper mydb;
    LinearLayout snackbar1;
    LinearLayout layout_nomarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        progress_events=findViewById(R.id.progress_events);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mydb=new DatabaseHelper(this);
        SetuiWidgets();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GetData();
        mMap = googleMap;
        mMap.setMapType(mMap.MAP_TYPE_HYBRID);
//        icon_food.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.map_selfood));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
//            icon_stage.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_stage));
//            icon_carriage.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_carriage));
//            icon_privy.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_privy));
//            icon_hourse.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_horse));
        }
        GetFestivaletail(festival_id,"foods");
    }
   public void GetData()
   {
       try {
           Bundle data = getIntent().getExtras();
           if(data!=null)
           {
               festivals = (Festivals) data.getParcelable("festival");
               Log.d("festva ID =","is"+festivals.getId());

           }
       }
       catch (Exception ex)
       {
           Log.d("GetData","Event Detail"+ex.getMessage());
       }
   }
    public void GetFestivaletail(String id, final String table_name) {
        progress_events.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = BASE_URL_MARKER + "festival_id=" + ChecksSend(id)+"&"+"table_name="+table_name;
        Log.d("GETEVENTS", "URL" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            progress_events.setVisibility(View.GONE);
                            JSONObject jsonObj = new JSONObject(response);
                            String response_value = jsonObj.getString("response");
                            if (response_value.equals("1"))
                            {
                                JSONArray json2 = jsonObj.getJSONArray("data");
                                Log.d("Total", "Detial" + json2.length());
                                for (int i = 0; i < json2.length(); i++)
                                {
                                    JSONObject c = json2.getJSONObject(i);
                                    String event_latitude = c.getString("latitude");
                                    String event_longitude = c.getString("longitude");
                                    String name= c.getString("name");
                                    LatLng festival_location = new LatLng(Double.parseDouble(event_latitude), Double.parseDouble(event_longitude));
                                    mMap.addMarker(new MarkerOptions().position(festival_location).title(""+name)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(festival_location , 18));
                                }
                            }
                            else if(response_value.equals("0"))
                            {
                                layout_nomarker.setVisibility(View.VISIBLE);
                            }
                        }
                        catch (Exception ex)
                        {

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress_events.setVisibility(View.GONE);
                ShowSnackBar("Unable to connect to server");
            }
        });
        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

  public void SetuiWidgets()
  {
      layout_nomarker=findViewById(R.id.layout_nomarker);
      snackbar1=findViewById(R.id.snackbar1);
      icon_food=findViewById(R.id.icon_food);
      icon_stage=findViewById(R.id.icon_stage);
      icon_carriage=findViewById(R.id.icon_carriage);
      icon_privy=findViewById(R.id.icon_privy);
      icon_hourse=findViewById(R.id.icon_hourse);
      icon_hourse.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              layout_nomarker.setVisibility(View.GONE);
//              icon_hourse.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_selhorse));
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
              {
//                  icon_food.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_food));
//                  icon_stage.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_stage));
//                  icon_privy.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_privy));
//                  icon_carriage.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_carriage));
              }
              mMap.clear();
              GetFestivaletail(festival_id,"poi");

          }
      });
      icon_food.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              layout_nomarker.setVisibility(View.GONE);
//              icon_food.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.map_selfood));
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
              {
//                  icon_stage.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_stage));
//                  icon_carriage.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_carriage));
//                  icon_privy.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_privy));
//                  icon_hourse.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_horse));
              }
              mMap.clear();
              GetFestivaletail(festival_id,"foods");
          }
      });
      icon_carriage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              layout_nomarker.setVisibility(View.GONE);
//              icon_carriage.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_selcarriage));
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
              {
//                  icon_stage.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_stage));
//                  icon_hourse.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_horse));
//                  icon_privy.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_privy));
//                  icon_food.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.map_food));
              }
              ReadLocalMarkers();
          }
      });
      icon_privy.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              layout_nomarker.setVisibility(View.GONE);
//            icon_privy.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_selprivy));
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
              {
//                  icon_food.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_food));
//                  icon_stage.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_stage));
//                  icon_carriage.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_carriage));
//                  icon_hourse.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_horse));
              }
              mMap.clear();
              GetFestivaletail(festival_id,"priveys");
          }
      });
      icon_stage.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              layout_nomarker.setVisibility(View.GONE);
//              icon_stage.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_selstage));
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
              {
//                  icon_food.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_food));
//                  icon_privy.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_privy));
//                  icon_carriage.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_carriage));
//                  icon_hourse.setImageDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.icon_horse));
              }
              mMap.clear();
              GetStages(festival_id,"theatre");
          }
      });
  }
public void ReadLocalMarkers()
{
    mMap.clear();
    if (mydb.getAllMarkersData().getCount() >= 1)
    {
        Log.d("MARKERS", "FOUND");
        Cursor cursor = mydb.getAllMarkersData();
        if (cursor.moveToFirst()) {
            do
            {
                Log.d("LATITUDE","is"+cursor.getString(cursor.getColumnIndex("LATITUDE")));
                Log.d("LONGITUDE","is"+cursor.getString(cursor.getColumnIndex("LONGITUDE")));
                Log.d("TITLE","is"+cursor.getString(cursor.getColumnIndex("TITLE")));
                LatLng point1=new LatLng(Double.parseDouble(cursor.getString(cursor.getColumnIndex("LATITUDE"))),
                        Double.parseDouble(cursor.getString(cursor.getColumnIndex("LONGITUDE"))));
//Populating Marker from sqlite
                mMap.addMarker(new MarkerOptions()
                        .position(point1)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .title(cursor.getString(cursor.getColumnIndex("TITLE"))));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point1 , 13));
            }
            while (cursor.moveToNext());
        }

    }
    else
    {
        layout_nomarker.setVisibility(View.VISIBLE);
    }
}
    public void GetStages(String fest_id,String order_by)
    {
        progress_events.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = BASE_URL + "festival_id=" + ChecksSend(fest_id)+"&"+"order_by="+order_by;
        Log.d("GETEVENTS", "URL" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        progress_events.setVisibility(View.GONE);
                      try
                       {
                        JSONObject jsonObj = new JSONObject(response);
                        String response_value = jsonObj.getString("response");
                        if (response_value.equals("1")) {
                            JSONArray json2 = jsonObj.getJSONArray("data");
                            Log.d("Total", "Detial" + json2.length());
                            for (int i = 0; i < json2.length(); i++) {
                                JSONObject c = json2.getJSONObject(i);
                                Log.d("TheatreName ", "is" + c.getString("TheatreName"));
                                String event_latitude = c.getString("latitude");
                                String event_longitude = c.getString("longitude");
                                String name= c.getString("TheatreName");
                                LatLng festival_location = new LatLng(Double.parseDouble(event_latitude), Double.parseDouble(event_longitude));
                                mMap.addMarker(new MarkerOptions().position(festival_location).title(""+name)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(festival_location , 18));
                            }
                        }
                        else if(response_value.equals("0"))
                        {
                            layout_nomarker.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            ShowSnackBar("Unable to connect");
                        }
                       }
                       catch (JSONException ex)
                       {

                       }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress_events.setVisibility(View.GONE);
                ShowSnackBar("Unable to connect to server");
            }
        });
        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    public void ShowSnackBar(String message)
    {
        Snackbar snackbar = Snackbar
                .make(snackbar1, message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorerror));
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)snackBarView.getLayoutParams();
        params.gravity = Gravity.TOP;
        snackBarView.setLayoutParams(params);
        snackbar.show();

    }
}

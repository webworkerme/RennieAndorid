package com.demo.festivalapp.festivalapp.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.demo.festivalapp.festivalapp.Models.DatabaseHelper;
import com.demo.festivalapp.festivalapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class AddMarker extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    DatabaseHelper mydb;
    LinearLayout snackbar1;
    private FusedLocationProviderClient mFusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marker);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    //intializing database
        mydb=new DatabaseHelper(this);
        snackbar1=findViewById(R.id.snackbar);
        ShowSnackBar("Please long press on location where you want to add marker");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(mMap.MAP_TYPE_HYBRID);
        Log.d("MARKERTABLE", "SIZE" + mydb.getAllMarkersData().getCount());
        if (mydb.getAllMarkersData().getCount() >= 1)
        {
         ReadMarkersData();
        }
        else
        {
            Log.d("NO", "MARKERS FOUND");
            GetData();
        }
    }
   public void GetData()
   {
       Bundle bundle=getIntent().getExtras().getBundle("DATA");
       if(bundle!=null)
       {
           mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
           if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
               ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
               return;

           }
           else
           {
               mFusedLocationClient.getLastLocation()
                       .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                           @Override
                           public void onSuccess(Location location) {
                               // Got last known location. In some rare situations this can be null.
                               if (location != null) {
                                   // Logic to handle location object
                                   LatLng user_current_latlng = new LatLng(location.getLatitude(),location.getLongitude());
                                   mMap.moveCamera(CameraUpdateFactory.newLatLng(user_current_latlng));
                                   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(user_current_latlng, 18.0f));
                                   mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                                       @Override
                                       public void onMapLongClick(LatLng point) {
                                           ShowDialogue(point);

                                       }
                                   });
                               }
                           }
                       });
           }

       }
       else
       {

       }
   }
    public void ReadMarkersData()
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
                        .title(cursor.getString(cursor.getColumnIndex("TITLE"))));
                GetData();
            }
            while (cursor.moveToNext());
        }

    }
   public boolean AddMarker(String latitude,String longitude,String title)
   {
       boolean marker_value=false;
       try
       {
       long result=mydb.insertMarkerData(latitude,longitude,title);
           Log.d("RESULT_ADD_MARKER","VALUE"+result);
       if(result==-1)
       {
           marker_value=false;
       }
       else
       {
           marker_value=true;
       }
       }
       catch (Exception ex)
       {

       }
return marker_value;
   }
    public void ShowDialogue(final LatLng point)
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.test_layout);
        dialog.setTitle("Enter Code Sent on Email...");

        // set the custom dialog components - text, image and button
        final EditText f_name = (EditText) dialog.findViewById(R.id.f_name);
        Button btn_cancel =dialog.findViewById(R.id.btn_cancel);
        LinearLayout bedLcl=dialog.findViewById(R.id.bedLcl);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_updatrecord);
        dialogButton.setText("Save");
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_marker=f_name.getText().toString();
                if(title_marker.equals(""))
                {

                }
                else {
                    dialog.dismiss();
                    boolean is_marker_added = AddMarker(Double.toString(point.latitude), Double.toString(point.longitude), title_marker);
                    if (is_marker_added)
                    {
                        Toast.makeText(getApplicationContext(), "Marker is Add Successfully", Toast.LENGTH_SHORT).show();
                        mMap.addMarker(new MarkerOptions()
                                .position(point)
                                .title(point.toString())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Unable to add Marker", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthLcl = (int) (displayMetrics.widthPixels*0.9f);
        int heightLcl = (int) (displayMetrics.heightPixels*0.4f);
        FrameLayout.LayoutParams paramsLcl = (FrameLayout.LayoutParams)
                bedLcl.getLayoutParams();
        paramsLcl.width = widthLcl;
        paramsLcl.height =heightLcl ;
        paramsLcl.gravity = Gravity.CENTER;
        dialog.show();

        Window window = dialog.getWindow();
        bedLcl.setLayoutParams(paramsLcl);
    }
    public void ShowSnackBar(String message)
    {
        Snackbar snackbar = Snackbar.make(snackbar1, message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorerror));
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)snackBarView.getLayoutParams();
        params.gravity = Gravity.BOTTOM;
        snackBarView.setLayoutParams(params);
        snackbar.show();

    }
}

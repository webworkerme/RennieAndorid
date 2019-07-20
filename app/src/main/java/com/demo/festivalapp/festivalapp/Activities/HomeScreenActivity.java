package com.demo.festivalapp.festivalapp.Activities;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.festivalapp.festivalapp.Adapters.FestivalListAdapter;
import com.demo.festivalapp.festivalapp.Adapters.NearbyFestivalListAdapter;
import com.demo.festivalapp.festivalapp.Models.DatabaseHelper;
import com.demo.festivalapp.festivalapp.Models.Festivals;
import com.demo.festivalapp.festivalapp.Models.NearBy;
import com.demo.festivalapp.festivalapp.R;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.util.IabHelper;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.util.IabResult;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.util.Inventory;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.util.Purchase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.ChecksSend;
import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.LOCAL_STATE_NAME;
import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.STATE_NAME;
import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.URL_GET_FESTIVAL;
import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.is_app_purchased;
import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.is_wishlist;

//import com.anjlab.android.iab.v3.BillingProcessor;
//import com.anjlab.android.iab.v3.TransactionDetails;

public class HomeScreenActivity extends AppCompatActivity  {
    private List<Festivals> movieList = new ArrayList<>();
    private List<NearBy> nearby = new ArrayList<>();
    private RecyclerView recyclerView,recyclerView1;
    private  RecyclerView recyclerview_state;
    private FestivalListAdapter mAdapter;
    private NearbyFestivalListAdapter nearbyFestivalListAdapter;
    private ProgressBar progressbar;
    Typeface custom_font,custom_font_statename;
    TextView txt_state_name,text_neaby;
    boolean is_copy;
//    BillingProcessor bp;
    DatabaseHelper myDb;
    private MenuItem action_purchaseapp;
    private MenuItem action_suggestion;
    private MenuItem action_add_marker;
    private MenuItem action_clear_marker;
    private MenuItem action_my_wishlist;
    private MenuItem action_suggestions;
    LinearLayout success_layout,layout_error;
    Button btn_retry;
    String image_state=null;
    ImageView image_state_img;
    private FusedLocationProviderClient mFusedLocationClient;
    //Current latlng
    LatLng latLng_current;
    EditText edt_suggestion,u_email;
    private static final String TAG =
            "InAppBilling";
    IabHelper mHelper;
    static final String ITEM_SKU = "reminder_price";
    static final String ITEM_SKU1 = "stagefilter_price";
    static final String ITEM_SKU2 = "wishlist_upgrade";
    static final String ITEM_SKU3 = "maps_upgrade";
    SharedPreferences prefs;
    LinearLayout layout_festival, layout_states;
    TextView txt_error_homepage;
    private String arrayString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_statein);
        image_state=null;
//        ShowDialogueAddClass();
        ConvertTimertt24Hours();
        SetAlarm();
        SetAlarm1();

        myDb=new DatabaseHelper(this);
        prefs = getSharedPreferences("PURCHASE_DETAIS", MODE_PRIVATE);
        IntiatePayment();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);//intializing fused location api
        //Setting Action Bar Logo
        SetCustomTitle();
       SetUiWidgets();
        if(getIntent().getExtras()!=null)
        {
        GetNearByFestivalList();
        txt_state_name.setText(STATE_NAME);
        }
        else
        {
       GetUSerCuurentLocation();

//            GetStateFromLatLng(34.0489,-111.0937);
        }
        if(is_app_purchased)
        {

        }
        else
        {

        }

    }
    public void SetUiWidgets()
    {
        txt_error_homepage=findViewById(R.id.txt_error_homepage);
        layout_festival=findViewById(R.id.layout_festival);
        layout_states=findViewById(R.id.layout_states);
        image_state_img=findViewById(R.id.image_state);
        //intiating layouts
        success_layout=findViewById(R.id.success_layout);
        layout_error=findViewById(R.id.layout_error);
        custom_font_statename = Typeface.createFromAsset(getAssets(),  "Gobold Regular.ttf");
        custom_font = Typeface.createFromAsset(getAssets(),  "Roycroft-Regular.otf");
        txt_state_name=findViewById(R.id.txt_state_name);
        txt_state_name.setTypeface(custom_font_statename);
        text_neaby=findViewById(R.id.text_neaby);
        text_neaby.setTypeface(custom_font);
        progressbar=findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerview_state= (RecyclerView)  findViewById(R.id.recyclerview_state);
        mAdapter = new FestivalListAdapter(movieList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        nearbyFestivalListAdapter = new NearbyFestivalListAdapter(nearby,this);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        recyclerview_state.setLayoutManager(mLayoutManager1);
        recyclerview_state.setItemAnimator(new DefaultItemAnimator());
        recyclerview_state.setAdapter(nearbyFestivalListAdapter);

        btn_retry=findViewById(R.id.btn_retry);
        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                layout_error.setVisibility(View.GONE);
                success_layout.setVisibility(View.VISIBLE);
                GetNearByFestivalList();
            }
        });
    }


    public void GetNearByFestivalList()
    {

        // Request a string response from the provided URL.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =URL_GET_FESTIVAL+"state="+ChecksSend(STATE_NAME.replaceAll(" ", "%20"));
        Log.e("GETFestivalURL","is"+url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
//                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        progressbar.setVisibility(View.GONE);
//                        Log.d("Server Reponse","is"+response.substring(0,500));
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String resposnse_value = jsonObj.getString("response");
                            Log.d("Response Value", "is" + resposnse_value);
                            if (resposnse_value.equals("1")) {
                                JSONArray json2 = jsonObj.getJSONArray("data");
                                Log.d("Data", "length" + json2.length());

                                for (int i = 0; i <json2.length(); i++) {
//                                    if (i == 4) {
//                                        break;
//                                    } else {
                                        JSONObject c = json2.getJSONObject(i);

                                   boolean neatby_state=c.has("near_by_states");
                                    if(neatby_state)
                                    {
                                    Log.d("NearBy","States found");
                                    JSONArray near_stetes=c.optJSONArray("near_by_states");
                                        Log.d("Neaby Elments","Array"+near_stetes);
                                        Log.d("Neaby Elments","Array Length"+near_stetes.length());
                                        for (int j=0;j<near_stetes.length();j++)
                                        {
//                                            JSONObject nearby = near_stetes.getJSONObject(j);
                                            Log.d("StateName","is"+near_stetes.get(j).toString().replace("[{","").replace("}]",""));
                                        String state_keyvaluepair=near_stetes.get(j).toString().replace("[{","").replace("}]","");
                                            if (state_keyvaluepair.equals(null)||state_keyvaluepair.equals(""))
                                            {

                                            }
                                            else {
                                                try {
                                                String[] separated = state_keyvaluepair.split(":");
                                                String key = separated[0];
                                                String value = separated[1].replace("\"", "");
                                                Log.d("Key" + key, "value" + value);
                                                NearBy movie = new NearBy(value);
                                                nearby.add(movie);
                                                }
                                                catch (Exception ex)
                                                {
                                                    Toast.makeText(getApplicationContext(),"No Near BY List Found",Toast.LENGTH_SHORT).show();
                                                }
                                                }
                                        }
                                        nearbyFestivalListAdapter.notifyDataSetChanged();
                                    }
                                    else
                                    {
                                        Log.d("FestivalName is =", "" + c.getString("FestivalName"));
                                        if(image_state!=null)
                                        {

                                        }
                                        else
                                        {
                                            image_state= c.getString("state_image");
                                        }
                                        Festivals movie = new Festivals(c.getString("FestivalID"),c.getString("FestivalName"), c.getString("FestivalLocation"), c.getString("StartDate"),c.getString("EndDate"),
                                                c.getString("files_home"), c.getString("file_map"), c.getString("files_profile"), c.getString("files_info"),
                                                c.getString("FestivalLat"),c.getString("FestivalLong"),
                                                c.getString("weekdays"),c.getString("repeatWeekday"),
                                                c.getString("holiday"));
                                        movieList.add(movie);
                                    }
//

                                }
                                float density = getApplicationContext().getResources().getDisplayMetrics().density;
                                Log.d("DENSITY","is"+density);
                                Picasso.get().load("http://renfestapp.com/"+image_state).into(image_state_img);
                                if(movieList.size()<2)
                                {

                                    if((int)density==3)
                                    {
                                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                                                layout_festival.getLayoutParams();
                                        params.weight = 1f;
                                        layout_festival.setLayoutParams(params);
                                        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams)
                                                layout_states.getLayoutParams();
                                        params1.weight = 1f;
                                        layout_states.setLayoutParams(params1);
                                    }
                                    else  if ((int)density<3)
                                    {

                                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                                                layout_festival.getLayoutParams();
                                        params.weight = 1f;
                                        layout_festival.setLayoutParams(params);
                                        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams)
                                                layout_states.getLayoutParams();
                                        params1.weight = 1.5f;
                                        layout_states.setLayoutParams(params1);
                                    }
                        }
                                else if (movieList.size()==2)
                                {
                                    if((int)density==3)
                                    {
                                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                                                layout_festival.getLayoutParams();
                                        params.weight = 1.3f;
                                        layout_festival.setLayoutParams(params);
                                        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams)
                                                layout_states.getLayoutParams();
                                        params1.weight = 0.7f;
                                        layout_states.setLayoutParams(params1);
                                    }
                                    else  if ((int)density<3) {
                                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                                                layout_festival.getLayoutParams();
                                        params.weight = 1f;
                                        layout_festival.setLayoutParams(params);
                                        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams)
                                                layout_states.getLayoutParams();
                                        params1.weight = 1f;
                                        layout_states.setLayoutParams(params1);
                                    }
                                    }
                                else if (movieList.size()==3)
                                {
                                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                                            layout_festival.getLayoutParams();
                                    params.weight = 1.3f;
                                    layout_festival.setLayoutParams(params);
                                    LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams)
                                            layout_states.getLayoutParams();
                                    params1.weight = 0.7f;
                                    layout_states.setLayoutParams(params1);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                            else if(resposnse_value.equals("0"))
                            {
                                Log.d("Show Error","layout");
                                String message=jsonObj.getString("message");
                                if(message!=null)
                                {

                                    layout_error.setVisibility(View.VISIBLE);
                                    txt_error_homepage.setText(""+message);
                                    success_layout.setVisibility(View.GONE);
                                }
                            }
                            else
                            {
                                layout_error.setVisibility(View.VISIBLE);
                                success_layout.setVisibility(View.GONE);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            progressbar.setVisibility(View.GONE);
                            layout_error.setVisibility(View.VISIBLE);
                            success_layout.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
                Toast.makeText(getApplicationContext(),"No internet",Toast.LENGTH_SHORT).show();
                layout_error.setVisibility(View.VISIBLE);
                success_layout.setVisibility(View.GONE);
            }
        });
        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    public void GetStateFromLatLng(double lat,double lng)
    {
        Geocoder geocoder = new Geocoder(HomeScreenActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            STATE_NAME=obj.getAdminArea();
            LOCAL_STATE_NAME=obj.getAdminArea();
            Log.v("State", "Name" + STATE_NAME);
            if(obj.getAdminArea()!=null) {
                txt_state_name.setText(STATE_NAME);
                GetNearByFestivalList();
            }
            else
            {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"State not found",Toast.LENGTH_SHORT).show();
            }
                // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_purchased, menu);
        action_purchaseapp = menu.findItem(R.id.action_purchaseapp);
        action_suggestion = menu.findItem(R.id.action_suggestion);
        action_add_marker = menu.findItem(R.id.action_add_marker);
        action_clear_marker = menu.findItem(R.id.action_clear_marker);
        action_my_wishlist = menu.findItem(R.id.action_my_wishlist);
        action_suggestions = menu.findItem(R.id.action_suggestions);
        if(prefs.getBoolean("All",false))
        {
            action_purchaseapp.setVisible(false);
            action_add_marker.setVisible(true);
            action_clear_marker.setVisible(true);
            action_my_wishlist.setVisible(true);
        }
        else if (prefs.getBoolean("map_upgrade",false))
        {
            action_add_marker.setVisible(true);
            action_clear_marker.setVisible(true);
        }
        else if (prefs.getBoolean("wish_list",false))
        {
            action_my_wishlist.setVisible(true);
            is_wishlist=true;
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_purchaseapp:
                Toast.makeText(getApplicationContext(), "purchase this app", Toast.LENGTH_SHORT).show();
                mHelper.launchPurchaseFlow(HomeScreenActivity.this, ITEM_SKU, 10001,
                        mPurchaseFinishedListener, "mypurchasetoken");
                return true;
            case R.id.action_clear_marker:
                if(myDb.EmptyDatabase())
                {
                    Toast.makeText(getApplicationContext(), "All markers are removed", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Unable to removed Markers", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_my_wishlist:
                Intent save_wish_list=new Intent(HomeScreenActivity.this,LocalMainFestivalListActivity.class);
                startActivity(save_wish_list);
                return true;
            case R.id.action_add_marker:
//                Toast.makeText(getApplicationContext(), "Add Marker", Toast.LENGTH_SHORT).show();
//                latLng_current=new LatLng(C,-101.4650291);
                  if(latLng_current!=null) {
                      Bundle data = new Bundle();
                      data.putString("LATITUDE", Double.toString(latLng_current.latitude));
                      data.putString("LONGITUDE", Double.toString(latLng_current.longitude));
                      Intent user_add_marker = new Intent(HomeScreenActivity.this, AddMarker.class);
                      user_add_marker.putExtra("DATA",data);
                      startActivity(user_add_marker);
                  }
                else
                  {
                      Toast.makeText(getApplicationContext(),"Please Access Later",Toast.LENGTH_SHORT).show();
                  }
                return true;
            case R.id.action_suggestion:
                ShowSuggestionsDialogue();
                return true;
            default:
        }
        return false;
    }


   public void WriteSharedPrefrences()
   {
       SharedPreferences.Editor editor = getSharedPreferences("PURCHASE_DETAIS", MODE_PRIVATE).edit();
       editor.putBoolean("All", true);
       editor.putBoolean("map_upgrade", true);
       editor.putBoolean("wish_list", true);
       editor.putBoolean("event_upgrade", true);
       editor.commit();
   }
    public void SetCustomTitle()
    {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Typeface tf = Typeface.createFromAsset(getAssets(), "Roycroft-Regular.otf");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.layout_actionbar);
        TextView title=(TextView)findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        title.setText("");
        title.setTypeface(tf);

    }
    public void GetUSerCuurentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        else
        {
            Log.d("Permission","AlreadyGranted");
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                 latLng_current=new LatLng(location.getLatitude(),location.getLongitude());
                                GetStateFromLatLng(location.getLatitude(),location.getLongitude());
                            }
                            else
                            {

                                if(getLocationMode(HomeScreenActivity.this) == 3)
                                {
// do stuff
                                }
                                else
                                {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                                progressbar.setVisibility(View.GONE);
                                layout_error.setVisibility(View.VISIBLE);
                                txt_error_homepage.setText("No Location Found Please Check your location for better experience select high accouracy mode");
                                success_layout.setVisibility(View.GONE);

//                                success_layout.setVisibility();
                            }
                        }
                    });
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "Granted");

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object
                                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                    GetStateFromLatLng(location.getLatitude(),location.getLongitude());
                                }
                                else {
                                    if(getLocationMode(HomeScreenActivity.this) == 3)
                                    {
// do stuff
                                    }
                                    else
                                     {
                                         startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    }
                                    progressbar.setVisibility(View.GONE);
                                    layout_error.setVisibility(View.VISIBLE);
                                    txt_error_homepage.setText("No Location Found Please Check your location for better experience select high accouracy mode");
                                    success_layout.setVisibility(View.GONE);
                                }
                            }
                        });
//          Toast.makeText(getActivity(),"Permission Grandted",Toast.LENGTH_SHORT).show();

            }
            else
            {
                Log.d("Permission","Denied");
//                Toast.makeText(getActivity(),"Permission Denieed",Toast.LENGTH_SHORT).show();
            }
            // Permission was denied. Display an error message.
        }
    }

    public void ShowSuggestionsDialogue() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.layout_dialogue_suggestions);
        u_email = (AutoCompleteTextView) dialog.findViewById(R.id.u_email);
        edt_suggestion=dialog.findViewById(R.id.edt_suggestion);
        Button btn_close = dialog.findViewById(R.id.btn_close);
        LinearLayout bedLcl = dialog.findViewById(R.id.bedLcl);
        btn_close.setOnClickListener(new View.OnClickListener() {
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
            String to,suggestion;
                to=u_email.getText().toString();
                suggestion=edt_suggestion.getText().toString();
                if(to==null||to.equals("")||suggestion==null||suggestion.equals(""))
                {

                }
                else
                {
                    SendEmail(to, suggestion);
                }
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthLcl = (int) (displayMetrics.widthPixels * 0.9f);
        int heightLcl = (int) (displayMetrics.heightPixels * 0.5f);
        FrameLayout.LayoutParams paramsLcl = (FrameLayout.LayoutParams)
                bedLcl.getLayoutParams();
        paramsLcl.width = widthLcl;
        paramsLcl.height = heightLcl;
        paramsLcl.gravity = Gravity.CENTER;
        dialog.show();

        Window window = dialog.getWindow();
        bedLcl.setLayoutParams(paramsLcl);
    }
    public void IntiatePayment()
    {
        String base64EncodedPublicKey = getResources().getString(R.string.string_inapp_key);
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d("RESULT","IS"+result);
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app Billing setup failed: " +
                            result);
                }
                else
                {
                    Log.d(TAG, "In-app Billing is set up OK");

                        mHelper.queryInventoryAsync(mGotInventoryListener);

                    }
            }
        });
        Log.d("INAPPSUPPORTED","AVAILABLE"+mHelper.subscriptionsSupported());
//        Bundle ownedItems = mHelper.getPurchases(3, getPackageName(), "inapp", null);
    }
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(ITEM_SKU)) {
                consumeItem();

            }

        }
    };
    public void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                // Handle failure
            } else {
                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                        mConsumeFinishedListener);
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess())
                    {
                        Log.d("AllItem","purchased is ="+purchase);
//                        ShowDialogueAddClass();
                        WriteSharedPrefrences();
                    }
                    else
                    {
                        // handle error
                    }
                }
            };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {

        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
//            Log.d(TAG, "Query inventory finished."+inventory.hasDetails(ITEM_SKU3));
            if (result.isFailure()) {
                Log.d(TAG, "Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");
            if(inventory.hasPurchase(ITEM_SKU))
            {
                Log.d("ALLITEMS", "already purchased : " );
                action_purchaseapp.setVisible(false);
                action_add_marker.setVisible(true);
                action_clear_marker.setVisible(true);
                action_my_wishlist.setVisible(true);
                WriteSharedPrefrences();

            }
            if(inventory.hasPurchase(ITEM_SKU2))
            {

                Log.d("WISHLIST", "already purchased : ");
                action_my_wishlist.setVisible(true);
//                is_wishlist=true;
                SharedPreferences.Editor editor = getSharedPreferences("PURCHASE_DETAIS", MODE_PRIVATE).edit();
                editor.putBoolean("wish_list", true);
                editor.commit();
            }
            if(inventory.hasPurchase(ITEM_SKU1))
            {

                Log.d("EVENT", "already purchased : ");
                action_my_wishlist.setVisible(true);
                SharedPreferences.Editor editor = getSharedPreferences("PURCHASE_DETAIS", MODE_PRIVATE).edit();
                editor.putBoolean("event_upgrade", true);
                editor.commit();
            }
            if(inventory.hasPurchase(ITEM_SKU3))
            {
                action_add_marker.setVisible(true);
                action_clear_marker.setVisible(true);
                SharedPreferences.Editor editor = getSharedPreferences("PURCHASE_DETAIS", MODE_PRIVATE).edit();
                editor.putBoolean("map_upgrade", true);
                editor.commit();
                Log.d("MAPS", "already purchased : ");
            }

        }
    };
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener2 = new IabHelper.QueryInventoryFinishedListener() {

        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
//            Log.d(TAG, "Query inventory finished."+inventory.hasDetails(ITEM_SKU3));
            if (result.isFailure()) {
                Log.d(TAG, "Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");
            if(inventory.hasPurchase(ITEM_SKU))
            {

            Log.d("ALL","PURCHAED");
            }
            if(inventory.hasPurchase(ITEM_SKU2))
            {
                Log.d("WISHLIST","PURCHAED");

            }
            if(inventory.hasPurchase(ITEM_SKU1))
            {
                Log.d("STAGE","PURCHAED");
                SharedPreferences.Editor editor = getSharedPreferences("PURCHASE_DETAIS", MODE_PRIVATE).edit();
                editor.putBoolean("event_upgrade", true);
                editor.commit();

            }
            if(inventory.hasPurchase(ITEM_SKU3))
            {
                Log.d("MAPS","PURCHAED");
                SharedPreferences.Editor editor = getSharedPreferences("PURCHASE_DETAIS", MODE_PRIVATE).edit();
                editor.putBoolean("map_upgrade", true);
                editor.commit();
            }

        }
    };

    public void SendEmail(String to,String message)
    {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
        email.putExtra(Intent.EXTRA_SUBJECT, "test");
        email.putExtra(Intent.EXTRA_TEXT, message);

        //need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }
    public void ShowDialogueAddClass() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.layout_success_purchase);
        LinearLayout bedLcl = dialog.findViewById(R.id.bedLcl);
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_contiue);
//        dialogButton.setText("Save");
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthLcl = (int) (displayMetrics.widthPixels * 0.9f);
        int heightLcl = (int) (displayMetrics.heightPixels * 0.3f);
        FrameLayout.LayoutParams paramsLcl = (FrameLayout.LayoutParams)
                bedLcl.getLayoutParams();
        paramsLcl.width = widthLcl;
        paramsLcl.height = heightLcl;
        paramsLcl.gravity = Gravity.CENTER;
        dialog.show();

        Window window = dialog.getWindow();
        bedLcl.setLayoutParams(paramsLcl);
    }
    public void SetAlarm()
    {

//     AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
//        Calendar calendar=new GregorianCalendar();
//        calendar = GregorianCalendar.getInstance();
//      //  calendar.setTimeInMillis(System.currentTimeMillis());
//        //calendar.clear();
//
//        calendar.set(2019,01,16,11,28);
//        Intent intent=new Intent("your.company.blah.mybroadcast");
//        intent.putExtra("test", "ValueReceived1");
//        final int _id = (int) System.currentTimeMillis();
//        PendingIntent broadcast=PendingIntent.getBroadcast(this,_id,intent,0);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),broadcast);
//        }
    }
    public void SetAlarm1()
    {

//        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
//        Calendar calendar=new GregorianCalendar();
//        calendar = GregorianCalendar.getInstance();
//        //  calendar.setTimeInMillis(System.currentTimeMillis());
//        //calendar.clear();
//        calendar.set(2019,01,16,11,32);
//        Intent intent=new Intent("your.company.blah.mybroadcast");
//        intent.putExtra("test", "ValueReceived2");
//        final int _id = (int) System.currentTimeMillis();
//        PendingIntent broadcast=PendingIntent.getBroadcast(this,_id,intent,0);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),broadcast);
//        }
    }
public void ConvertTimertt24Hours() {

        Date date=new Date("2019/1/16 8:22 am");
        System.out.println("Time in 24Hours ="+new SimpleDateFormat("HH:mm").format(date));


}
    public int getLocationMode(Context context)
    {
        int value = 0;
        try
        {
            value= Settings.Secure.getInt(getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
return value;
    }
}

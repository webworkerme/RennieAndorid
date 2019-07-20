package com.demo.festivalapp.festivalapp.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.festivalapp.festivalapp.Adapters.FestivalEventListAdapter;
import com.demo.festivalapp.festivalapp.Models.DatabaseHelper;
import com.demo.festivalapp.festivalapp.Models.EventModel;
import com.demo.festivalapp.festivalapp.Models.Festivals;
import com.demo.festivalapp.festivalapp.R;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.util.IabHelper;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.util.IabResult;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.util.Inventory;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.util.Purchase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.BASE_URL;
import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.ChecksSend;
import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.festival_Name;
import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.festival_id;

//import com.anjlab.android.iab.v3.BillingProcessor;
//import com.anjlab.android.iab.v3.TransactionDetails;

public class FestivalEventListActivity extends AppCompatActivity   {
    private List<EventModel> movieList = new ArrayList<>();
    private List<EventModel> filtetimeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FestivalEventListAdapter mAdapter;
    ProgressBar progressbar_event_list;
    TextView festival_name;
    LinearLayout layout_error;
    Button btn_retry;
    boolean is_added;
    DatabaseHelper myDb;
    LinearLayout filter_stage,filter_artist,filter_time;
    SharedPreferences prefs;
    LinearLayout layout_paid_users;
    TextView tv_stage,tv_artist;
    private static final String TAG =
            "InAppBilling";
    IabHelper mHelper;
    static final String ITEM_SKU = "maps_upgrade";
    static final String ITEM_SKU1 = "stagefilter_price";
    static final String ITEM_SKU2 = "wishlist_upgrade";
    static final String ITEM_SKU3 = "reminder_price";
    Festivals festivals;
    String week_days;
    String repeat_week_days;
    String holidays;
    String festival_startdate;
    String festival_enddate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival_event_list);
        myDb = new DatabaseHelper(this);
        IntiatePayment();
        prefs = getSharedPreferences("PURCHASE_DETAIS", MODE_PRIVATE);
        int total_save_festval_size = myDb.getAllFestivaData().getCount();
        Log.d("FESTIVAL_TABLE", "is" + total_save_festval_size);
        GetData();
        GetAllSaveFestivalList();
        SetCustomTitle();
        SetUiWidget();
        filter_time.setBackgroundColor(Color.parseColor("#25A8D8"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            filter_stage.setBackground(ContextCompat.getDrawable(FestivalEventListActivity.this, R.drawable.my_custom_background_linear));
            filter_artist.setBackground(ContextCompat.getDrawable(FestivalEventListActivity.this, R.drawable.my_custom_background_linear));
        }
        GetFestivaletail(festival_id,"time");
        if(prefs.getBoolean("event_upgrade",false))
        {

        }
        else
        {
            ShowDialogue();
        }
    }

    public void GetFestivaletail(String id, final String order_by) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = BASE_URL + "festival_id=" + ChecksSend(id)+"&"+"order_by="+order_by;
        Log.d("GETEVENTS", "URL" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
//                        Log.d("Festival Events","is"+response.substring(0,500));
                        progressbar_event_list.setVisibility(View.GONE);
                        if(order_by.equals("time"))
                        {
                            MapJsonWithTime(response);
                        }
                        else if(order_by.equals("theatre"))
                        {
                        MapJsonWithStage(response);
                        }
                        else if(order_by.equals("artist"))
                        {
                    MapJsonWithArtist(response);
                        }
                        else
                        {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressbar_event_list.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                layout_error.setVisibility(View.VISIBLE);
            }
        });
        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void SetUiWidget()
    {
        layout_paid_users = findViewById(R.id.layout_paid_users);
        tv_stage=findViewById(R.id.tv_stage);
        tv_artist=findViewById(R.id.tv_artist);
        if(prefs.getBoolean("event_upgrade",false))
        {
            tv_stage.setText("Stage");
            tv_artist.setText("Artist");
        layout_paid_users.setVisibility(View.VISIBLE);
        }
        else
        {
            layout_paid_users.setVisibility(View.GONE);
        }
//        bp.subscribe(FestivalEventListActivity.this, "stagefilter_price");
        layout_error = findViewById(R.id.layout_error);
        btn_retry = findViewById(R.id.btn_retry);
        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar_event_list.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                layout_error.setVisibility(View.GONE);

            }
        });
        Typeface tf = Typeface.createFromAsset(getAssets(), "Roycroft-Regular.otf");
        festival_name = findViewById(R.id.festival_name);
        festival_name.setText("" + festival_Name);
        festival_name.setTypeface(tf);
        progressbar_event_list = findViewById(R.id.progressbar_event_list);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_event);
        mAdapter = new FestivalEventListAdapter(filtetimeList, this,festivals);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
       //Getting filter icons
        filter_time=findViewById(R.id.filter_time);
        filter_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    filter_time.setBackgroundColor(Color.parseColor("#25A8D8"));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    {
                        filter_stage.setBackground(ContextCompat.getDrawable(FestivalEventListActivity.this, R.drawable.my_custom_background_linear));
                        filter_artist.setBackground(ContextCompat.getDrawable(FestivalEventListActivity.this, R.drawable.my_custom_background_linear));
                    }
                    movieList.clear();
                    filtetimeList.clear();
                    mAdapter.notifyDataSetChanged();
                    progressbar_event_list.setVisibility(View.VISIBLE);
                    GetFestivaletail(festival_id, "time");
                }
                catch (Exception ex)
                {

                }
            }
        });
        filter_stage=findViewById(R.id.filter_stage);
        filter_stage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    try
                    {
                        filter_stage.setBackgroundColor(Color.parseColor("#25A8D8"));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                        {
                            filter_time.setBackground(ContextCompat.getDrawable(FestivalEventListActivity.this, R.drawable.my_custom_background_linear));
                            filter_artist.setBackground(ContextCompat.getDrawable(FestivalEventListActivity.this, R.drawable.my_custom_background_linear));
                        }
                        movieList.clear();
                        filtetimeList.clear();
                        mAdapter.notifyDataSetChanged();
                        progressbar_event_list.setVisibility(View.VISIBLE);
                        GetFestivaletail(festival_id, "theatre");
                    }
                    catch (Exception ex)
                    {

                    }


            }
        });
        filter_artist=findViewById(R.id.filter_artist);
        filter_artist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    try
                    {
                        filter_artist.setBackgroundColor(Color.parseColor("#25A8D8"));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                        {
                            filter_time.setBackground(ContextCompat.getDrawable(FestivalEventListActivity.this, R.drawable.my_custom_background_linear));
                            filter_stage.setBackground(ContextCompat.getDrawable(FestivalEventListActivity.this, R.drawable.my_custom_background_linear));
                        }
                        movieList.clear();
                        filtetimeList.clear();
                        mAdapter.notifyDataSetChanged();
                        progressbar_event_list.setVisibility(View.VISIBLE);
                        GetFestivaletail(festival_id, "artist");
                    }
                    catch (Exception ex)
                    {

                    }

            }
        });
    }

    public void SetCustomTitle() {
        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            Typeface tf = Typeface.createFromAsset(getAssets(), "Roycroft-Regular.otf");
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.layout_actionbar);
            TextView title = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
            title.setText("");
            title.setTypeface(tf);
        } catch (Exception ex) {

        }
    }

    public void GetAllSaveFestivalList()
    {
        Cursor cursor = myDb.getAllFestivaData();
        if (cursor.moveToFirst()) {
            do {
                Log.d("Data", "is" + cursor.getString(cursor.getColumnIndex("TIME")));
                Log.d("Data", "is" + cursor.getString(cursor.getColumnIndex("NAME")));
                Log.d("Data", "is" + cursor.getString(cursor.getColumnIndex("PLACE")));
            } while (cursor.moveToNext());
        }
    }
   public void MapJsonWithTime(String response)
   {
       try {
           JSONObject jsonObj = new JSONObject(response);
           String response_value = jsonObj.getString("response");
           if (response_value.equals("1")) {
               JSONArray json2 = jsonObj.getJSONArray("data");
               Log.d("Total", "Detial" + json2.length());
               for (int i = 0; i < json2.length(); i++) {
                   JSONObject c = json2.getJSONObject(i);
                   Log.d("ArtistsID ", "is" + c.getString("ArtistsID"));
                   String formatted_string = c.getString("StartTime");
                   EventModel movie = new EventModel(formatted_string, c.getString("ShowName"), c.getString("TheatreName"), "orginal", c.getString("ArtistsName"),
                           c.getString("ShowStartDate"),"time",week_days,repeat_week_days,holidays,c.getString("ShowEndDate")
                           ,c.getString("ShowID"),festival_startdate,festival_enddate);
                   movieList.add(movie);
               }
               try {
                   int listsize = movieList.size();
                   for (int i = 0; i <= movieList.size(); i++) {
                       is_added = false;
                       String item = movieList.get(i).getStartTime();
                       if(i>0)
                       {
                           Log.d("OLD","ELEMNET"+movieList.get(i - 1).getStartTime());
                           if (movieList.get(i - 1).getStartTime().equals(item)) {
                               movieList.get(i).setType("copy");
                           }
                       }
                       EventModel movie = new EventModel(movieList.get(i).getStartTime(), movieList.get(i).getEventname(),
                               movieList.get(i).getEventplace(), movieList.get(i).getType(),
                               movieList.get(i).getArtistsName(),
                               movieList.get(i).getStart_date(),"time",
                               week_days,repeat_week_days,holidays,
                               movieList.get(i).getEnd_date(),
                               movieList.get(i).getFest_id(),
                               movieList.get(i).getFestival_startdate(),
                               movieList.get(i).getFestival_enddate());
                       filtetimeList.add(movie);
                   }
                   Log.d("MOVIES","LIST_SIZE"+movieList.size());
                   for (int i=0;i<filtetimeList.size();i++)
                   {
                       Log.e("RECHECK","ELEMNETS"+filtetimeList.get(i).getStartTime()+""+filtetimeList.get(i).getType());
                   }
                   mAdapter.notifyDataSetChanged();
               } catch (Exception ex) {
//                                    Toast.makeText(getApplicationContext(), "Unwated Error Occured", Toast.LENGTH_SHORT).show();
               }

           } else {
               progressbar_event_list.setVisibility(View.GONE);

               Log.d("Show Error", "layout");
           }
       } catch (JSONException e) {
           progressbar_event_list.setVisibility(View.GONE);
           e.printStackTrace();
       }
   }
  public void MapJsonWithStage(String response)
  {
      try {
          JSONObject jsonObj = new JSONObject(response);
          String response_value = jsonObj.getString("response");
          if (response_value.equals("1")) {
              JSONArray json2 = jsonObj.getJSONArray("data");
              Log.d("Total", "Detial" + json2.length());
              for (int i = 0; i < json2.length(); i++) {
                  JSONObject c = json2.getJSONObject(i);
                  Log.d("ArtistsID ", "is" + c.getString("ArtistsID"));
                  String formatted_string = c.getString("StartTime");
                  EventModel movie = new EventModel(formatted_string, c.getString("ShowName"), c.getString("TheatreName"), "orginal", c.getString("ArtistsName"),
                          c.getString("ShowStartDate"),"theatre",week_days,repeat_week_days,holidays,c.getString("ShowEndDate")
                          ,c.getString("ShowID"),festival_startdate,festival_enddate);
                  movieList.add(movie);
              }
              try {
                  int listsize = movieList.size();
                  for (int i = 0; i <= movieList.size(); i++) {
                      is_added = false;
                      String item = movieList.get(i).getEventplace();
                      if(i>0)
                      {
//                          Log.d("OLD","ELEMNET"+movieList.get(i - 1).getEventplace());
                          if (movieList.get(i - 1).getEventplace().equals(item)) {
                              movieList.get(i).setType("copy");
                          }
                      }
                      EventModel movie = new EventModel(movieList.get(i).getStartTime(), movieList.get(i).getEventname(),
                              movieList.get(i).getEventplace(), movieList.get(i).getType(),
                              movieList.get(i).getArtistsName(),
                              movieList.get(i).getStart_date(),"theatre",week_days,repeat_week_days,holidays,
                              movieList.get(i).getEnd_date(),
                              movieList.get(i).getFest_id(),
                              movieList.get(i).getFestival_startdate(),
                              movieList.get(i).getFestival_enddate());
                      filtetimeList.add(movie);
                  }
                  Log.d("MOVIES","LIST_SIZE"+movieList.size());
                  for (int i=0;i<filtetimeList.size();i++)
                  {
                      Log.e("RECHECK","ELEMNETS"+filtetimeList.get(i).getEventplace()+""+filtetimeList.get(i).getType());
                  }
                  mAdapter.notifyDataSetChanged();
              } catch (Exception ex) {
//                                    Toast.makeText(getApplicationContext(), "Unwated Error Occured", Toast.LENGTH_SHORT).show();
              }

          } else {
              progressbar_event_list.setVisibility(View.GONE);

              Log.d("Show Error", "layout");
          }
      } catch (JSONException e) {
          progressbar_event_list.setVisibility(View.GONE);
          e.printStackTrace();
      }

  }
   public void MapJsonWithArtist(String response)
   {
       try {
           JSONObject jsonObj = new JSONObject(response);
           String response_value = jsonObj.getString("response");
           if (response_value.equals("1")) {
               JSONArray json2 = jsonObj.getJSONArray("data");
               Log.d("Total", "Detial" + json2.length());
               for (int i = 0; i < json2.length(); i++) {
                   JSONObject c = json2.getJSONObject(i);
                   Log.d("ArtistsID ", "is" + c.getString("ArtistsID"));
                   String formatted_string = c.getString("StartTime");
                   EventModel movie = new EventModel(formatted_string, c.getString("ShowName"), c.getString("TheatreName"), "orginal", c.getString("ArtistsName"),
                           c.getString("ShowStartDate"),"artist",week_days,repeat_week_days,holidays,c.getString("ShowEndDate")
                           ,c.getString("ShowID"),
                           festival_startdate,festival_enddate);
                   movieList.add(movie);
               }
               try {
                   int listsize = movieList.size();
                   for (int i = 0; i <= movieList.size(); i++) {
                       is_added = false;
                       String item = movieList.get(i).getArtistsName();
                       if(i>0)
                       {
                           Log.d("OLD","ELEMNET"+movieList.get(i - 1).getStartTime());
                           if (movieList.get(i - 1).getArtistsName().equals(item)) {
                               movieList.get(i).setType("copy");
                           }
                       }
                       EventModel movie = new EventModel(movieList.get(i).getStartTime(), movieList.get(i).getEventname(),
                               movieList.get(i).getEventplace(), movieList.get(i).getType(),
                               movieList.get(i).getArtistsName(),
                               movieList.get(i).getStart_date(),"artist",week_days,repeat_week_days,holidays,
                               movieList.get(i).getEnd_date(),
                               movieList.get(i).getFest_id(),
                               movieList.get(i).getFestival_startdate(),
                               movieList.get(i).getFestival_enddate());
                       filtetimeList.add(movie);
                   }
                   Log.d("MOVIES","LIST_SIZE"+movieList.size());
                   for (int i=0;i<filtetimeList.size();i++)
                   {
                       Log.e("RECHECK","ELEMNETS"+filtetimeList.get(i).getStartTime()+""+filtetimeList.get(i).getType());
                   }
                   mAdapter.notifyDataSetChanged();
               } catch (Exception ex) {
//                                    Toast.makeText(getApplicationContext(), "Unwated Error Occured", Toast.LENGTH_SHORT).show();
               }

           } else {
               progressbar_event_list.setVisibility(View.GONE);

               Log.d("Show Error", "layout");
           }
       } catch (JSONException e) {
           progressbar_event_list.setVisibility(View.GONE);
           e.printStackTrace();
       }
   }

    public void ShowDialogue() {
        final Dialog dialog = new Dialog(this);
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.layout_dialogue_info);
        LinearLayout bedLcl = dialog.findViewById(R.id.bedLcl);
        RadioButton radio_event_upgrade=dialog.findViewById(R.id.radio_event_upgrade);
        radio_event_upgrade.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    dialog.dismiss();
                    mHelper.launchPurchaseFlow(FestivalEventListActivity.this, ITEM_SKU1, 10001,
                            mPurchaseFinishedListener, "mypurchasetoken");
                }
                else
                {

                }
            }
        });
        RadioButton radio_map_upgrade=dialog.findViewById(R.id.radio_map_upgrade);
        radio_map_upgrade.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    dialog.dismiss();
                    mHelper.launchPurchaseFlow(FestivalEventListActivity.this, ITEM_SKU, 10001,
                            mPurchaseFinishedListener, "mypurchasetoken");
                }
                else
                {

                }
            }
        });
        RadioButton radio_wishlist_upgrade=dialog.findViewById(R.id.radio_wishlist_upgrade);
        radio_wishlist_upgrade.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //
                if(isChecked)
                {
                    dialog.dismiss();
                    mHelper.launchPurchaseFlow(FestivalEventListActivity.this, ITEM_SKU2, 10001,
                            mPurchaseFinishedListener, "mypurchasetoken");
                }
                else
                {

                }
            }
        });
        RadioButton radio_all_upgrade=dialog.findViewById(R.id.radio_all_upgrade);
        radio_all_upgrade.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //
                if(isChecked)
                {
                    dialog.dismiss();
                    mHelper.launchPurchaseFlow(FestivalEventListActivity.this, ITEM_SKU3, 10001,
                            mPurchaseFinishedListener, "mypurchasetoken");
                }
                else
                {

                }
            }
        });
        RadioButton radio_donothing=dialog.findViewById(R.id.radio_donothing);
        radio_donothing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //
                if(isChecked)
                {
                    dialog.dismiss();
                }
                else
                {

                }
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthLcl = (int) (displayMetrics.widthPixels * 0.9f);
        int heightLcl = (int) (displayMetrics.heightPixels * 0.8f);
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
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app Billing setup failed: " +
                            result);
                } else {
                    Log.d(TAG, "In-app Billing is set up OK");
                }
            }
        });
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
            else if (purchase.getSku().equals(ITEM_SKU2)) {
                consumeItem();

            }
            else if (purchase.getSku().equals(ITEM_SKU3)) {
                consumeItem();

            }
            else if (purchase.getSku().equals(ITEM_SKU3)) {
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
            }
            else
            {
                if(inventory.getPurchase(ITEM_SKU).equals(ITEM_SKU))
                {
                    mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                            mConsumeFinishedListener);
                }
                else if(inventory.getPurchase(ITEM_SKU1).equals(ITEM_SKU1))
                {
                    mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU1),
                            mConsumeFinishedListener);
                }
                else if(inventory.getPurchase(ITEM_SKU2).equals(ITEM_SKU2))
                {
                    mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU2),
                            mConsumeFinishedListener);
                }
                else if(inventory.getPurchase(ITEM_SKU3).equals(ITEM_SKU3))
                {
                    mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU3),
                            mConsumeFinishedListener);
                }
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess())
                    {

//                        ShowDialogueAddClass();
                        Log.d("Item","purchased is"+purchase);
                        if(purchase.getOrderId().equals(ITEM_SKU))
                        {
                            SharedPreferences.Editor editor = getSharedPreferences("PURCHASE_DETAIS", MODE_PRIVATE).edit();
                            editor.putBoolean("map_upgrade", true);
                            editor.commit();
                        }
                        if(purchase.getOrderId().equals(ITEM_SKU1))
                        {
                            SharedPreferences.Editor editor = getSharedPreferences("PURCHASE_DETAIS", MODE_PRIVATE).edit();
                            editor.putBoolean("event_upgrade", true);
                            editor.commit();
                        }
                        if(purchase.getOrderId().equals(ITEM_SKU2))
                        {
                            SharedPreferences.Editor editor = getSharedPreferences("PURCHASE_DETAIS", MODE_PRIVATE).edit();
                            editor.putBoolean("wish_list", true);
                            editor.commit();
                        }
                        if(purchase.getOrderId().equals(ITEM_SKU3))
                        {
                            SharedPreferences.Editor editor = getSharedPreferences("PURCHASE_DETAIS", MODE_PRIVATE).edit();
                            editor.putBoolean("All", true);
                            editor.commit();
                        }
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
   public void GetData()
   {
       try {
           Bundle data = getIntent().getExtras();
           if (data != null)
           {
               festivals = (Festivals) data.getParcelable("festival");
               week_days=festivals.getWeekdays();
               repeat_week_days=festivals.getRepeatWeekday();
               holidays=festivals.getHoliday();
               festival_startdate=festivals.getStartDate();
               festival_enddate=festivals.getEnd_date();
           }
       }
       catch (Exception ex)
       {

       }
   }
}
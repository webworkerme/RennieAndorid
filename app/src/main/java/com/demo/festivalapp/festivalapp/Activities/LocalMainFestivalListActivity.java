package com.demo.festivalapp.festivalapp.Activities;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.festivalapp.festivalapp.Adapters.LocalFestivalListAdapter;
import com.demo.festivalapp.festivalapp.Adapters.LocalNearbyFestivalListAdapter;
import com.demo.festivalapp.festivalapp.Models.DatabaseHelper;
import com.demo.festivalapp.festivalapp.Models.LocalFestivalListModel;
import com.demo.festivalapp.festivalapp.Models.SaveStatesModel;
import com.demo.festivalapp.festivalapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.ChecksSend;
import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.LOCAL_STATE_NAME;
import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.URL_GET_FESTIVAL;


public class LocalMainFestivalListActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    List<String> state_list;
    ArrayList<String> demo_list;
    private List<LocalFestivalListModel> movieList = new ArrayList<>();
    private List<SaveStatesModel> nearby_festival = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView recyclerview_state;
    private LocalFestivalListAdapter mAdapter;
    private LocalNearbyFestivalListAdapter mAdapter1;
    TextView txt_state_name;
    Typeface custom_font_statename;
    LinearLayout layout_festival, layout_states;
    String  image_state;
    ImageView image_state_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_main_festival_list);
        myDb = new DatabaseHelper(this);
        SetCustomTitle();
        SetUIWidgets();
        demo_list = new ArrayList<>();
        GetSqliteData(LOCAL_STATE_NAME);
        GetStateImage(LOCAL_STATE_NAME);
    }

    public void GetSqliteData(String state_name) {
        int i = 0;
        Log.d("SIZELOCALFESTIVALS", "ARE" + myDb.getAllSavedFestivalList(state_name));
        if (myDb.getAllSavedFestivalList(state_name).getCount() > 0) {
            Cursor cursor = myDb.getAllSavedFestivalList(state_name);
            if (cursor.moveToFirst()) {
                do {
                    Log.d("Data", "is" + cursor.getString(cursor.getColumnIndex("FESTIVALNAME")));
                    Log.d("State", "is" + cursor.getString(cursor.getColumnIndex("FESTIVALSTATE")));
                    Log.d("LOCALState", "is" + LOCAL_STATE_NAME);
                    if (cursor.getString(cursor.getColumnIndex("FESTIVALSTATE")).equals(state_name)) {
                        LocalFestivalListModel festivalListModel = new LocalFestivalListModel(String.valueOf(cursor.getInt(cursor.getColumnIndex("ID"))),
                                cursor.getString(cursor.getColumnIndex("FESTIVALID")), cursor.getString(cursor.getColumnIndex("FESTIVALNAME")),
                                cursor.getString(cursor.getColumnIndex("FESTIVALDATE")), cursor.getString(cursor.getColumnIndex("FESTIVALADDRESS")),
                                cursor.getString(cursor.getColumnIndex("FESTIVALIMAGE")));
                        if (demo_list.contains(cursor.getString(cursor.getColumnIndex("FESTIVALID")))) {

                        } else {
                            movieList.add(festivalListModel);
                            demo_list.add(cursor.getString(cursor.getColumnIndex("FESTIVALID")));
                        }


                    }
                } while (cursor.moveToNext());
                float density = getApplicationContext().getResources().getDisplayMetrics().density;

                if (movieList.size() < 2) {

                    if ((int) density == 3) {
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                                layout_festival.getLayoutParams();
                        params.weight = 1f;
                        layout_festival.setLayoutParams(params);
                        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams)
                                layout_states.getLayoutParams();
                        params1.weight = 1f;
                        layout_states.setLayoutParams(params1);
                    } else if ((int) density < 3) {

                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                                layout_festival.getLayoutParams();
                        params.weight = 1f;
                        layout_festival.setLayoutParams(params);
                        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams)
                                layout_states.getLayoutParams();
                        params1.weight = 1.5f;
                        layout_states.setLayoutParams(params1);
                    }
                } else if (movieList.size() == 2) {
                    if ((int) density == 3) {
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                                layout_festival.getLayoutParams();
                        params.weight = 1.3f;
                        layout_festival.setLayoutParams(params);
                        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams)
                                layout_states.getLayoutParams();
                        params1.weight = 0.7f;
                        layout_states.setLayoutParams(params1);
                    } else if ((int) density < 3) {
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                                layout_festival.getLayoutParams();
                        params.weight = 1f;
                        layout_festival.setLayoutParams(params);
                        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams)
                                layout_states.getLayoutParams();
                        params1.weight = 1f;
                        layout_states.setLayoutParams(params1);
                    }
                } else if (movieList.size() == 3) {
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
                PopulateStates(state_name);

            }

        } else {

        }
    }

    public void SetUIWidgets() {
        layout_festival = findViewById(R.id.layout_festival);
        layout_states = findViewById(R.id.layout_states);
        image_state_img=findViewById(R.id.image_state);
        IntiateOtherViews();
        IntiateReclerViews();
    }

    public void IntiateReclerViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new LocalFestivalListAdapter(movieList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerview_state = (RecyclerView) findViewById(R.id.recyclerview_state);
        mAdapter1 = new LocalNearbyFestivalListAdapter(nearby_festival, this);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        recyclerview_state.setLayoutManager(mLayoutManager1);
        recyclerview_state.setItemAnimator(new DefaultItemAnimator());
        recyclerview_state.setAdapter(mAdapter1);
    }

    public void PopulateStates(String state_name) {
        state_list = new ArrayList<String>();
        Cursor cursor = myDb.getAllSavedFestivalList(state_name);
        if (cursor.moveToFirst()) {
            do {
                Log.d("Data", "is" + cursor.getString(cursor.getColumnIndex("FESTIVALNAME")));
                Log.d("State", "is" + cursor.getString(cursor.getColumnIndex("FESTIVALSTATE")));
                state_list.add(cursor.getString(cursor.getColumnIndex("FESTIVALSTATE")));

            } while (cursor.moveToNext());
            HashSet<String> hashSet = new HashSet<String>();
            hashSet.addAll(state_list);
            state_list.clear();
            state_list.addAll(hashSet);
            //Alphabetic sorting.
            Collections.sort(state_list);

            Log.d("REARRANGE", "STATE" + state_list.size());
            for (int i = 0; i < state_list.size(); i++) {
                SaveStatesModel saveStatesModel = new SaveStatesModel(state_list.get(i));
                nearby_festival.add(saveStatesModel);
            }
            mAdapter1.notifyDataSetChanged();
        }
    }

    public void IntiateOtherViews() {
        custom_font_statename = Typeface.createFromAsset(getAssets(), "Gobold Regular.ttf");
        txt_state_name = findViewById(R.id.txt_state_name);
        txt_state_name.setTypeface(custom_font_statename);
        txt_state_name.setText(LOCAL_STATE_NAME);
    }

    public void SetCustomTitle() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Typeface tf = Typeface.createFromAsset(getAssets(), "Roycroft-Regular.otf");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.layout_actionbar);
        TextView title = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        title.setText("");
        title.setTypeface(tf);

    }

    public void GetStateImage(String state_name)
    {
        image_state=null;
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =URL_GET_FESTIVAL+"state="+ChecksSend(state_name.replaceAll(" ", "%20"));
        Log.e("GETFestivalURL","is"+url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    //                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String resposnse_value = jsonObj.getString("response");
                            Log.d("Response Value", "is" + resposnse_value);
                            if (resposnse_value.equals("1")) {
                                JSONArray json2 = jsonObj.getJSONArray("data");
                                Log.d("Data", "length" + json2.length());

                                for (int i = 0; i < json2.length(); i++) {
//                                    if (i == 4) {
//                                        break;
//                                    } else {
                                    JSONObject c = json2.getJSONObject(i);

                                  Log.d("STATE_IMAGE","IS"+image_state);
                                if(image_state!=null)
                                {

                                }
                                else
                                {
                                    image_state= c.getString("state_image");
                                }
                                }
                                Picasso.get().load("http://renfestapp.com/"+image_state).into(image_state_img);
                            }
                        }
                        catch (Exception ex)
                        {
                    Log.d("JSON_ExCEPTION","IS :"+ex.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
                Toast.makeText(getApplicationContext(),"No internet",Toast.LENGTH_SHORT).show();
//                layout_error.setVisibility(View.VISIBLE);
//                success_layout.setVisibility(View.GONE);
            }
        });
        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    }

}


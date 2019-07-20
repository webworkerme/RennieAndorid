package com.demo.festivalapp.festivalapp.Activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.festivalapp.festivalapp.Models.Festivals;
import com.demo.festivalapp.festivalapp.Models.PoliciesModel;
import com.demo.festivalapp.festivalapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.URL_GET_FESTIVAL_POLICIES;

public class FestivalDetailActivty extends AppCompatActivity {

    TextView festival_date,festival_location,festival_name;
    ImageView img_info;
    Festivals festivals;
    ProgressBar info_homeprogress;
    LinearLayout detail_layout_address,detail_layout_timing,detail_layout_cost,detail_layout_pets,detail_layout_precaution,
            layout_miltary,layout_parking,layout_shuttle,layout_camping,layout_cahrity,layout_weapons,layout_food,layout_wheelchair,
            layout_costume,layout_alcohol,layout_tickets;
    TextView festival_cost,festival_pets,festival_smoking;
    //new textviews
    TextView festival_tickets,festival_military_discount,festival_shuttle,festival_parking,festival_charity,
            festival_camping,festival_weapons,festival_food,festival_wheel,festival_costume,festival_alcohol;
    PoliciesModel p_model;
    ImageView img_alcohol,img_costume,img_wheelchair,img_food,img_weapons,
            img_camping,img_charity,img_parking,img_shuttle,img_miltary,img_tickets,
            img_smoking,img_pets,img_cost,img_hours,img_location;
    ProgressBar progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_festival_detail);
        SetCustomTitle();
        SetUiWidgets();
        PopulatetData();

    }

    public void SetUiWidgets()
    {
        progressbar=findViewById(R.id.progressbar);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "Roycroft-Regular.otf");
        festival_name=findViewById(R.id.festival_name);
        festival_name.setTypeface(custom_font);
        festival_name.setVisibility(View.GONE);
        festival_location=findViewById(R.id.festival_location);
        festival_date=findViewById(R.id.festival_date);
        img_info=findViewById(R.id.img_info);
        info_homeprogress=findViewById(R.id.info_homeprogress);
//intiate linear layout
        detail_layout_address=findViewById(R.id.detail_layout_address);
        detail_layout_timing=findViewById(R.id.detail_layout_timing);

        detail_layout_pets=findViewById(R.id.detail_layout_pets);
        detail_layout_precaution=findViewById(R.id.detail_layout_precaution);

        layout_miltary=findViewById(R.id.layout_miltary);
        layout_parking=findViewById(R.id.layout_parking);
        layout_shuttle=findViewById(R.id.layout_shuttle);
        layout_camping=findViewById(R.id.layout_camping);
        layout_cahrity=findViewById(R.id.layout_cahrity);
        layout_weapons=findViewById(R.id.layout_weapons);
        layout_food=findViewById(R.id.layout_food);
        layout_wheelchair=findViewById(R.id.layout_wheelchair);
        layout_costume=findViewById(R.id.layout_costume);

        layout_alcohol=findViewById(R.id.layout_alcohol);
        layout_tickets=findViewById(R.id.layout_tickets);

//intiating textviews

        festival_pets=findViewById(R.id.festival_pets);
        festival_smoking=findViewById(R.id.festival_smoking);

        festival_tickets=findViewById(R.id.festival_tickets);
        festival_military_discount=findViewById(R.id.festival_military_discount);
        festival_shuttle=findViewById(R.id.festival_shuttle);
        festival_parking=findViewById(R.id.festival_parking);
        festival_charity=findViewById(R.id.festival_charity);
        festival_camping=findViewById(R.id.festival_camping);
        festival_weapons=findViewById(R.id.festival_weapons);
        festival_food=findViewById(R.id.festival_food);
        festival_wheel=findViewById(R.id.festival_wheel);
        festival_costume=findViewById(R.id.festival_costume);
        festival_alcohol=findViewById(R.id.festival_alcohol);
//Intiating image views
        img_location=findViewById(R.id.img_location);
        img_hours=findViewById(R.id.img_hours);
        img_pets=findViewById(R.id.img_pets);
        img_smoking=findViewById(R.id.img_smoking);
        img_tickets=findViewById(R.id.img_tickets);
        img_miltary=findViewById(R.id.img_miltary);
        img_shuttle=findViewById(R.id.img_shuttle);
        img_parking=findViewById(R.id.img_parking);
        img_charity=findViewById(R.id.img_charity);
        img_camping=findViewById(R.id.img_camping);
        img_weapons=findViewById(R.id.img_weapons);
        img_food=findViewById(R.id.img_food);
        img_wheelchair=findViewById(R.id.img_wheelchair);
        img_costume=findViewById(R.id.img_costume);
        img_alcohol=findViewById(R.id.img_alcohol);

    }
    public void PopulatetData()
    {
        try {
            Bundle data = getIntent().getExtras();
            if(data!=null)
            {
                festivals = (Festivals) data.getParcelable("festival");
                Log.d("festva ID =","is"+festivals.getId());
                GetFestivalPolicies(festivals.getId());
                festival_name.setText(""+festivals.getTitle());
                Log.d("INFOPAGEIMAGE","URL"+"http://renfestapp.com/"+festivals.getFiles_info());
                Picasso.get().load("http://renfestapp.com/"+festivals.getFiles_info()).into(img_info, new Callback() {
                    @Override
                    public void onSuccess() {
                        info_homeprogress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        info_homeprogress.setVisibility(View.GONE);
                        img_info.setImageDrawable(ContextCompat.getDrawable(FestivalDetailActivty.this,R.drawable.profile_no_img));
                    }
                });
            }
        }
        catch (Exception ex)
        {
            Log.d("GetData","Event Detail"+ex.getMessage());
        }
    }
    public void SetCustomTitle()
    {
        try
        {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            Typeface tf = Typeface.createFromAsset(getAssets(), "Roycroft-Regular.otf");
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.layout_actionbar);
            TextView title=(TextView)findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
            title.setText("");
            title.setTypeface(tf);
        }
        catch (Exception ex)
        {

        }
    }
    public String MakeDate(String date)
    {
        String formatted_date = null;
        String year,month,day;

        try
        {
            year=date.substring(0,4);
            month=date.substring(4,6);
            if (date.length()<8)
            {
                day=date.substring(6,7);
            }
            else
            {
                day=date.substring(6,8);
            }

            formatted_date=year+"/"+month+"/"+day;
        }
        catch (Exception ex)
        {

        }
        return  formatted_date;
    }
    public void GetFestivalPolicies(String festival_id)
    {
        progressbar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url_signup = URL_GET_FESTIVAL_POLICIES+"festival_id="+festival_id;
        Log.d("GELLALL", "URL" + url_signup);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_signup,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressbar.setVisibility(View.GONE);
                            JSONObject jsonObj = new JSONObject(response);
                            String resposnse_value = jsonObj.getString("response");
                            Log.d("GELLALL_Response Value", "is" + resposnse_value);
                            if (resposnse_value.equals("1"))
                            {
//                                p_model=new PoliciesModel();
                                 JSONObject data_ibject=jsonObj.getJSONObject("data");
                                Log.d("ADDRESS","VALUE"+data_ibject.getString("Address:"));
                             if(data_ibject.has("Address:")) {
                                 if(data_ibject.getString("Address:").equals(""))
                                 {
                                     detail_layout_address.setVisibility(View.GONE);
                                 }
                                 else
                                 {

                                     festival_location.setText("" + data_ibject.getString("Address:"));
                                 }
                             }

                                else
                             {
                                 detail_layout_address.setVisibility(View.GONE);
                             }
                                if(data_ibject.has("Dates and Hours:"))
                                {
                                    if(data_ibject.getString("Dates and Hours:").equals(""))
                                    {
                                        detail_layout_timing.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        festival_date.setText("" + data_ibject.getString("Dates and Hours:"));
                                    }
                                    }
                                else
                                {
                                    detail_layout_timing.setVisibility(View.GONE);
                                }
                                if(data_ibject.has("Smoking:"))
                                {
                                    if(data_ibject.getString("Smoking:").equals(""))
                                    {
                                        detail_layout_precaution.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        festival_smoking.setText("" + data_ibject.getString("Smoking:"));
                                    }
                                }
                                else
                                {
                                    detail_layout_precaution.setVisibility(View.GONE);
                                }
                                if(data_ibject.has("Pets:"))
                                {
                                    if(data_ibject.getString("Pets:").equals(""))
                                    {
                                        detail_layout_pets.setVisibility(View.GONE);
                                    }
                                    else
                                    {

                                    festival_pets.setText("" + data_ibject.getString("Pets:"));
                                    }
                                }
                                else
                                {
                                    detail_layout_pets.setVisibility(View.GONE);
                                }
                                if(data_ibject.has("Tickets:"))
                                {
                                    if(data_ibject.getString("Tickets:").equals(""))
                                    {
                                        layout_tickets.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                    festival_tickets.setText("" + data_ibject.getString("Tickets:"));
                                    }
                                }
                                else
                                {
                                    layout_tickets.setVisibility(View.GONE);
                                }
                                if(data_ibject.has("Camping"))
                                {
                                    if(data_ibject.getString("Camping").equals(""))
                                    {
                                        layout_camping.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        festival_camping.setText("" + data_ibject.getString("Camping"));
                                    }
                                }
                                else
                                {
                                    layout_camping.setVisibility(View.GONE);
                                }
                                if(data_ibject.has("Charity:"))
                                {
                                    if(data_ibject.getString("Charity:").equals(""))
                                    {
                                        layout_cahrity.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                    festival_charity.setText("" + data_ibject.getString("Charity:"));
                                    }
                                }
                                else
                                {
                                    layout_cahrity.setVisibility(View.GONE);
                                }
                                if(data_ibject.has("Military Discount"))
                                {
                                    if(data_ibject.getString("Military Discount").equals(""))
                                    {
                                        layout_miltary.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                    festival_military_discount.setText("" + data_ibject.getString("Military Discount"));
                                    }
                                }
                                else
                                {
                                    layout_miltary.setVisibility(View.GONE);
                                }
                                if(data_ibject.has("Alcohol:"))
                                {
                                    if(data_ibject.getString("Alcohol:").equals(""))
                                    {
                                        layout_alcohol.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                    festival_alcohol.setText("" + data_ibject.getString("Alcohol:"));
                                    }
                                }
                                else
                                {
                                    layout_alcohol.setVisibility(View.GONE);
                                }
                                if(data_ibject.has("Costumes:"))
                                {
                                    if(data_ibject.getString("Costumes:").equals(""))
                                    {
                                        layout_costume.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                    festival_costume.setText("" + data_ibject.getString("Costumes:"));
                                    }
                                }
                                else
                                {
                                    layout_costume.setVisibility(View.GONE);
                                }
                                if(data_ibject.has("Weapons:"))
                                {
                                    if(data_ibject.getString("Weapons:").equals(""))
                                    {
                                        layout_weapons.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        festival_weapons.setText("" + data_ibject.getString("Weapons:"));
                                    }
                                }
                                else
                                {
                                    layout_weapons.setVisibility(View.GONE);
                                }
                                if(data_ibject.has("Wheelchairs/Strollers:"))
                                {
                                    if(data_ibject.getString("Wheelchairs/Strollers:").equals(""))
                                    {
                                        layout_wheelchair.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                    festival_wheel.setText("" + data_ibject.getString("Wheelchairs/Strollers:"));
                                    }
                                }
                                else
                                {
                                    layout_wheelchair.setVisibility(View.GONE);
                                }
                                if(data_ibject.has("Outside Food/Drink:"))
                                {
                                    if(data_ibject.getString("Outside Food/Drink:").equals(""))
                                    {
                                        layout_food.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                    festival_food.setText("" + data_ibject.getString("Outside Food/Drink:"));
                                    }
                                }
                                else
                                {
                                    layout_food.setVisibility(View.GONE);
                                }
                                if(data_ibject.has("Shuttle:"))
                                {
                                    if(data_ibject.getString("Shuttle:").equals(""))
                                    {
                                        layout_shuttle.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        festival_shuttle.setText("" + data_ibject.getString("Shuttle:"));
                                    }
                                }
                                else
                                {
                                    layout_shuttle.setVisibility(View.GONE);
                                }
                                if(data_ibject.has("Parking"))
                                {
                                    if(data_ibject.getString("Parking").equals(""))
                                    {
//                                        layout_shuttle.setVisibility(View.GONE);
                                    img_parking.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        festival_parking.setText("" + data_ibject.getString("Parking"));
                                    }
                                }
                                else
                                {
                                    img_parking.setVisibility(View.GONE);
                                }

                            }
                            else if (resposnse_value.equals("-1"))
                            {
                                Log.d("Show Error", "layout");
                                Toast.makeText(getApplicationContext(),"Unkown Error Occured",Toast.LENGTH_SHORT).show();
                            }
                            else if (resposnse_value.equals("0"))
                            {
                                Log.d("Show Error", "layout");
                                Toast.makeText(getApplicationContext(),"No Record Found",Toast.LENGTH_SHORT).show();
                            }

                            else
                            {
                                Toast.makeText(getApplicationContext(),"Unkown Error Occured",Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Unkown Error Occured",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Unable to connect to Server", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);
            }
        });
        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
}

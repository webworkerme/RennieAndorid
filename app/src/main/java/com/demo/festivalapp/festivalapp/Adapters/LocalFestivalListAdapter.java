package com.demo.festivalapp.festivalapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demo.festivalapp.festivalapp.Activities.AllLocalWishlist;
import com.demo.festivalapp.festivalapp.Models.LocalFestivalListModel;
import com.demo.festivalapp.festivalapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.festival_Name;
import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.festival_id;

/**
 * Created by Abbasi on 9/3/2018.
 */

public class LocalFestivalListAdapter extends RecyclerView.Adapter<LocalFestivalListAdapter.MyViewHolder>{
    private List<LocalFestivalListModel> festivalsList;
Context mcontext;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date, festival_location;
        public LinearLayout layout_festival_detail;
        public ImageView img_profile;
        ProgressBar homeprogress;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            date = (TextView) view.findViewById(R.id.fest_date);
            festival_location = (TextView) view.findViewById(R.id.layout_festival_location);
            img_profile=view.findViewById(R.id.img_profile);
            layout_festival_detail=view.findViewById(R.id.layout_festival_detail);
            homeprogress=view.findViewById(R.id.homeprogress);

        }
    }


    public LocalFestivalListAdapter(List<LocalFestivalListModel> moviesList, Context context) {
        this.festivalsList = moviesList;
        mcontext=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_adapter_festival_listadapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final LocalFestivalListModel festival = festivalsList.get(position);
        holder.title.setText(festival.getFestival_name());
        Log.d("LOCAL_SAVED","DATE"+festival.getFestival_address());
        String[] separated = festival.getFestival_address().split(",");
        String date1=separated[0];
        String date2=separated[1];
        holder.date.setText(MakeDate(date1)+"/"+MakeDate(date2));
        holder.festival_location.setText(festival.getFestival_date());
        Log.d("Profile","Festival Image"+"http://renfestapp.com/"+festival.getFestival_image());
        Picasso.get().load("http://renfestapp.com/"+festival.getFestival_image()).into(holder.img_profile,new Callback() {
            @Override
            public void onSuccess() {
                holder.homeprogress.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                holder.homeprogress.setVisibility(View.GONE);
                holder.img_profile.setImageDrawable(ContextCompat.getDrawable(mcontext,R.drawable.profile_no_img));
            }
            });
        holder.layout_festival_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                festival_id=festival.getFestival_id();
                festival_Name=festival.getFestival_name();
                Intent acitvity_wishlist=new Intent(mcontext, AllLocalWishlist.class);
                mcontext.startActivity(acitvity_wishlist);
            }
        });
    }

    @Override
    public int getItemCount() {
        return festivalsList.size();
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

            formatted_date=day+"/"+month+"/"+year.substring(2,4);
        }
        catch (Exception ex)
        {

        }
        return  formatted_date;
    }
}

package com.demo.festivalapp.festivalapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.festivalapp.festivalapp.Activities.HomeScreenActivity;
import com.demo.festivalapp.festivalapp.Models.NearBy;
import com.demo.festivalapp.festivalapp.R;

import java.util.List;

import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.STATE_NAME;

/**
 * Created by Abbasi on 9/3/2018.
 */

public class NearbyFestivalListAdapter extends RecyclerView.Adapter<NearbyFestivalListAdapter.MyViewHolder>{
    private List<NearBy> festivalsList;
    private  Context mcontext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView state_name;

        public MyViewHolder(View view) {
            super(view);
            state_name = (TextView) view.findViewById(R.id.state_name);
            Typeface custom_font = Typeface.createFromAsset(mcontext.getAssets(),  "Gobold Regular.ttf");
            state_name.setTypeface(custom_font);
        }
    }


    public NearbyFestivalListAdapter(List<NearBy> moviesList, Context context) {
        this.festivalsList = moviesList;
        mcontext=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_nearby_listadapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final NearBy nearBy = festivalsList.get(position);
        holder.state_name.setText(nearBy.getStateName());
        holder.state_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                STATE_NAME=nearBy.getStateName();
                Intent relaunch_state_festivals=new Intent(mcontext, HomeScreenActivity.class);
                relaunch_state_festivals.putExtra("Data","available");
                mcontext.startActivity(relaunch_state_festivals);
//                ((Activity)mcontext).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return festivalsList.size();
    }
}

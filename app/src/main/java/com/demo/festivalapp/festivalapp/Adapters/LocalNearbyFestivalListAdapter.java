package com.demo.festivalapp.festivalapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.festivalapp.festivalapp.Activities.LocalMainFestivalListActivity;
import com.demo.festivalapp.festivalapp.Models.SaveStatesModel;
import com.demo.festivalapp.festivalapp.R;

import java.util.List;

import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.LOCAL_STATE_NAME;

/**
 * Created by Abbasi on 9/3/2018.
 */

public class LocalNearbyFestivalListAdapter extends RecyclerView.Adapter<LocalNearbyFestivalListAdapter.MyViewHolder>{
    private List<SaveStatesModel> festivalsList;
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


    public LocalNearbyFestivalListAdapter(List<SaveStatesModel> moviesList, Context context) {
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
        final SaveStatesModel nearBy = festivalsList.get(position);
        holder.state_name.setText(nearBy.getState_name());
        holder.state_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LOCAL_STATE_NAME=nearBy.getState_name();
                Intent relaunch_state_festivals=new Intent(mcontext, LocalMainFestivalListActivity.class);
                relaunch_state_festivals.putExtra("Data","available");
                mcontext.startActivity(relaunch_state_festivals);
                ((Activity)mcontext).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return festivalsList.size();
    }
}

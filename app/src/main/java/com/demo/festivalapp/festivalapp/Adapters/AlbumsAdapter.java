package com.demo.festivalapp.festivalapp.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.festivalapp.festivalapp.Models.Album;
import com.demo.festivalapp.festivalapp.R;

import java.util.List;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Album> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
//            title = (TextView) view.findViewById(R.id.title);
//            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
//            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public AlbumsAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wish_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Album album = albumList.get(position);
        holder.thumbnail.setImageBitmap(album.getThumbnail());
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialogueBox(album.getName(),album.getCategory_name(),album.getThumbnail(),album.getCategory_notes(),album.getCategory_latitude());
            }
        });

    }



    @Override
    public int getItemCount() {
        return albumList.size();
    }
public void ShowDialogueBox(String name,String slected_category,Bitmap bmp,String notes,String latlng)
{
    // custom dialog
    final Dialog dialog = new Dialog(mContext);
    dialog.setContentView(R.layout.layout_previewimage);
    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    lp.copyFrom(dialog.getWindow().getAttributes());
    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
    lp.gravity = Gravity.CENTER;
    dialog.getWindow().setAttributes(lp);
    TextView name_wishlist_item=dialog.findViewById(R.id.name_wishlist_item);
    name_wishlist_item.setText(""+name);
    TextView spinner_textview=dialog.findViewById(R.id.spinner_textview);
    spinner_textview.setText(""+slected_category);
   TextView text_notes=dialog.findViewById(R.id.text_notes);
    text_notes.setText(""+notes);
    TextView wish_latlng=dialog.findViewById(R.id.wish_latlng);
    wish_latlng.setText(""+latlng);
    ImageView image = (ImageView) dialog.findViewById(R.id.image_preview);
    image.setImageBitmap(bmp);
    dialog.show();
}
}

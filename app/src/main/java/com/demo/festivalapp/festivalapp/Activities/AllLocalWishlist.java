package com.demo.festivalapp.festivalapp.Activities;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.festivalapp.festivalapp.Adapters.AlbumsAdapter;
import com.demo.festivalapp.festivalapp.Models.Album;
import com.demo.festivalapp.festivalapp.Models.DatabaseHelper;
import com.demo.festivalapp.festivalapp.Models.Festivals;
import com.demo.festivalapp.festivalapp.R;

import java.util.ArrayList;
import java.util.List;

import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.festival_id;

public class AllLocalWishlist extends AppCompatActivity {
    LinearLayout btn_addnewwish;
    DatabaseHelper myDb;
    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private List<Album> albumList;
    LinearLayout filter_jewelery1,filter_clothing,filter_armory,filter_trinket;
    Festivals festivals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_wishlist);
//        GetData();
        SetCustomTitle();
        myDb = new DatabaseHelper(AllLocalWishlist.this);
        SetUiWidgets();
        Log.d("CheckSize","sqlite"+myDb.getAllData().getCount());
        if(myDb.getAllData().getCount()>0)
        {
            filter_clothing.setBackgroundColor(Color.parseColor("#25A8D8"));
            PopulateList("Clothing");
        }

    }
    public void SetUiWidgets() {
        albumList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_wish_list);
        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter(this, albumList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        btn_addnewwish = findViewById(R.id.btn_addnewwish);
        btn_addnewwish.setVisibility(View.GONE);
        filter_jewelery1=findViewById(R.id.filter_jewelery);
        filter_jewelery1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Jewlery","item clicked");
                filter_jewelery1.setBackgroundColor(Color.parseColor("#25A8D8"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                 filter_clothing.setBackground(ContextCompat.getDrawable(AllLocalWishlist.this, R.drawable.my_custom_background_linear));
                filter_armory.setBackground(ContextCompat.getDrawable(AllLocalWishlist.this, R.drawable.my_custom_background_linear));
                filter_trinket.setBackground(ContextCompat.getDrawable(AllLocalWishlist.this, R.drawable.my_custom_background_linear));
                }
                    albumList.clear(); //clear list
                adapter.notifyDataSetChanged();
                if (myDb.getAllData().getCount()>0) {
                    PopulateList("Jewelery");
                }
            }
        });
        filter_clothing=findViewById(R.id.filter_clothing);
        filter_clothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    filter_clothing.setBackgroundColor(Color.parseColor("#25A8D8"));
                    filter_jewelery1.setBackground(ContextCompat.getDrawable(AllLocalWishlist.this, R.drawable.my_custom_background_linear));
                    filter_armory.setBackground(ContextCompat.getDrawable(AllLocalWishlist.this, R.drawable.my_custom_background_linear));
                    filter_trinket.setBackground(ContextCompat.getDrawable(AllLocalWishlist.this, R.drawable.my_custom_background_linear));
                }
                albumList.clear(); //clear list
                adapter.notifyDataSetChanged();
                if (myDb.getAllData().getCount()>0)
                {
                    PopulateList("Clothing");
                }
            }
        });
        filter_armory=findViewById(R.id.filter_armory);
        filter_armory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                {
                    filter_armory.setBackgroundColor(Color.parseColor("#25A8D8"));
                    filter_clothing.setBackground(ContextCompat.getDrawable(AllLocalWishlist.this, R.drawable.my_custom_background_linear));
                    filter_jewelery1.setBackground(ContextCompat.getDrawable(AllLocalWishlist.this, R.drawable.my_custom_background_linear));
                    filter_trinket.setBackground(ContextCompat.getDrawable(AllLocalWishlist.this, R.drawable.my_custom_background_linear));
                }
                albumList.clear(); //clear list
                adapter.notifyDataSetChanged();
                if (myDb.getAllData().getCount()>0) {
                    PopulateList("Armory");
                }
            }
        });
        filter_trinket=findViewById(R.id.filter_trinket);
        filter_trinket.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                Log.d("trinket","item clicked");
                filter_trinket.setBackgroundColor(Color.parseColor("#25A8D8"));
                filter_clothing.setBackground(ContextCompat.getDrawable(AllLocalWishlist.this, R.drawable.my_custom_background_linear));
                filter_armory.setBackground(ContextCompat.getDrawable(AllLocalWishlist.this, R.drawable.my_custom_background_linear));
                filter_jewelery1.setBackground(ContextCompat.getDrawable(AllLocalWishlist.this, R.drawable.my_custom_background_linear));

                albumList.clear(); //clear list
                adapter.notifyDataSetChanged();
                if (myDb.getAllData().getCount()>0) {
                    PopulateList("Trinket");
                }
            }
        });
    }
    public void PopulateList(String slected_category)
    {
        Cursor cursor=myDb.getAllData();
        if (cursor.moveToFirst()){
            do{

                Log.d("Data","is"+cursor.getBlob(cursor.getColumnIndex("IMAGE")));
                Log.d("Data","is"+cursor.getString(cursor.getColumnIndex("NAME")));
                Log.d("Category","is"+cursor.getString(cursor.getColumnIndex("SURNAME")));

                if(cursor.getString(cursor.getColumnIndex("SURNAME")).equals(slected_category)&&cursor.getString(cursor.getColumnIndex("MARKS")).equals(festival_id))
               {
                   Album a = new Album(cursor.getString(cursor.getColumnIndex("NAME")), cursor.getString(cursor.getColumnIndex("SURNAME")), getImage(cursor.getBlob(cursor.getColumnIndex("IMAGE"))),
                           cursor.getString(cursor.getColumnIndex("NOTES")),cursor.getString(cursor.getColumnIndex("LATLNG")));
                   albumList.add(a);
               }
//

            }while(cursor.moveToNext());
            adapter.notifyDataSetChanged();
        }
        cursor.close();
    }
    public  Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("RESUME","Method called");

    }

}

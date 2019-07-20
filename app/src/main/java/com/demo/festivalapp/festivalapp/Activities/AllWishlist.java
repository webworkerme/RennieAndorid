package com.demo.festivalapp.festivalapp.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.festivalapp.festivalapp.Adapters.AlbumsAdapter;
import com.demo.festivalapp.festivalapp.Models.Album;
import com.demo.festivalapp.festivalapp.Models.DatabaseHelper;
import com.demo.festivalapp.festivalapp.Models.Festivals;
import com.demo.festivalapp.festivalapp.R;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.util.IabHelper;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.util.IabResult;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.util.Inventory;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.util.Purchase;

import java.util.ArrayList;
import java.util.List;

import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.festival_id;

public class AllWishlist extends AppCompatActivity {
    LinearLayout btn_addnewwish;
    DatabaseHelper myDb;
    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private List<Album> albumList;
    LinearLayout filter_jewelery1,filter_clothing,filter_armory,filter_trinket;
    Festivals festivals;
    SharedPreferences prefs;
    private static final String TAG =
            "InAppBilling";
    IabHelper mHelper;
    static final String ITEM_SKU = "maps_upgrade";
    static final String ITEM_SKU1 = "stagefilter_price";
    static final String ITEM_SKU2 = "wishlist_upgrade";
    static final String ITEM_SKU3 = "reminder_price";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_wishlist);
        GetData();
        SetCustomTitle();
        myDb = new DatabaseHelper(AllWishlist.this);
        IntiatePayment();
        prefs = getSharedPreferences("PURCHASE_DETAIS", MODE_PRIVATE);
        if(prefs.getBoolean("wish_list",false))
        {

        }
        else
        {
            ShowDialogue();
        }
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
        btn_addnewwish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prefs.getBoolean("wish_list",false))
                {
                    Intent add_wish=new Intent(AllWishlist.this,AddNewWishActivty.class);
                    add_wish.putExtra("festival", new Festivals(festivals.getId(),festivals.getTitle(),festivals.getGenre(),
                            festivals.getStartDate(),festivals.getEnd_date(),festivals.getFiles_home(),festivals.getFile_map(),festivals.getFiles_profile(),festivals.getFiles_info(),festivals.getFestival_lat(),
                            festivals.getFestival_lng(),festivals.getWeekdays(),festivals.getRepeatWeekday(),festivals.getHoliday()));
                    startActivity(add_wish);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Purchase this feature",Toast.LENGTH_SHORT).show();
                }

            }
        });
        filter_jewelery1=findViewById(R.id.filter_jewelery);
        filter_jewelery1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Jewlery","item clicked");
                filter_jewelery1.setBackgroundColor(Color.parseColor("#25A8D8"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                 filter_clothing.setBackground(ContextCompat.getDrawable(AllWishlist.this, R.drawable.my_custom_background_linear));
                filter_armory.setBackground(ContextCompat.getDrawable(AllWishlist.this, R.drawable.my_custom_background_linear));
                filter_trinket.setBackground(ContextCompat.getDrawable(AllWishlist.this, R.drawable.my_custom_background_linear));
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
                    filter_jewelery1.setBackground(ContextCompat.getDrawable(AllWishlist.this, R.drawable.my_custom_background_linear));
                    filter_armory.setBackground(ContextCompat.getDrawable(AllWishlist.this, R.drawable.my_custom_background_linear));
                    filter_trinket.setBackground(ContextCompat.getDrawable(AllWishlist.this, R.drawable.my_custom_background_linear));
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
                    filter_clothing.setBackground(ContextCompat.getDrawable(AllWishlist.this, R.drawable.my_custom_background_linear));
                    filter_jewelery1.setBackground(ContextCompat.getDrawable(AllWishlist.this, R.drawable.my_custom_background_linear));
                    filter_trinket.setBackground(ContextCompat.getDrawable(AllWishlist.this, R.drawable.my_custom_background_linear));
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
                filter_clothing.setBackground(ContextCompat.getDrawable(AllWishlist.this, R.drawable.my_custom_background_linear));
                filter_armory.setBackground(ContextCompat.getDrawable(AllWishlist.this, R.drawable.my_custom_background_linear));
                filter_jewelery1.setBackground(ContextCompat.getDrawable(AllWishlist.this, R.drawable.my_custom_background_linear));

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
   public void GetData()
   {
       try
       {
           Bundle data = getIntent().getExtras();
           if (data != null)
           {
               festivals = (Festivals) data.getParcelable("festival");
           }
           else
           {

           }
       }
       catch (Exception ex)
       {

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
                    mHelper.launchPurchaseFlow(AllWishlist.this, ITEM_SKU1, 10001,
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
                    mHelper.launchPurchaseFlow(AllWishlist.this, ITEM_SKU, 10001,
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
                    mHelper.launchPurchaseFlow(AllWishlist.this, ITEM_SKU2, 10001,
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
                    mHelper.launchPurchaseFlow(AllWishlist.this, ITEM_SKU3, 10001,
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
}

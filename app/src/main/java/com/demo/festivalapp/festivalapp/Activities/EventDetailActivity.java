package com.demo.festivalapp.festivalapp.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.demo.festivalapp.festivalapp.Models.Festivals;
import com.demo.festivalapp.festivalapp.R;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.util.IabHelper;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.util.IabResult;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.util.Inventory;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.util.Purchase;
public class EventDetailActivity extends AppCompatActivity {
    LinearLayout btn_festivel_info, btn_event_list, btn_wish_list, btn_showmapview;
    ImageView home_page;
    String home_image_str, home_fest_name;
    Festivals festivals;
    ProgressBar festival_homeprogress;
    TextView name_festival, name_subfestival;
    SharedPreferences prefs;
    static final String ITEM_SKU3 = "maps_upgrade";
    static final String ITEM_SKU2 = "wishlist_upgrade";
    static final String ITEM_SKU = "reminder_price";
    private static final String TAG =
            "InAppBilling";
    IabHelper mHelper;

    boolean enable_wish_upgrade = false;
    boolean enable_map_upgrade = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_festival_main);
        try {
            SetCustomTitle();
            GetData();
            IntiatePayment();
            prefs = getSharedPreferences("PURCHASE_DETAIS", MODE_PRIVATE);
//            enable_map_upgrade = prefs.getBoolean("map_upgrade", false);
            Log.d("SHARED", "VALUE" + enable_wish_upgrade);
//            enable_wish_upgrade = prefs.getBoolean("wish_list", false);
            SetUiwidgets();
        } catch (Exception ex) {

        }
    }

    public void SetUiwidgets() {
        try {
            name_festival = findViewById(R.id.name_festival);
            name_subfestival = findViewById(R.id.name_subfestival);
            if (home_fest_name != null || home_fest_name.equals("")) {
                Typeface custom_font = Typeface.createFromAsset(getAssets(), "Roycroft-Regular.otf");
                name_festival.setText("" + home_fest_name);
                name_festival.setTypeface(custom_font);

            }
            btn_showmapview = findViewById(R.id.btn_showmapview);
            btn_showmapview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (prefs.getBoolean("map_upgrade", false)) {
                        Intent real_timemap = new Intent(EventDetailActivity.this, MapsActivity.class);
                        real_timemap.putExtra("festival", new Festivals(festivals.getId(), festivals.getTitle(), festivals.getGenre(),
                                festivals.getStartDate(), festivals.getEnd_date(), festivals.getFiles_home(), festivals.getFile_map(), festivals.getFiles_profile(), festivals.getFiles_info(),
                                festivals.getFestival_lat(),
                                festivals.getFestival_lng(), festivals.getWeekdays(), festivals.getRepeatWeekday(), festivals.getHoliday()));
                        startActivity(real_timemap);
                    } else {
                        Intent map = new Intent(EventDetailActivity.this, FreeUserMapActivty.class);
                        map.putExtra("festival", new Festivals(festivals.getId(), festivals.getTitle(), festivals.getGenre(),
                                festivals.getStartDate(), festivals.getEnd_date(), festivals.getFiles_home(), festivals.getFile_map(), festivals.getFiles_profile(), festivals.getFiles_info(),
                                festivals.getFestival_lat(), festivals.getFestival_lng(), festivals.getWeekdays(), festivals.getRepeatWeekday(), festivals.getHoliday()));
                        startActivity(map);
                    }
                }
            });
            festival_homeprogress = findViewById(R.id.festival_homeprogress);
            home_page = findViewById(R.id.home_page);
            if (home_image_str != null) {
                String image_url;
                image_url = "http://renfestapp.com/" + home_image_str;
                Log.d("HOME_PAGE", "URL" + image_url);
                LoadImageUsingGlide(image_url);

//                Picasso.get()
//                        .load(image_url)
//                        .into(new com.squareup.picasso.Target() {
//                            @Override
//                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//
//                                home_page.setImageBitmap(bitmap);
//                                festival_homeprogress.setVisibility(View.GONE);
//                            }
//
//                            @Override
//                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//                                Log.d("IMAGE_LOADING", "failed");
//                                festival_homeprogress.setVisibility(View.GONE);
//                                home_page.setImageDrawable(ContextCompat.getDrawable(EventDetailActivity.this, R.drawable.profile_no_img));
//                            }
//
//                            @Override
//                            public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                            }
//                        });
            }
            btn_festivel_info = findViewById(R.id.btn_festivel_info);
            btn_festivel_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent_festival_info = new Intent(EventDetailActivity.this, FestivalDetailActivty.class);
                    intent_festival_info.putExtra("festival", new Festivals(festivals.getId(), festivals.getTitle(), festivals.getGenre(),
                            festivals.getStartDate(), festivals.getEnd_date(), festivals.getFiles_home(), festivals.getFile_map(), festivals.getFiles_profile(), festivals.getFiles_info(),
                            festivals.getFestival_lat(), festivals.getFestival_lng()
                            , festivals.getWeekdays(), festivals.getRepeatWeekday(), festivals.getHoliday()));
                    startActivity(intent_festival_info);
                }
            });
            btn_event_list = findViewById(R.id.btn_event_list);
            btn_event_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent_festivalevent_list = new Intent(EventDetailActivity.this, FestivalEventListActivity.class);
                    intent_festivalevent_list.putExtra("festival", new Festivals(festivals.getId(), festivals.getTitle(), festivals.getGenre(),
                            festivals.getStartDate(), festivals.getEnd_date(), festivals.getFiles_home(), festivals.getFile_map(), festivals.getFiles_profile(), festivals.getFiles_info(),
                            festivals.getFestival_lat(), festivals.getFestival_lng(), festivals.getWeekdays(), festivals.getRepeatWeekday(), festivals.getHoliday()));
                    startActivity(intent_festivalevent_list);

                }
            });
            btn_wish_list = findViewById(R.id.btn_wish_list);
            btn_wish_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    if(prefs.getBoolean("wish_list",false))
//                    {
                    Intent intent_festivalevent_list = new Intent(EventDetailActivity.this, AllWishlist.class);
                    intent_festivalevent_list.putExtra("festival", new Festivals(festivals.getId(), festivals.getTitle(), festivals.getGenre(),
                            festivals.getStartDate(), festivals.getEnd_date(), festivals.getFiles_home(), festivals.getFile_map(), festivals.getFiles_profile(), festivals.getFiles_info(),
                            festivals.getFestival_lat(), festivals.getFestival_lng(), festivals.getWeekdays(), festivals.getRepeatWeekday(), festivals.getHoliday()));
                    startActivity(intent_festivalevent_list);
//                    }
//                    else
//                    {
//                        mHelper.launchPurchaseFlow(EventDetailActivity.this, ITEM_SKU2, 10001,
//                                mPurchaseFinishedListener, "mypurchasetoken");
//
//                    }
                }
            });
        } catch (Exception ex) {
            Log.d("SetUiwidgets", "Error Message" + ex.getMessage());
        }
    }

    public void GetData() {
        try {
            Bundle data = getIntent().getExtras();
            if (data != null) {
                try {
                    festivals = (Festivals) data.getParcelable("festival");
                    Log.d("festva ID =", "is" + festivals.getId());
                    home_image_str = festivals.getFiles_home();
                    home_fest_name = festivals.getTitle();
                } catch (Exception ex) {

                }
            }
        } catch (Exception ex) {
            Log.d("GetData", "Event Detail" + ex.getMessage());
        }
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

    public void IntiatePayment() {
        String base64EncodedPublicKey = getResources().getString(R.string.string_inapp_key);
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d("RESULT", "IS" + result);
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app Billing setup failed: " +
                            result);
                } else {
                    Log.d(TAG, "In-app Billing is set up OK");
                }
            }
        });
        Log.d("INAPPSUPPORTED", "AVAILABLE" + mHelper.subscriptionsSupported());

    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase) {
            if (result.isFailure()) {
                // Handle error
                return;
            } else if (purchase.getSku().equals(ITEM_SKU)) {
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

                    if (result.isSuccess()) {
                        ShowDialogueAddClass();
                        Log.d("AllItem", "purchased is =" + purchase);
                        SharedPreferences.Editor editor = getSharedPreferences("PURCHASE_DETAIS", MODE_PRIVATE).edit();
                        editor.putBoolean("wish_list", true);
                    } else {
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
                                    Intent data) {
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

    public void LoadImageUsingGlide(String img_url) {
        Glide.with(this)
                .asBitmap()
                .load(img_url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        home_page.setImageBitmap(resource);
                        festival_homeprogress.setVisibility(View.GONE);
                        Log.d("IMAGE_FETCHED", "TRUE");
                    }

                });
    }
}

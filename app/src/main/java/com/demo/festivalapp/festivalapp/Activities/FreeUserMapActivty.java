package com.demo.festivalapp.festivalapp.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.demo.festivalapp.festivalapp.Models.Festivals;
import com.demo.festivalapp.festivalapp.R;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.util.IabHelper;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.util.IabResult;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.util.Inventory;
import com.demo.festivalapp.festivalapp.UtilitiesClasses.util.Purchase;
import com.github.chrisbanes.photoview.PhotoView;

public class FreeUserMapActivty extends AppCompatActivity {
    PhotoView photoView;
    Festivals festivals;
    ProgressBar info_homeprogress;
    SharedPreferences prefs;
    private static final String TAG =
            "InAppBilling";
    IabHelper mHelper;
    static final String ITEM_SKU = "maps_upgrade";
    static final String ITEM_SKU1 = "stagefilter_price";
    static final String ITEM_SKU2 = "wishlist_upgrade";
    static final String ITEM_SKU3 = "reminder_price";

    ProgressBar festival_homeprogress;
    TextView tv_error_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_user_map_activty);
        SetCustomTitle();
        IntiatePayment();
        consumeItem();
        photoView = (PhotoView) findViewById(R.id.img_info);
        info_homeprogress=findViewById(R.id.info_homeprogress);
        festival_homeprogress=findViewById(R.id.festival_homeprogress);
        tv_error_loading=findViewById(R.id.tv_error_loading);
        prefs = getSharedPreferences("PURCHASE_DETAIS", MODE_PRIVATE);
        ShowDialogue();
        PopulatetData();
    }
    public void PopulatetData()
    {
        try {
            Bundle data = getIntent().getExtras();
            if(data!=null)
            {
                festivals = (Festivals) data.getParcelable("festival");
                Log.d("festva ID =","is"+festivals.getId());
                festival_homeprogress.setVisibility(View.VISIBLE);
                LoadImageUsingGlide("http://renfestapp.com/"+festivals.getFile_map());
            }
        }
        catch (Exception ex)
        {
            Log.d("GetData","Event Detail"+ex.getMessage());
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
                    mHelper.launchPurchaseFlow(FreeUserMapActivty.this, ITEM_SKU1, 10001,
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
                    mHelper.launchPurchaseFlow(FreeUserMapActivty.this, ITEM_SKU, 10001,
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
                    mHelper.launchPurchaseFlow(FreeUserMapActivty.this, ITEM_SKU2, 10001,
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
                    mHelper.launchPurchaseFlow(FreeUserMapActivty.this, ITEM_SKU3, 10001,
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
    public void LoadImageUsingGlide(String img_url) {
        Glide.with(this)
                .asBitmap()
                .load(img_url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        Drawable d = new BitmapDrawable(getResources(), resource);
                        photoView.setImageDrawable(d);
                        festival_homeprogress.setVisibility(View.GONE);
                        Log.d("IMAGE_FETCHED", "TRUE");
                    }
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        festival_homeprogress.setVisibility(View.GONE);
                        tv_error_loading.setVisibility(View.VISIBLE);

                    }

                });
    }
}

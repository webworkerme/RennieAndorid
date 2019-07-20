package com.demo.festivalapp.festivalapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.festivalapp.festivalapp.Models.DatabaseHelper;
import com.demo.festivalapp.festivalapp.Models.Festivals;
import com.demo.festivalapp.festivalapp.R;

import java.io.ByteArrayOutputStream;

import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.STATE_NAME;
import static com.demo.festivalapp.festivalapp.UtilitiesClasses.GlobalVariables.festival_id;

public class AddNewWishActivty extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    Bitmap photo;
    LinearLayout btn_savewish;
    DatabaseHelper myDb;
    Spinner spinner_wishlist;
    EditText name_wishlist_item,image_notes;
    int clickable_count=0;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    Festivals festivals;
    TextView festival_latlng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_wish_activty);
        myDb = new DatabaseHelper(AddNewWishActivty.this);
        SetCustomTitle();
        GetData();
        SetUiWidgets();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
             photo = (Bitmap) data.getExtras().get("data");
            if(photo !=null)
            {
                imageView.setImageBitmap(photo);
//            onBackPressed();

            }

            }
    }
public void SetUiWidgets()
{
    festival_latlng=findViewById(R.id.festival_latlng);
    festival_latlng.setText("Location "+" lat"+festivals.getFestival_lat()+" , lng "+festivals.getFestival_lng());
    image_notes=findViewById(R.id.image_notes);
    name_wishlist_item=findViewById(R.id.name_wishlist_item);
    spinner_wishlist = (Spinner)findViewById(R.id.spinner_wishlist);
    btn_savewish=findViewById(R.id.btn_savewish);
    btn_savewish.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String spinner_selcted_value =  spinner_wishlist.getSelectedItem().toString();
            String name_wishlist=name_wishlist_item.getText().toString().trim();
            String image_notes_str=image_notes.getText().toString().trim();
            Log.d("spinner_selcted_value"+spinner_selcted_value,"name_wishlist"+name_wishlist);
            if(photo!= null)
            {
                if(name_wishlist.equals("")||name_wishlist.equals(null))
                {
                  Toast.makeText(getApplicationContext(),"Please Enter Name",Toast.LENGTH_SHORT).show();
                }
                else
                {
                        String make_latlng_string=festivals.getFestival_lat()+","+festivals.getFestival_lng();
                        btn_savewish.setEnabled(false);
                        SaveImageSqlite(name_wishlist, spinner_selcted_value, festival_id, getBytes(photo),image_notes_str,make_latlng_string);

                    }

                }
            else
            {
//                Log.d("BYTE","Array"+getBytes(photo));
                Toast.makeText(getApplicationContext(),"Plzz Take image first",Toast.LENGTH_LONG).show();
                onBackPressed();
            }

        }
    });
    imageView = (ImageView)this.findViewById(R.id.imageView1);
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }
    else
    {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }
}
    public void SaveImageSqlite(String name,String category,String festival_id,byte[] image,String notes,String latlng)
    {
        boolean isInserted = myDb.insertData(name,
                category,
                festival_id,
                image,
                notes,
                latlng);
        if(isInserted == true)
        {
            if(myDb.insertFestivalWishData(festivals.getId(),festivals.getTitle(),festivals.getStartDate()+","+festivals.getEnd_date(),
                    festivals.getGenre(),festivals.getFiles_profile(),STATE_NAME))
            {
                Toast.makeText(AddNewWishActivty.this, "Data Inserted", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
            else
            {
            Toast.makeText(getApplicationContext(),"Unable to add Data",Toast.LENGTH_SHORT).show();
            }

        }

        else
        {
            btn_savewish.setEnabled(true);
            Toast.makeText(AddNewWishActivty.this, "Data not Inserted", Toast.LENGTH_LONG).show();
        }
        }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
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
}

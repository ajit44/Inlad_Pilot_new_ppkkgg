package com.inland.pilot.LoadImage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.inland.pilot.Location.SetTripStatusUpdateModel;
import com.inland.pilot.MyTrip.TripMasterModel;
import com.inland.pilot.NavigationActivity;
import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.VehicleMaster.InsuranceCapture;
import com.inland.pilot.VehicleMaster.PermitCapture;
import com.inland.pilot.VehicleMaster.RcCapture;
import com.inland.pilot.databinding.ActivityLoadImageListUploadBinding;
import com.inland.pilot.databinding.ActivityVehicleDocsUploadBinding;

public class LoadUploadListActivity extends AppCompatActivity {
    private ActivityLoadImageListUploadBinding binding;
    private Context mCon;
    private boolean image_8, image_1, image_2, image_3, image_4, image_5, image_6, image_7;

    private String TripId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_load_image_list_upload);
        mCon = this;
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            TripId = bundle.getString("TripId");
        }

        refresh_text_labels();

        binding.image1Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, LoadImageCapture.class);
                Bundle bundle = new Bundle();
                bundle.putString("image_no", "1");
                bundle.putString("TripId", TripId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        binding.image2Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, LoadImageCapture.class);
                Bundle bundle = new Bundle();
                bundle.putString("image_no", "2");
                bundle.putString("TripId", TripId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        binding.image3Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, LoadImageCapture.class);
                Bundle bundle = new Bundle();
                bundle.putString("image_no", "3");
                bundle.putString("TripId", TripId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        binding.image4Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, LoadImageCapture.class);
                Bundle bundle = new Bundle();
                bundle.putString("image_no", "4");
                bundle.putString("TripId", TripId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        binding.image5Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, LoadImageCapture.class);
                Bundle bundle = new Bundle();
                bundle.putString("image_no", "5");
                bundle.putString("TripId", TripId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        binding.image6Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, LoadImageCapture.class);
                Bundle bundle = new Bundle();
                bundle.putString("image_no", "6");
                bundle.putString("TripId", TripId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        binding.image7Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, LoadImageCapture.class);
                Bundle bundle = new Bundle();
                bundle.putString("image_no", "7");
                bundle.putString("TripId", TripId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        binding.image8Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, LoadImageCapture.class);
                Bundle bundle = new Bundle();
                bundle.putString("image_no", "8");
                bundle.putString("TripId", TripId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        binding.saveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkUpload()) {
                    SharedPreferences preferences_shared = PreferenceManager.getDefaultSharedPreferences(mCon);
                    SharedPreferences.Editor editor = preferences_shared.edit();
                    editor.putBoolean("reaching_to_start", false);
                    editor.putBoolean("reaching_to_final", true);
                    editor.commit();
                    SharedPreferences pref = getSharedPreferences("tripImageLoad",MODE_PRIVATE);
                    pref.edit().putString("loadImage", "uploadImageDone").commit();

                    Intent intent=new Intent(mCon, NavigationActivity.class);
                    mCon.startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(mCon, "Upload atleat 4 images to proceed further !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private boolean checkUpload()
    {
        if(image_1 && image_2 && image_3 && image_4 )
            return  true;
        else
            return false;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        refresh_text_labels();
    }
    private void refresh_text_labels()
    {
        SharedPreferences preferences_shared = PreferenceManager.getDefaultSharedPreferences(this);
        image_1 = preferences_shared.getBoolean("image_1",false);
        if(image_1)
        {
            binding.image1Text.setText("Upload 1 Image (Done)");
            binding.image1Text.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.image2Linear.setVisibility(View.VISIBLE);
        }
        image_2 = preferences_shared.getBoolean("image_2",false);
        if(image_2)
        {
            binding.image2Text.setText("Upload 2 Image (Done)");
            binding.image2Text.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.image3Linear.setVisibility(View.VISIBLE);
        }
        image_3 = preferences_shared.getBoolean("image_3",false);
        if(image_3)
        {
            binding.image3Text.setText("Upload 3 Image (Done)");
            binding.image3Text.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.image4Linear.setVisibility(View.VISIBLE);
        }
        image_4 = preferences_shared.getBoolean("image_4",false);
        if(image_4)
        {
            binding.image4Text.setText("Upload 4 Image (Done)");
            binding.image4Text.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.image5Linear.setVisibility(View.VISIBLE);
        }
        image_5 = preferences_shared.getBoolean("image_5",false);
        if(image_5)
        {
            binding.image5Text.setText("Upload 5 Image (Done)");
            binding.image5Text.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.image6Linear.setVisibility(View.VISIBLE);
        }
        image_6 = preferences_shared.getBoolean("image_6",false);
        if(image_6)
        {
            binding.image6Text.setText("Upload 6 Image (Done)");
            binding.image6Text.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.image7Linear.setVisibility(View.VISIBLE);
        }
        image_7 = preferences_shared.getBoolean("image_7",false);
        if(image_7)
        {
            binding.image7Text.setText("Upload 7 Image (Done)");
            binding.image7Text.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.image8Linear.setVisibility(View.VISIBLE);
        }
        image_8 = preferences_shared.getBoolean("image_8",false);
        if(image_8)
        {
            binding.image8Text.setText("Upload 8 Image (Done)");
            binding.image8Text.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }
}
package com.inland.pilot.unloading;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.afollestad.materialdialogs.MaterialDialog;

import com.inland.pilot.Location.SetTripStatusUpdateModel;
import com.inland.pilot.Login.VerifyMobileNoResponseModel;
import com.inland.pilot.NavigationActivity;
import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.ActivityPodImageListUploadBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnloadingUploadListActivity extends AppCompatActivity {
    private ActivityPodImageListUploadBinding binding;
    private Context mCon;
    private boolean image_1, image_2, image_3, image_4, image_5, image_6;
    SharedPreferences pref;
    private String TripId="";
    private String loginIdStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pod_image_list_upload);
        mCon = this;
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            TripId = bundle.getString("TripId");
        }
        pref = mCon.getSharedPreferences("currentActiveTrip",MODE_PRIVATE);
        refresh_text_labels();

        if (PreferenceUtil.getUser() != null) {
            if (PreferenceUtil.getUser().getP_MOBILENO() != null && !PreferenceUtil.getUser().getP_MOBILENO().isEmpty()) {
                loginIdStr = PreferenceUtil.getUser().getP_MOBILENO();
            }
        }

        binding.image1Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, UnloadingImageCapture.class);
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
                Intent intent = new Intent(mCon, UnloadingImageCapture.class);
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
                Intent intent = new Intent(mCon, UnloadingImageCapture.class);
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
                Intent intent = new Intent(mCon, UnloadingImageCapture.class);
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
                Intent intent = new Intent(mCon, UnloadingImageCapture.class);
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
                Intent intent = new Intent(mCon, UnloadingImageCapture.class);
                Bundle bundle = new Bundle();
                bundle.putString("image_no", "6");
                bundle.putString("TripId", TripId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        binding.saveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkUpload()) {
                    SetTripStatusUpdateModel requestModel = new SetTripStatusUpdateModel("2", TripId, loginIdStr);
                    tripStatusUpdate(requestModel);
                }
                else
                {
                    Toast.makeText(mCon, "Upload atleast 3 images to proceed further !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private boolean checkUpload()
    {
        if(image_1 && image_2 && image_3)
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
        image_1 = preferences_shared.getBoolean("unloading_1",false);
        if(image_1)
        {
            binding.image1Text.setText("Upload 1 Image (Done)");
            binding.image1Text.setTextColor(getResources().getColor(R.color.colorPrimary));
            //binding.image1Text.setTextColor(ContextCompat.getColor(mCon, R.color.colorPrimary));
            binding.image2Linear.setVisibility(View.VISIBLE);
        }
        image_2 = preferences_shared.getBoolean("unloading_2",false);
        if(image_2)
        {
            binding.image2Text.setText("Upload 2 Image (Done)");
            binding.image2Text.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.image3Linear.setVisibility(View.VISIBLE);
        }
        image_3 = preferences_shared.getBoolean("unloading_3",false);
        if(image_3)
        {
            binding.image3Text.setText("Upload 3 Image (Done)");
            binding.image3Text.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.image4Linear.setVisibility(View.VISIBLE);
        }
        image_4 = preferences_shared.getBoolean("unloading_4",false);
        if(image_4)
        {
            binding.image4Text.setText("Upload 4 Image (Done)");
            binding.image4Text.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.image5Linear.setVisibility(View.VISIBLE);
        }
        image_5 = preferences_shared.getBoolean("unloading_5",false);
        if(image_5)
        {
            binding.image5Text.setText("Upload 5 Image (Done)");
            binding.image5Text.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.image6Linear.setVisibility(View.VISIBLE);
        }
        image_6 = preferences_shared.getBoolean("unloading_6",false);
        if(image_6)
        {
            binding.image6Text.setText("Upload 6 Image (Done)");
            binding.image6Text.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }
    private void tripStatusUpdate(SetTripStatusUpdateModel mpinModel) {
        Log.e("req",mpinModel.toString());
        try {
            Call<VerifyMobileNoResponseModel> call = ApiClient.getNetworkService().setTripStatus(mpinModel);

            final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                    .content(R.string.loading)
                    .canceledOnTouchOutside(false)
                    .progress(true, 0)
                    .widgetColorRes(R.color.colorPrimary)
                    .show();

            call.enqueue(new Callback<VerifyMobileNoResponseModel>() {
                @Override
                public void onResponse(Call<VerifyMobileNoResponseModel> call, Response<VerifyMobileNoResponseModel> response) {
                    Log.e("response",""+response.code());
                    if (response.isSuccessful()) {

                        if (response.body() != null && response.body().getVerifyMobileNoModels() != null &&
                                !response.body().getVerifyMobileNoModels().isEmpty()) {
                            String messageStr = response.body().getVerifyMobileNoModels().get(0).getMESSAGE();
                            Log.e("status",messageStr);
                            if (messageStr.equalsIgnoreCase("Trip Completed Successfully"))
                            {
                                Toast.makeText(mCon, "Trip Completed Successfully !", Toast.LENGTH_LONG).show();

                                pref.edit().putBoolean("isTripActive", false).commit();

                                SharedPreferences preferences_shared = PreferenceManager.getDefaultSharedPreferences(mCon);
                                SharedPreferences.Editor editor = preferences_shared.edit();
                                editor.putBoolean("reaching_to_start", false);
                                editor.putBoolean("reaching_to_final", true);
                                editor.commit();
                                editor.remove("unloading_1");
                                editor.remove("unloading_2");
                                editor.remove("unloading_3");
                                editor.remove("unloading_4");
                                editor.remove("unloading_5");
                                editor.remove("unloading_6");
                                editor.commit();

                                SharedPreferences pref = getSharedPreferences("tripImageLoad",MODE_PRIVATE);
                                pref.edit().putString("loadImage", "pending").commit();

                                Intent intent=new Intent(mCon, NavigationActivity.class);
                                mCon.startActivity(intent);
                                UnloadingUploadListActivity.this.finish();

                            } else {
                                Toast.makeText(mCon, "1" + messageStr, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mCon, R.string.no_data, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mCon, R.string.something_went_wrong+11, Toast.LENGTH_SHORT).show();
                        Log.d("check", "onResponse Trip Status update: " + response.errorBody().toString());
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<VerifyMobileNoResponseModel> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Toast.makeText(mCon, "Please check internet connection", Toast.LENGTH_SHORT).show();
                        Log.d("check", "onFailure: " + t.getMessage());
                    }
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("check", "setMPin: " + e.getMessage());
        }
    }
}
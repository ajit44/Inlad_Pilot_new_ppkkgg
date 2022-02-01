package com.inland.pilot.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.inland.pilot.BankDetails.BankDetailsActivity;
import com.inland.pilot.BankDetails.BankDetailsFragment;
import com.inland.pilot.Login.LoginDetailsModel;
import com.inland.pilot.Login.LoginResponseModel;
import com.inland.pilot.Login.ModifyRegistrationActivity;
import com.inland.pilot.Login.PinLoginActivity;
import com.inland.pilot.Login.RegistrationActivity;
import com.inland.pilot.Login.SetMpinModel;
import com.inland.pilot.MainActivity;
import com.inland.pilot.NavigationActivity;
import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.databinding.ActivityProfileBinding;
import com.inland.pilot.Login.LoginActivity;
import com.inland.pilot.Util.PreferenceUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private Context mCon;
    private String TSTATUS,loginMobileNoStr,deviceIdStr,loginPinStr;
    private LoginDetailsModel loginDetailsModel;
    String mobileNoStr="";
    TextView AdharTextIndicator,PanTextIndicator,DLTextIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        mobileNoStr = getIntent().getStringExtra("mobileNo");
        mCon = this;

        AdharTextIndicator = findViewById(R.id.AdharTextIndicator);
        PanTextIndicator = findViewById(R.id.PanTextIndicator);
        DLTextIndicator = findViewById(R.id.DLTextIndicator);


        fetchDriverImage();
       // isUserActivated();

        SharedPreferences preferences_shared = PreferenceManager.getDefaultSharedPreferences(this);
        boolean is_adhar_upload = preferences_shared.getBoolean("is_adhar_upload",false);
        if(is_adhar_upload)
        {
            binding.AdharText.setText("Upload Adhar Details (Done)");
        }
        boolean is_pan_upload = preferences_shared.getBoolean("is_pan_upload",false);
        if(is_pan_upload)
        {
            binding.PanText.setText("Upload PAN Details (Done)");
        }

        boolean is_dl_upload = preferences_shared.getBoolean("is_dl_upload",false);
        if(is_dl_upload)
        {
            binding.DLText.setText("Upload Driving License Details (Done)");
        }

        binding.personalDetailsLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, PersonalDetailsActivity.class);
                startActivity(intent);
            }
        });

        binding.bankDetailsLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, BankDetailsActivity.class);
                startActivity(intent);
            }
        });

        binding.changePasswordLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, ChangePinActivity.class);
                startActivity(intent);
            }
        });

        binding.AdharLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, AdharCapture.class);
                startActivity(intent);
            }
        });

        binding.panLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, PanActivity.class);
                startActivity(intent);
            }
        });

        binding.DrivingLisenceLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, DlActivity.class);
                startActivity(intent);
            }
        });

        binding.RegisterLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, ModifyRegistrationActivity.class);
                startActivity(intent);
            }
        });

        binding.logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(mCon)
                        .title("Logout")
                        .titleColor(getResources().getColor(R.color.colorPrimaryDark))
                        .content("Are you sure, you want to logout?")
                        .contentColor(getResources().getColor(R.color.colorPrimaryLight))
                        .positiveText("Yes")
                        .positiveColor(getResources().getColor(R.color.colorAccent))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                PreferenceUtil.setUserLoggedIn(false);
                                //PreferenceUtil.clearAll();
                                Intent intent = new Intent(mCon, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .negativeText("Cancel")
                        .negativeColor(getResources().getColor(R.color.colorAccent))
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        if (PreferenceUtil.getUser() != null) {
            loginDetailsModel = PreferenceUtil.getUser();

            if (loginDetailsModel.getP_MOBILENO() != null && !loginDetailsModel.getP_MOBILENO().isEmpty()) {
                loginMobileNoStr = loginDetailsModel.getP_MOBILENO();
            } else {
                loginMobileNoStr = "";
            }
        }
        if (PreferenceUtil.getDeviceId() != null && !PreferenceUtil.getDeviceId().isEmpty()) {
            deviceIdStr = PreferenceUtil.getDeviceId();
            Log.d("check", "onCreate: " + deviceIdStr);
        } else {
            deviceIdStr = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }

        if (PreferenceUtil.getUser() != null)
            loginPinStr = PreferenceUtil.getPin();
    }

    void fetchDriverImage()
    {

        final JSONObject request = new JSONObject();
        try {
            request.put("LoginId",mobileNoStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post("https://api.inland.in/Pilot/api/Driver/GetDriverImage")
                .addJSONObjectBody(request)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                       //   Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        try {
                            JSONArray data = response.getJSONArray("Table");
                            AdharTextIndicator.setText("(Pending)");
                            AdharTextIndicator.setTextColor(ContextCompat.getColor(mCon, R.color.red_alert));

                            PanTextIndicator.setText("(Pending)");
                            PanTextIndicator.setTextColor(ContextCompat.getColor(mCon, R.color.red_alert));

                            DLTextIndicator.setText("(Pending)");
                            DLTextIndicator.setTextColor(ContextCompat.getColor(mCon, R.color.red_alert));

                            for(int i=0;i<data.length();i++)
                            {
                                JSONObject  WHA1=data.getJSONObject(i);
                                if(WHA1.getString("ImageType").equals("ADHAR")){
                                    AdharTextIndicator.setText("(Completed)");
                                    AdharTextIndicator.setTextColor(ContextCompat.getColor(mCon, R.color.teal_700));
                                } if(WHA1.getString("ImageType").equals("PAN")){
                                    PanTextIndicator.setText("(Completed)");
                                    PanTextIndicator.setTextColor(ContextCompat.getColor(mCon, R.color.teal_700));
                                } if(WHA1.getString("ImageType").equals("DRIV")){
                                    DLTextIndicator.setText("(Completed)");
                                    DLTextIndicator.setTextColor(ContextCompat.getColor(mCon, R.color.teal_700));
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ERROR", anError.getMessage());
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(AdharTextIndicator != null || !mobileNoStr.equals("")) {
            isUserActivated();
            fetchDriverImage();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(mCon, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    void isUserActivated()
    {

        final JSONObject request = new JSONObject();
        try {
            request.put("P_MobileNo",mobileNoStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post("https://api.inland.in/Pilot/api/Driver/Get_Registration")
                .addJSONObjectBody(request)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        try {
                            JSONArray data = response.getJSONArray("Table");

                            for(int i=0;i<data.length();i++)
                            {
                                JSONObject  WHA1=data.getJSONObject(i);
                                if(WHA1.getString("IsActive").equalsIgnoreCase("Y")){
                                    userActivatedDialog();
                                  }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ERROR", anError.getMessage());
                    }
                });
    }



   void userActivatedDialog(){
       Dialog notFoundDialog = new Dialog(this);
        notFoundDialog.setContentView(R.layout.user_activated_dialog);
        notFoundDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        notFoundDialog.setCancelable(false);
        Window window = notFoundDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        final WindowManager.LayoutParams params = window.getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes((WindowManager.LayoutParams) params);
        ImageView Icon = notFoundDialog.findViewById(R.id.dialog_icon);
        TextView title = notFoundDialog.findViewById(R.id.dialog_title);
        LinearLayout backBtn = notFoundDialog.findViewById(R.id.dialog_button_positive);
        Icon.setImageDrawable(this.getResources().getDrawable(R.drawable.done));
        backBtn.setVisibility(View.INVISIBLE);
        title.setText("Your Account is Activated!");
        notFoundDialog.show();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notFoundDialog.dismiss();
            }
        });
        //delay...
       final Handler handler = new Handler(Looper.getMainLooper());
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               SetMpinModel setMpinModel = new SetMpinModel(deviceIdStr, loginPinStr, loginMobileNoStr);
               verifyPin(setMpinModel);
           }
       }, 2000);
    }

    private void verifyPin(SetMpinModel mpinModel) {
        try {

            Call<LoginResponseModel> call = ApiClient.getNetworkService().verifyMpin(mpinModel);

            final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                    .content(R.string.loading)
                    .canceledOnTouchOutside(false)
                    .progress(true, 0)
                    .widgetColorRes(R.color.colorPrimary)
                    .show();

            call.enqueue(new Callback<LoginResponseModel>() {
                @Override
                public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {

                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getLoginDetailsModels() != null &&
                                !response.body().getLoginDetailsModels().isEmpty()) {
                            Log.e("tstaus2",response.body().toString());
                            LoginDetailsModel loginModel = response.body().getLoginDetailsModels().get(0);
                            Log.e("tstaus",loginModel.toString());

                            String messageStr = loginModel.getMESSAGE();
                            String tokenStr = loginModel.getTOKENNO();
                            TSTATUS = loginModel.getTSTATUS();
                            Log.e("aaadeviceId",deviceIdStr);
                            Log.e("aaamobile",loginMobileNoStr);
                            Log.e("aaapin",mpinModel.getPIN());
                            String registrationStatusStr = loginModel.getRACTIVE();
                            Log.e("response ",registrationStatusStr+"");
                            //  Log.e("token",tokenStr);
                            //  Log.e("deviceId",deviceIdStr);
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
                            String loginIdStr = preferences.getString("LoginId", "");
                            String TripId = preferences.getString("TripId", "");
                            //deviceIdStr=preferences.getString("deviceIdStr","");
                            String tokenNoStr = preferences.getString("tokenNoStr", "");
                            boolean reaching_to_start = preferences.getBoolean("reaching_to_start", false);
                            boolean reaching_to_final = preferences.getBoolean("reaching_to_final", false);

                            if (messageStr.equalsIgnoreCase("login successfully")) {

                                PreferenceUtil.clearAll();
                                /*PreferenceUtil.clearPin();*/
                                PreferenceUtil.clearToken();
                                PreferenceUtil.clearRegistrationStatus();
                                PreferenceUtil.setTokenNo(tokenStr);
                                PreferenceUtil.setDeviceId(deviceIdStr);
                                PreferenceUtil.setRegistrationStatus(registrationStatusStr);
                                LoginDetailsModel model_login=new LoginDetailsModel(messageStr, tokenStr, registrationStatusStr,
                                        loginModel.getID(), loginModel.getNAME(), loginModel.getS_MOBILENO(),
                                        loginModel.getPANNO(), loginModel.getAADHARNO(), loginModel.getDLNO(),
                                        loginModel.getSTATECODE(), loginModel.getADDRESS(), loginModel.getPINCODE(),
                                        loginModel.getNO_OF_VEHICLES(), loginModel.getTYPE(), loginModel.getDeviceId(),
                                        loginModel.getP_MOBILENO(), loginModel.getPIN(),loginModel.getTSTATUS(),loginModel.getIMAGEUPLOAD());
                                model_login.setTSTATUS(TSTATUS);
                                PreferenceUtil.setUser(model_login);
                                PreferenceUtil.setUserLoggedIn(true);



                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("mobileNoStr", loginMobileNoStr);
                                editor.putString("LoginId", loginIdStr+"");
                                editor.putString("TripId", TripId);
                                editor.putString("deviceIdStr", deviceIdStr+"");
                                editor.putString("tokenNoStr", tokenNoStr+"");
                                editor.putBoolean("reaching_to_start", reaching_to_start);
                                editor.putBoolean("reaching_to_final", reaching_to_final);
                                editor.commit();

                                PreferenceUtil.setPin(mpinModel.getPIN());
                                if(TSTATUS.equalsIgnoreCase("Y")) {
                                    //  if(1==1)
                                    //{
                                    Intent intent = new Intent(mCon, NavigationActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                                else if(loginModel.getTYPE().equals("")){
                                    Intent intent = new Intent(mCon, RegistrationActivity.class);
                                    intent.putExtra("mobileNo", loginMobileNoStr);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(mCon, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                                }
                            } else {
                                Toast.makeText(mCon, "" + messageStr, Toast.LENGTH_SHORT).show();
                            }
                            /*if (response.body().getDashboardStatus() != null &&
                                    !response.body().getDashboardStatus().isEmpty()) {
                                PreferenceUtil.setDashboardStatus(response.body().getDashboardStatus());
                            } else {
                                PreferenceUtil.setDashboardStatus("N");
                            }*/
                        } else {
                            Toast.makeText(mCon, R.string.no_data, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mCon, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                        Log.d("check", "onResponse setMPin: " + response.errorBody());
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                    if (!call.isCanceled()) {
                        //Snackbar.make(binding.linearLayoutView,"Please check internet connection.",Snackbar.LENGTH_LONG).show();
                        Toast.makeText(mCon, "Please check internet connection", Toast.LENGTH_SHORT).show();
                        Log.e("check", "onFailure: " + t.getMessage());
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
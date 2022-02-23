package com.inland.pilot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import com.afollestad.materialdialogs.MaterialDialog;

import com.google.android.material.button.MaterialButton;
import com.inland.pilot.Login.LoginDetailsModel;
import com.inland.pilot.Login.LoginResponseModel;
import com.inland.pilot.Login.PinLoginActivity;
import com.inland.pilot.Login.RegistrationActivity;
import com.inland.pilot.Login.SetMpinModel;
import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.Profile.AdharCapture;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.ActivitySplashScreenBinding;
import com.inland.pilot.Login.LoginActivity;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {
    private ActivitySplashScreenBinding binding;
    private Context mCon;
    String deviceIdStr,loginPinStr,loginMobileNoStr,TSTATUS;
    private LoginDetailsModel loginDetailsModel;
    SharedPreferences DisclosurePref;
    int lang_selected;
    SharedPreferences prefSettings;
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
    };
    private static final int REQUEST_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefSettings = getSharedPreferences("AppSettings",MODE_PRIVATE);
        String languageToLoad  = prefSettings.getString("Language","en"); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
        mCon = this;

        final Handler handler = new Handler();

           DisclosurePref = mCon.getSharedPreferences("permissionsDisclosure",MODE_PRIVATE);
          Boolean viewDisclosure = DisclosurePref.getBoolean("viewDisclosure", true);

          if(viewDisclosure) {
              //binding.disclosureLayout.setVisibility(View.VISIBLE);
              showDisclosure();
          }
          else {
              handler.postDelayed(new Runnable() {
                  @Override
                  public void run() {
                      if (!PreferenceUtil.isUserLoggedIn()) {
                          startActivity(new Intent(mCon, LoginActivity.class));
                          finish();
                      } else {
                          // startActivity(new Intent(mCon, PinLoginActivity.class));
                          fetchRequiredData();
                          SetMpinModel setMpinModel = new SetMpinModel(deviceIdStr, loginPinStr, loginMobileNoStr);
                          verifyPin(setMpinModel);
                      }
                  }
              }, 2400);
          }
    }

    private void showDisclosure() {
        Dialog dialog = new Dialog(SplashScreenActivity.this);
        dialog.setContentView(R.layout.disclosure_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        final WindowManager.LayoutParams params = window.getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes((WindowManager.LayoutParams) params);
        TextView Privacy_link = dialog.findViewById(R.id.Privacy_link);
        MaterialButton declineBtn = dialog.findViewById(R.id.declineDisclosure);
        MaterialButton acceptBtn = dialog.findViewById(R.id.acceptDisclosure);
        dialog.show();
        Privacy_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCon.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.inlandworldlogistics.com/privacypolicy/")));
            }
        });
        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                DisclosurePref.edit().putBoolean("viewDisclosure", false).commit();
                GetPermission();
              selectLanguage();
            }
        });
    }

    private void selectLanguage(){
        final String[] Language = {"ENGLISH","हिन्दी","मराठी"};
        final int checkItem;
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SplashScreenActivity.this);
        dialogBuilder.setTitle("Select a Language")
                .setSingleChoiceItems(Language, lang_selected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(Language[i].equals("ENGLISH")){
                            prefSettings.edit().putString("Language", "en").commit();
                        }
                        if(Language[i].equals("हिन्दी"))
                        {
                            prefSettings.edit().putString("Language", "hi").commit();
                        }
                        if(Language[i].equals("मराठी"))
                        {
                            prefSettings.edit().putString("Language", "mr").commit();
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if (!PreferenceUtil.isUserLoggedIn()) {
                            startActivity(new Intent(mCon, LoginActivity.class));
                            finish();
                        } else {
                            // startActivity(new Intent(mCon, PinLoginActivity.class));
                            fetchRequiredData();
                            SetMpinModel setMpinModel = new SetMpinModel(deviceIdStr, loginPinStr, loginMobileNoStr);
                            verifyPin(setMpinModel);
                        }
                    }
                });
        dialogBuilder.create().show();
    }
    private void fetchRequiredData() {
        if (PreferenceUtil.getUser() != null) {
            loginDetailsModel = PreferenceUtil.getUser();

            if (loginDetailsModel.getP_MOBILENO() != null && !loginDetailsModel.getP_MOBILENO().isEmpty()) {
                loginMobileNoStr = loginDetailsModel.getP_MOBILENO();
            } else {
                loginMobileNoStr = "";
            }
        }

        if (PreferenceUtil.getUser() != null)
            loginPinStr = PreferenceUtil.getPin();

        if (PreferenceUtil.getDeviceId() != null && !PreferenceUtil.getDeviceId().isEmpty()) {
            deviceIdStr = PreferenceUtil.getDeviceId();
            Log.d("check", "onCreate: " + deviceIdStr);
        } else {
            deviceIdStr = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
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
                            Log.e("deviceId",deviceIdStr);
                            Log.e("mobile",loginMobileNoStr);
                           // Log.e("pin",mpinModel.getPIN());
                            String registrationStatusStr = loginModel.getRACTIVE();
                            Log.e("response ",registrationStatusStr+"");
                            //  Log.e("token",tokenStr);
                            //  Log.e("deviceId",deviceIdStr);
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SplashScreenActivity.this);
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
                                    Intent intent = new Intent(mCon, com.inland.pilot.NavigationActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }

                                else
                                {
                                    startActivity(new Intent(mCon, PinLoginActivity.class));
                                    finish();

                                }
                            } else {
                               //if pin not match
                                openLoginActivity();
                            }
                            /*if (response.body().getDashboardStatus() != null &&
                                    !response.body().getDashboardStatus().isEmpty()) {
                                PreferenceUtil.setDashboardStatus(response.body().getDashboardStatus());
                            } else {
                                PreferenceUtil.setDashboardStatus("N");
                            }*/
                        } else {
                            openLoginActivity();
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

    private void openLoginActivity(){
        PreferenceUtil.setUserLoggedIn(false);
        PreferenceUtil.clearAll();
        Intent intent = new Intent(mCon, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void GetPermission()
    {
        if (ActivityCompat.checkSelfPermission(mCon, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mCon, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    (Activity) mCon,
                    PERMISSIONS_LOCATION,
                    REQUEST_LOCATION
            );
        } else if (ActivityCompat.checkSelfPermission(mCon, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mCon, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    (Activity) mCon,
                    PERMISSIONS_LOCATION,
                    REQUEST_LOCATION
            );
        }
    }

}
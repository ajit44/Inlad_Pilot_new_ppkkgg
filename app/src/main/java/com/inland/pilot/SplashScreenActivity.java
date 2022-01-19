package com.inland.pilot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import com.inland.pilot.Login.LoginDetailsModel;
import com.inland.pilot.Login.LoginResponseModel;
import com.inland.pilot.Login.PinLoginActivity;
import com.inland.pilot.Login.RegistrationActivity;
import com.inland.pilot.Login.SetMpinModel;
import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.ActivitySplashScreenBinding;
import com.inland.pilot.Login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {
    private ActivitySplashScreenBinding binding;
    private Context mCon;
    String deviceIdStr,loginPinStr,loginMobileNoStr,TSTATUS;
    private LoginDetailsModel loginDetailsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);

        mCon = this;

        final Handler handler = new Handler();
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
                            Log.e("pin",mpinModel.getPIN());
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
}
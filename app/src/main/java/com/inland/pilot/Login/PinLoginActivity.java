package com.inland.pilot.Login;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.afollestad.materialdialogs.MaterialDialog;
import com.inland.pilot.MainActivity;
import com.inland.pilot.NavigationActivity;
import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.ActivityPinLoginBinding;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PinLoginActivity extends AppCompatActivity {
    private ActivityPinLoginBinding binding;
    private Context mCon;
    private String pinStr, loginPinStr;
    private LoginDetailsModel loginDetailsModel;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private String deviceIdStr;
    private String TSTATUS;
    private String loginMobileNoStr;
    private static final int REQUEST_LOCATION = 1;
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pin_login);

        mCon = this;

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

        if (ActivityCompat.checkSelfPermission(mCon, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mCon, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    (Activity)mCon,
                    PERMISSIONS_LOCATION,
                    REQUEST_LOCATION
            );
        }

        /*Log.d("check", "onCreate mobileNo: " + PreferenceUtil.getUser().getMobileNo());
        Log.d("check", "onCreate Pin: " + PreferenceUtil.getPin());*/

        if (PreferenceUtil.getUser() != null) {
            loginPinStr = PreferenceUtil.getPin();
            /*
            if (PreferenceUtil.getUser().getPIN() != null && !PreferenceUtil.getUser().getPIN().isEmpty()) {
                loginPinStr = PreferenceUtil.getPin();

            } else {
                loginPinStr = "";
            }

             */
        }

        binding.pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.d("check", "onTextChanged() called with: s = [" + s + "], start = [" + start + "], before = [" + before + "], count = [" + count + "]");
                Log.e("pin no",loginPinStr+"");
                Log.e("token no",PreferenceUtil.getTokenNo()+"");
                Log.e("device no",PreferenceUtil.getDeviceId()+"");
                if (start == 3 && s != null) {
                 /*   if (loginPinStr.equalsIgnoreCase(String.valueOf(s))) {
                        //Toast.makeText(mCon, "successfully login", Toast.LENGTH_SHORT).show();

                        if(PreferenceUtil.getUser().getTSTATUS().equalsIgnoreCase("Y"))
                        {
                            Intent intent = new Intent(mCon, NavigationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Intent intent = new Intent(mCon, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }




                    } else {
                        Log.e("pin no",loginPinStr+"");
                        //Toast.makeText(mCon, "login pin is "+loginPinStr, Toast.LENGTH_SHORT).show();
                        Toast.makeText(mCon, "Incorrect PIN", Toast.LENGTH_SHORT).show();
                    }

                  */
                    com.inland.pilot.Login.SetMpinModel setMpinModel = new com.inland.pilot.Login.SetMpinModel(deviceIdStr, String.valueOf(s), loginMobileNoStr);
                    verifyPin(setMpinModel);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceUtil.setUserLoggedIn(false);
                PreferenceUtil.clearAll();
                Intent intent = new Intent(mCon, com.inland.pilot.Login.LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    /*private void validatePin(String loginPin) {
        boolean isValidPin = false;

        if (pinStr.length() > 0) {
            if (pinStr.length() < 4) {
                binding.pinTextLayout.setError(getResources().getString(R.string.enter_valid_pin));
            } else {
                if (loginPinStr.equalsIgnoreCase(pinStr)) {
                    binding.pinTextLayout.setError(null);
                    isValidPin = true;
                } else {
                    binding.pinTextLayout.setError(getResources().getString(R.string.enter_valid_pin));
                }
            }
        } else {
            binding.pinTextLayout.setError(getResources().getString(R.string.cannot_be_empty));
        }

        if (isValidPin) {
            Intent intent = new Intent(mCon, MainActivity.class);
            PreferenceUtil.setUserLoggedIn(true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }*/
//////////////////////////////////////

    private void verifyPin(com.inland.pilot.Login.SetMpinModel mpinModel) {
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
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PinLoginActivity.this);
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
                                    Intent intent = new Intent(mCon, com.inland.pilot.Login.RegistrationActivity.class);
                                    intent.putExtra("mobileNo", loginMobileNoStr);
                                    startActivity(intent);
                                }
                                else if(loginModel.getIMAGEUPLOAD().equals("N"))
                                    showDialogForKYC();
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

    private void showDialogForKYC() {
            Dialog notFoundDialog = new Dialog(this);
            notFoundDialog.setContentView(R.layout.no_internet_layout);
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
            TextView msg = notFoundDialog.findViewById(R.id.dialog_msg);
            LinearLayout backBtn = notFoundDialog.findViewById(R.id.dialog_button_positive);
            backBtn.setVisibility(View.GONE);
            LinearLayout backAndContinueLayout = notFoundDialog.findViewById(R.id.bottom);
            LinearLayout skip_btn = notFoundDialog.findViewById(R.id.skip_btn);
            LinearLayout update_next_btn = notFoundDialog.findViewById(R.id.update_next_btn);
            backAndContinueLayout.setVisibility(View.VISIBLE);
            Icon.setImageDrawable(this.getResources().getDrawable(R.drawable.update));
            //Icon.setBackgroundColor(R.color.white);
            title.setText("KYC PENDING");
            msg.setVisibility(View.VISIBLE);
            msg.setText("For full access of this Application\nplease complete you KYC details");
            notFoundDialog.show();
            skip_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notFoundDialog.dismiss();
                    Intent intent = new Intent(mCon, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });
          update_next_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notFoundDialog.dismiss();
                    Intent intent = new Intent(mCon, MainActivity.class);
                    intent.putExtra("goForKYC", "goForKYC");
                    intent.putExtra("mobileNo", loginMobileNoStr);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });

    }
}
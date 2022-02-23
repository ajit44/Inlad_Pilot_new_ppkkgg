package com.inland.pilot.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.inland.pilot.MainActivity;
import com.inland.pilot.NavigationActivity;
import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.SettingsActivity;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.ActivityLoginBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private boolean already_exists=false;
    private ActivityLoginBinding binding;
    private Context mCon;
    private TextInputLayout otpTextInput;
    private TextInputEditText otpEditText, confirmPinEditText, newPinEditText;
    private TextView resendOtpTextView, otpTimerTextView, cancelTextView, submitTextView;
    private String otpStr, mobileNoStr, deviceIdStr, pinStr, loginMobileNoStr, loginPinStr;
    private CountDownTimer countDownTimer;
    private LoginDetailsModel loginDetailsModel;
    private SetMpinModel setMpinModel;
    private boolean isValidMobileNo = false;
    private MaterialButton changePinButton;
    private MaterialDialog dialog_pin;
    private String TSTATUS;
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
    };
    private static final int REQUEST_LOCATION = 0;


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefSettings = getSharedPreferences("AppSettings",MODE_PRIVATE);
        String languageToLoad  = prefSettings.getString("Language","en"); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        mCon = this;


        if (PreferenceUtil.getUser() != null) {
            loginDetailsModel = PreferenceUtil.getUser();

            if (loginDetailsModel.getP_MOBILENO() != null && !loginDetailsModel.getP_MOBILENO().isEmpty()) {
                loginMobileNoStr = loginDetailsModel.getP_MOBILENO();
            } else {
                loginMobileNoStr = "";
            }

            if (loginDetailsModel.getPIN() != null && !loginDetailsModel.getPIN().isEmpty()) {
                loginPinStr = loginDetailsModel.getPIN();
            } else {
                loginPinStr = "";
            }
        }
        PackageInfo pInfo = null;
        try {
            pInfo = mCon.getPackageManager().getPackageInfo(mCon.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        binding.versionCode.setText(" "+pInfo.versionCode);
        if (PreferenceUtil.getDeviceId() != null && !PreferenceUtil.getDeviceId().isEmpty()) {
            deviceIdStr = PreferenceUtil.getDeviceId();
            Log.d("check", "onCreate: " + deviceIdStr);
        } else {
            deviceIdStr = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }

       // SharedPreferences DisclosurePref = mCon.getSharedPreferences("permissionsDisclosure",MODE_PRIVATE);
       // Boolean viewDisclosure = DisclosurePref.getBoolean("viewDisclosure", true);



        binding.mobileNoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (start == 9 && charSequence != null) {
                    validateMobileNo(String.valueOf(charSequence), deviceIdStr, "pinLogin");
                    Log.d("check", "onTextChanged: " + start + " " + charSequence);
                    //Toast.makeText(mCon, "valid number", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.loginWithOtpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.pinTextLayout.setVisibility(View.GONE);
                binding.loginWithPinTextView.setVisibility(View.VISIBLE);
                binding.loginWithOtpTextView.setVisibility(View.GONE);
                binding.pinLoginButton.setVisibility(View.GONE);
                binding.otpLoginButton.setVisibility(View.VISIBLE);
            }
        });

        binding.loginWithPinTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.pinTextLayout.setVisibility(View.VISIBLE);
                binding.loginWithPinTextView.setVisibility(View.GONE);
                binding.loginWithOtpTextView.setVisibility(View.VISIBLE);
                binding.pinLoginButton.setVisibility(View.VISIBLE);
                binding.otpLoginButton.setVisibility(View.GONE);
            }
        });

        binding.registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(mCon, RegistrationActivity.class));
               // finish();
                binding.pinTextLayout.setVisibility(View.GONE);
                binding.loginWithPinTextView.setVisibility(View.VISIBLE);
                binding.loginWithOtpTextView.setVisibility(View.GONE);
                binding.pinLoginButton.setVisibility(View.GONE);
                binding.otpLoginButton.setVisibility(View.VISIBLE);
            }
        });

        binding.forgotPinTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mCon, ForgetPinActivity.class));
                finish();
            }
        });

        binding.pinLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileNoStr = binding.mobileNoEditText.getText().toString().trim();
                if(mobileNoStr.length()<10)
                {
                    binding.mobileNoEditText.setError("Enter valid mobile no.");
                    return;
                }
                pinStr = binding.pinEditText.getText().toString().trim();

                validatePin(deviceIdStr);
            }
        });

        binding.otpLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileNoStr = binding.mobileNoEditText.getText().toString().trim();
                if(mobileNoStr.length()<10)
                {
                    binding.mobileNoEditText.setError("Enter valid mobile no.");
                    return;
                }
                openOtpDialog(mobileNoStr, deviceIdStr);
                validateMobileNo(mobileNoStr, deviceIdStr, "otpLogin");
            }
        });

        /*binding.otpLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileNoStr = binding.mobileNoEditText.getText().toString();
                //openOtpDialog(mobileNoStr);
                if (mobileNoStr.length() > 0) {
                    if (mobileNoStr.length() < 10) {
                        binding.mobileNoTextLayout.setError("" + getResources().getString(R.string.valid_mobile_no));
                    } else if (!mobileNoStr.startsWith("6") && !mobileNoStr.startsWith("7") &&
                            !mobileNoStr.startsWith("8") && !mobileNoStr.startsWith("9")) {
                        binding.mobileNoTextLayout.setError("" + getResources().getString(R.string.valid_mobile_no));
                    } else {
                        binding.mobileNoTextLayout.setError(null);
                        //startActivity(new Intent(mCon, MainActivity.class));
                        RequestModel otpModel = new RequestModel(mobileNoStr);
                        verifyMobileNo(otpModel, deviceIdStr);
                    }
                } else {
                    binding.mobileNoTextLayout.setError("" + getResources().getString(R.string.cannot_be_empty));
                }
            }
        });*/
        RequestLocationWithDisclosure();
    }

    private void validateMobileNo(String mobileNo, String androidDeviceId, String validateType) {
        Log.d("check", "validateMobileNo: " + mobileNo);
        if (mobileNo.length() > 0) {
            if (mobileNo.length() == 10) {
                binding.mobileNoTextLayout.setError(null);
                isValidMobileNo = true;
            } else {
                binding.mobileNoTextLayout.setError("" + getResources().getString(R.string.valid_mobile_no));
                isValidMobileNo = false;
            }
        } else {
            binding.mobileNoTextLayout.setError(getResources().getString(R.string.valid_mobile_no));
            isValidMobileNo = false;
        }

        if (isValidMobileNo) {
            if (validateType.equalsIgnoreCase("pinLogin")) {
                RequestModel otpModel = new RequestModel(mobileNo);
                verifyMobileNo(otpModel, androidDeviceId);

            } else if (validateType.equalsIgnoreCase("otpLogin")) {
                RequestModel otpModel = new RequestModel(mobileNo);
                requestOtp(otpModel, androidDeviceId);
            }
        }
    }

    private void validatePin(String deviceId) {
        boolean isValidPin = false, isValidDeviceId = false;

        if (!TextUtils.isEmpty(deviceId)) {
            isValidDeviceId = true;
        } else {
            isValidDeviceId = false;
        }

        if (pinStr.length() > 0) {
            if (pinStr.length() < 4) {
                binding.pinTextLayout.setError(getResources().getString(R.string.enter_valid_pin));
            } else {
                binding.pinTextLayout.setError(null);
                isValidPin = true;
            }
        } else {
            binding.pinTextLayout.setError(getResources().getString(R.string.cannot_be_empty));
        }

        if (isValidPin && true && isValidDeviceId) {
            setMpinModel = new SetMpinModel(deviceId, pinStr, mobileNoStr);
            verifyPin(setMpinModel);
        }
    }

    private void openOtpDialog(String mobileNo, String androidDeviceId) {
        MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                .title("Verify OTP")
                .customView(R.layout.verify_otp_dialog, true)
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .show();

        View customView = dialog.getCustomView();
        otpTextInput = customView.findViewById(R.id.otpTextLayout);
        otpEditText = customView.findViewById(R.id.otpEditText);
        resendOtpTextView = customView.findViewById(R.id.resendOtpTextView);
        otpTimerTextView = customView.findViewById(R.id.otpTimerTextView);
        cancelTextView = customView.findViewById(R.id.cancelTextView);
        submitTextView = customView.findViewById(R.id.submitTextView);

        resendOtpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //verifyMobileNo();
                dialog.dismiss();
                RequestModel otpModel = new RequestModel(mobileNo);
                resendOtp(otpModel, androidDeviceId);
            }
        });

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.cancel();
                dialog.dismiss();
            }
        });

        submitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otpStr = otpEditText.getText().toString();
                //mobileNo = otpEditText.getText().toString();
                Log.d("check", "onClick otpStr: " + otpStr);
                Log.d("check", "onClick mobileNo: " + mobileNo);
              //  validateOtp(mobileNo, deviceIdStr, dialog);
                if(otpStr.length()==4){
                    VerifyOtpModel verifyOtpModel = new VerifyOtpModel(mobileNo, otpStr, otpStr);
                    Log.d("check", "onClick: " + verifyOtpModel);
                    verifyOtp(verifyOtpModel, dialog);
                }
            }
        });
    }

    private void openPinDialog(String mobileNo, String androidDeviceId, String type) {
        dialog_pin = new MaterialDialog.Builder(mCon)
                .title("Set PIN")
                .customView(R.layout.fragment_change_pin, true)
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .show();

        View customView = dialog_pin.getCustomView();
        newPinEditText = customView.findViewById(R.id.newPinEditText);
        confirmPinEditText = customView.findViewById(R.id.confirmPinEditText);
        changePinButton = customView.findViewById(R.id.changePinButton);

        changePinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String new_pin = newPinEditText.getText().toString();
                String conf_pin = confirmPinEditText.getText().toString();

                if(!(new_pin.equals(conf_pin)))
                {
                    Toast.makeText(mCon, "Pin dosn't match", Toast.LENGTH_SHORT).show();
                    return;
                }
                //mobileNo = otpEditText.getText().toString();
                Log.d("check", "onClick otpStr: " + otpStr);
                Log.d("check", "onClick mobileNo: " + mobileNo);
                SetMpinModel requestModel = new SetMpinModel(deviceIdStr, new_pin, mobileNoStr);
                Log.e("device id", deviceIdStr+"");
                Log.e("mobile no", mobileNoStr+"");
                setPin(requestModel,type);

            }
        });
    }

    private void validateOtp(String mobileNo, String deviceId, MaterialDialog dialog) {
        boolean isValidOtp = false;

        if (!TextUtils.isEmpty(otpStr) && !TextUtils.isEmpty(mobileNo)) {
            if (otpStr.length() < 4) {
                otpTextInput.setError(getResources().getString(R.string.enter_valid_otp));
            } else {
                otpTextInput.setError(null);
                isValidOtp = true;
            }
        } else {
            Log.d("check", "onClick empty: ");
            otpTextInput.setError("Enter OTP");
            isValidOtp = false;
        }

        if (isValidOtp) {
            VerifyOtpModel verifyOtpModel = new VerifyOtpModel(mobileNo, otpStr, deviceId);
            Log.d("check", "onClick: " + verifyOtpModel);
            verifyOtp(verifyOtpModel, dialog);
        }
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(60000, 1000) { // adjust the milli seconds here
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            public void onTick(long millisUntilFinished) {
                resendOtpTextView.setVisibility(View.GONE);
                otpTimerTextView.setVisibility(View.VISIBLE);
                otpTimerTextView.setText("" + String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500);

                otpTimerTextView.setVisibility(View.GONE);
                resendOtpTextView.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void verifyMobileNo(RequestModel requestModel, String deviceId) {
        Call<VerifyMobileNoResponseModel> call = ApiClient.getNetworkService().verifyMobileNo(requestModel);

      /*  final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                .content(R.string.loading)
                .canceledOnTouchOutside(false)
                .progress(true, 0)
                .widgetColorRes(R.color.colorPrimary)
                .show();*/

        call.enqueue(new Callback<VerifyMobileNoResponseModel>() {
            @Override
            public void onResponse(Call<VerifyMobileNoResponseModel> call, Response<VerifyMobileNoResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getVerifyMobileNoModels() != null &&
                            !response.body().getVerifyMobileNoModels().isEmpty()) {
                        String messageStr = response.body().getVerifyMobileNoModels().get(0).getMESSAGE();
                        if (messageStr != null && !messageStr.isEmpty()) {
                            if (messageStr.equalsIgnoreCase("otp send successfully")) {
                                already_exists=false;
                                TSTATUS=response.body().getVerifyMobileNoModels().get(0).getTSTATUS();
                                mobileNoStr=requestModel.getP_MobileNo();
                                Toast.makeText(mCon, "" + messageStr, Toast.LENGTH_SHORT).show();
                                binding.pinTextLayout.setVisibility(View.GONE);
                                binding.loginWithPinTextView.setVisibility(View.VISIBLE);
                                binding.loginWithOtpTextView.setVisibility(View.GONE);
                                openOtpDialog(requestModel.getP_MobileNo(), deviceId);
                                resendOtpTextView.setVisibility(View.GONE);
                                otpTimerTextView.setVisibility(View.VISIBLE);
                                startTimer();
                            } else if (messageStr.equalsIgnoreCase("already registered")) {
                                already_exists=true;
                                TSTATUS=response.body().getVerifyMobileNoModels().get(0).getTSTATUS();
                                mobileNoStr=requestModel.getP_MobileNo();
                                binding.pinTextLayout.setVisibility(View.VISIBLE);
                                binding.loginWithPinTextView.setVisibility(View.GONE);
                                binding.loginWithOtpTextView.setVisibility(View.VISIBLE);
                            }
                        } else {
                        //    Toast.makeText(mCon, R.string.no_data, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                     //   Toast.makeText(mCon, R.string.no_data, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mCon, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    Log.d("check", "onResponse verifyMobileNo: " + response.errorBody());
                }
              //  dialog.dismiss();
            }

            @Override
            public void onFailure(Call<VerifyMobileNoResponseModel> call, Throwable t) {
                if (!call.isCanceled()) {
                    Snackbar.make(binding.linearLayoutView,"Please check internet connection.",Snackbar.LENGTH_LONG).show();
                    //Toast.makeText(mCon, "Please check internet connection.", Toast.LENGTH_SHORT).show();
                    Log.e("check", "onFailure: " + t.getMessage());
                }
              //  dialog.dismiss();
            }
        });
    }

    private void requestOtp(RequestModel requestModel, String deviceId) {
        Call<RequestOtpResponseModel> call = ApiClient.getNetworkService().requestOtp(requestModel);

        final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                .content(R.string.loading)
                .canceledOnTouchOutside(false)
                .progress(true, 0)
                .widgetColorRes(R.color.colorPrimary)
                .show();

        call.enqueue(new Callback<RequestOtpResponseModel>() {
            @Override
            public void onResponse(Call<RequestOtpResponseModel> call, Response<RequestOtpResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getRequestOtpModels() != null
                            && !response.body().getRequestOtpModels().isEmpty()) {
                        String messageStr = response.body().getRequestOtpModels().get(0).getMESSAGE();
                        if (messageStr != null && !messageStr.isEmpty() &&
                                messageStr.equalsIgnoreCase("otp send successfully")) {
                            openOtpDialog(requestModel.getP_MobileNo(), deviceId);
                            resendOtpTextView.setVisibility(View.GONE);
                            otpTimerTextView.setVisibility(View.VISIBLE);
                            startTimer();
                        } else {
                            Toast.makeText(mCon, "" + messageStr, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mCon, R.string.no_data, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mCon, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    Log.d("check", "onResponse verifyMobileNo: " + response.errorBody());
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<RequestOtpResponseModel> call, Throwable t) {
                if (!call.isCanceled()) {
                    Snackbar.make(binding.linearLayoutView,"Please check internet connection.",Snackbar.LENGTH_LONG).show();
                    // Toast.makeText(mCon, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    Log.e("check", "onFailure requestOtp: " + t.getMessage());
                }
                dialog.dismiss();
            }
        });
    }

    private void verifyOtp(VerifyOtpModel otpModel, MaterialDialog otpDialog) {
        otpModel.setDeviceId(otpModel.getOtp());
        Call<LoginResponseModel> call = ApiClient.getNetworkService().verifyOtp(otpModel);

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
                    Log.d("otpData",""+response.body().toString());
                    if (response.body() != null && response.body().getLoginDetailsModels() != null
                            && !response.body().getLoginDetailsModels().isEmpty()) {
                        LoginDetailsModel loginModel = response.body().getLoginDetailsModels().get(0);
                        String messageStr = loginModel.getMESSAGE();
                        String tokenStr = loginModel.getTOKENNO();
                        String registrationStatusStr = loginModel.getRACTIVE();

                        if (messageStr != null && !messageStr.isEmpty() &&
                                messageStr.equalsIgnoreCase("login successfully")) {

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                            Log.d("checkT",loginModel.getTYPE());
                            PreferenceUtil.clearAll();
                            LoginDetailsModel model_login=new LoginDetailsModel(messageStr, tokenStr, registrationStatusStr,
                                    loginModel.getID(), loginModel.getNAME(), loginModel.getS_MOBILENO(),
                                    loginModel.getPANNO(), loginModel.getAADHARNO(), loginModel.getDLNO(),
                                    loginModel.getSTATECODE(), loginModel.getADDRESS(), loginModel.getPINCODE(),
                                    loginModel.getNO_OF_VEHICLES(), loginModel.getTYPE(), loginModel.getDeviceId(),
                                    loginModel.getP_MOBILENO(), loginModel.getPIN(),loginModel.getTSTATUS(),loginModel.getIMAGEUPLOAD());
                            model_login.setTSTATUS(TSTATUS);
                            PreferenceUtil.setUser(model_login);
                            PreferenceUtil.setUserLoggedIn(true);
                            PreferenceUtil.clearToken();
                            PreferenceUtil.clearRegistrationStatus();
                            PreferenceUtil.setTokenNo(tokenStr);
                            PreferenceUtil.setDeviceId(deviceIdStr);
                            PreferenceUtil.setRegistrationStatus(registrationStatusStr);
                            otpDialog.dismiss();
                          //  countDownTimer.cancel();
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("mobileNoStr", otpModel.getP_MobileNo()+"");
                            editor.commit();
                            if(already_exists==false) {
                                openPinDialog(mobileNoStr, deviceIdStr,loginModel.getTYPE());
                            }
                            else
                            {
                                if(TSTATUS.equalsIgnoreCase("Y")) {
                                    Intent intent = new Intent(mCon, NavigationActivity.class);
                                    intent.putExtra("mobileNo", otpModel.getP_MobileNo());
                                    startActivity(intent);
                                    finish();
                                }
                                else if(loginModel.getTYPE() == null||loginModel.getTYPE().equals("")){
                                    Intent intent = new Intent(mCon, RegistrationActivity.class);
                                    intent.putExtra("mobileNo", mobileNoStr);

                                    startActivity(intent);
                                }
                                else if(loginModel.getIMAGEUPLOAD().equals("N"))
                                    showDialogForKYC();
                                else
                                {
                                    Intent intent = new Intent(mCon, MainActivity.class);
                                    intent.putExtra("mobileNo", otpModel.getP_MobileNo());
                                    intent.putExtra("imageupload", model_login.getIMAGEUPLOAD());
                                    intent.putExtra("type", model_login.getTYPE());
                                    startActivity(intent);
                                    finish();
                                }
                            }

                        } else {
                            Toast.makeText(mCon, "" + messageStr, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mCon, R.string.no_data, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mCon, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    Log.d("check", "onResponse verifyOtp: " + response.errorBody());
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                if (!call.isCanceled()) {
                    Snackbar.make(binding.linearLayoutView,"Please check internet connection.",Snackbar.LENGTH_LONG).show();
                    // Toast.makeText(mCon, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    Log.e("check", "onFailure: " + t.getMessage());
                }
                dialog.dismiss();
            }
        });
    }

    private void verifyPin(SetMpinModel mpinModel) {
        final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                .content(R.string.loading)
                .canceledOnTouchOutside(false)
                .progress(true, 0)
                .widgetColorRes(R.color.colorPrimary)
                .show();
        final JSONObject request = new JSONObject();
        try {

            request.put("P_MobileNo",mpinModel.getP_MobileNo());
            request.put("PIN",mpinModel.getPIN());
            request.put("DeviceId",mpinModel.getDeviceId());

            Log.v("awbData",request.toString());

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }//

        AndroidNetworking.post("https://api.inland.in/Pilot/api/Driver/Verify_PIN")
                .addJSONObjectBody(request)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("idgpsupload",response.toString());
                        JSONArray data = null;
                        JSONObject  WHA1= null;
                        try {
                            data = response.getJSONArray("Table");

                            WHA1=data.getJSONObject(0);
                            //Log.v("city_data",WHA1.getString("CityName"));

                            if(WHA1.toString().contains("Pin not match"))
                                Toast.makeText(mCon, "Pin not match", Toast.LENGTH_SHORT).show();
                            else if (data.length() >0) {
                                LoginDetailsModel loginModel = new LoginDetailsModel(WHA1.getString("MESSAGE"),
                                        WHA1.getString("TOKENNO"),
                                        WHA1.getString("RACTIVE"),
                                        WHA1.getString("ID"),
                                        WHA1.getString("NAME"),
                                        WHA1.getString("S_MOBILENO"),
                                        WHA1.getString("PANNO"),
                                        WHA1.getString("AADHARNO"),
                                        WHA1.getString("DLNO"),
                                        WHA1.getString("STATECODE"),
                                        WHA1.getString("ADDRESS"),
                                        WHA1.getString("PINCODE"),
                                        WHA1.getString("NO_OF_VEHICLES"),
                                        WHA1.getString("TYPE"),
                                        WHA1.getString("DEVICEID"),
                                        WHA1.getString("P_MOBILENO"),
                                        WHA1.getString("PIN"),
                                        WHA1.getString("TSTATUS"),
                                        WHA1.getString("IMAGEUPLOAD"));
                                String messageStr = loginModel.getMESSAGE();
                                String tokenStr = loginModel.getTOKENNO();
                                String registrationStatusStr = loginModel.getRACTIVE();
                                Log.d("pintest",loginModel.toString());
                                //   Log.e("response ",registrationStatusStr+"");
                                //    Log.e("token",tokenStr);
                                //    Log.e("deviceId",deviceIdStr);
                                if (messageStr.equalsIgnoreCase("Login Successfully")) {
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
                                    TSTATUS= model_login.getTSTATUS();
                                    PreferenceUtil.setUser(model_login);
                                    PreferenceUtil.setUserLoggedIn(true);

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
                                        intent.putExtra("mobileNo", mobileNoStr);
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
                                    Toast.makeText(mCon, "1" + messageStr, Toast.LENGTH_SHORT).show();
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

                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                        dialog.dismiss();

                    }
                    //
                    @Override
                    public void onError(ANError anError) {
                        Log.e("ERROR", anError.toString());
                        dialog.dismiss();
                    }
                });
    }
    private void setPin(SetMpinModel mpinModel, String type) {

      //  Call<VerifyMobileNoResponseModel> call = ApiClient.getNetworkService().setPin(mpinModel);

            final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                    .content(R.string.loading)
                    .canceledOnTouchOutside(false)
                    .progress(true, 0)
                    .widgetColorRes(R.color.colorPrimary)
                    .show();
            final JSONObject request = new JSONObject();
            try {

                request.put("P_MobileNo",mpinModel.getP_MobileNo());
                request.put("PIN",mpinModel.getPIN());
                request.put("DeviceId",mpinModel.getDeviceId());

                Log.v("awbData",request.toString());

            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }//

            AndroidNetworking.post("https://api.inland.in/Pilot/api/Driver/Set_Pin")
                    .addJSONObjectBody(request)
                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("idgpsupload",response.toString());
                            JSONArray data = null;
                            JSONObject  WHA1= null;
                            try {
                                data = response.getJSONArray("Table");

                                WHA1=data.getJSONObject(0);
                                //Log.v("city_data",WHA1.getString("CityName"));


                                if (data.length() >0) {
                                    VerifyMobileNoModel loginModel = new VerifyMobileNoModel(WHA1.getString("MESSAGE"));
                                    PreferenceUtil.clearPin();
                                    LoginDetailsModel model_login=new LoginDetailsModel("", "", "",
                                            "", "", mobileNoStr, "", "", "",
                                            "", "", "", "", "",
                                            deviceIdStr, mobileNoStr, "","","");
                                    model_login.setTSTATUS(TSTATUS);
                                    PreferenceUtil.setUser(model_login);
                                    PreferenceUtil.setUserLoggedIn(true);
                                    PreferenceUtil.setPin(mpinModel.getPIN());

                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("mobileNoStr", mobileNoStr);
                                    editor.commit();

                                    Log.e("set pin ",PreferenceUtil.getPin());
                                    dialog_pin.dismiss();
                                     if(type == null|| type.equals("")){
                                        Intent intent = new Intent(mCon, RegistrationActivity.class);
                                        intent.putExtra("mobileNo", mobileNoStr);

                                        startActivity(intent);
                                    }else {
                                         Intent intent = new Intent(mCon, LoginActivity.class);
                                         intent.putExtra("mobileNo", mobileNoStr);
                                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                         startActivity(intent);
                                         finish();
                                     }
                                } else {
                                    Toast.makeText(mCon, "" + "something wrong", Toast.LENGTH_SHORT).show();
                                }
                             } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.d("check", "setMPin: " + e.getMessage());
                            }
                        }
                        //
                        @Override
                        public void onError(ANError anError) {
                            Log.e("ERROR", anError.toString());
                            dialog.dismiss();
                        }
                    });
         /*   call.enqueue(new Callback<VerifyMobileNoResponseModel>() {
                @Override
                public void onResponse(Call<VerifyMobileNoResponseModel> call, Response<VerifyMobileNoResponseModel> response) {
                    if (response.isSuccessful()) {
                        Log.d("pinRes", response.body().toString());
                        if (response.body() != null && response.body().getVerifyMobileNoModels() != null &&
                                !response.body().getVerifyMobileNoModels().isEmpty()) {
                            String messageStr = response.body().getVerifyMobileNoModels().get(0).getMESSAGE();
                            if (messageStr.equalsIgnoreCase("pin set successfully")) {
                                PreferenceUtil.clearPin();
                                LoginDetailsModel model_login=new LoginDetailsModel("", "", "",
                                        "", "", mobileNoStr, "", "", "",
                                        "", "", "", "", "",
                                        deviceIdStr, mobileNoStr, "","","");
                                model_login.setTSTATUS(TSTATUS);
                                PreferenceUtil.setUser(model_login);
                                PreferenceUtil.setUserLoggedIn(true);
                                PreferenceUtil.setPin(mpinModel.getPIN());

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("mobileNoStr", mobileNoStr);
                                editor.commit();

                                Log.e("set pin ",PreferenceUtil.getPin());
                                dialog_pin.dismiss();
                                Intent intent = new Intent(mCon, RegistrationActivity.class);
                                intent.putExtra("mobileNo", mobileNoStr);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(mCon, "" + messageStr, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mCon, R.string.no_data, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mCon, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                        Log.d("check", "onResponse setPin: " + response.errorBody());
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<VerifyMobileNoResponseModel> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Snackbar.make(binding.linearLayoutView,"Please check internet connection.",Snackbar.LENGTH_LONG).show();
                        //Toast.makeText(mCon, "Please check internet connection", Toast.LENGTH_SHORT).show();
                        Log.e("check", "onFailure: " + t.getMessage());
                    }
                    dialog.dismiss();
                }
            });*/

    }

    private void resendOtp(RequestModel requestModel, String deviceId) {
        Call<RequestOtpResponseModel> call = ApiClient.getNetworkService().resendOtp(requestModel);

        final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                .content(R.string.loading)
                .canceledOnTouchOutside(false)
                .progress(true, 0)
                .widgetColorRes(R.color.colorPrimary)
                .show();

        call.enqueue(new Callback<RequestOtpResponseModel>() {
            @Override
            public void onResponse(Call<RequestOtpResponseModel> call, Response<RequestOtpResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getRequestOtpModels() != null
                            && !response.body().getRequestOtpModels().isEmpty()) {
                        String messageStr = response.body().getRequestOtpModels().get(0).getMESSAGE();
                        if (messageStr != null && !messageStr.isEmpty() &&
                                messageStr.equalsIgnoreCase("otp send successfully")) {
                            Toast.makeText(mCon, "" + messageStr, Toast.LENGTH_SHORT).show();
                            openOtpDialog(requestModel.getP_MobileNo(), deviceId);
                            resendOtpTextView.setVisibility(View.GONE);
                            otpTimerTextView.setVisibility(View.VISIBLE);
                            startTimer();
                        } else {
                            Toast.makeText(mCon, "" + messageStr, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mCon, R.string.no_data, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mCon, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    Log.d("check", "onResponse resendOtp: " + response.errorBody());
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<RequestOtpResponseModel> call, Throwable t) {
                if (!call.isCanceled()) {
                    Snackbar.make(binding.linearLayoutView,"Please check internet connection.",Snackbar.LENGTH_LONG).show();
                    //Toast.makeText(mCon, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    Log.e("check", "onFailure resendOtp: " + t.getMessage());
                }
                dialog.dismiss();
            }
        });
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
                intent.putExtra("mobileNo", mobileNoStr);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }

    public void settings(View view) {
        Intent intent =new Intent(mCon, SettingsActivity.class);
        intent.putExtra("activity","Login");
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

    private void RequestLocationWithDisclosure()
    {
        Dialog dialog = new Dialog(mCon);
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
            }
        });
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                GetPermission();
            }
        });
    }
}
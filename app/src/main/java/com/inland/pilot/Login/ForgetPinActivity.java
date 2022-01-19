package com.inland.pilot.Login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.ActivityForgetPinBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPinActivity extends AppCompatActivity {
    private ActivityForgetPinBinding binding;
    private Context mCon;
    private TextInputLayout otpTextInput;
    private TextInputEditText otpEditText;
    private TextView resendOtpTextView, otpTimerTextView, cancelTextView, submitTextView;
    private String otpStr, mobileNoStr, deviceIdStr, newPinStr, confirmPinStr;
    private CountDownTimer countDownTimer;
    private MaterialDialog otpDialog;
    private List<LoginDetailsModel> loginDetailsModelList = new ArrayList<>();
    private LoginDetailsModel loginModel;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_pin);

        mCon = this;

        deviceIdStr = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("check", "onCreate: " + deviceIdStr);
        if (!deviceIdStr.isEmpty() && deviceIdStr != null) {
            PreferenceUtil.setDeviceId(deviceIdStr);
        }

        binding.loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mCon, com.inland.pilot.Login.LoginActivity.class));
                finish();
            }
        });

        binding.verifyOtpButton.setOnClickListener(new View.OnClickListener() {
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
                        requestOtp(otpModel, deviceIdStr);
                    }
                } else {
                    binding.mobileNoTextLayout.setError("" + getResources().getString(R.string.cannot_be_empty));
                }
            }
        });

        /*binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.forgetPinLinear.setVisibility(View.VISIBLE);
                //binding.verifyOtpLinear.setVisibility(View.GONE);
                binding.changePinLinear.setVisibility(View.GONE);
            }
        });

        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.forgetPinLinear.setVisibility(View.GONE);
                //binding.verifyOtpLinear.setVisibility(View.GONE);
                binding.changePinLinear.setVisibility(View.VISIBLE);
            }
        });*/

        binding.changePinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPinStr = binding.newPinEditText.getText().toString().trim();
                confirmPinStr = binding.confirmPinEditText.getText().toString().trim();
                validatePin(deviceIdStr, mobileNoStr);
            }
        });
    }

    private void validatePin(String deviceId, String mobileNoStr) {
        boolean isValidNewPassword = false, isValidConfirmPassword = false;

        if (TextUtils.isEmpty(newPinStr)) {
            binding.newPinTextLayout.setError(getResources().getString(R.string.cannot_be_empty));
        } else if (newPinStr.length() < 4) {
            binding.newPinTextLayout.setError(getResources().getString(R.string.enter_valid_pin));
        } else {
            binding.newPinTextLayout.setError(null);
            isValidNewPassword = true;
        }

        if (!TextUtils.isEmpty(confirmPinStr)) {
            if (!(confirmPinStr.length() < 4)) {
                if (newPinStr.equals(confirmPinStr)) {
                    binding.confirmPinTextLayout.setError(null);
                    isValidConfirmPassword = true;
                } else {
                    binding.confirmPinTextLayout.setError(getResources().getString(R.string.pin_did_not_match));
                }
            } else {
                binding.confirmPinTextLayout.setError(getResources().getString(R.string.enter_valid_pin));
            }
        } else {
            binding.confirmPinTextLayout.setError(getResources().getString(R.string.cannot_be_empty));
        }

        if (isValidNewPassword && isValidConfirmPassword) {
            com.inland.pilot.Login.SetMpinModel requestModel = new com.inland.pilot.Login.SetMpinModel(deviceId, newPinStr, mobileNoStr);
            Log.d("check", "validatePin loginModel: " + loginModel);
            setPin(requestModel, loginModel);
        }
    }

    private void openOtpDialog(String mobileNo, String androidDeviceId) {
        otpDialog = new MaterialDialog.Builder(mCon)
                .title("Verify mobile no.")
                .customView(R.layout.verify_otp_dialog, true)
                .canceledOnTouchOutside(false)
                .cancelable(false)
                /*.autoDismiss(false)*/
                .show();

        View customView = otpDialog.getCustomView();
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
                otpDialog.dismiss();
                RequestModel otpModel = new RequestModel(mobileNo);
                resendOtp(otpModel, androidDeviceId);
            }
        });

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.cancel();
                otpDialog.dismiss();
            }
        });

        submitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otpStr = otpEditText.getText().toString();
                Log.d("check", "onClick otpStr: " + otpStr);
                validateOtp(deviceIdStr, otpDialog);
            }
        });
    }

    private void validateOtp(String deviceId, MaterialDialog dialog) {
        boolean isValidOtp = false;

        if (!TextUtils.isEmpty(otpStr) && !TextUtils.isEmpty(mobileNoStr)) {
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
            com.inland.pilot.Login.VerifyOtpModel otpModel = new com.inland.pilot.Login.VerifyOtpModel(mobileNoStr, otpStr, deviceId);
            Log.d("check", "onClick: " + otpModel);
            verifyOtp(otpModel, dialog);
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

    private void requestOtp(RequestModel requestModel, String deviceId) {
        Call<com.inland.pilot.Login.RequestOtpResponseModel> call = ApiClient.getNetworkService().requestOtp(requestModel);

        final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                .content(R.string.loading)
                .canceledOnTouchOutside(false)
                .progress(true, 0)
                .widgetColorRes(R.color.colorPrimary)
                .show();

        call.enqueue(new Callback<com.inland.pilot.Login.RequestOtpResponseModel>() {
            @Override
            public void onResponse(Call<com.inland.pilot.Login.RequestOtpResponseModel> call, Response<com.inland.pilot.Login.RequestOtpResponseModel> response) {
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
                    Log.d("check", "onResponse requestOtp: " + response.errorBody());
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<com.inland.pilot.Login.RequestOtpResponseModel> call, Throwable t) {
                if (!call.isCanceled()) {
                    Toast.makeText(mCon, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    Log.d("check", "onFailure requestOtp: " + t.getMessage());
                }
                dialog.dismiss();
            }
        });
    }

    private void verifyMobileNo(RequestModel requestModel, String deviceId) {
        Call<com.inland.pilot.Login.VerifyMobileNoResponseModel> call = ApiClient.getNetworkService().verifyMobileNo(requestModel);

        final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                .content(R.string.loading)
                .canceledOnTouchOutside(false)
                .progress(true, 0)
                .widgetColorRes(R.color.colorPrimary)
                .show();

        call.enqueue(new Callback<com.inland.pilot.Login.VerifyMobileNoResponseModel>() {
            @Override
            public void onResponse(Call<com.inland.pilot.Login.VerifyMobileNoResponseModel> call, Response<com.inland.pilot.Login.VerifyMobileNoResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getVerifyMobileNoModels() != null &&
                            !response.body().getVerifyMobileNoModels().isEmpty()) {
                        String messageStr = response.body().getVerifyMobileNoModels().get(0).getMESSAGE();
                        if (messageStr.equalsIgnoreCase("otp send successfully")) {
                            Toast.makeText(mCon, "" + messageStr, Toast.LENGTH_SHORT).show();
                            openOtpDialog(requestModel.getP_MobileNo(), deviceId);
                            resendOtpTextView.setVisibility(View.GONE);
                            otpTimerTextView.setVisibility(View.VISIBLE);
                            startTimer();
                        } else if (messageStr.equalsIgnoreCase("already registered")) {

                        }
                    } else {
                        Toast.makeText(mCon, "" + response.errorBody(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mCon, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    Log.d("check", "onResponse verifyMobileNo: " + response.errorBody());
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<com.inland.pilot.Login.VerifyMobileNoResponseModel> call, Throwable t) {
                if (!call.isCanceled()) {
                    Toast.makeText(mCon, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    Log.d("check", "onFailure: " + t.getMessage());
                }
                dialog.dismiss();
            }
        });
    }

    private void verifyOtp(com.inland.pilot.Login.VerifyOtpModel otpModel, MaterialDialog otpDialog) {
        try {
            Call<com.inland.pilot.Login.LoginResponseModel> call = ApiClient.getNetworkService().verifyOtp(otpModel);

            final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                    .content(R.string.loading)
                    .canceledOnTouchOutside(false)
                    .progress(true, 0)
                    .widgetColorRes(R.color.colorPrimary)
                    .show();

            call.enqueue(new Callback<com.inland.pilot.Login.LoginResponseModel>() {
                @Override
                public void onResponse(Call<com.inland.pilot.Login.LoginResponseModel> call, Response<com.inland.pilot.Login.LoginResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getLoginDetailsModels() != null
                                && !response.body().getLoginDetailsModels().isEmpty()) {
                            loginModel = response.body().getLoginDetailsModels().get(0);
                            String messageStr = loginModel.getMESSAGE();
                            String tokenStr = loginModel.getTOKENNO();
                            String registrationStatusStr = loginModel.getRACTIVE();
                            if (messageStr != null && !messageStr.isEmpty() &&
                                    messageStr.equalsIgnoreCase("login successfully")) {
                                PreferenceUtil.clearToken();
                                PreferenceUtil.clearRegistrationStatus();
                                PreferenceUtil.setTokenNo(tokenStr);
                                PreferenceUtil.setRegistrationStatus(registrationStatusStr);
                                otpDialog.dismiss();
                                countDownTimer.cancel();
                                binding.forgetPinLinear.setVisibility(View.GONE);
                                binding.changePinLinear.setVisibility(View.VISIBLE);
                               /* Intent intent = new Intent(mCon, MainActivity.class);
                                startActivity(intent);
                                finish();*/
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
                public void onFailure(Call<com.inland.pilot.Login.LoginResponseModel> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Toast.makeText(mCon, "Please check internet connection", Toast.LENGTH_SHORT).show();
                        Log.d("check", "onFailure: " + t.getMessage());
                    }
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("check", "verifyOtp: " + e.getMessage());
        }
    }

    private void setPin(com.inland.pilot.Login.SetMpinModel mpinModel, LoginDetailsModel loginModel) {
        try {
            Call<com.inland.pilot.Login.VerifyMobileNoResponseModel> call = ApiClient.getNetworkService().setPin(mpinModel);

            final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                    .content(R.string.loading)
                    .canceledOnTouchOutside(false)
                    .progress(true, 0)
                    .widgetColorRes(R.color.colorPrimary)
                    .show();

            call.enqueue(new Callback<com.inland.pilot.Login.VerifyMobileNoResponseModel>() {
                @Override
                public void onResponse(Call<com.inland.pilot.Login.VerifyMobileNoResponseModel> call, Response<com.inland.pilot.Login.VerifyMobileNoResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getVerifyMobileNoModels() != null &&
                                !response.body().getVerifyMobileNoModels().isEmpty()) {
                            String messageStr = response.body().getVerifyMobileNoModels().get(0).getMESSAGE();
                            if (messageStr.equalsIgnoreCase("pin set successfully")) {
                                PreferenceUtil.clearAll();
                                PreferenceUtil.setUser(new LoginDetailsModel(loginModel.getMESSAGE(), loginModel.getTOKENNO(),
                                        loginModel.getRACTIVE(), loginModel.getID(), loginModel.getNAME(), loginModel.getS_MOBILENO(),
                                        loginModel.getPANNO(), loginModel.getAADHARNO(), loginModel.getDLNO(),
                                        loginModel.getSTATECODE(), loginModel.getADDRESS(), loginModel.getPINCODE(),
                                        loginModel.getNO_OF_VEHICLES(), loginModel.getTYPE(), loginModel.getDeviceId(),
                                        loginModel.getP_MOBILENO(), loginModel.getPIN(),loginModel.getTSTATUS(),loginModel.getIMAGEUPLOAD()));
                                PreferenceUtil.setUserLoggedIn(true);



                                    Intent intent = new Intent(mCon, com.inland.pilot.Login.LoginActivity.class);
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
                public void onFailure(Call<com.inland.pilot.Login.VerifyMobileNoResponseModel> call, Throwable t) {
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

    private void resendOtp(RequestModel requestModel, String deviceId) {
        Call<com.inland.pilot.Login.RequestOtpResponseModel> call = ApiClient.getNetworkService().resendOtp(requestModel);

        final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                .content(R.string.loading)
                .canceledOnTouchOutside(false)
                .progress(true, 0)
                .widgetColorRes(R.color.colorPrimary)
                .show();

        call.enqueue(new Callback<com.inland.pilot.Login.RequestOtpResponseModel>() {
            @Override
            public void onResponse(Call<com.inland.pilot.Login.RequestOtpResponseModel> call, Response<com.inland.pilot.Login.RequestOtpResponseModel> response) {
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
            public void onFailure(Call<com.inland.pilot.Login.RequestOtpResponseModel> call, Throwable t) {
                if (!call.isCanceled()) {
                    Toast.makeText(mCon, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    Log.d("check", "onFailure resendOtp: " + t.getMessage());
                }
                dialog.dismiss();
            }
        });
    }

    /*private void changePin(RequestModel requestModel) {
        try {
            Call<List<LoginDetailsModel>> call = ApiClient.getNetworkService().changePin(requestModel);

            final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                    .content(R.string.loading)
                    .canceledOnTouchOutside(false)
                    .progress(true, 0)
                    .widgetColorRes(R.color.colorPrimary)
                    .show();

            call.enqueue(new Callback<List<LoginDetailsModel>>() {
                @Override
                public void onResponse(Call<List<LoginDetailsModel>> call, Response<List<LoginDetailsModel>> response) {
                    if (response.isSuccessful()) {
                        PreferenceUtil.clearPin();
                        *//*PreferenceUtil.setUser(new LoginDetailsModel(deviceIdStr, "", mobileNoStr,
                                "", ""));*//*
                        PreferenceUtil.setPin(newPinStr);
                        Log.d("check", "onResponse: " + PreferenceUtil.getUser());
                        //PreferenceUtil.setUser(loginDetailsModelList.get(0));
                        Intent intent = new Intent(mCon, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        *//*if (response.body() != null) {
                            Toast.makeText(mCon, "PIN changed successfully", Toast.LENGTH_SHORT).show();
                            loginDetailsModelList = response.body();
                            if (loginDetailsModelList != null && !loginDetailsModelList.isEmpty()) {
                                PreferenceUtil.setUser(loginDetailsModelList.get(0));
                                Intent intent = new Intent(mCon, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(mCon, R.string.no_data + " 1 " + response.errorBody(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mCon, R.string.no_data + " 2 " + response.errorBody(), Toast.LENGTH_SHORT).show();
                        }*//*
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<List<LoginDetailsModel>> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Toast.makeText(mCon, "Please check internet connection", Toast.LENGTH_SHORT).show();
                        Log.d("check", "onFailure: " + t.getMessage());
                    }
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("check", "changePin: " + e.getMessage());
        }
    }*/
}
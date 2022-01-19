package com.inland.pilot.Profile;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.afollestad.materialdialogs.MaterialDialog;
import com.inland.pilot.Login.SetMpinModel;
import com.inland.pilot.Login.VerifyMobileNoResponseModel;
import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.FragmentChangePinBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePinActivity extends AppCompatActivity {
    private FragmentChangePinBinding binding;
    private Context mCon;
    private String deviceIdStr, newPinStr, confirmPinStr, mobileNoStr;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_change_pin);

        mCon = this;

        if (PreferenceUtil.getUser() != null) {
            if (PreferenceUtil.getUser().getP_MOBILENO() != null && !PreferenceUtil.getUser().getP_MOBILENO().isEmpty()) {
                mobileNoStr = PreferenceUtil.getUser().getP_MOBILENO();
            } else {
                mobileNoStr = "";
            }

            if (PreferenceUtil.getUser().getDeviceId() != null && !PreferenceUtil.getUser().getDeviceId().isEmpty()) {
                deviceIdStr = PreferenceUtil.getUser().getDeviceId();
                Log.d("check", "onCreate deviceIdStr: " + deviceIdStr);
            } else {
                deviceIdStr = Settings.Secure.getString(mCon.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }

        /*if (PreferenceUtil.getUser().getMobileNo() != null && !PreferenceUtil.getUser().getMobileNo().isEmpty()) {
            mobileNoStr = PreferenceUtil.getUser().getMobileNo();
            Log.d("check", "onCreate mobileNoStr: " + mobileNoStr);
        } else {
            mobileNoStr = "";
        }*/

        binding.changePinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPinStr = binding.newPinEditText.getText().toString().trim();
                confirmPinStr = binding.confirmPinEditText.getText().toString().trim();
                validatePin(deviceIdStr, mobileNoStr);
            }
        });


    }

    /*@SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_pin);


    }*/

    private void validatePin(String deviceId, String mobileNoStr) {
        boolean isValidNewPassword = false, isValidConfirmPassword = false, isValidMobileNo = false;

        if (mobileNoStr != null && !mobileNoStr.isEmpty()) {
            isValidMobileNo = true;
        } else {
            isValidMobileNo = false;
        }

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

        if (isValidNewPassword && isValidConfirmPassword && isValidMobileNo) {
            SetMpinModel requestModel = new SetMpinModel(deviceId, newPinStr, mobileNoStr);
            setPin(requestModel);
        }
    }

    private void setPin(SetMpinModel mpinModel) {
        try {
            Call<VerifyMobileNoResponseModel> call = ApiClient.getNetworkService().setPin(mpinModel);

            final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                    .content(R.string.loading)
                    .canceledOnTouchOutside(false)
                    .progress(true, 0)
                    .widgetColorRes(R.color.colorPrimary)
                    .show();

            call.enqueue(new Callback<VerifyMobileNoResponseModel>() {
                @Override
                public void onResponse(Call<VerifyMobileNoResponseModel> call, Response<VerifyMobileNoResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getVerifyMobileNoModels() != null &&
                                !response.body().getVerifyMobileNoModels().isEmpty()) {
                            String messageStr = response.body().getVerifyMobileNoModels().get(0).getMESSAGE();
                            if (messageStr.equalsIgnoreCase("pin set successfully")) {
                                PreferenceUtil.clearPin();
                                PreferenceUtil.setPin(mpinModel.getPIN());
                                Toast.makeText(mCon, "" + messageStr, Toast.LENGTH_SHORT).show();
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
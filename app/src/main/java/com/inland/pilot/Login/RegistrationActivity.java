package com.inland.pilot.Login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.inland.pilot.MainActivity;
import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.ActivityRegistrationBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {
    private ActivityRegistrationBinding binding;
    private Context mCon;
    private LinearLayout ownerLiner, driverLinear, bothLinear;
    private ArrayAdapter stateAdapter;
    private List<String> stateNameList = new ArrayList<>();
    private List<StateModel> stateModelList = new ArrayList<>();
    private String registrationTypeStr = "Owner", nameStr, mobileNoStr, panNoStr, aadharNoStr, addressStr,
            stateCodeStr, pinCodeStr, dlNoStr, noOfVehicleStr, otpStr, stateNameStr, deviceIdStr, regType, bankName, bankACNo, bankIFSC;
    private RegistrationModel registrationModel;
    private TextInputLayout otpTextInput;
    private TextInputEditText otpEditText;
    private TextView resendOtpTextView, otpTimerTextView, cancelTextView, submitTextView;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration);

        mCon = this;

        if (getIntent() != null) {
            if (getIntent().getStringExtra("mobileNo") != null &&
                    !getIntent().getStringExtra("mobileNo").isEmpty()) {

                mobileNoStr = getIntent().getStringExtra("mobileNo");
                binding.mobileNoEditText.setText(mobileNoStr);
            }
        }
        SharedPreferences preferences_shared = PreferenceManager.getDefaultSharedPreferences(this);
        if(!preferences_shared.getString("mobileNoStr","").equals(""))
        mobileNoStr = preferences_shared.getString("mobileNoStr","");

        binding.mobileNoEditText.setText(mobileNoStr);

        if (PreferenceUtil.getDeviceId() != null && !PreferenceUtil.getDeviceId().isEmpty()) {
            deviceIdStr = PreferenceUtil.getDeviceId();
            Log.d("check", "onCreate: " + deviceIdStr);
        }

        openTypeDialog();

        binding.stateTextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.stateSpinner.performClick();
            }
        });

        getStateList();

        binding.nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() != 0) {
                    binding.nameEditText.setError(null);
                    binding.nameTextLayout.setError(null);
                    binding.nameTextLayout.setErrorEnabled(false);
                } else {
                    //binding.nameEditText.setError(getString(R.string.enter_name));
                    Toast.makeText(mCon, "" + getResources().getString(R.string.enter_name), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.mobileNoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    binding.mobileNoEditText.setError(null);
                    binding.mobileNoTextLayout.setError(null);
                    binding.mobileNoTextLayout.setErrorEnabled(false);
                } else {
                    //binding.nameEditText.setError(getString(R.string.enter_name));
                    Toast.makeText(mCon, "" + getResources().getString(R.string.enter_mobile_no), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.panNoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    binding.panNoEditText.setError(null);
                    binding.panNoTextLayout.setError(null);
                    binding.panNoTextLayout.setErrorEnabled(false);
                } else {
                    //binding.nameEditText.setError(getString(R.string.enter_name));
                    Toast.makeText(mCon, "" + getResources().getString(R.string.enter_pan_no), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.aadharNoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    binding.aadharNoEditText.setError(null);
                    binding.aadharNoTextLayout.setError(null);
                    binding.aadharNoTextLayout.setErrorEnabled(false);
                } else {
                    //binding.nameEditText.setError(getString(R.string.enter_name));
                    Toast.makeText(mCon, "" + getResources().getString(R.string.enter_aadhar_no), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.addressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    binding.addressEditText.setError(null);
                    binding.addressTextLayout.setError(null);
                    binding.addressTextLayout.setErrorEnabled(false);
                } else {
                    //binding.nameEditText.setError(getString(R.string.enter_name));
                    Toast.makeText(mCon, "" + getResources().getString(R.string.enter_address), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.pinCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    binding.pinCodeEditText.setError(null);
                    binding.pinCodeTextLayout.setError(null);
                    binding.pinCodeTextLayout.setErrorEnabled(false);
                } else {
                    //binding.nameEditText.setError(getString(R.string.enter_name));
                    Toast.makeText(mCon, "" + getResources().getString(R.string.enter_pincode), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.dlNoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    binding.dlNoEditText.setError(null);
                    binding.dlNoTextLayout.setError(null);
                    binding.dlNoTextLayout.setErrorEnabled(false);
                } else {
                    //binding.nameEditText.setError(getString(R.string.enter_name));
                    Toast.makeText(mCon, "" + getResources().getString(R.string.enter_dl_no), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.bankNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    binding.bankNameEditText.setError(null);
                    binding.bankNameTextLayout.setError(null);
                    binding.bankNameTextLayout.setErrorEnabled(false);
                } else {
                    //binding.nameEditText.setError(getString(R.string.enter_name));
                    Toast.makeText(mCon, "" + getResources().getString(R.string.enter_bank_name), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.accountNoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    binding.accountNoEditText.setError(null);
                    binding.accountNoTextLayout.setError(null);
                    binding.accountNoTextLayout.setErrorEnabled(false);
                } else {
                    //binding.nameEditText.setError(getString(R.string.enter_name));
                    Toast.makeText(mCon, "" + getResources().getString(R.string.enter_account_no), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.ifscCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    binding.ifscCodeEditText.setError(null);
                    binding.ifscCodeTextLayout.setError(null);
                    binding.ifscCodeTextLayout.setErrorEnabled(false);
                } else {
                    //binding.nameEditText.setError(getString(R.string.enter_name));
                    Toast.makeText(mCon, "" + getResources().getString(R.string.enter_ifsc_code), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mCon, LoginActivity.class));
                finish();
            }
        });

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    nameStr = binding.nameEditText.getText().toString();
                    mobileNoStr = binding.mobileNoEditText.getText().toString();
                    panNoStr = binding.panNoEditText.getText().toString();
                    aadharNoStr = binding.aadharNoEditText.getText().toString();
                    addressStr = binding.addressEditText.getText().toString();
                    pinCodeStr = binding.pinCodeEditText.getText().toString();
                    dlNoStr = binding.dlNoEditText.getText().toString();
                    noOfVehicleStr = binding.noOfVehicleEditText.getText().toString();
                    bankName = binding.bankNameEditText.getText().toString();
                    bankACNo = binding.accountNoEditText.getText().toString();
                    bankIFSC = binding.ifscCodeEditText.getText().toString();

                    registrationTypeStr = binding.regTypeTextView.getText().toString();
                    validate(registrationTypeStr, deviceIdStr);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("check", "onClick: " + e.getMessage());
                }
            }
        });

        binding.registrationTypeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTypeDialog();
            }
        });
    }
    public boolean isValidPanCardNo(String panCardNo)
    {

        // Regex to check valid PAN Card number.
        String regex = "[A-Z]{5}[0-9]{4}[A-Z]{1}";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
        if (panCardNo == null)
        {
            return false;
        }

        Matcher m = p.matcher(panCardNo);
        return m.matches();
    }
    private void validate(String registrationType, String deviceId) {
        boolean isValidName = false, isValidMobileNo = false, isValidPanNo = true, isValidaAadharNo = true,
                isValidAddress = false, isValidPinCode = false, isValidDlNo = false, isValidNoOfVehicle = false,
                isValidStateCode = false, isValidBankName= false, isValidAcNo=false, isValidIfsc= false;

        if (TextUtils.isEmpty(nameStr)) {
            binding.nameTextLayout.setError(getResources().getString(R.string.enter_name));
        } else {
            binding.nameTextLayout.setError(null);
            binding.nameTextLayout.setErrorEnabled(false);
            isValidName = true;
        }

        if (mobileNoStr.length() > 0) {
            if (mobileNoStr.length() < 10) {
                binding.mobileNoTextLayout.setError("" + getResources().getString(R.string.valid_mobile_no));
            } else if (!mobileNoStr.startsWith("6") && !mobileNoStr.startsWith("7") &&
                    !mobileNoStr.startsWith("8") && !mobileNoStr.startsWith("9")) {
                binding.mobileNoTextLayout.setError("" + getResources().getString(R.string.valid_mobile_no));
            } else {
                isValidMobileNo = true;
                binding.mobileNoTextLayout.setError(null);
            }
        } else {
            binding.mobileNoTextLayout.setError("" + getResources().getString(R.string.cannot_be_empty));
        }

        /*if (TextUtils.isEmpty(mobileNoStr)) {
            binding.mobileNoTextLayout.setError(getResources().getString(R.string.enter_mobile_no));
            //Toast.makeText(mCon, "" + getResources().getString(R.string.enter_mobile_no), Toast.LENGTH_SHORT).show();
        } else {
            binding.mobileNoTextLayout.setError(null);
            binding.mobileNoTextLayout.setErrorEnabled(false);
            isValidMobileNo = true;
        }*/

        if (!TextUtils.isEmpty(panNoStr)) {
            //binding.panNoTextLayout.setError(getResources().getString(R.string.enter_pan_no));
            //Toast.makeText(mCon, "" + getResources().getString(R.string.enter_pan_no), Toast.LENGTH_SHORT).show();

            if(panNoStr.length()<10) {
                binding.panNoTextLayout.setError("Enter 10 digit valid PAN no.");
            }
            else
            {
                if(isValidPanCardNo(panNoStr)) {
                    binding.panNoTextLayout.setError(null);
                    binding.panNoTextLayout.setErrorEnabled(false);
                    isValidPanNo = true;
                }
                else
                {
                    binding.panNoTextLayout.setError("Enter 10 digit valid PAN no.");
                }
            }
        }

        if (!TextUtils.isEmpty(aadharNoStr)) {
            //binding.aadharNoTextLayout.setError(getResources().getString(R.string.enter_aadhar_no));
            //Toast.makeText(mCon, "" + getResources().getString(R.string.enter_aadhar_no), Toast.LENGTH_SHORT).show();

            if(aadharNoStr.length()<12) {
                binding.aadharNoTextLayout.setError("Enter 12 digit valid Adhar no.");
            }
            else
            {
                binding.aadharNoTextLayout.setError(null);
                binding.aadharNoTextLayout.setErrorEnabled(false);
                isValidaAadharNo = true;
            }
        }

        if (TextUtils.isEmpty(addressStr)) {
            binding.addressTextLayout.setError(getResources().getString(R.string.enter_address));
            //Toast.makeText(mCon, "" + getResources().getString(R.string.enter_address), Toast.LENGTH_SHORT).show();
        } else {
            binding.addressTextLayout.setError(null);
            binding.addressTextLayout.setErrorEnabled(false);
            isValidAddress = true;
        }

        if (TextUtils.isEmpty(pinCodeStr)) {
            binding.pinCodeTextLayout.setError(getResources().getString(R.string.enter_pincode));
            //Toast.makeText(mCon, "" + getResources().getString(R.string.enter_pincode), Toast.LENGTH_SHORT).show();
        } else {
            binding.pinCodeTextLayout.setError(null);
            binding.pinCodeTextLayout.setErrorEnabled(false);
            isValidPinCode = true;
        }

        if (TextUtils.isEmpty(stateCodeStr)) {
            Toast.makeText(mCon, "" + getResources().getString(R.string.select_state), Toast.LENGTH_SHORT).show();
        } else {
            isValidStateCode = true;
        }

        if (TextUtils.isEmpty(dlNoStr)) {
            binding.dlNoTextLayout.setError(getResources().getString(R.string.enter_dl_no));
            //Toast.makeText(mCon, "" + getResources().getString(R.string.enter_dl_no), Toast.LENGTH_SHORT).show();
        } else {
            binding.dlNoTextLayout.setError(null);
            binding.dlNoTextLayout.setErrorEnabled(false);
            isValidDlNo = true;
        }

        if (TextUtils.isEmpty(noOfVehicleStr)) {
            binding.noOfVehicleTextLayout.setError(getResources().getString(R.string.no_of_vehicle));
            //Toast.makeText(mCon, "" + getResources().getString(R.string.no_of_vehicle), Toast.LENGTH_SHORT).show();
        } else {
            binding.noOfVehicleTextLayout.setError(null);
            binding.noOfVehicleTextLayout.setErrorEnabled(false);
            isValidNoOfVehicle = true;
        }
        isValidBankName = true;
        isValidAcNo = true;
        isValidIfsc = true;

/*
        if (TextUtils.isEmpty(bankName)) {
            binding.bankNameTextLayout.setError(getResources().getString(R.string.enter_bank_name));
        } else {
            binding.bankNameTextLayout.setError(null);
            binding.bankNameTextLayout.setErrorEnabled(false);
            isValidBankName = true;
        }

        if (TextUtils.isEmpty(bankACNo)) {
            binding.accountNoTextLayout.setError(getResources().getString(R.string.enter_account_no));
        } else {
            binding.accountNoTextLayout.setError(null);
            binding.accountNoTextLayout.setErrorEnabled(false);
            isValidAcNo = true;
        }

        if (TextUtils.isEmpty(bankIFSC)) {
            binding.ifscCodeTextLayout.setError(getResources().getString(R.string.enter_ifsc_code));
        } else {
            binding.ifscCodeTextLayout.setError(null);
            binding.ifscCodeTextLayout.setErrorEnabled(false);
            isValidIfsc = true;
        }


 */
        if (registrationType.equalsIgnoreCase("owner")) {
            regType = "OW";
        } else if (registrationType.equalsIgnoreCase("driver cum owner")) {
            regType = "DO";
        } else if (registrationType.equalsIgnoreCase("driver")) {
            regType = "DM";
        }

        if (registrationType.equalsIgnoreCase("owner") || registrationType.equalsIgnoreCase("driver cum owner")) {
            if (isValidName && isValidMobileNo && isValidPanNo && isValidaAadharNo && isValidAddress &&
                    isValidPinCode && isValidDlNo && isValidNoOfVehicle && isValidStateCode) {
                registrationModel = new RegistrationModel(aadharNoStr, addressStr, dlNoStr, nameStr,
                        noOfVehicleStr, mobileNoStr, panNoStr, pinCodeStr, "", "",
                        stateCodeStr, regType, bankACNo, bankName, bankIFSC);
                registerUser(registrationModel, deviceId);
            }
        } else if (registrationType.equalsIgnoreCase("driver")) {
            if (isValidName && isValidMobileNo && isValidPanNo && isValidaAadharNo && isValidAddress &&
                    isValidPinCode && isValidDlNo && isValidStateCode) {
                registrationModel = new RegistrationModel(aadharNoStr, addressStr, dlNoStr, nameStr,
                        "", mobileNoStr, panNoStr, pinCodeStr, "", "",
                        stateCodeStr, regType, bankACNo, bankName, bankIFSC);
                registerUser(registrationModel, deviceId);
            }
        }
    }

    private void openTypeDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                .title("Registration Type")
                .customView(R.layout.register_type_dialog, true)
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .positiveText("Ok")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        registrationTypeStr = binding.regTypeTextView.getText().toString();
                        Log.d("check", "onClick typeStr: " + registrationTypeStr);
                        dialog.dismiss();
                    }
                })
                .show();

        View customView = dialog.getCustomView();
        ownerLiner = customView.findViewById(R.id.ownerLinear);
        driverLinear = customView.findViewById(R.id.driverLinear);
        bothLinear = customView.findViewById(R.id.bothLinear);

        ownerLiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.regTypeTextView.setText("Owner");
                binding.nameTextLayout.setHint(R.string.owner_name);
                binding.panNoTextLayout.setHint(R.string.owner_pan_no);
                binding.aadharNoTextLayout.setHint(R.string.owner_aadhar_no);
                binding.noOfVehicleTextLayout.setVisibility(View.VISIBLE);
                ownerLiner.setBackgroundColor(getResources().getColor(R.color.grey_200));
                driverLinear.setBackgroundColor(getResources().getColor(R.color.white));
                bothLinear.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        driverLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //registrationTypeStr = "driver";
                binding.regTypeTextView.setText("Driver");
                binding.nameTextLayout.setHint(R.string.driver_name);
                binding.panNoTextLayout.setHint(R.string.driver_pan_no);
                binding.aadharNoTextLayout.setHint(R.string.driver_aadhar_no);
                binding.noOfVehicleTextLayout.setVisibility(View.GONE);
                ownerLiner.setBackgroundColor(getResources().getColor(R.color.white));
                driverLinear.setBackgroundColor(getResources().getColor(R.color.grey_200));
                bothLinear.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        bothLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //registrationTypeStr = "both";
                binding.regTypeTextView.setText("Driver Cum Owner");
                binding.nameTextLayout.setHint("Name");
                binding.panNoTextLayout.setHint("PAN No.");
                binding.aadharNoTextLayout.setHint("Aadhar No.");
                binding.noOfVehicleTextLayout.setVisibility(View.VISIBLE);
                ownerLiner.setBackgroundColor(getResources().getColor(R.color.white));
                driverLinear.setBackgroundColor(getResources().getColor(R.color.white));
                bothLinear.setBackgroundColor(getResources().getColor(R.color.grey_200));
            }
        });
    }

    private void getStateList() {
        try {
            Call<StateListModel> call = ApiClient.getNetworkService().getStateList();

            call.enqueue(new Callback<StateListModel>() {
                @Override
                public void onResponse(Call<StateListModel> call, Response<StateListModel> response) {
                    Log.v("states",response.body().toString());
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getStateModelList() != null &&
                                !response.body().getStateModelList().isEmpty()) {
                            stateModelList = response.body().getStateModelList();

                            stateNameList.clear();

                            if (!stateModelList.isEmpty()) {
                                for (StateModel model : stateModelList) {
                                    if (model.getName() != null && !model.getName().isEmpty()) {
                                        stateNameList.add(model.getName());
                                    }
                                }

                                stateAdapter = new ArrayAdapter(mCon, android.R.layout.simple_spinner_item, stateNameList);
                                stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                binding.stateSpinner.setAdapter(stateAdapter);

                                binding.stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        stateNameStr = binding.stateSpinner.getSelectedItem().toString();

                                        if (stateNameStr != null && !stateNameStr.isEmpty()) {
                                            for (StateModel model : stateModelList) {
                                                if (model.getName() != null && !model.getName().isEmpty() &&
                                                        model.getName().equalsIgnoreCase(stateNameStr)) {
                                                    stateCodeStr = model.getCode();
                                                    Log.d("check", "onItemSelected: " + stateCodeStr);
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }

                        } else {
                            Toast.makeText(mCon, R.string.no_data, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mCon, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                        Log.d("check", "onResponse getStateList: " + mCon.getResources().getString(R.string.something_went_wrong));
                    }
                }

                @Override
                public void onFailure(Call<StateListModel> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Toast.makeText(mCon, "Please check internet connection", Toast.LENGTH_SHORT).show();
                        Log.d("check", "onFailure: " + t.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("check", "getStateList: " + e.getMessage());
        }
    }

    private void registerUser(RegistrationModel regModel, String deviceId) {
        try {
            Call<VerifyMobileNoResponseModel> call = ApiClient.getNetworkService().registerUser(regModel);

            call.enqueue(new Callback<VerifyMobileNoResponseModel>() {
                @Override
                public void onResponse(Call<VerifyMobileNoResponseModel> call, Response<VerifyMobileNoResponseModel> response) {
                    Log.v("register",call.request()+"");
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getVerifyMobileNoModels() != null &&
                                !response.body().getVerifyMobileNoModels().isEmpty()) {
                            String messageStr = response.body().getVerifyMobileNoModels().get(0).getMESSAGE().trim();
                            if (messageStr != null && !messageStr.isEmpty() &&
                                    messageStr.equalsIgnoreCase("Details Updated Successfully") ||
                                    messageStr.equalsIgnoreCase("details updated successfully")) {
                                Toast.makeText(mCon, "" + messageStr, Toast.LENGTH_SHORT).show();
                                String mpin=PreferenceUtil.getPin();
                                String token_no=PreferenceUtil.getTokenNo();
                                String device_id=PreferenceUtil.getDeviceId();
                                PreferenceUtil.clearAll();
                                PreferenceUtil.clearRegistrationStatus();
                                PreferenceUtil.setRegistrationStatus("Y");
                                PreferenceUtil.setPin(mpin);
                                PreferenceUtil.setTokenNo(token_no);
                                PreferenceUtil.setDeviceId(device_id);
                                LoginDetailsModel model_login=new LoginDetailsModel(messageStr, "", "Y",
                                        "", regModel.getName(), "",
                                        regModel.getPanNo(), regModel.getAadharNo(), regModel.getDLNo(),
                                        "", regModel.getAddress(), regModel.getPinCode(),
                                        regModel.getNo_Of_vehicles(), "", deviceId,
                                        regModel.getP_MobileNo(), "","","");
                                model_login.setTSTATUS("N");
                                PreferenceUtil.setUser(model_login);

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RegistrationActivity.this);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("mobileNoStr", mobileNoStr);
                                editor.commit();

                                PreferenceUtil.setUserLoggedIn(true);


                                    showDialogForKYC(mobileNoStr);

                               /* openOtpDialog(mobileNo, deviceId);
                                otpTimerTextView.setVisibility(View.VISIBLE);
                                resendOtpTextView.setVisibility(View.GONE);*/
                            } else {
                                Toast.makeText(mCon, "" + messageStr, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mCon, R.string.no_data, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mCon, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                        Log.d("check", "onResponse registerUser: " + mCon.getResources().getString(R.string.something_went_wrong));
                    }
                }

                @Override
                public void onFailure(Call<VerifyMobileNoResponseModel> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Toast.makeText(mCon, "Please check internet connection", Toast.LENGTH_SHORT).show();
                        Log.d("check", "onFailure registerUser: " + t.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("check", "registerUser: " + e.getMessage());
        }
    }

   /* private void openOtpDialog(String mobileNo, String androidDeviceId) {
        final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                .title("Verify mobile no.")
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
                RequestModel otpModel = new RequestModel(mobileNo);
                verifyMobileNo(otpModel, androidDeviceId);
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
                Log.d("check", "onClick otpStr: " + otpStr);
                validateOtp(deviceIdStr, dialog);
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
            VerifyOtpModel verifyOtpModel = new VerifyOtpModel(mobileNoStr, otpStr, deviceId);
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
            public void onFailure(Call<VerifyMobileNoResponseModel> call, Throwable t) {
                if (!call.isCanceled()) {
                    Toast.makeText(mCon, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    Log.d("check", "onFailure: " + t.getMessage());
                }
                dialog.dismiss();
            }
        });
    }

    private void verifyOtp(VerifyOtpModel otpModel, MaterialDialog otpDialog) {
        Call<VerifyOtpResponseModel> call = ApiClient.getNetworkService().verifyOtp(otpModel);

        final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                .content(R.string.loading)
                .canceledOnTouchOutside(false)
                .progress(true, 0)
                .widgetColorRes(R.color.colorPrimary)
                .show();

        call.enqueue(new Callback<VerifyOtpResponseModel>() {
            @Override
            public void onResponse(Call<VerifyOtpResponseModel> call, Response<VerifyOtpResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getVerifyOtpModels() != null
                            && !response.body().getVerifyOtpModels().isEmpty()) {
                        String messageStr = response.body().getVerifyOtpModels().get(0).getMESSAGE();
                        String tokenStr = response.body().getVerifyOtpModels().get(0).getToken();
                        String registrationStatusStr = response.body().getVerifyOtpModels().get(0).getRACTIVE();
                        if (messageStr != null && !messageStr.isEmpty() &&
                                messageStr.equalsIgnoreCase("login successfully")) {
                            PreferenceUtil.clearToken();
                            PreferenceUtil.clearRegistrationStatus();
                            PreferenceUtil.setTokenNo(tokenStr);
                            PreferenceUtil.setRegistrationStatus(registrationStatusStr);
                            otpDialog.dismiss();
                            countDownTimer.cancel();
                            Intent intent = new Intent(mCon, MainActivity.class);
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
                    Log.d("check", "onResponse verifyOtp: " + response.errorBody());
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<VerifyOtpResponseModel> call, Throwable t) {
                if (!call.isCanceled()) {
                    Toast.makeText(mCon, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    Log.d("check", "onFailure: " + t.getMessage());
                }
                dialog.dismiss();
            }
        });
    }*/

    private void showDialogForKYC(String mobileNoStr) {
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

}
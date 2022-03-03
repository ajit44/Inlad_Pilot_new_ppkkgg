package com.inland.pilot.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.inland.pilot.MainActivity;
import com.inland.pilot.NavigationActivity;
import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.ActivityModifyRegistrationBinding;
import com.inland.pilot.databinding.ActivityRegistrationBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyRegistrationActivity extends AppCompatActivity {
    private ActivityModifyRegistrationBinding binding;
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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_modify_registration);

        mCon = this;

        if (getIntent() != null) {
            if (getIntent().getStringExtra("mobileNo") != null &&
                    !getIntent().getStringExtra("mobileNo").isEmpty()) {

                mobileNoStr = getIntent().getStringExtra("mobileNo");
                binding.mobileNoEditText.setText(mobileNoStr);
            }
        }
        SharedPreferences preferences_shared = PreferenceManager.getDefaultSharedPreferences(this);
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

        RequestModel fetchModel = new RequestModel(mobileNoStr);

        fetchUser(fetchModel);
    }
    public boolean isValidPanCardNo(String panCardNo)
    {

        // Regex to check valid PAN Card number.
        String regex = "[A-Z]{5}[0-9]{4}[A-Z]{1}";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the PAN Card number
        // is empty return false
        if (panCardNo == null)
        {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given
        // PAN Card number using regular expression.
        Matcher m = p.matcher(panCardNo);

        // Return if the PAN Card number
        // matched the ReGex
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
                        noOfVehicleStr, mobileNoStr, panNoStr, pinCodeStr, "1", "",
                        stateCodeStr, regType, bankACNo, bankName, bankIFSC);
                registerUser(registrationModel, deviceId);
            }
        } else if (registrationType.equalsIgnoreCase("driver")) {
            if (isValidName && isValidMobileNo && isValidPanNo && isValidaAadharNo && isValidAddress &&
                    isValidPinCode && isValidDlNo && isValidStateCode) {
                registrationModel = new RegistrationModel(aadharNoStr, addressStr, dlNoStr, nameStr,
                        "", mobileNoStr, panNoStr, pinCodeStr, "1", "",
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
            Call<VerifyMobileNoResponseModel_Message> call = ApiClient.getNetworkService().registerUser_MESSAGE(regModel);
            Log.e("requesting",regModel.toString());
            call.enqueue(new Callback<VerifyMobileNoResponseModel_Message>() {
                @Override
                public void onResponse(Call<VerifyMobileNoResponseModel_Message> call, Response<VerifyMobileNoResponseModel_Message> response) {
                    Log.e("modifyReg",response.body().toString());
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getVerifyMobileNoModels() != null &&
                                !response.body().getVerifyMobileNoModels().isEmpty()) {
                            String messageStr = response.body().getVerifyMobileNoModels().get(0).getMESSAGE().trim();
                            if (messageStr != null && !messageStr.isEmpty() &&
                                    messageStr.equalsIgnoreCase("details save successfully") ||
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

                                PreferenceUtil.setUserLoggedIn(true);

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ModifyRegistrationActivity.this);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("mobileNoStr", mobileNoStr);
                                editor.commit();

                                finish();
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
                public void onFailure(Call<VerifyMobileNoResponseModel_Message> call, Throwable t) {
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

    private void fetchUser(RequestModel requestModel) {
        try {
            Call<FetchRegResponseModel> call = ApiClient.getNetworkService().fetchUser(requestModel);

            final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                    .content(R.string.loading)
                    .canceledOnTouchOutside(false)
                    .progress(true, 0)
                    .widgetColorRes(R.color.colorPrimary)
                    .show();

            call.enqueue(new Callback<FetchRegResponseModel>() {
                @Override
                public void onResponse(Call<FetchRegResponseModel> call, Response<FetchRegResponseModel> response) {

                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getRegDetailsModels() != null &&
                                !response.body().getRegDetailsModels().isEmpty()) {

                            FetchRegDetailsModel loginModel = response.body().getRegDetailsModels().get(0);
                            Log.e("tstaus",loginModel.toString());
                            binding.nameEditText.setText(loginModel.getName());
                            binding.mobileNoEditText.setText(loginModel.getP_MobileNo());
                            binding.panNoEditText.setText(loginModel.getPanNo());
                            binding.aadharNoEditText.setText(loginModel.getAadharNo());
                            binding.addressEditText.setText(loginModel.getAddress());
                            binding.pinCodeEditText.setText(loginModel.getPinCode());
                            binding.dlNoEditText.setText(loginModel.getDLNo());
                            binding.bankNameEditText.setText(loginModel.getBankname());
                            binding.accountNoEditText.setText(loginModel.getB_Accountno());
                            binding.ifscCodeEditText.setText(loginModel.getB_Ifsc());
                            binding.noOfVehicleEditText.setText(loginModel.getNo_Of_vehicles());

                            for(int i=0;i<stateNameList.size();i++)
                            {
                                if(stateNameList.get(i).equals(loginModel.getStateCode()))
                                {
                                    binding.stateSpinner.setSelection(i);
                                }
                            }
                            // String messageStr = loginModel.getMESSAGE();
                            //  String tokenStr = loginModel.getTOKENNO();

                            //   String registrationStatusStr = loginModel.getRACTIVE();
                            //   Log.e("response ",registrationStatusStr+"");
                            //  Log.e("token",tokenStr);
                            //  Log.e("deviceId",deviceIdStr);

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
                public void onFailure(Call<FetchRegResponseModel> call, Throwable t) {
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
            Log.d("check", "fetchuser: " + e.getMessage());
        }
    }
}
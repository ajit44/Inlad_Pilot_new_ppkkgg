package com.inland.pilot.VehicleMaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.inland.pilot.Login.VerifyMobileNoResponseModel;
import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.ActivityAddUpdateVehicleDetailsBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUpdateVehicleDetailsActivity extends AppCompatActivity {
    private ActivityAddUpdateVehicleDetailsBinding binding;
    private Context mCon;
    private String vehicleNoStr, vehicleTypeStr, modelNoStr, makeNoStr, chassisNoStr, engineNoStr,
            fitnessDateStr, registryDateStr, weightCapacityStr, rcBookNoStr, freeOutSpaceStr, vehicleSpaceStr,
            insuranceDetailsStr, insuranceValidityDateStr, rtNoStr, vehicleRegisterStateStr,
            vehicleIdStr, remarkStr, loginIdStr, actionTypeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_update_vehicle_details);

        mCon = this;

        if (PreferenceUtil.getUser() != null) {
            if (PreferenceUtil.getUser().getP_MOBILENO() != null && !PreferenceUtil.getUser().getP_MOBILENO().isEmpty()) {
                loginIdStr = PreferenceUtil.getUser().getP_MOBILENO();
            }
        }

        if (getIntent().getStringExtra("typeStr") != null && !getIntent().getStringExtra("typeStr").isEmpty()) {
            actionTypeStr = getIntent().getStringExtra("typeStr");
        }
        if (getIntent().getStringExtra("ID") != null && !getIntent().getStringExtra("ID").isEmpty()) {
            binding.vehicleIdEditText.setText(getIntent().getStringExtra("ID"));
        }
        if (getIntent().getStringExtra("VehicleNo") != null && !getIntent().getStringExtra("VehicleNo").isEmpty()) {
            binding.vehicleNoEditText.setText(getIntent().getStringExtra("VehicleNo"));
        }
        if (getIntent().getStringExtra("VehicleType") != null && !getIntent().getStringExtra("VehicleType").isEmpty()) {
            binding.vehicleTypeEditText.setText(getIntent().getStringExtra("VehicleType"));
        }
        if (getIntent().getStringExtra("Modelno") != null && !getIntent().getStringExtra("Modelno").isEmpty()) {
            binding.modelNoEditText.setText(getIntent().getStringExtra("Modelno"));
        }
        if (getIntent().getStringExtra("Make_no") != null && !getIntent().getStringExtra("Make_no").isEmpty()) {
            binding.makeNoEditText.setText(getIntent().getStringExtra("Make_no"));
        }
        if (getIntent().getStringExtra("Chassis_no") != null && !getIntent().getStringExtra("Chassis_no").isEmpty()) {
            binding.chassisNoEditText.setText(getIntent().getStringExtra("Chassis_no"));
        }
        if (getIntent().getStringExtra("Engine_no") != null && !getIntent().getStringExtra("Engine_no").isEmpty()) {
            binding.engineNoEditText.setText(getIntent().getStringExtra("Engine_no"));
        }
        if (getIntent().getStringExtra("Fitness_Date") != null && !getIntent().getStringExtra("Fitness_Date").isEmpty()) {
            binding.fitnessDateEditText.setText(getIntent().getStringExtra("Fitness_Date"));
        }
        if (getIntent().getStringExtra("Registry_data") != null && !getIntent().getStringExtra("Registry_data").isEmpty()) {
            binding.registryDateEditText.setText(getIntent().getStringExtra("Registry_data"));
        }
        if (getIntent().getStringExtra("Weight_capacity") != null && !getIntent().getStringExtra("Weight_capacity").isEmpty()) {
            binding.weightCapacityEditText.setText(getIntent().getStringExtra("Weight_capacity"));
        }
        if (getIntent().getStringExtra("RC_Book_no") != null && !getIntent().getStringExtra("RC_Book_no").isEmpty()) {
            binding.rcBookNoEditText.setText(getIntent().getStringExtra("RC_Book_no"));
        }
        if (getIntent().getStringExtra("Free_Out_Space") != null && !getIntent().getStringExtra("Free_Out_Space").isEmpty()) {
            binding.freeOutSpaceEditText.setText(getIntent().getStringExtra("Free_Out_Space"));
        }
        if (getIntent().getStringExtra("Vehicle_Space") != null && !getIntent().getStringExtra("Vehicle_Space").isEmpty()) {
            binding.vehicleSpaceEditText.setText(getIntent().getStringExtra("Vehicle_Space"));
        }
        if (getIntent().getStringExtra("Insurance_Details") != null && !getIntent().getStringExtra("Insurance_Details").isEmpty()) {
            binding.insuranceDetailsEditText.setText(getIntent().getStringExtra("Insurance_Details"));
        }
        if (getIntent().getStringExtra("Insurance_Valid_tilldate") != null && !getIntent().getStringExtra("Insurance_Valid_tilldate").isEmpty()) {
            binding.insuranceValidityDateEditText.setText(getIntent().getStringExtra("Insurance_Valid_tilldate"));
        }
        if (getIntent().getStringExtra("RT_No") != null && !getIntent().getStringExtra("RT_No").isEmpty()) {
            binding.rtNoEditText.setText(getIntent().getStringExtra("RT_No"));
        }
        if (getIntent().getStringExtra("Vehicle_register_State") != null && !getIntent().getStringExtra("Vehicle_register_State").isEmpty()) {
            binding.vehicleRegisterStateEditText.setText(getIntent().getStringExtra("Vehicle_register_State"));
        }
        if (getIntent().getStringExtra("Remark") != null && !getIntent().getStringExtra("Remark").isEmpty()) {
            binding.remarkEditText.setText(getIntent().getStringExtra("Remark"));
        }

        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date_fitness = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                binding.fitnessDateEditText.setText(sdf.format(myCalendar.getTime()));
            }

        };
        binding.fitnessDateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(mCon, date_fitness, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //---------------------------registry date ---------------
        DatePickerDialog.OnDateSetListener date_registry = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                binding.registryDateEditText.setText(sdf.format(myCalendar.getTime()));
            }

        };
        binding.registryDateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(mCon, date_registry, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //---------------------------insurance date ---------------
        DatePickerDialog.OnDateSetListener date_insurance = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                binding.insuranceValidityDateEditText.setText(sdf.format(myCalendar.getTime()));
            }

        };
        binding.insuranceValidityDateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(mCon, date_insurance, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        binding.addVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vehicleNoStr = binding.vehicleNoEditText.getText().toString();
                vehicleTypeStr = binding.vehicleTypeEditText.getText().toString();
                modelNoStr = binding.modelNoEditText.getText().toString();
                makeNoStr = binding.makeNoEditText.getText().toString();
                chassisNoStr = binding.chassisNoEditText.getText().toString();
                engineNoStr = binding.engineNoEditText.getText().toString();
                fitnessDateStr = binding.fitnessDateEditText.getText().toString();
                registryDateStr = binding.registryDateEditText.getText().toString();
                weightCapacityStr = binding.weightCapacityEditText.getText().toString();
                rcBookNoStr = binding.rcBookNoEditText.getText().toString();
                freeOutSpaceStr = binding.freeOutSpaceEditText.getText().toString();
                vehicleSpaceStr = binding.vehicleSpaceEditText.getText().toString();
                insuranceDetailsStr = binding.insuranceDetailsEditText.getText().toString();
                insuranceValidityDateStr = binding.insuranceValidityDateEditText.getText().toString();
                rtNoStr = binding.rtNoEditText.getText().toString();
                vehicleRegisterStateStr = binding.vehicleRegisterStateEditText.getText().toString();
                vehicleIdStr = binding.vehicleIdEditText.getText().toString();
                remarkStr = binding.remarkEditText.getText().toString();

                validate(actionTypeStr);
            }
        });
    }

    private void validate(String actionType) {
        boolean isValidVehicleNo = false, isValidVehicleType = false, isValidModelNo = false,
                isValidaMakeNo = false, isValidChassisNo = false, isValidEngineNo = false,
                isValidFitnessDate = false, isValidRegistryDate = false, isValidWeightCapacity = false,
                isValidRcBookNo = false, isValidFreeOutSpace = false, isValidVehicleSpace = false,
                isValidInsuranceDetails = false, isValidInsuranceValidityDate = false,
                isValidRtNo = false, isValidVehicleRegisterState = false, isValidVehicleId = false,
                isValidRemark = false;

        if (!TextUtils.isEmpty(vehicleNoStr)) {
            binding.vehicleNoTextLayout.setError(null);
            isValidVehicleNo = true;
        } else {
            binding.vehicleNoTextLayout.setError(getResources().getString(R.string.cannot_be_empty));
        }

        if (!TextUtils.isEmpty(vehicleTypeStr)) {
            binding.vehicleTypeTextLayout.setError(null);
            isValidVehicleType = true;
        } else {
            binding.vehicleTypeTextLayout.setError(getResources().getString(R.string.cannot_be_empty));
        }

        if (!TextUtils.isEmpty(modelNoStr)) {
            binding.modelNoTextLayout.setError(null);
            isValidModelNo = true;
        } else {
            binding.modelNoTextLayout.setError(getResources().getString(R.string.cannot_be_empty));
        }

        if (!TextUtils.isEmpty(makeNoStr)) {
            binding.makeNoTextLayout.setError(null);
            isValidaMakeNo = true;
        } else {
            binding.makeNoTextLayout.setError(getResources().getString(R.string.cannot_be_empty));
        }

        if (!TextUtils.isEmpty(chassisNoStr)) {
            binding.chassisNoTextLayout.setError(null);
            isValidChassisNo = true;
        } else {
            binding.chassisNoTextLayout.setError(getResources().getString(R.string.cannot_be_empty));
        }

        if (!TextUtils.isEmpty(engineNoStr)) {
            binding.engineNoTextLayout.setError(null);
            isValidEngineNo = true;
        } else {
            binding.engineNoTextLayout.setError(getResources().getString(R.string.cannot_be_empty));
        }

        if (!TextUtils.isEmpty(rcBookNoStr)) {
            binding.rcBookNoTextLayout.setError(null);
            isValidRcBookNo = true;
        } else {
            binding.rcBookNoTextLayout.setError(getResources().getString(R.string.cannot_be_empty));
        }

        if (isValidVehicleNo && isValidVehicleType && isValidModelNo && isValidaMakeNo && isValidChassisNo &&
                isValidEngineNo && isValidRcBookNo) {
            if (actionType != null && !actionType.isEmpty()) {
                if (actionType.equalsIgnoreCase("add")) {
                    VehicleMasterModel vehicleMasterModel = new VehicleMasterModel("1", vehicleNoStr, vehicleTypeStr,
                            modelNoStr, makeNoStr, chassisNoStr, engineNoStr, fitnessDateStr, registryDateStr,
                            weightCapacityStr, rcBookNoStr, freeOutSpaceStr, vehicleSpaceStr, insuranceDetailsStr,
                            insuranceValidityDateStr, rtNoStr, vehicleRegisterStateStr, remarkStr, vehicleIdStr,
                            loginIdStr);
                    addUpdateDetails(vehicleMasterModel, actionType);
                } else if (actionType.equalsIgnoreCase("update")) {
                    VehicleMasterModel vehicleMasterModel = new VehicleMasterModel("2", vehicleNoStr, vehicleTypeStr,
                            modelNoStr, makeNoStr, chassisNoStr, engineNoStr, fitnessDateStr, registryDateStr,
                            weightCapacityStr, rcBookNoStr, freeOutSpaceStr, vehicleSpaceStr, insuranceDetailsStr,
                            insuranceValidityDateStr, rtNoStr, vehicleRegisterStateStr, remarkStr, vehicleIdStr,
                            loginIdStr);
                    addUpdateDetails(vehicleMasterModel, actionType);
                }
            }
        }
    }

    private void addUpdateDetails(VehicleMasterModel vehicleMasterModel, String typeStr) {
        try {
            Call<VerifyMobileNoResponseModel> call = ApiClient.getNetworkService().addUpdateVehicleDetails(vehicleMasterModel);
            final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                    .content(R.string.loading)
                    .canceledOnTouchOutside(false)
                    .progress(true, 0)
                    .widgetColorRes(R.color.colorPrimary)
                    .show();
            call.enqueue(new Callback<VerifyMobileNoResponseModel>() {
                @Override
                public void onResponse(Call<VerifyMobileNoResponseModel> call, Response<VerifyMobileNoResponseModel> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getVerifyMobileNoModels() != null &&
                                !response.body().getVerifyMobileNoModels().isEmpty()) {
                            String messageStr = response.body().getVerifyMobileNoModels().get(0).getMESSAGE().trim();
                            if (messageStr.equalsIgnoreCase("success")) {
                                if (typeStr.equalsIgnoreCase("add")) {
                                    Toast.makeText(mCon, "Vehicle details added successfully", Toast.LENGTH_SHORT).show();
                                    PreferenceUtil.setVehicleNo(vehicleNoStr);
                                    PreferenceUtil.setRcNo(rcBookNoStr);
                                    PreferenceUtil.setInsNo(insuranceDetailsStr);
                                    PreferenceUtil.setPermit(rtNoStr);
                                } else if (typeStr.equalsIgnoreCase("update")) {
                                    Toast.makeText(mCon, "Vehicle details updated successfully", Toast.LENGTH_SHORT).show();
                                    PreferenceUtil.setVehicleNo(vehicleNoStr);
                                    PreferenceUtil.setRcNo(rcBookNoStr);
                                    PreferenceUtil.setInsNo(insuranceDetailsStr);
                                    PreferenceUtil.setPermit(rtNoStr);
                                }
                                Intent intent = new Intent(mCon, VehicleDocsUploadActivity.class);
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
                        Log.d("check", "onResponse addUpdateDetails: " + mCon.getResources().getString(R.string.something_went_wrong));
                    }
                }

                @Override
                public void onFailure(Call<VerifyMobileNoResponseModel> call, Throwable t) {
                    if (!call.isCanceled()) {
                        dialog.dismiss();
                        Toast.makeText(mCon, "Please check internet connection", Toast.LENGTH_SHORT).show();
                        Log.d("check", "onFailure addUpdateDetails: " + t.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("check", "addUpdateDetails: " + e.getMessage());
        }
    }
}
package com.inland.pilot.RouteMaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.inland.pilot.Login.StateListModel;
import com.inland.pilot.Login.StateModel;
import com.inland.pilot.Login.VerifyMobileNoResponseModel;
import com.inland.pilot.MyTrip.RequestActiveTripListModel;
import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.VehicleMaster.VehicleDocsUploadActivity;
import com.inland.pilot.VehicleMaster.VehicleMasterModel;
import com.inland.pilot.databinding.ActivityAddRouteBinding;
import com.inland.pilot.databinding.ActivityRegistrationBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRouteActivity extends AppCompatActivity {
    private ActivityAddRouteBinding binding;
    private Context mCon;
    private ArrayAdapter srcstateAdapter, dststateAdapter;
    private List<String> stateNameList = new ArrayList<>();
    private List<StateModel> stateModelList = new ArrayList<>();
    private String srcstateNameStr, dststateNameStr, srcstateIDStr, dststateIDStr;
    private String deviceIDStr;
    private String loginIdStr, tokenNoStr;
    private String routeId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_route);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_route);

        mCon = this;

        if (PreferenceUtil.getUser() != null) {
            if (PreferenceUtil.getUser().getP_MOBILENO() != null && !PreferenceUtil.getUser().getP_MOBILENO().isEmpty()) {
                loginIdStr = PreferenceUtil.getUser().getP_MOBILENO();
                tokenNoStr= PreferenceUtil.getTokenNo();
            }
        }
        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routeId=binding.routeIdEditText.getText().toString();
                routeId="1";
                /*
                if(routeId.length()<1)
                {
                    Toast.makeText(AddRouteActivity.this,"Enter Route Id",Toast.LENGTH_LONG).show();
                    binding.routeIdEditText.setError("Enter Route Id");
                    return;
                }

                 */
                deviceIDStr=PreferenceUtil.getDeviceId();
                Log.e("device id", deviceIDStr+"");
                RouteMasterModel model=new RouteMasterModel("insert",srcstateIDStr,srcstateNameStr,dststateIDStr,dststateNameStr, loginIdStr,routeId,deviceIDStr);
                addRouteDetails(model,"");
            }
        });
        getStateList();
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

                                srcstateAdapter = new ArrayAdapter(mCon, android.R.layout.simple_spinner_item, stateNameList);
                                srcstateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                binding.srcstateSpinner.setAdapter(srcstateAdapter);

                                binding.srcstateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        srcstateNameStr = binding.srcstateSpinner.getSelectedItem().toString();

                                        if (srcstateNameStr != null && !srcstateNameStr.isEmpty()) {
                                            for (StateModel model : stateModelList) {
                                                if (model.getName() != null && !model.getName().isEmpty() &&
                                                        model.getName().equalsIgnoreCase(srcstateNameStr)) {
                                                    srcstateIDStr = model.getCode();
                                                    Log.d("check", "onItemSelected: " + srcstateNameStr);
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                                //////////////////////////////////////
                                dststateAdapter = new ArrayAdapter(mCon, android.R.layout.simple_spinner_item, stateNameList);
                                dststateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                binding.dststateSpinner.setAdapter(dststateAdapter);
                                binding.dststateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        dststateNameStr = binding.dststateSpinner.getSelectedItem().toString();
                                        if (dststateNameStr != null && !dststateNameStr.isEmpty()) {
                                            for (StateModel model : stateModelList) {
                                                if (model.getName() != null && !model.getName().isEmpty() &&
                                                        model.getName().equalsIgnoreCase(dststateNameStr)) {
                                                    dststateIDStr = model.getCode();
                                                    Log.d("check", "dst onItemSelected: " + dststateNameStr);
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

    private void addRouteDetails(RouteMasterModel routeMasterModel, String typeStr) {
        try {
            Log.e("route",routeMasterModel.toString());
            Call<VerifyMobileNoResponseModel> call = ApiClient.getNetworkService().addUpdateRouteDetails(routeMasterModel);

            call.enqueue(new Callback<VerifyMobileNoResponseModel>() {
                @Override
                public void onResponse(Call<VerifyMobileNoResponseModel> call, Response<VerifyMobileNoResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getVerifyMobileNoModels() != null &&
                                !response.body().getVerifyMobileNoModels().isEmpty()) {
                            String messageStr = response.body().getVerifyMobileNoModels().get(0).getMESSAGE().trim();
                            Log.e("messagestr", response.body().getVerifyMobileNoModels().toString());

                            if (messageStr.equalsIgnoreCase("RECORD ADDED SUCCESSFULLY")) {
                                Toast.makeText(mCon, "RECORD ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
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
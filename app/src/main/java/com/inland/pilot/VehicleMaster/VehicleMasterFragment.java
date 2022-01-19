package com.inland.pilot.VehicleMaster;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.FragmentVehicleMasterBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleMasterFragment extends Fragment {
    private FragmentVehicleMasterBinding binding;
    private Context mCon;
    private VehicleListAdapter vehicleListAdapter;
    private List<VehicleMasterModel> vehicleMasterModelList = new ArrayList<>();
    private String loginIdStr;
    private RequestVehicleListModel vehicleListModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vehicle_master, container, false);

        mCon = getActivity();

        vehicleListAdapter = new VehicleListAdapter(mCon);

        binding.vehicleListRecycler.setHasFixedSize(true);
        binding.vehicleListRecycler.setLayoutManager(new LinearLayoutManager(mCon));
        binding.vehicleListRecycler.setItemAnimator(new DefaultItemAnimator());

        Log.d("check", "onCreateView user: " + PreferenceUtil.getUser());
        if (PreferenceUtil.getUser() != null) {
            if (PreferenceUtil.getUser().getP_MOBILENO() != null && !PreferenceUtil.getUser().getP_MOBILENO().isEmpty()) {
                loginIdStr = PreferenceUtil.getUser().getP_MOBILENO();
                vehicleListModel = new RequestVehicleListModel(loginIdStr);
                getVehicleList(vehicleListModel);
            }
        }

        binding.addVehicleFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, AddUpdateVehicleDetailsActivity.class);
                intent.putExtra("typeStr", "add");
                mCon.startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    private void getVehicleList(RequestVehicleListModel requestVehicleListModel) {
        try {
            Call<VehicleListModel> call = ApiClient.getNetworkService().getVehicleList(requestVehicleListModel);

            final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                    .title(R.string.loading)
                    .content(R.string.loading)
                    .canceledOnTouchOutside(false)
                    .progress(true, 0)
                    .widgetColorRes(R.color.colorPrimary)
                    .show();

            call.enqueue(new Callback<VehicleListModel>() {
                @Override
                public void onResponse(Call<VehicleListModel> call, Response<VehicleListModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getVehicleListModels() != null &&
                                !response.body().getVehicleListModels().isEmpty()) {
                            vehicleMasterModelList = response.body().getVehicleListModels();
                            vehicleListAdapter.addAll(vehicleMasterModelList);
                            binding.vehicleListRecycler.setVisibility(View.VISIBLE);
                            binding.errorTextView.setVisibility(View.GONE);
                            binding.vehicleListRecycler.setAdapter(vehicleListAdapter);
                        } else {
                            binding.vehicleListRecycler.setVisibility(View.GONE);
                            binding.errorTextView.setVisibility(View.VISIBLE);
                            binding.errorTextView.setText(mCon.getResources().getString(R.string.no_data));
                            Toast.makeText(mCon, " " + mCon.getResources().getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        binding.vehicleListRecycler.setVisibility(View.GONE);
                        binding.errorTextView.setVisibility(View.VISIBLE);
                        binding.errorTextView.setText(mCon.getResources().getString(R.string.something_went_wrong));
                        Toast.makeText(mCon, " " + mCon.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<VehicleListModel> call, Throwable t) {
                    if (!call.isCanceled()) {
                        binding.vehicleListRecycler.setVisibility(View.GONE);
                        binding.errorTextView.setVisibility(View.VISIBLE);
                        binding.errorTextView.setText(mCon.getResources().getString(R.string.check_internet));
                        Toast.makeText(mCon, " " + mCon.getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
                        Log.d("check", "onFailure: " + t.getMessage());
                    }
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("check", "getNotificationList: " + e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (vehicleListModel != null) {
            getVehicleList(vehicleListModel);
        }
    }
}
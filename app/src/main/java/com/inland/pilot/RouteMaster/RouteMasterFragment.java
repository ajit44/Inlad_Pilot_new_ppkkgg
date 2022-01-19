package com.inland.pilot.RouteMaster;

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

import com.inland.pilot.MyTrip.RequestActiveTripListModel;
import com.inland.pilot.MyTrip.TripListAdapter;
import com.inland.pilot.MyTrip.TripListModel;
import com.inland.pilot.MyTrip.TripMasterModel;
import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.VehicleMaster.AddUpdateVehicleDetailsActivity;
import com.inland.pilot.databinding.FragmentRouteMasterBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteMasterFragment extends Fragment {
    private FragmentRouteMasterBinding binding;
    private Context mCon;
    private List<RouteMasterModel> routeMasterModelList = new ArrayList<>();
    private RouteListAdapter routeListAdapter;
    private String loginIdStr;
    private String tokenNoStr;
    private String deviceIDStr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_route_master, container, false);

        mCon = getActivity();
        routeListAdapter = new RouteListAdapter(mCon);
        binding.routeListRecycler.setHasFixedSize(true);
        binding.routeListRecycler.setLayoutManager(new LinearLayoutManager(mCon));
        binding.routeListRecycler.setItemAnimator(new DefaultItemAnimator());
        if (PreferenceUtil.getUser() != null) {
            if (PreferenceUtil.getUser().getP_MOBILENO() != null && !PreferenceUtil.getUser().getP_MOBILENO().isEmpty()) {
                loginIdStr = PreferenceUtil.getUser().getP_MOBILENO();
                tokenNoStr= PreferenceUtil.getTokenNo();
            }
        }
        binding.addRouteFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, AddRouteActivity.class);
                mCon.startActivity(intent);
            }
        });
        deviceIDStr=PreferenceUtil.getDeviceId();
        RouteMasterModel model=new RouteMasterModel("","","","","", loginIdStr,"",deviceIDStr);
        getRouteList(model);

        return binding.getRoot();
    }
    private void getRouteList(RouteMasterModel requestRouteListModel) {
        try {
            Call<RouteListModel> call = ApiClient.getNetworkService().getRouteListAll(requestRouteListModel);

            final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                    .title(R.string.loading)
                    .content(R.string.loading)
                    .canceledOnTouchOutside(false)
                    .progress(true, 0)
                    .widgetColorRes(R.color.colorPrimary)
                    .show();

            call.enqueue(new Callback<RouteListModel>() {
                @Override
                public void onResponse(Call<RouteListModel> call, Response<RouteListModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getRouteListModels() != null &&
                                !response.body().getRouteListModels().isEmpty()) {

                            routeMasterModelList = response.body().getRouteListModels();
                            Log.e("model",routeMasterModelList.toString());
                            routeListAdapter.addAll(routeMasterModelList);
                            binding.routeListRecycler.setVisibility(View.VISIBLE);
                            binding.errorTextView.setVisibility(View.GONE);
                            binding.routeListRecycler.setAdapter(routeListAdapter);
                        } else {
                            binding.routeListRecycler.setVisibility(View.GONE);
                            binding.errorTextView.setVisibility(View.VISIBLE);
                            Toast.makeText(mCon, "No Route found !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        binding.routeListRecycler.setVisibility(View.GONE);
                        binding.errorTextView.setVisibility(View.VISIBLE);
                        Toast.makeText(mCon, " " + mCon.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<RouteListModel> call, Throwable t) {
                    if (!call.isCanceled()) {
                        binding.routeListRecycler.setVisibility(View.GONE);
                        binding.errorTextView.setVisibility(View.VISIBLE);
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
        if(routeListAdapter!=null) {
            RouteMasterModel model = new RouteMasterModel("", "", "", "", "", loginIdStr, "", deviceIDStr);
            getRouteList(model);
        }
    }
}
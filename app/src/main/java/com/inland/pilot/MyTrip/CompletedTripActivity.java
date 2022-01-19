package com.inland.pilot.MyTrip;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.ActivityOnGoingTripBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedTripActivity extends AppCompatActivity {
    private ActivityOnGoingTripBinding binding;
    private Context mCon;
    private TripCompletedListAdapter tripListAdapter;
    private List<TripMasterModel> tripMasterModelList = new ArrayList<>();
    private String loginIdStr;
    private RequestTripListModel tripListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_going_trip);

        mCon = this;
        tripListAdapter = new TripCompletedListAdapter(mCon);
        binding.tripListRecycler.setHasFixedSize(true);
        binding.tripListRecycler.setLayoutManager(new LinearLayoutManager(mCon));
        binding.tripListRecycler.setItemAnimator(new DefaultItemAnimator());

        if (PreferenceUtil.getUser() != null) {
            if (PreferenceUtil.getUser().getP_MOBILENO() != null && !PreferenceUtil.getUser().getP_MOBILENO().isEmpty()) {
                loginIdStr = PreferenceUtil.getUser().getP_MOBILENO();
                tripListModel = new RequestTripListModel("2",loginIdStr);
                getTripList(tripListModel);
            }
        }
    }
    private void getTripList(RequestTripListModel requestTripListModel) {
        try {
            Call<TripListModel> call = ApiClient.getNetworkService().getTripListAll(requestTripListModel);

            final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                    .title(R.string.loading)
                    .content(R.string.loading)
                    .canceledOnTouchOutside(false)
                    .progress(true, 0)
                    .widgetColorRes(R.color.colorPrimary)
                    .show();

            call.enqueue(new Callback<TripListModel>() {
                @Override
                public void onResponse(Call<TripListModel> call, Response<TripListModel> response) {
                    if (response.isSuccessful()) {
                        Log.d("completeCheck",response.body().getTripListModels().toString());
                        if (response.body() != null && response.body().getTripListModels() != null &&
                                !response.body().getTripListModels().isEmpty()) {
                            tripMasterModelList = response.body().getTripListModels();
                            tripListAdapter.addAll(tripMasterModelList);
                            binding.tripListRecycler.setVisibility(View.VISIBLE);
                            binding.errorTextView.setVisibility(View.GONE);
                            binding.tripListRecycler.setAdapter(tripListAdapter);
                        } else {
                            binding.tripListRecycler.setVisibility(View.GONE);
                            binding.errorTextView.setVisibility(View.VISIBLE);
                       //     Toast.makeText(mCon, "No Completed trip found !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        binding.tripListRecycler.setVisibility(View.GONE);
                        binding.errorTextView.setVisibility(View.VISIBLE);
                        Toast.makeText(mCon, " " + mCon.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<TripListModel> call, Throwable t) {
                    if (!call.isCanceled()) {
                        binding.tripListRecycler.setVisibility(View.GONE);
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
        if (tripListModel != null) {
            getTripList(tripListModel);
        }
    }
}
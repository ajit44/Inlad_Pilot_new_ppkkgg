package com.inland.pilot.MyTrip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.view.View;

import com.inland.pilot.Location.LocationAlarmScheduler;
import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.Restarter;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.VehicleMaster.RequestVehicleListModel;
import com.inland.pilot.VehicleMaster.VehicleListAdapter;
import com.inland.pilot.VehicleMaster.VehicleListModel;
import com.inland.pilot.VehicleMaster.VehicleMasterModel;
import com.inland.pilot.databinding.FragmentTripDetailsBinding;
import com.inland.pilot.databinding.FragmentVehicleMasterBinding;

public class TripDetailsActivity extends AppCompatActivity {

    private FragmentTripDetailsBinding binding;
    private Context mCon;
    private String loginIdStr;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String tokenNoStr;


    // TODO: Rename and change types and number of parameters

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_trip_details);

        mCon = this;

        if (PreferenceUtil.getUser() != null) {
            if (PreferenceUtil.getUser().getP_MOBILENO() != null && !PreferenceUtil.getUser().getP_MOBILENO().isEmpty()) {
                loginIdStr = PreferenceUtil.getUser().getP_MOBILENO();
                tokenNoStr= PreferenceUtil.getTokenNo();
            }
        }

        binding.ongoingTripCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mCon, OnGoingTripActivity.class);
                startActivity(intent);
            }
        });

        binding.completedTripCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mCon, CompletedTripActivity.class);
                startActivity(intent);
            }
        });

        binding.upcomingTripCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mCon, UpcomingTripActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
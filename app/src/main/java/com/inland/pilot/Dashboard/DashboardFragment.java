package com.inland.pilot.Dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.inland.pilot.LoadImage.LoadUploadListActivity;
import com.inland.pilot.LoadIntemationNew.LoadStateListActivity;
import com.inland.pilot.Location.LocationActivity;
import com.inland.pilot.Login.RegistrationActivity;
import com.inland.pilot.MyTrip.OnGoingTripActivity;
import com.inland.pilot.MyTrip.ReachFinalTripListActivity;
import com.inland.pilot.MyTrip.TripDetailsActivity;
import com.inland.pilot.OriginOfLoads.OriginOfLoadsActivity;
import com.inland.pilot.R;
import com.inland.pilot.SettingsActivity;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.FragmentDashboardBinding;
import com.inland.pilot.pod.PodListActivity;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private Context mCon;
    private String registrationStatus, mobileNoStr, userNameStr;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);

        mCon = getActivity();

        PackageInfo pInfo = null;
        try {
            pInfo = mCon.getPackageManager().getPackageInfo(mCon.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        binding.versionCode.setText(" "+pInfo.versionCode);
        if (PreferenceUtil.getUser() != null) {
            if (PreferenceUtil.getUser().getP_MOBILENO() != null && !PreferenceUtil.getUser().getP_MOBILENO().isEmpty()) {
                mobileNoStr = PreferenceUtil.getUser().getP_MOBILENO();
            } else {
                mobileNoStr = "";
            }

            if (PreferenceUtil.getUser().getNAME() != null && !PreferenceUtil.getUser().getNAME().isEmpty()) {
                userNameStr = PreferenceUtil.getUser().getNAME();
                binding.userNameTextView.setText(userNameStr);
            } else {
                binding.userNameTextView.setText("----");
            }

            if (PreferenceUtil.getUser().getRACTIVE() != null && !PreferenceUtil.getUser().getRACTIVE().isEmpty()
                    && !PreferenceUtil.getUser().getRACTIVE().equalsIgnoreCase(" ")) {
                //dashboardStatusStr = PreferenceUtil.getDashboardStatus();
                registrationStatus = PreferenceUtil.getUser().getRACTIVE();
                if (registrationStatus.equalsIgnoreCase("y")) {
                    binding.dashboardConstraint.setVisibility(View.VISIBLE);
                    binding.userActivationTextView.setVisibility(View.GONE);
                } else {
                    binding.userActivationTextView.setVisibility(View.VISIBLE);
                    binding.dashboardConstraint.setVisibility(View.GONE);
                }
            } else {
                Intent intent = new Intent(mCon, RegistrationActivity.class);
                intent.putExtra("mobileNo", mobileNoStr);
                startActivity(intent);
                binding.userActivationTextView.setVisibility(View.VISIBLE);
                binding.dashboardConstraint.setVisibility(View.GONE);
            }
        }



        binding.loadingAvailabilityCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(mCon, LoadStateListActivity.class));
            }
        });

        binding.getTripCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mCon, TripDetailsActivity.class));
            }
        });

        binding.loadImageCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mCon, OnGoingTripActivity.class));
            }
        });

        binding.unloadingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mCon, ReachFinalTripListActivity.class));
            }
        });

        binding.podCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mCon, PodListActivity.class));
            }
        });
        binding.fcmTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        binding.SettingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(mCon, SettingsActivity.class);
                intent.putExtra("activity","Nav");
                startActivity(intent);
                getActivity().finish();
            }
        });
        return binding.getRoot();
    }

}
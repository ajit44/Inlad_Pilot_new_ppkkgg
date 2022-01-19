package com.inland.pilot.VehicleMaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;

import com.inland.pilot.Login.LoginActivity;
import com.inland.pilot.Profile.AdharCapture;
import com.inland.pilot.Profile.DlActivity;
import com.inland.pilot.Profile.PanActivity;
import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.ActivityAddUpdateVehicleDetailsBinding;
import com.inland.pilot.databinding.ActivityRcCaptureBinding;
import com.inland.pilot.databinding.ActivityVehicleDocsUploadBinding;

public class VehicleDocsUploadActivity extends AppCompatActivity {
    private ActivityVehicleDocsUploadBinding binding;
    private Context mCon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vehicle_docs_upload);
        mCon = this;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        SharedPreferences preferences_shared = PreferenceManager.getDefaultSharedPreferences(this);
        boolean is_veh_upload = preferences_shared.getBoolean("is_veh_upload",false);
        if(is_veh_upload)
        {
            binding.VehicleText.setText("Upload Vehicle Photo (Done)");
        }
        boolean is_rc_upload = preferences_shared.getBoolean("is_rc_upload",false);
        if(is_rc_upload)
        {
            binding.RCText.setText("Upload Registration Certificate (RC) Docs (Done)");
        }
        boolean is_ins_upload = preferences_shared.getBoolean("is_ins_upload",false);
        if(is_ins_upload)
        {
            binding.InsuranceText.setText("Upload Vehicle Insurance Docs (Done)");
        }

        boolean is_permit_upload = preferences_shared.getBoolean("is_permit_upload",false);
        if(is_permit_upload)
        {
            binding.STText.setText("Upload Transport Permit Docs (Done)");
        }

        binding.VehicleLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, VehicleCapture.class);
                startActivity(intent);
            }
        });

        binding.RCLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, RcCapture.class);
                startActivity(intent);
            }
        });

        binding.insLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, InsuranceCapture.class);
                startActivity(intent);
            }
        });

        binding.StateTransportLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, PermitCapture.class);
                startActivity(intent);
            }
        });

        binding.saveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
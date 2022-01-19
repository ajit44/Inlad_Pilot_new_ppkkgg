package com.inland.pilot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import com.inland.pilot.Location.LocationActivity;
import com.inland.pilot.Login.PinLoginActivity;
import com.inland.pilot.Login.RegistrationActivity;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.ActivityMainBinding;
import com.inland.pilot.Profile.ProfileActivity;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Context mCon;
    private String registrationStatus, mobileNoStr;
    String KEY_GO_FOR_KYC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mCon = this;


        if (PreferenceUtil.getUser() != null) {
            if (PreferenceUtil.getUser().getP_MOBILENO() != null && !PreferenceUtil.getUser().getP_MOBILENO().isEmpty()) {
                mobileNoStr = PreferenceUtil.getUser().getP_MOBILENO();
            } else {
                mobileNoStr = "";
            }
        }

        Log.d("check"," in main "+mobileNoStr);

        //...go for kyc
        KEY_GO_FOR_KYC = getIntent().getStringExtra("goForKYC");
        if(KEY_GO_FOR_KYC !=null)
        {
            Intent intent = new Intent(mCon, ProfileActivity.class);
            intent.putExtra("mobileNo", mobileNoStr);
            startActivity(intent);
        }
        //snack bar...
        CoordinatorLayout cbLayout = findViewById(R.id.coordinatorLayout);
        Snackbar.make(cbLayout,"User is not activated yet, please complete your KYC.", Snackbar.LENGTH_INDEFINITE)
                .setAction("UPDATE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mCon, ProfileActivity.class);
                        intent.putExtra("mobileNo", mobileNoStr);
                        startActivity(intent);
                    }
                }).show();

        if (PreferenceUtil.getRegistrationStatus() != null && !PreferenceUtil.getRegistrationStatus().isEmpty()
                && !PreferenceUtil.getRegistrationStatus().equalsIgnoreCase(" ")) {
            //dashboardStatusStr = PreferenceUtil.getDashboardStatus();
            registrationStatus = PreferenceUtil.getRegistrationStatus();
            binding.userActivationTextView.setVisibility(View.GONE);
            /*
            if (registrationStatus.equalsIgnoreCase("y")) {
                binding.userActivationTextView.setVisibility(View.VISIBLE);
            } else {
                binding.userActivationTextView.setVisibility(View.GONE);
                //binding.dashboardLinear.setVisibility(View.VISIBLE);
            }

             */
        } else {


            Intent intent = new Intent(mCon, RegistrationActivity.class);
            intent.putExtra("mobileNo", mobileNoStr);
            startActivity(intent);
            binding.userActivationTextView.setVisibility(View.VISIBLE);
            binding.dashboardLinear.setVisibility(View.GONE);
        }

        binding.loadingAvaibilityCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mCon, "User is not activated yet, please wait for approval.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.placementCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mCon, "User is not activated yet, please wait for approval.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.loadImageCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mCon, "User is not activated yet, please wait for approval.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.trackingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mCon, "User is not activated yet, please wait for approval.", Toast.LENGTH_SHORT).show();
            }
        });
        binding.unloadingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mCon, "User is not activated yet, please wait for approval.", Toast.LENGTH_SHORT).show();
            }
        });
        binding.podCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mCon, "User is not activated yet, please wait for approval.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        /*if (id == R.id.menu_logout) {
            new MaterialDialog.Builder(mCon)
                    .title("Logout")
                    .content("Are you sure, you want to logout?")
                    .positiveText("Yes")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            //PreferenceUtil.setUserLoggedIn(false);
                            //PreferenceUtil.clearAll();
                            Intent intent = new Intent(mCon, PinLoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .negativeText("Cancel")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else*/
        if (id == R.id.menu_profile) {
            Intent intent = new Intent(mCon, ProfileActivity.class);
            intent.putExtra("mobileNo", mobileNoStr);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
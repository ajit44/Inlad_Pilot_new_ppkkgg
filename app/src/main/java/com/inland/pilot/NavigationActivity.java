package com.inland.pilot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.inland.pilot.Location.DBHelper;
import com.inland.pilot.Location.ExampleJobService;
import com.inland.pilot.Location.LocationAlarmScheduler;
import com.inland.pilot.Login.LoginActivity;
import com.inland.pilot.Login.PinLoginActivity;
import com.inland.pilot.Login.RegistrationActivity;
import com.inland.pilot.Notification.NotificationActivity;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.VehicleMaster.RequestVehicleListModel;
import com.inland.pilot.databinding.ActivityNavigationBinding;

import java.util.Locale;


public class NavigationActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigationBinding binding;
    private Context mCon;
    private String userNameStr;

    private Intent mServiceIntent;
    protected LocationManager locationManager;
    boolean isGPSEnabled = false;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationRequest locationRequest;

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(NavigationActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Click on enable to start GPS");

        // On pressing Settings button
        alertDialog.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                NavigationActivity.this.startActivity(intent);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    public interface onGpsListener {
        void gpsStatus(boolean isGPSEnable);
    }
    public void turnGPSOn(onGpsListener onGpsListener) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (onGpsListener != null) {
                onGpsListener.gpsStatus(true);
            }
        } else {
            mSettingsClient
                    .checkLocationSettings(mLocationSettingsRequest)
                    .addOnSuccessListener((Activity) this, new OnSuccessListener<LocationSettingsResponse>() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {//  GPS is already enable, callback GPS status through listener
                            if (onGpsListener != null) {
                                onGpsListener.gpsStatus(true);
                            }
                        }
                    })
                    .addOnFailureListener((Activity) this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            int statusCode = ((ApiException) e).getStatusCode();
                            switch (statusCode) {
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    try {
                                        // Show the dialog by calling startResolutionForResult(), and check the
                                        // result in onActivityResult().
                                        ResolvableApiException rae = (ResolvableApiException) e;
                                        rae.startResolutionForResult((Activity) NavigationActivity.this, 1001);
                                    } catch (IntentSender.SendIntentException sie) {
                                        Log.e("gps_fail", "PendingIntent unable to execute request.");
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    String errorMessage = "Location settings are inadequate, and cannot be " +
                                            "fixed here. Fix in Settings.";
                                    Log.e("gps_fail", errorMessage);
                                    Toast.makeText((Activity) NavigationActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefSettings = getSharedPreferences("AppSettings",MODE_PRIVATE);
        String languageToLoad  = prefSettings.getString("Language","en"); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mCon = this;

        setSupportActionBar(binding.appBarNavigation.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_dashboard,
                R.id.nav_vehicle_master/*, R.id.nav_route_master*/, R.id.nav_profile,
                /*R.id.nav_bank_details,*/ R.id.nav_change_pin)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if (PreferenceUtil.getUser() != null) {
            if (PreferenceUtil.getUser().getNAME() != null && !PreferenceUtil.getUser().getNAME().isEmpty()) {
                userNameStr = PreferenceUtil.getUser().getNAME();
            }
        }

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.userNameTextView);
        navUsername.setText(userNameStr);

        MenuItem logout = navigationView.getMenu().findItem(R.id.nav_logout);
        logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                drawer.closeDrawer(GravityCompat.START);
                new MaterialDialog.Builder(mCon)
                        .title("Logout")
                        .content("Are you sure, you want to logout?")
                        .positiveText("Yes")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                PreferenceUtil.setUserLoggedIn(false);
                                PreferenceUtil.clearAll();
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("currentActiveTrip",MODE_PRIVATE);
                                pref.edit().putBoolean("isTripActive", false).commit();
                                pref.edit().putBoolean("isLocationServiceActive", false).commit();

                                SharedPreferences pref1 = getSharedPreferences("tripImageLoad",MODE_PRIVATE);
                                pref1.edit().putString("loadImage", "pending").commit();

                                DBHelper mydb = new DBHelper(mCon);
                                mydb.deleteData();

                                Intent intent = new Intent(mCon, LoginActivity.class);
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
                return true;
            }
        });


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mSettingsClient = LocationServices.getSettingsClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(2 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        mLocationSettingsRequest = builder.build();//**************************
        builder.setAlwaysShow(true); //this is the key ingredient

        turnGPSOn(isGPSEnable -> {
        });
/*
        try {
            locationManager = (LocationManager) NavigationActivity.this
                    .getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!isGPSEnabled)
            {
                showSettingsAlert();
            }

        } catch (Exception e) {
            Log.e("gps exception",e.getMessage());
        }

 */
    /*   LocationAlarmScheduler alarmService = new LocationAlarmScheduler();
        mServiceIntent = new Intent(this, LocationAlarmScheduler.class);
        if (!isMyServiceRunning(alarmService.getClass())) {
            startService(mServiceIntent);
        }*/

        SharedPreferences pref = mCon.getSharedPreferences("currentActiveTrip",MODE_PRIVATE);
        if(pref.getBoolean("isLocationServiceActive",false)) {
            ComponentName componentName = new ComponentName(this, ExampleJobService.class);
            JobInfo info = new JobInfo.Builder(123, componentName)
                    .setPersisted(true)
                    .setPeriodic(15 * 60 * 1000)
                    .build();

            JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            int resultCode = scheduler.schedule(info);
            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Log.d("TAG", "Job scheduled");
            } else {
                Log.d("TAG", "Job scheduling failed");
            }
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.e ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_notification:
                startActivity(new Intent(mCon, NotificationActivity.class));
                return true;
            case R.id.action_settings:
                Intent intent =new Intent(mCon, SettingsActivity.class);
                intent.putExtra("activity","Nav");
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onDestroy() {
        //stopService(mServiceIntent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("resultcode",resultCode+"");
        if(requestCode==1001 && resultCode==0)
        {
            //finish();
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            mLocationSettingsRequest = builder.build();//**************************
            builder.setAlwaysShow(true); //this is the key ingredient

            turnGPSOn(isGPSEnable -> {
            });
        }

    }
}
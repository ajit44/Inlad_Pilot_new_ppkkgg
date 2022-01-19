package com.inland.pilot.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.jetbrains.annotations.NotNull;

import com.inland.pilot.NavigationActivity;
import com.inland.pilot.R;
import com.inland.pilot.databinding.ActivityLocationBinding;

public class LocationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private ActivityLocationBinding binding;
    private Context mCon;
    private GpsTracker gpsTracker;
    //private double latitude, longitude;
    private String latitude, longitude;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 12;
    private static final int REQUEST_PERMISSION_SETTING = 2;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private double currentLatitude;
    private double currentLongitude;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    protected static Location mCurrentLocation;
    protected Boolean mRequestingLocationUpdates;
    private NavigationActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location);

        mCon = this;

        activity = new NavigationActivity();

        /*try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]
                        {android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        binding.locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });
    }

    public void getLocation() {
        gpsTracker = new GpsTracker(mCon);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.latitude+"";
            longitude = gpsTracker.longitude+"";
            binding.locationTextView.setText(String.valueOf(latitude) + " : " + String.valueOf(longitude));
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    startLocationUpdates();
                    break;
                case Activity.RESULT_CANCELED:
                    checkLocationSettings();
                    break;
            }
        } else if (requestCode == REQUEST_PERMISSION_SETTING) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    detectLocation();
                    break;
                case Activity.RESULT_CANCELED:
                    getLocation();
                    break;
                default:
                    break;
            }
        }
    }
/*
    private void getLocation() {
        final boolean result = checkPermission(mCon);
        if (result) {
            buildGoogleApiClient();
            createLocationRequest();
            buildLocationSettingsRequest();
            checkLocationSettings();
        }
    }
*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    new MaterialDialog.Builder(mCon)
                            .title(R.string.permissionNecessary)
                            .content(R.string.locationPermissionIsNecessaryGivePermissionFromSetting)
                            .cancelable(false)
                            .positiveText("Yes")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
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
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(mCon,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                    }

                } else {
                    boolean showRationale = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                    }
                    if (!showRationale) {
                        new MaterialDialog.Builder(mCon)
                                .title(R.string.permissionNecessary)
                                .content(R.string.locationPermissionIsNecessaryGivePermissionFromSetting)
                                .cancelable(false)
                                .positiveText("Yes")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", mCon.getPackageName(), null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
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
                    } else {
                        Toast.makeText(mCon, R.string.permissionDenied, Toast.LENGTH_LONG).show();
                        getLocation();
                    }


                    // checkLocationPermission();
                }
            }
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mCon)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        // result.setResultCallback(this);
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates locationSettingsStates = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        if (mGoogleApiClient.isConnected()) {
                            startLocationUpdates();
                        } else {
                            buildGoogleApiClient();
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });

    }

    protected void startLocationUpdates() {
        detectLocation();
    }

    private void detectLocation() {
        if (ContextCompat.checkSelfPermission(mCon,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            mRequestingLocationUpdates = true;
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //gpsTracker.stopListener();
    }

    @Override
    public void onConnected(@Nullable @org.jetbrains.annotations.Nullable Bundle bundle) {
        if (mCurrentLocation == null) {

            if (ContextCompat.checkSelfPermission(mCon,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                updateLocationUI();
            }
        }
    }

    private void updateLocationUI() {
        if (mCurrentLocation != null) {

            final boolean mock = isMockSettingsON(mCon, mCurrentLocation);
            // Log.d("check", "mock = " + mock);
            if (mock) {
                Toast.makeText(mCon, R.string.turnOffMockLocation, Toast.LENGTH_SHORT).show();
                //finish();
            } else {

                latitude = String.valueOf(mCurrentLocation.getLatitude());
                longitude = String.valueOf(mCurrentLocation.getLongitude());
                binding.locationTextView.setText(latitude + " : " + longitude);

                // Log.d("check", latitude + ", " + longitude);
                //   Toast.makeText(mCon, String.valueOf(mCurrentLocation.getLatitude()) + String.valueOf(mCurrentLocation.getLongitude()), Toast.LENGTH_LONG).show();
            }
        }
    }

    public static boolean isMockSettingsON(Context context, Location location) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            boolean mockSettingOff = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
            return !mockSettingOff;
        } else {
            return location.isFromMockProvider(); //should use location object to detect mock locaiton
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull @NotNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.d("check", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(@NonNull @NotNull Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        binding.locationTextView.setText(String.valueOf(currentLatitude) + " : " + String.valueOf(currentLongitude));
    }
}
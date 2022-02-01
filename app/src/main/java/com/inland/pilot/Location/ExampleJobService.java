package com.inland.pilot.Location;

import static com.inland.pilot.MyTrip.UpcomingTripActivity.dst_lati;
import static com.inland.pilot.MyTrip.UpcomingTripActivity.dst_longi;
import static com.inland.pilot.MyTrip.UpcomingTripActivity.tvETA;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.inland.pilot.Login.VerifyMobileNoResponseModel;
import com.inland.pilot.MyTrip.UpcomingTripActivity;
import com.inland.pilot.Network.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExampleJobService extends JobService {
    private static final String TAG = "ExampleJobService";
    private boolean jobCancelled = false;
    private boolean isJobActive = false;
    String latitude, longitude;
    DBHelper mydb;
   // DBHelper dbHelper;
    SharedPreferences pref;
   FusedLocationProviderClient mFusedLocationClient;
    static String lat="";
    private PendingIntent mAlarmSender;
    private AlarmManager am;
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");
        mydb = new DBHelper(this);
        doBackgroundWork(params);

       // dbHelper = new DBHelper(getApplicationContext());
        return true;
    }

    private void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
              //  if(dbHelper == null)
              //      dbHelper = new DBHelper(getApplicationContext());
              //  Intent intent_alarm = new Intent(getApplicationContext(), LocationUpdateService.class);
              //  mAlarmSender = PendingIntent.getService(getApplicationContext(), 0, intent_alarm, 0);
              //  am = (AlarmManager) getSystemService(ALARM_SERVICE);
              //  am.setRepeating(AlarmManager.RTC, 0, 20 * 1000, mAlarmSender);
                pref = getApplicationContext().getSharedPreferences("currentActiveTrip",MODE_PRIVATE);
                isJobActive= pref.getBoolean("isLocationServiceActive", false);
                while (isJobActive){

                    isJobActive= pref.getBoolean("isLocationServiceActive", false);
                    if (!isJobActive) {
                        return;
                    }
                   // Intent intent_alarm = new Intent(getApplicationContext(), LocationUpdateService.class);
                   //   mAlarmSender = PendingIntent.getService(getApplicationContext(), 0, intent_alarm, 0);
                   //   am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    //  am.setRepeating(AlarmManager.RTC, 0, 20 * 1000, mAlarmSender);
                  //////  startService(new Intent(getApplicationContext(), LocationUpdateService.class));
                    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

                    // method to get the location
                    getLastLocation();


                 //   dbHelper.insertData("i","","","","","");
                    Log.d(TAG, " data: " +lat);


                    try {
                        Thread.sleep(60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                  jobFinished(params, false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            Log.d("gps2",location.getLatitude() + "");
                            Log.d("gps2",location.getLongitude() + "");
                            latitude = location.getLatitude()+"";
                            longitude = location.getLongitude()+"";
                            uploadLocFlow();
                        }
                    }
                });
            } else {
              /*  Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);*/
            }
        } else {
            // if permissions aren't available,
            // request for permissions

        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            Log.d("gps","Latitude: " + mLastLocation.getLatitude() + "");
            Log.d("gps","Longitude: " + mLastLocation.getLongitude() + "");
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions


    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void uploadLocFlow(){
        try {
            Log.d("flow","11");
            if(mydb == null)    mydb = new DBHelper(this);
            if(isConnected()) {
                Log.d("flow","22");
                JSONArray js =mydb.getSqlToJSONResults();
                if(js.length()!=0){
                    Log.d("flow","33");
                    insertGPS();
                    uploadOfflineGPSData(mydb.getSqlToJSONResults());
                }else {
                    uploadLoc();
                    Log.d("flow","44");
                }
            }else{
                Log.d("flow","55");
                insertGPS();
            }
        } catch (InterruptedException e) {
            Log.e("service_flow",e.toString());
        } catch (IOException e) {
            Log.e("service_flow",e.toString());
        }
    }

    public boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        Boolean intr= Runtime.getRuntime().exec(command).waitFor() == 0;
        if(intr)
            return intr;
        else return isInternetOn();
    }

    private void insertGPS(){
        Log.d("databasesql","ddd");

        SharedPreferences preferences_shared = PreferenceManager.getDefaultSharedPreferences(this);
        String LoginId = preferences_shared.getString("LoginId", "");
        String TripId = preferences_shared.getString("TripId","");
        String deviceIdStr = preferences_shared.getString("deviceIdStr","");
        String tokenNoStr = preferences_shared.getString("tokenNoStr","");

        mydb.insertData(TripId,LoginId,longitude,latitude,deviceIdStr,tokenNoStr);

    }

    void uploadOfflineGPSData(JSONArray array)
    {
        Log.d("array",array.toString());
        final JSONObject request = new JSONObject();
        try {
            request.put("ConsiData",array.toString());

            Log.v("awbData",request.toString());

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }//

        AndroidNetworking.post("https://api.inland.in/Pilot/api/Driver/InsertOffLineTripGsp")
                .addJSONObjectBody(request)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("idgpsupload",response.toString());
                        if(response.toString().contains("OK Multiple"))
                            mydb.deleteData();

                    }
                    //
                    @Override
                    public void onError(ANError anError) {
                        Log.e("ERROR", anError.toString());

                    }
                });
    }

    public void uploadLoc()
    {

        if(tvETA != null)
            if(UpcomingTripActivity.KEY_TOTAL_DISTANCE !=0){
                float[] results = new float[1];
                Location.distanceBetween( Double.parseDouble(latitude),  Double.parseDouble(longitude),
                        dst_lati, dst_longi, results);
                float distance =(int) (results[0] / 1000);
                tvETA.setText( distance+"");
            }

        try {
            SharedPreferences preferences_shared = PreferenceManager.getDefaultSharedPreferences(this);
            String LoginId = preferences_shared.getString("LoginId", "");
            String TripId = preferences_shared.getString("TripId","");
            String deviceIdStr = preferences_shared.getString("deviceIdStr","");
            String tokenNoStr = preferences_shared.getString("tokenNoStr","");

            ContentValues contentValues = new ContentValues();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String currentDateandTime  = dateFormat.format(new Date());
            Log.d("gpsDatas","1"+"|"+TripId+"|"+LoginId+"|"+longitude+"|"+latitude+"|"+deviceIdStr+"|"+tokenNoStr+"|"+currentDateandTime);

            SetGPSModel requestModel = new SetGPSModel("1", TripId, LoginId, longitude, latitude, deviceIdStr, tokenNoStr,currentDateandTime);
            Log.d("sendData",requestModel.toString());
            Call<VerifyMobileNoResponseModel> call = ApiClient.getNetworkService().setGPS(requestModel);

            call.enqueue(new Callback<VerifyMobileNoResponseModel>() {
                @Override
                public void onResponse(Call<VerifyMobileNoResponseModel> call, Response<VerifyMobileNoResponseModel> response) {
                    Log.d("onGPSDATA",""+response.body().toString());
                    if (response.isSuccessful()) {
                        String messageStr = response.body().getVerifyMobileNoModels().get(0).getMESSAGE();

                        if (messageStr.equalsIgnoreCase("OK Single")) {
                            Log.e("check", "GPS send" + messageStr);
                        } else {
                            Log.e("check", "invalid msg" + messageStr);
                        }
                    } else {
                        Log.e("check", "error" );
                    }
                }

                @Override
                public void onFailure(Call<VerifyMobileNoResponseModel> call, Throwable t) {
                    if (!call.isCanceled()) {
                        //Toast.makeText(mCon, "Please check internet connection", Toast.LENGTH_SHORT).show();
                        Log.d("check", "onFailure: " + t.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("check", "GPS: " + e.getMessage());
        }
        stopSelf();
    }

    public boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {


            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {


            return false;
        }
        return false;
    }

}
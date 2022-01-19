package com.inland.pilot.Location;

import static com.inland.pilot.MyTrip.UpcomingTripActivity.dst_lati;
import static com.inland.pilot.MyTrip.UpcomingTripActivity.dst_longi;
import static com.inland.pilot.MyTrip.UpcomingTripActivity.tvETA;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.inland.pilot.Login.SetMpinModel;
import com.inland.pilot.Login.VerifyMobileNoResponseModel;
import com.inland.pilot.MyTrip.UpcomingTripActivity;
import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationUpdateService extends Service {
    private Thread triggerService;
    private LocationManager lm;
    private FusedLocationProviderClient mFusedLocationClient;
    private Context mCon;
    DBHelper mydb;

    public LocationUpdateService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mCon=this;
        mydb = new DBHelper(this);
        addLocationListener();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.e("location_tag","entered update hmmm");
        super.onStart(intent, startId);
        mCon=this;
        addLocationListener();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("location_tag","entered update");
        mCon=this;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        addLocationListener();

        return START_STICKY;
    }
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
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

    private String latitude, longitude;
    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude=mLastLocation.getLatitude() + "";
            longitude=mLastLocation.getLongitude() + "";
            Log.e("Latitude: " ,mLastLocation.getLatitude() + "");
            Log.e("Longitude: " , mLastLocation.getLongitude() + "");
            uploadLocFlow();

        }
    };
    @SuppressLint("MissingPermission")
    private void addLocationListener() {

                try {
                    Log.e("location_tag","entered update 1");
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
                                    latitude=location.getLatitude() + "";
                                    longitude=location.getLongitude() + "";
                                    uploadLocFlow();
                                    Log.e("Latitude 1: " ,location.getLatitude() + "");
                                    Log.e("Longitude 1: " , location.getLongitude() + "");

                                }
                            }
                        });
                    } else {
                        Toast.makeText(LocationUpdateService.this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
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

    public boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
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
                        if(response.toString().contains("SUCCESSFULLY"))
                            mydb.deleteData();

                    }
                    //
                    @Override
                    public void onError(ANError anError) {
                        Log.e("ERROR", anError.toString());

                    }
                });
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
   /*// private String getCurrentAddress() throws IOException {
        Geocoder gCoder = new Geocoder(mCon);
        ArrayList<Address> addresses = gCoder.getFromLocation(123456789, 123456789, 1);
        if (addresses != null && addresses.size() > 0) {
            Toast.makeText(myContext, "country: " + addresses.get(0).getCountryName(), Toast.LENGTH_LONG).show();
        }
        return null;
    }*/
}
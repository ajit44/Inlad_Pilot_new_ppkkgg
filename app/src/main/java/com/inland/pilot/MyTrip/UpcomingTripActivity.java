package com.inland.pilot.MyTrip;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.inland.pilot.LoadImage.LoadUploadListActivity;
import com.inland.pilot.Location.DBHelper;
import com.inland.pilot.Location.LocationAlarmScheduler;
import com.inland.pilot.Location.MapsActivity;
import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.ActivityUpcomingTripBinding;
import com.inland.pilot.databinding.FragmentTripDetailsBinding;
import com.inland.pilot.unloading.UnloadingUploadListActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingTripActivity extends AppCompatActivity {

    private ActivityUpcomingTripBinding binding;
    boolean isActiveTripNOTAvailable=true;
    String deviceIdStr;
    private Context mCon;
    private TripListAdapter tripListAdapter;
    private List<TripMasterModel> tripMasterModelList = new ArrayList<>();
    private String loginIdStr;
    private RequestActiveTripListModel tripListModel;
    private RequestTripListModel reqTripListModel;
    public static TextView tvETA;
    public static double dst_longi=0.0,dst_lati=0.0;
    String mapButtonFlg="";
    SharedPreferences pref;
    public static int KEY_TOTAL_DISTANCE=0;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SharedPreferences pref1;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String tokenNoStr;


    // TODO: Rename and change types and number of parameters

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upcoming_trip);

        tvETA = findViewById(R.id.tvETA);
        mCon = this;
        tripListAdapter = new TripListAdapter(mCon, this);
        binding.tripListRecycler.setHasFixedSize(true);
        binding.tripListRecycler.setLayoutManager(new LinearLayoutManager(mCon));
        binding.tripListRecycler.setItemAnimator(new DefaultItemAnimator());

        pref1 = getSharedPreferences("tripImageLoad",MODE_PRIVATE);

        pref = mCon.getSharedPreferences("currentActiveTrip",MODE_PRIVATE);
        //SharedPreferences preferences_shared = PreferenceManager.getDefaultSharedPreferences(mCon);
       // Toast.makeText(this, "b "+preferences_shared.getBoolean("reaching_to_final",false), Toast.LENGTH_SHORT).show();
        if (PreferenceUtil.getUser() != null) {
            if (PreferenceUtil.getUser().getP_MOBILENO() != null && !PreferenceUtil.getUser().getP_MOBILENO().isEmpty()) {
                loginIdStr = PreferenceUtil.getUser().getP_MOBILENO();
                tokenNoStr= PreferenceUtil.getTokenNo();
                reqTripListModel = new RequestTripListModel("1",loginIdStr);
                getTripList_1(reqTripListModel);
                //setCurretTripData();
                //tripListModel = new RequestActiveTripListModel(loginIdStr, tokenNoStr);
                //getTripList(tripListModel);
            }
        }

        LocationAlarmScheduler alarmService = new LocationAlarmScheduler();
        Intent mServiceIntent = new Intent(this, LocationAlarmScheduler.class);
        if (!isMyServiceRunning(alarmService.getClass())) {
            startService(mServiceIntent);
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
        Log.e ("Service status", "Not running");
        return false;
    }

    private void getTripList(RequestActiveTripListModel requestTripListModel) {
        try {

            Call<TripListModel> call = ApiClient.getNetworkService().getTripList(requestTripListModel);

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
                        dialog.dismiss();
                        if (response.body() != null && response.body().getTripListModels() != null &&
                                !response.body().getTripListModels().isEmpty()) {

                            tripMasterModelList = response.body().getTripListModels();
                            tripMasterModelList= reverseList(tripMasterModelList);

                            tripListAdapter.addAll(tripMasterModelList);
                            binding.tripListRecycler.setVisibility(View.VISIBLE);
                            binding.errorTextView.setVisibility(View.GONE);
                            binding.tripListRecycler.setAdapter(tripListAdapter);
                        } else {
                            binding.tripListRecycler.setVisibility(View.GONE);
                            binding.errorTextView.setVisibility(View.VISIBLE);
                          //  Toast.makeText(mCon, "No Upcoming trip found !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        binding.tripListRecycler.setVisibility(View.GONE);
                        binding.errorTextView.setVisibility(View.VISIBLE);
                        Toast.makeText(mCon, "2 " + mCon.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<TripListModel> call, Throwable t) {
                    if (!call.isCanceled()) {
                        dialog.dismiss();
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
           // getTripList(tripListModel);
        }
        /*if(pref !=null)
            setCurretTripData();*/
    }
    public static<T> List<T> reverseList(List<T> list)
    {
        List<T> reverse = new ArrayList<>(list);
        Collections.reverse(reverse);
        return reverse;
    }


    private void getTripList_1(RequestTripListModel requestTripListModel) {
        try {
            Log.d("trip","1");
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
                        if (response.body() != null && response.body().getTripListModels() != null &&
                                !response.body().getTripListModels().isEmpty()) {
                            tripMasterModelList = response.body().getTripListModels();


                            TripMasterModel current= tripMasterModelList.get(0);
                                if(current.getTP_Active().equals("1")){
                                    String test =pref.getString("tripno", "empty");
                                    if (test.equals("empty")){
                                        if (PreferenceUtil.getUser() != null) {
                                            if (PreferenceUtil.getUser().getP_MOBILENO() != null && !PreferenceUtil.getUser().getP_MOBILENO().isEmpty()) {
                                                loginIdStr = PreferenceUtil.getUser().getP_MOBILENO();
                                                deviceIdStr = PreferenceUtil.getUser().getDeviceId();
                                                tokenNoStr = PreferenceUtil.getUser().getTOKENNO();
                                                Log.e("token no", tokenNoStr);
                                            }
                                        }
                                        //  TripMasterModel latest_pos = data.get(pos_inner);
                                        SharedPreferences preferences_shared = PreferenceManager.getDefaultSharedPreferences(mCon);
                                        SharedPreferences.Editor editor = preferences_shared.edit();
                                        editor.putString("LoginId", loginIdStr+"");
                                        editor.putString("TripId", current.getPlacementid()+"");
                                        editor.putString("deviceIdStr", deviceIdStr+"");
                                        editor.putString("tokenNoStr", tokenNoStr+"");
                                        editor.putBoolean("reaching_to_start", true);
                                        editor.putBoolean("reaching_to_final", false);
                                        editor.commit();

                                        editor.remove("image_1");
                                        editor.remove("image_2");
                                        editor.remove("image_3");
                                        editor.remove("image_4");
                                        editor.remove("image_5");
                                        editor.remove("image_6");
                                        editor.remove("image_7");
                                        editor.remove("image_8");
                                        editor.commit();
                                        pref.edit().putBoolean("isTripActive", true).commit();
                                        pref.edit().putBoolean("isLocationServiceActive", true).commit();
                                        pref.edit().putString("tripno", current.getTripno()).commit();
                                        Log.d("tttrrid",current.getTripno());
                                        pref.edit().putString("Placementid", current.getPlacementid()).commit();
                                        pref.edit().putString("placementdt", current.getPlacementdt()).commit();
                                        pref.edit().putString("insertdate", current.getInsertdate()).commit();
                                        pref.edit().putString("Loading_location", current.getLoading_location()).commit();
                                        pref.edit().putString("loading_Latitude", current.getLoading_Latitude()).commit();
                                        pref.edit().putString("loading_Longitude", current.getLoading_Longitude()).commit();
                                        pref.edit().putString("Unloading_location", current.getUnloading_location()).commit();
                                        pref.edit().putString("Unloading_Latitude", current.getUnloading_Latitude()).commit();
                                        pref.edit().putString("Unloading_Longitude", current.getUnloading_Longitude()).commit();
                                        pref.edit().putString("Tp_Active", current.getTP_Active()).commit();
                                    }
                                    else setCurretTripData();

                                }else {
                                    Log.d("trip0","here");

                                }


                            //tripListAdapter.addAll(tripMasterModelList);
                            binding.tvOnGoingTripLayout.setVisibility(View.VISIBLE);
                        } else {
                            reqTripListModel = new RequestTripListModel("0",loginIdStr);
                            getTripList_0(reqTripListModel);
                            binding.tvOnGoingTripLayout.setVisibility(View.GONE);
                         //   Toast.makeText(mCon, "No Active trip found !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        binding.tvOnGoingTripLayout.setVisibility(View.GONE);
                        Toast.makeText(mCon, "1 " + mCon.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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
    private void getTripList_0(RequestTripListModel requestTripListModel) {
        try {
            Log.d("trip","2");
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
                    Log.d("trip0",response.body().toString());
                    if (response.isSuccessful()) {
                        dialog.dismiss();
                        if (response.body() != null && response.body().getTripListModels() != null &&
                                !response.body().getTripListModels().isEmpty()) {

                            tripMasterModelList = response.body().getTripListModels();
                            tripMasterModelList= reverseList(tripMasterModelList);

                            tripListAdapter.addAll(tripMasterModelList);
                            binding.tripListRecycler.setVisibility(View.VISIBLE);
                            binding.errorTextView.setVisibility(View.GONE);
                            binding.tripListRecycler.setAdapter(tripListAdapter);
                        } else {
                            binding.tripListRecycler.setVisibility(View.GONE);
                            binding.errorTextView.setVisibility(View.VISIBLE);
                            //  Toast.makeText(mCon, "No Upcoming trip found !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        binding.tripListRecycler.setVisibility(View.GONE);
                        binding.errorTextView.setVisibility(View.VISIBLE);
                        Toast.makeText(mCon, "2 " + mCon.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }

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


    public static String getDate(String dt){
        String arr[];
        if(dt.contains("T")) {
            arr = dt.split("T");
            return arr[0];
        }
        return dt;
    }

    void setCurretTripData() {
        //   pref.edit().putBoolean("isTripActive", true).commit();
        if (pref.getBoolean("isTripActive", false)){

            binding.hintUpcTrip.setVisibility(View.GONE);
            binding.rvUpcomingTrip.setVisibility(View.GONE);

            TripMasterModel current = new TripMasterModel(pref.getString("tripno", ""), pref.getString("Placementid", ""), pref.getString("placementdt", ""), pref.getString("insertdate", ""), pref.getString("Loading_location", ""), pref.getString("loading_Latitude", ""),
                    pref.getString("loading_Longitude", ""), pref.getString("Unloading_location", ""), pref.getString("Unloading_Latitude", ""), pref.getString("Unloading_Longitude", ""), pref.getString("Tp_Active", ""));
        final int pos_inner = 0;
        binding.tvOnGoingTripLayout.setVisibility(View.VISIBLE);
/*
 holder.binding.tvTripId.setText("Trip ID: "+current.getPlacementid());
        String addr[]=current.getLoading_location().split(", ");
        holder.binding.tvSourceCityNstate.setText(addr[0]);
        holder.binding.tvSourceAddress.setText(addr[1]+", "+addr[2]);

        String addr2[]=current.getUnloading_location().split(", ");
        holder.binding.tvDestCity.setText(addr2[0]);
        holder.binding.tvDestAddress.setText(addr2[1]+", "+addr2[2]);

        String dateFormat[] = current.getInsertdate().split(".");
        String dateANDtime[] = dateFormat[0].split("T");

        holder.binding.tvAssignedDate.setText(dateANDtime[0]);
        holder.binding.tvAssignedTime.setText(dateANDtime[1]);
 */
        binding.tvTripId.setText("Trip ID: " + current.getPlacementid());
        String addr[]=current.getLoading_location().split(", ");
        binding.tvSourceCityNstate.setText(addr[0]);
        binding.tvSourceAddress.setText(addr[1]+", "+addr[2]);

        String addr2[]=current.getUnloading_location().split(", ");
        binding.tvDestCity.setText(addr2[0]);
        binding.tvDestAddress.setText(addr2[1]+", "+addr2[2]);

        binding.tvAssignedDate.setText(getDate(current.getInsertdate()));
        binding.tvExpectedDate.setText(getDate(current.getPlacementdt()));
        if (current.getTP_Active().equals("0")) {
            binding.acceptButton.setVisibility(View.VISIBLE);
        } else {
            binding.acceptButton.setVisibility(View.INVISIBLE);
        }
        //fetching distance
        double src_longi = 0.0;
        double src_lati = 0.0;

        src_longi = Double.parseDouble(current.getLoading_Longitude());
        src_lati = Double.parseDouble(current.getLoading_Latitude());

        dst_longi = Double.parseDouble(current.getUnloading_Longitude());
        dst_lati = Double.parseDouble(current.getUnloading_Latitude());
        float[] results = new float[1];
        Location.distanceBetween(src_lati, src_longi,
                dst_lati, dst_longi, results);
        float distance = (int) (results[0] / 1000);
        KEY_TOTAL_DISTANCE = (int) distance;
        binding.tvTotalKm.setText(distance + "");
        binding.mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double src_longi = 0.0;
                double src_lati = 0.0;
                double dst_longi = 0.0;
                double dst_lati = 0.0;
                try {
                    src_longi = Double.parseDouble(current.getLoading_Longitude());
                    src_lati = Double.parseDouble(current.getLoading_Latitude());

                    dst_longi = Double.parseDouble(current.getUnloading_Longitude());
                    dst_lati = Double.parseDouble(current.getUnloading_Latitude());

                    Uri gmmIntentUri;
                    if(mapButtonFlg.equals("current_to_dest"))
                        gmmIntentUri= Uri.parse("google.navigation:q="+dst_lati+","+dst_longi+"&mode=d");
                    else
                        gmmIntentUri=Uri.parse("google.navigation:q="+src_lati+","+src_longi+"&mode=d");

                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                    //startActivity(intent);
                } catch (Exception ex) {
                    Toast.makeText(mCon, "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.loadingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, LoadUploadListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TripId", current.getPlacementid());
                intent.putExtras(bundle);
                mCon.startActivity(intent);
                finish();
            }
        });
        SharedPreferences preferences_shared = PreferenceManager.getDefaultSharedPreferences(mCon);
        boolean reaching_to_start = preferences_shared.getBoolean("reaching_to_start", false);
        boolean reaching_to_final = preferences_shared.getBoolean("reaching_to_final", false);
        String isImageLoad = pref1.getString("loadImage", "pending");

        //  Toast.makeText(mCon, "imgd "+isImageLoad, Toast.LENGTH_SHORT).show();
        if (reaching_to_start == false && isImageLoad.equals("uploadImage")) {
            binding.loadingImage.setVisibility(View.VISIBLE);
            binding.acceptButton.setVisibility(View.GONE);
            binding.reachedToDestinationButton.setVisibility(View.GONE);
            binding.tvStatus.setText("(Currently at Start Location)");
            mapButtonFlg="current_to_dest";
            // tvETA.setText("0");
        } else if (reaching_to_start == true) {
            binding.loadingImage.setVisibility(View.GONE);
            binding.acceptButton.setVisibility(View.VISIBLE);
            binding.reachedToDestinationButton.setVisibility(View.GONE);
            binding.tvStatus.setText("(On way to Start Location)");
            mapButtonFlg="current_to_start";
            // tvETA.setText("0");
        } else if (reaching_to_final) {
            binding.loadingImage.setVisibility(View.GONE);
            binding.acceptButton.setVisibility(View.GONE);
            binding.reachedToDestinationButton.setVisibility(View.VISIBLE);
            binding.tvStatus.setText("On way to Destination...");
            mapButtonFlg="current_to_dest";
        } else if (reaching_to_final == false) {
            binding.unLoadingImage.setVisibility(View.VISIBLE);
            binding.loadingImage.setVisibility(View.GONE);
            binding.acceptButton.setVisibility(View.GONE);
            binding.reachedToDestinationButton.setVisibility(View.GONE);
            binding.tvStatus.setText("(Currently at Destination)");
            mapButtonFlg="current_to_dest";
        }

        //reachedToDestinationButton
        binding.reachedToDestinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reaching_to_final) {
                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(mCon);
                    dialogBuilder.setTitle("Confirmation");
                    dialogBuilder.setMessage("Proceed to POD Image Upload ?");
                    dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            SharedPreferences preferences_shared = PreferenceManager.getDefaultSharedPreferences(mCon);
                            SharedPreferences.Editor editor = preferences_shared.edit();
                            editor.putBoolean("reaching_to_final", false);

                           //(adding into pod upload) pref.edit().putBoolean("isLocationServiceActive", false).commit();
                            editor.commit();

                            TripMasterModel current_trip = current;
                            Intent intent = new Intent(mCon, UnloadingUploadListActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("TripId", current_trip.getPlacementid());
                            intent.putExtras(bundle);
                            mCon.startActivity(intent);
                        }
                    });
                    dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.create();
                    dialogBuilder.show();
                } else {
                    TripMasterModel current_trip = current;
                    Intent intent = new Intent(mCon, UnloadingUploadListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("TripId", current_trip.getPlacementid());
                    intent.putExtras(bundle);
                    mCon.startActivity(intent);
                }
            }
        });
        binding.unLoadingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TripMasterModel current_trip = current;
                Intent intent = new Intent(mCon, UnloadingUploadListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TripId", current_trip.getPlacementid());
                intent.putExtras(bundle);
                mCon.startActivity(intent);
            }
        });

///// if reaching to source
        binding.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences_shared = PreferenceManager.getDefaultSharedPreferences(mCon);
                boolean reaching_to_start = preferences_shared.getBoolean("reaching_to_start", false);
                if (reaching_to_start) {
                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(mCon);
                    dialogBuilder.setTitle("Confirmation");
                    dialogBuilder.setMessage("Are you reached to Start Location?");
                    dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            SharedPreferences preferences_shared = PreferenceManager.getDefaultSharedPreferences(mCon);
                            SharedPreferences.Editor editor = preferences_shared.edit();
                            editor.putBoolean("reaching_to_start", false);
                            editor.commit();

                            pref1.edit().putString("loadImage", "uploadImage").commit();

                            binding.loadingImage.setVisibility(View.VISIBLE);
                            binding.acceptButton.setVisibility(View.GONE);
                        }
                    });
                    dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.create();
                    dialogBuilder.show();
                } else {
                    Intent intent = new Intent(mCon, LoadUploadListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("TripId", current.getPlacementid());
                    intent.putExtras(bundle);
                    mCon.startActivity(intent);
                }
            }
        });
    }

}
    private String getFormatedDate(String tp_date, String tp_time) {
        String formattedDate=null;
        try {
            DateFormat originalFormat = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("hh:mm");
            Date date = originalFormat.parse(tp_time);
            formattedDate = targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tp_date+" | "+formattedDate;
    }

}
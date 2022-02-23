package com.inland.pilot.MyTrip;

import static android.content.Context.JOB_SCHEDULER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.inland.pilot.Location.ExampleJobService;
import com.inland.pilot.Location.LocationAlarmScheduler;
import com.inland.pilot.Location.LocationUpdateService;
import com.inland.pilot.Location.MapsActivity;
import com.inland.pilot.Location.SetTripStatusUpdateModel;
import com.inland.pilot.Login.RegistrationActivity;
import com.inland.pilot.Login.SetMpinModel;
import com.inland.pilot.Login.VerifyMobileNoResponseModel;
import com.inland.pilot.NavigationActivity;
import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.SplashScreenActivity;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.VehicleMaster.AddUpdateVehicleDetailsActivity;
import com.inland.pilot.VehicleMaster.VehicleMasterModel;
import com.inland.pilot.databinding.TripAllottedListRowBinding;
import com.inland.pilot.databinding.UserActivatedDialogBinding;
import com.inland.pilot.databinding.VehicleListRowBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.MyViewHolder> {
    private Context mCon;
    private LayoutInflater inflater;
    private List<TripMasterModel> data;
    private String loginIdStr;
    private String deviceIdStr, tokenNoStr;
    SharedPreferences pref;
    private UpcomingTripActivity parentActivity;



    public TripListAdapter(Context context, UpcomingTripActivity parentActivity) {
        mCon = context;
        this.parentActivity=parentActivity;
        data = new ArrayList<>();
        inflater = LayoutInflater.from(mCon);
        pref = mCon.getSharedPreferences("currentActiveTrip",MODE_PRIVATE);

    }

    public void addAll(List<TripMasterModel> list) {
        if (data != null) {
            clear();
            data.addAll(list);
            notifyDataSetChanged();
        }
    }

    private void clear() {
        if (data != null) {
            data.clear();
            notifyDataSetChanged();
        }
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        TripAllottedListRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.trip_allotted_list_row, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        final TripMasterModel current = data.get(position);
        final int pos_inner=position;

        holder.binding.tvTripId.setText("Trip ID: "+current.getPlacementid());
        String addr[]=current.getLoading_location().split(", ");
        holder.binding.tvSourceCityNstate.setText(addr[0]);
        holder.binding.tvSourceAddress.setText(addr[1]+", "+addr[2]);

        String addr2[]=current.getUnloading_location().split(", ");
        holder.binding.tvDestCity.setText(addr2[0]);
        holder.binding.tvDestAddress.setText(addr2[1]+", "+addr2[2]);


        String dateANDtime[] = current.getInsertdate().split("T");
        holder.binding.tvAssignedDate.setText(dateANDtime[0]);
        holder.binding.tvAssignedTime.setText((dateANDtime[1].length() >5)? dateANDtime[1].substring(0,5):dateANDtime[1]);
        holder.binding.tvExpectedDate.setText(UpcomingTripActivity.getDate(current.getPlacementdt()));
      //  if(current.getTP_Active().equals("0"))

            holder.binding.acceptButton.setVisibility(View.VISIBLE);

        //fetching distance
        double src_longi=0.0;
        double src_lati=0.0;
        double dst_longi=0.0;
        double dst_lati=0.0;
        TripMasterModel latest_pos = data.get(pos_inner);
        src_longi = Double.parseDouble(current.getLoading_Longitude());
        src_lati = Double.parseDouble(current.getLoading_Latitude());

        dst_longi = Double.parseDouble(current.getUnloading_Longitude());
        dst_lati = Double.parseDouble(current.getUnloading_Latitude());
        float[] results = new float[1];
        Location.distanceBetween(src_lati, src_longi,
                dst_lati, dst_longi, results);
        float distance = (int) (results[0] / 1000);
        holder.binding.tvTotalKm.setText( distance+"");
        holder.binding.mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TripMasterModel latest_pos = data.get(pos_inner);
                double src_longi=0.0;
                double src_lati=0.0;
                double dst_longi=0.0;
                double dst_lati=0.0;
                try {
                    src_longi = Double.parseDouble(current.getLoading_Longitude());
                    src_lati = Double.parseDouble(current.getLoading_Latitude());

                    dst_longi = Double.parseDouble(current.getUnloading_Longitude());
                    dst_lati = Double.parseDouble(current.getUnloading_Latitude());

                 /*   Intent intent = new Intent(mCon, MapsActivity.class);
                    intent.putExtra("src_longi",src_longi);
                    intent.putExtra("src_lati",src_lati);
                    intent.putExtra("dst_longi",dst_longi);
                    intent.putExtra("dst_lati",dst_lati);
                   // intent.putExtra("start_addr",current.getTP_SL_Address());
                  //  intent.putExtra("dst_addr",current.getTP_DL_Address());
                    mCon.startActivity(intent);*/
                    Uri gmmIntentUri = Uri.parse("https://www.google.co.in/maps/dir/"+src_lati+","+src_longi+"/"+dst_lati+","+dst_longi);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    mCon.startActivity(mapIntent);
                }
                catch(Exception ex)
                {
                    Toast.makeText(mCon, "Error: "+ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.binding.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(mCon);
                    dialogBuilder.setTitle("Confirmation");
                    dialogBuilder.setMessage("Are you sure to accept the trip?");
                    dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (PreferenceUtil.getUser() != null) {
                                if (PreferenceUtil.getUser().getP_MOBILENO() != null && !PreferenceUtil.getUser().getP_MOBILENO().isEmpty()) {
                                    loginIdStr = PreferenceUtil.getUser().getP_MOBILENO();
                                    deviceIdStr = PreferenceUtil.getUser().getDeviceId();
                                    tokenNoStr = PreferenceUtil.getUser().getTOKENNO();
                                    Log.e("token no", tokenNoStr);
                                }
                            }
                            TripMasterModel latest_pos = data.get(pos_inner);
                            SharedPreferences preferences_shared = PreferenceManager.getDefaultSharedPreferences(mCon);
                            SharedPreferences.Editor editor = preferences_shared.edit();
                            editor.putString("LoginId", loginIdStr + "");
                            editor.putString("TripId", latest_pos.getPlacementid() + "");
                            editor.putString("deviceIdStr", deviceIdStr + "");
                            editor.putString("tokenNoStr", tokenNoStr + "");
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
                            Log.d("tttrrid", current.getTripno());
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


                            //    Intent mServiceIntent = new Intent(mCon, LocationAlarmScheduler.class);
                            //    mCon.startService(mServiceIntent);
                            ComponentName componentName = new ComponentName(mCon, ExampleJobService.class);
                            JobInfo info = new JobInfo.Builder(123, componentName)
                                    .setPersisted(true)
                                    .setPeriodic(15 * 60 * 1000)
                                    .build();

                            JobScheduler scheduler = (JobScheduler) mCon.getSystemService(JOB_SCHEDULER_SERVICE);
                            int resultCode = scheduler.schedule(info);
                            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                                Log.d("TAG", "Job scheduled");
                            } else {
                                Log.d("TAG", "Job scheduling failed");
                            }

                            //Intent intent_main=new Intent(mCon, UpcomingTripActivity.class);
                            // mCon.startActivity(intent_main);
                            //TripListAdapter.this.parentActivity.finish();
                            SetTripStatusUpdateModel requestModel = new SetTripStatusUpdateModel("1", latest_pos.getPlacementid(), loginIdStr);
                            tripStatusUpdate(requestModel);

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

            }
        });

//*****************************************************************
   /*     holder.binding.tripToFroTextView.setText("Location: "+current.getTP_B_Location()+" - " + current.getTP_T_Location());
        holder.binding.tripDateTextView.setText(current.getTP_Date());
        holder.binding.tripTimeTextView.setText(current.getTP_Time());
        holder.binding.startLocTextView.setText(current.getTP_SL_Address());
        holder.binding.endLocTextView.setText(current.getTP_DL_Address());
        holder.binding.tripIDTextView.setText("Trip ID: "+current.getPlacementid());
        if(current.getTP_Active().equals("1"))
        {
            holder.binding.acceptBtn.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.binding.acceptBtn.setVisibility(View.GONE);
        }
        holder.binding.mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TripMasterModel latest_pos = data.get(pos_inner);
                double src_longi=0.0;
                double src_lati=0.0;
                double dst_longi=0.0;
                double dst_lati=0.0;
                try {
                    src_longi = Double.parseDouble(latest_pos.getTP_Longitude());
                    src_lati = Double.parseDouble(latest_pos.getTP_Latitude());

                    dst_longi = Double.parseDouble(latest_pos.getTP_DLongitude());
                    dst_lati = Double.parseDouble(latest_pos.getTP_DLatitude());

                    Intent intent = new Intent(mCon, MapsActivity.class);
                    intent.putExtra("src_longi",src_longi);
                    intent.putExtra("src_lati",src_lati);
                    intent.putExtra("dst_longi",dst_longi);
                    intent.putExtra("dst_lati",dst_lati);
                    intent.putExtra("start_addr",current.getTP_SL_Address());
                    intent.putExtra("dst_addr",current.getTP_DL_Address());
                    mCon.startActivity(intent);
                }
                catch(Exception ex)
                {
                    Toast.makeText(mCon, "Error: "+ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.binding.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(mCon);
                dialogBuilder.setTitle("Confirmation");
                dialogBuilder.setMessage("Are you sure to accept the trip?");
                dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (PreferenceUtil.getUser() != null) {
                            if (PreferenceUtil.getUser().getP_MOBILENO() != null && !PreferenceUtil.getUser().getP_MOBILENO().isEmpty()) {
                                loginIdStr = PreferenceUtil.getUser().getP_MOBILENO();
                                deviceIdStr = PreferenceUtil.getUser().getDeviceId();
                                tokenNoStr = PreferenceUtil.getUser().getTOKENNO();
                                Log.e("token no", tokenNoStr);
                            }
                        }
                        TripMasterModel latest_pos = data.get(pos_inner);
                        SharedPreferences preferences_shared = PreferenceManager.getDefaultSharedPreferences(mCon);
                        SharedPreferences.Editor editor = preferences_shared.edit();
                        editor.putString("LoginId", loginIdStr+"");
                        editor.putString("TripId", latest_pos.getPlacementid()+"");
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

                        SetTripStatusUpdateModel requestModel = new SetTripStatusUpdateModel("1", latest_pos.getPlacementid(), loginIdStr);
                        tripStatusUpdate(requestModel);

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
            }
        });
*/
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

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TripAllottedListRowBinding binding;

        public MyViewHolder(@NonNull @NotNull TripAllottedListRowBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            binding = itemViewBinding;
        }
    }

    private void tripStatusUpdate(SetTripStatusUpdateModel mpinModel) {
        try {
            Call<VerifyMobileNoResponseModel> call = ApiClient.getNetworkService().setTripStatus(mpinModel);

            final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                    .content(R.string.loading)
                    .canceledOnTouchOutside(false)
                    .progress(true, 0)
                    .widgetColorRes(R.color.colorPrimary)
                    .show();

            call.enqueue(new Callback<VerifyMobileNoResponseModel>() {
                @Override
                public void onResponse(Call<VerifyMobileNoResponseModel> call, Response<VerifyMobileNoResponseModel> response) {
                    Log.d("tripUpdate","req "+mpinModel.toString()+" res "+response.code());
                    if (response.isSuccessful()) {
                        try {
                            if (response.body() != null && response.body().getVerifyMobileNoModels() != null &&
                                    !response.body().getVerifyMobileNoModels().isEmpty()) {
                                String messageStr = response.body().getVerifyMobileNoModels().get(0).getMESSAGE();
                                Log.e("statusss", messageStr);
                                if (messageStr.equalsIgnoreCase("Status updated Successfully")) {
                                    Toast.makeText(mCon, "Your trip has been started ! Please reach to start point.", Toast.LENGTH_LONG).show();
                                    //Intent mServiceIntent = new Intent(mCon, LocationAlarmScheduler.class);
                                    //mCon.startService(mServiceIntent);

                                    Intent intent_main = new Intent(mCon, UpcomingTripActivity.class);
                                    mCon.startActivity(intent_main);
                                    TripListAdapter.this.parentActivity.finish();
                                } else {
                                    Toast.makeText(mCon, "" + messageStr, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mCon, R.string.no_data, Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Intent intent_main = new Intent(mCon, UpcomingTripActivity.class);
                            mCon.startActivity(intent_main);
                            TripListAdapter.this.parentActivity.finish();
                        }
                    } else {
                        Toast.makeText(mCon, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                        Log.d("check", "onResponse Trip Status update: " + response.errorBody());
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<VerifyMobileNoResponseModel> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Toast.makeText(mCon, "Please check internet connection", Toast.LENGTH_SHORT).show();
                        Log.d("check", "onFailure: " + t.getMessage());
                    }
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("check", "setMPin: " + e.getMessage());
        }
    }


}

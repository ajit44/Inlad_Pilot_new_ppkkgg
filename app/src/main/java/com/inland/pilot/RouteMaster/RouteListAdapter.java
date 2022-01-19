package com.inland.pilot.RouteMaster;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import com.inland.pilot.Location.LocationAlarmScheduler;
import com.inland.pilot.Location.SetTripStatusUpdateModel;
import com.inland.pilot.Login.VerifyMobileNoResponseModel;
import com.inland.pilot.MyTrip.TripDetailsActivity;
import com.inland.pilot.MyTrip.TripMasterModel;
import com.inland.pilot.NavigationActivity;
import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.RouteListRowBinding;
import com.inland.pilot.databinding.TripAllottedListRowBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.MyViewHolder> {
    private Context mCon;
    private LayoutInflater inflater;
    private List<RouteMasterModel> data;
    private String loginIdStr;
    private String deviceIdStr, tokenNoStr;

    public RouteListAdapter(Context context) {
        mCon = context;
        data = new ArrayList<>();
        inflater = LayoutInflater.from(mCon);
    }

    public void addAll(List<RouteMasterModel> list) {
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
        RouteListRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.route_list_row, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        final RouteMasterModel current = data.get(position);
        final int pos_inner=position;
        holder.binding.routeIdValTextView.setText(current.getROUTE_ID());
        holder.binding.tripToFroTextView.setText(current.getSRCRTNAME()+" - " + current.getDSTRTNAME());

        holder.binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(mCon);
                dialogBuilder.setTitle("Confirmation");
                dialogBuilder.setMessage("Are you sure to delete the route?");
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
                        RouteMasterModel latest_pos = data.get(pos_inner);

                        RouteMasterModel requestModel = new RouteMasterModel("delete",latest_pos.getFSTATE_ID(), latest_pos.getSRCRTNAME(), latest_pos.getTSTATE_ID(),latest_pos.getDSTRTNAME(),latest_pos.getLoginId(), latest_pos.getROUTE_ID(), latest_pos.getDeviceId());
                        routeDelete(requestModel);

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

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RouteListRowBinding binding;

        public MyViewHolder(@NonNull @NotNull RouteListRowBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            binding = itemViewBinding;
        }
    }

    private void routeDelete(RouteMasterModel routeModel) {
        try {
            Call<VerifyMobileNoResponseModel> call = ApiClient.getNetworkService().addUpdateRouteDetails(routeModel);

            final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                    .content(R.string.loading)
                    .canceledOnTouchOutside(false)
                    .progress(true, 0)
                    .widgetColorRes(R.color.colorPrimary)
                    .show();

            call.enqueue(new Callback<VerifyMobileNoResponseModel>() {
                @Override
                public void onResponse(Call<VerifyMobileNoResponseModel> call, Response<VerifyMobileNoResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getVerifyMobileNoModels() != null &&
                                !response.body().getVerifyMobileNoModels().isEmpty()) {
                            String messageStr = response.body().getVerifyMobileNoModels().get(0).getMESSAGE();
                            Log.e("status",messageStr);
                            if (messageStr.equalsIgnoreCase("RECORD DELETED SUCCESSFULLY")) {
                                Toast.makeText(mCon, "Route list updated successfully", Toast.LENGTH_LONG).show();
                                for(int i=0;i<data.size();i++)
                                {
                                    if(data.get(i).getROUTE_ID()==routeModel.getROUTE_ID())
                                        data.remove(i);
                                }
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(mCon, "" + messageStr, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mCon, R.string.no_data, Toast.LENGTH_SHORT).show();
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

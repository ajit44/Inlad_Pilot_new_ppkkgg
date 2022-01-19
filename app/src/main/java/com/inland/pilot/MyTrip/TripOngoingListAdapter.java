package com.inland.pilot.MyTrip;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import com.inland.pilot.LoadImage.LoadUploadListActivity;
import com.inland.pilot.Location.LocationAlarmScheduler;
import com.inland.pilot.Location.SetTripStatusUpdateModel;
import com.inland.pilot.Login.VerifyMobileNoResponseModel;
import com.inland.pilot.NavigationActivity;
import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.TripAllottedListRowBinding;
import com.inland.pilot.databinding.TripOngoingListRowBinding;

public class TripOngoingListAdapter extends RecyclerView.Adapter<TripOngoingListAdapter.MyViewHolder> {
    private Context mCon;
    private LayoutInflater inflater;
    private List<TripMasterModel> data;
    private boolean reaching_to_final, reaching_to_start;

    public TripOngoingListAdapter(Context context) {
        mCon = context;
        data = new ArrayList<>();
        inflater = LayoutInflater.from(mCon);
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
        TripOngoingListRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.trip_ongoing_list_row, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        final TripMasterModel current = data.get(position);
        final int position_final=position;
        holder.binding.tripToFroTextView.setText(current.getUnloading_location()+" - " + current.getLoading_location());
        holder.binding.tripDateTextView.setText(current.getPlacementdt());
        holder.binding.tripTimeTextView.setVisibility(View.INVISIBLE);
        holder.binding.startLocTextView.setVisibility(View.INVISIBLE);
        holder.binding.endLocTextView.setVisibility(View.INVISIBLE);

        SharedPreferences preferences_shared = PreferenceManager.getDefaultSharedPreferences(mCon);
        reaching_to_start = preferences_shared.getBoolean("reaching_to_start",false);
     /*
        if(preferences_shared.getAll().containsKey("reaching_to_start") && reaching_to_start==false)
        {
            holder.binding.acceptBtn.setVisibility(View.GONE);
        }
        else
        {
            holder.binding.acceptBtn.setVisibility(View.VISIBLE);
        }

      */
        holder.binding.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences_shared = PreferenceManager.getDefaultSharedPreferences(mCon);
                boolean reaching_to_start = preferences_shared.getBoolean("reaching_to_start", false);
                if (reaching_to_start) {
                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(mCon);
                    dialogBuilder.setTitle("Confirmation");
                    dialogBuilder.setMessage("Proceed to Loading Image Upload ?");
                    dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            SharedPreferences preferences_shared = PreferenceManager.getDefaultSharedPreferences(mCon);
                            SharedPreferences.Editor editor = preferences_shared.edit();
                            editor.putBoolean("reaching_to_start", false);
                            editor.commit();

                            TripMasterModel current_trip = data.get(position_final);
                            Intent intent = new Intent(mCon, LoadUploadListActivity.class);
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
                    TripMasterModel current_trip = data.get(position_final);
                    Intent intent = new Intent(mCon, LoadUploadListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("TripId", current_trip.getPlacementid());
                    intent.putExtras(bundle);
                    mCon.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TripOngoingListRowBinding binding;

        public MyViewHolder(@NonNull @NotNull TripOngoingListRowBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            binding = itemViewBinding;
        }
    }
}

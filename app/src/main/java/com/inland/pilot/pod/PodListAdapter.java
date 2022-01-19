package com.inland.pilot.pod;

import static com.inland.pilot.MyTrip.UpcomingTripActivity.getDate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import com.inland.pilot.Location.SetTripStatusUpdateModel;
import com.inland.pilot.MyTrip.TripMasterModel;
import com.inland.pilot.MyTrip.UpcomingTripActivity;
import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.PodListRowBinding;
import com.inland.pilot.databinding.TripCompletedListRowBinding;
import com.inland.pilot.unloading.UnloadingImageCapture;

public class PodListAdapter extends RecyclerView.Adapter<PodListAdapter.MyViewHolder> {
    private Context mCon;
    private LayoutInflater inflater;
    private List<TripMasterModel> data;
    private PodListActivity parentActivity;

    public PodListAdapter(Context context, PodListActivity parentActivity) {
        mCon = context;
        this.parentActivity= parentActivity;
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
        PodListRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.pod_list_row, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        final int position_inner= position;
        final TripMasterModel current = data.get(position);

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

        //holder.binding.tripToFroTextView.setText(current.getTP_T_Location()+" - " + current.getTP_B_Location());
        //holder.binding.tripDateTextView.setText(current.getTP_Date());
        //holder.binding.tripTimeTextView.setText(current.getTP_Time());
        //holder.binding.startLocTextView.setText(current.getTP_SL_Address());
        //holder.binding.endLocTextView.setText(current.getTP_DL_Address());
        holder.binding.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TripMasterModel current_trip = data.get(position_inner);
                Intent intent = new Intent(mCon, PodImageCapture.class);
                Bundle bundle = new Bundle();
                bundle.putString("image_no", "1");
                bundle.putString("TripId", current_trip.getPlacementid());
                intent.putExtras(bundle);
                mCon.startActivity(intent);
                parentActivity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        PodListRowBinding binding;

        public MyViewHolder(@NonNull @NotNull PodListRowBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            binding = itemViewBinding;
        }
    }
}

package com.inland.pilot.MyTrip;

import static com.inland.pilot.MyTrip.UpcomingTripActivity.getDate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import com.inland.pilot.R;
import com.inland.pilot.databinding.TripCompletedListRowBinding;
import com.inland.pilot.databinding.TripOngoingListRowBinding;

public class TripCompletedListAdapter extends RecyclerView.Adapter<TripCompletedListAdapter.MyViewHolder> {
    private Context mCon;
    private LayoutInflater inflater;
    private List<TripMasterModel> data;

    public TripCompletedListAdapter(Context context) {
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
        TripCompletedListRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.trip_completed_list_row, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        final TripMasterModel current = data.get(position);

        holder.binding.tvTripId.setText("Trip ID: "+current.getPlacementid());
        holder.binding.tvSourceCityNstate.setText(current.getLoading_location());
        holder.binding.tvSourceAddress.setVisibility(View.INVISIBLE);
        holder.binding.tvDestCity.setText(current.getUnloading_location());
        holder.binding.tvDestAddress.setVisibility(View.INVISIBLE);
        holder.binding.tvAssignedTime.setVisibility(View.INVISIBLE);
        holder.binding.tvAssignedDate.setText(current.getPlacementdt());
        holder.binding.tvExpectedDate.setText(UpcomingTripActivity.getDate(current.getPlacementdt()));


     /*   holder.binding.tripToFroTextView.setText(current.getTP_T_Location()+" - " + current.getTP_B_Location());
        holder.binding.tripDateTextView.setText(current.getTP_Date());
        holder.binding.tripTimeTextView.setText(current.getTP_Time());
        holder.binding.startLocTextView.setText(current.getTP_SL_Address());
        holder.binding.endLocTextView.setText(current.getTP_DL_Address());
*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TripCompletedListRowBinding binding;

        public MyViewHolder(@NonNull @NotNull TripCompletedListRowBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            binding = itemViewBinding;
        }
    }


}

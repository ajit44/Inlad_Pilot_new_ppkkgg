package com.inland.pilot.VehicleMaster;

import android.content.Context;
import android.content.Intent;
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
import com.inland.pilot.databinding.VehicleListRowBinding;

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.MyViewHolder> {
    private Context mCon;
    private LayoutInflater inflater;
    private List<VehicleMasterModel> data;

    public VehicleListAdapter(Context context) {
        mCon = context;
        data = new ArrayList<>();
        inflater = LayoutInflater.from(mCon);
    }

    public void addAll(List<VehicleMasterModel> list) {
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
        VehicleListRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.vehicle_list_row, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        final VehicleMasterModel current = data.get(position);

        holder.binding.vehicleTypeTextView.setText(current.getVehicleType());
        holder.binding.vehicleNoTextView.setText(current.getVehicleNo());
        holder.binding.modelNoTextView.setText(current.getModelno());

        holder.binding.editVehicleDetailsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, AddUpdateVehicleDetailsActivity.class);
                intent.putExtra("typeStr", "update");
                intent.putExtra("ID",current.getID());
                intent.putExtra("VehicleNo",current.getVehicleNo());
                intent.putExtra("VehicleType",current.getVehicleType());
                intent.putExtra("Modelno",current.getModelno());
                intent.putExtra("Make_no",current.getMake_no());
                intent.putExtra("Chassis_no",current.getChassis_no());
                intent.putExtra("Engine_no",current.getEngine_no());
                intent.putExtra("Fitness_Date",current.getFitness_Date());
                intent.putExtra("Registry_data",current.getRegistry_data());
                intent.putExtra("Weight_capacity",current.getWeight_capacity());
                intent.putExtra("RC_Book_no",current.getRC_Book_no());
                intent.putExtra("Free_Out_Space",current.getFree_Out_Space());
                intent.putExtra("Vehicle_Space",current.getVehicle_Space());
                intent.putExtra("Insurance_Details",current.getInsurance_Details());
                intent.putExtra("Insurance_Valid_tilldate",current.getInsurance_Valid_tilldate());
                intent.putExtra("RT_No",current.getRT_No());
                intent.putExtra("Vehicle_register_State",current.getVehicle_register_State());
                intent.putExtra("Remark",current.getRemark());

                mCon.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        VehicleListRowBinding binding;

        public MyViewHolder(@NonNull @NotNull VehicleListRowBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            binding = itemViewBinding;
        }
    }
}

package com.inland.pilot.BankDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.inland.pilot.R;
import com.inland.pilot.databinding.BankRowBinding;

public class BankDetailsAdapter extends RecyclerView.Adapter<BankDetailsAdapter.MyViewHolder> {
    private Context mCon;
    private LayoutInflater inflater;
    private List<BankDetailsModel> data;

    public BankDetailsAdapter(Context context) {
        mCon = context;
        data = new ArrayList<>();
        inflater = LayoutInflater.from(mCon);
    }

    public void addAll(List<BankDetailsModel> list) {
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
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BankRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.bank_row, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final BankDetailsModel current = data.get(position);

        holder.binding.bankNameTextView.setText(current.getBankName());
        holder.binding.accountNoTextView.setText(current.getAccountNo());
        holder.binding.ifscCodeTextView.setText(current.getIfscCode());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        BankRowBinding binding;

        public MyViewHolder(@NonNull BankRowBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            binding = itemViewBinding;
        }
    }
}

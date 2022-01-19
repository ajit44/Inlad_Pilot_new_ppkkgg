package com.inland.pilot.MyTrip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.inland.pilot.R;
import com.inland.pilot.databinding.FragmentMyTripBinding;

public class MyTripFragment extends Fragment {
    private FragmentMyTripBinding binding;
    private Context mCon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_trip, container, false);

        mCon = getActivity();

        binding.addTripFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mCon, AddTripActivity.class));
            }
        });

        return binding.getRoot();
    }
}
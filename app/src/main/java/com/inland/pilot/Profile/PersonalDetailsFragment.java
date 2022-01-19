package com.inland.pilot.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.FragmentPersonalDetailsBinding;

public class PersonalDetailsFragment extends Fragment {
    private FragmentPersonalDetailsBinding binding;
    private Context mCon;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal_details, container, false);

        mCon = getActivity();

        if (PreferenceUtil.getUser() != null) {
            if (PreferenceUtil.getUser().getNAME() != null && !PreferenceUtil.getUser().getNAME().isEmpty()) {
                binding.nameTextView.setText(PreferenceUtil.getUser().getNAME());
            } else {
                binding.nameTextView.setText("----");
            }
            if (PreferenceUtil.getUser().getP_MOBILENO() != null && !PreferenceUtil.getUser().getP_MOBILENO().isEmpty()) {
                binding.mobileNoTextView.setText(PreferenceUtil.getUser().getP_MOBILENO());
            } else {
                binding.mobileNoTextView.setText("----");
            }
            if (PreferenceUtil.getUser().getADDRESS() != null && !PreferenceUtil.getUser().getADDRESS().isEmpty()) {
                binding.addressTextView.setText(PreferenceUtil.getUser().getADDRESS());
            } else {
                binding.addressTextView.setText("----");
            }
            if (PreferenceUtil.getUser().getPANNO() != null && !PreferenceUtil.getUser().getPANNO().isEmpty()) {
                binding.panNoTextView.setText(PreferenceUtil.getUser().getPANNO());
            } else {
                binding.panNoTextView.setText("----");
            }
            if (PreferenceUtil.getUser().getAADHARNO() != null && !PreferenceUtil.getUser().getAADHARNO().isEmpty()) {
                binding.aadharNoTextView.setText(PreferenceUtil.getUser().getAADHARNO());
            } else {
                binding.aadharNoTextView.setText("----");
            }
        } else {
            binding.nameTextView.setText("----");
            binding.mobileNoTextView.setText("----");
            binding.addressTextView.setText("----");
            binding.panNoTextView.setText("----");
            binding.aadharNoTextView.setText("----");
        }

        return binding.getRoot();
    }
}
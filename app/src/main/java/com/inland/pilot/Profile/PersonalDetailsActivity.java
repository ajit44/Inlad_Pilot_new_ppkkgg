package com.inland.pilot.Profile;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.FragmentPersonalDetailsBinding;

public class PersonalDetailsActivity extends AppCompatActivity {
    private FragmentPersonalDetailsBinding binding;
    private Context mCon;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_personal_details);

        mCon = this;

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
    }
}
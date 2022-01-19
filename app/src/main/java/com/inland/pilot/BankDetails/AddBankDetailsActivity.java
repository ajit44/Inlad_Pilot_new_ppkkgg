package com.inland.pilot.BankDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.inland.pilot.Login.VerifyMobileNoResponseModel;
import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.RouteMaster.RouteMasterModel;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.ActivityAddBankDetailsBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBankDetailsActivity extends AppCompatActivity {
    private ActivityAddBankDetailsBinding binding;
    private Context mCon;
    private String bankNameStr, accountNoStr, ifscCodeStr;
    private String loginIdStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_bank_details);

        mCon = this;
        if (PreferenceUtil.getUser() != null) {
            if (PreferenceUtil.getUser().getP_MOBILENO() != null && !PreferenceUtil.getUser().getP_MOBILENO().isEmpty()) {
                loginIdStr = PreferenceUtil.getUser().getP_MOBILENO();
            }
        }
        binding.bankNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    binding.bankNameEditText.setError(null);
                    binding.bankNameTextLayout.setError(null);
                    binding.bankNameTextLayout.setErrorEnabled(false);
                } else {
                    //binding.nameEditText.setError(getString(R.string.enter_name));
                    Toast.makeText(mCon, "" + getResources().getString(R.string.enter_bank_name), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.accountNoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    binding.accountNoEditText.setError(null);
                    binding.accountNoTextLayout.setError(null);
                    binding.accountNoTextLayout.setErrorEnabled(false);
                } else {
                    //binding.nameEditText.setError(getString(R.string.enter_name));
                    Toast.makeText(mCon, "" + getResources().getString(R.string.enter_account_no), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.ifscCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    binding.ifscCodeEditText.setError(null);
                    binding.ifscCodeTextLayout.setError(null);
                    binding.ifscCodeTextLayout.setErrorEnabled(false);
                } else {
                    //binding.nameEditText.setError(getString(R.string.enter_name));
                    Toast.makeText(mCon, "" + getResources().getString(R.string.enter_ifsc_code), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.addBankDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bankNameStr = binding.bankNameEditText.getText().toString().trim();
                accountNoStr = binding.accountNoEditText.getText().toString().trim();
                ifscCodeStr = binding.ifscCodeEditText.getText().toString().trim();

                validate();
            }
        });
    }

    private void validate() {
        boolean isValidBankName = false, isValidAccountNo = false, isValidIfscCode = false;

        if (TextUtils.isEmpty(bankNameStr)) {
            binding.bankNameTextLayout.setError(getResources().getString(R.string.enter_bank_name));
        } else {
            binding.bankNameTextLayout.setError(null);
            binding.bankNameTextLayout.setErrorEnabled(false);
            isValidBankName = true;
        }

        if (TextUtils.isEmpty(accountNoStr)) {
            binding.accountNoTextLayout.setError(getResources().getString(R.string.enter_bank_name));
        } else {
            binding.accountNoTextLayout.setError(null);
            binding.accountNoTextLayout.setErrorEnabled(false);
            isValidAccountNo = true;
        }

        if (TextUtils.isEmpty(ifscCodeStr)) {
            binding.ifscCodeTextLayout.setError(getResources().getString(R.string.enter_bank_name));
        } else {
            binding.ifscCodeTextLayout.setError(null);
            binding.ifscCodeTextLayout.setErrorEnabled(false);
            isValidIfscCode = true;
        }

        if (isValidBankName && isValidAccountNo && isValidIfscCode) {
            BankDetailsAddModel model=new BankDetailsAddModel("Insert", bankNameStr,accountNoStr,ifscCodeStr,loginIdStr,"1");
            addBankDetails(model,"");
        }
    }

    private void addBankDetails(BankDetailsAddModel bankMasterModel, String typeStr) {
        try {
            Log.e("route",bankMasterModel.toString());
            Call<VerifyMobileNoResponseModel> call = ApiClient.getNetworkService().addUpdateBankDetails(bankMasterModel);

            call.enqueue(new Callback<VerifyMobileNoResponseModel>() {
                @Override
                public void onResponse(Call<VerifyMobileNoResponseModel> call, Response<VerifyMobileNoResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getVerifyMobileNoModels() != null &&
                                !response.body().getVerifyMobileNoModels().isEmpty()) {
                            String messageStr = response.body().getVerifyMobileNoModels().get(0).getMESSAGE().trim();
                            Log.e("messagestr", response.body().getVerifyMobileNoModels().toString());

                            if (messageStr.equalsIgnoreCase("RECORD SAVED SUCCESSFULLY")) {
                                Toast.makeText(mCon, "RECORD ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                                finish();

                            } else {
                                Toast.makeText(mCon, "" + messageStr, Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(mCon, R.string.no_data, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mCon, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                        Log.d("check", "onResponse addUpdateDetails: " + mCon.getResources().getString(R.string.something_went_wrong));
                    }
                }

                @Override
                public void onFailure(Call<VerifyMobileNoResponseModel> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Toast.makeText(mCon, "Please check internet connection", Toast.LENGTH_SHORT).show();
                        Log.d("check", "onFailure addUpdateDetails: " + t.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("check", "addUpdateDetails: " + e.getMessage());
        }
    }
}
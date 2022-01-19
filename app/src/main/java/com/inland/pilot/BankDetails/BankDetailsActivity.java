package com.inland.pilot.BankDetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.RouteMaster.RouteListModel;
import com.inland.pilot.RouteMaster.RouteMasterModel;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.FragmentBankDetailsBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankDetailsActivity extends AppCompatActivity {
    private FragmentBankDetailsBinding binding;
    private Context mCon;
    private BankDetailsAdapter bankDetailsAdapter;
    private List<BankDetailsModel> bankDetailsModelList = new ArrayList<>();
    private String loginIdStr;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_bank_details);

        mCon = this;

        if (PreferenceUtil.getUser() != null) {
            if (PreferenceUtil.getUser().getP_MOBILENO() != null && !PreferenceUtil.getUser().getP_MOBILENO().isEmpty()) {
                loginIdStr = PreferenceUtil.getUser().getP_MOBILENO();
            }
        }
        bankDetailsAdapter = new BankDetailsAdapter(mCon);

        binding.bankDetailsRecyclerView.setHasFixedSize(true);
        binding.bankDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(mCon));
        binding.bankDetailsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        binding.addBankDetailsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCon, AddBankDetailsActivity.class);
                startActivity(intent);
            }
        });

        BankDetailsModel model=new BankDetailsModel("","","", loginIdStr);
        getBankList(model);
        //setData();

    }
/*
    private void setData() {

        BankDetailsModel data = new BankDetailsModel("ICICI Bank", "35441456841696", "ICIC0007707");
        bankDetailsModelList.add(data);

    //    data = new BankDetailsModel("SBI Bank", "85212646439723", "SBIN0004666");
   //     bankDetailsModelList.add(data);

   //     data = new BankDetailsModel("HDFC Bank", "25786164465770", "HDFC0000522");
   //     bankDetailsModelList.add(data);

        bankDetailsAdapter.addAll(bankDetailsModelList);
        binding.bankDetailsRecyclerView.setAdapter(bankDetailsAdapter);


    }

 */
    private void getBankList(BankDetailsModel requestBankListModel) {
        try {
            Log.e("request",requestBankListModel.toString()+"");
            Call<BankListModel> call = ApiClient.getNetworkService().getBankListAll(requestBankListModel);

            final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                    .title(R.string.loading)
                    .content(R.string.loading)
                    .canceledOnTouchOutside(false)
                    .progress(true, 0)
                    .widgetColorRes(R.color.colorPrimary)
                    .show();

            call.enqueue(new Callback<BankListModel>() {
                @Override
                public void onResponse(Call<BankListModel> call, Response<BankListModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getBankListModels() != null &&
                                !response.body().getBankListModels().isEmpty()) {

                            bankDetailsModelList = response.body().getBankListModels();
                            Log.e("model",bankDetailsModelList.toString());
                            bankDetailsAdapter.addAll(bankDetailsModelList);
                            binding.bankDetailsRecyclerView.setVisibility(View.VISIBLE);
                            binding.errorTextView.setVisibility(View.GONE);
                            binding.bankDetailsRecyclerView.setAdapter(bankDetailsAdapter);
                        } else {
                            binding.bankDetailsRecyclerView.setVisibility(View.GONE);
                            binding.errorTextView.setVisibility(View.VISIBLE);
                            Toast.makeText(mCon, "No bank details found !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        binding.bankDetailsRecyclerView.setVisibility(View.GONE);
                        binding.errorTextView.setVisibility(View.VISIBLE);
                        Toast.makeText(mCon, " " + mCon.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<BankListModel> call, Throwable t) {
                    if (!call.isCanceled()) {
                        binding.bankDetailsRecyclerView.setVisibility(View.GONE);
                        binding.errorTextView.setVisibility(View.VISIBLE);
                        Toast.makeText(mCon, " " + mCon.getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
                        Log.d("check", "onFailure: " + t.getMessage());
                    }
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("check", "getNotificationList: " + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BankDetailsModel model=new BankDetailsModel("","","", loginIdStr);
        getBankList(model);
    }
}
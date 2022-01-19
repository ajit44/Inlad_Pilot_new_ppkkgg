package com.inland.pilot.OriginOfLoads;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import com.inland.pilot.Network.ApiClient;
import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OriginOfLoadsActivity extends AppCompatActivity {

    RecyclerView rvStates;
    ArrayList<String> stateList;
    ArrayList<StateModel> stateModel;
    private Context mCon;
    AdapterOriginStats adapterState;
    String loginIdStr;
    ArrayList<Integer> stateContentCountList;
    RequestLoadIntimationModel requestLoadIntimationModel;
    private List<LoadDetailsModel> loadDetailsModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_origin_of_loads);

        mCon=this;
        rvStates = findViewById(R.id.rvStateList);
        rvStates.setHasFixedSize(true);
        rvStates.setLayoutManager(new LinearLayoutManager(mCon));
        rvStates.setItemAnimator(new DefaultItemAnimator());
        stateList = new ArrayList<>();
        stateModel = new ArrayList<>();
        stateContentCountList = new ArrayList<>();
        if (PreferenceUtil.getUser() != null) {
            if (PreferenceUtil.getUser().getP_MOBILENO() != null && !PreferenceUtil.getUser().getP_MOBILENO().isEmpty()) {
                loginIdStr = PreferenceUtil.getUser().getP_MOBILENO();
                //tokenNoStr= PreferenceUtil.getTokenNo();
                requestLoadIntimationModel = new RequestLoadIntimationModel(loginIdStr);
                getLoadIntimationList(requestLoadIntimationModel);
            }
        }

         adapterState = new AdapterOriginStats(OriginOfLoadsActivity.this, stateModel);
        rvStates.setAdapter(adapterState);
    }

    private void getLoadIntimationList(RequestLoadIntimationModel requestLoadIntimationModel) {
        try {
            stateList.clear();
            Call<LoadIntimationModel> call = ApiClient.getNetworkService().getLoadIntimation(requestLoadIntimationModel);

            final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                    .title(R.string.loading)
                    .content(R.string.loading)
                    .canceledOnTouchOutside(false)
                    .progress(true, 0)
                    .widgetColorRes(R.color.colorPrimary)
                    .show();

            call.enqueue(new Callback<LoadIntimationModel>() {
                @Override
                public void onResponse(Call<LoadIntimationModel> call, Response<LoadIntimationModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getLoadIntimationModel() != null &&
                                !response.body().getLoadIntimationModel().isEmpty()) {

                            int isExist=-1;
                            loadDetailsModels = response.body().getLoadIntimationModel();
                            stateList.add(loadDetailsModels.get(0).getFromStatecode());

                            for(LoadDetailsModel model : loadDetailsModels) {

                                isExist = stateList.indexOf(model.getFromStatecode());
                                if (isExist !=-1 && stateModel.size() !=0) {
                                    StateModel st = stateModel.get(isExist);
                                    st.verifyDetails(model);
                                    stateModel.set(isExist, st);

                                } else stateModel.add(new StateModel(model.getFromStatecode(), model));
                            }

                                adapterState.notifyDataSetChanged();

                            Log.d("datasss", loadDetailsModels.toString());
                     /*      tripList                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        Adapter.addAll(tripMasterModelList);
                            binding.tripListRecycler.setVisibility(View.VISIBLE);
                            binding.errorTextView.setVisibility(View.GONE);
                            binding.tripListRecycler.setAdapter(tripListAdapter);*/
                        } else {
                        //    binding.tripListRecycler.setVisibility(View.GONE);
                        //    binding.errorTextView.setVisibility(View.VISIBLE);
                            Toast.makeText(mCon, "No Upcoming trip found !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                     //   binding.tripListRecycler.setVisibility(View.GONE);
                      //  binding.errorTextView.setVisibility(View.VISIBLE);
                        Toast.makeText(mCon, " " + mCon.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<LoadIntimationModel> call, Throwable t) {
                    if (!call.isCanceled()) {
                    //    binding.tripListRecycler.setVisibility(View.GONE);
                     //   binding.errorTextView.setVisibility(View.VISIBLE);
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

}
package com.inland.pilot.LoadIntemationNew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.inland.pilot.ApiConstants;
import com.inland.pilot.LoadIntemationNew.Model.LoadState;
import com.inland.pilot.OriginOfLoads.AdapterOriginStats;
import com.inland.pilot.OriginOfLoads.OriginOfLoadsActivity;
import com.inland.pilot.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoadStateListActivity extends AppCompatActivity {

    RecyclerView rvStateList;
    AdapterOriginStats_new adapterState;
    List<LoadState> StateList;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_state_list);

        rvStateList = findViewById(R.id.rvStateList);
        progressBar = findViewById(R.id.progressBar);
        rvStateList.setHasFixedSize(true);
        rvStateList.setLayoutManager(new LinearLayoutManager(this));
        rvStateList.setItemAnimator(new DefaultItemAnimator());
        StateList = new ArrayList<>();


        try {
            fetchState();
        } catch (Exception e) {
            Log.e("stated1",e.toString());
        }
        adapterState = new AdapterOriginStats_new(LoadStateListActivity.this, StateList);
        rvStateList.setAdapter(adapterState);
    }

    void fetchState() throws JSONException {
        progressBar.setVisibility(View.VISIBLE);
        final JSONObject request = new JSONObject();
        request.put("UserID","18519");
        request.put("StateCode","DL");
        request.put("MobileNo","");

        AndroidNetworking.post(ApiConstants.Api_GetPendingLoadStateWise)
                .addJSONObjectBody(request)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            StateList.clear();
                            Log.e("stated1",response.toString());


                            if(!(0>=response.length())){
                                for(int i=0;i<response.length();i++)
                                {
                                    JSONObject  WHA1=response.getJSONObject(i);
                                    LoadState st = new LoadState(WHA1.getString("StateCode"),
                                                             WHA1.getString("StateName"),
                                                             WHA1.getString("PendingLoad"));
                                    StateList.add(st);
                                    Log.e("stated",WHA1.toString());

                                }
                                progressBar.setVisibility(View.GONE);
                                adapterState.notifyDataSetChanged();
                            }else Toast.makeText(LoadStateListActivity.this, "No Data found", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.e("statedhd",e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ERROR", anError.getMessage());
                        Toast.makeText(LoadStateListActivity.this, "errr "+anError, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
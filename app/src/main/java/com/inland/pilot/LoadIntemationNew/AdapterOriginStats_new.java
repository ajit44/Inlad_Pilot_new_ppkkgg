package com.inland.pilot.LoadIntemationNew;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.inland.pilot.ApiConstants;
import com.inland.pilot.LoadIntemationNew.Model.LoadCity;
import com.inland.pilot.LoadIntemationNew.Model.LoadState;
import com.inland.pilot.OriginOfLoads.AdapterOriginCitys;
import com.inland.pilot.OriginOfLoads.CityModel;
import com.inland.pilot.OriginOfLoads.StateModel;
import com.inland.pilot.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdapterOriginStats_new extends RecyclerView.Adapter<AdapterOriginStats_new.ViewHolder> {

    Context context;
    List<LoadState> StateList;
    List<LoadCity> cityList;

    public AdapterOriginStats_new(Context context, List<LoadState> State) {
        this.context = context;

        this.context = context;
        this.StateList =State;
        cityList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v =  LayoutInflater.from(context).inflate(R.layout.layout_state_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.tvName.setText(StateList.get(position).getStateName());
        holder.tvCount.setText(StateList.get(position).getPendingLoad()+"");///////////////////// set city count

        holder.layoutState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.layoutCity.getVisibility()== View.VISIBLE) {
                    holder.layoutCity.setVisibility(View.GONE);
                    holder.imgDropdown.animate().rotationBy(180f).start();
                }else {
                    holder.imgDropdown.animate().rotationBy(180f).start();
                    holder.layoutCity.setVisibility(View.VISIBLE);
                    //holder.adapterCity = new AdapterOriginCitys(context,StateList.get(position).getCityList());
                    try {
                        fetchState(holder.progressBar, holder.rvCityList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                   // holder.rvCityList.setAdapter(holder.adapterCity);
                }
            }
        });





    }

    private void cityVisiblity(RelativeLayout layoutCity, ImageView imgDropdown, AdapterOriginCitys adapterCity,ArrayList<CityModel> cityList) {

    }

    @Override
    public int getItemCount() {
        return StateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public TextView tvName,tvCount;
        ImageView imgDropdown;
        ConstraintLayout layoutState;
        RelativeLayout layoutCity;
        RecyclerView rvCityList;
        AdapterOriginStats_new adapterCity;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCount = itemView.findViewById(R.id.tvCount);
            imgDropdown = itemView.findViewById(R.id.img_dropdown);
            layoutState = itemView.findViewById(R.id.layout_state_name_area);
            layoutCity = itemView.findViewById(R.id.layout_city_area);
            rvCityList = itemView.findViewById(R.id.rvCityList);
            progressBar = itemView.findViewById(R.id.progressBar);

            rvCityList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        }

    }

    void fetchState(ProgressBar progressBar, RecyclerView rvCityList) throws JSONException {
        progressBar.setVisibility(View.VISIBLE);
        final JSONObject request = new JSONObject();
        request.put("UserID","18519");
        request.put("StateCode","DL");
        request.put("MobileNo","");

        AndroidNetworking.post(ApiConstants.Api_GetPendingLoadCityWise)
                .addJSONObjectBody(request)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            cityList.clear();
                            Log.e("stated1",response.toString());


                            if(!(0>=response.length())){
                                for(int i=0;i<response.length();i++)
                                {
                                    JSONObject  WHA1=response.getJSONObject(i);
                                    LoadCity st = new LoadCity(WHA1.getString("CityCode"),
                                            WHA1.getString("CityName"),
                                            WHA1.getString("PendingLoad"));
                                    cityList.add(st);
                                    Log.e("stated",WHA1.toString());

                                }
                                progressBar.setVisibility(View.GONE);
                                AdapterOriginCitys_new ad = new AdapterOriginCitys_new(context, cityList);
                                rvCityList.setAdapter(ad);
                            }else Toast.makeText(context, "No Data found", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.e("statedhd",e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ERROR", anError.getMessage());
                        Toast.makeText(context, "errr "+anError, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}



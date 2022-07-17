package com.inland.pilot.LoadIntemationNew;

import android.annotation.SuppressLint;
import android.content.Context;
import android.transition.TransitionManager;
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
import com.google.gson.Gson;
import com.inland.pilot.ApiConstants;
import com.inland.pilot.LoadIntemationNew.Model.CityLoadDetails;
import com.inland.pilot.LoadIntemationNew.Model.LoadCity;
import com.inland.pilot.Login.LoginDetailsModel;
import com.inland.pilot.OriginOfLoads.AdapterOriginTracks;
import com.inland.pilot.OriginOfLoads.CityModel;
import com.inland.pilot.R;
import com.inland.pilot.Util.PreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdapterOriginCitys_new extends RecyclerView.Adapter<AdapterOriginCitys_new.ViewHolder> {

    Context context;
    List<LoadCity> cityList;
    List<CityLoadDetails> cityLoadDetailsList;
    LoginDetailsModel loginDetailsModel;
    String loginMobileNoStr;


    public AdapterOriginCitys_new(Context context, List<LoadCity> testCity) {
        this.context = context;

        this.context = context;
        this.cityList=testCity;
        cityLoadDetailsList = new ArrayList<>();

        loginDetailsModel = PreferenceUtil.getUser();

        if (loginDetailsModel.getP_MOBILENO() != null && !loginDetailsModel.getP_MOBILENO().isEmpty()) {
            loginMobileNoStr = loginDetailsModel.getP_MOBILENO();
        } else {
            loginMobileNoStr = "";
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v =  LayoutInflater.from(context).inflate(R.layout.layout_origan_city_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.layoutTrack.setVisibility(View.GONE);

        holder.tvCityName.setText(cityList.get(position).getCityName());
        holder.tvCount.setText(cityList.get(position).getPendingLoad()+"");///////////////////// set city count
        holder.progressBar.setVisibility(View.INVISIBLE);
        holder.layoutCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(holder.transactionContainer);
                if(holder.layoutTrack.getVisibility()== View.VISIBLE) {
                    holder.imgDropdown.animate().rotationBy(45f).start();
                    holder.layoutTrack.setVisibility(View.GONE);

                }else {
                   holder.imgDropdown.animate().rotationBy(45f).start();
                    holder.layoutTrack.setVisibility(View.VISIBLE);
                    //holder.adapterTracks = new AdapterOriginTracks(context,cityList.get(position).getTrackList());
                    //holder.rvTracksList.setAdapter(holder.adapterTracks);
                    try {
                        fetchDetails(holder.progressBar, holder.rvTracksList,cityList.get(position).getCityCode());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });





    }



    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public TextView tvCityName,tvCount;
        ConstraintLayout layoutCity;
        ImageView imgDropdown;
        ViewGroup transactionContainer;
        RelativeLayout layoutTrack;
        RecyclerView rvTracksList;
        AdapterOriginTracks adapterTracks;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCityName = itemView.findViewById(R.id.tvCityName);
            tvCount = itemView.findViewById(R.id.tvCount);
            transactionContainer = itemView.findViewById(R.id.transactionContainer);
            imgDropdown = itemView.findViewById(R.id.img_dropdown);
            layoutCity = itemView.findViewById(R.id.layout_city_name_area);
            layoutTrack = itemView.findViewById(R.id.layout_track_area);
            progressBar = itemView.findViewById(R.id.progressBar);

            rvTracksList = itemView.findViewById(R.id.rvTracksList);
            rvTracksList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));



        }

    }

    void fetchDetails(ProgressBar progressBar, RecyclerView rvCityList, String cityCode) throws JSONException {
        progressBar.setVisibility(View.VISIBLE);
        Gson gson = new Gson();
        final JSONObject request = new JSONObject();
        request.put("UserID",loginMobileNoStr);
        request.put("CityCode",cityCode);

        Log.e("fetchDetails",request.toString() );
        AndroidNetworking.post(ApiConstants.Api_GetPendingLoadDtlCityWise)
                .addJSONObjectBody(request)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            cityLoadDetailsList.clear();
                            Log.e("stated1",response.toString());
                            progressBar.setVisibility(View.INVISIBLE);

                            if(!(0>=response.length())){
                                for(int i=0;i<response.length();i++)
                                {
                                    CityLoadDetails details = gson.fromJson(String.valueOf(response.getJSONObject(i)), CityLoadDetails.class);
                                    cityLoadDetailsList.add(details);
                                    Log.e("datta",details.getLoadDate());
                                   /* JSONObject  WHA1=response.getJSONObject(i);
                                    LoadCity st = new LoadCity(WHA1.getString("CityCode"),
                                            WHA1.getString("CityName"),
                                            WHA1.getString("PendingLoad"));
                                    cityList.add(st);
                                    Log.e("stated",WHA1.toString());*/

                                }
                                //progressBar.setVisibility(View.GONE);
                                AdapterOriginTracks_new ad = new AdapterOriginTracks_new(context, cityLoadDetailsList);
                                rvCityList.setAdapter(ad);
                            }else Toast.makeText(context, "No Data found", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.e("statedhd",e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.e("ERROR", anError.getMessage());
                        Toast.makeText(context, "errr "+anError, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}



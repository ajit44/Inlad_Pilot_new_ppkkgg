package com.inland.pilot.LoadIntemationNew;

import android.content.Context;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inland.pilot.LoadIntemationNew.Model.LoadCity;
import com.inland.pilot.OriginOfLoads.AdapterOriginTracks;
import com.inland.pilot.OriginOfLoads.CityModel;
import com.inland.pilot.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterOriginCitys_new extends RecyclerView.Adapter<AdapterOriginCitys_new.ViewHolder> {

    Context context;
    List<LoadCity> cityList;


    public AdapterOriginCitys_new(Context context, List<LoadCity> testCity) {
        this.context = context;

        this.context = context;
        this.cityList=testCity;

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
                    holder.rvTracksList.setAdapter(holder.adapterTracks);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCityName = itemView.findViewById(R.id.tvCityName);
            tvCount = itemView.findViewById(R.id.tvCount);
            transactionContainer = itemView.findViewById(R.id.transactionContainer);
            imgDropdown = itemView.findViewById(R.id.img_dropdown);
            layoutCity = itemView.findViewById(R.id.layout_city_name_area);
            layoutTrack = itemView.findViewById(R.id.layout_track_area);


            rvTracksList = itemView.findViewById(R.id.rvTracksList);
            rvTracksList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));



        }

    }
}



package com.inland.pilot.OriginOfLoads;

import android.content.Context;
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


import java.util.ArrayList;
import java.util.List;

import com.inland.pilot.R;

public class AdapterOriginStats extends RecyclerView.Adapter<AdapterOriginStats.ViewHolder> {

    Context context;
    List<StateModel> StateList;

    public AdapterOriginStats(Context context, List<StateModel> State) {
        this.context = context;

        this.context = context;
        this.StateList =State;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v =  LayoutInflater.from(context).inflate(R.layout.layout_state_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.tvName.setText(StateList.get(position).getStateCode());
        holder.tvCount.setText(StateList.get(position).getCount()+"");///////////////////// set city count

        holder.layoutState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.layoutCity.getVisibility()== View.VISIBLE) {
                    holder.layoutCity.setVisibility(View.GONE);
                    holder.imgDropdown.animate().rotationBy(180f).start();
                }else {
                    holder.imgDropdown.animate().rotationBy(180f).start();
                    holder.layoutCity.setVisibility(View.VISIBLE);
                    holder.adapterCity = new AdapterOriginCitys(context,StateList.get(position).getCityList());
                    holder.rvCityList.setAdapter(holder.adapterCity);
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
        AdapterOriginCitys adapterCity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCount = itemView.findViewById(R.id.tvCount);
            imgDropdown = itemView.findViewById(R.id.img_dropdown);
            layoutState = itemView.findViewById(R.id.layout_state_name_area);
            layoutCity = itemView.findViewById(R.id.layout_city_area);
            rvCityList = itemView.findViewById(R.id.rvCityList);

            rvCityList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        }

    }
}



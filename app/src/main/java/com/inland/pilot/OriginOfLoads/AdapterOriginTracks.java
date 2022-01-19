package com.inland.pilot.OriginOfLoads;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.inland.pilot.R;

public class AdapterOriginTracks extends RecyclerView.Adapter<AdapterOriginTracks.ViewHolder> {

    Context context;
    ArrayList<LoadDetailsModel> trackList;


    public AdapterOriginTracks(Context context, ArrayList<LoadDetailsModel> trackList) {
        this.context = context;

        this.trackList = trackList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v =  LayoutInflater.from(context).inflate(R.layout.layout_origan_track_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LoadDetailsModel data = trackList.get(position);

        holder.tvLoadRef.setText("Load Ref: " +  data.getLoadID());
        holder.tvSourceCity.setText(data.getFromCityName());
        holder.tvSourceStateCode.setText(data.getFromStatecode());
        holder.tvDestCity.setText(data.getToCityName());
        holder.tvDestStateCode.setText(data.getToStatecode());
        holder.tvWeight.setText("Open | "+data.getPayloaddesc());
        holder.tvDocketContain.setText(data.getProductName());
        holder.tvDate.setText(getDate(data.getLoadDate()));


    }

    public static String getDate(String dt){
        String arr[];
        if(dt.contains("T")) {
            arr = dt.split("T");
            return arr[0];
        }
        return dt;
    }


    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public TextView tvSourceCity,tvSourceStateCode;
        public TextView tvLoadRef,tvDestCity,tvDestStateCode;
        public TextView tvWeight,tvDocketContain,tvDate;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSourceCity = itemView.findViewById(R.id.tv_source_city);
            tvSourceStateCode = itemView.findViewById(R.id.tv_source_state_code);
            tvLoadRef = itemView.findViewById(R.id.tv_load_ref);
            tvDestCity = itemView.findViewById(R.id.tv_dest_city);
            tvDestStateCode = itemView.findViewById(R.id.tv_dest_state_code);
            tvWeight = itemView.findViewById(R.id.tv_weight);
            tvDocketContain = itemView.findViewById(R.id.tv_docket_contain);
            tvDate = itemView.findViewById(R.id.tv_date);




        }

    }
}



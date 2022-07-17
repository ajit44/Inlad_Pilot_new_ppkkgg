package com.inland.pilot.LoadIntemationNew;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.inland.pilot.LoadIntemationNew.Model.CityLoadDetails;
import com.inland.pilot.OriginOfLoads.LoadDetailsModel;
import com.inland.pilot.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterOriginTracks_new extends RecyclerView.Adapter<AdapterOriginTracks_new.ViewHolder> {

    Context context;
    List<CityLoadDetails> trackList;


    public AdapterOriginTracks_new(Context context, List<CityLoadDetails> trackList) {
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

        CityLoadDetails data = trackList.get(position);

        holder.tvLoadRef.setText("Load Ref: " +  data.getLoadId());
        holder.tvSourceCity.setText(data.getFromCityName());
        holder.tvSourceStateCode.setText(data.getFromStateCode());
        holder.tvDestCity.setText(data.getToCityName());
        holder.tvDestStateCode.setText(data.getToStateCode());
        holder.tvWeight.setText("Open | "+data.getPayLoadDesc());
        holder.tvDocketContain.setText(data.getProductName());
        holder.tvDate.setText(getDate(data.getLoadDate()));

        holder.lvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", data.getSupport_No(), null));
                context.startActivity(intent);
            }
        });


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
        public ConstraintLayout lvContact;


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
            lvContact = itemView.findViewById(R.id.lvContact);




        }

    }
}



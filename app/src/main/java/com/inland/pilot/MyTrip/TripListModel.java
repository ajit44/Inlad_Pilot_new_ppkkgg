package com.inland.pilot.MyTrip;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.inland.pilot.VehicleMaster.VehicleMasterModel;

public class TripListModel {

    @SerializedName("Table")
    private List<TripMasterModel> tripListModels;

    public TripListModel() {
    }

    public TripListModel(List<TripMasterModel> tripListModels) {
        this.tripListModels = tripListModels;
    }

    public List<TripMasterModel> getTripListModels() {
        return tripListModels;
    }

    public void setTripListModels(List<TripMasterModel> tripListModels) {
        this.tripListModels = tripListModels;
    }

    @Override
    public String toString() {
        return "TripListModel{" +
                "TripListModels=" + tripListModels +
                '}';
    }
}

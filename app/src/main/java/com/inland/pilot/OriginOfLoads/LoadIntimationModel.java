package com.inland.pilot.OriginOfLoads;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.inland.pilot.MyTrip.TripMasterModel;

public class LoadIntimationModel {

    @SerializedName("Table")
    private List<LoadDetailsModel> loadIntimationModel;

    public LoadIntimationModel() {
    }


    public LoadIntimationModel(List<LoadDetailsModel> loadIntimationModel) {
        this.loadIntimationModel = loadIntimationModel;
    }

    public List<LoadDetailsModel> getLoadIntimationModel() {
        return loadIntimationModel;
    }

    public void setLoadIntimationModel(List<LoadDetailsModel> loadIntimationModel) {
        this.loadIntimationModel = loadIntimationModel;
    }

    @Override
    public String toString() {
        return "LoadIntimationModel{" +
                "loadIntimationModel=" + loadIntimationModel +
                '}';
    }
}

package com.inland.pilot.VehicleMaster;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleListModel {

    @SerializedName("Table")
    private List<VehicleMasterModel> vehicleListModels;

    public VehicleListModel() {
    }

    public VehicleListModel(List<VehicleMasterModel> vehicleListModels) {
        this.vehicleListModels = vehicleListModels;
    }

    public List<VehicleMasterModel> getVehicleListModels() {
        return vehicleListModels;
    }

    public void setVehicleListModels(List<VehicleMasterModel> vehicleListModels) {
        this.vehicleListModels = vehicleListModels;
    }

    @Override
    public String toString() {
        return "VehicleListModel{" +
                "vehicleListModels=" + vehicleListModels +
                '}';
    }
}

package com.inland.pilot.RouteMaster;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.inland.pilot.MyTrip.TripMasterModel;

public class RouteListModel {

    @SerializedName("Table")
    private List<RouteMasterModel> routeListModels;

    public RouteListModel() {
    }

    public RouteListModel(List<RouteMasterModel> routeListModels) {
        this.routeListModels = routeListModels;
    }

    public List<RouteMasterModel> getRouteListModels() {
        return routeListModels;
    }

    public void setRouteListModels(List<RouteMasterModel> routeListModels) {
        this.routeListModels = routeListModels;
    }

    @Override
    public String toString() {
        return "RouteListModel{" +
                "RouteListModels=" + routeListModels +
                '}';
    }
}

package com.inland.pilot.VehicleMaster;

public class RequestVehicleListModel {
    private String LoginId;

    public RequestVehicleListModel() {
    }

    public RequestVehicleListModel(String loginId) {
        LoginId = loginId;
    }

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
    }

    @Override
    public String toString() {
        return "RequestVehicleListModel{" +
                "LoginId='" + LoginId + '\'' +
                '}';
    }
}

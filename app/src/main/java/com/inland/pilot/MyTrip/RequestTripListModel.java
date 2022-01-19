package com.inland.pilot.MyTrip;

public class RequestTripListModel {
    private String LoginId;
    private String Flag;

    public RequestTripListModel() {
    }

    public RequestTripListModel(String Flag, String loginId) {
        LoginId = loginId;
        this.Flag=Flag;
    }

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    @Override
    public String toString() {
        return "RequestTripListModel{" +
                "LoginId='" + LoginId + '\'' +
                "Flag='" + Flag + '\'' +
                '}';
    }
}

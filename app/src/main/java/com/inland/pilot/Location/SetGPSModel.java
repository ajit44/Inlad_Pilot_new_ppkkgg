package com.inland.pilot.Location;

public class SetGPSModel {
    private String Query;
    private String TripId;
    private String LoginId;
    private String T_Longitude;
    private String T_Latitude;
    private String DeviceId;
    private String TokenNo;
    private String lastUpdate;

    public SetGPSModel() {
    }

    public SetGPSModel(String Query, String TripId, String LoginId, String T_Longitude, String T_Latitude, String DeviceId, String TokenNo, String lastUpdate) {
        this.Query = Query;
        this.TripId = TripId;
        this.LoginId = LoginId;
        this.T_Longitude = T_Longitude;
        this.T_Latitude = T_Latitude;
        this.DeviceId = DeviceId;
        this.TokenNo = TokenNo;
        this.lastUpdate = lastUpdate;
    }

    public String getQuery() {
        return Query;
    }

    public void setQuery(String query) {
        Query = query;
    }

    public String getTripId() {
        return TripId;
    }

    public void setTripId(String tripId) {
        TripId = tripId;
    }

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
    }

    public String getT_Longitude() {
        return T_Longitude;
    }

    public void setT_Longitude(String t_Longitude) {
        T_Longitude = t_Longitude;
    }

    public String getT_Latitude() {
        return T_Latitude;
    }

    public void setT_Latitude(String t_Latitude) {
        T_Latitude = t_Latitude;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getTokenNo() {
        return TokenNo;
    }

    public void setTokenNo(String tokenNo) {
        TokenNo = tokenNo;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "SetGPSModel{" +
                "Query='" + Query + '\'' +
                ", TripId='" + TripId + '\'' +
                ", LoginId='" + LoginId + '\'' +
                ", T_Longitude='" + T_Longitude + '\'' +
                ", T_Latitude='" + T_Latitude + '\'' +
                ", DeviceId='" + DeviceId + '\'' +
                ", TokenNo='" + TokenNo + '\'' +
                ", lastUpdate='" + lastUpdate + '\'' +
                '}';
    }
}

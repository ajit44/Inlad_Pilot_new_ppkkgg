package com.inland.pilot.Location;

public class SetTripStatusUpdateModel {
    private String Flag;
    private String Tripid;
    private String LoginId;

    public SetTripStatusUpdateModel() {
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    public String getTripid() {
        return Tripid;
    }

    public void setTripid(String tripid) {
        Tripid = tripid;
    }

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
    }

    public SetTripStatusUpdateModel(String Flag, String Tripid, String LoginId) {
        this.Flag=Flag;
        this.Tripid = Tripid;
        this.LoginId = LoginId;
    }

    @Override
    public String toString() {
        return "SetGPSModel{" +
                "Flag='" + Flag + '\'' +
                ", Tripid='" + Tripid + '\'' +
                ", LoginId='" + LoginId + '\'' +
                '}';
    }
}

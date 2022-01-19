package com.inland.pilot.RouteMaster;

public class RouteMasterModel {
    private String ID;
    private String Flag;
    private String FSTATE_ID;
    private String SRCRTNAME;
    private String TSTATE_ID;
    private String DSTRTNAME;
    private String LoginId;
    private String ROUTE_ID;
    private String DeviceId;

    public String getQuery() {
        return Flag;
    }

    public void setQuery(String query) {
        this.Flag = query;
    }

    public RouteMasterModel() {
    }

    // add vehicle details
    public RouteMasterModel(String query, String SRCID, String SRCRTNAME, String DSTID, String DSTRTNAME, String LoginId, String ROUTE_ID, String DeviceId) {
        this.Flag= query;
        this.FSTATE_ID = SRCID;
        this.SRCRTNAME = SRCRTNAME;
        this.TSTATE_ID = DSTID;
        this.DSTRTNAME = DSTRTNAME;
        this.LoginId = LoginId;
        this.ROUTE_ID = ROUTE_ID;
        this.DeviceId= DeviceId;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSRCRTNAME() {
        return SRCRTNAME;
    }

    public void setSRCRTNAME(String SRCRTNAME) {
        this.SRCRTNAME = SRCRTNAME;
    }

    public String getDSTRTNAME() {
        return DSTRTNAME;
    }

    public void setDSTRTNAME(String DSTRTNAME) {
        this.DSTRTNAME = DSTRTNAME;
    }

    public String getFSTATE_ID() {
        return FSTATE_ID;
    }

    public void setFSTATE_ID(String FSTATE_ID) {
        this.FSTATE_ID = FSTATE_ID;
    }

    public String getTSTATE_ID() {
        return TSTATE_ID;
    }

    public void setTSTATE_ID(String TSTATE_ID) {
        this.TSTATE_ID = TSTATE_ID;
    }

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
    }

    public String getROUTE_ID() {
        return ROUTE_ID;
    }

    public void setROUTE_ID(String ROUTE_ID) {
        this.ROUTE_ID = ROUTE_ID;
    }

    @Override
    public String toString() {
        return "RouteMasterModel{" +
                "Flag='" + Flag + '\'' +
                ", ID='" + ID + '\'' +
                ", FSTATE_ID='" + FSTATE_ID + '\'' +
                ", SRCRTNAME='" + SRCRTNAME + '\'' +
                ", TSTATE_ID='" + TSTATE_ID + '\'' +
                ", DSTRTNAME='" + DSTRTNAME + '\'' +
                ", DeviceId='" + DeviceId + '\'' +
                ", LoginId='" + LoginId + '\'' +
                ", ROUTE_ID='" + ROUTE_ID + '\'' +
                '}';
    }
}

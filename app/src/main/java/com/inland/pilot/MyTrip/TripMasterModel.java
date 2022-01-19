package com.inland.pilot.MyTrip;

public class TripMasterModel {
    private String tripno;
    private String Placementid;
    private String placementdt;
    private String insertdate;
    private String Loading_location;
    private String loading_Latitude;
    private String loading_Longitude;
    private String Unloading_location;
    private String Unloading_Latitude;
    private String Unloading_Longitude;
    private String Tp_Active;

    public TripMasterModel(String tripno, String placementid, String placementdt, String insertdate, String loading_location, String loading_Latitude, String loading_Longitude, String unloading_location, String unloading_Latitude, String unloading_Longitude, String Tp_Active) {
        this.tripno = tripno;
        Placementid = placementid;
        this.placementdt = placementdt;
        this.insertdate = insertdate;
        Loading_location = loading_location;
        this.loading_Latitude = loading_Latitude;
        this.loading_Longitude = loading_Longitude;
        Unloading_location = unloading_location;
        Unloading_Latitude = unloading_Latitude;
        Unloading_Longitude = unloading_Longitude;
        this.Tp_Active = Tp_Active;
    }

    public TripMasterModel() {
    }



    String getDate(String dt){
        String arr[];
        if(dt.contains("T")) {
            arr = dt.split("T");
            return arr[0];
        }
        return dt;
    }

    public String getTripno() {
        return tripno;
    }

    public void setTripno(String tripno) {
        this.tripno = tripno;
    }

    public String getPlacementid() {
        return Placementid;
    }

    public void setPlacementid(String placementid) {
        Placementid = placementid;
    }

    public String getPlacementdt() {
        return placementdt;
    }

    public void setPlacementdt(String placementdt) {
        this.placementdt = placementdt;
    }

    public String getInsertdate() {
        return insertdate;
    }

    public void setInsertdate(String insertdate) {
        this.insertdate = insertdate;
    }

    public String getLoading_location() {
        return Loading_location;
    }

    public void setLoading_location(String loading_location) {
        Loading_location = loading_location;
    }

    public String getLoading_Latitude() {
        return loading_Latitude;
    }

    public void setLoading_Latitude(String loading_Latitude) {
        this.loading_Latitude = loading_Latitude;
    }

    public String getLoading_Longitude() {
        return loading_Longitude;
    }

    public void setLoading_Longitude(String loading_Longitude) {
        this.loading_Longitude = loading_Longitude;
    }

    public String getUnloading_location() {
        return Unloading_location;
    }

    public void setUnloading_location(String unloading_location) {
        Unloading_location = unloading_location;
    }

    public String getUnloading_Latitude() {
        return Unloading_Latitude;
    }

    public void setUnloading_Latitude(String unloading_Latitude) {
        Unloading_Latitude = unloading_Latitude;
    }

    public String getUnloading_Longitude() {
        return Unloading_Longitude;
    }

    public void setUnloading_Longitude(String unloading_Longitude) {
        Unloading_Longitude = unloading_Longitude;
    }

    public String getTP_Active() {
        return Tp_Active;
    }

    public void setTP_Active(String tp_Active) {
        Tp_Active = tp_Active;
    }

    @Override
    public String toString() {
        return "TripMasterModel{" +
                "tripno='" + tripno + '\'' +
                ", Placementid='" + Placementid + '\'' +
                ", placementdt='" + placementdt + '\'' +
                ", insertdate='" + insertdate + '\'' +
                ", Loading_location='" + Loading_location + '\'' +
                ", loading_Latitude='" + loading_Latitude + '\'' +
                ", loading_Longitude='" + loading_Longitude + '\'' +
                ", Unloading_location='" + Unloading_location + '\'' +
                ", Unloading_Latitude='" + Unloading_Latitude + '\'' +
                ", Unloading_Longitude='" + Unloading_Longitude + '\'' +
                '}';
    }
}

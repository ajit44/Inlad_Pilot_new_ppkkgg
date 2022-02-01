package com.inland.pilot.LoadImage;

public class LoadingRequestModel {
    //mobile_no+ "_"+TripId+"_loading_"+image_no+ "";
    private String mobile_no;
    private String TripId;
    private String ImageName;

    public LoadingRequestModel(String mobile_no, String tripId, String imageName) {
        this.mobile_no = mobile_no;
        TripId = tripId;
        ImageName = imageName;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getTripId() {
        return TripId;
    }

    public void setTripId(String tripId) {
        TripId = tripId;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    @Override
    public String toString() {
        return "loadingRequestModel{" +
                "mobile_no='" + mobile_no + '\'' +
                ", TripId='" + TripId + '\'' +
                ", ImageName='" + ImageName + '\'' +
                '}';
    }
}

package com.inland.pilot.Login;

public class RequestModel {
    private String P_MobileNo;
    private String Query;
    private String DeviceId;
    private String Otp;
    private String PIN;
    private String TokenNo;

    public RequestModel() {
    }

    public RequestModel(String p_MobileNo) {
        P_MobileNo = p_MobileNo;
    }

    public RequestModel(String p_MobileNo, String otp) {
        P_MobileNo = p_MobileNo;
        Otp = otp;
    }

    public RequestModel(String p_MobileNo, String deviceId, String otp) {
        P_MobileNo = p_MobileNo;
        DeviceId = deviceId;
        Otp = otp;
    }

    public RequestModel(String p_MobileNo, String deviceId, String PIN, String tokenNo) {
        P_MobileNo = p_MobileNo;
        DeviceId = deviceId;
        this.PIN = PIN;
        TokenNo = tokenNo;
    }

    public String getP_MobileNo() {
        return P_MobileNo;
    }

    public void setP_MobileNo(String p_MobileNo) {
        P_MobileNo = p_MobileNo;
    }

    public String getQuery() {
        return Query;
    }

    public void setQuery(String query) {
        Query = query;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String otp) {
        Otp = otp;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public String getTokenNo() {
        return TokenNo;
    }

    public void setTokenNo(String tokenNo) {
        TokenNo = tokenNo;
    }

    @Override
    public String toString() {
        return "RequestOtpModel{" +
                "P_MobileNo='" + P_MobileNo + '\'' +
                ", Query='" + Query + '\'' +
                ", DeviceId='" + DeviceId + '\'' +
                ", Otp='" + Otp + '\'' +
                ", PIN='" + PIN + '\'' +
                ", TokenNo='" + TokenNo + '\'' +
                '}';
    }
}

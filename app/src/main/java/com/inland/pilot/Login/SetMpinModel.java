package com.inland.pilot.Login;

public class SetMpinModel {
    private String DeviceId;
    private String PIN;
    private String P_MobileNo;

    public SetMpinModel() {
    }

    public SetMpinModel(String deviceId, String PIN, String p_MobileNo) {
        DeviceId = deviceId;
        this.PIN = PIN;
        P_MobileNo = p_MobileNo;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public String getP_MobileNo() {
        return P_MobileNo;
    }

    public void setP_MobileNo(String p_MobileNo) {
        P_MobileNo = p_MobileNo;
    }

    @Override
    public String toString() {
        return "SetMpinModel{" +
                "DeviceId='" + DeviceId + '\'' +
                ", PIN='" + PIN + '\'' +
                ", P_MobileNo='" + P_MobileNo + '\'' +
                '}';
    }
}

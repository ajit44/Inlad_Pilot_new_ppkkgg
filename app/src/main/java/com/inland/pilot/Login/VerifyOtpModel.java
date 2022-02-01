package com.inland.pilot.Login;

public class VerifyOtpModel {
    private String P_MobileNo;
    private String Otp;
    private String DeviceId;
    //private String Token;
   // private String MESSAGE;
   // private String RACTIVE;

    public VerifyOtpModel() {
    }

    public VerifyOtpModel(String token, String MESSAGE) {
     //   Token = token;
     //   this.MESSAGE = MESSAGE;
    }

    public VerifyOtpModel(String p_MobileNo, String otp, String deviceId) {
        P_MobileNo = p_MobileNo;
        Otp = otp;
        DeviceId = deviceId;
    }

    public VerifyOtpModel(String p_MobileNo, String otp, String deviceId, String token, String MESSAGE, String RACTIVE) {
        P_MobileNo = p_MobileNo;
        Otp = otp;
        DeviceId = deviceId;
       // Token = token;
      //  this.MESSAGE = MESSAGE;
      //  this.RACTIVE = RACTIVE;
    }

    public String getP_MobileNo() {
        return P_MobileNo;
    }

    public void setP_MobileNo(String p_MobileNo) {
        P_MobileNo = p_MobileNo;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String otp) {
        Otp = otp;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

  /* public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public String getRACTIVE() {
        return RACTIVE;
    }

    public void setRACTIVE(String RACTIVE) {
        this.RACTIVE = RACTIVE;
    }*/

    @Override
    public String toString() {
        return "VerifyOtpModel{" +
                "P_MobileNo='" + P_MobileNo + '\'' +
                ", Otp='" + Otp + '\'' +
                ", DeviceId='" + DeviceId + '\'' +
               /* ", Token='" + Token + '\'' +
                ", MESSAGE='" + MESSAGE + '\'' +
                ", RACTIVE='" + RACTIVE + '\'' +*/
                '}';
    }
}

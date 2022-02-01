package com.inland.pilot.Login;

public class VerifyMobileNoModel {
    private String SETPASSWORD;
    private String Msg;
    private String OTP;
    private String TSTATUS;

    public String getTSTATUS() {
        return TSTATUS;
    }

    public void setTSTATUS(String TSTATUS) {
        this.TSTATUS = TSTATUS;
    }

    public VerifyMobileNoModel() {
    }

    public VerifyMobileNoModel(String Msg) {
        this.Msg = Msg;
    }

    public VerifyMobileNoModel(String SETPASSWORD, String Msg, String OTP, String TSTATUS) {
        this.SETPASSWORD = SETPASSWORD;
        this.Msg = Msg;
        this.OTP = OTP;
        this.TSTATUS=TSTATUS;
    }

    public String getSETPASSWORD() {
        return SETPASSWORD;
    }

    public void setSETPASSWORD(String SETPASSWORD) {
        this.SETPASSWORD = SETPASSWORD;
    }

    public String getMESSAGE() {
        return Msg;
    }

    public void setMESSAGE(String MESSAGE) {
        this.Msg = MESSAGE;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    @Override
    public String toString() {
        return "VerifyMobileNoModel{" +
                "SETPASSWORD='" + SETPASSWORD + '\'' +
                ", Msg='" + Msg + '\'' +
                ", OTP='" + OTP + '\'' +
                ", TSTATUS='" + TSTATUS + '\'' +
                '}';
    }
}

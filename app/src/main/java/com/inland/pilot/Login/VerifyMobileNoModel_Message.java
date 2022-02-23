package com.inland.pilot.Login;

public class VerifyMobileNoModel_Message {
    private String SETPASSWORD;
    private String MESSAGE;
    private String OTP;
    private String TSTATUS;

    public String getTSTATUS() {
        return TSTATUS;
    }

    public void setTSTATUS(String TSTATUS) {
        this.TSTATUS = TSTATUS;
    }

    public VerifyMobileNoModel_Message() {
    }

    public VerifyMobileNoModel_Message(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public VerifyMobileNoModel_Message(String SETPASSWORD, String MESSAGE, String OTP, String TSTATUS) {
        this.SETPASSWORD = SETPASSWORD;
        this.MESSAGE = MESSAGE;
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
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
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
                ", MESSAGE='" + MESSAGE + '\'' +
                ", OTP='" + OTP + '\'' +
                ", TSTATUS='" + TSTATUS + '\'' +
                '}';
    }
}

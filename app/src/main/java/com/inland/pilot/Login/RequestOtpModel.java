package com.inland.pilot.Login;

public class RequestOtpModel {
    private String MESSAGE;
    private String OTP;
    private boolean SuccessCode;
    private String DashboardStatus;
    private String PasswordStatus;

    public RequestOtpModel() {
    }

    public RequestOtpModel(String MESSAGE, String OTP) {
        this.MESSAGE = MESSAGE;
        this.OTP = OTP;
    }

    public RequestOtpModel(String MESSAGE, String OTP, boolean successCode) {
        this.MESSAGE = MESSAGE;
        this.OTP = OTP;
        SuccessCode = successCode;
    }

    public RequestOtpModel(String MESSAGE, String OTP, boolean successCode, String dashboardStatus) {
        this.MESSAGE = MESSAGE;
        this.OTP = OTP;
        SuccessCode = successCode;
        DashboardStatus = dashboardStatus;
    }

    public RequestOtpModel(String MESSAGE, String OTP, boolean successCode, String dashboardStatus, String passwordStatus) {
        this.MESSAGE = MESSAGE;
        this.OTP = OTP;
        SuccessCode = successCode;
        DashboardStatus = dashboardStatus;
        PasswordStatus = passwordStatus;
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

    public boolean isSuccessCode() {
        return SuccessCode;
    }

    public void setSuccessCode(boolean successCode) {
        SuccessCode = successCode;
    }

    public String getDashboardStatus() {
        return DashboardStatus;
    }

    public void setDashboardStatus(String dashboardStatus) {
        DashboardStatus = dashboardStatus;
    }

    public String getPasswordStatus() {
        return PasswordStatus;
    }

    public void setPasswordStatus(String passwordStatus) {
        PasswordStatus = passwordStatus;
    }

    @Override
    public String toString() {
        return "RequestOtpResponseModel{" +
                "MESSAGE='" + MESSAGE + '\'' +
                ", OTP='" + OTP + '\'' +
                ", SuccessCode=" + SuccessCode +
                ", DashboardStatus='" + DashboardStatus + '\'' +
                ", PasswordStatus='" + PasswordStatus + '\'' +
                '}';
    }
}

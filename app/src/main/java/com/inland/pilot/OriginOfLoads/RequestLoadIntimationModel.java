package com.inland.pilot.OriginOfLoads;

public class RequestLoadIntimationModel {
    private String LoginId;

    public RequestLoadIntimationModel() {
    }

    public RequestLoadIntimationModel(String LoginId) {
        this.LoginId = LoginId;
    }

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
    }


    @Override
    public String toString() {
        return "RequestLoadIntimationModel{" +
                "LoginId='" + LoginId + '\'' +
                '}';
    }
}

package com.inland.pilot.Login;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponseModel {
    @SerializedName("Table")
    private List<LoginDetailsModel> loginDetailsModels;

    public LoginResponseModel() {
    }

    public LoginResponseModel(List<LoginDetailsModel> loginDetailsModels) {
        this.loginDetailsModels = loginDetailsModels;
    }

    public List<LoginDetailsModel> getLoginDetailsModels() {
        return loginDetailsModels;
    }

    public void setLoginDetailsModels(List<LoginDetailsModel> loginDetailsModels) {
        this.loginDetailsModels = loginDetailsModels;
    }

    @Override
    public String toString() {
        return "LoginResponseModel{" +
                "loginDetailsModels=" + loginDetailsModels +
                '}';
    }
}

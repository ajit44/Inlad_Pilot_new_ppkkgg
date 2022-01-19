package com.inland.pilot.Login;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchRegResponseModel {
    @SerializedName("Table")
    private List<com.inland.pilot.Login.FetchRegDetailsModel> loginDetailsModels;

    public FetchRegResponseModel() {
    }

    public FetchRegResponseModel(List<com.inland.pilot.Login.FetchRegDetailsModel> loginDetailsModels) {
        this.loginDetailsModels = loginDetailsModels;
    }

    public List<com.inland.pilot.Login.FetchRegDetailsModel> getRegDetailsModels() {
        return loginDetailsModels;
    }

    public void setRegDetailsModels(List<com.inland.pilot.Login.FetchRegDetailsModel> loginDetailsModels) {
        this.loginDetailsModels = loginDetailsModels;
    }

    @Override
    public String toString() {
        return "LoginResponseModel{" +
                "loginDetailsModels=" + loginDetailsModels +
                '}';
    }
}

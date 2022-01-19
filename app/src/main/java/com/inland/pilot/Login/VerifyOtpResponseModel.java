package com.inland.pilot.Login;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VerifyOtpResponseModel {
    @SerializedName("Table")
    private List<com.inland.pilot.Login.VerifyOtpModel> verifyOtpModels;

    public VerifyOtpResponseModel() {
    }

    public VerifyOtpResponseModel(List<com.inland.pilot.Login.VerifyOtpModel> verifyOtpModels) {
        this.verifyOtpModels = verifyOtpModels;
    }

    public List<com.inland.pilot.Login.VerifyOtpModel> getVerifyOtpModels() {
        return verifyOtpModels;
    }

    public void setVerifyOtpModels(List<com.inland.pilot.Login.VerifyOtpModel> verifyOtpModels) {
        this.verifyOtpModels = verifyOtpModels;
    }

    @Override
    public String toString() {
        return "VerifyOtpResponseModel{" +
                "verifyOtpModels=" + verifyOtpModels +
                '}';
    }
}

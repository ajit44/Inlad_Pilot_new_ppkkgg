package com.inland.pilot.Login;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VerifyMobileNoResponseModel {
    @SerializedName("Table")
    private List<VerifyMobileNoModel> verifyMobileNoModels;

    public VerifyMobileNoResponseModel() {
    }

    public VerifyMobileNoResponseModel(List<VerifyMobileNoModel> verifyMobileNoModels) {
        this.verifyMobileNoModels = verifyMobileNoModels;
    }

    public List<VerifyMobileNoModel> getVerifyMobileNoModels() {
        return verifyMobileNoModels;
    }

    public void setVerifyMobileNoModels(List<VerifyMobileNoModel> verifyMobileNoModels) {
        this.verifyMobileNoModels = verifyMobileNoModels;
    }

    @Override
    public String toString() {
        return "VerifyMobileNoResponseModel{" +
                "verifyMobileNoModels=" + verifyMobileNoModels +
                '}';
    }
}

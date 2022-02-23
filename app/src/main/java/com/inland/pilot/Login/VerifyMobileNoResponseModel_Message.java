package com.inland.pilot.Login;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VerifyMobileNoResponseModel_Message {
    @SerializedName("Table")
    private List<VerifyMobileNoModel_Message> verifyMobileNoModels;

    public VerifyMobileNoResponseModel_Message() {
    }

    public VerifyMobileNoResponseModel_Message(List<VerifyMobileNoModel_Message> verifyMobileNoModels) {
        this.verifyMobileNoModels = verifyMobileNoModels;
    }

    public List<VerifyMobileNoModel_Message> getVerifyMobileNoModels() {
        return verifyMobileNoModels;
    }

    public void setVerifyMobileNoModels(List<VerifyMobileNoModel_Message> verifyMobileNoModels) {
        this.verifyMobileNoModels = verifyMobileNoModels;
    }

    @Override
    public String toString() {
        return "VerifyMobileNoResponseModel{" +
                "verifyMobileNoModels=" + verifyMobileNoModels +
                '}';
    }
}

package com.inland.pilot.Login;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VerifyPinResponseModel {
    @SerializedName("Table")
    private List<VerifyOtpModel> verifyPin;

    public VerifyPinResponseModel() {
    }

    public VerifyPinResponseModel(List<VerifyOtpModel> verifyPin) {
        this.verifyPin = verifyPin;
    }

    public List<VerifyOtpModel> getVerifyPin() {
        return verifyPin;
    }

    public void setVerifyPin(List<VerifyOtpModel> verifyPin) {
        this.verifyPin = verifyPin;
    }

    @Override
    public String toString() {
        return "VerifyPinResponseModel{" +
                "verifyPin=" + verifyPin +
                '}';
    }
}

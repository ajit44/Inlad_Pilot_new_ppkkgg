package com.inland.pilot.Login;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestOtpResponseModel {
    @SerializedName("Table")
    private List<RequestOtpModel> requestOtpModels;

    public RequestOtpResponseModel() {
    }

    public RequestOtpResponseModel(List<RequestOtpModel> requestOtpModels) {
        this.requestOtpModels = requestOtpModels;
    }

    public List<RequestOtpModel> getRequestOtpModels() {
        return requestOtpModels;
    }

    public void setRequestOtpModels(List<RequestOtpModel> requestOtpModels) {
        this.requestOtpModels = requestOtpModels;
    }

    @Override
    public String toString() {
        return "RequestOtpResponseModel{" +
                "requestOtpModels=" + requestOtpModels +
                '}';
    }
}

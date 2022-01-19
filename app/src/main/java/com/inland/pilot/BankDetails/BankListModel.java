package com.inland.pilot.BankDetails;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.inland.pilot.RouteMaster.RouteMasterModel;

public class BankListModel {

    @SerializedName("Table")
    private List<BankDetailsModel> bankListModels;

    public BankListModel() {
    }

    public BankListModel(List<BankDetailsModel> bankListModels) {
        this.bankListModels = bankListModels;
    }

    public List<BankDetailsModel> getBankListModels() {
        return bankListModels;
    }

    public void setBankListModels(List<BankDetailsModel> bankListModels) {
        this.bankListModels = bankListModels;
    }

    @Override
    public String toString() {
        return "BankListModel{" +
                "BankListModels=" + bankListModels +
                '}';
    }
}

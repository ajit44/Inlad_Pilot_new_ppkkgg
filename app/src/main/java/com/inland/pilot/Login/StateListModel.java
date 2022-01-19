package com.inland.pilot.Login;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StateListModel {
    @SerializedName("Table")
    private List<com.inland.pilot.Login.StateModel> stateModelList;

    public StateListModel() {
    }

    public StateListModel(List<com.inland.pilot.Login.StateModel> stateModelList) {
        this.stateModelList = stateModelList;
    }

    public List<com.inland.pilot.Login.StateModel> getStateModelList() {
        return stateModelList;
    }

    public void setStateModelList(List<com.inland.pilot.Login.StateModel> stateModelList) {
        this.stateModelList = stateModelList;
    }

    @Override
    public String toString() {
        return "StateListModel{" +
                "stateModelList=" + stateModelList +
                '}';
    }
}

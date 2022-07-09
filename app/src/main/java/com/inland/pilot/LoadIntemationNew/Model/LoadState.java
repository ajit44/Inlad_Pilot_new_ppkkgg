package com.inland.pilot.LoadIntemationNew.Model;

public class LoadState {
    private String StateCode;
    private String StateName;
    private String PendingLoad;

    public LoadState(String stateCode, String stateName, String pendingLoad) {
        StateCode = stateCode;
        StateName = stateName;
        PendingLoad = pendingLoad;
    }

    public String getStateCode() {
        return StateCode;
    }

    public void setStateCode(String stateCode) {
        StateCode = stateCode;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public String getPendingLoad() {
        return PendingLoad;
    }

    public void setPendingLoad(String pendingLoad) {
        PendingLoad = pendingLoad;
    }
}

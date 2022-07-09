package com.inland.pilot.LoadIntemationNew.Model;

public class LoadCity {
    private String CityCode;
    private String CityName;
    private String PendingLoad;

    public LoadCity(String cityCode, String cityName, String pendingLoad) {
        CityCode = cityCode;
        CityName = cityName;
        PendingLoad = pendingLoad;
    }

    public String getCityCode() {
        return CityCode;
    }

    public void setCityCode(String cityCode) {
        CityCode = cityCode;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getPendingLoad() {
        return PendingLoad;
    }

    public void setPendingLoad(String pendingLoad) {
        PendingLoad = pendingLoad;
    }
}

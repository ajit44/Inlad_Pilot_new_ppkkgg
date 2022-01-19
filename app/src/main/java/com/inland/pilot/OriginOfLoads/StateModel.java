package com.inland.pilot.OriginOfLoads;

import java.util.ArrayList;

public class StateModel {
    private String StateCode;
    private int Count;
    ArrayList<CityModel> CityList;
 ArrayList<String> CityNameList;

    public StateModel(String stateCode, LoadDetailsModel model) {
        if(CityList==null) {
            CityList = new ArrayList<>();
            CityNameList = new ArrayList<>();
        }
        StateCode = stateCode;
        Count = 1;
        verifyDetails(model);
    }

    public String getStateCode() {
        return StateCode;
    }

    public void setStateCode(String stateCode) {
        StateCode = stateCode;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public ArrayList<CityModel> getCityList() {
        return CityList;
    }

    public void setCityList(ArrayList<CityModel> cityList) {
        CityList = cityList;
    }

    public void addCity(CityModel city) {
        CityList.add(city);
    }

    public void increaseStateCount() {
        Count++;
    }

    public void verifyDetails(LoadDetailsModel loadDetailsModel) {
        int isExist=-1;
        isExist=CityNameList.indexOf(loadDetailsModel.getToCityName());
        if (isExist !=-1) {
            CityModel ct = CityList.get(isExist);
            ct.addTrack(loadDetailsModel);
            CityList.set(isExist, ct);
        }else {
            CityNameList.add(loadDetailsModel.getToCityName());
            CityList.add(new CityModel(loadDetailsModel.getToCityName(),loadDetailsModel));
            Count = CityList.size();
        }
    }
}

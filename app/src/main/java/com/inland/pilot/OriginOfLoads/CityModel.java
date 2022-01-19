package com.inland.pilot.OriginOfLoads;

import java.util.ArrayList;

public class CityModel {
    private String CityName;
    private int Count;
    ArrayList<LoadDetailsModel> trackList;


    public CityModel(String cityName, LoadDetailsModel loadDetailsModel) {
        if(trackList == null)
            trackList=new ArrayList<>();
        CityName = cityName;
        trackList.add(loadDetailsModel);
        Count = 1;
    }


    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public ArrayList<LoadDetailsModel> getTrackList() {
        return trackList;
    }

    public void setTrackList(ArrayList<LoadDetailsModel> trackList) {
        this.trackList = trackList;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }


       public void addTrack(LoadDetailsModel track) {
        trackList.add(track);
        Count=trackList.size();
    }
    public void increaseCityCount() {
        Count++;
    }

    public void verifyDetails(LoadDetailsModel loadDetailsModel) {
        /*int isExist=-1;
        isExist=trackList.indexOf(loadDetailsModel);
        if (isExist !=-1) {
            CityModel ct = CityList.get(isExist);
            ct.verifyDetails(loadDetailsModel);
            CityList.set(isExist, ct);
        }else {
            Count = CityList.size();
        }*/

    }

}

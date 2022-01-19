package com.inland.pilot.MyTrip;

public class RequestActiveTripListModel {
    private String LoginId;
    private String Tokenno;
    public RequestActiveTripListModel() {
    }

    public RequestActiveTripListModel(String loginId, String Tokenno) {
        LoginId = loginId;
        this.Tokenno= Tokenno;
    }

    public String getTokenno() {
        return Tokenno;
    }

    public void setTokenno(String tokenno) {
        Tokenno = tokenno;
    }

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
    }


    @Override
    public String toString() {
        return "RequestTripListModel{" +
                "LoginId='" + LoginId + '\'' +
                "Tokenno='" + Tokenno + '\'' +
                '}';
    }
}

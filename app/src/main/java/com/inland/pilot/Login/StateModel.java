package com.inland.pilot.Login;

public class StateModel {
    private String Code;
    private String NAME;

    public StateModel() {
    }

    public StateModel(String code, String NAME) {
        Code = code;
        this.NAME = NAME;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return NAME;
    }

    public void setName(String NAME) {
        this.NAME = NAME;
    }

    @Override
    public String toString() {
        return "StateModel{" +
                "Code='" + Code + '\'' +
                ", NAME='" + NAME + '\'' +
                '}';
    }
}

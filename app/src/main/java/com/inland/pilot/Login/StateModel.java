package com.inland.pilot.Login;

public class StateModel {
    private String Code;
    private String Name;

    public StateModel() {
    }

    public StateModel(String code, String name) {
        Code = code;
        Name = name;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "StateModel{" +
                "Code='" + Code + '\'' +
                ", Name='" + Name + '\'' +
                '}';
    }
}

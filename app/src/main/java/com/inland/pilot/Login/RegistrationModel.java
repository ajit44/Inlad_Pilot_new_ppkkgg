package com.inland.pilot.Login;

public class RegistrationModel {
    private String AadharNo;
    private String Address;
    private String DLNo;
    private String Name;
    private String P_MobileNo;
    private String No_Of_vehicles;
    private String PanNo;
    private String PinCode;
    private String Query;
    private String S_MobileNo;
    private String StateCode;
    private String Type;
    private String Accountno;
    private String BankName;
    private String IFSCCode;
    private String FireBaseToken;

    public String getFireBaseToken() {
        return FireBaseToken;
    }

    public void setFireBaseToken(String fireBaseToken) {
        FireBaseToken = fireBaseToken;
    }

    public String getBankACNo() {
        return Accountno;
    }

    public void setBankACNo(String bankACNo) {
        this.Accountno = bankACNo;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        this.BankName = bankName;
    }

    public String getBankIFSC() {
        return IFSCCode;
    }

    public void setBankIFSC(String bankIFSC) {
        this.IFSCCode = bankIFSC;
    }

    public RegistrationModel() {
    }

    public RegistrationModel(String aadharNo, String address, String DLNo, String name, String no_Of_vehicles, String p_MobileNo, String panNo, String pinCode, String query, String s_MobileNo, String stateCode, String type, String bankACNo, String bankName ,String bankIFSC) {
        AadharNo = aadharNo;
        Address = address;
        this.DLNo = DLNo;
        Name = name;
        No_Of_vehicles = no_Of_vehicles;
        P_MobileNo = p_MobileNo;
        PanNo = panNo;
        PinCode = pinCode;
        Query = query;
        S_MobileNo = s_MobileNo;
        StateCode = stateCode;
        Type = type;
        this.Accountno=bankACNo;
        this.BankName=bankName;
        this.IFSCCode=bankIFSC;
    }

    public String getAadharNo() {
        return AadharNo;
    }

    public void setAadharNo(String aadharNo) {
        AadharNo = aadharNo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDLNo() {
        return DLNo;
    }

    public void setDLNo(String DLNo) {
        this.DLNo = DLNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNo_Of_vehicles() {
        return No_Of_vehicles;
    }

    public void setNo_Of_vehicles(String no_Of_vehicles) {
        No_Of_vehicles = no_Of_vehicles;
    }

    public String getP_MobileNo() {
        return P_MobileNo;
    }

    public void setP_MobileNo(String p_MobileNo) {
        P_MobileNo = p_MobileNo;
    }

    public String getPanNo() {
        return PanNo;
    }

    public void setPanNo(String panNo) {
        PanNo = panNo;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    public String getQuery() {
        return Query;
    }

    public void setQuery(String query) {
        Query = query;
    }

    public String getS_MobileNo() {
        return S_MobileNo;
    }

    public void setS_MobileNo(String s_MobileNo) {
        S_MobileNo = s_MobileNo;
    }

    public String getStateCode() {
        return StateCode;
    }

    public void setStateCode(String stateCode) {
        StateCode = stateCode;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    @Override
    public String toString() {
        return "RegistrationModel{" +
                "AadharNo='" + AadharNo + '\'' +
                ", Address='" + Address + '\'' +
                ", DLNo='" + DLNo + '\'' +
                ", Name='" + Name + '\'' +
                ", No_Of_vehicles='" + No_Of_vehicles + '\'' +
                ", P_MobileNo='" + P_MobileNo + '\'' +
                ", PanNo='" + PanNo + '\'' +
                ", PinCode='" + PinCode + '\'' +
                ", Query='" + Query + '\'' +
                ", S_MobileNo='" + S_MobileNo + '\'' +
                ", StateCode='" + StateCode + '\'' +
                ", BankName='" + BankName + '\'' +
                ", Accountno='" + Accountno + '\'' +
                ", IFSCCode='" + IFSCCode + '\'' +
                ", Type='" + Type + '\'' +
                '}';
    }
}

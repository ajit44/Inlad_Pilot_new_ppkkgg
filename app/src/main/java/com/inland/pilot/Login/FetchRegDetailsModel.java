package com.inland.pilot.Login;

public class FetchRegDetailsModel {
    private String TokenNo;
    private String ID;
    private String Name;
    private String S_MobileNo;
    private String PanNo;
    private String AadharNo;
    private String DLNo;
    private String StateCode;
    private String Address;
    private String PinCode;
    private String No_Of_vehicles;
    private String Bankname;
    private String B_Accountno;
    private String B_Ifsc;
    private String IsActive;

    private String Type;
    private String DeviceId;
    private String P_MobileNo;
    public FetchRegDetailsModel() {
    }

    public FetchRegDetailsModel(String ID, String Name, String P_MobileNo, String S_MobileNo, String PanNo, String AadharNo, String DLNo, String StateCode, String Address, String PinCode, String No_Of_vehicles, String Bankname, String B_Accountno, String B_Ifsc, String DeviceId, String TokenNo) {
        this.ID = ID;
        this.Name = Name;
        this.P_MobileNo = P_MobileNo;
        this.S_MobileNo = ID;
        this.PanNo = PanNo;
        this.AadharNo = AadharNo;
        this.DLNo = DLNo;
        this.StateCode = StateCode;
        this.Address = Address;
        this.PinCode = PinCode;
        this.No_Of_vehicles = No_Of_vehicles;
        this.Bankname = Bankname;
        this.B_Accountno = B_Accountno;
        this.B_Ifsc = B_Ifsc;
        this.Type = Type;
        this.IsActive = IsActive;
        this.DeviceId = DeviceId;
        this.TokenNo = TokenNo;
    }

    public String getTokenNo() {
        return TokenNo;
    }

    public void setTokenNo(String tokenNo) {
        TokenNo = tokenNo;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getS_MobileNo() {
        return S_MobileNo;
    }

    public void setS_MobileNo(String s_MobileNo) {
        S_MobileNo = s_MobileNo;
    }

    public String getPanNo() {
        return PanNo;
    }

    public void setPanNo(String panNo) {
        PanNo = panNo;
    }

    public String getAadharNo() {
        return AadharNo;
    }

    public void setAadharNo(String aadharNo) {
        AadharNo = aadharNo;
    }

    public String getDLNo() {
        return DLNo;
    }

    public void setDLNo(String DLNo) {
        this.DLNo = DLNo;
    }

    public String getStateCode() {
        return StateCode;
    }

    public void setStateCode(String stateCode) {
        StateCode = stateCode;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    public String getNo_Of_vehicles() {
        return No_Of_vehicles;
    }

    public void setNo_Of_vehicles(String no_Of_vehicles) {
        No_Of_vehicles = no_Of_vehicles;
    }

    public String getBankname() {
        return Bankname;
    }

    public void setBankname(String bankname) {
        Bankname = bankname;
    }

    public String getB_Accountno() {
        return B_Accountno;
    }

    public void setB_Accountno(String b_Accountno) {
        B_Accountno = b_Accountno;
    }

    public String getB_Ifsc() {
        return B_Ifsc;
    }

    public void setB_Ifsc(String b_Ifsc) {
        B_Ifsc = b_Ifsc;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getP_MobileNo() {
        return P_MobileNo;
    }

    public void setP_MobileNo(String p_MobileNo) {
        P_MobileNo = p_MobileNo;
    }

    @Override
    public String toString() {
        return "RegDetailsModel{" +
                "ID='" + ID + '\'' +
                ", Name='" + Name + '\'' +
                ", P_MobileNo='" + P_MobileNo + '\'' +
                ", S_MobileNo='" + S_MobileNo + '\'' +
                ", PanNo='" + PanNo + '\'' +
                ", AadharNo='" + AadharNo + '\'' +
                ", DLNo='" + DLNo + '\'' +
                ", StateCode='" + StateCode + '\'' +
                ", Address='" + Address + '\'' +
                ", PinCode='" + PinCode + '\'' +
                ", No_Of_vehicles='" + No_Of_vehicles + '\'' +
                ", Bankname='" + Bankname + '\'' +
                ", B_Accountno='" + B_Accountno + '\'' +
                ", B_Ifsc='" + B_Ifsc + '\'' +
                ", Type='" + Type + '\'' +
                ", IsActive='" + IsActive + '\'' +
                ", DeviceId='" + DeviceId + '\'' +
                ", TokenNo='" + TokenNo + '\'' +
                '}';
    }
}

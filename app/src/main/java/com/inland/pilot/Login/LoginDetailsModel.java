package com.inland.pilot.Login;

public class LoginDetailsModel {
    private String MESSAGE;
    private String TOKENNO;
    private String RACTIVE;
    private String ID;
    private String NAME;
    private String S_MOBILENO;
    private String PANNO;
    private String AADHARNO;
    private String DLNO;
    private String STATECODE;
    private String ADDRESS;
    private String PINCODE;
    private String NO_OF_VEHICLES;
    private String TYPE;
    private String DeviceId;
    private String P_MOBILENO;
    private String PIN;
    private String TSTATUS;
    private String IMAGEUPLOAD;
    public LoginDetailsModel() {
    }

    public String getIMAGEUPLOAD() {
        return IMAGEUPLOAD;
    }

    public void setIMAGEUPLOAD(String IMAGEUPLOAD) {
        this.IMAGEUPLOAD = IMAGEUPLOAD;
    }

    public String getTSTATUS() {
        return TSTATUS;
    }

    public void setTSTATUS(String TSTATUS) {
        this.TSTATUS = TSTATUS;
    }

    public LoginDetailsModel(String MESSAGE, String TOKENNO, String RACTIVE, String ID, String NAME, String s_MOBILENO, String PANNO, String AADHARNO, String DLNO, String STATECODE, String ADDRESS, String PINCODE, String NO_OF_VEHICLES, String TYPE, String deviceId, String p_MOBILENO, String PIN,String TSTATUS,String IMAGEUPLOAD) {
        this.MESSAGE = MESSAGE;
        this.TOKENNO = TOKENNO;
        this.RACTIVE = RACTIVE;
        this.ID = ID;
        this.NAME = NAME;
        S_MOBILENO = s_MOBILENO;
        this.PANNO = PANNO;
        this.AADHARNO = AADHARNO;
        this.DLNO = DLNO;
        this.STATECODE = STATECODE;
        this.ADDRESS = ADDRESS;
        this.PINCODE = PINCODE;
        this.NO_OF_VEHICLES = NO_OF_VEHICLES;
        this.TYPE = TYPE;
        DeviceId = deviceId;
        P_MOBILENO = p_MOBILENO;
        this.PIN = PIN;
        this.TSTATUS =  TSTATUS;
        this.IMAGEUPLOAD =  IMAGEUPLOAD;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public String getTOKENNO() {
        return TOKENNO;
    }

    public void setTOKENNO(String TOKENNO) {
        this.TOKENNO = TOKENNO;
    }

    public String getRACTIVE() {
        return RACTIVE;
    }

    public void setRACTIVE(String RACTIVE) {
        this.RACTIVE = RACTIVE;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getS_MOBILENO() {
        return S_MOBILENO;
    }

    public void setS_MOBILENO(String s_MOBILENO) {
        S_MOBILENO = s_MOBILENO;
    }

    public String getPANNO() {
        return PANNO;
    }

    public void setPANNO(String PANNO) {
        this.PANNO = PANNO;
    }

    public String getAADHARNO() {
        return AADHARNO;
    }

    public void setAADHARNO(String AADHARNO) {
        this.AADHARNO = AADHARNO;
    }

    public String getDLNO() {
        return DLNO;
    }

    public void setDLNO(String DLNO) {
        this.DLNO = DLNO;
    }

    public String getSTATECODE() {
        return STATECODE;
    }

    public void setSTATECODE(String STATECODE) {
        this.STATECODE = STATECODE;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getPINCODE() {
        return PINCODE;
    }

    public void setPINCODE(String PINCODE) {
        this.PINCODE = PINCODE;
    }

    public String getNO_OF_VEHICLES() {
        return NO_OF_VEHICLES;
    }

    public void setNO_OF_VEHICLES(String NO_OF_VEHICLES) {
        this.NO_OF_VEHICLES = NO_OF_VEHICLES;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public String getP_MOBILENO() {
        return P_MOBILENO;
    }

    public void setP_MOBILENO(String p_MOBILENO) {
        P_MOBILENO = p_MOBILENO;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    @Override
    public String toString() {
        return "LoginDetailsModel{" +
                "MESSAGE='" + MESSAGE + '\'' +
                ", TOKENNO='" + TOKENNO + '\'' +
                ", RACTIVE='" + RACTIVE + '\'' +
                ", ID='" + ID + '\'' +
                ", NAME='" + NAME + '\'' +
                ", S_MOBILENO='" + S_MOBILENO + '\'' +
                ", PANNO='" + PANNO + '\'' +
                ", AADHARNO='" + AADHARNO + '\'' +
                ", DLNO='" + DLNO + '\'' +
                ", STATECODE='" + STATECODE + '\'' +
                ", ADDRESS='" + ADDRESS + '\'' +
                ", PINCODE='" + PINCODE + '\'' +
                ", NO_OF_VEHICLES='" + NO_OF_VEHICLES + '\'' +
                ", TYPE='" + TYPE + '\'' +
                ", DeviceId='" + DeviceId + '\'' +
                ", P_MOBILENO='" + P_MOBILENO + '\'' +
                ", PIN='" + PIN + '\'' +
                 ", TSTATUS='" + TSTATUS + '\'' +
                '}';
    }
}

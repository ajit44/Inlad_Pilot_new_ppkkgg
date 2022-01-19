package com.inland.pilot.BankDetails;

public class BankDetailsAddModel {
    private String Flag;
    private String BankName;
    private String Accountno;
    private String IFSCCode;
    private String LoginId;
    private String ID;

    public BankDetailsAddModel() {
    }

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
    }

    public BankDetailsAddModel(String Flag, String bankName, String accountNo, String ifscCode, String LoginId, String ID) {
        this.Flag= Flag;
        this.BankName = bankName;
        this.Accountno = accountNo;
        this.IFSCCode = ifscCode;
        this.LoginId=LoginId;
        this.ID= ID;
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getAccountno() {
        return Accountno;
    }

    public void setAccountno(String accountno) {
        Accountno = accountno;
    }

    public String getIFSCCode() {
        return IFSCCode;
    }

    public void setIFSCCode(String IFSCCode) {
        this.IFSCCode = IFSCCode;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "BankDetailsModel{" +
                "BankName='" + BankName + '\'' +
                ", Accountno='" + Accountno + '\'' +
                ", IFSCCode='" + IFSCCode + '\'' +
                ", LoginId='" + LoginId + '\'' +
                ", Flag='" + Flag + '\'' +
                ", ID='" + ID + '\'' +
                '}';
    }
}

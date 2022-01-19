package com.inland.pilot.BankDetails;

public class BankDetailsModel {
    private String BankName;
    private String Accountno;
    private String IFSCCode;
    private String loginId;

    public BankDetailsModel() {
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        loginId = loginId;
    }

    public BankDetailsModel(String bankName, String accountNo, String ifscCode, String LoginId) {
        this.BankName = bankName;
        this.Accountno = accountNo;
        this.IFSCCode = ifscCode;
        this.loginId=LoginId;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        this.BankName = bankName;
    }

    public String getAccountNo() {
        return Accountno;
    }

    public void setAccountNo(String accountNo) {
        this.Accountno = accountNo;
    }

    public String getIfscCode() {
        return IFSCCode;
    }

    public void setIfscCode(String ifscCode) {
        this.IFSCCode = ifscCode;
    }

    @Override
    public String toString() {
        return "BankDetailsModel{" +
                "bankName='" + BankName + '\'' +
                ", accountNo='" + Accountno + '\'' +
                ", ifscCode='" + IFSCCode + '\'' +
                ", LoginId='" + loginId + '\'' +
                '}';
    }
}

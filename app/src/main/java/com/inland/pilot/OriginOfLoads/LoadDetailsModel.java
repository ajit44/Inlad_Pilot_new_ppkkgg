package com.inland.pilot.OriginOfLoads;

public class LoadDetailsModel {
    private String id;
    private String DriverCode;
    private String LoadID;
    private String LoadDate;
    private String FromCityCode;
    private String FromCityName;
    private String FromStatecode;
    private String ToCityCode;
    private String ToCityName;
    private String ToStatecode;
    private String VehicleType_Id;
    private String vehicletype_name;
    private String VehiclePayLoad_Id;
    private String Payloaddesc;
    private String VehicleTyres_Id;
    private String Tyresdesc;
    private String Productcode;
    private String ProductName;
    private String Remarks = null;
    private String Pickup_Location;
    private String Delivery_Location;
    private String ExpectedRPT;
    private String LoadContactPersonName;
    private String LoadContactPersonMobileNO;
    private String ExpectedDeliveryDate = null;
    private String Entry_By = null;
    private String Entry_Date = null;
    private String Modify_By = null;
    private String Modify_Date = null;


    // Getter Methods

    public String getId() {
        return id;
    }

    public String getDriverCode() {
        return DriverCode;
    }

    public String getLoadID() {
        return LoadID;
    }

    public String getLoadDate() {
        return LoadDate;
    }

    public String getFromCityCode() {
        return FromCityCode;
    }

    public String getFromCityName() {
        return FromCityName;
    }

    public String getFromStatecode() {
        return FromStatecode;
    }

    public String getToCityCode() {
        return ToCityCode;
    }

    public String getToCityName() {
        return ToCityName;
    }

    public String getToStatecode() {
        return ToStatecode;
    }

    public String getVehicleType_Id() {
        return VehicleType_Id;
    }

    public String getVehicletype_name() {
        return vehicletype_name;
    }

    public String getVehiclePayLoad_Id() {
        return VehiclePayLoad_Id;
    }

    public String getPayloaddesc() {
        return Payloaddesc;
    }

    public String getVehicleTyres_Id() {
        return VehicleTyres_Id;
    }

    public String getTyresdesc() {
        return Tyresdesc;
    }

    public String getProductcode() {
        return Productcode;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getRemarks() {
        return Remarks;
    }

    public String getPickup_Location() {
        return Pickup_Location;
    }

    public String getDelivery_Location() {
        return Delivery_Location;
    }

    public String getExpectedRPT() {
        return ExpectedRPT;
    }

    public String getLoadContactPersonName() {
        return LoadContactPersonName;
    }

    public String getLoadContactPersonMobileNO() {
        return LoadContactPersonMobileNO;
    }

    public String getExpectedDeliveryDate() {
        return ExpectedDeliveryDate;
    }

    public String getEntry_By() {
        return Entry_By;
    }

    public String getEntry_Date() {
        return Entry_Date;
    }

    public String getModify_By() {
        return Modify_By;
    }

    public String getModify_Date() {
        return Modify_Date;
    }

    // Setter Methods

    public void setId(String id) {
        this.id = id;
    }

    public void setDriverCode(String DriverCode) {
        this.DriverCode = DriverCode;
    }

    public void setLoadID(String LoadID) {
        this.LoadID = LoadID;
    }

    public void setLoadDate(String LoadDate) {
        this.LoadDate = LoadDate;
    }

    public void setFromCityCode(String FromCityCode) {
        this.FromCityCode = FromCityCode;
    }

    public void setFromCityName(String FromCityName) {
        this.FromCityName = FromCityName;
    }

    public void setFromStatecode(String FromStatecode) {
        this.FromStatecode = FromStatecode;
    }

    public void setToCityCode(String ToCityCode) {
        this.ToCityCode = ToCityCode;
    }

    public void setToCityName(String ToCityName) {
        this.ToCityName = ToCityName;
    }

    public void setToStatecode(String ToStatecode) {
        this.ToStatecode = ToStatecode;
    }

    public void setVehicleType_Id(String VehicleType_Id) {
        this.VehicleType_Id = VehicleType_Id;
    }

    public void setVehicletype_name(String vehicletype_name) {
        this.vehicletype_name = vehicletype_name;
    }

    public void setVehiclePayLoad_Id(String VehiclePayLoad_Id) {
        this.VehiclePayLoad_Id = VehiclePayLoad_Id;
    }

    public void setPayloaddesc(String Payloaddesc) {
        this.Payloaddesc = Payloaddesc;
    }

    public void setVehicleTyres_Id(String VehicleTyres_Id) {
        this.VehicleTyres_Id = VehicleTyres_Id;
    }

    public void setTyresdesc(String Tyresdesc) {
        this.Tyresdesc = Tyresdesc;
    }

    public void setProductcode(String Productcode) {
        this.Productcode = Productcode;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }

    public void setPickup_Location(String Pickup_Location) {
        this.Pickup_Location = Pickup_Location;
    }

    public void setDelivery_Location(String Delivery_Location) {
        this.Delivery_Location = Delivery_Location;
    }

    public void setExpectedRPT(String ExpectedRPT) {
        this.ExpectedRPT = ExpectedRPT;
    }

    public void setLoadContactPersonName(String LoadContactPersonName) {
        this.LoadContactPersonName = LoadContactPersonName;
    }

    public void setLoadContactPersonMobileNO(String LoadContactPersonMobileNO) {
        this.LoadContactPersonMobileNO = LoadContactPersonMobileNO;
    }

    public void setExpectedDeliveryDate(String ExpectedDeliveryDate) {
        this.ExpectedDeliveryDate = ExpectedDeliveryDate;
    }

    public void setEntry_By(String Entry_By) {
        this.Entry_By = Entry_By;
    }

    public void setEntry_Date(String Entry_Date) {
        this.Entry_Date = Entry_Date;
    }

    public void setModify_By(String Modify_By) {
        this.Modify_By = Modify_By;
    }

    public void setModify_Date(String Modify_Date) {
        this.Modify_Date = Modify_Date;
    }

    @Override
    public String toString() {
        return "LoadDetailsModel{" +
                "id='" + id + '\'' +
                ", DriverCode='" + DriverCode + '\'' +
                ", LoadID='" + LoadID + '\'' +
                ", LoadDate='" + LoadDate + '\'' +
                ", FromCityCode='" + FromCityCode + '\'' +
                ", FromCityName='" + FromCityName + '\'' +
                ", FromStatecode='" + FromStatecode + '\'' +
                ", ToCityCode='" + ToCityCode + '\'' +
                ", ToCityName='" + ToCityName + '\'' +
                ", ToStatecode='" + ToStatecode + '\'' +
                ", VehicleType_Id='" + VehicleType_Id + '\'' +
                ", vehicletype_name='" + vehicletype_name + '\'' +
                ", VehiclePayLoad_Id='" + VehiclePayLoad_Id + '\'' +
                ", Payloaddesc='" + Payloaddesc + '\'' +
                ", VehicleTyres_Id='" + VehicleTyres_Id + '\'' +
                ", Tyresdesc='" + Tyresdesc + '\'' +
                ", Productcode='" + Productcode + '\'' +
                ", ProductName='" + ProductName + '\'' +
                ", Remarks='" + Remarks + '\'' +
                ", Pickup_Location='" + Pickup_Location + '\'' +
                ", Delivery_Location='" + Delivery_Location + '\'' +
                ", ExpectedRPT='" + ExpectedRPT + '\'' +
                ", LoadContactPersonName='" + LoadContactPersonName + '\'' +
                ", LoadContactPersonMobileNO='" + LoadContactPersonMobileNO + '\'' +
                ", ExpectedDeliveryDate='" + ExpectedDeliveryDate + '\'' +
                ", Entry_By='" + Entry_By + '\'' +
                ", Entry_Date='" + Entry_Date + '\'' +
                ", Modify_By='" + Modify_By + '\'' +
                ", Modify_Date='" + Modify_Date + '\'' +
                '}';
    }
}
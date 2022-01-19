package com.inland.pilot.VehicleMaster;

public class VehicleMasterModel {
    private String ID;
    private String Query;
    private String VehicleNo;
    private String VehicleType;
    private String Modelno;
    private String Make_no;
    private String Chassis_no;
    private String Engine_no;
    private String Fitness_Date;
    private String Registry_data;
    private String Weight_capacity;
    private String RC_Book_no;
    private String Free_Out_Space;
    private String Vehicle_Space;
    private String Insurance_Details;
    private String Insurance_Valid_tilldate;
    private String RT_No;
    private String Vehicle_register_State;
    private String Remark;
    private String VehicleId;
    private String LoginId;

    public VehicleMasterModel() {
    }

    // add vehicle details
    public VehicleMasterModel(String query, String vehicleNo, String vehicleType, String modelno, String make_no, String chassis_no, String engine_no, String fitness_Date, String registry_data, String weight_capacity, String RC_Book_no, String free_Out_Space, String vehicle_Space, String insurance_Details, String insurance_Valid_tilldate, String RT_No, String vehicle_register_State, String remark, String vehicleId, String loginId) {
        Query = query;
        VehicleNo = vehicleNo;
        VehicleType = vehicleType;
        Modelno = modelno;
        Make_no = make_no;
        Chassis_no = chassis_no;
        Engine_no = engine_no;
        Fitness_Date = fitness_Date;
        Registry_data = registry_data;
        Weight_capacity = weight_capacity;
        this.RC_Book_no = RC_Book_no;
        Free_Out_Space = free_Out_Space;
        Vehicle_Space = vehicle_Space;
        Insurance_Details = insurance_Details;
        Insurance_Valid_tilldate = insurance_Valid_tilldate;
        this.RT_No = RT_No;
        Vehicle_register_State = vehicle_register_State;
        Remark = remark;
        VehicleId = vehicleId;
        LoginId = loginId;
    }

    // update vehicle details
    public VehicleMasterModel(String ID, String vehicleNo, String vehicleType, String modelno, String make_no, String chassis_no, String engine_no, String fitness_Date, String registry_data, String weight_capacity, String RC_Book_no, String free_Out_Space, String vehicle_Space, String insurance_Details, String insurance_Valid_tilldate, String RT_No, String vehicle_register_State, String remark) {
        this.ID = ID;
        VehicleNo = vehicleNo;
        VehicleType = vehicleType;
        Modelno = modelno;
        Make_no = make_no;
        Chassis_no = chassis_no;
        Engine_no = engine_no;
        Fitness_Date = fitness_Date;
        Registry_data = registry_data;
        Weight_capacity = weight_capacity;
        this.RC_Book_no = RC_Book_no;
        Free_Out_Space = free_Out_Space;
        Vehicle_Space = vehicle_Space;
        Insurance_Details = insurance_Details;
        Insurance_Valid_tilldate = insurance_Valid_tilldate;
        this.RT_No = RT_No;
        Vehicle_register_State = vehicle_register_State;
        Remark = remark;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getQuery() {
        return Query;
    }

    public void setQuery(String query) {
        Query = query;
    }

    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        VehicleNo = vehicleNo;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    public String getModelno() {
        return Modelno;
    }

    public void setModelno(String modelno) {
        Modelno = modelno;
    }

    public String getMake_no() {
        return Make_no;
    }

    public void setMake_no(String make_no) {
        Make_no = make_no;
    }

    public String getChassis_no() {
        return Chassis_no;
    }

    public void setChassis_no(String chassis_no) {
        Chassis_no = chassis_no;
    }

    public String getEngine_no() {
        return Engine_no;
    }

    public void setEngine_no(String engine_no) {
        Engine_no = engine_no;
    }

    public String getFitness_Date() {
        return Fitness_Date;
    }

    public void setFitness_Date(String fitness_Date) {
        Fitness_Date = fitness_Date;
    }

    public String getRegistry_data() {
        return Registry_data;
    }

    public void setRegistry_data(String registry_data) {
        Registry_data = registry_data;
    }

    public String getWeight_capacity() {
        return Weight_capacity;
    }

    public void setWeight_capacity(String weight_capacity) {
        Weight_capacity = weight_capacity;
    }

    public String getRC_Book_no() {
        return RC_Book_no;
    }

    public void setRC_Book_no(String RC_Book_no) {
        this.RC_Book_no = RC_Book_no;
    }

    public String getFree_Out_Space() {
        return Free_Out_Space;
    }

    public void setFree_Out_Space(String free_Out_Space) {
        Free_Out_Space = free_Out_Space;
    }

    public String getVehicle_Space() {
        return Vehicle_Space;
    }

    public void setVehicle_Space(String vehicle_Space) {
        Vehicle_Space = vehicle_Space;
    }

    public String getInsurance_Details() {
        return Insurance_Details;
    }

    public void setInsurance_Details(String insurance_Details) {
        Insurance_Details = insurance_Details;
    }

    public String getInsurance_Valid_tilldate() {
        return Insurance_Valid_tilldate;
    }

    public void setInsurance_Valid_tilldate(String insurance_Valid_tilldate) {
        Insurance_Valid_tilldate = insurance_Valid_tilldate;
    }

    public String getRT_No() {
        return RT_No;
    }

    public void setRT_No(String RT_No) {
        this.RT_No = RT_No;
    }

    public String getVehicle_register_State() {
        return Vehicle_register_State;
    }

    public void setVehicle_register_State(String vehicle_register_State) {
        Vehicle_register_State = vehicle_register_State;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getVehicleId() {
        return VehicleId;
    }

    public void setVehicleId(String vehicleId) {
        VehicleId = vehicleId;
    }

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
    }

    @Override
    public String toString() {
        return "VehicleMasterModel{" +
                "ID='" + ID + '\'' +
                ", Query='" + Query + '\'' +
                ", VehicleNo='" + VehicleNo + '\'' +
                ", VehicleType='" + VehicleType + '\'' +
                ", Modelno='" + Modelno + '\'' +
                ", Make_no='" + Make_no + '\'' +
                ", Chassis_no='" + Chassis_no + '\'' +
                ", Engine_no='" + Engine_no + '\'' +
                ", Fitness_Date='" + Fitness_Date + '\'' +
                ", Registry_data='" + Registry_data + '\'' +
                ", Weight_capacity='" + Weight_capacity + '\'' +
                ", RC_Book_no='" + RC_Book_no + '\'' +
                ", Free_Out_Space='" + Free_Out_Space + '\'' +
                ", Vehicle_Space='" + Vehicle_Space + '\'' +
                ", Insurance_Details='" + Insurance_Details + '\'' +
                ", Insurance_Valid_tilldate='" + Insurance_Valid_tilldate + '\'' +
                ", RT_No='" + RT_No + '\'' +
                ", Vehicle_register_State='" + Vehicle_register_State + '\'' +
                ", Remark='" + Remark + '\'' +
                ", VehicleId='" + VehicleId + '\'' +
                ", LoginId='" + LoginId + '\'' +
                '}';
    }
}

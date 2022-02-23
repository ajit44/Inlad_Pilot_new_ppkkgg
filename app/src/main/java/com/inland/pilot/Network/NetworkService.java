package com.inland.pilot.Network;

import com.inland.pilot.BankDetails.BankDetailsAddModel;
import com.inland.pilot.BankDetails.BankDetailsModel;
import com.inland.pilot.BankDetails.BankListModel;
import com.inland.pilot.Location.SetGPSModel;
import com.inland.pilot.Location.SetTripStatusUpdateModel;
import com.inland.pilot.Login.FetchRegResponseModel;
import com.inland.pilot.Login.LoginDetailsModel;
import com.inland.pilot.Login.LoginResponseModel;
import com.inland.pilot.Login.RegistrationModel;
import com.inland.pilot.Login.RequestModel;
import com.inland.pilot.Login.RequestOtpModel;
import com.inland.pilot.Login.RequestOtpResponseModel;
import com.inland.pilot.Login.SetMpinModel;
import com.inland.pilot.Login.StateListModel;
import com.inland.pilot.Login.StateModel;
import com.inland.pilot.Login.VerifyMobileNoResponseModel;
import com.inland.pilot.Login.VerifyMobileNoResponseModel_Message;
import com.inland.pilot.Login.VerifyOtpModel;
import com.inland.pilot.Login.VerifyOtpResponseModel;
import com.inland.pilot.Login.VerifyPinResponseModel;
import com.inland.pilot.MyTrip.RequestActiveTripListModel;
import com.inland.pilot.MyTrip.RequestTripListModel;
import com.inland.pilot.MyTrip.TripListModel;
import com.inland.pilot.MyTrip.TripMasterModel;
import com.inland.pilot.OriginOfLoads.LoadIntimationModel;
import com.inland.pilot.OriginOfLoads.RequestLoadIntimationModel;
import com.inland.pilot.RouteMaster.RouteListModel;
import com.inland.pilot.RouteMaster.RouteMasterModel;
import com.inland.pilot.VehicleMaster.RequestVehicleListModel;
import com.inland.pilot.VehicleMaster.VehicleListModel;
import com.inland.pilot.VehicleMaster.VehicleMasterModel;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface NetworkService {
    @POST("GetState")
    Call<StateListModel> getStateList();

    @POST("Verify_MobileNo")
    Call<VerifyMobileNoResponseModel> verifyMobileNo(@Body RequestModel requestModel);

    @POST("Get_OTP")
    Call<RequestOtpResponseModel> requestOtp(@Body RequestModel requestModel);

    @POST("ReSend_OTP")
    Call<RequestOtpResponseModel> resendOtp(@Body RequestModel requestModel);

    @POST("Verify_OTP")
    Call<LoginResponseModel> verifyOtp(@Body VerifyOtpModel verifyOtpModel);

    /*@POST("Pin_change")
    Call<List<LoginDetailsModel>> changePin(@Body RequestModel requestModel);*/

    @POST("Driver_save")
    Call<VerifyMobileNoResponseModel> registerUser(@Body RegistrationModel registrationModel);

 @POST("Driver_save")
    Call<VerifyMobileNoResponseModel_Message> registerUser_MESSAGE(@Body RegistrationModel registrationModel);

    @POST("Verify_PIN")
    Call<LoginResponseModel> verifyMpin(@Body SetMpinModel setMpinModel);

    @POST("Set_Pin")
    Call<VerifyMobileNoResponseModel> setPin(@Body SetMpinModel setMpinModel);

    @POST("InsertTripGsp")
    Call<VerifyMobileNoResponseModel> setGPS(@Body SetGPSModel setGPSModel);

    @POST("TripStatusupdate")
    Call<VerifyMobileNoResponseModel> setTripStatus(@Body SetTripStatusUpdateModel setTripStatusUpdateModel);

    @POST("VehicleList")
    Call<VehicleListModel> getVehicleList(@Body RequestVehicleListModel requestVehicleListModel);

    @POST("BankList")
    Call<BankListModel> getBankListAll(@Body BankDetailsModel requestBankListModel);

    @POST("InsertBank")
    Call<VerifyMobileNoResponseModel> addUpdateBankDetails(@Body BankDetailsAddModel bankMasterModel);

    @POST("GetTrip")
    Call<TripListModel> getTripList(@Body RequestActiveTripListModel requestTripListModel);

 @POST("Get_LoadIntimation")
    Call<LoadIntimationModel> getLoadIntimation(@Body RequestLoadIntimationModel requestLoadIntimationModel);

    @POST("TripList")
    Call<TripListModel> getTripListAll(@Body RequestTripListModel requestTripListModel);

    @POST("Vehicle_masteradd")
    Call<VerifyMobileNoResponseModel> addUpdateVehicleDetails(@Body VehicleMasterModel vehicleMasterModel);

    @Multipart
    @POST("DriverDoc/UploadImage")
    Call<Response<String>> uploadPhoto(@Header("Access-Token") String header, @Part MultipartBody.Part imageFile);

    @POST("RouteMaster")
    Call<VerifyMobileNoResponseModel> addUpdateRouteDetails(@Body RouteMasterModel routeMasterModel);

    @POST("RouteList")
    Call<RouteListModel> getRouteListAll(@Body RouteMasterModel requestRouteListModel);

    @POST("Get_Registration")
    Call<FetchRegResponseModel> fetchUser(@Body RequestModel requestModel);
}


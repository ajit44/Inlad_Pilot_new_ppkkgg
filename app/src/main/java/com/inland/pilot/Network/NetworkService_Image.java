package com.inland.pilot.Network;

import com.inland.pilot.Login.LoginResponseModel;
import com.inland.pilot.Login.RegistrationModel;
import com.inland.pilot.Login.RequestModel;
import com.inland.pilot.Login.RequestOtpResponseModel;
import com.inland.pilot.Login.SetMpinModel;
import com.inland.pilot.Login.StateListModel;
import com.inland.pilot.Login.VerifyMobileNoResponseModel;
import com.inland.pilot.Login.VerifyOtpModel;
import com.inland.pilot.VehicleMaster.RequestVehicleListModel;
import com.inland.pilot.VehicleMaster.VehicleListModel;
import com.inland.pilot.VehicleMaster.VehicleMasterModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface NetworkService_Image {

    @Multipart
    @POST("DriverDoc/UploadImage")
    Call<Response<String>> uploadPhoto(@Part MultipartBody.Part file, @Part("file") RequestBody name);

    @Multipart
    @POST("tripimage/Insertimage")
    Call<Response<String>> uploadLoading(@Part MultipartBody.Part file, @Part("file") RequestBody name);

    @Multipart
    @POST("Vehicalimage/InsertVehicalimage")
    Call<Response<String>> uploadVehicleImage(@Part MultipartBody.Part file, @Part("file") RequestBody name);

    @Multipart
    @POST("podimage/InsertPod")
    Call<Response<String>> uploadPod(@Part MultipartBody.Part file, @Part("file") RequestBody name);
}


package com.inland.pilot.Network;

import com.inland.pilot.LoadImage.LoadingRequestModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
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
    //Call<Response<String>> uploadLoading(@Part MultipartBody.Part file, @Body LoadingRequestModel loadingRequestModel);

    @Multipart
    @POST("Vehicalimage/InsertVehicalimage")
    Call<Response<String>> uploadVehicleImage(@Part MultipartBody.Part file, @Part("file") RequestBody name);

    @Multipart
    @POST("podimage/InsertPod")
    Call<Response<String>> uploadPod(@Part MultipartBody.Part file, @Part("file") RequestBody name);
}


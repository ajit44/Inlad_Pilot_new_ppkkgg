package com.inland.pilot.Network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient_Image {
    private static final String API_URL = "https://api.inland.in/Pilot/api/";
   // private static final String API_URL = "http://api.airmancourier.in/api/";

    private static Retrofit retrofit = null;
    private static NetworkService_Image networkService = null;

    private ApiClient_Image() {
    }

    public static NetworkService_Image getNetworkService() {
        if (networkService == null) {
            networkService = getClient().create(NetworkService_Image.class);
        }
        return networkService;
    }

    private static Retrofit getClient() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .connectTimeout(180, TimeUnit.SECONDS)
                    .readTimeout(180, TimeUnit.SECONDS)
                    .writeTimeout(180, TimeUnit.SECONDS).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}

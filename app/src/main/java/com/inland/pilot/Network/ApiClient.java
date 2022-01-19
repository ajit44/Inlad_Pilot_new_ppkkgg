package com.inland.pilot.Network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String API_URL = "https://api.inland.in/Pilot/api/Driver/";
   // private static final String API_URL = "http://api.airmancourier.in/api/Driver/";

    private static Retrofit retrofit = null;
    private static NetworkService networkService = null;

    private ApiClient() {
    }

    public static NetworkService getNetworkService() {
        if (networkService == null) {
            networkService = getClient().create(NetworkService.class);
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

package com.inland.pilot.Util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import com.inland.pilot.Base.App;
import com.inland.pilot.Login.LoginDetailsModel;

public class PreferenceUtil {
    private static SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.getContext());
    private static LoginDetailsModel loginDetailsModel = null;

    public static LoginDetailsModel getUser() {
        if (loginDetailsModel == null) {
            loginDetailsModel = new Gson().fromJson(preferences.getString("user", null), LoginDetailsModel.class);
        }
        return loginDetailsModel;
    }

    public static void clearUserData() {
        if (loginDetailsModel != null) {
            loginDetailsModel = null;
        }
    }

    public static void clearAll() {
        loginDetailsModel = null;
        preferences.edit().clear().apply();
    }

    public static void setUser(LoginDetailsModel user) {
        preferences.edit().putString("user", new Gson().toJson(user)).apply();
    }

    public static boolean isUserLoggedIn() {
        return preferences.getBoolean("is_logged_in", false);
    }

    public static void setUserLoggedIn(boolean isLoggedIn) {
        preferences.edit().putBoolean("is_logged_in", isLoggedIn).apply();
    }

    public static String getDeviceId() {
        return preferences.getString("deviceId", null);
    }

    public static void setDeviceId(String deviceId) {
        preferences.edit().putString("deviceId", deviceId).apply();
    }
    public static void setVehicleNo(String vehicleNo)
    {
        preferences.edit().putString("vehicleNo", vehicleNo).apply();
    }
    public static String getVehicleNo() {
        return preferences.getString("vehicleNo", null);
    }

    public static void setRcNo(String rc_no)
    {
        preferences.edit().putString("rc_no", rc_no).apply();
    }
    public static String getRcNo() {
        return preferences.getString("rc_no", null);
    }

    public static void setInsNo(String insNo)
    {
        preferences.edit().putString("ins_no", insNo).apply();
    }
    public static String getInsNo() {
        return preferences.getString("ins_no", null);
    }

    public static void setPermit(String permit_no)
    {
        preferences.edit().putString("permit_no", permit_no).apply();
    }
    public static String getPermit() {
        return preferences.getString("permit_no", null);
    }

    public static void clearPin() {
        preferences.edit().remove("pin").apply();
    }

    public static String getPin() {
        return preferences.getString("pin", null);
    }

    public static void setPin(String tokenNo) {
        preferences.edit().putString("pin", tokenNo).apply();
    }

    public static void clearToken() {
        preferences.edit().remove("tokenNo").apply();
    }

    public static String getTokenNo() {
        return preferences.getString("tokenNo", null);
    }

    public static void setTokenNo(String tokenNo) {
        preferences.edit().putString("tokenNo", tokenNo).apply();
    }

    public static void clearRegistrationStatus() {
        preferences.edit().remove("registrationStatus").apply();
    }

    public static String getRegistrationStatus() {
        return preferences.getString("registrationStatus", null);
    }

    public static void setRegistrationStatus(String registrationStatus) {
        preferences.edit().putString("registrationStatus", registrationStatus).apply();
    }

    public static String getDashboardStatus() {
        return preferences.getString("dashboardStatus", null);
    }

    public static void setDashboardStatus(String dashboardStatus) {
        preferences.edit().putString("dashboardStatus", dashboardStatus).apply();
    }

}

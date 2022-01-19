package com.inland.pilot.Base;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.inland.pilot.BuildConfig;

public class App extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }

        //Stetho.initializeWithDefaults(App.this);

    }

    public static Context getContext() {
        return context;
    }

}


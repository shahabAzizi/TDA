package com.tda.tda;

import android.app.Application;
import android.content.Context;


import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MyApp extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}


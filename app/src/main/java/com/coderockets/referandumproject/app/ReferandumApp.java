package com.coderockets.referandumproject.app;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import org.androidannotations.annotations.EApplication;

import io.fabric.sdk.android.Fabric;

/**
 * Created by aykutasil on 2.06.2016.
 */
@EApplication
public class ReferandumApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
    }
}

package com.coderockets.referandumproject.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.coderockets.referandumproject.BuildConfig;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.model.ModelUser;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.EApplication;

import hugo.weaving.DebugLog;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by aykutasil on 2.06.2016.
 */
@EApplication
public class ReferandumApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initReferandum();
        initActiveAndroid();
    }

    @DebugLog
    private void initReferandum() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Xenotron.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        Fabric.with(this, new Crashlytics());
        Iconify.with(new FontAwesomeModule());

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    @DebugLog
    private void initActiveAndroid() {
        ActiveAndroid.initialize(new Configuration.Builder(getApplicationContext())
                .addModelClass(ModelUser.class)
                .create());
    }

    @DebugLog
    public void initSweetLoc() {
        Logger.init("ReferandumLogger")                // default PRETTYLOGGER or use just init()
                .methodCount(3)                      // default 2
                .logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE)        // default LogLevel.FULL
                .methodOffset(0);                    // default 0
        //.hideThreadInfo()                          // default shown
        //.logAdapter(new AndroidLogAdapter());      //default AndroidLogAdapter

        Fabric.with(this, new Crashlytics());
    }

    @Override
    protected void attachBaseContext(Context base) {
        try {
            super.attachBaseContext(base);
            MultiDex.install(this);
        } catch (RuntimeException ignored) {
            // Multidex support doesn't play well with Robolectric yet
        }
    }
}

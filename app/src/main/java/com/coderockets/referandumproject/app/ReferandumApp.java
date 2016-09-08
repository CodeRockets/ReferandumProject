package com.coderockets.referandumproject.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.coderockets.referandumproject.BuildConfig;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.coderockets.referandumproject.model.ModelUser;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.fuck_boilerplate.rx_paparazzo.RxPaparazzo;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.EApplication;

import java.util.concurrent.Callable;

import hugo.weaving.DebugLog;
import io.fabric.sdk.android.Fabric;
import rx.Observable;
import rx.functions.Action1;
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
                //.setDefaultFontPath("fonts/ShadowsIntoLight.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        Fabric.with(this, new Crashlytics());
        Iconify.with(new FontAwesomeModule());

        Logger.init("ReferandumLogger")                // default PRETTYLOGGER or use just init()
                .methodCount(3)                      // default 2
                .logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE)        // default LogLevel.FULL
                .methodOffset(0);                    // default 0
        //.hideThreadInfo()                          // default shown
        //.logAdapter(new AndroidLogAdapter());      //default AndroidLogAdapter

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        RxPaparazzo.register(this);
        //LeakCanary.install(this);

    }

    @DebugLog
    private void initActiveAndroid() {
        ActiveAndroid.initialize(new Configuration.Builder(getApplicationContext())
                .addModelClass(ModelUser.class)
                .addModelClass(ModelQuestionInformation.class)
                .create());
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

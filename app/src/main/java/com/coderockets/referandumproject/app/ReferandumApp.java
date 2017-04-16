package com.coderockets.referandumproject.app;

import android.support.multidex.MultiDexApplication;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.aykuttasil.androidbasichelperlib.PrefsHelper;
import com.coderockets.referandumproject.BuildConfig;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.coderockets.referandumproject.model.ModelUser;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.miguelbcr.ui.rx_paparazzo.RxPaparazzo;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.util.UUID;

import hugo.weaving.DebugLog;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by aykutasil on 2.06.2016.
 */
public class ReferandumApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initReferandum();
        initActiveAndroid();
    }

    @DebugLog
    private void initReferandum() {

        if (PrefsHelper.readPrefString(this, Const.PREFS_KEY_REFERANDUM_UUID) == null) {
            String uuid = UUID.randomUUID().toString();
            Logger.i("UUID: " + uuid);
            PrefsHelper.writePrefString(this, Const.PREFS_KEY_REFERANDUM_UUID, uuid);
        }

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build());

        Fabric.with(this, new Crashlytics());

        Iconify.with(new FontAwesomeModule());

        Logger.init("ReferandumLogger")
                .methodCount(3)
                .logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE)
                .methodOffset(0);

        FacebookSdk.sdkInitialize(getApplicationContext());

        AppEventsLogger.activateApp(this);

        RxPaparazzo.register(this);

        FirebaseAnalytics.getInstance(getApplicationContext());

    }

    @DebugLog
    private void initActiveAndroid() {

        ActiveAndroid.initialize(new Configuration.Builder(getApplicationContext())
                .addModelClass(ModelUser.class)
                .addModelClass(ModelQuestionInformation.class)
                .create());

    }

}

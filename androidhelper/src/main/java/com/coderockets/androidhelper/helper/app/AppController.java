package com.coderockets.androidhelper.helper.app;

import android.app.Application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

/**
 * Created by aykutasil on 2.06.2016.
 */
public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Iconify.with(new FontAwesomeModule());
    }
}

package com.coderockets.referandumproject.app;

import com.facebook.stetho.Stetho;

/**
 * Created by aykutasil on 9.08.2016.
 */
public class ReferandumAppDebug extends ReferandumApp {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}


package com.coderockets.referandumproject.helper;

import com.facebook.AccessToken;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 30.07.2016.
 */
public class SuperHelper extends com.aykuttasil.androidbasichelperlib.SuperHelper {

    @DebugLog
    public static boolean checkUser() {
        return AccessToken.getCurrentAccessToken() != null;
    }
}

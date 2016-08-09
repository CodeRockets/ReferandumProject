package com.coderockets.referandumproject.helper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.facebook.AccessToken;
import com.orhanobut.logger.Logger;
import com.slmyldz.random.Randoms;

import java.util.UUID;

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

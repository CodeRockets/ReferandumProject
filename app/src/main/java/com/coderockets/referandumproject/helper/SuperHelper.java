package com.coderockets.referandumproject.helper;

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

    @DebugLog
    public static String setRandomImage(Fragment fragment, ImageView imageView) {
        String randomUrl = Randoms.imageUrl("jpg");
        Logger.i("Random Image Url: " + randomUrl);
        Glide.with(fragment)
                .load(randomUrl)
                .signature(new StringSignature(UUID.randomUUID().toString()))
                .fitCenter()
                .into(imageView);
        return randomUrl;
    }
}

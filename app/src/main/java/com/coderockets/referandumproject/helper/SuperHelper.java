package com.coderockets.referandumproject.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.orhanobut.logger.Logger;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 30.07.2016.
 */
public class SuperHelper extends com.aykuttasil.androidbasichelperlib.SuperHelper {

    @DebugLog
    public static boolean checkUser() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    public static void ReplaceFragmentBeginTransaction(AppCompatActivity activity, Fragment fragment, int containerID, String tag, boolean isBackStack) {

        FragmentManager.enableDebugLogging(true);
        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment alreadyFragment = fragmentManager.findFragmentByTag(tag);
        if (alreadyFragment == null) {
            Logger.i("alreadyFragment == null");
            if (isBackStack) {
                Logger.i("isBackStack : true");
                fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
            }
            //fragmentTransaction.commitAllowingStateLoss();
            fragmentTransaction.replace(containerID, fragment, tag);
            fragmentTransaction.commit();
        } else {
            Logger.i("alreadyFragment != null");
            fragmentTransaction.replace(containerID, alreadyFragment, tag);
            fragmentTransaction.commit();
        }


    }


}

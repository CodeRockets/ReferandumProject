package com.coderockets.referandumproject.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.facebook.AccessToken;
import com.orhanobut.logger.Logger;
import com.slmyldz.random.Randoms;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 30.07.2016.
 */
public class SuperHelper extends com.aykuttasil.androidbasichelperlib.SuperHelper {

    @DebugLog
    public static boolean checkUser() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    /**
     * İstenilen Activity içerisindeki bir container a fragment replace etmek için kullanılır.
     * Eğer replace edilmeye çalışılan Fragment daha önce replace edilmiş ise popstack yapılarak yükleme yapılır.
     *
     * @param activity
     * @param fragment
     * @param containerID
     * @param isBackStack
     */
    public static void ReplaceFragmentBeginTransaction(AppCompatActivity activity, Fragment fragment, int containerID, boolean isBackStack) {

        /*
        FragmentManager akışını loglamak istiyorsan comment satırını aktifleştir.
         */
        //FragmentManager.enableDebugLogging(true);
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        /*
        Eğer daha önce yüklenen bir fragment tekrar yüklenmeye çalışılıyor ise replace yapmak yerine popstack ile yükleme yapıyoruz.
         */
        Fragment alreadyFragment = fragmentManager.findFragmentByTag(fragment.getClass().getSimpleName());

        if (alreadyFragment == null) {
            //Logger.i("alreadyFragment == null");
            if (isBackStack) {
                //Logger.i("isBackStack : true");
                fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
            }
            fragmentTransaction.replace(containerID, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        } else {
            /*
            for (Fragment frg : fragmentManager.getFragments()) {
                if (frg != null) {
                    Logger.i(frg.getTag());
                }
            }
            */
            //Logger.i("Fragment is visible: " + alreadyFragment.isVisible());
            if (!alreadyFragment.isVisible()) {
                //Logger.i(alreadyFragment.getClass().getSimpleName());

                 /*
                 * {@link fragmentManager.popBackStackImmediate()} kullanarak popstack olup olmadığını yakalayabiliriz.
                 * Eğer fragment, uygulama ilk açıldığında yüklenen fragment ise popstack false olarak commit edileceği için
                 * popBackStackImmadiate = false döner.
                 * Bunun kontorülü yapıyoruz ve ilk yüklenen fragment herhangi bir butona basılarak tekrar yüklenmeye çalışılır ise
                 * isBackStack değişken kontrolü yaparak replace ediyoruz.
                 */
                boolean isPopStack = fragmentManager.popBackStackImmediate(alreadyFragment.getClass().getSimpleName(), 0);
                if (!isPopStack) {
                    if (isBackStack) {
                        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                    }
                    fragmentTransaction.replace(containerID, fragment, fragment.getClass().getSimpleName());
                    fragmentTransaction.commit();
                }
            }
        }


    }

    @DebugLog
    public static String setRandomImage(Context context, final ImageView imageView, String signature) {
        String randomUrl = Randoms.imageUrl("png");
        Glide.with(context)
                .load(randomUrl)
                .signature(new StringSignature(signature))
                .fitCenter()
                .into(imageView);
        Logger.i("Random Image Url: " + randomUrl);
        return randomUrl;
    }


}

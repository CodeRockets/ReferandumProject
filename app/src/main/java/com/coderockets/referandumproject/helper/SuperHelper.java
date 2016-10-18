package com.coderockets.referandumproject.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.db.DbManager;
import com.coderockets.referandumproject.model.ModelUser;
import com.coderockets.referandumproject.rest.ApiManager;
import com.coderockets.referandumproject.rest.RestModel.UserRequest;
import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.orhanobut.logger.Logger;
import com.slmyldz.random.Randoms;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import hugo.weaving.DebugLog;
import jp.wasabeef.blurry.Blurry;

/**
 * Created by aykutasil on 30.07.2016.
 */
@EBean
public class SuperHelper extends com.aykuttasil.androidbasichelperlib.SuperHelper {

    @DebugLog
    public static boolean checkUser() {
        return AccessToken.getCurrentAccessToken() != null && DbManager.getModelUser() != null;
    }

    @DebugLog
    public static String setRandomImage(Context context, final ImageView imageView, String signature) {
        String randomUrl = Randoms.imageUrl("png");
        Picasso.with(context)
                .load(randomUrl)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .placeholder(R.drawable.loading)
                .resize(1280, 680)
                .centerCrop()
                .into(imageView);
        Logger.i("Random Image Url: " + randomUrl);
        return randomUrl;
    }

    @UiThread
    @DebugLog
    public void makeBlur(Context context, View view, ImageView into) {
        Blurry.with(context)
                .async()
                .radius(25)
                .sampling(2)
                .capture(view)
                .into(into);

    }

    @DebugLog
    public static void sendFacebookToken(Context context) {
        if (checkUser()) {
            UserRequest userRequest = new UserRequest();
            userRequest.setToken(AccessToken.getCurrentAccessToken().getToken());
            ApiManager.getInstance(context).SaveUser(userRequest).subscribe();
        }
    }
//    /**
//     * İstenilen Activity içerisindeki bir container a fragment replace etmek için kullanılır.
//     * Eğer replace edilmeye çalışılan Fragment daha önce replace edilmiş ise popstack yapılarak yükleme yapılır.
//     *
//     * @param activity
//     * @param fragment
//     * @param containerID
//     * @param isBackStack
//     */
//    public static void ReplaceFragmentBeginTransaction(AppCompatActivity activity, Fragment fragment, int containerID, boolean isBackStack) {
//
//        /*
//        FragmentManager akışını loglamak istiyorsan comment satırını aktifleştir.
//         */
//        //FragmentManager.enableDebugLogging(true);
//        FragmentManager fragmentManager = activity.getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//
//        /*
//        Eğer daha önce yüklenen bir fragment tekrar yüklenmeye çalışılıyor ise replace yapmak yerine popstack ile yükleme yapıyoruz.
//         */
//        Fragment alreadyFragment = fragmentManager.findFragmentByTag(fragment.getClass().getSimpleName());
//
//        if (alreadyFragment == null) {
//            //Logger.i("alreadyFragment == null");
//            if (isBackStack) {
//                //Logger.i("isBackStack : true");
//                fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
//            }
//            fragmentTransaction.replace(containerID, fragment, fragment.getClass().getSimpleName());
//            fragmentTransaction.commit();
//        } else {
//            /*
//            for (Fragment frg : fragmentManager.getFragments()) {
//                if (frg != null) {
//                    Logger.i(frg.getTag());
//                }
//            }
//            */
//            //Logger.i("Fragment is visible: " + alreadyFragment.isVisible());
//            if (!alreadyFragment.isVisible()) {
//                //Logger.i(alreadyFragment.getClass().getSimpleName());
//
//                 /*
//                 * {@link fragmentManager.popBackStackImmediate()} kullanarak popstack olup olmadığını yakalayabiliriz.
//                 * Eğer fragment, uygulama ilk açıldığında yüklenen fragment ise popstack false olarak commit edileceği için
//                 * popBackStackImmadiate = false döner.
//                 * Bunun kontorülü yapıyoruz ve ilk yüklenen fragment herhangi bir butona basılarak tekrar yüklenmeye çalışılır ise
//                 * isBackStack değişken kontrolü yaparak replace ediyoruz.
//                 */
//                boolean isPopStack = fragmentManager.popBackStackImmediate(alreadyFragment.getClass().getSimpleName(), 0);
//                if (!isPopStack) {
//                    if (isBackStack) {
//                        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
//                    }
//                    fragmentTransaction.replace(containerID, fragment, fragment.getClass().getSimpleName());
//                    fragmentTransaction.commit();
//                }
//            }
//        }
//
//
//    }

    /**
     * Bitmap.CompressFormat can be PNG,JPEG or WEBP.
     * <p>
     * quality goes from 1 to 100. (Percentage).
     * <p>
     * dir you can get from many places like Environment.getExternalStorageDirectory() or mContext.getFilesDir()
     * depending on where you want to save the image.
     */
    @DebugLog
    public static boolean saveBitmapToFile(File dir, String fileName, Bitmap bm, Bitmap.CompressFormat format, int quality) {

        File imageFile = new File(dir, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);

            bm.compress(format, quality, fos);

            fos.close();

            return true;
        } catch (IOException e) {
            Log.e("app", e.getMessage());
            CrashlyticsLog(e);
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return false;
    }

    public static void CrashlyticsLog(Throwable e) {
        ModelUser modelUser = DbManager.getModelUser();
        if (modelUser != null) {
            Crashlytics.setUserIdentifier(modelUser.getUserId());
            Crashlytics.setUserName(modelUser.getName());
        }

        Crashlytics.logException(e);
    }

}

package com.coderockets.referandumproject.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.db.DbManager;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.model.Event.ResetEvent;
import com.coderockets.referandumproject.model.Event.UpdateLoginEvent;
import com.coderockets.referandumproject.model.ModelUser;
import com.coderockets.referandumproject.rest.ApiManager;
import com.coderockets.referandumproject.rest.RestModel.UserRequest;
import com.coderockets.referandumproject.util.PicassoCircleTransform;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Set;

import hugo.weaving.DebugLog;
import rx.Subscription;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by aykutasil on 2.06.2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    AccessTokenTracker mAccessTokenTracker;
    ProfileTracker mProfileTracker;

    abstract void updateUi();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAccesTokenTracker();
        setProfileTracker();
    }

    @DebugLog
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @DebugLog
    public void setAccesTokenTracker() {

        mAccessTokenTracker = new AccessTokenTracker() {
            @DebugLog
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                AccessToken.setCurrentAccessToken(currentAccessToken);

                EventBus.getDefault().postSticky(new ResetEvent());

                if (currentAccessToken != null) {
                    Set<String> deniedPermissions = currentAccessToken.getDeclinedPermissions();

                    if (deniedPermissions.contains("user_friends")) {
                        LoginManager.getInstance().logInWithReadPermissions(BaseActivity.this, Arrays.asList("user_friends"));
                    } else {
                        saveUser(currentAccessToken.getToken());
                    }
                } else {
                    DbManager.deleteModelUser();
                    updateUi();
                }
            }
        };

        mAccessTokenTracker.startTracking();
    }

    public void setProfileTracker() {

        mProfileTracker = new ProfileTracker() {
            @DebugLog
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Profile.setCurrentProfile(currentProfile);
            }
        };

        mProfileTracker.startTracking();
    }

    @DebugLog
    public void saveUser(String token) {

        MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                .cancelable(false)
                .icon(new IconDrawable(this, FontAwesomeIcons.fa_angle_right).actionBarSize().colorRes(com.aykuttasil.androidbasichelperlib.R.color.accent))
                .content("Lütfen Bekleyiniz..")
                .progress(true, 0)
                .show();

        UserRequest userRequest = new UserRequest();
        userRequest.setToken(token);

        Logger.i(userRequest.getToken());
        //Logger.i(SuperHelper.getDeviceId(BaseActivity.this));
        //Logger.i("User save request: " + new Gson().toJson(userRequest));

        try {
            Subscription subscription = ApiManager.getInstance(this).SaveUser(userRequest)
                    .subscribe(response -> {
                                Logger.i(response.getData().getName());
                                response.getData().save();
                                EventBus.getDefault().postSticky(new UpdateLoginEvent());
                                updateUi();
                            }, error -> {
                                materialDialog.dismiss();
                                SuperHelper.CrashlyticsLog(error);
                                UiHelper.UiDialog.newInstance(this).getOKDialog("HATA", error.getMessage(), null).show();
                            },
                            materialDialog::dismiss
                    );
            //mListSubscription.add(subscription);
        } catch (Exception e) {
            SuperHelper.CrashlyticsLog(e);
            materialDialog.dismiss();
            e.printStackTrace();
            //UiHelper.UiSnackBar.showSimpleSnackBar(getView(), e.getMessage(), Snackbar.LENGTH_INDEFINITE);
        }

    }

    @DebugLog
    public void updateProfileIcon(MenuItem menuItem) {
        ModelUser modelUser = DbManager.getModelUser();
        if (modelUser != null) {

            WeakReference<MenuItem> itemWeakReference = new WeakReference<>(menuItem);
            Target target = new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    MenuItem menuItem = itemWeakReference.get();

                    if (menuItem != null) {
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                        drawable.setBounds(0, 0, 100, 100);
                        menuItem.setIcon(drawable);
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.with(this)
                    .load(modelUser.getProfileImageUrl())
                    .transform(new PicassoCircleTransform())
                    .into(target);

        } else {
            menuItem.setIcon(R.drawable.ic_account_circle_pink_900_48dp);
        }
    }

    @DebugLog
    @Override
    protected void onDestroy() {
        mAccessTokenTracker.stopTracking();
        mProfileTracker.stopTracking();

        super.onDestroy();
    }
}

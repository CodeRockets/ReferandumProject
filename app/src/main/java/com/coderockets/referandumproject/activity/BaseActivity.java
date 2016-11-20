package com.coderockets.referandumproject.activity;

import android.content.Context;
import android.content.Intent;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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
    public CallbackManager mCallbackManager;

    abstract void updateUi();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAccesTokenTracker();
        setProfileTracker();
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @DebugLog
            @Override
            public void onSuccess(LoginResult loginResult) {
                Logger.i("AccessToken: " + loginResult.getAccessToken());
                Logger.i("registerCallback: onSuccess()");
            }

            @DebugLog
            @Override
            public void onCancel() {
                Logger.i("registerCallback: onCancel()");
            }

            @DebugLog
            @Override
            public void onError(FacebookException error) {
                Logger.e(error, "HATA");
            }
        });
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

                if (currentAccessToken != null) {

                    for (String prms : AccessToken.getCurrentAccessToken().getPermissions()) {
                        Logger.i("Facebook Permission Access Token: " + prms);
                    }
                    for (String prms : currentAccessToken.getPermissions()) {
                        Logger.i("Facebook Permission Current: " + prms);
                    }
                }

                EventBus.getDefault().postSticky(new ResetEvent());

                if (currentAccessToken != null) {
                    Set<String> deniedPermissions = currentAccessToken.getDeclinedPermissions();

                    if (deniedPermissions.contains("user_friends")) {
                        LoginManager.getInstance().logInWithReadPermissions(BaseActivity.this, Arrays.asList("user_friends"));
                    } else if (!currentAccessToken.getPermissions().contains("publish_actions")) { // Facebook da yayın yapmak için izin istiyoruz
                        LoginManager.getInstance().logInWithPublishPermissions(BaseActivity.this, Arrays.asList("publish_actions"));
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

        } catch (Exception e) {
            SuperHelper.CrashlyticsLog(e);
            materialDialog.dismiss();
            e.printStackTrace();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @DebugLog
    @Override
    protected void onDestroy() {
        mAccessTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
        super.onDestroy();
    }
}

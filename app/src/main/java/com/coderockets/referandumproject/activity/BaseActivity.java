package com.coderockets.referandumproject.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.coderockets.referandumproject.db.DbManager;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.model.Event.ResetEvent;
import com.coderockets.referandumproject.model.Event.UpdateLoginEvent;
import com.coderockets.referandumproject.rest.ApiManager;
import com.coderockets.referandumproject.rest.RestModel.UserRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

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
                    saveUser(currentAccessToken.getToken());
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
    @Override
    protected void onDestroy() {
        mAccessTokenTracker.stopTracking();
        mProfileTracker.stopTracking();

        super.onDestroy();
    }
}

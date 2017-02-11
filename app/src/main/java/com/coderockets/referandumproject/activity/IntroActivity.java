package com.coderockets.referandumproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.db.DbManager;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.model.Event.ResetEvent;
import com.coderockets.referandumproject.model.Event.UpdateLoginEvent;
import com.coderockets.referandumproject.rest.ApiManager;
import com.coderockets.referandumproject.rest.RestModel.UserRequest;
import com.coderockets.referandumproject.rest.RestModel.UserResponse;
import com.coderockets.referandumproject.util.view.LoginSlide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.Set;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;
import hugo.weaving.DebugLog;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by aykutasil on 31.01.2017.
 */

public class IntroActivity extends MaterialIntroActivity {


    public static final String SHOW_JUST_LOGIN_SLIDE = "ShowJustLoginSlide";

    AccessTokenTracker mAccessTokenTracker;
    public CallbackManager mCallbackManager;
    ImageButton mButtonNext;

    @DebugLog
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mButtonNext = (ImageButton) findViewById(R.id.button_next);

        setAccesTokenTracker();
        mCallbackManager = CallbackManager.Factory.create();
        fbRegisterCallback();

        enableLastSlideAlphaExitTransition(true);

        setSkipButtonVisible();

        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @DebugLog
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });


        if (getIntent() != null && getIntent().getBooleanExtra(SHOW_JUST_LOGIN_SLIDE, false)) {
            addSlide(new LoginSlide());
            return;
        }

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.slide1_background)
                .buttonsColor(R.color.slide1_buttons)
                .image(R.drawable.ekrangoruntusu1)
                .description(getResources().getString(R.string.slide1))
                .build());


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.slide2_background)
                .buttonsColor(R.color.slide2_buttons)
                .image(R.drawable.ekrangoruntusu2)
                .description(getResources().getString(R.string.slide2))
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.slide3_background)
                .buttonsColor(R.color.slide3_buttons)
                .image(R.drawable.ekrangoruntusu3)
                .description(getResources().getString(R.string.slide3))
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.slide4_background)
                .buttonsColor(R.color.slide4_buttons)
                .image(R.drawable.ekrangoruntusu4)
                .description(getResources().getString(R.string.slide4))
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.slide5_background)
                .buttonsColor(R.color.slide5_buttons)
                .image(R.drawable.ekrangoruntusu5)
                .description(getResources().getString(R.string.slide5))
                .build());

        /*
        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.third_slide_background)
                        .buttonsColor(R.color.third_slide_buttons)
                        .possiblePermissions(new String[]{Manifest.permission.GET_ACCOUNTS})
                        .neededPermissions(new String[]{Manifest.permission.CAMERA})
                        .image(R.drawable.anonym)
                        .title("We provide best tools")
                        .description("ever")
                        .build(),
                new MessageButtonBehaviour(v -> showMessage("Try us!"), "Tools"));
                */

        addSlide(new LoginSlide());

    }

    private void fbRegisterCallback() {

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
                        LoginManager.getInstance().logInWithReadPermissions(IntroActivity.this, Arrays.asList("user_friends"));
                    } else {
                        saveUser(currentAccessToken.getToken());
                    }
                } else {
                    DbManager.deleteModelUser();
                }
            }
        };

        mAccessTokenTracker.startTracking();
    }

    @DebugLog
    public void saveUser(String token) {

        MaterialDialog materialDialog = new MaterialDialog.Builder(IntroActivity.this)
                .cancelable(false)
                .icon(new IconDrawable(IntroActivity.this, FontAwesomeIcons.fa_angle_right).actionBarSize().colorRes(com.aykuttasil.androidbasichelperlib.R.color.accent))
                .content("Lütfen Bekleyiniz..")
                .progress(true, 0)
                .show();

        UserRequest userRequest = new UserRequest();
        userRequest.setToken(token);

        Logger.i(userRequest.getToken());

        try {
            ApiManager.getInstance(IntroActivity.this).SaveUser(userRequest)
                    .flatMap(new Func1<UserResponse, Observable<UserResponse>>() {
                        @Override
                        public Observable<UserResponse> call(UserResponse response) {

                            Logger.i(response.getData().getName());

                            response.getData().save();

                            EventBus.getDefault().postSticky(new UpdateLoginEvent());

                            return Observable.just(response);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {

                                //mButtonNext.performClick();

                                MainActivity_.intent(IntroActivity.this)
                                        .flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        .start();

                            }, error -> {
                                materialDialog.dismiss();

                                SuperHelper.CrashlyticsLog(error);

                                UiHelper.UiDialog.newInstance(IntroActivity.this).getOKDialog("HATA", error.getMessage(), null).show();
                            },
                            materialDialog::dismiss
                    );

        } catch (Exception e) {
            SuperHelper.CrashlyticsLog(e);
            materialDialog.dismiss();
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //moveTaskToBack(true);
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
        super.onDestroy();
    }

}

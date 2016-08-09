package com.coderockets.referandumproject.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.MainActivity;
import com.coderockets.referandumproject.app.Const;
import com.coderockets.referandumproject.db.DbManager;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.rest.RestClient;
import com.coderockets.referandumproject.rest.RestModel.UserRequest;
import com.coderockets.referandumproject.rest.RestModel.UserResponse;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;
import jp.wasabeef.blurry.Blurry;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by aykutasil on 2.06.2016.
 */
@EFragment(R.layout.fragment_profile_layout)
public class ProfileFragment extends BaseFragment {

    @ViewById(R.id.ProfileMainLayout)
    ViewGroup mProfileMainLayout;

    @ViewById(R.id.ProfileLoginLayout)
    ViewGroup mProfileLoginLayout;

    @ViewById(R.id.LoginButton)
    LoginButton mLoginButton;

    @ViewById(R.id.ButtonCikisYap)
    Button mButtonCikisYap;

    @ViewById(R.id.ImageViewLoginBackground)
    ImageView mImageViewLoginBackground;
    //
    Context mContext;
    MainActivity mActivity;
    CallbackManager mCallbackManager;
    AccessTokenTracker mAccessTokenTracker;
    ProfileTracker mProfileTracker;


    @DebugLog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @DebugLog
    @AfterViews
    public void ProfileFragmentInit() {
        mContext = getActivity();
        mActivity = (MainActivity) getActivity();
        mCallbackManager = CallbackManager.Factory.create();
        //
        setLoginButton();
        setAccesTokenTracker();
        setProfileTracker();
        updateUI();
    }

    @DebugLog
    @Override
    public void onResume() {
        super.onResume();
    }

    private void setLoginButton() {
        mLoginButton.setReadPermissions("public_profile");
        mLoginButton.setFragment(this);


        /*
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Logger.i("Login -> onSucess -> Token:" + loginResult.getAccessToken().getToken());
                updateUI();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        */

    }

    private void setAccesTokenTracker() {
        mAccessTokenTracker = new AccessTokenTracker() {
            @DebugLog
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                AccessToken.setCurrentAccessToken(currentAccessToken);

                if (SuperHelper.checkPermissions(mContext, Const.PERMISSIONS_GENERAL)) {
                    if (currentAccessToken != null) {
                        saveUser(currentAccessToken.getToken());
                    }
                } else {
                    ActivityCompat.requestPermissions(mActivity, Const.PERMISSIONS_GENERAL, Const.PERMISIONS_REQUEST_GENERAL);
                }

            }
        };
        mAccessTokenTracker.startTracking();
    }

    @DebugLog
    private void saveUser(String token) {
        UserRequest userRequest = new UserRequest();
        userRequest.setToken(token);

        Observable<UserResponse> api = RestClient.getInstance().getApiService().User(
                Const.CLIENT_ID,
                Const.REFERANDUM_VERSION,
                SuperHelper.getDeviceId(mContext),
                userRequest
        );
        api.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                            response.getData().save();
                            updateUI();
                        },
                        onError -> {
                            UiHelper.UiDialog.newInstance(mContext).getOKDialog("HATA", onError.getMessage(), null).show();
                        }
                );

    }

    private void setProfileTracker() {
        mProfileTracker = new ProfileTracker() {
            @DebugLog
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                Profile.setCurrentProfile(currentProfile);
                //updateUI();
            }
        };
        mProfileTracker.startTracking();
    }


    @DebugLog
    private void updateUI() {

        // Kullanıcı Login değilse
        if (!SuperHelper.checkUser()) {
            hideMainContent();
            showLoginContent();
            mActivity.makeBlur(mContext, mImageViewLoginBackground, mImageViewLoginBackground);
        } else {
            Blurry.delete(mProfileMainLayout);
            hideLoginContent();
            showMainContent();
        }
    }

    private void showLoginContent() {
        mProfileLoginLayout.setVisibility(View.VISIBLE);
    }

    private void hideLoginContent() {
        mProfileLoginLayout.setVisibility(View.GONE);
    }

    private void showMainContent() {
        mProfileMainLayout.setVisibility(View.VISIBLE);
    }

    private void hideMainContent() {
        mProfileMainLayout.setVisibility(View.GONE);
    }

    @DebugLog
    @Click(R.id.ButtonCikisYap)
    public void ButtonCikisYapClick() {
        LoginManager.getInstance().logOut();
        DbManager.deleteModelUser();
        updateUI();
    }

    @DebugLog
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @DebugLog
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Const.PERMISIONS_REQUEST_GENERAL: {

                boolean allPermissionGranted = true;
                for (int a = 0; a < permissions.length; a++) {
                    if (grantResults[a] != PackageManager.PERMISSION_GRANTED) {
                        allPermissionGranted = false;
                        break;
                    }
                }
                if (allPermissionGranted) {
                    Logger.i("Permissions Granted !");
                    saveUser(AccessToken.getCurrentAccessToken().getToken());
                } else {
                    ActivityCompat.requestPermissions(mActivity, Const.PERMISSIONS_GENERAL, Const.PERMISIONS_REQUEST_GENERAL);
                }
            }
        }
    }

    @DebugLog
    @Override
    public void onPause() {
        super.onPause();
    }

    @DebugLog
    @Override
    public void onDestroyView() {
        mAccessTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
        super.onDestroyView();
    }

}

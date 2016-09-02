package com.coderockets.referandumproject.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.MainActivity;
import com.coderockets.referandumproject.db.DbManager;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.rest.ApiManager;
import com.coderockets.referandumproject.rest.RestModel.UserRequest;
import com.coderockets.referandumproject.util.adapter.MyFragmentPagerAdapter;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;
import jp.wasabeef.blurry.Blurry;
import rx.android.schedulers.AndroidSchedulers;

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

    @ViewById(R.id.tabs)
    TabLayout mTabs;

    @ViewById(R.id.ProfileViewPager)
    ViewPager mProfileViewPager;
    //
    Context mContext;
    MainActivity mActivity;
    CallbackManager mCallbackManager;
    AccessTokenTracker mAccessTokenTracker;
    ProfileTracker mProfileTracker;
    RxPermissions mRxPermissions;


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
        mRxPermissions = RxPermissions.getInstance(mContext);
        mRxPermissions.setLogging(true);
        //
        setLoginButton();
        setAccesTokenTracker();
        setProfileTracker();
        updateUI();
    }

    @DebugLog
    private void updateUI() {

        // Kullanıcı Login değilse
        if (!SuperHelper.checkUser()) {
            if (DbManager.getModelUser() == null && AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut();
            }
            hideMainContent();
            showLoginContent();
            mActivity.makeBlur(mContext, mImageViewLoginBackground, mImageViewLoginBackground);
        } else {
            Blurry.delete(mProfileMainLayout);
            hideLoginContent();
            showMainContent();
            //setTabs();
            setupViewPager(mProfileViewPager);
            mTabs.setupWithViewPager(mProfileViewPager);
        }
    }

    private void setupViewPager(ViewPager viewPager) {

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getChildFragmentManager());
        adapter.addFragment(ProfileMe_.builder().build(), "Profil");
        adapter.addFragment(ProfileMyQuestions_.builder().build(), "Sorularım");
        viewPager.setAdapter(adapter);
    }

    private void setLoginButton() {
        mLoginButton.setReadPermissions("public_profile");
        mLoginButton.setFragment(this);
    }

    @DebugLog
    public void saveUser(String token) {

        MaterialDialog materialDialog = new MaterialDialog.Builder(mContext)
                .cancelable(false)
                .icon(new IconDrawable(mContext, FontAwesomeIcons.fa_angle_right).actionBarSize().colorRes(com.aykuttasil.androidbasichelperlib.R.color.accent))
                .content("Lütfen Bekleyiniz..")
                .progress(true, 0)
                .show();

        UserRequest userRequest = new UserRequest();
        userRequest.setToken(token);

        Logger.i(userRequest.getToken());
        Logger.i(SuperHelper.getDeviceId(mContext));
        Logger.i("User save request: " + new Gson().toJson(userRequest));

        try {
            ApiManager.getInstance(mContext).SaveUser(userRequest)
                    .subscribe(response -> {
                                Logger.i(response.getData().getName());
                                response.getData().save();
                                updateUI();

                            }, error -> {
                                materialDialog.dismiss();
                                UiHelper.UiSnackBar.showSimpleSnackBar(getView(), error.getMessage(), Snackbar.LENGTH_INDEFINITE);
                            },
                            materialDialog::dismiss
                    );
        } catch (Exception e) {
            materialDialog.dismiss();
            e.printStackTrace();
            //UiHelper.UiSnackBar.showSimpleSnackBar(getView(), e.getMessage(), Snackbar.LENGTH_INDEFINITE);
        }

    }

    private void setAccesTokenTracker() {
        mAccessTokenTracker = new AccessTokenTracker() {
            @DebugLog
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                AccessToken.setCurrentAccessToken(currentAccessToken);

                if (!mRxPermissions.isGranted(Manifest.permission.READ_PHONE_STATE)) {

                    mRxPermissions.request(Manifest.permission.READ_PHONE_STATE)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(granted -> {
                                if (granted) {
                                    Logger.i("Permission granted !");
                                    saveUser(currentAccessToken.getToken());
                                } else {
                                    Logger.i("Permission denied !");
                                }
                            }, error -> {
                                UiHelper.UiDialog.showSimpleDialog(mContext, "RxPermission", error.getMessage());
                            });
                } else {

                    // Eğer izin onaylanmış ise ve giriş yapılmaya çalışılır ise buraya girer
                    Logger.i("Permission is granted.");
                    Logger.i("Access Token: " + currentAccessToken);
                    if (currentAccessToken != null) {
                        saveUser(currentAccessToken.getToken());
                    }
                    // Çıkış yapıldığında buraya girer.
                    else {
                        DbManager.deleteModelUser();
                        updateUI();
                    }
                }
            }
        };
        mAccessTokenTracker.startTracking();
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
        mLoginButton.performClick();
    }


    /*
    private void clickLoginButton() {
        // Must be done during an initialization phase like onCreate
        RxView.clicks(mButtonCikisYap)
                .compose(RxPermissions.getInstance(mContext).ensure(Manifest.permission.READ_PHONE_STATE))
                .subscribe(granted -> {
                    // R.id.enableCamera has been clicked
                });
    }
    */

    /*
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
    */

    @DebugLog
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @DebugLog
    @Override
    public void onDestroyView() {
        mAccessTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
        super.onDestroyView();
    }

}

package com.coderockets.referandumproject.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.db.DbManager;
import com.coderockets.referandumproject.fragment.ProfileMe_;
import com.coderockets.referandumproject.fragment.ProfileMyFavorites_;
import com.coderockets.referandumproject.fragment.ProfileMyQuestions_;
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
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;
import jp.wasabeef.blurry.Blurry;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by aykutasil on 5.09.2016.
 */
@EActivity(R.layout.activiy_profile)
public class ProfileActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.ProfileMainLayout)
    ViewGroup mProfileMainLayout;

    @ViewById(R.id.ProfileLoginLayout)
    ViewGroup mProfileLoginLayout;

    @ViewById(R.id.LoginButton)
    public LoginButton mLoginButton;

    @ViewById(R.id.ImageViewLoginBackground)
    ImageView mImageViewLoginBackground;

    @ViewById(R.id.ImageViewLoginBackgroundContainer)
    LinearLayout mImageViewLoginBackgroundContainer;

    @ViewById(R.id.tabs)
    TabLayout mTabs;

    @ViewById(R.id.ProfileViewPager)
    ViewPager mProfileViewPager;
    //
    CallbackManager mCallbackManager;
    AccessTokenTracker mAccessTokenTracker;
    ProfileTracker mProfileTracker;
    RxPermissions mRxPermissions;

    @DebugLog
    @AfterViews
    public void ProfileActivityInit() {
        mCallbackManager = CallbackManager.Factory.create();
        mRxPermissions = RxPermissions.getInstance(this);
        mRxPermissions.setLogging(true);
        //
        setToolbar();
        setLoginButton();
        setAccesTokenTracker();
        setProfileTracker();
        updateUI();
    }

    @DebugLog
    private void setToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //getSupportActionBar().setHomeAsUpIndicator(new IconDrawable(this, FontAwesomeIcons.fa_home).actionBarSize().getCurrent());
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
            makeBlur(ProfileActivity.this, mImageViewLoginBackground, mImageViewLoginBackground);
        } else {
            Blurry.delete(mProfileMainLayout);
            hideLoginContent();
            showMainContent();
            setupViewPager(mProfileViewPager);
            mTabs.setupWithViewPager(mProfileViewPager);
        }
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

    private void setupViewPager(ViewPager viewPager) {

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ProfileMe_.builder().build(), "Profil");
        adapter.addFragment(ProfileMyQuestions_.builder().build(), "Sorularım");
        adapter.addFragment(ProfileMyFavorites_.builder().build(), "Favorilerim");
        viewPager.setAdapter(adapter);
    }

    private void setLoginButton() {
        mLoginButton.setReadPermissions("public_profile");
    }

    @DebugLog
    public void saveUser(String token) {

        MaterialDialog materialDialog = new MaterialDialog.Builder(ProfileActivity.this)
                .cancelable(false)
                .icon(new IconDrawable(ProfileActivity.this, FontAwesomeIcons.fa_angle_right).actionBarSize().colorRes(com.aykuttasil.androidbasichelperlib.R.color.accent))
                .content("Lütfen Bekleyiniz..")
                .progress(true, 0)
                .show();

        UserRequest userRequest = new UserRequest();
        userRequest.setToken(token);

        Logger.i(userRequest.getToken());
        Logger.i(SuperHelper.getDeviceId(ProfileActivity.this));
        Logger.i("User save request: " + new Gson().toJson(userRequest));

        try {
            ApiManager.getInstance(ProfileActivity.this).SaveUser(userRequest)
                    .subscribe(response -> {
                                Logger.i(response.getData().getName());
                                response.getData().save();
                                updateUI();

                            }, error -> {
                                materialDialog.dismiss();
                                UiHelper.UiSnackBar.showSimpleSnackBar(getCurrentFocus(), error.getMessage(), Snackbar.LENGTH_INDEFINITE);
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
                                UiHelper.UiDialog.showSimpleDialog(ProfileActivity.this, "RxPermission", error.getMessage());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @DebugLog
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        mAccessTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
        super.onDestroy();
    }

}

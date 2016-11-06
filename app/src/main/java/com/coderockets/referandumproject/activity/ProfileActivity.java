package com.coderockets.referandumproject.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.db.DbManager;
import com.coderockets.referandumproject.fragment.ProfileMyFavorites_;
import com.coderockets.referandumproject.fragment.ProfileMyQuestions_;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.model.Event.UpdateLoginEvent;
import com.coderockets.referandumproject.model.ModelUser;
import com.coderockets.referandumproject.util.adapter.MyFragmentPagerAdapter;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;
import jp.wasabeef.blurry.Blurry;
import rx.Subscription;

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
    RxPermissions mRxPermissions;
    List<Subscription> mListSubscription;

    @DebugLog
    @AfterViews
    public void ProfileActivityInit() {
        mRxPermissions = RxPermissions.getInstance(this);
        mListSubscription = new ArrayList<>();
        //
        setToolbar();
        setLoginButton();
        updateUi();
    }

    @DebugLog
    private void setToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_indigo_300_24dp);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //getSupportActionBar().setHomeAsUpIndicator(new IconDrawable(this, FontAwesomeIcons.fa_home).actionBarSize().getCurrent());
    }

    private void setLoginButton() {
        mLoginButton.setReadPermissions("public_profile", "email", "user_friends");
    }

    @DebugLog
    @Override
    public void updateUi() {

        // Kullanıcı Login değilse
        if (!SuperHelper.checkUser()) {
            //if (DbManager.getModelUser() == null && AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
            //}
            hideMainContent();
            showLoginContent();
            makeBlur(ProfileActivity.this, mImageViewLoginBackground, mImageViewLoginBackground);
        } else {
            Blurry.delete(mProfileMainLayout);
            hideLoginContent();
            showMainContent();
            setupViewPager(mProfileViewPager);
            mTabs.setupWithViewPager(mProfileViewPager);
            setUi();
        }
    }

    @DebugLog
    private void setUi() {
        ModelUser modelUser = DbManager.getModelUser();
        mToolbar.setTitle(modelUser.getName());
        supportInvalidateOptionsMenu();
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
        //adapter.addFragment(ProfileMe_.builder().build(), "Profil");
        adapter.addFragment(ProfileMyQuestions_.builder().build(), "Sorularım");
        adapter.addFragment(ProfileMyFavorites_.builder().build(), "Favorilerim");
        viewPager.setAdapter(adapter);

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_profil_menu, menu);
        updateProfileIcon(menu.findItem(R.id.menuProfil));
        return super.onCreateOptionsMenu(menu);
    }

    @DebugLog
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //
        switch (item.getItemId()) {
            case android.R.id.home: {
                NavUtils.navigateUpFromSameTask(this);
                return true;
            }
            case R.id.menuCikisYap: {
                mLoginButton.performClick();
                EventBus.getDefault().postSticky(new UpdateLoginEvent());
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {

        for (Subscription subscription : mListSubscription) {
            if (!subscription.isUnsubscribed()) {
                //subscription.unsubscribe();
            }
        }

        super.onDestroy();
    }

}

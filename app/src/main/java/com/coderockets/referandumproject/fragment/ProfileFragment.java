package com.coderockets.referandumproject.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.MainActivity;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;
import jp.wasabeef.blurry.Blurry;

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

    @DebugLog
    @AfterViews
    public void ProfileFragmentInit() {
        mContext = getActivity();
        mActivity = (MainActivity) getActivity();
        mCallbackManager = CallbackManager.Factory.create();
        //
        updateUI();
    }

    @DebugLog
    @Override
    public void onResume() {
        super.onResume();
    }


    @DebugLog
    private void updateUI() {

        // Kullanıcı Login değilse
        if (!SuperHelper.checkUser()) {
            hideMainContent();
            showLoginContent();
            mActivity.makeBlur(mContext, mImageViewLoginBackground, mImageViewLoginBackground);
            setLoginButton();
        } else {
            Blurry.delete(mProfileMainLayout);
            hideLoginContent();
            showMainContent();
        }
    }

    @DebugLog
    private void setLoginButton() {
        mLoginButton.setReadPermissions("public_profile", "email");
        mLoginButton.setFragment(this);
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
    }

    @DebugLog
    private void showLoginContent() {
        mProfileLoginLayout.setVisibility(View.VISIBLE);
    }

    @DebugLog
    private void hideLoginContent() {
        mProfileLoginLayout.setVisibility(View.GONE);
    }

    @DebugLog
    private void showMainContent() {
        mProfileMainLayout.setVisibility(View.VISIBLE);
    }

    @DebugLog
    private void hideMainContent() {
        mProfileMainLayout.setVisibility(View.GONE);
    }

    @Click(R.id.ButtonCikisYap)
    public void ButtonCikisYapClick() {
        LoginManager.getInstance().logOut();
        updateUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @DebugLog
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}

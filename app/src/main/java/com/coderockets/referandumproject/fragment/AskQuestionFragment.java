package com.coderockets.referandumproject.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.MainActivity;
import com.coderockets.referandumproject.rest.RestModel.SoruSorRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.orhanobut.logger.Logger;
import com.slmyldz.random.Randoms;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;
import jp.wasabeef.blurry.Blurry;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
@EFragment(R.layout.fragment_askquestion_layout)
public class AskQuestionFragment extends BaseFragment {

    @ViewById(R.id.EditText_SoruText)
    EditText mEditText_SoruText;

    @ViewById(R.id.AskQuestionMainLayout)
    LinearLayout mAskQuestionMainLayout;

    @ViewById(R.id.AskQuestionLoginLayout)
    LinearLayout mAskQuestionLoginLayout;

    @ViewById(R.id.FabAddImage)
    FloatingActionButton mFabAddImage;

    @ViewById(R.id.LoginButton)
    LoginButton mLoginButton;
    //
    Context mContext;
    MainActivity mActivity;
    CallbackManager mCallbackManager;

    @AfterViews
    @DebugLog
    public void AskQuestionFragmentInit() {
        this.mContext = getActivity();
        this.mActivity = (MainActivity) getActivity();
        mCallbackManager = CallbackManager.Factory.create();
        //
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!checkUser()) {
            mFabAddImage.hide();
            makeBlur();
            showLoginContent();
            mLoginButton.setReadPermissions("public_profile", "email");
            mLoginButton.setFragment(this);
            mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });
        }
    }

    @DebugLog
    private void showLoginContent() {
        mAskQuestionLoginLayout.setVisibility(View.VISIBLE);
    }

    @DebugLog
    public boolean checkUser() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    @DebugLog
    @Click(R.id.Button_SoruGonder)
    public void Button_SoruGonderClick() {

        SoruSorRequest soruSorRequest = new SoruSorRequest();
        soruSorRequest.setUserId("123");
        soruSorRequest.setQuestionText(mEditText_SoruText.getText().toString());
        soruSorRequest.setQuestionImage(Randoms.imageUrl("png"));

        //ApiManager.getInstance(mContext).SoruSor(soruSorRequest);

    }

    @Click(R.id.FabAddImage)
    public void FabClick() {
        makeBlur();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        //.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    public void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(mContext)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("SALLA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }


    @org.androidannotations.annotations.UiThread
    @DebugLog
    public void makeBlur() {
        Blurry.delete(mAskQuestionMainLayout);
        Blurry.with(mContext)
                .radius(25)
                .sampling(2)
                .onto(mAskQuestionMainLayout);
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

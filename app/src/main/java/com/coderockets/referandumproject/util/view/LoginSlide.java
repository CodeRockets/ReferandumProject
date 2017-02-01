package com.coderockets.referandumproject.util.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.facebook.login.widget.LoginButton;

import agency.tango.materialintroscreen.SlideFragment;
import hugo.weaving.DebugLog;

public class LoginSlide extends SlideFragment {

    LoginButton mLoginButton;

    @DebugLog
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.slide_login_layout, container, false);

        mLoginButton = (LoginButton) view.findViewById(R.id.LoginButton);

        setLoginButton();

        return view;
    }

    @DebugLog
    @Override
    public int backgroundColor() {
        return R.color.slide6_background;
    }

    @DebugLog
    @Override
    public int buttonsColor() {
        return R.color.slide6_buttons;
    }

    @DebugLog
    @Override
    public boolean canMoveFurther() {
        return SuperHelper.checkUser();
    }

    @DebugLog
    @Override
    public String cantMoveFurtherErrorMessage() {
        return "Giriş yapmanız gerekmektedir.";
    }

    @DebugLog
    private void setLoginButton() {
        mLoginButton.setReadPermissions("public_profile", "email", "user_friends");
    }

}
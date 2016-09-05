package com.coderockets.referandumproject.fragment;

import android.content.Context;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.MainActivity;
import com.coderockets.referandumproject.activity.ProfileActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

/**
 * Created by aykutasil on 2.09.2016.
 */
@EFragment(R.layout.profile_myquestions_layout)
public class ProfileMyQuestions extends BaseProfile {


    //
    Context mContext;
    ProfileActivity mActivity;

    @AfterViews
    public void ProfileMeInit() {
        this.mContext = getActivity();
        this.mActivity = (ProfileActivity) getActivity();
    }
}

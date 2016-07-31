package com.coderockets.referandumproject.fragment;

import android.content.Context;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.MainActivity;
import com.facebook.FacebookActivity;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 2.06.2016.
 */
@EFragment(R.layout.fragment_profile_layout)
public class ProfileFragment extends BaseFragment {

    Context mContext;
    MainActivity mActivity;

    @DebugLog
    @AfterViews
    public void ProfileFragmentInit() {
        mContext = getActivity();
        mActivity = (MainActivity) getActivity();
        //
    }

    @DebugLog
    private void checkUser() {

    }

}

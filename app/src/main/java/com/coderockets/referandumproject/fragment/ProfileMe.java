package com.coderockets.referandumproject.fragment;

import android.content.Context;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.MainActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

/**
 * Created by aykutasil on 2.09.2016.
 */
@EFragment(R.layout.profil_me_layout)
public class ProfileMe extends BaseProfile {


    //
    Context mContext;
    MainActivity mActivity;

    @AfterViews
    public void ProfileMeInit() {
        this.mContext = getActivity();
        this.mActivity = (MainActivity) getActivity();
    }

}

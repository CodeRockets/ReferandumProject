package com.coderockets.referandumproject.fragment;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.ProfileActivity;
import com.coderockets.referandumproject.db.DbManager;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import de.hdodenhof.circleimageview.CircleImageView;
import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 2.09.2016.
 */
@EFragment(R.layout.profil_me_layout)
public class ProfileMe extends BaseProfile implements AppBarLayout.OnOffsetChangedListener {

    @ViewById(R.id.LinearLayoutCollapsingInf)
    LinearLayout mLinearLayoutCollapsingInf;

    @ViewById(R.id.TextViewToolbarTitle)
    TextView mTextViewToolbarTitle;

    @ViewById(R.id.ButtonCikisYap)
    Button mButtonCikisYap;

    @ViewById(R.id.Appbarlayout)
    AppBarLayout mProfileMeAppBarLayout;

    @ViewById(R.id.ImageviewCollapsingBackground)
    ImageView mImageviewCollapsingBackground;

    @ViewById(R.id.CircleimageviewProfilePicture)
    CircleImageView mCircleimageviewProfilePicture;

    //@ViewById(R.id.ImageViewProfileMe)
    //ImageView mImageViewProfileMe;
    //
    Context mContext;
    ProfileActivity mActivity;

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.7f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 300;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    @DebugLog
    @AfterViews
    public void ProfileMeInit() {
        this.mContext = getActivity();
        this.mActivity = (ProfileActivity) getActivity();
        setProfilePicture();
        mProfileMeAppBarLayout.addOnOffsetChangedListener(this);
    }

    private void setProfilePicture() {

        Uri profileImageUri = Uri.parse(DbManager.getModelUser().getProfileImageUrl());

        Picasso.with(mContext)
                .load(profileImageUri)
                .placeholder(mContext.getDrawable(R.drawable.anonym))
                .into(mImageviewCollapsingBackground);


        Picasso.with(mContext)
                .load(profileImageUri)
                .placeholder(mContext.getDrawable(R.drawable.anonym))
                .into(mCircleimageviewProfilePicture);
    }

    @DebugLog
    @Click(R.id.ButtonCikisYap)
    public void ButtonCikisYapClick() {
        mActivity.mLoginButton.performClick();
    }

    @DebugLog
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        Logger.i("maxScroll: " + maxScroll);
        Logger.i("percentage: " + percentage);

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    @DebugLog
    private void handleToolbarTitleVisibility(float percentage) {
//
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTextViewToolbarTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTextViewToolbarTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }

        }

    }

    @DebugLog
    private void handleAlphaOnTitle(float percentage) {

        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {

            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mLinearLayoutCollapsingInf, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mLinearLayoutCollapsingInf, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }

        }
    }

    @DebugLog
    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
}

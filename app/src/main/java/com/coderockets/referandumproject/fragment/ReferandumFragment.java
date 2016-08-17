package com.coderockets.referandumproject.fragment;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.MainActivity;
import com.coderockets.referandumproject.util.CustomButton;
import com.coderockets.referandumproject.util.adapter.CustomViewPagerAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;

@EFragment(R.layout.fragment_referandum_layout)
public class ReferandumFragment extends BaseFragment {

    @ViewById(R.id.ViewPagerSorular)
    ViewPager mViewPagerSorular;

    @ViewById(R.id.ButtonTrue)
    CustomButton mButtonTrue;


    @ViewById(R.id.ButtonFalse)
    CustomButton mButtonFalse;


    Context mContext;
    MainActivity mActivity;
    CustomViewPagerAdapter mViewPagerAdapter;

    @DebugLog
    @AfterViews
    public void ReferandumFragmentInit() {
        this.mContext = getActivity();
        this.mActivity = (MainActivity) getActivity();
        //
        setViewPager();
        setSorular();

    }

    private void setViewPager() {
        mViewPagerAdapter = new CustomViewPagerAdapter();

        mViewPagerSorular.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPagerSorular.setAdapter(mViewPagerAdapter);
    }

    @DebugLog
    private void setSorular() {

        LinearLayout cardView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.question_layout, null);
        mViewPagerAdapter.addView(cardView);
        //mViewPagerAdapter.addView(cardView);
        //mViewPagerAdapter.addView(cardView);

    }


    @Click(R.id.ButtonTrue)
    public void ButtonTrueClick() {
        mButtonTrue.changeButtonScale();
        //setSorular();


    }


}

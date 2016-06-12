package com.coderockets.referandumproject.activity;


import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.fragment.AskQuestionFragment_;
import com.coderockets.referandumproject.fragment.ProfileFragment;
import com.coderockets.referandumproject.fragment.ProfileFragment_;
import com.coderockets.referandumproject.fragment.ReferandumFragment_;
import com.coderockets.referandumproject.util.adapter.MyFragmentPagerAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


import hugo.weaving.DebugLog;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.viewPager)
    ViewPager mViewPager;

    @ViewById(R.id.tabLayout)
    TabLayout mTabLayout;

    @DebugLog
    @AfterViews
    public void MainActivityInit() {
        //UiHelper.UiDialog.newInstance(this).getOKDialog("Deneme Title", "Deneme Content", null).show();
        setToolbar();
        setupViewPager(mViewPager);
        setTabLayout();
    }

    @DebugLog
    private void setToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("REFERANDUM");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @DebugLog
    private void setupViewPager(ViewPager viewPager) {
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(AskQuestionFragment_.builder().build(), "Soru Sor");
        adapter.addFragment(ReferandumFragment_.builder().build(), "Referandum");
        adapter.addFragment(ProfileFragment_.builder().build(), "Profil");
        viewPager.setAdapter(adapter);
    }

    @DebugLog
    private void setTabLayout() {
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_receipt_black_24dp);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_thumb_up_black_24dp);
        mTabLayout.getTabAt(2).setIcon(R.drawable.ic_perm_identity_black_24dp);


    }


}

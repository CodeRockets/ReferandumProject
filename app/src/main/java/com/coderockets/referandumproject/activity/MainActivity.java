package com.coderockets.referandumproject.activity;


import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.fragment.AskQuestionFragment_;
import com.coderockets.referandumproject.fragment.ProfileFragment_;
import com.coderockets.referandumproject.fragment.ReferandumFragment;
import com.coderockets.referandumproject.fragment.ReferandumFragment_;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    //@ViewById(R.id.viewPager)
    //ViewPager mViewPager;

    //@ViewById(R.id.tabLayout)
    //TabLayout mTabLayout;
    //
    public static int FRAGMENT_CONTAINER = R.id.Container;

    @DebugLog
    @AfterViews
    public void MainActivityInit() {
        setToolbar();
        setFragment();
        //setupViewPager(mViewPager);
        //setTabLayout();
    }

    @DebugLog
    private void setFragment() {
        SuperHelper.ReplaceFragmentBeginTransaction(
                this,
                ReferandumFragment_.builder().build(),
                FRAGMENT_CONTAINER,
                ReferandumFragment.class.getSimpleName(),
                false);
    }

    @DebugLog
    private void setToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("REFERANDUM");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(new IconDrawable(this, FontAwesomeIcons.fa_home).actionBarSize().getCurrent());
    }


    /*
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
    */

    @DebugLog
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @DebugLog
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAskQuestion: {

                if (!SuperHelper.checkUser()) {
                    SuperHelper.ReplaceFragmentBeginTransaction(
                            this,
                            ProfileFragment_.builder().build(),
                            MainActivity.FRAGMENT_CONTAINER,
                            MainActivity.class.getSimpleName(),
                            true);
                } else {
                    SuperHelper.ReplaceFragmentBeginTransaction(
                            this,
                            AskQuestionFragment_.builder().build(),
                            MainActivity.FRAGMENT_CONTAINER,
                            MainActivity.class.getSimpleName(),
                            true);
                }
                break;
            }
            case R.id.menuProfil: {
                SuperHelper.ReplaceFragmentBeginTransaction(
                        this,
                        ProfileFragment_.builder().build(),
                        MainActivity.FRAGMENT_CONTAINER,
                        MainActivity.class.getSimpleName(),
                        true);
                break;
            }
            case android.R.id.home: {
                SuperHelper.ReplaceFragmentBeginTransaction(
                        this,
                        ReferandumFragment_.builder().build(),
                        MainActivity.FRAGMENT_CONTAINER,
                        MainActivity.class.getSimpleName(),
                        true);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

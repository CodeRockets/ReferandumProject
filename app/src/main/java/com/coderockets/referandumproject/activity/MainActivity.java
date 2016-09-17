package com.coderockets.referandumproject.activity;


import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.fragment.ReferandumFragment_;
import com.coderockets.referandumproject.helper.SuperHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    public static int FRAGMENT_CONTAINER = R.id.Container;

    @DebugLog
    @AfterViews
    public void MainActivityInit() {
        setToolbar();
        setFragment();
    }

    @DebugLog
    private void setFragment() {
        SuperHelper.ReplaceFragmentBeginTransaction(
                this,
                ReferandumFragment_.builder().build(),
                FRAGMENT_CONTAINER,
                false);
    }

    @DebugLog
    private void setToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Referandum");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.mipmap.ic_launcher));
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
                    Intent activityIntent = new Intent(this, ProfileActivity_.class);
                    activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(activityIntent);

                    /*
                    SuperHelper.ReplaceFragmentBeginTransaction(
                            this,
                            ProfileFragment_.builder().build(),
                            FRAGMENT_CONTAINER,
                            true);
                            */
                } else {
                    Intent activityIntent = new Intent(this, QuestionActivity_.class);
                    activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(activityIntent);

                    /*
                    SuperHelper.ReplaceFragmentBeginTransaction(
                            this,
                            AskQuestionFragment_.builder().build(),
                            FRAGMENT_CONTAINER,
                            true);
                            */
                }
                break;
            }
            case R.id.menuProfil: {
                Intent activityIntent = new Intent(this, ProfileActivity_.class);
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(activityIntent);

                /*SuperHelper.ReplaceFragmentBeginTransaction(
                        this,
                        ProfileFragment_.builder().build(),
                        FRAGMENT_CONTAINER,
                        true);*/
                break;
            }
            case android.R.id.home: {
                SuperHelper.ReplaceFragmentBeginTransaction(
                        this,
                        ReferandumFragment_.builder().build(),
                        FRAGMENT_CONTAINER,
                        true);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

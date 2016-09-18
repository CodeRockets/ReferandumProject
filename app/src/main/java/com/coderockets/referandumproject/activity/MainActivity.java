package com.coderockets.referandumproject.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.db.DbManager;
import com.coderockets.referandumproject.fragment.ReferandumFragment_;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.model.Event.UpdateLoginEvent;
import com.coderockets.referandumproject.model.ModelUser;
import com.coderockets.referandumproject.util.PicassoCircleTransform;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

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

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
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
        updateUi(menu.getItem(0));
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
                } else {
                    Intent activityIntent = new Intent(this, QuestionActivity_.class);
                    activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(activityIntent);
                }
                break;
            }
            case R.id.menuProfil: {
                Intent activityIntent = new Intent(this, ProfileActivity_.class);
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(activityIntent);
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

    @DebugLog
    private void updateUi(MenuItem menuItem) {
        ModelUser modelUser = DbManager.getModelUser();
        if (modelUser != null) {

            WeakReference<MenuItem> itemWeakReference = new WeakReference<>(menuItem);
            Target target = new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    MenuItem menuItem = itemWeakReference.get();

                    if (menuItem != null) {
                        Drawable drawable = new BitmapDrawable(bitmap);
                        //drawable.setBounds(0, 0, targetWidth, targetHeight);
                        menuItem.setIcon(drawable);
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.with(this)
                    .load(modelUser.getProfileImageUrl())
                    .transform(new PicassoCircleTransform())
                    .into(target);

        }
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(UpdateLoginEvent loginEvent) {
        updateUi(mToolbar.getMenu().getItem(0));
        EventBus.getDefault().removeStickyEvent(loginEvent);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }
}

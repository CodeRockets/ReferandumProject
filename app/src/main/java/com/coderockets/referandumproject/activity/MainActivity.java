package com.coderockets.referandumproject.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.fragment.ReferandumFragment_;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.model.Event.UpdateLoginEvent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import co.mobiwise.materialintro.shape.Focus;
import hugo.weaving.DebugLog;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    public Toolbar mToolbar;

    private final String INTRO_KEY_ASK_QUESTION = "ask_question3";

    public static int FRAGMENT_CONTAINER = R.id.Container;

    @DebugLog
    @AfterViews
    public void MainActivityInit() {

        if (SuperHelper.checkUser()) {

            setToolbar();

            setFragment();

            SuperHelper.sendFacebookToken(this);

        } else {
            Intent i = new Intent(MainActivity.this, IntroActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

    }

    @DebugLog
    @Override
    protected void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);

        if (SuperHelper.checkUser() && mToolbar.findViewById(R.id.menuAskQuestion) != null) {

            View vi = mToolbar.findViewById(R.id.menuAskQuestion);

            SuperHelper.showIntro(this,
                    vi,
                    s -> {
                    },
                    INTRO_KEY_ASK_QUESTION,
                    "Sorunuzu buradan oluşturabilirsiniz",
                    Focus.MINIMUM);
        }

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

    @DebugLog
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_main_menu, menu);

        updateProfileIcon(menu.getItem(0));

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
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(UpdateLoginEvent loginEvent) {
        if (mToolbar != null && mToolbar.getMenu().size() > 0) {
            updateProfileIcon(mToolbar.getMenu().getItem(0));
        }
    }

    @Override
    public void updateUi() {

    }

    @DebugLog
    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @DebugLog
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

package com.coderockets.referandumproject.activity;


import android.support.v7.widget.Toolbar;

import com.coderockets.androidhelper.helper.UiHelper;
import com.coderockets.referandumproject.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @DebugLog
    @AfterViews
    public void MainActivityInit() {
        //UiHelper.UiDialog.newInstance(this).getOKDialog("Deneme Title", "Deneme Content", null).show();
        setToolbar();
    }

    @DebugLog
    private void setToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("REFERANDUM");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }


}

package com.coderockets.referandumproject.activity;


import com.coderockets.androidhelper.helper.UiHelper;
import com.coderockets.referandumproject.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import hugo.weaving.DebugLog;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {


    @DebugLog
    @AfterViews
    public void MainActivityInit() {
        UiHelper.UiDialog.newInstance(this).getOKDialog("Deneme Title", "Deneme Content", null).show();
    }

}

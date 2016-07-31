package com.coderockets.referandumproject.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.androidannotations.annotations.EActivity;

import hugo.weaving.DebugLog;
import jp.wasabeef.blurry.Blurry;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by aykutasil on 2.06.2016.
 */
@EActivity
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @org.androidannotations.annotations.UiThread
    @DebugLog
    public void makeBlur(Context context, ViewGroup viewGroup) {
        Blurry.delete(viewGroup);
        Blurry.with(context)
                .radius(25)
                .sampling(2)
                .onto(viewGroup);
    }

    @org.androidannotations.annotations.UiThread
    @DebugLog
    public void makeBlur(Context context, View view, ImageView into) {
        Blurry.with(context)
                .radius(25)
                .sampling(2)
                .capture(view)
                .into(into);
    }
}

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

    @DebugLog
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        /*
        switch (requestCode) {
            case 123: {

                boolean allPermissionGranted = true;
                for (int a = 0; a < permissions.length; a++) {
                    if (grantResults[a] == PackageManager.PERMISSION_GRANTED) {
                        allPermissionGranted = true;
                    } else {
                        allPermissionGranted = false;
                        break;
                    }
                }
                if (!allPermissionGranted) {
                    final MaterialDialog dialog = UiHelper.UiDialog.newInstance(this, 1).getOKDialog("Reaktif Uygulama İzinleri", "Tüm izinleri vermeniz gerekmektedir !", null);
                    dialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            setResult(Const.ACTIVITY_RESULT_CODE_SIGNUP_ERROR);
                            finish();
                        }
                    });
                    dialog.show();
                }
                break;
            }
            default: {
                setResult(Const.ACTIVITY_RESULT_CODE_SIGNUP_ERROR);
                finish();
                //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
        */

    }
}

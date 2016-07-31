package com.coderockets.referandumproject.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.MainActivity;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.rest.RestModel.SoruSorRequest;
import com.slmyldz.random.Randoms;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
@EFragment(R.layout.fragment_askquestion_layout)
public class AskQuestionFragment extends BaseFragment {

    @ViewById(R.id.EditText_SoruText)
    EditText mEditText_SoruText;

    @ViewById(R.id.AskQuestionMainLayout)
    LinearLayout mAskQuestionMainLayout;

    @ViewById(R.id.FabAddImage)
    FloatingActionButton mFabAddImage;
    //
    Context mContext;
    MainActivity mActivity;

    @AfterViews
    @DebugLog
    public void AskQuestionFragmentInit() {
        this.mContext = getActivity();
        this.mActivity = (MainActivity) getActivity();
        //
        updateUI();
    }

    @DebugLog
    @Override
    public void onResume() {
        super.onResume();
    }


    @DebugLog
    private void updateUI() {
        if (!SuperHelper.checkUser()) {
            SuperHelper.ReplaceFragmentBeginTransaction(mActivity,
                    ProfileFragment_.builder().build(),
                    MainActivity.FRAGMENT_CONTAINER,
                    ProfileFragment.class.getSimpleName(),
                    false);
        }
    }

    @DebugLog
    @Click(R.id.Button_SoruGonder)
    public void Button_SoruGonderClick() {

        SoruSorRequest soruSorRequest = new SoruSorRequest();
        soruSorRequest.setUserId("123");
        soruSorRequest.setQuestionText(mEditText_SoruText.getText().toString());
        soruSorRequest.setQuestionImage(Randoms.imageUrl("png"));

        //ApiManager.getInstance(mContext).SoruSor(soruSorRequest);

    }

    @Click(R.id.FabAddImage)
    public void FabClick() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        //.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    public void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(mContext)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("SALLA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }
}

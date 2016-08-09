package com.coderockets.referandumproject.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.MainActivity;
import com.coderockets.referandumproject.app.Const;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.rest.ApiManager;
import com.coderockets.referandumproject.rest.RestModel.SoruSorRequest;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.slmyldz.random.Randoms;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;


@EFragment(R.layout.fragment_askquestion_layout)
public class AskQuestionFragment extends BaseFragment {

    @ViewById(R.id.EditText_SoruText)
    EditText mEditText_SoruText;

    @ViewById(R.id.AskQuestionMainLayout)
    LinearLayout mAskQuestionMainLayout;

    @ViewById(R.id.FabRefreshImage)
    FloatingActionButton mFabRefreshImage;

    @ViewById(R.id.FabUploadImage)
    FloatingActionButton mFabUploadImage;

    @ViewById(R.id.ImageView_SoruImage)
    ImageView mImageView_SoruImage;
    //
    Context mContext;
    MainActivity mActivity;

    @DebugLog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
        this.mActivity = (MainActivity) getActivity();
    }

    @AfterViews
    @DebugLog
    public void AskQuestionFragmentInit() {
        setFab();
        updateUI();
    }

    private void setFab() {
        mFabRefreshImage.setImageDrawable(new IconDrawable(mContext, FontAwesomeIcons.fa_refresh).sizeDp(150).color(R.color.color4).getCurrent());
        mFabUploadImage.setImageDrawable(new IconDrawable(mContext, FontAwesomeIcons.fa_upload).sizeDp(150).color(R.color.color4).getCurrent());
    }

    @DebugLog
    @Override
    public void onResume() {
        super.onResume();
    }


    @DebugLog
    private void updateUI() {
        SuperHelper.setRandomImage(mContext, mImageView_SoruImage);
    }

    @DebugLog
    @Click(R.id.FabRefreshImage)
    public void FabRefreshImageClick() {
        SuperHelper.setRandomImage(mContext, mImageView_SoruImage);
    }

    @DebugLog
    @Click(R.id.FabUploadImage)
    public void FabUploadImageClick() {
        //SuperHelper.setRandomImage(this, mImageView_SoruImage);
    }

    @DebugLog
    @Click(R.id.Button_SoruGonder)
    public void Button_SoruGonderClick() {
        if (!SuperHelper.checkPermissions(mContext, Const.PERMISSIONS_GENERAL)) {
            ActivityCompat.requestPermissions(mActivity, Const.PERMISSIONS_GENERAL, Const.PERMISIONS_REQUEST_GENERAL);
            return;
        }
        sendQuestionRequest();
    }

    @DebugLog
    public void sendQuestionRequest() {
        SoruSorRequest soruSorRequest = SoruSorRequest.SoruSorRequestInstance();
        soruSorRequest.setQuestionText(mEditText_SoruText.getText().toString());
        soruSorRequest.setQuestionImage(Randoms.imageUrl("png"));

        ApiManager.getInstance(mContext).SoruSor(soruSorRequest);
    }

    @DebugLog
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Const.PERMISIONS_REQUEST_GENERAL: {
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
                    sendQuestionRequest();
                } else {
                    ActivityCompat.requestPermissions(mActivity, Const.PERMISSIONS_GENERAL, Const.PERMISIONS_REQUEST_GENERAL);
                }
            }
            default: {

            }
        }
    }

    /*
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
    */

}

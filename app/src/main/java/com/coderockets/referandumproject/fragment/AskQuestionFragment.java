package com.coderockets.referandumproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.MainActivity;
import com.coderockets.referandumproject.app.Const;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.rest.RestClient;
import com.coderockets.referandumproject.rest.RestModel.SoruSorRequest;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.slmyldz.random.Randoms;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


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
    RxPermissions mRxPermissions;

    @DebugLog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
        this.mActivity = (MainActivity) getActivity();
        mRxPermissions = RxPermissions.getInstance(mContext);
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

        mRxPermissions.requestEach(Const.PERMISSIONS_GENERAL)
                .subscribe(permissions -> {
                    if (permissions.granted) {
                        sendQuestionRequest();
                    } else {
                        UiHelper.UiSnackBar.showSimpleSnackBar(getView(),
                                "Soru gönderebilmeniz için gerekli izinleri vermelisiniz !",
                                Snackbar.LENGTH_INDEFINITE);
                    }
                });
    }

    @DebugLog
    public void sendQuestionRequest() {

        if (validateIsEmpty(mEditText_SoruText)) return;

        SoruSorRequest soruSorRequest = SoruSorRequest.SoruSorRequestInstance();
        soruSorRequest.setQuestionText(mEditText_SoruText.getText().toString());
        soruSorRequest.setQuestionImage(Randoms.imageUrl("png"));

        MaterialDialog materialDialog = new MaterialDialog.Builder(mContext)
                .cancelable(false)
                .icon(new IconDrawable(mContext, FontAwesomeIcons.fa_angle_right).actionBarSize().colorRes(com.aykuttasil.androidbasichelperlib.R.color.accent))
                .title("Sorunuz Gönderiliyor")
                .content("Lütfen Bekleyiniz...")
                .progress(true, 0)
                .show();

        RestClient.getInstance().getApiService().SoruSor(
                Const.CLIENT_ID,
                Const.REFERANDUM_VERSION,
                SuperHelper.getDeviceId(mContext),
                soruSorRequest
        )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    UiHelper.UiSnackBar.showSimpleSnackBar(getView(), "Sorunuz gönderildi.", Snackbar.LENGTH_INDEFINITE);
                }, error -> {
                    UiHelper.UiDialog.showSimpleDialog(mContext, "HATA", error.getMessage());
                }, materialDialog::dismiss);
    }

    private boolean validateIsEmpty(EditText... editTexts) {

        boolean flag = false;
        for (EditText editText : editTexts) {
            if (editText.getText().toString().length() == 0) {
                editText.setError("Boş bırakmayınız !");
                flag = true;
            } else {
                editText.setError(null);
            }
        }
        return flag;
    }

}

package com.coderockets.referandumproject.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.app.Const;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.rest.ApiManager;
import com.coderockets.referandumproject.rest.RestModel.SoruSorRequest;
import com.coderockets.referandumproject.util.AutoFitTextView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.miguelbcr.ui.rx_paparazzo.RxPaparazzo;
import com.miguelbcr.ui.rx_paparazzo.entities.Options;
import com.miguelbcr.ui.rx_paparazzo.entities.size.ScreenSize;
import com.nightonke.jellytogglebutton.JellyToggleButton;
import com.nightonke.jellytogglebutton.State;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import hugo.weaving.DebugLog;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by aykutasil on 5.09.2016.
 */
@Fullscreen
@EActivity(R.layout.activity_question)
public class QuestionActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.EditText_SoruText)
    EditText mEditText_SoruText;

    @ViewById(R.id.AskQuestionMainLayout)
    ViewGroup mAskQuestionMainLayout;

    @ViewById(R.id.FabUploadImage)
    FabSpeedDial mFabUploadImage;

    @ViewById(R.id.ImageView_SoruImage)
    ImageView mImageView_SoruImage;

    @ViewById(R.id.AutoFitTextViewSoru)
    AutoFitTextView mAutoFitTextViewSoru;

    @ViewById(R.id.JellyToggleButtonFriendNotif)
    JellyToggleButton mJellyToggleButtonFriendNotif;

    @ViewById(R.id.HeaderContainer)
    ViewGroup mHeaderContainer;

    @ViewById(R.id.FriendNotifContainer)
    ViewGroup mFriendNotifContainer;

    RxPermissions mRxPermissions;
    JellyToggleButton mJellyToggleButtonPrivateQuestion;
    MaterialDialog materialDialog;
    private String mQuestionImageFilePath = null;


    @DebugLog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        mRxPermissions = new RxPermissions(this);
    }

    @AfterViews
    @DebugLog
    @Override
    public void initAfterViews() {
        if (materialDialog == null) {
            materialDialog = UiHelper.UiDialog.newInstance(this).getProgressDialog("Lütfen Bekleyiniz..", null);
        }
        setToolbar();
        setFab();
        setReactiveEditText();
    }

    @DebugLog
    private void setToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Soru Sor");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_indigo_300_24dp);
        getSupportActionBar().setHomeButtonEnabled(true);

        //getSupportActionBar().setHomeAsUpIndicator(new IconDrawable(this, FontAwesomeIcons.fa_home).actionBarSize().getCurrent());
    }

    private void setReactiveEditText() {
        RxTextView.textChanges(mEditText_SoruText)
                .map(charSequence -> {
                    if (charSequence.length() > 0) {
                        //return charSequence.toString().substring(0, 1).toUpperCase() + charSequence.toString().substring(1).toLowerCase();
                        return charSequence.toString().toLowerCase();
                    } else {
                        return "?";
                    }
                })
                .subscribe(text -> {
                    mAutoFitTextViewSoru.setText(text);
                }, error -> {
                    error.printStackTrace();
                    SuperHelper.CrashlyticsLog(error);
                });
    }

    private void setFab() {
        mFabUploadImage.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_capture: {
                        //captureImage();
                        captureImageWithCrop();
                        break;
                    }
                    case R.id.action_galery: {
                        pickupImage();
                        break;
                    }
                }
                return false;
            }

            @Override
            public void onMenuClosed() {

            }
        });
    }

    @Override
    public void updateUi() {

    }

    @DebugLog
    @Click(R.id.Button_SoruGonder)
    public void Button_SoruGonderClick() {
        mRxPermissions.request(Const.PERMISSIONS_ASK_QUESTION)
                .subscribe(granted -> {
                            if (granted) {
                                SoruSorRequest request = SoruSorRequest.SoruSorRequestInstance();
                                request.setPrivate(mJellyToggleButtonPrivateQuestion.isChecked());
                                request.setNotifyFriend(mJellyToggleButtonFriendNotif.isChecked());
                                sendQuestionRequest(request);
                            } else {
                                UiHelper.UiSnackBar.showSimpleSnackBar(getCurrentFocus(),
                                        "Soru gönderebilmeniz için gerekli izinleri vermelisiniz !",
                                        Snackbar.LENGTH_INDEFINITE);
                            }
                        }, error -> {
                            UiHelper.UiDialog.showSimpleDialog(QuestionActivity.this, "HATA", error.getMessage());
                            SuperHelper.CrashlyticsLog(error);
                        }
                );
    }

    @DebugLog
    public void sendQuestionRequest(SoruSorRequest request) {
        if (SuperHelper.validateIsEmpty(mEditText_SoruText)) return;

        materialDialog.show();
        try {
            // Eğer soru resmi yüklenmemiş ise
            if (mQuestionImageFilePath == null) {
                String randomImageUrl = "";
                request.setQuestionText(mEditText_SoruText.getText().toString());
                request.setQuestionImage(randomImageUrl);

                if (mJellyToggleButtonPrivateQuestion.isChecked()) {
                    request.setNotifyFriend(false);
                    sendPrivateQuestionWithDefaultImage(request);
                } else {
                    sendPublicQuestionWithDefaultImage(request);
                }
            } else {
                File file = new File(mQuestionImageFilePath);
                Logger.i("FilePath: " + mQuestionImageFilePath);

                Map<String, RequestBody> map = new HashMap<>();
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
                map.put("file\"; filename=\"" + "file", requestBody);

                if (mJellyToggleButtonPrivateQuestion.isChecked()) {
                    request.setNotifyFriend(false);
                    sendPrivateQuestionWithImage(request, map);
                } else {
                    sendPublicQuestionWithImage(request, map);
                }
            }
        } catch (Exception error) {
            materialDialog.dismiss();
            SuperHelper.CrashlyticsLog(error);
            UiHelper.UiDialog.showSimpleDialog(QuestionActivity.this, "HATA", error.getMessage());
        }
    }

    private void sendPublicQuestionWithImage(SoruSorRequest request, Map<String, RequestBody> map) {
        ApiManager.getInstance(this).ImageUpload(map)
                .flatMap(response -> {
                    Logger.i("Image Url: " + response.getData());
                    request.setQuestionText(mEditText_SoruText.getText().toString());
                    request.setQuestionImage(response.getData());

                    return ApiManager.getInstance(this).SoruSor(request);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    {
                        mQuestionImageFilePath = null;
                        mEditText_SoruText.setText("");
                        //UiHelper.UiSnackBar.showSimpleSnackBar(getCurrentFocus(), "Sorunuz gönderildi.", Snackbar.LENGTH_LONG);
                        EventBus.getDefault().postSticky(response.getData());
                        NavUtils.navigateUpFromSameTask(QuestionActivity.this);
                    }
                }, error -> {
                    materialDialog.dismiss();
                    SuperHelper.CrashlyticsLog(error);
                    error.printStackTrace();
                    UiHelper.UiDialog.showSimpleDialog(QuestionActivity.this, "HATA", error.getMessage());
                }, materialDialog::dismiss);
    }

    private void sendPrivateQuestionWithImage(SoruSorRequest request, Map<String, RequestBody> map) {
        ApiManager.getInstance(this).ImageUpload(map)
                .flatMap(response -> {
                    Logger.i("Image Url: " + response.getData());
                    request.setQuestionText(mEditText_SoruText.getText().toString());
                    request.setQuestionImage(response.getData());
                    request.setPrivate(true);
                    request.setPrivateUrl("");
                    return ApiManager.getInstance(this).SoruSor(request);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    materialDialog.dismiss();
                    Logger.d(resp);
                    //NavUtils.navigateUpFromSameTask(QuestionActivity.this);
                }, error ->
                {
                    materialDialog.dismiss();
                    UiHelper.UiDialog.showSimpleDialog(QuestionActivity.this, "HATA", error.getMessage());
                    SuperHelper.CrashlyticsLog(error);
                    Logger.e(error, "HATA");
                });
    }

    private void sendPublicQuestionWithDefaultImage(SoruSorRequest soruSorRequest) {
        ApiManager.getInstance(this).SoruSor(soruSorRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    materialDialog.dismiss();
                    mQuestionImageFilePath = null;
                    mEditText_SoruText.setText("");
                    EventBus.getDefault().postSticky(response.getData());
                    NavUtils.navigateUpFromSameTask(QuestionActivity.this);
                }, error ->
                {
                    materialDialog.dismiss();
                    Logger.e(error, "HATA");
                });
    }

    private void sendPrivateQuestionWithDefaultImage(SoruSorRequest soruSorRequest) {
        ApiManager.getInstance(this).SoruSor(soruSorRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    Logger.d(resp);
                    materialDialog.dismiss();
                    SuperHelper.sharePrivateUrl(QuestionActivity.this, "Referandum'da yeni bir soru sordum", resp.getData().getPrivateUrl(), true);
                }, error ->
                {
                    materialDialog.dismiss();
                    Logger.e(error, "HATA");
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_question, menu);
        mJellyToggleButtonPrivateQuestion = (JellyToggleButton) MenuItemCompat.getActionView(menu.findItem(R.id.MenuPrivateQuestion)).findViewById(R.id.JellyToggleButtonPrivateQuestion);
        mJellyToggleButtonPrivateQuestion.setOnStateChangeListener((process, state, jtb) -> {
            Transition transition = new Slide(Gravity.END);
            transition.setDuration(500);
            TransitionManager.beginDelayedTransition(mHeaderContainer, transition);
            mFriendNotifContainer.setVisibility(state == State.LEFT ? View.VISIBLE : View.GONE);
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void captureImage() {
        RxPaparazzo.takeImage(this)
                .size(new ScreenSize())
                .useInternalStorage()
                .usingCamera()
                .subscribe(response -> {

                    if (response.resultCode() != Activity.RESULT_OK) {
                        response.targetUI().showUserCanceled();
                        return;
                    }

                    Logger.i(response.data());
                    mQuestionImageFilePath = response.data();
                    response.targetUI().loadImage(response.data());
                });
    }

    private void captureImageWithCrop() {
        Options options = new Options();
        //options.useSourceImageAspectRatio();
        options.setToolbarTitle("Düzenle");
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        //options.setMaxResultSize(1600, 980);
        options.setAspectRatio(16, 6);
        //options.setAspectRatio(100, 60);
        //options.setMaxResultSize(640, 480);
        //options.setActiveWidgetColor(getResources().getColor(R.color.bpDark_gray)); //
        //options.setCropGridColor(Color.BLUE);
        //options.setLogoColor(Color.BLACK);

        RxPaparazzo.takeImage(this)
                .size(new ScreenSize())
                .crop(options)
                //.useInternalStorage()
                .usingCamera()
                .subscribe(response -> {
                    if (response.resultCode() != Activity.RESULT_OK) {
                        response.targetUI().showUserCanceled();
                        return;
                    }

                    Logger.i(response.data());
                    mQuestionImageFilePath = response.data();
                    response.targetUI().loadImage(response.data());
                }, error -> {
                    error.printStackTrace();
                    SuperHelper.CrashlyticsLog(error);
                });
    }

    private void pickupImage() {
        Options options = new Options();
        //options.useSourceImageAspectRatio();
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        options.setToolbarTitle("Düzenle");
        //options.setMaxResultSize(1600, 980);
        options.setAspectRatio(16, 6);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        //options.withAspectRatio(150,60);

        RxPaparazzo.takeImage(this)
                //.useInternalStorage()
                .crop(options)
                .size(new ScreenSize())
                .usingGallery()
                .subscribe(response -> {
                    Logger.i(response.data());
                    if (response.resultCode() != Activity.RESULT_OK) {
                        response.targetUI().showUserCanceled();
                        return;
                    }
                    Logger.i(response.data());
                    mQuestionImageFilePath = response.data();
                    response.targetUI().loadImage(response.data());
                }, error -> {
                    error.printStackTrace();
                    SuperHelper.CrashlyticsLog(error);
                    UiHelper.UiDialog.showSimpleDialog(QuestionActivity.this, "HATA", error.getMessage());
                    //UiHelper.UiSnackBar.showSimpleSnackBar(mToolbar, error.getMessage(), Snackbar.LENGTH_INDEFINITE);
                });
    }

    private void pickupImages() {
        RxPaparazzo.takeImages(this)
                .useInternalStorage()
                .crop()
                .size(new ScreenSize())
                .usingGallery()
                .subscribe(response -> {
                    if (response.resultCode() != Activity.RESULT_OK) {
                        response.targetUI().showUserCanceled();
                        return;
                    }

                    if (response.data().size() == 1)
                        response.targetUI().loadImage(response.data().get(0));
                    //else response.targetUI().loadImages(response.data());
                });
    }

    @DebugLog
    private void loadImage(String filePath) {

        mImageView_SoruImage.setScaleType(ImageView.ScaleType.CENTER);

        File imageFile = new File(filePath);
        Picasso.with(this)
                .load(imageFile)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .placeholder(R.drawable.loading)
                .into(mImageView_SoruImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        //photoViewAttacher.update();
                        mImageView_SoruImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    private void showUserCanceled() {
        Toast.makeText(this, "İptal edildi.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home: {
                NavUtils.navigateUpFromSameTask(this);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}

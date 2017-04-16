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
import android.view.Menu;
import android.view.MenuItem;
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
import com.coderockets.referandumproject.rest.RestModel.DynamicLinkRequest;
import com.coderockets.referandumproject.rest.RestModel.DynamicLinkResponse;
import com.coderockets.referandumproject.rest.RestModel.SoruSorRequest;
import com.coderockets.referandumproject.rest.RestModel.SoruSorResponse;
import com.coderockets.referandumproject.util.AutoFitTextView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.miguelbcr.ui.rx_paparazzo.RxPaparazzo;
import com.miguelbcr.ui.rx_paparazzo.entities.Options;
import com.miguelbcr.ui.rx_paparazzo.entities.size.ScreenSize;
import com.nightonke.jellytogglebutton.JellyToggleButton;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions.RxPermissions;

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
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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

    //@ViewById(R.id.FabRefreshImage)
    //FloatingActionButton mFabRefreshImage;

    @ViewById(R.id.FabUploadImage)
    FabSpeedDial mFabUploadImage;

    @ViewById(R.id.ImageView_SoruImage)
    ImageView mImageView_SoruImage;

    @ViewById(R.id.AutoFitTextViewSoru)
    AutoFitTextView mAutoFitTextViewSoru;

    @ViewById(R.id.JellyToggleButtonFriendNotif)
    JellyToggleButton mJellyToggleButtonFriendNotif;

    RxPermissions mRxPermissions;
    JellyToggleButton mJellyToggleButtonPrivateQuestion;
    private String mFilePath = null;

    //PhotoViewAttacher photoViewAttacher;


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
        setToolbar();
        setFab();
        setReactiveEditText();
        //setRandomImage();
        //photoViewAttacher = new PhotoViewAttacher(mImageView_SoruImage);
        //photoViewAttacher.setAllowParentInterceptOnEdge(true);
        //photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
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


        /*mFabRefreshImage.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_refresh)
                .sizeDp(150).color(R.color.color4).getCurrent());*/

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

    /*
    @DebugLog
    @Click(R.id.FabRefreshImage)
    public void FabRefreshImageClick() {
        setRandomImage();
    }

    @DebugLog
    public void setRandomImage() {
        mImageView_SoruImage.setScaleType(ImageView.ScaleType.CENTER);
        String randomUrl = "http://loremflickr.com/g/600/200/abstract/";//Randoms.imageUrl("png");
        Picasso.with(this)
                .load(randomUrl)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                //.placeholder(android.R.drawable.progress_indeterminate_horizontal)
                .placeholder(R.drawable.loading)
                .into(mImageView_SoruImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        //photoViewAttacher.update();
                        mImageView_SoruImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }
    */
    @DebugLog
    @Click(R.id.Button_SoruGonder)
    public void Button_SoruGonderClick() {

        mRxPermissions.request(Const.PERMISSIONS_ASK_QUESTION)
                .subscribe(granted -> {
                            if (granted) {
                                sendQuestionRequest();
                            } else {
                                UiHelper.UiSnackBar.showSimpleSnackBar(getCurrentFocus(),
                                        "Soru gönderebilmeniz için gerekli izinleri vermelisiniz !",
                                        Snackbar.LENGTH_INDEFINITE);
                            }
                        }, error -> {
                            //UiHelper.UiDialog.newInstance(QuestionActivity.this).getOKDialog("HATA", error.getMessage(), null).show();
                            UiHelper.UiDialog.showSimpleDialog(QuestionActivity.this, "HATA", error.getMessage());
                            SuperHelper.CrashlyticsLog(error);
                        }
                );
    }

    @DebugLog
    public void sendQuestionRequest() {

        if (SuperHelper.validateIsEmpty(mEditText_SoruText)) return;

        MaterialDialog materialDialog = UiHelper.UiDialog.newInstance(this).getProgressDialog("Lütfen Bekleyiniz..", null);
        materialDialog.show();

        try {

            if (mFilePath == null) {

                String randomImageUrl = "";
                SoruSorRequest soruSorRequest = SoruSorRequest.SoruSorRequestInstance();
                soruSorRequest.setQuestionText(mEditText_SoruText.getText().toString());
                soruSorRequest.setQuestionImage(randomImageUrl);

                if (mJellyToggleButtonPrivateQuestion.isChecked()) {

                    soruSorRequest.setQuestionPrivate(true);

                    ApiManager.getInstance(this).SoruSor(soruSorRequest)
                            .flatMap(new Func1<SoruSorResponse, Observable<DynamicLinkResponse>>() {
                                @Override
                                public Observable<DynamicLinkResponse> call(SoruSorResponse response) {
                                    String referandumImageLink = "https://scontent-otp1-1.xx.fbcdn.net/v/t31.0-8/15137646_969137279896357_8350515276412161_o.jpg?oh=51f40534d2ca39ce0db6d097b88cd638&oe=59811AA3";
                                    return ApiManager.getInstance(QuestionActivity.this).DynamicLink(
                                            DynamicLinkRequest.getDynamicLinkRequest(
                                                    "http://referandum?questionId=" + response.getData().getSoruId(),
                                                    response.getData().getQuestionText(),
                                                    "",
                                                    referandumImageLink));
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(resp -> {
                                materialDialog.dismiss();
                                Logger.d(resp);
                                Logger.i("DynamicLink: " + resp.getShortLink());
                                Logger.i("DynamicLink Preview: " + resp.getPreviewLink());


                                NavUtils.navigateUpFromSameTask(QuestionActivity.this);
                            }, error ->
                            {
                                materialDialog.dismiss();
                                Logger.e(error, "HATA");
                            });
                } else {

                    ApiManager.getInstance(this).SoruSor(soruSorRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response -> {
                                materialDialog.dismiss();
                                mFilePath = null;
                                mEditText_SoruText.setText("");
                                EventBus.getDefault().postSticky(response.getData());
                                NavUtils.navigateUpFromSameTask(QuestionActivity.this);
                            }, error ->
                            {
                                materialDialog.dismiss();
                                Logger.e(error, "HATA");
                            });
                }

                /*
                Drawable drawable = mImageView_SoruImage.getDrawable();
                File externalDir = new File(Environment.getExternalStorageDirectory().getPath() + "/ReferandumProject");
                if (!externalDir.exists()) externalDir.mkdir();

                Bitmap bitmap = SuperHelper.drawableToBitmap(drawable);
                SuperHelper.saveBitmapToFile(externalDir, "ReferandumSoru.jpg", bitmap, Bitmap.CompressFormat.JPEG, 100);

                String soruImageFilePath = externalDir.getPath() + "/ReferandumSoru.jpg";
                Logger.i(soruImageFilePath);
                mFilePath = soruImageFilePath;
                */

            } else {

                File file = new File(mFilePath);

                Logger.i("FilePath: " + mFilePath);

                Map<String, RequestBody> map = new HashMap<>();
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
                map.put("file\"; filename=\"" + "file", requestBody);


                if (mJellyToggleButtonPrivateQuestion.isChecked()) {

                    ApiManager.getInstance(this).ImageUpload(map)
                            .flatMap(response -> {

                                Logger.i("Image Url: " + response.getData());

                                SoruSorRequest soruSorRequest = SoruSorRequest.SoruSorRequestInstance();
                                soruSorRequest.setQuestionText(mEditText_SoruText.getText().toString());
                                soruSorRequest.setQuestionImage(response.getData());
                                soruSorRequest.setQuestionPrivate(true);

                                return ApiManager.getInstance(this).SoruSor(soruSorRequest);
                            })
                            .flatMap(new Func1<SoruSorResponse, Observable<DynamicLinkResponse>>() {
                                @Override
                                public Observable<DynamicLinkResponse> call(SoruSorResponse response) {
                                    return ApiManager.getInstance(QuestionActivity.this).DynamicLink(
                                            DynamicLinkRequest.getDynamicLinkRequest(
                                                    "http://referandum?questionId=" + response.getData().getSoruId(),
                                                    response.getData().getQuestionText(),
                                                    "",
                                                    response.getData().getQuestionImage()
                                            ));
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(resp -> {
                                materialDialog.dismiss();
                                Logger.d(resp);
                                Logger.i("DynamicLink: " + resp.getShortLink());
                                Logger.i("DynamicLink Preview: " + resp.getPreviewLink());
                                NavUtils.navigateUpFromSameTask(QuestionActivity.this);
                            }, error ->
                            {
                                materialDialog.dismiss();
                                UiHelper.UiDialog.showSimpleDialog(QuestionActivity.this, "HATA", error.getMessage());
                                SuperHelper.CrashlyticsLog(error);
                                Logger.e(error, "HATA");
                            });
                } else {

                    ApiManager.getInstance(this).ImageUpload(map)
                            .flatMap(response -> {

                                Logger.i("Image Url: " + response.getData());

                                SoruSorRequest soruSorRequest = SoruSorRequest.SoruSorRequestInstance();
                                soruSorRequest.setQuestionText(mEditText_SoruText.getText().toString());
                                soruSorRequest.setQuestionImage(response.getData());

                                return ApiManager.getInstance(this).SoruSor(soruSorRequest);
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response -> {
                                {
                                    mFilePath = null;
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
            }


        } catch (Exception error) {
            materialDialog.dismiss();
            SuperHelper.CrashlyticsLog(error);
            UiHelper.UiDialog.showSimpleDialog(QuestionActivity.this, "HATA", error.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_question, menu);
        mJellyToggleButtonPrivateQuestion = (JellyToggleButton) MenuItemCompat.getActionView(menu.findItem(R.id.MenuPrivateQuestion)).findViewById(R.id.JellyToggleButtonPrivateQuestion);
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
                    mFilePath = response.data();
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
                    mFilePath = response.data();
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
                    mFilePath = response.data();
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

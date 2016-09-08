package com.coderockets.referandumproject.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
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
import com.coderockets.referandumproject.rest.RestModel.SoruSorRequest;
import com.coderockets.referandumproject.util.AutoFitTextView;
import com.fuck_boilerplate.rx_paparazzo.RxPaparazzo;
import com.fuck_boilerplate.rx_paparazzo.entities.Options;
import com.fuck_boilerplate.rx_paparazzo.entities.Size;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import hugo.weaving.DebugLog;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by aykutasil on 5.09.2016.
 */
@EActivity(R.layout.activity_question)
public class QuestionActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.EditText_SoruText)
    EditText mEditText_SoruText;

    @ViewById(R.id.AskQuestionMainLayout)
    ViewGroup mAskQuestionMainLayout;

    @ViewById(R.id.FabRefreshImage)
    FloatingActionButton mFabRefreshImage;

    @ViewById(R.id.FabUploadImage)
    FabSpeedDial mFabUploadImage;

    @ViewById(R.id.ImageView_SoruImage)
    ImageView mImageView_SoruImage;

    @ViewById(R.id.RxAutoFitTextViewSoru)
    AutoFitTextView mRxAutoFitTextViewSoru;

    RxPermissions mRxPermissions;
    private String mFilePath = null;

    @DebugLog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxPermissions = RxPermissions.getInstance(this);
    }

    @AfterViews
    @DebugLog
    public void QuestionActivityInit() {
        setToolbar();
        setFab();
        setReactiveEditText();
        updateUI();
    }

    @DebugLog
    private void setToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Soru Sor");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //getSupportActionBar().setHomeAsUpIndicator(new IconDrawable(this, FontAwesomeIcons.fa_home).actionBarSize().getCurrent());
    }

    private void setReactiveEditText() {
        RxTextView.textChanges(mEditText_SoruText)
                .map(charSequence -> charSequence.toString().toUpperCase())
                .subscribe(text -> {
                    mRxAutoFitTextViewSoru.setText(text + " ?");
                });
    }

    private void setFab() {
        mFabRefreshImage.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_refresh).sizeDp(150).color(R.color.color4).getCurrent());

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

    private void updateUI() {
        SuperHelper.setRandomImage(this, mImageView_SoruImage, UUID.randomUUID().toString());
    }

    @DebugLog
    @Click(R.id.FabRefreshImage)
    public void FabRefreshImageClick() {
        SuperHelper.setRandomImage(this, mImageView_SoruImage, UUID.randomUUID().toString());
    }

    /*
    @DebugLog
    @Click(R.id.FabUploadImage)
    public void FabUploadImageClick() {
        //captureImageWithCrop();
    }
    */

    @DebugLog
    @Click(R.id.Button_SoruGonder)
    public void Button_SoruGonderClick() {

        mRxPermissions.request(Const.PERMISSIONS_GENERAL)
                .subscribe(granted -> {
                            if (granted) {
                                sendQuestionRequest();
                            } else {
                                UiHelper.UiSnackBar.showSimpleSnackBar(getCurrentFocus(),
                                        "Soru gönderebilmeniz için gerekli izinleri vermelisiniz !",
                                        Snackbar.LENGTH_INDEFINITE);
                            }
                        }, error -> {
                            UiHelper.UiSnackBar.showSimpleSnackBar(getCurrentFocus(),
                                    error.getMessage(),
                                    Snackbar.LENGTH_INDEFINITE);
                        }
                );
    }

    @DebugLog
    public void sendQuestionRequest() {

        if (validateIsEmpty(mEditText_SoruText)) return;

        MaterialDialog materialDialog = UiHelper.UiDialog.newInstance(this).getProgressDialog("Lütfen Bekleyiniz..", null);
        materialDialog.show();

        try {

            if (mFilePath == null) {
                Drawable drawable = mImageView_SoruImage.getDrawable();
                File externalDir = new File(Environment.getExternalStorageDirectory().getPath() + "/ReferandumProject");
                if (!externalDir.exists()) externalDir.mkdir();

                Bitmap bitmap = SuperHelper.drawableToBitmap(drawable);
                SuperHelper.saveBitmapToFile(externalDir, "ReferandumSoru.jpg", bitmap, Bitmap.CompressFormat.JPEG, 100);

                String soruImageFilePath = externalDir.getPath() + "/ReferandumSoru.jpg";
                Logger.i(soruImageFilePath);
                mFilePath = soruImageFilePath;
            }
            File file;
            file = new File(mFilePath);
            Logger.i("FilePath: " + mFilePath);
            Map<String, RequestBody> map = new HashMap<>();
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
            map.put("file\"; filename=\"" + "file", requestBody);


            ApiManager.getInstance(this).ImageUpload(map)
                    .flatMap(response -> {
                        SoruSorRequest soruSorRequest = SoruSorRequest.SoruSorRequestInstance();
                        soruSorRequest.setQuestionText(mEditText_SoruText.getText().toString());
                        //soruSorRequest.setQuestionImage(Randoms.imageUrl("png"));

                        Logger.i("Image Url: " + response.getData());
                        soruSorRequest.setQuestionImage(response.getData());

                        return ApiManager.getInstance(this).SoruSor(soruSorRequest);

                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            response -> {
                                {
                                    mFilePath = null;
                                    mEditText_SoruText.setText("");
                                    UiHelper.UiSnackBar.showSimpleSnackBar(getCurrentFocus(), "Sorunuz gönderildi.", Snackbar.LENGTH_LONG);
                                }
                            }, error -> {
                                materialDialog.dismiss();
                                error.printStackTrace();
                                UiHelper.UiDialog.showSimpleDialog(this, "HATA", error.getMessage());
                            }, materialDialog::dismiss);


        } catch (Exception e) {
            materialDialog.dismiss();
            UiHelper.UiSnackBar.showSimpleSnackBar(getCurrentFocus(), e.getMessage(), Snackbar.LENGTH_INDEFINITE);
        }
    }

    private void captureImage() {
        RxPaparazzo.takeImage(this)
                .size(Size.Screen)
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
        options.setMaxResultSize(1024, 640);
        options.setAspectRatio(150, 60);
        //options.setAspectRatio(100, 60);
        //options.setMaxResultSize(640, 480);
        //options.setActiveWidgetColor(getResources().getColor(R.color.bpDark_gray)); //
        //options.setCropGridColor(Color.BLUE);
        //options.setLogoColor(Color.BLACK);

        RxPaparazzo.takeImage(this)
                .size(Size.Screen)
                .crop(options)
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

    private void pickupImage() {
        Options options = new Options();
        //options.useSourceImageAspectRatio();
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        options.setToolbarTitle("Düzenle");
        options.setMaxResultSize(1024, 640);
        options.setAspectRatio(150, 60);

        RxPaparazzo.takeImage(this)
                .useInternalStorage()
                .crop(options)
                .size(Size.Screen)
                .usingGallery()
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

    private void pickupImages() {
        RxPaparazzo.takeImages(this)
                .useInternalStorage()
                .crop()
                .size(Size.Screen)
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

    private void loadImage(String filePath) {
        //mFilesPaths.clear();
        //mFilesPaths.add(filePath);
        //imageView.setVisibility(View.VISIBLE);
        //recyclerView.setVisibility(View.GONE);

        File imageFile = new File(filePath);

        Picasso.with(this)
                .load(imageFile)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .fit()
                .into(mImageView_SoruImage);
    }

    private void showUserCanceled() {
        Toast.makeText(this, "İptal edildi.", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

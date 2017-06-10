package com.coderockets.referandumproject.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.HandlerThread;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.blankj.utilcode.utils.ImageUtils;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.db.DbManager;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.coderockets.referandumproject.model.ModelUser;
import com.coderockets.referandumproject.rest.ApiManager;
import com.coderockets.referandumproject.rest.RestModel.SoruGetirBaseResponse;
import com.coderockets.referandumproject.util.PicassoCircleTransform;
import com.google.firebase.messaging.RemoteMessage;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hugo.weaving.DebugLog;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by aykutasil on 2.06.2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    abstract void initAfterViews();

    abstract void updateUi();


    @DebugLog
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @DebugLog
    public void updateProfileIcon(MenuItem menuItem) {
        ModelUser modelUser = DbManager.getModelUser();
        if (modelUser != null) {
            WeakReference<MenuItem> itemWeakReference = new WeakReference<>(menuItem);
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    MenuItem menuItem = itemWeakReference.get();
                    if (menuItem != null) {
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                        drawable.setBounds(0, 0, 100, 100);
                        menuItem.setIcon(drawable);
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.with(this)
                    .load(modelUser.getProfileImageUrl())
                    .transform(new PicassoCircleTransform())
                    .into(target);

        } else {
            menuItem.setIcon(R.drawable.ic_account_circle_pink_900_48dp);
        }
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRemoteMessage(RemoteMessage remoteMessage) {
        String questionId = remoteMessage.getData().get(UniqueQuestionActivity.PUSH_NODE_QUESTION_ID);
        ApiManager.getInstance(this).TekSoruGetir(questionId)
                .flatMap(new Func1<SoruGetirBaseResponse, Observable<Pair<SoruGetirBaseResponse, Drawable>>>() {
                    @Override
                    public Observable<Pair<SoruGetirBaseResponse, Drawable>> call(SoruGetirBaseResponse resp) {
                        try {
                            ModelQuestionInformation questionInformation = resp.getData().getRows().get(0);
                            Logger.i("Current Thread: " + HandlerThread.currentThread().getName());
                            Bitmap btmp = Picasso.with(BaseActivity.this).load(Uri.parse(questionInformation.getAskerProfileImg())).get();
                            Drawable drawable = ImageUtils.bitmap2Drawable(BaseActivity.this.getResources(), btmp);
                            return Observable.just(Pair.create(resp, drawable));
                        } catch (Exception e) {
                            Logger.e(e, "HATA");
                            return Observable.error(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    ModelQuestionInformation questionInformation = resp.first.getData().getRows().get(0);

                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                    sweetAlertDialog.setCustomImage(resp.second);
                    sweetAlertDialog.setTitleText(questionInformation.getAskerName() + " yeni bir soru sordu.");
                    sweetAlertDialog.setContentText("Cevaplamak ister misiniz?");
                    sweetAlertDialog.setCancelText("İptal Et");
                    sweetAlertDialog.setConfirmText("Cevapla");

                    sweetAlertDialog.setConfirmClickListener(sweetAlertDialog1 -> {
                        sweetAlertDialog1.dismiss();

                        Intent i = new Intent(getApplicationContext(), UniqueQuestionActivity_.class);
                        i.setAction(UniqueQuestionActivity.ACTION_OPEN_QUESTION);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra(UniqueQuestionActivity.PUSH_NODE_QUESTION_ID, questionId);
                        i.putExtra(UniqueQuestionActivity.ACTION_APP_ALREADY_OPEN_CONTROL, UniqueQuestionActivity.ACTION_APP_ALREADY_OPEN_CONTROL);

                        startActivity(i);
                    });
                    sweetAlertDialog.show();
                }, error -> {
                    Logger.e(error, "HATA");
                    SuperHelper.CrashlyticsLog(error);
                });
    }

}

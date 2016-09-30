package com.coderockets.referandumproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ToxicBakery.viewpager.transforms.BackgroundToForegroundTransformer;
import com.afollestad.materialdialogs.MaterialDialog;
import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.MainActivity;
import com.coderockets.referandumproject.app.Const;
import com.coderockets.referandumproject.db.DbManager;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.coderockets.referandumproject.model.ModelTempQuestionAnswer;
import com.coderockets.referandumproject.model.ModelUser;
import com.coderockets.referandumproject.rest.ApiManager;
import com.coderockets.referandumproject.rest.RestModel.AnswerRequest;
import com.coderockets.referandumproject.util.CustomAnswerPercent;
import com.coderockets.referandumproject.util.CustomButton;
import com.coderockets.referandumproject.util.adapter.CustomSorularAdapter;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import hugo.weaving.DebugLog;
import rx.Observable;
import rx.Subscription;

@EFragment(R.layout.fragment_referandum_layout)
public class ReferandumFragment extends BaseFragment {

    @ViewById(R.id.ViewPagerSorular)
    ViewPager mViewPagerSorular;

    @ViewById(R.id.ButtonTrue)
    CustomButton mButtonTrue;

    @ViewById(R.id.ButtonFalse)
    CustomButton mButtonFalse;
    //
    Context mContext;
    MainActivity mActivity;
    RxPermissions mRxPermission;
    CustomSorularAdapter mSorularAdapter;
    Hashtable<Integer, Boolean> modControl = new Hashtable<>();
    Hashtable<String, Boolean> answerAndTempQuestionControl = new Hashtable<>();
    Hashtable<String, ModelTempQuestionAnswer> tempAnswer = new Hashtable<>();
    List<Subscription> mListSubscription;

    @DebugLog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
        this.mActivity = (MainActivity) getActivity();
        this.mListSubscription = new ArrayList<>();
        this.mRxPermission = RxPermissions.getInstance(mContext);
    }

    @DebugLog
    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @DebugLog
    @AfterViews
    public void ReferandumFragmentInit() {
        setViewPager();
        setSorular();
    }

    private void setViewPager() {

        mSorularAdapter = new CustomSorularAdapter(getChildFragmentManager());
        mViewPagerSorular.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @DebugLog
            @Override
            public void onPageSelected(int position) {
                //setQuestionFragment();
                checkTempAnsweredAndShowResult(position);

                if (position > 0) {
                    ModelQuestionInformation modelQuestionInformation = getQuestionFragment(position - 1).getQuestion();
                    if (!checkAnswered(modelQuestionInformation)) {
                        answerAndTempQuestionControl.put(modelQuestionInformation.getSoruId(), true);
                        sendQuestionAnswer("atla", "s", modelQuestionInformation);
                    }

                }
                ControlTempQuestion();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPagerSorular.setAdapter(mSorularAdapter);
        mViewPagerSorular.setPageTransformer(true, new BackgroundToForegroundTransformer());
    }

    @DebugLog
    private void setSorular() {

        mRxPermission.request(Const.PERMISSIONS_GENERAL)
                .subscribe(success -> {
                    if (success) {
                        Logger.i("mSorularAdapter.getCount(): " + mSorularAdapter.getCount());
                        if (mSorularAdapter.getCount() == 0) {
                            addQuestionsToAdapter(20);
                        }
                    } else {
                        UiHelper.UiSnackBar.showSimpleSnackBar(getView(), "Tüm izinleri vermelisiniz.", Snackbar.LENGTH_INDEFINITE);
                    }
                }, error -> {
                    UiHelper.UiSnackBar.showSimpleSnackBar(getView(), error.getMessage(), Snackbar.LENGTH_INDEFINITE);
                    SuperHelper.CrashlyticsLog(error);
                });

    }

    @Click(R.id.ButtonTrue)
    public void ButtonTrueClick() {
        if (mSorularAdapter.getCount() == 0) return;
        try {
            ModelQuestionInformation modelQuestionInformation = getCurrentQuestionFragment().getQuestion();
            if (!checkAnswered(modelQuestionInformation)) {
                answerAndTempQuestionControl.put(modelQuestionInformation.getSoruId(), true);
                showAnswerResult("evet");
                sendQuestionAnswer("evet", "a", getCurrentQuestionFragment().getQuestion());
                skipNextQuestion();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.ButtonFalse)
    public void ButtonFalseClick() {
        if (mSorularAdapter.getCount() == 0) return;
        try {
            ModelQuestionInformation modelQuestionInformation = getCurrentQuestionFragment().getQuestion();
            if (!checkAnswered(modelQuestionInformation)) {
                answerAndTempQuestionControl.put(modelQuestionInformation.getSoruId(), true);
                showAnswerResult("hayir");
                sendQuestionAnswer("hayir", "b", modelQuestionInformation);
                skipNextQuestion();
            }
        } catch (Exception error) {
            error.printStackTrace();
            SuperHelper.CrashlyticsLog(error);
        }
    }

    private void skipNextQuestion() {
        Handler handler = new Handler();

        // En son soruda değilse
        if (mViewPagerSorular.getCurrentItem() != mSorularAdapter.getCount()) {
            handler.postDelayed(() -> mViewPagerSorular.setCurrentItem(mViewPagerSorular.getCurrentItem() + 1, true), 2000);
        }
    }

    private void ControlTempQuestion() {
        // Eğer modControl hashtable ında 10 ve katlarında bir kayıt yoksa 10 soru çekilir.
        // Bu kontrolün amacı 11. soruda iken 10. soruya dönüldüğünde tekrar soru yüklenmesini engellemek için.
        if (modControl.get(mViewPagerSorular.getCurrentItem()) == null) {
            Logger.i("ViewPagerSorular.getCurrentItem(): " + mViewPagerSorular.getCurrentItem());
            Logger.i("SorularAdapter.getCount(): " + mSorularAdapter.getCount());
            if ((mViewPagerSorular.getCurrentItem() > mSorularAdapter.getCount() - 10
                    && mViewPagerSorular.getCurrentItem() != 0)
                    || mViewPagerSorular.getCurrentItem() + 1 == mSorularAdapter.getCount())
            //if (mViewPagerSorular.getCurrentItem() % 10 == 0 && mViewPagerSorular.getCurrentItem() != 0)
            {
                // HashTable a ViewPager.getCurrentItem() değerini atıyoruz.
                // Bu sayede ViewPager.getCurrentItem() sayfasında iken null değeri dönmeyecek ve tekrar soru yüklmesi yapılmayacak.
                modControl.put(mViewPagerSorular.getCurrentItem(), true);
                addQuestionsToAdapter(10);
            }
        }
    }

    private void showAnswerResult(String which) {
        try {

            ModelQuestionInformation mqi = getCurrentQuestionFragment().getQuestion();
            switch (which) {
                case "evet": {
                    mqi.setOption_A_Count(mqi.getOption_A_Count() + 1);
                    break;
                }
                case "hayir": {
                    mqi.setOption_B_Count(mqi.getOption_B_Count() + 1);
                    break;
                }
            }

            showCustomAnswerPercent(getCurrentQuestionFragment());

            ModelTempQuestionAnswer modelTempQuestionAnswer = new ModelTempQuestionAnswer();
            modelTempQuestionAnswer.setValueA(mqi.getOption_B_Count());
            modelTempQuestionAnswer.setValueB(mqi.getOption_A_Count());
            tempAnswer.put(mqi.getSoruId(), modelTempQuestionAnswer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCustomAnswerPercent(QuestionFragment qf) {
        try {
            View alphaView = qf.getView().findViewById(R.id.SoruText);
            CustomAnswerPercent customAnswerPercent = (CustomAnswerPercent) qf.getView().findViewById(R.id.MyCustomAnswerPercent);
            customAnswerPercent.addAlphaView(alphaView);
            customAnswerPercent.addActivity(mActivity);
            customAnswerPercent.addFragment(this);
            customAnswerPercent.setAValue(qf.getQuestion().getOption_B_Count());
            customAnswerPercent.setBValue(qf.getQuestion().getOption_A_Count());
            customAnswerPercent.setFriendAnswer(qf.getQuestion().getModelFriends());
            customAnswerPercent.showResult();
        } catch (Exception e) {
            Logger.e("HATA: " + e);
        }
    }

    @DebugLog
    private void addQuestionsToAdapter(int count) {
        try {

            MaterialDialog progressDialog = UiHelper.UiDialog.newInstance(mContext).getProgressDialog("Lütfen bekleyiniz..", null);
            progressDialog.show();

            Subscription subscription = ApiManager.getInstance(mContext)
                    .SoruGetir(count)
                    .doOnError(error -> progressDialog.dismiss())
                    .doOnCompleted(progressDialog::dismiss)
                    .flatMap(soruGetirBaseResponse -> {
                        if (soruGetirBaseResponse.getData().getRows().size() > 0) {
                            return Observable.just(soruGetirBaseResponse);
                        } else {
                            UiHelper.UiSnackBar.showSimpleSnackBar(getView(), "Soru Yok", Snackbar.LENGTH_LONG);
                            return Observable.empty();
                        }
                    })
                    .subscribe(
                            response -> {
                                for (ModelQuestionInformation mqi : response.getData().getRows()) {
                                    //Logger.i(mqi.getQuestionText());
                                    // Soru daha önce eklenip eklenmediği kontrol ediliyor
                                    if (answerAndTempQuestionControl.get(mqi.getSoruId()) == null) {
                                        mSorularAdapter.addFragment(QuestionFragment_.builder().build(), mqi);
                                        answerAndTempQuestionControl.put(mqi.getSoruId(), false);
                                    }
                                }
                            }, error -> {
                                error.printStackTrace();
                                SuperHelper.CrashlyticsLog(error);
                                UiHelper.UiSnackBar.showSimpleSnackBar(getView(), error.getMessage(), Snackbar.LENGTH_LONG);
                            }
                    );

            mListSubscription.add(subscription);

        } catch (Exception e) {
            SuperHelper.CrashlyticsLog(e);
            e.printStackTrace();
        }
    }

    /*
    @DebugLog
    private void setQuestionFragment() {
        try {
            if (mSorularAdapter.getCount() == 0) {
                Logger.i("Soru yok..");
                return;
            }
            mQuestionFragment = (QuestionFragment) mSorularAdapter.getItem(mViewPagerSorular.getCurrentItem());
        } catch (Exception e) {
            Logger.e("HATA:" + e);
        }
    }
    */

    @DebugLog
    private void sendQuestionAnswer(String text, String option, ModelQuestionInformation mqi) {

        AnswerRequest answerRequest = AnswerRequest.AnswerRequestInit();
        answerRequest.setOption(option);
        answerRequest.setQuestionId(mqi.getSoruId());
        answerRequest.setText(text);

        Logger.i("AnswerQuestion: " + new Gson().toJson(answerRequest));

        Subscription subscription = ApiManager.getInstance(mContext)
                .Answer(answerRequest)
                .subscribe(success -> {
                    //UiHelper.UiSnackBar.showSimpleSnackBar(getView(), "Cevap gönderildi.", Snackbar.LENGTH_LONG);
                }, error -> {
                    SuperHelper.CrashlyticsLog(error);
                    UiHelper.UiSnackBar.showSimpleSnackBar(getView(), error.getMessage(), Snackbar.LENGTH_LONG);
                });

        mListSubscription.add(subscription);
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(ModelQuestionInformation mqi) {
        ModelUser modelUser = DbManager.getModelUser();
        mqi.setAskerProfileImg(modelUser.getProfileImageUrl());
        getCurrentQuestionFragment().setQuestion(mqi);

        EventBus.getDefault().removeStickyEvent(mqi);
    }

    private QuestionFragment getQuestionFragment(int position) {
        return (QuestionFragment) mSorularAdapter.getItem(position);
    }

    private QuestionFragment getCurrentQuestionFragment() {
        return (QuestionFragment) mSorularAdapter.getItem(mViewPagerSorular.getCurrentItem());
    }

    @DebugLog
    private boolean checkAnswered(ModelQuestionInformation mqi) {
        return answerAndTempQuestionControl.get(mqi.getSoruId()) == null ? false : answerAndTempQuestionControl.get(mqi.getSoruId());
    }

    private void checkTempAnsweredAndShowResult(int position) {
        ModelQuestionInformation modelQuestionInformation = getQuestionFragment(position).getQuestion();
        if (tempAnswer.get(modelQuestionInformation.getSoruId()) != null) {
            showCustomAnswerPercent(getCurrentQuestionFragment());
        }
    }

    @DebugLog
    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @DebugLog
    @Override
    public void onDestroy() {
        for (Subscription subscription : mListSubscription) {
            if (!subscription.isUnsubscribed()) {
                //subscription.unsubscribe();
            }
        }
        super.onDestroy();
    }
}

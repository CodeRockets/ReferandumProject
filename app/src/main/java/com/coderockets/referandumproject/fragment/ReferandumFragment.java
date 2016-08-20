package com.coderockets.referandumproject.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ToxicBakery.viewpager.transforms.BackgroundToForegroundTransformer;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.MainActivity;
import com.coderockets.referandumproject.app.Const;
import com.coderockets.referandumproject.db.DbManager;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.coderockets.referandumproject.rest.RestClient;
import com.coderockets.referandumproject.util.CustomAnswerPercent;
import com.coderockets.referandumproject.util.CustomButton;
import com.coderockets.referandumproject.util.adapter.CustomSorularAdapter;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Hashtable;

import hugo.weaving.DebugLog;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EFragment(R.layout.fragment_referandum_layout)
public class ReferandumFragment extends BaseFragment {

    @ViewById(R.id.ViewPagerSorular)
    ViewPager mViewPagerSorular;

    @ViewById(R.id.ButtonTrue)
    CustomButton mButtonTrue;

    @ViewById(R.id.ButtonFalse)
    CustomButton mButtonFalse;


    Context mContext;
    MainActivity mActivity;
    //CustomViewPagerAdapter mViewPagerAdapter;
    CustomSorularAdapter mSorularAdapter;
    Hashtable<Integer, Boolean> modControl = new Hashtable<>();

    @DebugLog
    @AfterViews
    public void ReferandumFragmentInit() {
        this.mContext = getActivity();
        this.mActivity = (MainActivity) getActivity();
        //
        setViewPager();
        setSorular();
    }

    private void setViewPager() {
        /*
        mViewPagerAdapter = new CustomViewPagerAdapter();

        mViewPagerSorular.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPagerSorular.setAdapter(mViewPagerAdapter);
        */

        mSorularAdapter = new CustomSorularAdapter(getChildFragmentManager());
        mViewPagerSorular.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
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
    private void ControlTempQuestion() {
        // Eğer modControl hashtable ında 10 ve katlarında bir kayıt yoksa 10 soru çekilir.
        // Bu kontrolün amacı 11. soruda iken 10. soruya dönüldüğünde tekrar soru yüklenmesini engellemek için.
        if (modControl.get(mViewPagerSorular.getCurrentItem()) == null) {
            if (mViewPagerSorular.getCurrentItem() % 10 == 0) {
                // HashTable a ViewPager.getCurrentItem() değerini atıyoruz.
                // Bu sayede ViewPager.getCurrentItem() sayfasında iken null değeri dönmeyecek ve tekrar soru yüklmesi yapılmayacak.
                modControl.put(mViewPagerSorular.getCurrentItem(), true);
                addQuestionsAdapter("10");
            }
        }
    }


    @DebugLog
    private void setSorular() {

        //mSorularAdapter = new CustomSorularAdapter(mActivity.getSupportFragmentManager());
        //mViewPagerAdapter.addView(cardView);
        //mViewPagerAdapter.addView(cardView);

        Logger.i("mSorularAdapter.getCount(): " + mSorularAdapter.getCount());
        if (mSorularAdapter.getCount() == 0) {
            addQuestionsAdapter("20");
        }
    }

    @Click(R.id.ButtonTrue)
    public void ButtonTrueClick() {
        try {
            showAnswerResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAnswerResult() {
        try {
            Fragment frg = mSorularAdapter.getItem(mViewPagerSorular.getCurrentItem());
            View viewGroup = frg.getView().findViewById(R.id.SoruText);

            CustomAnswerPercent customAnswerPercent = (CustomAnswerPercent) frg.getView().findViewById(R.id.MyCustomAnswerPercent);
            customAnswerPercent.addHostView(viewGroup);
            customAnswerPercent.setAValue(90);
            customAnswerPercent.setBValue(10);

            //customAnswerPercent.setWidthBarA(120);
            //customAnswerPercent.setWidthBarB(20);
            //customAnswerPercent.setSValue(76);
            //viewGroup.addView(customAnswerPercent);
            customAnswerPercent.showResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.ButtonFalse)
    public void ButtonFalseClick() {
        //addQuestionsAdapter("10");
    }

    private void addQuestionsAdapter(String count) {
        try {

            Logger.i("User Id: " + DbManager.getModelUser().getUserId());

            RestClient restClient = RestClient.getInstance();
            restClient.getApiService()
                    .SoruGetir(
                            Const.CLIENT_ID,
                            Const.REFERANDUM_VERSION,
                            SuperHelper.getDeviceId(mContext),
                            count,
                            SuperHelper.checkUser() ? DbManager.getModelUser().getUserId() : ""
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            response -> {
                                //QuestionFragment_ soruFragment = QuestionFragment_.builder().
                                for (ModelQuestionInformation mqi : response.getData().getRows()) {
                                    Logger.i(mqi.getQuestionText());
                                    mSorularAdapter.addFragment(QuestionFragment_.builder().build(), mqi);
                                }
                            }, error -> {
                                error.printStackTrace();
                            });

                    /*
                    .enqueue(new Callback<SoruGetirBaseResponse>() {
                        @Override
                        public void onResponse(Call<SoruGetirBaseResponse> call, Response<SoruGetirBaseResponse> response) {
                            for (ModelQuestionInformation mqi : response.body().getData().getRows()) {
                                Logger.i(mqi.getQuestionText());
                            }
                        }

                        @Override
                        public void onFailure(Call<SoruGetirBaseResponse> call, Throwable t) {

                        }
                    });
                    */

                    /*

                    */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

package com.coderockets.referandumproject.fragment;

import android.content.Context;
import android.support.v4.view.ViewPager;

import com.ToxicBakery.viewpager.transforms.BackgroundToForegroundTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.MainActivity;
import com.coderockets.referandumproject.app.Const;
import com.coderockets.referandumproject.db.DbManager;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.coderockets.referandumproject.rest.RestClient;
import com.coderockets.referandumproject.util.CustomButton;
import com.coderockets.referandumproject.util.adapter.CustomSorularAdapter;
import com.coderockets.referandumproject.util.adapter.CustomViewPagerAdapter;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

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
    CustomViewPagerAdapter mViewPagerAdapter;
    CustomSorularAdapter mSorularAdapter;

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
        mViewPagerSorular.setAdapter(mSorularAdapter);
        mViewPagerSorular.setPageTransformer(true, new BackgroundToForegroundTransformer());
    }

    @DebugLog
    private void setSorular() {

        //mSorularAdapter = new CustomSorularAdapter(mActivity.getSupportFragmentManager());


        try {
            //LinearLayout cardView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.question_layout, null);
            //mViewPagerAdapter.addView(cardView);

            Logger.i("User Id: " + DbManager.getModelUser().getUserId());

            RestClient restClient = RestClient.getInstance();
            restClient.getApiService()
                    .SoruGetir(
                            Const.CLIENT_ID,
                            Const.REFERANDUM_VERSION,
                            SuperHelper.getDeviceId(mContext),
                            "10",
                            SuperHelper.checkUser() ? DbManager.getModelUser().getUserId() : ""
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
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

        //mViewPagerAdapter.addView(cardView);
        //mViewPagerAdapter.addView(cardView);

    }


    @Click(R.id.ButtonTrue)
    public void ButtonTrueClick() {
        mButtonTrue.changeButtonScale();
        //setSorular();


    }


}

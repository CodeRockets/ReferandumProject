package com.coderockets.referandumproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.ProfileActivity;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.coderockets.referandumproject.rest.ApiManager;
import com.coderockets.referandumproject.util.adapter.MyFavoritesAdapter;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.shape.Focus;
import hugo.weaving.DebugLog;
import rx.Observable;

/**
 * Created by aykutasil on 8.09.2016.
 */
@EFragment(R.layout.profile_myfavorites_layout)
public class ProfileMyFavorites extends BaseProfile implements MaterialIntroListener {

    @ViewById(R.id.RecyclerViewFavorites)
    RecyclerView mRecyclerViewFavorites;

    //


    private final String INTRO_KEY_QUESTION_FAVORITE = "question_favorite4";

    Context mContext;
    ProfileActivity mActivity;
    MyFavoritesAdapter mMyFavoritesAdapter;
    List<ModelQuestionInformation> mList;
    View mLastIntroView;

    @DebugLog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
        this.mActivity = (ProfileActivity) getActivity();
        mList = new ArrayList<>();
        mMyFavoritesAdapter = new MyFavoritesAdapter(mContext, mList);
    }

    @DebugLog
    @AfterViews
    public void ProfileMyFavoritesInit() {
        if (mMyFavoritesAdapter.getItemCount() == 0) {
            getUserFavorites();
        }
        setAdapter();
    }

    private void setAdapter() {
        mRecyclerViewFavorites.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewFavorites.setAdapter(mMyFavoritesAdapter);
    }

    private void getUserFavorites() {

        ApiManager.getInstance(mContext).UserQuestions(9999)
                .flatMap(map -> {
                    List<ModelQuestionInformation> lst = map.getData().getFavorites().getRows();
                    Collections.reverse(lst);
                    return Observable.just(lst);
                })
                .subscribe(
                        this::convertResponseToUiView,
                        error -> {
                            SuperHelper.CrashlyticsLog(error);
                            Logger.e(error, "HATA");
                        });
    }

    @DebugLog
    private void convertResponseToUiView(List<ModelQuestionInformation> rows) {

        for (ModelQuestionInformation mqi : rows) {
            mMyFavoritesAdapter.addItem(mqi);
        }


        new Handler().postDelayed(() -> {

            try {
                if (mMyFavoritesAdapter.getItemCount() > 0) {
                    mLastIntroView = mRecyclerViewFavorites.getChildAt(0).findViewById(R.id.FabFavorite);
                }
            } catch (Exception e) {
                Logger.e(e, "HATA");
            }
        }, 1000);

    }

    @DebugLog
    @Override
    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }

    @DebugLog
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {

            SuperHelper.showIntro(getActivity(),
                    mLastIntroView,
                    this,
                    INTRO_KEY_QUESTION_FAVORITE,
                    "Favorilediğiniz soruları buradan kaldırabilirsiniz",
                    Focus.MINIMUM);
        }
    }

    @DebugLog
    @Override
    public void onUserClicked(String s) {

    }
}

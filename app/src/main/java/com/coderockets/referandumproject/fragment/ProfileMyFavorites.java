package com.coderockets.referandumproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.ProfileActivity;
import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.coderockets.referandumproject.rest.ApiManager;
import com.coderockets.referandumproject.util.adapter.MyQuestionsAdapter;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hugo.weaving.DebugLog;
import rx.Observable;

/**
 * Created by aykutasil on 8.09.2016.
 */
@EFragment(R.layout.profile_myfavorites_layout)
public class ProfileMyFavorites extends BaseProfile {

    @ViewById(R.id.RecyclerViewFavorites)
    RecyclerView mRecyclerViewFavorites;
    //
    Context mContext;
    ProfileActivity mActivity;
    MyQuestionsAdapter mMyQuestionsAdapter;
    List<ModelQuestionInformation> mList;

    @DebugLog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
        this.mActivity = (ProfileActivity) getActivity();
        mList = new ArrayList<>();
        mMyQuestionsAdapter = new MyQuestionsAdapter(mContext, mList);
    }

    @DebugLog
    @AfterViews
    public void ProfileMyFavoritesInit() {
        getUserFavorites();
        setAdapter();
    }

    private void setAdapter() {
        mRecyclerViewFavorites.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewFavorites.setAdapter(mMyQuestionsAdapter);
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
                            Logger.e(error, "HATA");
                        });
    }

    @DebugLog
    private void convertResponseToUiView(List<ModelQuestionInformation> rows) {

        for (ModelQuestionInformation mqi : rows) {
            mMyQuestionsAdapter.addUserQuestion(mqi);
        }

    }

}

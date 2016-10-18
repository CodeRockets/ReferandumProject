package com.coderockets.referandumproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.ProfileActivity;
import com.coderockets.referandumproject.helper.SuperHelper;
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
import rx.Subscription;

/**
 * Created by aykutasil on 2.09.2016.
 */
@EFragment(R.layout.profile_myquestions_layout)
public class ProfileMyQuestions extends BaseProfile {

    @ViewById(R.id.RecyclerViewMyQuestions)
    RecyclerView mRecyclerViewMyQuestions;
    //
    Context mContext;
    ProfileActivity mActivity;
    MyQuestionsAdapter mMyQuestionsAdapter;
    List<ModelQuestionInformation> mList;
    List<Subscription> mListSubscription;

    @DebugLog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
        this.mActivity = (ProfileActivity) getActivity();
        mList = new ArrayList<>();
        mMyQuestionsAdapter = new MyQuestionsAdapter(mList);
        mListSubscription = new ArrayList<>();
    }

    @DebugLog
    @AfterViews
    public void ProfileMyQuestionsInit() {
        if (mList.size() == 0) {
            getUserQuestions();
        }
        setAdapter();
    }

    private void setAdapter() {
        mRecyclerViewMyQuestions.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewMyQuestions.setAdapter(mMyQuestionsAdapter);
    }

    private void getUserQuestions() {

        MaterialDialog progressDialog = UiHelper.UiDialog.newInstance(mContext).getProgressDialog("Lütfen bekleyiniz..", null);
        progressDialog.show();

        Subscription subscription = ApiManager.getInstance(mContext).UserQuestions(9999)
                .flatMap(map -> {
                    List<ModelQuestionInformation> lst = map.getData().getQuestions().getRows();
                    Collections.reverse(lst);
                    return Observable.just(lst);
                })
                .subscribe(
                        success -> {
                            convertResponseToUiView(success);
                        },
                        error -> {
                            progressDialog.dismiss();
                            SuperHelper.CrashlyticsLog(error);
                            Logger.e(error, "HATA");
                            UiHelper.UiSnackBar.showSimpleSnackBar(getView(), error.getMessage(), Snackbar.LENGTH_LONG);
                        },
                        progressDialog::dismiss);

        mListSubscription.add(subscription);
    }

    @DebugLog
    private void convertResponseToUiView(List<ModelQuestionInformation> rows) {

        for (ModelQuestionInformation mqi : rows) {
            mMyQuestionsAdapter.addUserQuestion(mqi);
        }
    }

    @Override
    public void onDestroy() {
        for (Subscription subscription : mListSubscription) {
            if (!subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
        super.onDestroy();
    }
}

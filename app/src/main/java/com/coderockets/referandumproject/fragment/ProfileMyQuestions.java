package com.coderockets.referandumproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aykuttasil.androidbasichelperlib.UiHelper;
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
import java.util.List;

import hugo.weaving.DebugLog;

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

    @DebugLog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
        this.mActivity = (ProfileActivity) getActivity();
        mList = new ArrayList<>();
        mMyQuestionsAdapter = new MyQuestionsAdapter(mContext,mList);
    }

    @DebugLog
    @AfterViews
    public void ProfileMeInit() {
        getUserQuestions();
        setAdapter();
    }

    private void setAdapter() {
        mRecyclerViewMyQuestions.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewMyQuestions.setAdapter(mMyQuestionsAdapter);
    }

    private void getUserQuestions() {

        MaterialDialog progressDialog = UiHelper.UiDialog.newInstance(mContext).getProgressDialog("Lütfen bekleyiniz..", null);
        progressDialog.show();

        ApiManager.getInstance(mContext).UserQuestions(10)
                .subscribe(success -> {
                    List<ModelQuestionInformation> rows = success.getData().getQuestions().getRows();
                    convertResponseToUiView(rows);
                }, error -> {
                    Logger.e(error, "HATA");
                    progressDialog.dismiss();
                }, progressDialog::dismiss);
    }

    @DebugLog
    private void convertResponseToUiView(List<ModelQuestionInformation> rows) {

        StringBuilder stringBuilder = new StringBuilder();
        for (ModelQuestionInformation mqi : rows) {
            mMyQuestionsAdapter.addUserQuestion(mqi);
            //stringBuilder.append(mqi.getQuestionText());
            //stringBuilder.append("\n");
            //mTextViewSorular.setText(mqi.getQuestionText() + "\n");
            //Logger.i(mqi.getQuestionText());
        }
        //mTextViewSorular.setText(stringBuilder.toString());
    }

}

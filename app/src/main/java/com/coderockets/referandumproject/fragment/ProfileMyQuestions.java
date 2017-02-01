package com.coderockets.referandumproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.ProfileActivity;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.coderockets.referandumproject.rest.ApiManager;
import com.coderockets.referandumproject.util.SimpleItemTouchHelperCallback;
import com.coderockets.referandumproject.util.adapter.MyQuestionsAdapter;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.shape.Focus;
import hugo.weaving.DebugLog;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aykutasil on 2.09.2016.
 */
@EFragment(R.layout.profile_myquestions_layout)
public class ProfileMyQuestions extends BaseProfile implements MaterialIntroListener {

    @ViewById(R.id.RecyclerViewMyQuestions)
    RecyclerView mRecyclerViewMyQuestions;

    //

    private final String INTRO_KEY_SWIPE_DELETE = "swipe_delete_question";
    private final String INTRO_KEY_PERCENT_BAR = "percent_bar";

    Context mContext;
    ProfileActivity mActivity;
    MyQuestionsAdapter mMyQuestionsAdapter;
    List<ModelQuestionInformation> mList;
    CompositeSubscription mCompositeSubscriptions;
    View mLastIntroView;
    //List<Subscription> mListSubscription;
    //ItemTouchHelper mItemTouchHelper;

    @DebugLog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
        this.mActivity = (ProfileActivity) getActivity();
        mList = new ArrayList<>();
        mMyQuestionsAdapter = new MyQuestionsAdapter(mContext, mList);
        mCompositeSubscriptions = new CompositeSubscription();
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
        mRecyclerViewMyQuestions.setHasFixedSize(true);
        mRecyclerViewMyQuestions.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewMyQuestions.setAdapter(mMyQuestionsAdapter);

        initSwipeControl();
    }

    private void initSwipeControl() {
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mContext, mMyQuestionsAdapter, mRecyclerViewMyQuestions);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerViewMyQuestions);
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
                        this::convertResponseToUiView,
                        error -> {
                            progressDialog.dismiss();
                            SuperHelper.CrashlyticsLog(error);
                            Logger.e(error, "HATA");
                            UiHelper.UiSnackBar.showSimpleSnackBar(getView(), error.getMessage(), Snackbar.LENGTH_LONG);
                        },
                        progressDialog::dismiss);

        mCompositeSubscriptions.add(subscription);
    }

    @DebugLog
    private void convertResponseToUiView(List<ModelQuestionInformation> rows) {

        for (ModelQuestionInformation mqi : rows) {
            mMyQuestionsAdapter.addUserQuestion(mqi);
        }

        new Handler().postDelayed(() -> {

            try {
                if (mMyQuestionsAdapter.getItemCount() > 0) {

                    View vi = mRecyclerViewMyQuestions.getChildAt(0);

                    mLastIntroView = vi;

                    SuperHelper.showIntro(getActivity(),
                            vi,
                            this,
                            INTRO_KEY_SWIPE_DELETE,
                            "Soruları sağa veya sola sürükleyerek silebilirsiniz.",
                            Focus.MINIMUM);

                }
            } catch (Exception e) {
                Logger.e(e, "HATA");
            }
        }, 1000);


    }

    @DebugLog
    @Override
    public void onUserClicked(String materialIntroViewId) {

        if (materialIntroViewId.equals(INTRO_KEY_SWIPE_DELETE)) {

            if (mLastIntroView.findViewById(R.id.MyPercentBar) != null) {

                mLastIntroView = mLastIntroView.findViewById(R.id.MyPercentBar);

                SuperHelper.showIntro(getActivity(),
                        mLastIntroView,
                        this,
                        "percent_bar",
                        //UUID.randomUUID().toString(),
                        "Cevap oranlarını ve soruya cevap veren arkadaş listenizi buradan görebilirsiniz.",
                        Focus.MINIMUM);
            }

        } else if (materialIntroViewId.equals(INTRO_KEY_PERCENT_BAR)) {

        }
    }

    @Override
    public void onDestroy() {
        if (mCompositeSubscriptions.hasSubscriptions()) {
            mCompositeSubscriptions.clear();
        }
        super.onDestroy();
    }


}

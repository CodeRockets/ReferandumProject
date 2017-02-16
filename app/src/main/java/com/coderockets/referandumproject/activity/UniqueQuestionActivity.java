package com.coderockets.referandumproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.aykuttasil.percentbar.PercentBarView;
import com.aykuttasil.percentbar.models.BarImageModel;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.fragment.QuestionFragment;
import com.coderockets.referandumproject.fragment.QuestionFragment_;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.model.ModelFriend;
import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.coderockets.referandumproject.rest.ApiManager;
import com.coderockets.referandumproject.rest.RestModel.AnswerRequest;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;
import rx.Subscription;

/**
 * Created by aykutasil on 15.02.2017.
 */
@EActivity(R.layout.activity_unique_question_layout)
public class UniqueQuestionActivity extends BaseActivity {

    @ViewById(R.id.Container)
    ViewGroup mContainer;

    @ViewById(R.id.Toolbar)
    Toolbar mToolbar;

    //

    public static final String ACTION_OPEN_QUESTION = "ACTION_OPEN_FRIEND_QUESTION";
    public static final String PUSH_NODE_QUESTION_ID = "PayloadQuestionId";
    public static final String ACTION_APP_ALREADY_OPEN_CONTROL = "AlreadyOpenControl";

    QuestionFragment mQuestionFragment;

    @DebugLog
    @AfterViews
    @Override
    void initAfterViews() {

        Logger.i("intent" + getIntent());

        initToolbar();

        if (getIntent() != null && getIntent().getAction() != null && getIntent().getAction().equals(ACTION_OPEN_QUESTION)) {

            Bundle extras = getIntent().getExtras();

            String questionId = extras.getString(PUSH_NODE_QUESTION_ID);

            initQuestionFragment(questionId);
        }

    }

    @Override
    void updateUi() {

    }

    @DebugLog
    private void initToolbar() {

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Referandum");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @DebugLog
    private void initQuestionFragment(String questionId) {

        ApiManager.getInstance(this).TekSoruGetir(questionId)
                .subscribe(resp -> {

                    mQuestionFragment = QuestionFragment_.builder().build();

                    Bundle args = new Bundle();
                    args.putParcelable(ModelQuestionInformation.class.getSimpleName(), resp.getData().getRows().get(0));

                    mQuestionFragment.setArguments(args);

                    SuperHelper.ReplaceFragmentBeginTransaction(this, mQuestionFragment, R.id.Container, false);

                }, error -> {

                }, () -> {
                    new Handler().postDelayed(() -> {
                        if (mQuestionFragment.getView() != null)
                            mQuestionFragment.getView().findViewById(R.id.Fab_PreviousQuestion).setVisibility(View.INVISIBLE);

                    }, 10);
                });
    }

    @Click(R.id.ButtonTrue)
    public void ButtonTrueClick() {


        if (mQuestionFragment == null) return;

        try {

            ModelQuestionInformation modelQuestionInformation = mQuestionFragment.getQuestion();

            showAnswerResult("evet");
            sendQuestionAnswer("evet", "a", modelQuestionInformation);

        } catch (Exception e) {
            Logger.e(e, "HATA");
            e.printStackTrace();
        }
    }

    @Click(R.id.ButtonFalse)
    public void ButtonFalseClick() {

        if (mQuestionFragment == null) return;

        try {

            ModelQuestionInformation modelQuestionInformation = mQuestionFragment.getQuestion();

            showAnswerResult("hayir");
            sendQuestionAnswer("hayir", "b", modelQuestionInformation);

        } catch (Exception error) {
            error.printStackTrace();
            SuperHelper.CrashlyticsLog(error);
        }
    }

    @DebugLog
    private void showAnswerResult(String which) {
        try {

            ModelQuestionInformation mqi = mQuestionFragment.getQuestion();

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

            showCustomAnswerPercent(mQuestionFragment);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DebugLog
    private void showCustomAnswerPercent(QuestionFragment qf) {
        try {
            List<BarImageModel> mlist = new ArrayList<>();
            if (qf.getQuestion().getModelFriends() != null) {
                for (ModelFriend friend : qf.getQuestion().getModelFriends()) {
                    BarImageModel barImageModel = new BarImageModel();
                    barImageModel.setValue(friend.getOption().equals("a") ? PercentBarView.BarField.RIGHT : PercentBarView.BarField.LEFT);
                    barImageModel.setBarText(friend.getName());
                    barImageModel.setImageUrl(friend.getProfileImage());
                    mlist.add(barImageModel);
                }
            }

            View alphaView = qf.getView().findViewById(R.id.SoruText);
            PercentBarView percentBarView = (PercentBarView) qf.getView().findViewById(R.id.MyPercentBar);
            percentBarView.addAlphaView(alphaView);
            percentBarView.setLeftBarValue(qf.getQuestion().getOption_B_Count());
            percentBarView.setRightBarValue(qf.getQuestion().getOption_A_Count());
            percentBarView.setImages(mlist);
            percentBarView.setImagesListTitle(getResources().getString(R.string.title_dialog_percentbar_list));
            percentBarView.showResult();

            mQuestionFragment.showShareButton();

        } catch (Exception e) {
            Logger.e(e, "HATA");
            SuperHelper.CrashlyticsLog(e);
        }
    }

    @DebugLog
    private void sendQuestionAnswer(String text, String option, ModelQuestionInformation mqi) {

        AnswerRequest answerRequest = AnswerRequest.AnswerRequestInit();
        answerRequest.setOption(option);
        answerRequest.setQuestionId(mqi.getSoruId());
        answerRequest.setText(text);

        Logger.i("AnswerQuestion: " + new Gson().toJson(answerRequest));

        Subscription subscription = ApiManager.getInstance(this)
                .Answer(answerRequest)
                .subscribe(success -> {
                    //UiHelper.UiSnackBar.showSimpleSnackBar(getView(), "Cevap gönderildi.", Snackbar.LENGTH_LONG);
                }, error -> {
                    SuperHelper.CrashlyticsLog(error);
                    UiHelper.UiSnackBar.showSimpleSnackBar(mToolbar, error.getMessage(), Snackbar.LENGTH_LONG);
                });
    }

    @DebugLog
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @DebugLog
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {

                Intent upIntent = NavUtils.getParentActivityIntent(this);
                Logger.d(upIntent);

                if (shouldUpRecreateTask(this)) {

                    Logger.i("NavUtils.shouldUpRecreateTask");
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {

                    Logger.i(" NavUtils.navigateUpTo");
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;

            }
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean shouldUpRecreateTask(Activity activity) {

        String action = activity.getIntent().getAction();

        return action != null &&
                action.equals(ACTION_OPEN_QUESTION) &&
                activity.getIntent().getStringExtra(ACTION_APP_ALREADY_OPEN_CONTROL) == null;
    }

}

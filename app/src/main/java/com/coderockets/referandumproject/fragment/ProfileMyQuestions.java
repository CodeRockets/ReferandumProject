package com.coderockets.referandumproject.fragment;

import android.content.Context;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.ProfileActivity;
import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.coderockets.referandumproject.rest.ApiManager;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 2.09.2016.
 */
@EFragment(R.layout.profile_myquestions_layout)
public class ProfileMyQuestions extends BaseProfile {

    @ViewById(R.id.TextViewSorular)
    TextView mTextViewSorular;
    //
    Context mContext;
    ProfileActivity mActivity;

    @AfterViews
    public void ProfileMeInit() {
        this.mContext = getActivity();
        this.mActivity = (ProfileActivity) getActivity();
        getUserQuestions();
    }

    private void getUserQuestions() {

        MaterialDialog progressDialog = UiHelper.UiDialog.newInstance(mContext).getProgressDialog("Lütfen bekleyiniz..", null);
        progressDialog.show();

        ApiManager.getInstance(mContext).UserQuestions(10)
                .subscribe(success -> {
                    //Logger.i(success.getData().getQuestions().getRows().get(1).getQuestionText());
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
            stringBuilder.append(mqi.getQuestionText());
            stringBuilder.append("\n");
            //mTextViewSorular.setText(mqi.getQuestionText() + "\n");
            //Logger.i(mqi.getQuestionText());
        }
        mTextViewSorular.setText(stringBuilder.toString());
    }

}

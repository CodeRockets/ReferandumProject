package com.coderockets.referandumproject.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.MainActivity;
import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.coderockets.referandumproject.util.AutoFitTextView;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 18.08.2016.
 */
@EFragment(R.layout.question_layout)
public class QuestionFragment extends Fragment {

    @ViewById(R.id.SoruImage)
    ImageView mImageView_SoruImage;

    @ViewById(R.id.ProfilePicture)
    CircularImageView mProfilePicture;

    @ViewById(R.id.SoruText)
    AutoFitTextView mSoruText;
    //
    private Context mContext;
    private MainActivity mActivity;

    @DebugLog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @DebugLog
    @AfterViews
    public void SoruFragmentInstance() {
        this.mContext = getActivity();
        this.mActivity = (MainActivity) getActivity();
        //
        ModelQuestionInformation mqi = getArguments().getParcelable(ModelQuestionInformation.class.getSimpleName());
        setSoru(mqi);
    }

    private void setSoru(ModelQuestionInformation mqi) {

        Uri soruImageUri = Uri.parse(mqi.getQuestionImage());
        Glide.with(this)
                .load(soruImageUri)
                .signature(new StringSignature(mqi.getSoruId()))
                .into(mImageView_SoruImage);


        Uri profileImageUri = Uri.parse(mqi.getAskerProfileImg());
        Glide.with(this)
                .load(profileImageUri)
                .signature(new StringSignature(mqi.getAskerProfileImg()))
                .into(mProfilePicture);
        //mProfilePicture.setImageDrawable(new IconDrawable(mContext, FontAwesomeIcons.fa_github).sizeDp(150).getCurrent());
        mSoruText.setText(mqi.getQuestionText());
    }

}

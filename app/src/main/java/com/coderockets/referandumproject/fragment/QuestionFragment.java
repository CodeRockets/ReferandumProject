package com.coderockets.referandumproject.fragment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.MainActivity;
import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.coderockets.referandumproject.rest.ApiManager;
import com.coderockets.referandumproject.rest.RestModel.FavoriteRequest;
import com.coderockets.referandumproject.util.AutoFitTextView;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import de.hdodenhof.circleimageview.CircleImageView;
import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 18.08.2016.
 */
@EFragment(R.layout.question_layout)
public class QuestionFragment extends Fragment {

    @ViewById(R.id.FabFavorite)
    FloatingActionButton mFabFavorite;

    @ViewById(R.id.SoruImage)
    ImageView mImageView_SoruImage;

    @ViewById(R.id.ProfilePicture)
    CircleImageView mProfilePicture;

    @ViewById(R.id.SoruText)
    AutoFitTextView mSoruText;
    //
    private Context mContext;
    private MainActivity mActivity;
    private ModelQuestionInformation mqi;
    private final String FAVORITE_KEY = "Favorite";
    private boolean mIsFavorite = false;

    @DebugLog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mIsFavorite = savedInstanceState.getBoolean(FAVORITE_KEY, false);
            Logger.i("mIsFavorite: " + mIsFavorite);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FAVORITE_KEY, mIsFavorite);
    }

    @DebugLog
    @AfterViews
    public void SoruFragmentInstance() {
        this.mContext = getActivity();
        this.mActivity = (MainActivity) getActivity();
        //
        mqi = getArguments().getParcelable(ModelQuestionInformation.class.getSimpleName());
        setSoru(mqi);
        changeFavoriteFabColor();
    }

    private void changeFavoriteFabColor() {
        if (!mIsFavorite) {
            mFabFavorite.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        } else {
            mFabFavorite.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.accentColor)));
        }
    }

    public ModelQuestionInformation getQuestion() {
        return mqi;
    }

    private void setSoru(ModelQuestionInformation mqi) {

        Uri soruImageUri = Uri.parse(mqi.getQuestionImage());
        Picasso.with(mContext)
                .load(soruImageUri)
                .fit()
                .placeholder(new IconDrawable(mContext, FontAwesomeIcons.fa_refresh).sizeDp(50).color(Color.GRAY).getCurrent())
                .into(mImageView_SoruImage);


        Uri profileImageUri = Uri.parse(mqi.getAskerProfileImg());
        Picasso.with(mContext)
                .load(profileImageUri)
                .placeholder(mContext.getDrawable(R.drawable.anonym))
                .into(mProfilePicture);
        //mProfilePicture.setImageDrawable(new IconDrawable(mContext, FontAwesomeIcons.fa_github).sizeDp(150).getCurrent());
        mSoruText.setText(mqi.getQuestionText().toUpperCase());
    }

    @Click(R.id.FabFavorite)
    public void FabFavoriteClick() {
        mIsFavorite = !mIsFavorite;
        changeFavoriteFabColor();

        FavoriteRequest favoriteRequest = FavoriteRequest.FavoriteRequestInit();
        favoriteRequest.setQuestionId(mqi.getSoruId());
        favoriteRequest.setUnFavorite(!mIsFavorite);

        ApiManager.getInstance(mContext).Favorite(favoriteRequest)
                .subscribe(success -> {
                    if (mIsFavorite) {
                        UiHelper.UiSnackBar.showSimpleSnackBar(getView(), "Favorilere Eklendi", Snackbar.LENGTH_SHORT);
                    } else {
                        UiHelper.UiSnackBar.showSimpleSnackBar(getView(), "Favorilerden Çıkarıldı", Snackbar.LENGTH_SHORT);
                    }
                }, error -> {
                    error.printStackTrace();
                });
    }

}

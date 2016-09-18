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
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.MainActivity;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.coderockets.referandumproject.rest.ApiManager;
import com.coderockets.referandumproject.rest.RestModel.FavoriteRequest;
import com.coderockets.referandumproject.rest.RestModel.ReportAbuseRequest;
import com.coderockets.referandumproject.util.AutoFitTextView;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hugo.weaving.DebugLog;
import rx.Subscription;

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
    List<Subscription> mListSubscription;

    @DebugLog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mIsFavorite = savedInstanceState.getBoolean(FAVORITE_KEY, false);
            Logger.i("mIsFavorite: " + mIsFavorite);
        }
        mListSubscription = new ArrayList<>();
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
        setFavoriteFab();
        registerForContextMenu(mSoruText);
        //PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(mImageView_SoruImage);
    }

    private void changeFavoriteFabColor() {
        if (!mIsFavorite) {
            mFabFavorite.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        } else {
            mFabFavorite.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.accentColor)));
        }
    }

    private void setFavoriteFab() {
        if (SuperHelper.checkUser()) {
            mFabFavorite.show();
        } else {
            mFabFavorite.hide();
        }
    }

    public ModelQuestionInformation getQuestion() {
        return mqi;
    }

    public void setQuestion(ModelQuestionInformation mqi) {
        this.mqi = mqi;
        setSoru(mqi);
        changeFavoriteFabColor();
        setFavoriteFab();
    }

    private void setSoru(ModelQuestionInformation mqi) {

        mImageView_SoruImage.setScaleType(ImageView.ScaleType.CENTER);

        Uri soruImageUri = Uri.parse(mqi.getQuestionImage());
        Picasso.with(mContext)
                .load(soruImageUri)
                .placeholder(R.drawable.loading)
                .into(mImageView_SoruImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (mImageView_SoruImage != null) {
                            mImageView_SoruImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });


        Uri profileImageUri = Uri.parse(mqi.getAskerProfileImg());
        Picasso.with(mContext)
                .load(profileImageUri)
                .placeholder(R.drawable.loading)
                .into(mProfilePicture);
        //mProfilePicture.setImageDrawable(new IconDrawable(mContext, FontAwesomeIcons.fa_github).sizeDp(150).getCurrent());

        String soruText = mqi.getQuestionText();
        if (soruText.length() > 0) {
            soruText = soruText.substring(0, 1).toUpperCase() + soruText.substring(1).toLowerCase();
        }
        mSoruText.setText(soruText);
    }

    @DebugLog
    @Click(R.id.FabFavorite)
    public void FabFavoriteClick() {
        mIsFavorite = !mIsFavorite;
        changeFavoriteFabColor();

        FavoriteRequest favoriteRequest = FavoriteRequest.FavoriteRequestInit();
        favoriteRequest.setQuestionId(mqi.getSoruId());

        Logger.i("Favorite Soru Id: " + mqi.getSoruId());
        favoriteRequest.setUnFavorite(!mIsFavorite);

        Subscription subscription = ApiManager.getInstance(mContext).Favorite(favoriteRequest)
                .subscribe(success -> {
                    if (mIsFavorite) {
                        UiHelper.UiSnackBar.showSimpleSnackBar(getView(), "Favorilere Eklendi", Snackbar.LENGTH_SHORT);
                    } else {
                        UiHelper.UiSnackBar.showSimpleSnackBar(getView(), "Favorilerden Çıkarıldı", Snackbar.LENGTH_SHORT);
                    }
                }, error -> {
                    error.printStackTrace();
                    SuperHelper.CrashlyticsLog(error);
                });

        mListSubscription.add(subscription);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == mSoruText.getId()) {
            menu.clearHeader();
            menu.add(0, 0, 1, "Şikayet Et");
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Logger.i("ItemId: " + item.getItemId());
        Logger.i("getUserVisibleHint():" + getUserVisibleHint());

        if (!SuperHelper.checkUser()) {
            return true;
        }

        if (getUserVisibleHint()) {
            switch (item.getItemId()) {
                case 0: {

                    ReportAbuseRequest reportAbuseRequest = ReportAbuseRequest.ReportAbuseRequestInit();
                    reportAbuseRequest.setQuestionId(mqi.getSoruId());

                    Logger.i("Abuse Soru Id: " + mqi.getSoruId());

                    Subscription subscription = ApiManager.getInstance(mContext).ReportAbuse(reportAbuseRequest)
                            .subscribe(success -> {
                                Logger.i("Abuse Success");
                                UiHelper.UiSnackBar.showSimpleSnackBar(getView(), "Şikayetiniz bize ulaştı. Teşekkür ederiz.", Snackbar.LENGTH_LONG);
                            }, error -> {
                                Logger.e(error, "HATA");
                                SuperHelper.CrashlyticsLog(error);
                                UiHelper.UiSnackBar.showSimpleSnackBar(getView(), error.getMessage(), Snackbar.LENGTH_LONG);
                            });

                    mListSubscription.add(subscription);

                    return true;
                }
            }
        }
        return super.onContextItemSelected(item);
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

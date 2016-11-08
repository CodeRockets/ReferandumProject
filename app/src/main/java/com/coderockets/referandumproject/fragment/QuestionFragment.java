package com.coderockets.referandumproject.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.activity.MainActivity;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.model.Event.UpdateLoginEvent;
import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.coderockets.referandumproject.rest.ApiManager;
import com.coderockets.referandumproject.rest.RestModel.FavoriteRequest;
import com.coderockets.referandumproject.rest.RestModel.ReportAbuseRequest;
import com.coderockets.referandumproject.util.AutoFitTextView;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;
import hugo.weaving.DebugLog;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

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

    @ViewById(R.id.Appbarlayout)
    AppBarLayout mAppbarlayout;

    @ViewById(R.id.Toolbar_Share)
    Toolbar mToolbar_Share;

    @ViewById(R.id.Fab_PreviousQuestion)
    FloatingActionButton mFab_PreviousQuestion;

    @ViewById(R.id.SoruLayout)
    CardView mSoruLayout;
    //
    private Context mContext;
    private MainActivity mActivity;
    private ModelQuestionInformation mqi;
    private final String FAVORITE_KEY = "Favorite";
    private boolean mIsFavorite = false;
    LoginManager mLoginManager;
    CompositeSubscription mCompositeSubscriptions;
    ReferandumFragment mReferandumFragment;

    @DebugLog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mIsFavorite = savedInstanceState.getBoolean(FAVORITE_KEY, false);
        }
        mLoginManager = LoginManager.getInstance();
        mCompositeSubscriptions = new CompositeSubscription();
        mReferandumFragment = (ReferandumFragment) getParentFragment();
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
        mAppbarlayout.setExpanded(false);
        mqi = getArguments().getParcelable(ModelQuestionInformation.class.getSimpleName());
        setSoru(mqi);
        changeFavoriteFabColor();
        setFavoriteFab();
        registerForContextMenu(mSoruText);
        setShareToolbar();
        setFacebookShare();
    }

    private void setShareToolbar() {

        mToolbar_Share.inflateMenu(R.menu.question_share_menu);

        mToolbar_Share.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menuFacebook: {
                    mAppbarlayout.setExpanded(false, true);
                    shareQuestion();
                    break;
                }
            }
            return false;
        });
    }

    private void setFacebookShare() {

        mLoginManager.registerCallback(mActivity.mCallbackManager, new FacebookCallback<LoginResult>() {
            @DebugLog
            @Override
            public void onSuccess(LoginResult loginResult) {
                //shareQuestionToFacebook();
            }

            @Override
            public void onCancel() {

            }

            @DebugLog
            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
                UiHelper.UiSnackBar.showSimpleSnackBar(getView(), error.toString(), Snackbar.LENGTH_LONG);
                SuperHelper.CrashlyticsLog(error);
            }
        });
    }

    @DebugLog
    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
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
        mIsFavorite = false;
        setSoru(mqi);
        changeFavoriteFabColor();
        setFavoriteFab();
    }

    private void setSoru(ModelQuestionInformation mqi) {

        mImageView_SoruImage.setScaleType(ImageView.ScaleType.CENTER);

        Uri soruImageUri = Uri.parse(mqi.getQuestionImage());

        if (mqi.getQuestionImage().contains("loremflickr")) {
            Picasso.with(mContext).invalidate(soruImageUri);
        }

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
            //soruText = soruText.substring(0, 1).toUpperCase() + soruText.substring(1).toLowerCase();
        }
        mSoruText.setText(soruText.toLowerCase());
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
        mCompositeSubscriptions.add(subscription);
        mAppbarlayout.setExpanded(true, true);
    }


    @DebugLog
    @Click(R.id.Fab_PreviousQuestion)
    public void Fab_PreviousQuestionClick() {
        mReferandumFragment.skipPreviousQuestion(0);
    }

    @DebugLog
    private void shareQuestion() {
        /*
        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                .setContentTitle("Merhaba")
                .setContentDescription("hehe")
                .setImageUrl(Uri.parse("http://www.pngall.com/wp-content/uploads/2016/07/Car-Free-PNG-Image.png"))
                .build();
        */

        if (!AccessToken.getCurrentAccessToken().getPermissions().contains("publish_actions")) {
            LoginManager.getInstance().logInWithPublishPermissions(QuestionFragment.this, Collections.singletonList("publish_actions"));
        } else {
            shareQuestionToFacebook();
        }
    }

    @org.androidannotations.annotations.UiThread(delay = 1000)
    @DebugLog
    public void shareQuestionToFacebook() {

        View drawingView = mSoruLayout.getRootView().findViewById(R.id.ParentLayout);
        drawingView.buildDrawingCache(true);
        Bitmap bitmap = drawingView.getDrawingCache(true).copy(Bitmap.Config.ARGB_8888, false);
        drawingView.destroyDrawingCache();

        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .setCaption("Referandum Sonuçları")
                .build();

        SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                .addPhoto(sharePhoto)
                .build();

        if (ShareDialog.canShow(SharePhotoContent.class)) {
            ShareDialog.show(this, sharePhotoContent);
            Logger.i("shareQuestion is success");
        } else {
            Logger.i("shareQuestion is fatal");
        }
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(UpdateLoginEvent updateLoginEvent) {
        setFavoriteFab();
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

                    mCompositeSubscriptions.add(subscription);

                    return true;
                }
            }
        }
        return super.onContextItemSelected(item);
    }

    @DebugLog
    public void setPreviousNextButtonUi() {
        mReferandumFragment.mSorularAdapter.getRegisteredFragment(mReferandumFragment.mViewPagerSorular.getCurrentItem())
                .getView().findViewById(R.id.Fab_PreviousQuestion).setVisibility(View.INVISIBLE);
    }


    @DebugLog
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mActivity.mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @DebugLog
    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @DebugLog
    @Override
    public void onDestroy() {
        if (mCompositeSubscriptions.hasSubscriptions()) {
            mCompositeSubscriptions.clear();
        }
        super.onDestroy();
    }
}

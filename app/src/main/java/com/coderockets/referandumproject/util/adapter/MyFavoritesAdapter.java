package com.coderockets.referandumproject.util.adapter;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.coderockets.referandumproject.rest.ApiManager;
import com.coderockets.referandumproject.rest.RestModel.FavoriteRequest;
import com.coderockets.referandumproject.util.AutoFitTextView;
import com.coderockets.referandumproject.util.CustomAnswerPercent;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.List;

import hugo.weaving.DebugLog;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by aykutasil on 30.09.2016.
 */

public class MyFavoritesAdapter extends RecyclerView.Adapter<MyFavoritesAdapter.ViewHolder> {

    private List<ModelQuestionInformation> mList;

    public MyFavoritesAdapter(List<ModelQuestionInformation> list) {
        mList = list;
    }

    @Override
    public MyFavoritesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myfavorites_layout, parent, false);
        return new MyFavoritesAdapter.ViewHolder(vi);
    }

    @DebugLog
    @Override
    public void onBindViewHolder(MyFavoritesAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.bind(mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addUserQuestion(ModelQuestionInformation mqi) {
        mList.add(mqi);
        notifyDataSetChanged();
    }

    private void removeItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    @DebugLog
    class ViewHolder extends RecyclerView.ViewHolder {
        ModelQuestionInformation mMqi;
        AutoFitTextView mTextViewSoru;
        ImageView mImageViewSoruImage;
        FloatingActionButton mFabFavorite;
        CustomAnswerPercent mCustomAnswerPercent;

        ViewHolder(View itemView) {
            super(itemView);
            mTextViewSoru = (AutoFitTextView) itemView.findViewById(R.id.TextViewSoru);
            mImageViewSoruImage = (ImageView) itemView.findViewById(R.id.ImageViewSoruImage);
            mCustomAnswerPercent = (CustomAnswerPercent) itemView.findViewById(R.id.MyCustomAnswerPercent);
            mFabFavorite = (FloatingActionButton) itemView.findViewById(R.id.FabFavorite);
        }

        @DebugLog
        void bind(ModelQuestionInformation mqi, int position) {
            mMqi = mqi;
            mTextViewSoru.setText(mqi.getQuestionText());
            mFabFavorite.setOnClickListener(v -> {

                FavoriteRequest favoriteRequest = FavoriteRequest.FavoriteRequestInit();
                favoriteRequest.setQuestionId(mqi.getSoruId());
                favoriteRequest.setUnFavorite(true);

                ApiManager.getInstance(itemView.getContext()).Favorite(favoriteRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(success -> {
                            //UiHelper.UiSnackBar.showSimpleSnackBar(mFabFavorite, "Favorilerden Çıkarıldı", Snackbar.LENGTH_SHORT);
                        }, error -> {
                            error.printStackTrace();
                            SuperHelper.CrashlyticsLog(error);
                        }, () -> {
                            removeItem(getAdapterPosition());
                        });
            });

            Picasso.with(mImageViewSoruImage.getContext())
                    .load(mqi.getQuestionImage())
                    .into(mImageViewSoruImage);

            showCustomAnswerPercent();
        }

        @DebugLog
        private void showCustomAnswerPercent() {
            try {
                mCustomAnswerPercent.addAlphaView(mImageViewSoruImage);
                mCustomAnswerPercent.setAValue(mMqi.getOption_B_Count());
                mCustomAnswerPercent.setBValue(mMqi.getOption_A_Count());
                mCustomAnswerPercent.setFriendAnswer(mMqi.getModelFriends());
                mCustomAnswerPercent.setFriendAnswerViewSize(50);
                mCustomAnswerPercent.showResult();
            } catch (Exception e) {
                SuperHelper.CrashlyticsLog(e);
                Logger.e(e, "HATA");
            }
        }
    }
}

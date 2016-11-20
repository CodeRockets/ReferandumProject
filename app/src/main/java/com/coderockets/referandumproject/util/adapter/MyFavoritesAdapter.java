package com.coderockets.referandumproject.util.adapter;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aykuttasil.percentbar.PercentBarView;
import com.aykuttasil.percentbar.models.BarImageModel;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.model.ModelFriend;
import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.coderockets.referandumproject.rest.ApiManager;
import com.coderockets.referandumproject.rest.RestModel.FavoriteRequest;
import com.coderockets.referandumproject.util.AutoFitTextView;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by aykutasil on 30.09.2016.
 */

public class MyFavoritesAdapter extends RecyclerView.Adapter<MyFavoritesAdapter.ViewHolder> {

    private List<ModelQuestionInformation> mList;
    private Context mContext;

    public MyFavoritesAdapter(Context context, List<ModelQuestionInformation> list) {
        this.mList = list;
        this.mContext = context;
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
        holder.bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addItem(ModelQuestionInformation mqi) {
        mList.add(mqi);
        //notifyItemInserted(mList.size());
        notifyDataSetChanged();
    }

    private void removeItem(int position) {
        try {
            mList.remove(position);
            // FIXME: 19.10.2016 -> holder.setIsRecyclable(false); olduğu için düzgün çalışmıyor
            //notifyItemRemoved(position);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DebugLog
    class ViewHolder extends RecyclerView.ViewHolder {
        ModelQuestionInformation mMqi;
        AutoFitTextView mTextViewSoru;
        ImageView mImageViewSoruImage;
        FloatingActionButton mFabFavorite;
        PercentBarView mPercentBar;

        ViewHolder(View itemView) {
            super(itemView);
            mTextViewSoru = (AutoFitTextView) itemView.findViewById(R.id.TextViewSoru);
            mImageViewSoruImage = (ImageView) itemView.findViewById(R.id.ImageViewSoruImage);
            mPercentBar = (PercentBarView) itemView.findViewById(R.id.MyPercentBar);
            mFabFavorite = (FloatingActionButton) itemView.findViewById(R.id.FabFavorite);
        }

        @DebugLog
        void bind(ModelQuestionInformation mqi) {
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

            if (!mqi.getQuestionImage().contains("loremflickr") && !mqi.getQuestionImage().equals("") && mqi.getQuestionImage() != null) {
                Picasso.with(mImageViewSoruImage.getContext())
                        .load(mqi.getQuestionImage())
                        .into(mImageViewSoruImage);
            }

            showResultPercentBar();
        }

        @DebugLog
        private void showResultPercentBar() {
            try {

                List<BarImageModel> mlist = new ArrayList<>();
                for (ModelFriend friend : mMqi.getModelFriends()) {
                    BarImageModel barImageModel = new BarImageModel();
                    barImageModel.setValue(friend.getOption().equals("a") ? PercentBarView.BarField.RIGHT : PercentBarView.BarField.LEFT);
                    barImageModel.setBarText(friend.getName());
                    barImageModel.setImageUrl(friend.getProfileImage());
                    mlist.add(barImageModel);
                }
                mPercentBar.addAlphaView(mImageViewSoruImage);
                mPercentBar.setLeftBarValue(mMqi.getOption_B_Count());
                mPercentBar.setRightBarValue(mMqi.getOption_A_Count());
                mPercentBar.setImages(mlist);
                mPercentBar.setImagesListItemSize(50);
                mPercentBar.setImagesListTitle(mContext.getResources().getString(R.string.title_dialog_percentbar_list));
                mPercentBar.showResult();
            } catch (Exception e) {
                SuperHelper.CrashlyticsLog(e);
                Logger.e(e, "HATA");
            }
        }
    }
}

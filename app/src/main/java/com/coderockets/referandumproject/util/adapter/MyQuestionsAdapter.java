package com.coderockets.referandumproject.util.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.aykuttasil.percentbar.PercentBarView;
import com.aykuttasil.percentbar.models.BarImageModel;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.helper.SuperHelper;
import com.coderockets.referandumproject.model.ModelFriend;
import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.coderockets.referandumproject.rest.ApiManager;
import com.coderockets.referandumproject.util.AutoFitTextView;
import com.coderockets.referandumproject.util.ItemTouchHelperAdapter;
import com.coderockets.referandumproject.util.ItemTouchHelperViewHolder;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 7.09.2016.
 */
public class MyQuestionsAdapter extends RecyclerView.Adapter<MyQuestionsAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private List<ModelQuestionInformation> mList;
    private Context mContext;

    public MyQuestionsAdapter(Context context, List<ModelQuestionInformation> list) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myquestions_layout, parent, false);
        return new ViewHolder(vi);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Recyclerview ın recycle olmasını engelleyerek abidik gubidik bir şekilde itemların birbirine karışmasını engeller
        holder.setIsRecyclable(false);
        holder.bind(mList.get(position));
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
        notifyDataSetChanged();
        //notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @DebugLog
    @Override
    public void onItemDismiss(int position) {


        ApiManager.getInstance(mContext).QuestionDelete(mList.get(position).getSoruId())
                .subscribe(response -> {
                    //UiHelper.UiToast.showSimpleToast(mContext, "Sorunuz Silindi", Toast.LENGTH_LONG);
                }, error -> {
                    SuperHelper.CrashlyticsLog(error);
                    Logger.e(error, "HATA");
                });

        mList.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        ModelQuestionInformation mMqi;
        AutoFitTextView mTextViewSoru;
        ImageView mImageViewSoruImage;
        PercentBarView mPercentBarView;

        ViewHolder(View itemView) {
            super(itemView);
            mTextViewSoru = (AutoFitTextView) itemView.findViewById(R.id.TextViewSoru);
            mImageViewSoruImage = (ImageView) itemView.findViewById(R.id.ImageViewSoruImage);
            mPercentBarView = (PercentBarView) itemView.findViewById(R.id.MyPercentBar);
        }

        void bind(ModelQuestionInformation mqi) {

            mMqi = mqi;
            mTextViewSoru.setText(mqi.getQuestionText());

            Picasso.with(mImageViewSoruImage.getContext())
                    .load(mqi.getQuestionImage())
                    .into(mImageViewSoruImage);

            showCustomAnswerPercent();
        }


        private void showCustomAnswerPercent() {
            try {
                List<BarImageModel> mlist = new ArrayList<>();
                for (ModelFriend friend : mMqi.getModelFriends()) {
                    BarImageModel barImageModel = new BarImageModel();
                    barImageModel.setValue(friend.getOption().equals("a") ? PercentBarView.BarField.RIGHT : PercentBarView.BarField.LEFT);
                    barImageModel.setBarText(friend.getName());
                    barImageModel.setImageUrl(friend.getProfileImage());
                    mlist.add(barImageModel);
                }

                mPercentBarView.addAlphaView(mImageViewSoruImage);
                mPercentBarView.setLeftBarValue(mMqi.getOption_B_Count());
                mPercentBarView.setRightBarValue(mMqi.getOption_A_Count());
                mPercentBarView.setImages(mlist);
                mPercentBarView.setImagesListItemSize(50);
                mPercentBarView.setImagesListTitle(mContext.getResources().getString(R.string.title_dialog_percentbar_list));
                mPercentBarView.showResult();
            } catch (Exception e) {
                Logger.e(e, "HATA");
            }
        }

        @DebugLog
        @Override
        public void onItemSelected() {
            //itemView.setBackgroundColor(Color.LTGRAY);
        }

        @DebugLog
        @Override
        public void onItemClear() {
            //itemView.setBackgroundColor(Color.BLUE);
        }

        @Override
        public void onDismissed() {
            UiHelper.UiSnackBar.showSimpleSnackBar(itemView, "Sorunuz Silindi", Snackbar.LENGTH_SHORT);
        }
    }
}

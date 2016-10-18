package com.coderockets.referandumproject.util.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.model.ModelQuestionInformation;
import com.coderockets.referandumproject.util.AutoFitTextView;
import com.coderockets.referandumproject.util.CustomAnswerPercent;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by aykutasil on 7.09.2016.
 */
public class MyQuestionsAdapter extends RecyclerView.Adapter<MyQuestionsAdapter.ViewHolder> {

    List<ModelQuestionInformation> mList;

    public MyQuestionsAdapter(List<ModelQuestionInformation> list) {
        mList = list;
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
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ModelQuestionInformation mMqi;
        AutoFitTextView mTextViewSoru;
        ImageView mImageViewSoruImage;
        CustomAnswerPercent mCustomAnswerPercent;

        ViewHolder(View itemView) {
            super(itemView);
            mTextViewSoru = (AutoFitTextView) itemView.findViewById(R.id.TextViewSoru);
            mImageViewSoruImage = (ImageView) itemView.findViewById(R.id.ImageViewSoruImage);
            mCustomAnswerPercent = (CustomAnswerPercent) itemView.findViewById(R.id.MyCustomAnswerPercent);
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
                mCustomAnswerPercent.addAlphaView(mImageViewSoruImage);
                mCustomAnswerPercent.setAValue(mMqi.getOption_B_Count());
                mCustomAnswerPercent.setBValue(mMqi.getOption_A_Count());
                mCustomAnswerPercent.setFriendAnswer(mMqi.getModelFriends());
                mCustomAnswerPercent.setFriendAnswerViewSize(50);
                mCustomAnswerPercent.showResult();
            } catch (Exception e) {
                Logger.e(e, "HATA");
            }
        }
    }
}

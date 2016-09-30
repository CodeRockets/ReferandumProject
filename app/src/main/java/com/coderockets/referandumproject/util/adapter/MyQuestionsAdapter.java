package com.coderockets.referandumproject.util.adapter;

import android.content.Context;
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
    static Context mContext;

    public MyQuestionsAdapter(Context context, List<ModelQuestionInformation> list) {
        mList = list;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myquestions_layout, parent, false);
        return new ViewHolder(vi);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Logger.i(mList.get(position).getQuestionText());
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

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        ModelQuestionInformation mMqi;
        AutoFitTextView TextViewSoru;
        ImageView ImageViewSoruImage;
        CustomAnswerPercent customAnswerPercent;

        ViewHolder(View itemView) {
            super(itemView);
            TextViewSoru = (AutoFitTextView) itemView.findViewById(R.id.TextViewSoru);
            ImageViewSoruImage = (ImageView) itemView.findViewById(R.id.ImageViewSoruImage);
            customAnswerPercent = (CustomAnswerPercent) itemView.findViewById(R.id.MyCustomAnswerPercent);
        }

        public void bind(ModelQuestionInformation mqi) {
            Logger.i(mqi.getQuestionText());
            Logger.i(mqi.getQuestionImage());

            mMqi = mqi;
            TextViewSoru.setText(mqi.getQuestionText());
            Picasso.with(mContext).load(mqi.getQuestionImage()).into(ImageViewSoruImage);
            showCustomAnswerPercent();

            //Glide.with(imageView.getContext()).load(new File("")).into(imageView);
            //Picasso.with(imageView.getContext()).load(new File(imageUrl)).into(imageView);
        }


        private void showCustomAnswerPercent() {
            try {
                View alphaView = ImageViewSoruImage;
                //CustomAnswerPercent customAnswerPercent = (CustomAnswerPercent) qf.getView().findViewById(R.id.customAnswerPercent);
                customAnswerPercent.addAlphaView(alphaView);
                customAnswerPercent.setAValue(mMqi.getOption_B_Count());
                customAnswerPercent.setBValue(mMqi.getOption_A_Count());

                // FIXME: 29.09.2016 cevap veren arkadaşların bilgisi user/question/fetch route undan geliyor.
                // FIXME: 29.09.2016 Biz burda user/question route undan bilgileri çekiyoruz
                //Logger.i(this.getClass().getSimpleName() + mMqi.getModelFriends());
                //customAnswerPercent.setFriendAnswer(mMqi.getModelFriends());

                customAnswerPercent.showResult();
            } catch (Exception e) {
                Logger.e("HATA: " + e);
            }
        }
    }


}

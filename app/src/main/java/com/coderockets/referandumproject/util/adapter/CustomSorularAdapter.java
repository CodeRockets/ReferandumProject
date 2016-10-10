package com.coderockets.referandumproject.util.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.coderockets.referandumproject.fragment.QuestionFragment;
import com.coderockets.referandumproject.model.ModelQuestionInformation;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 18.08.2016.
 */
public class CustomSorularAdapter extends FragmentStatePagerAdapter {

    private final List<QuestionFragment> mFragmentList = new ArrayList<>();
    private final List<ModelQuestionInformation> mSoruList = new ArrayList<>();
    private boolean flagRemoveAllItems = false;

    public CustomSorularAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        QuestionFragment questionFragment = mFragmentList.get(position);

        if (questionFragment.isVisible()) {
            return questionFragment;
        }

        Bundle args = new Bundle();
        args.putParcelable(ModelQuestionInformation.class.getSimpleName(), mSoruList.get(position));
        questionFragment.setArguments(args);
        return questionFragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(QuestionFragment fragment, ModelQuestionInformation mqi) {
        mFragmentList.add(fragment);
        mSoruList.add(mqi);
        notifyDataSetChanged();
    }

    public void removeAdapterItems() {
        mFragmentList.clear();
        mSoruList.clear();
        flagRemoveAllItems = true;
        notifyDataSetChanged();
        flagRemoveAllItems = false;
    }

    @DebugLog
    @Override
    public int getItemPosition(Object object) {
        if (flagRemoveAllItems) {
            return POSITION_NONE;
        } else {
            return POSITION_UNCHANGED;
        }
    }

}

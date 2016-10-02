package com.coderockets.referandumproject.util.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDAdapter;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.model.ModelFriend;
import com.coderockets.referandumproject.util.PicassoCircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by aykutasil on 30.09.2016.
 */

public class FriendAnswerListAdapter extends ArrayAdapter<ModelFriend> implements MDAdapter {


    private MaterialDialog dialog;

    public FriendAnswerListAdapter(Context context, List<ModelFriend> modelFriendList) {
        super(context, R.layout.md_simplelist_item, android.R.id.title, modelFriendList);
    }

    @Override
    public void setDialog(MaterialDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int index, View convertView, ViewGroup parent) {
        final View view = super.getView(index, convertView, parent);
        if (dialog != null) {
            final ModelFriend item = getItem(index);
            ImageView ic = (ImageView) view.findViewById(android.R.id.icon);

            Picasso.with(getContext())
                    .load(item.getProfileImage())
                    .transform(new PicassoCircleTransform())
                    .into(ic);

            TextView tv = (TextView) view.findViewById(android.R.id.title);
            tv.setTextColor(dialog.getBuilder().getItemColor());
            tv.setText(item.getName());
            dialog.setTypeface(tv, dialog.getBuilder().getRegularFont());
        }
        return view;
    }
}


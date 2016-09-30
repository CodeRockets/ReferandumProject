package com.coderockets.referandumproject.util.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.coderockets.referandumproject.R;
import com.coderockets.referandumproject.model.ModelFriend;
import com.coderockets.referandumproject.util.PicassoCircleTransform;
import com.squareup.picasso.Picasso;

/**
 * Created by aykutasil on 30.09.2016.
 */

public class FriendAnswerListAdapter extends ArrayAdapter<MaterialSimpleListItem> implements MDAdapter {


    private MaterialDialog dialog;
    private Context mContext;
    private ModelFriend mModelFriend;

    public FriendAnswerListAdapter(Context context) {
        super(context, R.layout.md_simplelist_item, android.R.id.title);
        this.mContext = context;
    }

    public void setModelFriend(ModelFriend modelFriend) {
        this.mModelFriend = modelFriend;
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int index, View convertView, ViewGroup parent) {
        final View view = super.getView(index, convertView, parent);
        if (dialog != null) {
            final MaterialSimpleListItem item = getItem(index);
            ImageView ic = (ImageView) view.findViewById(android.R.id.icon);


            Picasso.with(mContext)
                    .load(mModelFriend.getProfileImage())
                    .transform(new PicassoCircleTransform())
                    .into(ic);

            /*
            if (item.getIcon() != null) {
                ic.setImageDrawable(item.getIcon());
                ic.setPadding(item.getIconPadding(), item.getIconPadding(),
                        item.getIconPadding(), item.getIconPadding());
                ic.getBackground().setColorFilter(item.getBackgroundColor(),
                        PorterDuff.Mode.SRC_ATOP);
            } else {
                ic.setVisibility(View.GONE);
            }
            */

            TextView tv = (TextView) view.findViewById(android.R.id.title);
            tv.setTextColor(dialog.getBuilder().getItemColor());
            tv.setText(item.getContent());
            dialog.setTypeface(tv, dialog.getBuilder().getRegularFont());
        }
        return view;
    }

//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//    private boolean isRTL() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
//            return false;
//        Configuration config = getContext().getResources().getConfiguration();
//        return config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
//    }
}


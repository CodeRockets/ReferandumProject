package com.coderockets.referandumproject.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

/**
 * Created by aykutasil on 18.09.2016.
 */
class MenuItemIconLoader {

    private final WeakReference<MenuItem> itemWeakReference;
    private final int targetHeight;
    private final int targetWidth;

    public MenuItemIconLoader(MenuItem menuItem, int targetHeight, int targetWidth) {
        this.itemWeakReference = new WeakReference<>(menuItem);
        this.targetHeight = targetHeight;
        this.targetWidth = targetWidth;
    }

    private final Target target = new Target() {

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            MenuItem menuItem = itemWeakReference.get();
            if (menuItem != null) {
                //Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                //drawable.setBounds(0, 0, targetWidth, targetHeight);
                //menuItem.setIcon(drawable);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };


    //public void load(Context context, Service service) {
    //    Picasso.with(context).load(service.getIconURL()).resize(targetWidth, targetHeight).into(target);
    //}

}
package com.coderockets.referandumproject.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.coderockets.referandumproject.R;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 30.10.2016.
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    public static final float ALPHA_FULL = 1.0f;

    private Paint p = new Paint();
    private final ItemTouchHelperAdapter mAdapter;
    private Context mContext;
    private RecyclerView mRecyclerView;

    public SimpleItemTouchHelperCallback(Context context, ItemTouchHelperAdapter adapter, RecyclerView recyclerView) {
        this.mAdapter = adapter;
        this.mContext = context;
        this.mRecyclerView = recyclerView;
    }

    @DebugLog
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @DebugLog
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @DebugLog
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // Set movement flags based on the layout manager
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @DebugLog
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {

        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }

        // Notify the adapter of the move
        mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @DebugLog
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        /*mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
        itemViewHolder.onDismissed();*/

        MaterialDialog dialog = UiHelper.UiDialog.newInstance(mContext).getOKCancelDialog("Uyarı", "Sorunuz silinecek.\nDevam etmek istiyor musunuz ?", null);
        dialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(view -> {
            dialog.dismiss();
            mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onDismissed();
        });
        dialog.getActionButton(DialogAction.NEGATIVE).setOnClickListener(view -> {
            dialog.dismiss();
            mRecyclerView.setAdapter(mRecyclerView.getAdapter());
        });
        dialog.show();

    }

    @DebugLog
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            Bitmap icon;
            View itemView = viewHolder.itemView;

            final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            float height = (float) itemView.getBottom() - (float) itemView.getTop();
            float width = height / 3;

            if (dX > 0) {
                //p.setColor(Color.parseColor("#388E3C"));
                p.setColor(Color.WHITE);
                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                c.drawRect(background, p);
                icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_delete_red_900_48dp);
                RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                c.drawBitmap(icon, null, icon_dest, p);
            } else {
                //p.setColor(Color.parseColor("#D32F2F"));
                p.setColor(Color.WHITE);
                RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                c.drawRect(background, p);
                icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_delete_red_900_48dp);
                RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                c.drawBitmap(icon, null, icon_dest, p);
            }

            // item swipe ın sonunda gelirken arka planı beyaz ile boyuyoruz
            // Eğer boyamazsak çizdiğimiz bitmap ekranda kalıyor.
            if (alpha < 0.01f) {
                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                c.drawRect(background, p);
            }

            itemView.setAlpha(alpha);
            itemView.setTranslationX(dX);

        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }


    @DebugLog
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // We only want the active item to change
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ItemTouchHelperViewHolder) {
                //this.mSelectedViewHolder = viewHolder;
                // Let the view holder know that this item is being moved or dragged
                ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @DebugLog
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        viewHolder.itemView.setAlpha(ALPHA_FULL);

        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            // Tell the view holder it's time to restore the idle state
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemClear();
        }
    }
}
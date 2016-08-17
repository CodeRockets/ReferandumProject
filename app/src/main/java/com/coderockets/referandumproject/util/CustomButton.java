package com.coderockets.referandumproject.util;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;

import com.joanzapata.iconify.widget.IconButton;
import com.orhanobut.logger.Logger;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 6.08.2016.
 */
public class CustomButton extends IconButton {


    Paint mPaint;
    Canvas mCanvas;

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(10);

        //setTextColor(Color.WHITE);
        //setBackgroundColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.mCanvas = canvas;
        //canvas.drawLine(0, 0, 400, 400, mPaint);

        super.onDraw(canvas);

    }

    public void changeButtonBgColor() {
        setBackgroundColor(Color.RED);
    }


    @DebugLog
    public void changeButtonScale() {

        getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
            @DebugLog
            @Override
            public void onDraw() {

            }
        });

        String infos = "getBaseline(): " + getBaseline() + " getBottom(): " + getBottom()
                + " getHeight(): " + getHeight() + " getLineHeight(): " + getLineHeight()
                + " getWidth(): " + getWidth() + " getX(): " + getX() + " getPivotX(): " + getPivotX()
                + " getY(): " + getY() + " getPivotY():" + getPivotY();
        Logger.i("infos: " + infos);
        //ScaleAnimation scale = new ScaleAnimation((float)1.0, (float)1.0, (float)1.0, (float)4.0);
        //scale.setFillAfter(true);
        //scale.setDuration(500);
        //imageView.startAnimation(scale);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.8f, 0.6f, 0.5f);
        valueAnimator.setDuration(1000);


        ResizeAnimation resizeAnimation = new ResizeAnimation(this, getMeasuredHeight() + 200, getMeasuredHeight());
        resizeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setAlpha(0.5f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });


        resizeAnimation.setDuration(2000);
        startAnimation(resizeAnimation);

    }

}

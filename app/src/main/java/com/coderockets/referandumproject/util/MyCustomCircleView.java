package com.coderockets.referandumproject.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.coderockets.referandumproject.R;

/**
 * Created by aykutasil on 19.06.2016.
 */
public class MyCustomCircleView extends View {

    float dim;
    int shape;
    Paint paint;
    int color;

    public MyCustomCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // R.styleable.deger  tanımlamasındaki, deger kelimesinin yerine gelecek kelime value/attrs.xml de tanımlanır
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MyCircleView,
                0, 0
        );

        try {
            // value/attrs.xml' de attr tagleri arasında kullanılan özellikleri atadık
            dim = a.getDimension(R.styleable.MyCircleView_dim, 20f);
            shape = a.getInteger(R.styleable.MyCircleView_shape, 0);
            color = a.getColor(R.styleable.MyCircleView_shape_color, Color.BLUE);
        } finally {
            a.recycle();
        }

        //Yuvarlagın rengini belirledik
        paint = new Paint();
        paint.setColor(color);
    }

    /**
     * Verilen boyut, renk gibi özellklere göre yuvarlıgı çizen metod (drawCircle) kullanıldı
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(dim, dim, dim, paint);
    }


}

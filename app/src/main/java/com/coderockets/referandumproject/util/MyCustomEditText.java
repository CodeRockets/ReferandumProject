package com.coderockets.referandumproject.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by aykutasil on 21.06.2016.
 */
public class MyCustomEditText extends EditText {

    public MyCustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }


}

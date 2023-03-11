package com.youwu.shouyinsaas.utils_view;


import android.content.Context;
import android.util.AttributeSet;

/**
 *  用于光标总是文本最后
 */
public class CustomEditText extends androidx.appcompat.widget.AppCompatEditText {

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context) {
        super(context);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        //保证光标始终在最后面
        if(selStart==selEnd){//防止不能多选
            setSelection(getText().length());
        }
    }
}



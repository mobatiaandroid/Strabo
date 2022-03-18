package com.vkc.strabo.manager;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.vkc.strabo.R;


public class CustomButtonWhite extends androidx.appcompat.widget.AppCompatButton {

    public CustomButtonWhite(Context context) {
        super(context);
        setFont();
    }

    public CustomButtonWhite(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CustomButtonWhite(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Bold.ttf");
        setTypeface(font, Typeface.NORMAL);
        setTextColor(getContext().getResources().getColor(R.color.white));
    }
}

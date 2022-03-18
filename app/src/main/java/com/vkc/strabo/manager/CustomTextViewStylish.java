package com.vkc.strabo.manager;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by user2 on 24/8/17.
 */
public class CustomTextViewStylish extends TextView {

    public CustomTextViewStylish(Context context) {
        super(context);
        setFont();
    }

    public CustomTextViewStylish(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CustomTextViewStylish(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Bold.ttf");
       // setTypeface(font, Typeface.BP);
        // setTextColor(getContext().getResources().getColor(R.color.black));
    }
}
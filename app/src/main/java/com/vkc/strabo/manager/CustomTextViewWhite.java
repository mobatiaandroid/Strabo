package com.vkc.strabo.manager;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.vkc.strabo.R;

/**
 * Created by user2 on 8/8/17.
 */
public class CustomTextViewWhite extends androidx.appcompat.widget.AppCompatTextView {

    public CustomTextViewWhite(Context context) {
        super(context);
        setFont();
    }

    public CustomTextViewWhite(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CustomTextViewWhite(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Light.ttf");
    //    setTypeface(font, Typeface.NORMAL);
        setTextColor(getContext().getResources().getColor(R.color.white));
    }
}


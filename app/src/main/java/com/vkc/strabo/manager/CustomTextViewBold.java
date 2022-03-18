package com.vkc.strabo.manager;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by user2 on 16/8/17.
 */
public class CustomTextViewBold extends androidx.appcompat.widget.AppCompatTextView {

    public CustomTextViewBold(Context context) {
        super(context);
        setFont();
    }

    public CustomTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CustomTextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Bold.ttf");
      //  setTypeface(font, Typeface.BOLD);
        // setTextColor(getContext().getResources().getColor(R.color.black));
    }
}
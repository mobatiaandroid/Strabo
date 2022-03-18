package com.vkc.strabo.manager;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vkc.strabo.R;

/**
 * Created by user2 on 21/2/18.
 */
public class CustomToastMessage {

    Activity mActivity;
    TextView mTextView;
    Toast mToast;
    String message;

    public CustomToastMessage(Activity mActivity, String message) {
        this.mActivity = mActivity;
        this.message = message;
        init();

    }

    public void init() {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View layouttoast = inflater.inflate(R.layout.custom_toast, null);
        mTextView = (TextView) layouttoast.findViewById(R.id.texttoast);

        mToast = new Toast(mActivity);
        mToast.setView(layouttoast);

        mTextView.setText(message);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }
}
package com.vkc.strabo.activity.common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vkc.strabo.R;
import com.vkc.strabo.constants.VKCUrlConstants;
import com.vkc.strabo.utils.UtilityMethods;

/**
 * Created by user2 on 27/7/17.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, VKCUrlConstants {

    Button btnLogin;
    TextView textSignup;
    Activity mContext;
    EditText mEditUserName, mEditPassword;
    String imei_no;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("PLAYGROUND", "Permission is not granted, requesting");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 123);
            //   button.setEnabled(false);
        } else {
            TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imei_no = mngr.getDeviceId();
        }

        initUI();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                imei_no = mngr.getDeviceId();
            } else {
                Log.d("PLAYGROUND", "Permission has been denied or request cancelled");

            }
        }
    }
    private void initUI() {
        btnLogin = (Button) findViewById(R.id.buttonLogin);
        textSignup = (TextView) findViewById(R.id.textSignup);
        mEditUserName = (EditText) findViewById(R.id.editUserName);
        mEditPassword = (EditText) findViewById(R.id.editPassword);
        textSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                if (mEditUserName.getText().toString().trim().equals("")) {
                    UtilityMethods.setErrorForEditText(mEditUserName,"Mandatory Field");
                } else if (mEditPassword.getText().toString().trim().equals("")) {
                    UtilityMethods.setErrorForEditText(mEditPassword,"Mandatory Field");
                } else {
                   // loginRequest();
                }
               /* startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();*/
                break;
            case R.id.textSignup:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                // finish();
                break;
        }
    }

 /*   public void loginRequest() {
        try {
            String[] name = {"email", "password", "imei_no"};
            String[] values = {mEditUserName.getText().toString(), mEditPassword.getText().toString(), imei_no};

            final VolleyWrapper manager = new VolleyWrapper(LOGIN__URL);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    JSONArray objResponseArray = rootObject.optJSONArray("response");
                                    JSONObject objResponse = objResponseArray.getJSONObject(0);

                                    String login = objResponse.optString("login");
                                    String role_id = objResponse.optString("role_id");
                                //    if (login.equalsIgnoreCase("success") && !role_id.equals("6")) {
                                        String user_id = objResponse.optString("user_id");
                                        String cust_id = objResponse.optString("cust_id");
                                        String user_name = objResponse.optString("shopName");
                                        AppPrefenceManager.saveUserName(mContext, user_name);
                                        AppPrefenceManager.saveUserId(mContext, user_id);
                                        AppPrefenceManager.saveCustomerId(mContext, cust_id);
                                    AppPrefenceManager.saveLoginStatusFlag(mContext,"yes");
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        finish();


                                //    } else {
                                      //  CustomToast toast = new CustomToast(mContext);
                                    //    toast.show(4);
                                 //   }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                CustomToast toast = new CustomToast(mContext);
                                toast.show(0);
                            }
                        }

                        @Override
                        public void responseFailure(String failureResponse) {
                            //CustomStatusDialog(RESPONSE_FAILURE);
                        }

                    });
        } catch (Exception e) {
            // CustomStatusDialog(RESPONSE_FAILURE);
            e.printStackTrace();
            Log.d("TAG", "Common error");
        }
    }*/
}
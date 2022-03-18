package com.vkc.strabo.activity.profile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mindorks.paracamera.Camera;
import com.squareup.picasso.Picasso;
import com.vkc.strabo.R;
import com.vkc.strabo.activity.common.SignUpActivity;
import com.vkc.strabo.activity.dealers.DealersActivity;
import com.vkc.strabo.activity.my_customers.MyCustomersActivity;
import com.vkc.strabo.constants.VKCUrlConstants;
import com.vkc.strabo.manager.AppPrefenceManager;
import com.vkc.strabo.manager.HeaderManager;
import com.vkc.strabo.utils.AndroidMultiPartEntity;
import com.vkc.strabo.utils.CustomToast;
import com.vkc.strabo.utils.UtilityMethods;
import com.vkc.strabo.volleymanager.VolleyWrapper;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Bibin Johnson on 9/8/17.
 */
public class ProfileActivity extends Activity implements View.OnClickListener, VKCUrlConstants {
    Camera camera;
    private static final int OPEN_DOCUMENT_CODE = 2;
    ArrayAdapter<String> adapter;
    String[] items;
    private String imagepath = "";
    private String imagepath1 = "";
    Activity mContext;
    AlertDialog dialogPic;
    File finalFilePhoto;
    AlertDialog.Builder builder;
    HeaderManager headermanager;
    LinearLayout relativeHeader;
    ImageView mImageBack, imageProfile;
    Button buttonUpdate;
    EditText editMobile, editOwner, editShop, editState, editDist, editPlace, editPin, editAddress, editMobile2, editEmail;
    TextView textCustId, textMydealers, textUpdate, textMyCustomers;
    String filePath = "";
    int ACTIVITY_REQUEST_CODE = 700;
    int ACTIVITY_FINISH_RESULT_CODE = 701;
    private Uri mImageCaptureUri;
    String otpValue = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setContentView(R.layout.activity_profile);
        mContext = this;
        initUI();
        getProfile();


    }


    private void initUI() {
        relativeHeader = (LinearLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(ProfileActivity.this, getResources().getString(R.string.profile));
        headermanager.getHeader(relativeHeader, 1);
        mImageBack = headermanager.getLeftButton();

        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        textCustId = (TextView) findViewById(R.id.textCustId);
        textUpdate = (TextView) findViewById(R.id.textUpdate);
        textMydealers = (TextView) findViewById(R.id.textMydealers);
        textMyCustomers = (TextView) findViewById(R.id.textMyCustomers);
        mImageBack.setOnClickListener(this);
        imageProfile = (ImageView) findViewById(R.id.imageProfile);
        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        editMobile = (EditText) findViewById(R.id.editMobile);
        editOwner = (EditText) findViewById(R.id.editOwner);
        editShop = (EditText) findViewById(R.id.editShop);
        editState = (EditText) findViewById(R.id.editState);
        editDist = (EditText) findViewById(R.id.editDistrict);
        editPlace = (EditText) findViewById(R.id.editPlace);
        editPin = (EditText) findViewById(R.id.editPin);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editMobile2 = (EditText) findViewById(R.id.editMobile2);
        editEmail = (EditText) findViewById(R.id.editEmail);
        // editMobile.setEnabled(false);
        editOwner.setEnabled(true);
        editShop.setEnabled(false);
        editState.setEnabled(false);
        editDist.setEnabled(false);
        editPlace.setEnabled(true);
        editPin.setEnabled(false);
        editAddress.setEnabled(false);
        if (AppPrefenceManager.getUserType(mContext).equals("6")) {
            textMydealers.setVisibility(View.GONE);
            textMyCustomers.setVisibility(View.VISIBLE);
        } else {
            textMyCustomers.setVisibility(View.GONE);

            textMydealers.setVisibility(View.VISIBLE);

        }
        buttonUpdate.setOnClickListener(this);
        textMydealers.setOnClickListener(this);
        imageProfile.setOnClickListener(this);
        textUpdate.setOnClickListener(this);
        textMyCustomers.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == mImageBack) {
            finish();
        } else if (v == buttonUpdate) {


            UpdateProfile upload = new UpdateProfile();
            upload.execute();

        } else if (v == imageProfile) {

           /* if ((int) Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(mContext,
                            new String[]{Manifest.permission.CAMERA},
                            100);

                    // Log.e("If permission is not granted", ", request for permission";


                } else {
                    createDialog();
                }
            }*/ /*else {
                createDialog();
            }*/

            createDialog();

        } else if (v == textMydealers) {
            startActivity(new Intent(ProfileActivity.this, DealersActivity.class));
        } else if (v == textMyCustomers) {
            startActivity(new Intent(ProfileActivity.this, MyCustomersActivity.class));
        } else if (v == textUpdate) {
            if (editMobile.getText().toString().trim().length() > 0) {
                if (editMobile.getText().toString().trim().length() == 10) {

                    //Update mobile dialog

                    if (AppPrefenceManager.getMobile(mContext).equals(editMobile.getText().toString().trim())) {

                    } else {
                        DialogUpdateMobile dialog = new DialogUpdateMobile(mContext);
                        dialog.show();
                    }

                } else {
                    CustomToast toast = new CustomToast(mContext);
                    toast.show(54);
                }

            } else {

                CustomToast toast = new CustomToast(mContext);
                toast.show(54);

            }
        }

    }

    public void getProfile() {
        try {

            String[] name = {"cust_id", "role"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), AppPrefenceManager.getUserType(mContext)};
            final VolleyWrapper manager = new VolleyWrapper(GET_PROFILE);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    JSONObject objResponse = rootObject.optJSONObject("response");
                                    String status = objResponse.optString("status");
                                    if (status.equals("Success")) {
                                        JSONObject objData = objResponse.optJSONObject("data");
                                        String name = objData.optString("name");
                                        String contact_person = objData.optString("contact_person");
                                        String district = objData.optString("district");
                                        String city = objData.optString("city");
                                        String state_name = objData.optString("state_name");
                                        String pincode = objData.optString("pincode");
                                        String phone = objData.optString("phone");
                                        String url = objData.optString("image");
                                        String mobile2 = objData.optString("phone2");
                                        String email = objData.optString("email");
                                        editMobile2.setText(mobile2);
                                        editEmail.setText(email);
                                        editShop.setText(name);
                                        editOwner.setText(contact_person);
                                        editDist.setText(district);
                                        editMobile.setText(phone);
                                        editPlace.setText(city);
                                        editState.setText(state_name);
                                        editPin.setText(pincode);
                                        editAddress.setText(objData.optString("address"));
                                        textCustId.setText("CUST_ID: - " + objData.optString("customer_id"));

                                        Picasso.with(mContext).load(url).placeholder(R.drawable.profile_image).into(imageProfile);//.transform(new CircleTransform())

                                    }


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
            //Log.d("TAG", "Common error");
        }
    }

    private class UpdateProfile extends AsyncTask<Void, Integer, String> {
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        private JSONObject obj;
        private String responseString = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(UPDATE_PROFILE);
                File file = new File(filePath);
                FileBody bin1 = new FileBody(file.getAbsoluteFile());
                AndroidMultiPartEntity entity;
                entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {

                            }
                        });
                entity.addPart("cust_id", new StringBody(AppPrefenceManager.getCustomerId(mContext)));
                entity.addPart("role", new StringBody(AppPrefenceManager.getUserType(mContext)));
                entity.addPart("phone", new StringBody(editMobile.getText().toString().trim()));
                entity.addPart("contact_person", new StringBody(editOwner.getText().toString().trim()));
                entity.addPart("city", new StringBody(editPlace.getText().toString().trim()));
                entity.addPart("phone2", new StringBody(editMobile2.getText().toString().trim()));
                entity.addPart("email", new StringBody(editEmail.getText().toString().trim()));
                if (filePath.equals("")) {

                } else {
                    entity.addPart("image", bin1);
                }

                httppost.setEntity(entity);


                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {

                    responseString = EntityUtils.toString(r_entity);


                } else {

                    responseString = EntityUtils.toString(r_entity);
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
                Log.e("UploadApp", "exception: " + responseString);
            } catch (IOException e) {
                responseString = e.toString();
                Log.e("UploadApp", "exception: " + responseString);
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            System.out.print("Result " + result);
            try {
                obj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject responseObj = obj.optJSONObject("response");
            responseString = responseObj.optString("status");

            if (responseString.equals("Success")) {
                CustomToast toast = new CustomToast(mContext);
                toast.show(26);
                getProfile();
            } else {
                CustomToast toast = new CustomToast(mContext);
                toast.show(27);
            }
        }

    }


    private int dpToPx(float dp) {
        float density = mContext.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }


    public void updateMobile() {
        try {

            String[] name = {"cust_id", "role", "phone"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), AppPrefenceManager.getUserType(mContext), editMobile.getText().toString().trim()};
            final VolleyWrapper manager = new VolleyWrapper(UPDATE_MOBILE);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    JSONObject objResponse = rootObject.optJSONObject("response");
                                    String status = objResponse.optString("status");
                                    if (status.equals("Success")) {
                                        OTPDialog dialog = new OTPDialog(mContext);
                                        dialog.show();
                                    } else {
                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(56);
                                    }


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
            //  Log.d("TAG", "Common error");
        }
    }

    public class DialogUpdateMobile extends Dialog implements
            View.OnClickListener {

        public Activity mActivity;
        String type, message;

        public DialogUpdateMobile(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.mActivity = a;


        }

        public DialogUpdateMobile(Activity a, String type, String message) {
            super(a);
            this.type = type;
            this.message = message;
            // TODO Auto-generated constructor stub


        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_update_mobile);
            init();

        }

        private void init() {

            // Button buttonSet = (Button) findViewById(R.id.buttonOk);
            TextView textYes = (TextView) findViewById(R.id.textYes);
            TextView textNo = (TextView) findViewById(R.id.textNo);

            textYes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                    // mActivity.finish();

                    updateMobile();

                }
            });
            textNo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();


                }
            });

        }

        @Override
        public void onClick(View v) {

            dismiss();
        }

    }

    public class OTPDialog extends Dialog implements
            View.OnClickListener {

        public Activity mActivity;


        public OTPDialog(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.mActivity = a;


        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            setContentView(R.layout.dialog_otp_mobile);
            init();

        }

        private void init() {

            final EditText editOtp1 = (EditText) findViewById(R.id.editOtp1);
            final EditText editOtp2 = (EditText) findViewById(R.id.editOtp2);
            final EditText editOtp3 = (EditText) findViewById(R.id.editOtp3);
            final EditText editOtp4 = (EditText) findViewById(R.id.editOtp4);

            TextView textOtp = (TextView) findViewById(R.id.textOtp);
            TextView textCancel = (TextView) findViewById(R.id.textCancel);
            String mob = AppPrefenceManager.getMobile(mContext).substring(6, 10);
            textOtp.setText("OTP has been sent to  XXXXXX" + mob);
            editOtp1.setCursorVisible(false);
            textCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    dismiss();

                }
            });

            editOtp1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editOtp1.setBackgroundResource(R.drawable.rounded_rect_full_white);
                    if (s.length() == 1) {
                        editOtp1.clearFocus();
                        editOtp2.requestFocus();
                    }
                    otpValue = editOtp1.getText().toString().trim() + editOtp2.getText().toString().trim() + editOtp3.getText().toString().trim() + editOtp4.getText().toString().trim();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        editOtp1.setBackgroundResource(R.drawable.rounded_rect_line);
                    }

                }
            });
            editOtp2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editOtp2.setBackgroundResource(R.drawable.rounded_rect_full_white);
                    if (s.length() == 1) {
                        editOtp2.clearFocus();
                        editOtp3.requestFocus();
                    }

                    otpValue = editOtp1.getText().toString().trim() + editOtp2.getText().toString().trim() + editOtp3.getText().toString().trim() + editOtp4.getText().toString().trim();
                    if (otpValue.length() == 1) {
                        editOtp2.clearFocus();
                        editOtp1.requestFocus();

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        editOtp2.setBackgroundResource(R.drawable.rounded_rect_line);
                    }

                }
            });
            editOtp3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editOtp3.setBackgroundResource(R.drawable.rounded_rect_full_white);
                    if (s.length() == 1) {
                        editOtp3.clearFocus();
                        editOtp4.requestFocus();
                    }

                    otpValue = editOtp1.getText().toString().trim() + editOtp2.getText().toString().trim() + editOtp3.getText().toString().trim() + editOtp4.getText().toString().trim();
                    if (otpValue.length() == 2) {
                        editOtp3.clearFocus();
                        editOtp2.requestFocus();
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        editOtp3.setBackgroundResource(R.drawable.rounded_rect_line);
                    }

                }
            });
            editOtp4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editOtp4.setBackgroundResource(R.drawable.rounded_rect_full_white);
                    otpValue = editOtp1.getText().toString().trim() + editOtp2.getText().toString().trim() + editOtp3.getText().toString().trim() + editOtp4.getText().toString().trim();

                    if (otpValue.length() == 3) {
                        editOtp3.requestFocus();
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        editOtp4.setBackgroundResource(R.drawable.rounded_rect_line);
                    } else {

                        verifyOTP(otpValue, editMobile.getText().toString().trim());
                    }


                }
            });


            Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
            buttonCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

        }

        @Override
        public void onClick(View v) {

            dismiss();
        }

    }

    public void verifyOTP(String otp, String mobile) {
        try {
            String[] name = {"otp", "role", "cust_id", "phone", "isnewMobile"};
            String[] values = {otp, AppPrefenceManager.getUserType(mContext), AppPrefenceManager.getCustomerId(mContext), mobile, "1"};

            final VolleyWrapper manager = new VolleyWrapper(OTP_VERIFY_URL);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    JSONObject objResponse = rootObject.optJSONObject("response");
                                    String status = objResponse.optString("status");

                                    if (status.equalsIgnoreCase("Success")) {


                                        //    AppPrefenceManager.setIsVerifiedOTP(mContext, "yes");
                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(55);
                                        Intent intent = new Intent(ProfileActivity.this, SignUpActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);


                                    } else {

                                    }

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


    }

    PermissionListener permissionCameralistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            onPickCamera();

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(mContext, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();

        }


    };
    PermissionListener permissionListenerGallery = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            onPickCameraGallery();

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(mContext, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();

        }


    };

    public void createDialog() {
        builder = new AlertDialog.Builder(this);

        // builder.setTitle(R.string.take_picture);

        items = new String[]{mContext.getResources().getString(R.string.take_picture),
                mContext.getResources().getString(R.string.open_gallery)};
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, items);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) { // pick from
                // camera
                if (item == 0) {
                    if ((int) Build.VERSION.SDK_INT >= 23) {
                        TedPermission.with(mContext)
                                .setPermissionListener(permissionCameralistener)
                                .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                                .check();
                    } else {
                        onPickCamera();
                    }
                } else {

                    if ((int) Build.VERSION.SDK_INT >= 23) {
                        TedPermission.with(mContext)
                                .setPermissionListener(permissionListenerGallery)
                                .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                                .check();
                    } else {
                        onPickCameraGallery();
                    }
                }


            }
        });

        dialogPic = builder.create();
        dialogPic.show();
    }

    public void onPickCamera() {

// Build the camera
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("Strabo_Pic" + mContext.getResources().getString(R.string.app_name) + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .build(this);

        // Call the camera takePicture method to open the existing camera
        try {
            camera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPickCameraGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_DOCUMENT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Picture:", "Picture requestCode! " + requestCode);
        Log.d("Picture:", "Picture resultCode! " + resultCode);
        Log.d("Picture:", "Picture data! " + data);

        if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Log.d("Picture:", "Picture kayarundu!");

            Bitmap bitmap = camera.getCameraBitmap();
            if (bitmap != null) {


                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                finalFilePhoto = new File(getRealPathFromURI(tempUri));
                filePath = getRealPathFromURI(tempUri);
//                 String path = FileUtils.getPath(this, tempUri);
                imageProfile.setImageBitmap(bitmap);
                imagepath = UtilityMethods.toFile(bitmap, "straboimage1.jpg");
//                if (dialogChooser != null)
//                    dialogChooser.dismiss();
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                Log.d("Picture:bitmap", bitmap.toString());


            } else {
//                Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
                Log.d("Picture:", "Picture not taken!");

            }
        }
        if (requestCode == OPEN_DOCUMENT_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // this is the image selected by the user
            Uri imageUri = data.getData();
            InputStream is = null;
            Bitmap bitmap = null;
            try {
                is = getContentResolver().openInputStream(imageUri);
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bitmap != null) {

                Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                finalFilePhoto = new File(getRealPathFromURI(tempUri));
                filePath = getRealPathFromURI(tempUri);

                imageProfile.setImageBitmap(bitmap);
                imagepath = UtilityMethods.toFile(bitmap, "straboimage2.jpg");
//                    if (dialogChooser != null)
//                        dialogChooser.dismiss();

            }

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}
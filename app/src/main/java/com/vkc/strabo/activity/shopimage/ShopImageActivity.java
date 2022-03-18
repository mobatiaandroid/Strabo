
package com.vkc.strabo.activity.shopimage;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mindorks.paracamera.Camera;
import com.squareup.picasso.Picasso;
import com.vkc.strabo.R;
import com.vkc.strabo.activity.shopimage.model.ImageListModel;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user2 on 11/8/17.
 */

public class ShopImageActivity extends Activity implements VKCUrlConstants, View.OnClickListener {


    AlertDialog.Builder builder;
    Camera camera;
    private static final int OPEN_DOCUMENT_CODE = 2;
    ArrayAdapter<String> adapter;
    String[] items;
    private String imagepath = "";
    private String imagepath1 = "";
    File finalFilePhoto;
    AlertDialog dialogPic;
    ImageView imageShop;
    Activity mContext;
    HeaderManager headermanager;
    LinearLayout relativeHeader;
    ImageView mImageBack;
    private Uri mImageCaptureUri;
    int ACTIVITY_REQUEST_CODE = 700;
    int ACTIVITY_FINISH_RESULT_CODE = 701;
    String filePath = "";
    Button btnCapture, btnUpload;
    ArrayList<ImageListModel> imageList;
    ImageView image1, image2, image1Delete, image2Delete;
    RelativeLayout relative1, relative2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_image);
        mContext = this;
        initUI();
    }

    private void initUI() {
        imageList = new ArrayList<>();
        relativeHeader = (LinearLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(ShopImageActivity.this, getResources().getString(R.string.shop_image));
        headermanager.getHeader(relativeHeader, 1);
        mImageBack = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);

        imageShop = (ImageView) findViewById(R.id.imageShop);
        image1 = (ImageView) findViewById(R.id.imageOne);
        image2 = (ImageView) findViewById(R.id.imageTwo);
        image1Delete = (ImageView) findViewById(R.id.deleteImage1);
        image2Delete = (ImageView) findViewById(R.id.deleteImage2);
        image1Delete.setVisibility(View.GONE);
        image2Delete.setVisibility(View.GONE);
        relative1 = (RelativeLayout) findViewById(R.id.relative1);
        relative2 = (RelativeLayout) findViewById(R.id.relative2);

        btnCapture = (Button) findViewById(R.id.buttonCapture);
        btnUpload = (Button) findViewById(R.id.buttonUpload);
        getImage();
        mImageBack.setOnClickListener(this);
        btnCapture.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image1Delete.setOnClickListener(this);
        image2Delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mImageBack) {
            finish();
        } else if (v == btnCapture) {
           /* if ((int) Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(mContext,
                            new String[]{Manifest.permission.CAMERA},
                            100);

                    // Log.e("If permission is not granted", ", request for permission";


                } else {
                    createDialog();
                }
            }*/

            // showCamera();
            createDialog();


        } else if (v == btnUpload) {
            if (filePath.equals("")) {
                CustomToast toast = new CustomToast(
                        mContext);
                toast.show(21);
            } else {
                try {
                    UploadFileToServer upload = new UploadFileToServer();
                    upload.execute();
                } catch (Exception e) {

                }
            }
        } else if (v == image1Delete) {

            DialogConfirm dialog = new DialogConfirm(mContext, imageList.get(0).getId());
            dialog.show();
            //  deleteImage(imageList.get(0).getId());
        } else if (v == image2Delete) {

            DialogConfirm dialog = new DialogConfirm(mContext, imageList.get(1).getId());
            dialog.show();

        }
    }


    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        private JSONObject obj;
        private String responseString = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Uploading...");
            pDialog.setCanceledOnTouchOutside(false);
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


                HttpPost httppost = new HttpPost(UPLOAD_IMAGE);

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
                entity.addPart("image", bin1);
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

            if (responseString.equalsIgnoreCase("Success")) {

                CustomToast toast = new CustomToast(
                        mContext);
                toast.show(19);
                getImageHistory();
            } else if (responseString.equalsIgnoreCase("Exceeded")) {

                CustomToast toast = new CustomToast(
                        mContext);
                toast.show(20);
                getImageHistory();
            } else {
                CustomToast toast = new CustomToast(
                        mContext);
                toast.show(0);
            }
        }

    }

    public void showCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/" + getResources().getString(R.string.app_name));
        myDir.mkdirs();
        File file = new File(myDir, "tmp_avatar_"
                + String.valueOf(System.currentTimeMillis()) + ".JPEG");
        mImageCaptureUri = Uri.fromFile(file);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                mImageCaptureUri);
        try {
            cameraIntent.putExtra("return-data", true);
            startActivityForResult(cameraIntent, 0);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_REQUEST_CODE) {
            if (resultCode == ACTIVITY_FINISH_RESULT_CODE) {
                finish();
            }
        } else {
            Bitmap bitmap = null;
            Uri imageUri = null;
            if (resultCode != Activity.RESULT_OK)
                return;
            switch (requestCode) {
                case 0:
                    imageUri = mImageCaptureUri;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }
            if (bitmap != null) {
                try {
                    File tempFile = new File(imageUri.getPath());
                    long size = tempFile.length();
                    ByteArrayOutputStream byteArrayOutputStream;
                    Log.e("Size image:", "" + size);
                    int minSize = 600;
                    int widthOfImage = bitmap.getWidth();
                    int heightOfImage = bitmap.getHeight();
                    Log.e("Width", "" + widthOfImage);
                    Log.e("Height", "" + heightOfImage);
                    int newHeight = 600;
                    int newWidht = 600;
                    if (widthOfImage < minSize && heightOfImage < minSize) {
                        newWidht = widthOfImage;
                        newHeight = heightOfImage;
                    } else {
                        if (widthOfImage >= heightOfImage) {
                            //int newheght = heightOfImage/600;
                            float ratio = (float) minSize / widthOfImage;
                            Log.e("Multi width greater", "" + minSize + "/" + widthOfImage + "=" + ratio);
                            newHeight = (int) (heightOfImage * ratio);
                            newWidht = minSize;
                        } else if (heightOfImage > widthOfImage) {
                            float ratio = (float) minSize / heightOfImage;
                            newWidht = (int) (widthOfImage * ratio);
                            newHeight = minSize;
                        }

                    }
                    if (size > 1024 * 1024) {
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        Bitmap b;
                        b = Bitmap.createScaledBitmap(bitmap, newWidht, newHeight, true);
                        b.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

                    } else {
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        Bitmap b;
                        b = Bitmap.createScaledBitmap(bitmap, newWidht, newHeight, true);
                        b.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                    }


                    if (size > (4 * 1024 * 1024)) {
                        //CustomStatusDialog(RESPONSE_LARGE_IMAGE);
                        if (bitmap != null && !bitmap.isRecycled()) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                    } else {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        int bounding = dpToPx(mContext.getResources().getDisplayMetrics().density);
                        float xScale = (100 * (float) bounding) / width;
                        float yScale = (100 * (float) bounding) / height;
                        float scale = (xScale <= yScale) ? xScale : yScale;
                        Matrix matrix = new Matrix();
                        matrix.postScale(scale, scale);
                        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);


                        BitmapDrawable result = new BitmapDrawable(scaledBitmap);

                        imageShop.setImageDrawable(result);
                        imageShop.setScaleType(ImageView.ScaleType.FIT_XY);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        filePath = "/sdcard/file" + "vkc" + ".jpg";
                        File f = new File(filePath);
                        try {
                            f.createNewFile();
                            FileOutputStream fos = null;

                            fos = new FileOutputStream(f);
                            fos.write(byteArray);
                            fos.close();
                            filePath = f.getAbsolutePath();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        if (bitmap != null && !bitmap.isRecycled()) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                    }
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    }

                    // CustomStatusDialog(RESPONSE_OUT_OF_MEMORY);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                }
            }
        }
    }*/

    private int dpToPx(float dp) {
        float density = mContext.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public void getImage() {
        try {


            String[] name = {"cust_id", "role"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), AppPrefenceManager.getUserType(mContext)};

            final VolleyWrapper manager = new VolleyWrapper(UPLOADED_IMAGE);
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
                                        String imageUrl = objData.optString("image");
                                        Picasso.with(mContext).load(imageUrl).placeholder(R.drawable.shop_image).into(imageShop);

                                        getImageHistory();
                                    }

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
    }

    public void getImageHistory() {
        imageList.clear();
        try {


            String[] name = {"cust_id", "role"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext), AppPrefenceManager.getUserType(mContext)};

            final VolleyWrapper manager = new VolleyWrapper(GET_IMAGE_HISTORY);
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
                                        JSONArray objData = objResponse.optJSONArray("data");
                                        if (objData.length() > 0) {
                                            for (int i = 0; i < objData.length(); i++) {
                                                JSONObject obj = objData.optJSONObject(i);
                                                ImageListModel model = new ImageListModel();
                                                model.setImage(obj.getString("image"));
                                                model.setId(obj.getString("id"));
                                                imageList.add(model);
                                            }
                                            if (imageList.size() > 1) {
                                                if (!imageList.get(0).getImage().equals("")) {

                                                    relative1.setVisibility(View.VISIBLE);
                                                    image1Delete.setVisibility(View.VISIBLE);
                                                    Picasso.with(mContext).load(imageList.get(0).getImage()).resize(200, 200).centerInside().into(image1);
                                                } else {
                                                    relative1.setVisibility(View.GONE);
                                                    image1Delete.setVisibility(View.GONE);

                                                }

                                                if (!imageList.get(1).getImage().equals("")) {
                                                    relative2.setVisibility(View.VISIBLE);
                                                    image2Delete.setVisibility(View.VISIBLE);
                                                    Picasso.with(mContext).load(imageList.get(1).getImage()).resize(200, 200).centerInside().into(image2);
                                                } else {
                                                    relative2.setVisibility(View.GONE);
                                                    image2Delete.setVisibility(View.GONE);
                                                }
                                            } else {

                                                relative2.setVisibility(View.GONE);
                                                image2Delete.setVisibility(View.GONE);
                                                if (!imageList.get(0).getImage().equals("")) {

                                                    relative1.setVisibility(View.VISIBLE);
                                                    image1Delete.setVisibility(View.VISIBLE);
                                                    Picasso.with(mContext).load(imageList.get(0).getImage()).resize(200, 200).centerInside().into(image1);
                                                } else {
                                                    relative1.setVisibility(View.GONE);
                                                    image1Delete.setVisibility(View.GONE);

                                                }
                                            }

                                        } else {

                                            relative1.setVisibility(View.GONE);
                                            image1Delete.setVisibility(View.GONE);
                                            relative2.setVisibility(View.GONE);
                                            image2Delete.setVisibility(View.GONE);
                                            // initUI();
                                            CustomToast toast = new CustomToast(mContext);
                                            toast.show(51);

                                        }
                                       /* String imageUrl = objData.optString("image");
                                        Picasso.with(mContext).load(imageUrl).placeholder(R.drawable.shop_image).into(imageShop);*/
                                    }

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
    }


    public void deleteImage(String id) {

        try {


            String[] name = {"id"};
            String[] values = {id};

            final VolleyWrapper manager = new VolleyWrapper(DELETE_IMAGE);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    //JSONObject objResponse = rootObject.optJSONObject("response");
                                    String status = rootObject.optString("status");
                                    if (status.equals("Success")) {
                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(52);
                                        getImageHistory();
                                    } else if (status.equals("Error")) {
                                        getImageHistory();
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

    public class DialogConfirm extends Dialog implements
            android.view.View.OnClickListener {

        public Activity mActivity;
        String type, message, id;

        public DialogConfirm(Activity a, String id) {
            super(a);
            // TODO Auto-generated constructor stub
            this.mActivity = a;
            this.id = id;

        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_delete_image);
            init();

        }

        private void init() {

            // Button buttonSet = (Button) findViewById(R.id.buttonOk);
            TextView textYes = (TextView) findViewById(R.id.textYes);
            TextView textNo = (TextView) findViewById(R.id.textNo);


            textYes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    deleteImage(id);
                    dismiss();
                }
            });


            textNo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                    // mActivity.finish();


                }
            });

        }

        @Override
        public void onClick(View v) {

            dismiss();
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
                image1.setImageBitmap(bitmap);
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
                image1.setImageBitmap(bitmap);
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
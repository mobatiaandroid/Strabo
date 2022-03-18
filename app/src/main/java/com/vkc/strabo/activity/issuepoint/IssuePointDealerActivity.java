package com.vkc.strabo.activity.issuepoint;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.vkc.strabo.R;
import com.vkc.strabo.activity.issuepoint.model.UserModel;
import com.vkc.strabo.constants.VKCUrlConstants;
import com.vkc.strabo.manager.AppPrefenceManager;
import com.vkc.strabo.manager.HeaderManager;
import com.vkc.strabo.utils.CustomToast;
import com.vkc.strabo.volleymanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user2 on 10/8/17.
 */
public class IssuePointDealerActivity extends AppCompatActivity implements View.OnClickListener,
        VKCUrlConstants {


    int mDisplayWidth = 0;
    int mDisplayHeight = 0;
    private TextView mTxtPoint;
    List<String> categories = new ArrayList<String>();
    private ArrayList<String> listArticleNumbers;
    Spinner spinnerUserType;
    Button btnSubmit, btnReset;
    EditText mEditPoint;
    int myPoint = 0;
    String userType;
    String selectedId;
    private AutoCompleteTextView edtSearch;
    ArrayList<UserModel> listUsers;
    TextView textId, textName, textAddress, textPhone, textType;
    LinearLayout llData;
    HeaderManager headermanager;
    Activity mContext;
    private LinearLayout relativeHeader;
    private ImageView mImageBack;
    private LinearLayout llUserType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_issue_point);
        mContext = this;
        init();
    }


    private void init() {
        listUsers = new ArrayList<>();
        spinnerUserType = (Spinner) findViewById(R.id.spinnerUserType);
        relativeHeader = (LinearLayout) findViewById(R.id.relativeHeader);
        selectedId = "";
        headermanager = new HeaderManager(IssuePointDealerActivity.this, getResources().getString(R.string.issue_point));
        headermanager.getHeader(relativeHeader, 1);
        mImageBack = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        mImageBack.setOnClickListener(this);
        mTxtPoint = (TextView) findViewById(R.id.textPoints);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnReset = (Button) findViewById(R.id.btnReset);
        mEditPoint = (EditText) findViewById(R.id.editPoints);
        llData = (LinearLayout) findViewById(R.id.llData);
        llData.setVisibility(View.GONE);
        // llUserType = (LinearLayout) findViewById(R.id.llUserType);

        textId = (TextView) findViewById(R.id.textViewId);
        textName = (TextView) findViewById(R.id.textViewName);
        textAddress = (TextView) findViewById(R.id.textViewAddress);
        textPhone = (TextView) findViewById(R.id.textViewPhone);
        textType = (TextView) findViewById(R.id.textViewType);
        edtSearch = (AutoCompleteTextView) findViewById(R.id.autoSearch);

        btnSubmit.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        categories.clear();
        categories.add("Select User Type");
        categories.add("Retailer");
        categories.add("Sub Dealer");
        edtSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                edtSearch.showDropDown();
            }
        });
        edtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                String selectedData = edtSearch.getText().toString();
                for (int i = 0; i < listUsers.size(); i++) {
                    if (listUsers.get(i).getUserName().equals(selectedData)) {
                        selectedId = listUsers.get(i).getUserId();
                        System.out.println("Selected Id : " + selectedId);
                        getUserData();
                        break;
                    } else {
                        selectedId = "";
                    }
                }
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {

                } else {
                    selectedId = "";
                    llData.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                mContext, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerUserType.setAdapter(dataAdapter);
        spinnerUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long arg3) {
                // TODO Auto-generated method stub
                if (pos > 0) {
                    if (pos == 1) {
                        userType = "5";

                        selectedId = "";
                        edtSearch.setText("");
                        // mEditPoint.setText("");
                        getUsers(userType);
                    } else {
                        userType = "7";
                        selectedId = "";
                        edtSearch.setText("");
                        getUsers(userType);
                    }
                } else {
                    userType = "";
                }
                System.out.println("User Type : " + userType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        getMyPoints();
    }

    /**
     * Method FeedbackSubmitApi Return Type:void parameters:null Date:Feb 18,
     * 2015 Author:Archana.S
     */
    public void getMyPoints() {
        String name[] = {"cust_id", "role"};
        String values[] = {AppPrefenceManager.getCustomerId(mContext),
                AppPrefenceManager.getUserType(mContext)};
        final VolleyWrapper manager = new VolleyWrapper(GET_DEALER_POINT);
        manager.getResponsePOST(mContext, 11, name, values,
                new VolleyWrapper.ResponseListener() {

                    @Override
                    public void responseSuccess(String successResponse) {

                        parseResponse(successResponse);
                    }

                    @Override
                    public void responseFailure(String failureResponse) {

                    }

                });
    }

    /**
     * Method Name:parseResponse Return Type:void parameters:response Date:Feb
     * 18, 2015 Author:Archana.S
     */
    public void parseResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject objResponse = jsonObject.optJSONObject("response");
            String status = objResponse.optString("status");

            if (status.equals("Success")) {


                String points = objResponse.optString("loyality_point");
                myPoint = Integer.parseInt(points);
                mTxtPoint.setText(points);
            } else {

                // Bibin  VKCUtils.showtoast(mContext, 13);
            }

        } catch (Exception e) {

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnSubmit) {
            if (userType.equals("")) {
                CustomToast toast = new CustomToast(mContext);
                toast.show(49);
            } else if (edtSearch.getText().toString().trim().equals("")) {
                CustomToast toast = new CustomToast(mContext);
                toast.show(51);
            } else if (mEditPoint.getText().toString().trim().equals("")) {
                // VKCUtils.textWatcherForEditText(mEditPoint,
                // "Mandatory field");
                CustomToast toast = new CustomToast(mContext);
                toast.show(52);

            } else if (Integer.parseInt(mEditPoint.getText().toString().trim()) > myPoint) {
                // FeedbackSubmitApi();
                CustomToast toast = new CustomToast(mContext);
                toast.show(48);
            } else {

                submitPoints();

            }

        } else if (v == btnReset) {

            spinnerUserType.setSelection(0);
            userType = "";
            selectedId = "";
            edtSearch.setText("");
            mEditPoint.setText("");
            llData.setVisibility(View.GONE);
        } else if (v == mImageBack) {
            finish();
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        // edtSearch.setText(AppController.articleNumber);
        super.onResume();
    }

    private void getUsers(String type) {
        listUsers.clear();

        String name[] = {"cust_id", "user_type"};
        String values[] = {AppPrefenceManager.getCustomerId(mContext),
                type};
        final VolleyWrapper manager = new VolleyWrapper(GET_USERS);
        manager.getResponsePOST(mContext, 11, name, values,
                new VolleyWrapper.ResponseListener() {

                    @Override
                    public void responseSuccess(String successResponse) {
                        try {
                            JSONObject responseObj = new JSONObject(
                                    successResponse);
                            JSONObject response = responseObj
                                    .getJSONObject("response");
                            String status = response.getString("status");
                            if (status.equals("Success")) {
                                JSONArray dataArray = response
                                        .optJSONArray("data");
                                if (dataArray.length() > 0) {
                                    for (int i = 0; i < dataArray.length(); i++) {
                                        // listArticle[i]=articleArray.getString(i);
                                        JSONObject obj = dataArray
                                                .getJSONObject(i);
                                        UserModel model = new UserModel();
                                        model.setUserId(obj.getString("id"));
                                        model.setUserName(obj.getString("name"));
                                        // model.setCity(obj.getString("city"));
                                        listUsers.add(model);
                                    }
                                    ArrayList<String> listUser = new ArrayList<>();
                                    for (int i = 0; i < listUsers.size(); i++) {
                                        listUser.add(listUsers.get(i)
                                                .getUserName());
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                            mContext,
                                            android.R.layout.simple_list_item_1,
                                            listUser);
                                    edtSearch.setThreshold(1);
                                    edtSearch.setAdapter(adapter);

                                } else {
                                    CustomToast toast = new CustomToast(
                                            mContext);
                                    toast.show(17);
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void responseFailure(String failureResponse) { // TODO
                        // Auto-generated method stub

                    }
                });

    }

    public void submitPoints() {
        String name[] = {"userid", "to_user_id", "to_role", "points", "role"};
        String values[] = {AppPrefenceManager.getCustomerId(mContext),
                selectedId, userType, mEditPoint.getText().toString(),
                AppPrefenceManager.getUserType(mContext)};
        final VolleyWrapper manager = new VolleyWrapper(SUBMIT_POINTS);
        manager.getResponsePOST(mContext, 11, name, values,
                new VolleyWrapper.ResponseListener() {

                    @Override
                    public void responseSuccess(String successResponse) {
                        // TODO Auto-generated method stub
                        // Log.v("LOG", "18022015 success" + successResponse);
                        try {
                            JSONObject objResponse = new JSONObject(
                                    successResponse);
                            String status = objResponse.optString("response");
                            if (status.equals("1")) {

                                CustomToast toast = new CustomToast(
                                        mContext);
                                toast.show(18);
                                edtSearch.setText("");
                                mEditPoint.setText("");

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                                        mContext,
                                        android.R.layout.simple_spinner_item,
                                        categories);
                                dataAdapter
                                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerUserType.setAdapter(dataAdapter);
                                getMyPoints();
                            } else {
                                CustomToast toast = new CustomToast(
                                        mContext);
                                toast.show(67);

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        // parseResponse(successResponse);
                    }

                    @Override
                    public void responseFailure(String failureResponse) {
                        // TODO Auto-generated method stub
                        Log.v("LOG", "18022015 Errror" + failureResponse);
                    }
                });
    }


    private void getUserData() {
        //listUsers.clear();

        String name[] = {"cust_id", "role"};
        String values[] = {selectedId, userType};
        final VolleyWrapper manager = new VolleyWrapper(GET_USER_DATA);
        manager.getResponsePOST(mContext, 11, name, values,
                new VolleyWrapper.ResponseListener() {

                    @Override
                    public void responseSuccess(String successResponse) {

                        try {
                            JSONObject responseObj = new JSONObject(
                                    successResponse);
                            JSONObject response = responseObj
                                    .getJSONObject("response");
                            String status = response.getString("status");
                            if (status.equals("Success")) {
                                JSONObject objData = response
                                        .optJSONObject("data");
                                String cust_id = objData
                                        .optString("customer_id");
                                String address = objData.optString("address");
                                String name = objData.optString("name");
                                String phone = objData.optString("phone");

                                if (userType.equals("5")) {
                                    textType.setText(": " + "Retailer");
                                } else if (userType.equals("7")) {
                                    textType.setText(": " + "Sub Dealer");
                                }
                                textId.setText(": " + cust_id);
                                textName.setText(": " + name);
                                textAddress.setText(": " + address);
                                textPhone.setText(": " + phone);
                                llData.setVisibility(View.VISIBLE);

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void responseFailure(String failureResponse) { // TODO
                        // Auto-generated method stub

                    }
                });

    }
}
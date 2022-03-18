package com.vkc.strabo.activity.my_customers;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vkc.strabo.R;
import com.vkc.strabo.activity.dealers.model.DealerModel;
import com.vkc.strabo.activity.my_customers.adapter.CustomerAdapter;
import com.vkc.strabo.activity.my_customers.model.CustomerModel;
import com.vkc.strabo.constants.VKCUrlConstants;
import com.vkc.strabo.manager.AppPrefenceManager;
import com.vkc.strabo.manager.HeaderManager;
import com.vkc.strabo.utils.CustomToast;
import com.vkc.strabo.volleymanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by user2 on 7/8/17.
 */
public class MyCustomersActivity extends Activity implements VKCUrlConstants, View.OnClickListener {
    Activity mContext;
    ArrayList<DealerModel> listDealers;
    ListView listViewDealer;
    TextView textSubmit;
    // ArrayList<String> listIds;
    HeaderManager headermanager;
    LinearLayout relativeHeader;
    ImageView mImageBack;


    ArrayList<CustomerModel> listCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setContentView(R.layout.activity_my_customers);
        mContext = this;
        ininUI();
        getCustomers();
    }

    private void ininUI() {
        listCustomers = new ArrayList<>();

        listViewDealer = (ListView) findViewById(R.id.listMyCustomer);
        // mImageSearch = (ImageView) findViewById(R.id.imageSearch);

        relativeHeader = (LinearLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(MyCustomersActivity.this, getResources().getString(R.string.my_customers));
        headermanager.getHeader(relativeHeader, 1);
        mImageBack = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        mImageBack.setOnClickListener(this);

    }

    public void getCustomers() {
        try {

            String name[] = { "cust_id" };
            String values[] = { AppPrefenceManager.getCustomerId(mContext) };
            final VolleyWrapper manager = new VolleyWrapper(GET_CUSTOMERS);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);


                            if (successResponse != null) {

                                try {
                                    JSONObject obj = new JSONObject(successResponse);
                                    JSONObject objResponse = obj
                                            .optJSONObject("response");
                                    String status = objResponse.optString("status");
                                    if (status.equals("Success")) {
                                        JSONArray arrayData = objResponse
                                                .optJSONArray("data");
                                        if (arrayData.length() > 0) {

                                            for (int i = 0; i < arrayData.length(); i++) {
                                                JSONObject objItem = arrayData
                                                        .optJSONObject(i);
                                                CustomerModel model = new CustomerModel();
                                                model.setName(objItem.optString("name"));
                                                model.setPhone(objItem
                                                        .optString("phone"));
                                                model.setRole(objItem.optString("role"));
                                                listCustomers.add(model);
                                            }
                                            CustomerAdapter adapter = new CustomerAdapter(
                                                  mContext, listCustomers);
                                            listViewDealer.setAdapter(adapter);

                                        } else {


                                            CustomToast toast = new CustomToast(mContext);
                                            toast.show(13);
                                        }

                                    } else {


                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(24);
                                    }

                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
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

    @Override
    public void onClick(View v) {
        if (v == mImageBack) {
            finish();
        }
        switch (v.getId()) {

        }
    }



    public class CustomComparator implements Comparator<DealerModel> {
        @Override
        public int compare(DealerModel o1, DealerModel o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}

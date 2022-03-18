package com.vkc.strabo.activity.redeem_report;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.vkc.strabo.R;
import com.vkc.strabo.activity.redeem_report.adapter.RedeemReportsAdapter;
import com.vkc.strabo.activity.redeem_report.model.RedeemReportModel;
import com.vkc.strabo.activity.redeem_report.model.ReportDetailModel;
import com.vkc.strabo.appcontroller.AppController;
import com.vkc.strabo.constants.VKCUrlConstants;
import com.vkc.strabo.manager.AppPrefenceManager;
import com.vkc.strabo.manager.HeaderManager;
import com.vkc.strabo.utils.CustomToast;
import com.vkc.strabo.volleymanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user2 on 9/8/17.
 */
public class RedeemReportActivity extends Activity implements View.OnClickListener, VKCUrlConstants {


    Activity mContext;

    HeaderManager headermanager;
    LinearLayout relativeHeader;
    ImageView mImageBack;
    private ListView listViewRedeemReportList;
    RedeemReportsAdapter adapter;
    private ArrayList<RedeemReportModel> tempRedeemReportList;
    EditText editSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setContentView(R.layout.activity_redeem_report);
        mContext = this;
        initUI();
        getReport();


    }


    private void initUI() {
        tempRedeemReportList = new ArrayList<RedeemReportModel>();
        relativeHeader = (LinearLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(RedeemReportActivity.this, getResources().getString(R.string.redeem_report));
        headermanager.getHeader(relativeHeader, 1);
        mImageBack = headermanager.getLeftButton();
        listViewRedeemReportList = (ListView) findViewById(R.id.listViewRedeemReportList);
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);

        mImageBack.setOnClickListener(this);

        editSearch = (EditText) findViewById(R.id.editSearch);
        editSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

                if (s.length() > 0) {
                    tempRedeemReportList.clear();
                    for (int i = 0; i < AppController.listRedeemReport.size(); i++) {
                        if (AppController.listRedeemReport.get(i).getCustId()
                                .contains(s)
                                || AppController.listRedeemReport.get(i)
                                .getCustName().toLowerCase().contains(s)
                                || AppController.listRedeemReport.get(i)
                                .getCustMobile().contains(s)
                                || AppController.listRedeemReport.get(i)
                                .getCustPlace().toLowerCase().contains(s)) {
                            tempRedeemReportList
                                    .add(AppController.listRedeemReport.get(i));
                        } else {
                            listViewRedeemReportList.setAdapter(null);
                        }
                    }
                    adapter = new RedeemReportsAdapter(mContext,
                            tempRedeemReportList);
                    adapter.notifyDataSetChanged();
                    listViewRedeemReportList.setAdapter(adapter);

                } else {
                    adapter = new RedeemReportsAdapter(mContext,
                            AppController.listRedeemReport);
                    adapter.notifyDataSetChanged();
                    listViewRedeemReportList.setAdapter(adapter);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

    }


    @Override
    public void onClick(View v) {
        if (v == mImageBack) {
            finish();
        }

    }

    public void getReport() {
        try {

            AppController.listRedeemReport.clear();
            String name[] = {"dealerId"};
            String values[] = {AppPrefenceManager.getCustomerId(mContext)};
            final VolleyWrapper manager = new VolleyWrapper(GET_REDEEM_REPORT_APP);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {


                                parseResponse(successResponse);

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


    private void parseResponse(String result) {
        try {
            JSONArray arrayData = null;
            JSONObject jsonObjectresponse = new JSONObject(result);
            JSONObject objResponse = jsonObjectresponse
                    .optJSONObject("response");
            String status = objResponse.getString("status");

            if (status.equals("Success")) {

				/* if (response.has("orders")) { */
                arrayData = objResponse.optJSONArray("data");
                // }

                // int len = arrayOrders.length();
                if (arrayData.length() > 0) {
                    for (int i = 0; i < arrayData.length(); i++) {
                        RedeemReportModel redeemModel = new RedeemReportModel();
                        JSONObject obj = arrayData.optJSONObject(i);
                        redeemModel.setCustId(obj.getString("cust_id"));
                        redeemModel.setCustName(obj.getString("name"));
                        redeemModel.setCustPlace(obj.getString("place"));
                        redeemModel.setCustMobile(obj.getString("phone"));
                        JSONArray objArray = obj.optJSONArray("details");
                        ArrayList<ReportDetailModel> listDetail = new ArrayList<>();
                        for (int j = 0; j < objArray.length(); j++) {
                            ReportDetailModel model = new ReportDetailModel();
                            JSONObject objData = objArray.optJSONObject(j);
                            model.setGift_name(objData.optString("gift_name"));
                            model.setGift_qty(objData.optString("gift_qty"));
                            model.setRwd_points(objData.optString("rwd_points"));
                            model.setTot_coupons(objData
                                    .optString("tot_coupons"));
                            listDetail.add(model);

                        }
                        redeemModel.setListReportDetail(listDetail);
                        AppController.listRedeemReport.add(redeemModel);

                    }

                    RedeemReportsAdapter adapter = new RedeemReportsAdapter(mContext,
                            AppController.listRedeemReport);
                    // adapter.notifyDataSetChanged();
                    listViewRedeemReportList.setAdapter(adapter);

                } else {
                    CustomToast toast = new CustomToast(mContext);
                    toast.show(0);
                }
            } else if (status.equals("scheme_error")) {
                CustomToast toast = new CustomToast(mContext);
                toast.show(68);
            }
        } catch (Exception e) {
            System.out.println("Error" + e);

        }
    }

}
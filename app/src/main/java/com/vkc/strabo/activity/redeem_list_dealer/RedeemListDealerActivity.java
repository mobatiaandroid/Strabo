package com.vkc.strabo.activity.redeem_list_dealer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.vkc.strabo.R;
import com.vkc.strabo.activity.redeem.model.RedeemModel;
import com.vkc.strabo.activity.redeem_list_dealer.adapter.RedeemListAdapter;
import com.vkc.strabo.activity.redeem_list_dealer.model.GiftListModel;
import com.vkc.strabo.activity.redeem_list_dealer.model.GiftUserModel;
import com.vkc.strabo.activity.redeem_report.RedeemReportActivity;
import com.vkc.strabo.constants.VKCUrlConstants;
import com.vkc.strabo.manager.AppPrefenceManager;
import com.vkc.strabo.manager.HeaderManager;
import com.vkc.strabo.utils.CustomToast;
import com.vkc.strabo.volleymanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RedeemListDealerActivity extends Activity implements VKCUrlConstants {
    Activity mContext;
    ArrayList<RedeemModel> listGifts;
    ListView listViewRedeem;
    ImageView btn_left;
    HeaderManager headermanager;
    LinearLayout relativeHeader;
    ImageView mImageBack, mImageConsolidated;
    ExpandableListView listViewHistory;
    // ArrayList<HistoryModel> ;
    ArrayList<GiftListModel> listGift;
    private int lastExpandedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setContentView(R.layout.activity_dealer_redeem_list);
        mContext = this;
        initUI();
    }

    private void initUI() {
        // get the reference of RecyclerView
        listGift = new ArrayList<>();

        mImageBack = (ImageView) findViewById(R.id.btn_left);
        mImageConsolidated = (ImageView) findViewById(R.id.btn_right);

        listViewHistory = (ExpandableListView)
                findViewById(R.id.listViewRedeem);
        listViewHistory
                .setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if (lastExpandedPosition != -1
                                && groupPosition != lastExpandedPosition) {
                            listViewHistory.collapseGroup(lastExpandedPosition);
                        }
                        lastExpandedPosition = groupPosition;
                    }
                });
        mImageConsolidated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RedeemListDealerActivity.this, RedeemReportActivity.class));
            }
        });
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getRedeemList();
    }

    public void getRedeemList() {
        listGift.clear();
        String name[] = {"cust_id"};
        String values[] = {AppPrefenceManager.getCustomerId(mContext)
        };
        final VolleyWrapper manager = new VolleyWrapper(GET_REDEEM_LIST);
        manager.getResponsePOST(mContext, 11, name, values,
                new VolleyWrapper.ResponseListener() {

                    @Override
                    public void responseSuccess(String successResponse) {
                        // TODO Auto-generated method stub
                        //   Log.v("LOG", "18022015 success" + successResponse);
                        parseResponse(successResponse);
                    }

                    @Override
                    public void responseFailure(String failureResponse) {
                        // TODO Auto-generated method stub
                        // Log.v("LOG", "18022015 Errror" + failureResponse);
                    }
                });
    }

    @SuppressLint("NewApi")
    public void parseResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject objResponse = jsonObject.optJSONObject("response");
            String status = objResponse.optString("status");

            if (status.equals("Success")) {

                JSONArray arrayData = objResponse.optJSONArray("data");
                if (arrayData.length() > 0) {

                    for (int i = 0; i < arrayData.length(); i++) {

                        GiftListModel model = new GiftListModel();
                        JSONObject obj = arrayData.optJSONObject(i);
                        JSONArray arrayDetail = obj.optJSONArray("details");
                        /*
                         * JSONArray arrayDetail = new JSONArray(
						 * obj.getString("details"));
						 */
                        System.out.println("Detail Array " + arrayDetail);
                        model.setName(obj.getString("name"));
                        model.setPhone(obj.getString("phone"));
                        ArrayList<GiftUserModel> listHist = new ArrayList<>();
                        for (int j = 0; j < arrayDetail.length(); j++) {
                            JSONObject obj1 = arrayDetail.optJSONObject(j);
                            GiftUserModel model1 = new GiftUserModel();
                            model1.setGift_title(obj1.getString("gift_title"));
                            model1.setGift_image(obj1.getString("gift_image"));
                            model1.setGift_type(obj1.getString("gift_type"));
                            model1.setQuantity(obj1.getString("quantity"));
                            // System.out.println("Date Value "+model1.getDateValue());
                            listHist.add(model1);
                        }

                        // System.out.println("Parsed " +
                        // listHist.get(i).getDateValue());
                        model.setListGiftUser(listHist);

                        listGift.add(model);
                        // System.out.println("List History "+listHistory.get(0).getListHistory().get(1).getDateValue());
                    }
                    /*
                     * HistoryAdapter adapter = new
					 * HistoryAdapter(getActivity(), listHistory);
					 * listViewHistory.setAdapter(adapter);
					 */
                    RedeemListAdapter adapter = new RedeemListAdapter(
                            mContext, listGift);
                    listViewHistory.setAdapter(adapter);
                } else {

                    CustomToast toast = new CustomToast(mContext);
                    toast.show(13);
                }

            } else {
                CustomToast toast = new CustomToast(mContext);
                toast.show(24);
            }

        } catch (Exception e) {

        }
    }


}


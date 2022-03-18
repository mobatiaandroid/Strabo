package com.vkc.strabo.activity.inbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.vkc.strabo.R;
import com.vkc.strabo.activity.inbox.adapter.InboxAdapter;
import com.vkc.strabo.activity.inbox.model.InboxModel;
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
 * Created by user2 on 4/12/17.
 */
public class InboxActivity extends AppCompatActivity implements View.OnClickListener, VKCUrlConstants {


    Activity mContext;
    ArrayList<InboxModel> listNotification;
    ListView listViewInbox;
    HeaderManager headermanager;
    LinearLayout relativeHeader;
    ImageView mImageBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        setContentView(R.layout.activity_inbox);
        mContext = this;
        initUI();

    }


    private void initUI() {
        listNotification = new ArrayList<>();
        relativeHeader = (LinearLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(InboxActivity.this, getResources().getString(R.string.inbox));
        headermanager.getHeader(relativeHeader, 1);
        mImageBack = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        listViewInbox = (ListView) findViewById(R.id.listViewInbox);

        mImageBack.setOnClickListener(this);
        getInbox();

        listViewInbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(InboxActivity.this, InboxDetailsActivity.class);
               /* intent.putExtra("title",listNotification.get(position).getTitle());
                intent.putExtra("message",listNotification.get(position).getMessage());
                intent.putExtra("created_on",listNotification.get(position).getCreatedon());
                intent.putExtra("image",listNotification.get(position).getImage());
                intent.putExtra("date_from",listNotification.get(position).getFrom_date());
                intent.putExtra("date_to",listNotification.get(position).getTo_date());*/
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onClick(View v) {
        if (v == mImageBack) {
            finish();
        }
    }

    public void getInbox() {


        AppController.listNotification.clear();
        try {
            String[] name = {"cust_id", "role"};
            String[] values = {AppPrefenceManager.getCustomerId(mContext),
                    AppPrefenceManager.getUserType(mContext)};

            final VolleyWrapper manager = new VolleyWrapper(GET_NOTIFICATIONS);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            //    System.out.println("Response---Login" + successResponse);
                            if (successResponse != null) {

                                try {
                                    JSONObject jsonObject = new JSONObject(successResponse);
                                    JSONObject objResponse = jsonObject.optJSONObject("response");
                                    String status = objResponse.optString("status");


                                    if (status.equals("Success")) {

                                        JSONArray arrayData = objResponse.optJSONArray("data");
                                        if (arrayData.length() > 0) {

                                            for (int i = 0; i < arrayData.length(); i++) {
                                                JSONObject obj = arrayData.getJSONObject(i);
                                                InboxModel model = new InboxModel();
                                                model.setTitle(obj.optString("title"));
                                                model.setMessage(obj.optString("message"));
                                                model.setImage(obj.optString("image"));
                                                model.setCreatedon(obj.optString("createdon"));
                                                model.setFrom_date(obj.optString("from_date"));
                                                model.setTo_date(obj.optString("to_date"));
                                                AppController.listNotification.add(model);
                                            }

                                            InboxAdapter adapter = new InboxAdapter(
                                                    mContext, AppController.listNotification);
                                            listViewInbox.setAdapter(adapter);

                                        } else {
                                            CustomToast toast = new CustomToast(mContext);
                                            toast.show(50);
                                        }

                                    } else {

                                        CustomToast toast = new CustomToast(mContext);
                                        toast.show(24);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        getInbox();
    }
}
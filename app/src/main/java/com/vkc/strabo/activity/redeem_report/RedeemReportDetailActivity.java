package com.vkc.strabo.activity.redeem_report;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.vkc.strabo.R;
import com.vkc.strabo.activity.redeem_report.adapter.ReportDetailAdapter;
import com.vkc.strabo.appcontroller.AppController;
import com.vkc.strabo.constants.VKCUrlConstants;
import com.vkc.strabo.manager.HeaderManager;

public class RedeemReportDetailActivity extends AppCompatActivity implements
        OnClickListener, VKCUrlConstants {


    ListView listViewRedeemReportDetail;
    int position;
    String cust_id;
    Activity mContext;

    HeaderManager headermanager;
    LinearLayout relativeHeader;
    ImageView mImageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_report_detail);
        mContext = this;
        initialiseUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // title/icon
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

    private void initialiseUI() {

        relativeHeader = (LinearLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(RedeemReportDetailActivity.this, getResources().getString(R.string.redeem_report_detail));
        headermanager.getHeader(relativeHeader, 1);
        mImageBack = headermanager.getLeftButton();
        listViewRedeemReportDetail = (ListView) findViewById(R.id.listViewRedeemReportDetail);
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);

        mImageBack.setOnClickListener(this);
        Intent intent = getIntent();
        //position = Integer.parseInt(intent.getExtras().getString("position"));
        cust_id = intent.getExtras().getString("cust_id");
        for (int i = 0; i < AppController.listRedeemReport.size(); i++) {
            if (AppController.listRedeemReport.get(i).getCustId()
                    .equals(cust_id)) {
                position = i;
                break;
            }
        }
        ReportDetailAdapter adapter = new ReportDetailAdapter(mContext,
                AppController.listRedeemReport.get(position)
                        .getListReportDetail());
        listViewRedeemReportDetail.setAdapter(adapter);
    }

    public void setActionBar() {
        // Enable action bar icon_luncher as toggle Home Button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("");
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        actionBar.show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        super.onResume();

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();

    }

    @Override
    public void onClick(View v) {
        if (v == mImageBack) {
            finish();
        }
    }

}

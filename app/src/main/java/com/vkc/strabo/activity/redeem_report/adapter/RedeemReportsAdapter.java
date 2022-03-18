package com.vkc.strabo.activity.redeem_report.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.vkc.strabo.R;
import com.vkc.strabo.activity.redeem_report.RedeemReportDetailActivity;
import com.vkc.strabo.activity.redeem_report.model.RedeemReportModel;

import java.util.ArrayList;

public class RedeemReportsAdapter extends BaseAdapter {
	Activity mActivity;

	LayoutInflater mLayoutInflater;
	ArrayList<RedeemReportModel> listModel;

	public RedeemReportsAdapter(Activity mActivity,
                                ArrayList<RedeemReportModel> listModel) {
	
		this.mActivity = mActivity;
		this.listModel=listModel;
		//this.notifyDataSetChanged();
		System.out.println("Length" + listModel.size());
		// mLayoutInflater = LayoutInflater.from(mActivity);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listModel.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	static class ViewHolder {
		TextView textCustId;
		TextView textCustName;
		TextView textCustPlace;
		TextView textCustNobile;
		TextView textView;

	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View view, ViewGroup parent) {

		ViewHolder viewHolder = null;
		View v = view;
		
		if (view == null) {
			
			LayoutInflater inflater = (LayoutInflater) mActivity
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			v = inflater.inflate(R.layout.item_redeem_report_list, null);
			viewHolder = new ViewHolder();
			viewHolder.textCustId = (TextView) v
					.findViewById(R.id.txtViewCustId);
			viewHolder.textCustName = (TextView) v.findViewById(R.id.txtViewCustName);
			viewHolder.textCustPlace = (TextView) v
					.findViewById(R.id.txtViewPlace);
			viewHolder.textCustNobile = (TextView) v
					.findViewById(R.id.txtViewMobile);
			viewHolder.textView = (TextView) v
					.findViewById(R.id.txtViewDetail);
			v.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) v.getTag();

		}

		if (position % 2 == 1) {
			// view.setBackgroundColor(Color.BLUE);
			v.setBackgroundColor(mActivity.getResources().getColor(
					R.color.list_row_color_grey));
		} else {
			v.setBackgroundColor(mActivity.getResources().getColor(
					R.color.list_row_color_white));
		}
		
		viewHolder.textCustId.setText(listModel.get(position).getCustId());
		viewHolder.textCustName.setText(listModel.get(position).getCustName());
		viewHolder.textCustPlace.setText(listModel.get(position).getCustPlace());
		viewHolder.textCustNobile.setText(listModel.get(position).getCustMobile());
		viewHolder.textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(mActivity,RedeemReportDetailActivity.class);
				intent.putExtra("position", String.valueOf(position));
				intent.putExtra("cust_id", listModel.get(position).getCustId());
				mActivity.startActivity(intent);
			}
		});
		
/*
			viewHolder.textDate.setText(orderList.getOrderDate());
			viewHolder.textName.setText(orderList.getName());
			System.out.println("Adapter Order Id"
					+ listModel.get(position).getOrderid());
			viewHolder.textAddress.setText(orderList.getTotalqty());
			viewHolder.textPhone.setText(status);
		*/

		return v;
	}

}

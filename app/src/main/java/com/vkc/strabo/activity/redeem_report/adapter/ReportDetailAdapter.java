package com.vkc.strabo.activity.redeem_report.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vkc.strabo.R;
import com.vkc.strabo.activity.redeem_report.model.ReportDetailModel;

import java.util.ArrayList;


public class ReportDetailAdapter extends BaseAdapter {
	Activity mActivity;

	LayoutInflater mLayoutInflater;
	ArrayList<ReportDetailModel> listModel;

	public ReportDetailAdapter(Activity mActivity,
                               ArrayList<ReportDetailModel> listModel) {

		this.mActivity = mActivity;
		this.listModel = listModel;
		// this.notifyDataSetChanged();
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
		TextView textGiftName;
		TextView textGiftQty;
		TextView textRewardPoints;
		TextView textTotalCoupons;

	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View view, ViewGroup parent) {

		ViewHolder viewHolder = null;
		View v = view;

		if (view == null) {

			LayoutInflater inflater = (LayoutInflater) mActivity
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			v = inflater.inflate(R.layout.item_report_detail, null);
			viewHolder = new ViewHolder();
			viewHolder.textGiftName = (TextView) v
					.findViewById(R.id.txtViewGift);
			viewHolder.textGiftQty = (TextView) v
					.findViewById(R.id.txtViewGiftQty);
			viewHolder.textRewardPoints = (TextView) v
					.findViewById(R.id.txtViewRedeemPoints);
			viewHolder.textTotalCoupons = (TextView) v
					.findViewById(R.id.txtViewTotalCoupons);

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

		viewHolder.textGiftName.setText(listModel.get(position).getGift_name());
		viewHolder.textGiftQty.setText(listModel.get(position).getGift_qty());
		viewHolder.textRewardPoints
				.setText(listModel.get(position).getRwd_points());
		viewHolder.textTotalCoupons.setText(listModel.get(position)
				.getTot_coupons());
	

	
		return v;
	}

}

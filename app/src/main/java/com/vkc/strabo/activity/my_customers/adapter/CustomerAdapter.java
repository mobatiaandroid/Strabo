package com.vkc.strabo.activity.my_customers.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.vkc.strabo.R;
import com.vkc.strabo.activity.my_customers.model.CustomerModel;

import java.util.ArrayList;

public class CustomerAdapter extends BaseAdapter {
	Activity mActivity;

	LayoutInflater mLayoutInflater;
	ArrayList<CustomerModel> listModel;

	public CustomerAdapter(Activity mActivity,
                           ArrayList<CustomerModel> listModel) {

		this.mActivity = mActivity;
		this.listModel = listModel;
		
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
		
		TextView textName;
		TextView textRole;
		TextView textPhone;

	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View view, ViewGroup parent) {

		ViewHolder viewHolder = null;
		View v = view;

		if (view == null) {

			LayoutInflater inflater = (LayoutInflater) mActivity
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			v = inflater.inflate(R.layout.item_cust_list, null);
			
			viewHolder = new ViewHolder();
			viewHolder.textName = (TextView) v
					.findViewById(R.id.textName);
			viewHolder.textPhone = (TextView) v.findViewById(R.id.textPhone);
			viewHolder.textRole = (TextView) v
					.findViewById(R.id.textRole);

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

	
	

			viewHolder.textName.setText(listModel.get(position).getName());
			viewHolder.textPhone.setText(listModel.get(position).getPhone());
			viewHolder.textRole.setText(listModel.get(position).getRole());
			
		

		return v;
	}

}


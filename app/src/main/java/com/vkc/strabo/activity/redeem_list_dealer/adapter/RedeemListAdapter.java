package com.vkc.strabo.activity.redeem_list_dealer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;


import com.vkc.strabo.R;
import com.vkc.strabo.activity.redeem_list_dealer.model.GiftListModel;
import com.vkc.strabo.activity.redeem_list_dealer.model.GiftUserModel;
import com.vkc.strabo.constants.VKCUrlConstants;

import java.util.ArrayList;

public class RedeemListAdapter extends BaseExpandableListAdapter implements
		VKCUrlConstants

{
	ArrayList<GiftListModel> listGift;
	ArrayList<GiftUserModel> giftUserList;
	Activity mContext;
	int positionValue;

	public RedeemListAdapter(Activity mContext,
                             ArrayList<GiftListModel> listTransaction) {
		this.listGift = listTransaction;
		this.mContext = mContext;
	}

	@Override
	public int getGroupCount() {
		return listGift.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {

		giftUserList = listGift.get(groupPosition).getListGiftUser();
		// return listTransaction.get(positionValue).getListHistory().size();
		return giftUserList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return giftUserList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return giftUserList.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.item_history_parent,
					null);
		}

		TextView textUser = (TextView) convertView.findViewById(R.id.textUser);
		TextView textPoints = (TextView) convertView
				.findViewById(R.id.textPoint);
		TextView textIcon = (TextView) convertView.findViewById(R.id.textIcon);
		textPoints.setText("Mobile :" + listGift.get(groupPosition).getPhone());
		textUser.setText(listGift.get(groupPosition).getName());
		positionValue = groupPosition;
		if (isExpanded) {
			textIcon.setText("-");
		} else {
			textIcon.setText("+");
		}
		return convertView;
	}

	static class ViewHolder {
		TextView textGiftType;
		TextView textGiftQuantity;
		TextView textGiftName;

		// ImageView imageGift;

	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
		/*
		 * System.out.println("Group Position:"+groupPosition+"Child Psoitiomn:"+
		 * childPosition);
		 * System.out.println("Count:"+listTransaction.get(groupPosition
		 * ).getListHistory().size());
		 */

		ViewHolder viewHolder = null;
		View v = convertView;

		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			v = inflater.inflate(R.layout.item_gift, null);
			viewHolder = new ViewHolder();
			viewHolder.textGiftName = (TextView) v.findViewById(R.id.textGiftName);
			viewHolder.textGiftType= (TextView) v.findViewById(R.id.textGiftType);
			viewHolder.textGiftQuantity= (TextView) v.findViewById(R.id.textGiftQuantity);
			v.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}
	
		// viewHolder.imageGift = (ImageView) v.findViewById(R.id.imageGift);

		// childPosition=childPosition-1;
		if (childPosition % 2 == 1) {
			// view.setBackgroundColor(Color.BLUE);
			v.setBackgroundColor(mContext.getResources().getColor(
					R.color.list_row_color_grey));
		} else {
			v.setBackgroundColor(mContext.getResources().getColor(
					R.color.list_row_color_white));
		}
		// String date=productList.get(childPosition).getDateValue();

		viewHolder.textGiftName.setText(giftUserList.get(childPosition)
				.getGift_title());
		viewHolder.textGiftType.setText(giftUserList.get(childPosition)
				.getGift_type());
		viewHolder.textGiftQuantity.setText(giftUserList.get(childPosition)
				.getQuantity());
		/*
		 * if (!giftUserList.get(childPosition).getGift_image().equals("")) {
		 * Picasso.with(mContext)
		 * .load(giftUserList.get(childPosition).getGift_image())
		 * .placeholder(R.drawable.gift).into(viewHolder.imageGift); } else {
		 * 
		 * }
		 */
		// System.out.println("Position Value:"+childPosition);

		return v;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
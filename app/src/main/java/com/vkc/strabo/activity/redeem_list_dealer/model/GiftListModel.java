package com.vkc.strabo.activity.redeem_list_dealer.model;

import java.util.ArrayList;

public class GiftListModel {
	private String name;
	private String phone;
	private ArrayList<GiftUserModel> listGiftUser;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public ArrayList<GiftUserModel> getListGiftUser() {
		return listGiftUser;
	}
	public void setListGiftUser(ArrayList<GiftUserModel> listGiftUser) {
		this.listGiftUser = listGiftUser;
	}
	
	

}

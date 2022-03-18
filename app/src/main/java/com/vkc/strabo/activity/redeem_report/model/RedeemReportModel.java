package com.vkc.strabo.activity.redeem_report.model;

import java.util.ArrayList;

public class RedeemReportModel {
private String custId;
private String custName;
private String custMobile;
private String custPlace;
public ArrayList<ReportDetailModel> listReportDetail;
public String getCustId() {
	return custId;
}
public void setCustId(String custId) {
	this.custId = custId;
}
public String getCustName() {
	return custName;
}
public void setCustName(String custName) {
	this.custName = custName;
}
public String getCustMobile() {
	return custMobile;
}
public void setCustMobile(String custMobile) {
	this.custMobile = custMobile;
}
public String getCustPlace() {
	return custPlace;
}
public void setCustPlace(String custPlace) {
	this.custPlace = custPlace;
}
public ArrayList<ReportDetailModel> getListReportDetail() {
	return listReportDetail;
}
public void setListReportDetail(ArrayList<ReportDetailModel> listReportDetail) {
	this.listReportDetail = listReportDetail;
}


}

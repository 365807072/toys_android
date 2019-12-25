package com.yyqq.commen.model;

public class ToysLeasePayListBean {

	private String toys_id = "";
	private String img_url = "";
	private String toys_title = "";
	private String toys_monery = "";

	public String getToys_id() {
		return toys_id;
	}

	public void setToys_id(String toys_id) {
		this.toys_id = toys_id;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public String getToys_title() {
		return toys_title;
	}

	public void setToys_title(String toys_title) {
		this.toys_title = toys_title;
	}

	public String getToys_monery() {
		return toys_monery;
	}

	public void setToys_monery(String toys_monery) {
		this.toys_monery = toys_monery;
	}

	@Override
	public String toString() {
		return "ToysLeasePayListBean [toys_id=" + toys_id + ", img_url="
				+ img_url + ", toys_title=" + toys_title + ", toys_monery="
				+ toys_monery + "]";
	}

}

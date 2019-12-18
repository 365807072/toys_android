package com.yyqq.commen.model;

public class ToysMainClassItemBean {
	private String business_id = "";
	private String business_title = "";
	private String sell_price = "";
	private String business_pic = "";
	private String source = "";
	private String web_link = "";
	private String unit_name = "";

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public String getBusiness_title() {
		return business_title;
	}

	public void setBusiness_title(String business_title) {
		this.business_title = business_title;
	}

	public String getSell_price() {
		return sell_price;
	}

	public void setSell_price(String sell_price) {
		this.sell_price = sell_price;
	}

	public String getBusiness_pic() {
		return business_pic;
	}

	public void setBusiness_pic(String business_pic) {
		this.business_pic = business_pic;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getWeb_link() {
		return web_link;
	}

	public void setWeb_link(String web_link) {
		this.web_link = web_link;
	}

	public String getUnit_name() {
		return unit_name;
	}

	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}

	@Override
	public String toString() {
		return "ToysMainClassItemBean [business_id=" + business_id
				+ ", business_title=" + business_title + ", sell_price="
				+ sell_price + ", business_pic=" + business_pic + ", source="
				+ source + ", web_link=" + web_link + ", unit_name="
				+ unit_name + "]";
	}

}
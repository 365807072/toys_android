package com.yyqq.commen.model;

public class MycardBean {

	private String business_pic = "";
	private String card_info = "";
	private String status_name = "";
	private String card_id_title = "";
	private String order_id = "";

	public MycardBean() {
	}

	public MycardBean(String business_pic, String card_info,
			String status_name, String card_id_title, String order_id) {
		super();
		this.business_pic = business_pic;
		this.card_info = card_info;
		this.status_name = status_name;
		this.card_id_title = card_id_title;
		this.order_id = order_id;
	}

	public String getBusiness_pic() {
		return business_pic;
	}

	public void setBusiness_pic(String business_pic) {
		this.business_pic = business_pic;
	}

	public String getCard_info() {
		return card_info;
	}

	public void setCard_info(String card_info) {
		this.card_info = card_info;
	}

	public String getStatus_name() {
		return status_name;
	}

	public void setStatus_name(String status_name) {
		this.status_name = status_name;
	}

	public String getCard_id_title() {
		return card_id_title;
	}

	public void setCard_id_title(String card_id_title) {
		this.card_id_title = card_id_title;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	@Override
	public String toString() {
		return "MycardBean [business_pic=" + business_pic + ", card_info="
				+ card_info + ", status_name=" + status_name
				+ ", card_id_title=" + card_id_title + ", order_id=" + order_id
				+ "]";
	}

}

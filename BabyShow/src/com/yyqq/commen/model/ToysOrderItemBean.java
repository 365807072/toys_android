package com.yyqq.commen.model;

public class ToysOrderItemBean {

	private String business_id = "";
	private String business_title = "";
	private String img_thumb = "";
	private String sell_price = "";
	private String status = "";
	private String status_name = "";
	private String order_id = "";
	private String is_reset = "";
	private String order_status = "";
	private String is_del = "";
	private String is_jump = "";
	private String post_url = "";
	private String category_icon_url = "";
	private String is_prize_status = "";
	
	public ToysOrderItemBean() {
	}

	public String getIs_prize_status() {
		return is_prize_status;
	}

	public void setIs_prize_status(String is_prize_status) {
		this.is_prize_status = is_prize_status;
	}

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

	public String getImg_thumb() {
		return img_thumb;
	}

	public void setImg_thumb(String img_thumb) {
		this.img_thumb = img_thumb;
	}

	public String getSell_price() {
		return sell_price;
	}

	public void setSell_price(String sell_price) {
		this.sell_price = sell_price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus_name() {
		return status_name;
	}

	public void setStatus_name(String status_name) {
		this.status_name = status_name;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getIs_reset() {
		return is_reset;
	}

	public void setIs_reset(String is_reset) {
		this.is_reset = is_reset;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getIs_del() {
		return is_del;
	}

	public void setIs_del(String is_del) {
		this.is_del = is_del;
	}

	public String getIs_jump() {
		return is_jump;
	}

	public void setIs_jump(String is_jump) {
		this.is_jump = is_jump;
	}

	public String getPost_url() {
		return post_url;
	}

	public void setPost_url(String post_url) {
		this.post_url = post_url;
	}

	public String getCategory_icon_url() {
		return category_icon_url;
	}

	public void setCategory_icon_url(String category_icon_url) {
		this.category_icon_url = category_icon_url;
	}

	@Override
	public String toString() {
		return "ToysOrderItemBean [business_id=" + business_id
				+ ", business_title=" + business_title + ", img_thumb="
				+ img_thumb + ", sell_price=" + sell_price + ", status="
				+ status + ", status_name=" + status_name + ", order_id="
				+ order_id + ", is_reset=" + is_reset + ", order_status="
				+ order_status + ", is_del=" + is_del + ", is_jump=" + is_jump
				+ ", post_url=" + post_url + ", category_icon_url="
				+ category_icon_url + ", is_prize_status=" + is_prize_status
				+ "]";
	}

}

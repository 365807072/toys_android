package com.yyqq.commen.model;

/**
 * 购物车
 * */
public class ToysLeaseCartItemBean {

	private String cart_id = "";
	private String business_id = "";
	private String business_title = "";
	private String every_price = "";
	private String img_thumb = "";
	private String check_state = "";
	private String is_order = "";
	private String order_id = "";
	private String description = "";
	private String small_img_thumb = "";
	private String close_order_des = "";
	private boolean isDelete = false;

	public ToysLeaseCartItemBean() {
		super();
	}

	public ToysLeaseCartItemBean(String cart_id, String business_id,
			String business_title, String every_price, String img_thumb,
			String check_state, String is_order, String order_id,
			String description, String small_img_thumb, String close_order_des,
			boolean isDelete) {
		super();
		this.cart_id = cart_id;
		this.business_id = business_id;
		this.business_title = business_title;
		this.every_price = every_price;
		this.img_thumb = img_thumb;
		this.check_state = check_state;
		this.is_order = is_order;
		this.order_id = order_id;
		this.description = description;
		this.small_img_thumb = small_img_thumb;
		this.close_order_des = close_order_des;
		this.isDelete = isDelete;
	}

	public String getCart_id() {
		return cart_id;
	}

	public void setCart_id(String cart_id) {
		this.cart_id = cart_id;
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

	public String getEvery_price() {
		return every_price;
	}

	public void setEvery_price(String every_price) {
		this.every_price = every_price;
	}

	public String getImg_thumb() {
		return img_thumb;
	}

	public void setImg_thumb(String img_thumb) {
		this.img_thumb = img_thumb;
	}

	public String getCheck_state() {
		return check_state;
	}

	public void setCheck_state(String check_state) {
		this.check_state = check_state;
	}

	public String getIs_order() {
		return is_order;
	}

	public void setIs_order(String is_order) {
		this.is_order = is_order;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSmall_img_thumb() {
		return small_img_thumb;
	}

	public void setSmall_img_thumb(String small_img_thumb) {
		this.small_img_thumb = small_img_thumb;
	}

	public String getClose_order_des() {
		return close_order_des;
	}

	public void setClose_order_des(String close_order_des) {
		this.close_order_des = close_order_des;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	@Override
	public String toString() {
		return "ToysLeaseCartItemBean [cart_id=" + cart_id + ", business_id="
				+ business_id + ", business_title=" + business_title
				+ ", every_price=" + every_price + ", img_thumb=" + img_thumb
				+ ", check_state=" + check_state + ", is_order=" + is_order
				+ ", order_id=" + order_id + ", description=" + description
				+ ", small_img_thumb=" + small_img_thumb + ", close_order_des="
				+ close_order_des + ", isDelete=" + isDelete + "]";
	}

}

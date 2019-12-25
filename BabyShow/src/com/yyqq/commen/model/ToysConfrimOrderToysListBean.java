package com.yyqq.commen.model;

/**
 * 玩具世界，确认订单，玩具列表bean
 * 
 * */
public class ToysConfrimOrderToysListBean {

	private String order_id = "";
	private String business_id = "";
	private String img_thumb = "";
	private String business_title = "";
	private String market_price = "";
	private String every_sell_price = "";
	private String member_price = "";
	private String category_icon_url = "";

	public ToysConfrimOrderToysListBean() {
		super();
	}

	public ToysConfrimOrderToysListBean(String order_id, String business_id,
			String img_thumb, String business_title, String market_price,
			String every_sell_price, String member_price,
			String category_icon_url) {
		super();
		this.order_id = order_id;
		this.business_id = business_id;
		this.img_thumb = img_thumb;
		this.business_title = business_title;
		this.market_price = market_price;
		this.every_sell_price = every_sell_price;
		this.member_price = member_price;
		this.category_icon_url = category_icon_url;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public String getImg_thumb() {
		return img_thumb;
	}

	public void setImg_thumb(String img_thumb) {
		this.img_thumb = img_thumb;
	}

	public String getBusiness_title() {
		return business_title;
	}

	public void setBusiness_title(String business_title) {
		this.business_title = business_title;
	}

	public String getMarket_price() {
		return market_price;
	}

	public void setMarket_price(String market_price) {
		this.market_price = market_price;
	}

	public String getEvery_sell_price() {
		return every_sell_price;
	}

	public void setEvery_sell_price(String every_sell_price) {
		this.every_sell_price = every_sell_price;
	}

	public String getMember_price() {
		return member_price;
	}

	public void setMember_price(String member_price) {
		this.member_price = member_price;
	}

	public String getCategory_icon_url() {
		return category_icon_url;
	}

	public void setCategory_icon_url(String category_icon_url) {
		this.category_icon_url = category_icon_url;
	}

	@Override
	public String toString() {
		return "ToysConfrimOrderToysListBean [order_id=" + order_id
				+ ", business_id=" + business_id + ", img_thumb=" + img_thumb
				+ ", business_title=" + business_title + ", market_price="
				+ market_price + ", every_sell_price=" + every_sell_price
				+ ", member_price=" + member_price + ", category_icon_url="
				+ category_icon_url + "]";
	}

}

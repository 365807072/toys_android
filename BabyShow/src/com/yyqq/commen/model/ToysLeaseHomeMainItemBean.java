package com.yyqq.commen.model;

public class ToysLeaseHomeMainItemBean {

	private String business_id = "";
	private String business_title = "";
	private String sell_price = "";
	private String market_price = "";
	private String img_thumb = "";
	private String img_thumb_width = "";
	private String img_thumb_height = "";
	private String type = "";
	private String style = "";
	private String category_id = "";
	private String is_link = "";
	private String post_url = "";
	private String icon_url = "";

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

	public String getMarket_price() {
		return market_price;
	}

	public void setMarket_price(String market_price) {
		this.market_price = market_price;
	}

	public String getImg_thumb() {
		return img_thumb;
	}

	public void setImg_thumb(String img_thumb) {
		this.img_thumb = img_thumb;
	}

	public String getImg_thumb_width() {
		return img_thumb_width;
	}

	public void setImg_thumb_width(String img_thumb_width) {
		this.img_thumb_width = img_thumb_width;
	}

	public String getImg_thumb_height() {
		return img_thumb_height;
	}

	public void setImg_thumb_height(String img_thumb_height) {
		this.img_thumb_height = img_thumb_height;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getIs_link() {
		return is_link;
	}

	public void setIs_link(String is_link) {
		this.is_link = is_link;
	}

	public String getPost_url() {
		return post_url;
	}

	public void setPost_url(String post_url) {
		this.post_url = post_url;
	}

	public String getIcon_url() {
		return icon_url;
	}

	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}

	public ToysLeaseHomeMainItemBean() {
	}

	public ToysLeaseHomeMainItemBean(String business_id, String business_title,
			String sell_price, String market_price, String img_thumb,
			String img_thumb_width, String img_thumb_height, String type,
			String style, String category_id, String is_link, String post_url,
			String icon_url) {
		super();
		this.business_id = business_id;
		this.business_title = business_title;
		this.sell_price = sell_price;
		this.market_price = market_price;
		this.img_thumb = img_thumb;
		this.img_thumb_width = img_thumb_width;
		this.img_thumb_height = img_thumb_height;
		this.type = type;
		this.style = style;
		this.category_id = category_id;
		this.is_link = is_link;
		this.post_url = post_url;
		this.icon_url = icon_url;
	}

	@Override
	public String toString() {
		return "ToysLeaseHomeMainItemBean [business_id=" + business_id
				+ ", business_title=" + business_title + ", sell_price="
				+ sell_price + ", market_price=" + market_price
				+ ", img_thumb=" + img_thumb + ", img_thumb_width="
				+ img_thumb_width + ", img_thumb_height=" + img_thumb_height
				+ ", type=" + type + ", style=" + style + ", category_id="
				+ category_id + ", is_link=" + is_link + ", post_url="
				+ post_url + ", icon_url=" + icon_url + "]";
	}

}
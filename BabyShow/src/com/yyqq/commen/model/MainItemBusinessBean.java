package com.yyqq.commen.model;

/**
 * 首页商品列表bean
 * */
public class MainItemBusinessBean {

	private String id = "";
	private String shopsName = "";
	private String shopsDiscountPrice = "";
	private String shopsPrice = "";
	private String shopsImage1 = "";
	private String shopsNumber = "";
	private String shopsType = "";
	private String cartNumber = "";

	public MainItemBusinessBean() {
	}

	public MainItemBusinessBean(String id, String shopsName,
			String shopsDiscountPrice, String shopsPrice, String shopsImage1,
			String shopsNumber, String shopsType, String cartNumber) {
		super();
		this.id = id;
		this.shopsName = shopsName;
		this.shopsDiscountPrice = shopsDiscountPrice;
		this.shopsPrice = shopsPrice;
		this.shopsImage1 = shopsImage1;
		this.shopsNumber = shopsNumber;
		this.shopsType = shopsType;
		this.cartNumber = cartNumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShopsName() {
		return shopsName;
	}

	public void setShopsName(String shopsName) {
		this.shopsName = shopsName;
	}

	public String getShopsDiscountPrice() {
		return shopsDiscountPrice;
	}

	public void setShopsDiscountPrice(String shopsDiscountPrice) {
		this.shopsDiscountPrice = shopsDiscountPrice;
	}

	public String getShopsPrice() {
		return shopsPrice;
	}

	public void setShopsPrice(String shopsPrice) {
		this.shopsPrice = shopsPrice;
	}

	public String getShopsImage1() {
		return shopsImage1;
	}

	public void setShopsImage1(String shopsImage1) {
		this.shopsImage1 = shopsImage1;
	}

	public String getShopsNumber() {
		return shopsNumber;
	}

	public void setShopsNumber(String shopsNumber) {
		this.shopsNumber = shopsNumber;
	}

	public String getShopsType() {
		return shopsType;
	}

	public void setShopsType(String shopsType) {
		this.shopsType = shopsType;
	}

	public String getCartNumber() {
		return cartNumber;
	}

	public void setCartNumber(String cartNumber) {
		this.cartNumber = cartNumber;
	}

	@Override
	public String toString() {
		return "MainItemBusinessBean [id=" + id + ", shopsName=" + shopsName
				+ ", shopsDiscountPrice=" + shopsDiscountPrice
				+ ", shopsPrice=" + shopsPrice + ", shopsImage1=" + shopsImage1
				+ ", shopsNumber=" + shopsNumber + ", shopsType=" + shopsType
				+ ", cartNumber=" + cartNumber + "]";
	}

}

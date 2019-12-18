package com.yyqq.commen.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.yyqq.commen.utils.ShoppingCartUtils;

public class ToysDetailBean02 {

	button_info buttonInfo = new button_info();
	ToysDetial toysDetial = new ToysDetial();

	public button_info getButtonInfo() {
		return buttonInfo;
	}

	public void setButtonInfo(button_info buttonInfo) {
		this.buttonInfo = buttonInfo;
	}

	public ToysDetial getToysDetial() {
		return toysDetial;
	}

	public void setToysDetial(ToysDetial toysDetial) {
		this.toysDetial = toysDetial;
	}

	/**
	 * 详情
	 * */
	public class ToysDetial {
		String business_id = "";
		String img_thumb = "";
		String order_status = "";
		String is_cart_number = "";
		String cart_number = "";
		String share_title = "";
		String share_des = "";
		String post_url = "";
		String wxPath = "";
		String spc_status = "";
		
		public String getSpc_status() {
			return spc_status;
		}

		public void setSpc_status(String spc_status) {
			this.spc_status = spc_status;
		}

		public String getWxPath() {
			return wxPath;
		}

		public void setWxPath(String wxPath) {
			this.wxPath = wxPath;
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

		public String getOrder_status() {
			return order_status;
		}

		public void setOrder_status(String order_status) {
			this.order_status = order_status;
		}

		public String getIs_cart_number() {
			return is_cart_number;
		}

		public void setIs_cart_number(String is_cart_number) {
			this.is_cart_number = is_cart_number;
		}

		public String getCart_number() {
			return cart_number;
		}

		public void setCart_number(String cart_number) {
			this.cart_number = cart_number;
		}

		public String getShare_title() {
			return share_title;
		}

		public void setShare_title(String share_title) {
			this.share_title = share_title;
		}

		public String getShare_des() {
			return share_des;
		}

		public void setShare_des(String share_des) {
			this.share_des = share_des;
		}

		public String getPost_url() {
			return post_url;
		}

		public void setPost_url(String post_url) {
			this.post_url = post_url;
		}

		@Override
		public String toString() {
			return "ToysDetial [business_id=" + business_id + ", img_thumb="
					+ img_thumb + ", order_status=" + order_status
					+ ", is_cart_number=" + is_cart_number + ", cart_number="
					+ cart_number + ", share_title=" + share_title
					+ ", share_des=" + share_des + ", post_url=" + post_url
					+ ", wxPath=" + wxPath + ", spc_status=" + spc_status + "]";
		}

	}

	/**
	 * 底部栏按钮
	 * */
	public class button_info {
		public other_button otherButton = new other_button();
		public order_button orderButton = new order_button();
		public change_button changeButton = new change_button();
		public appoint_button appointButton = new appoint_button();
		public cart_button cartButton = new cart_button();

		public other_button getOtherButton() {
			return otherButton;
		}

		public void setOtherButton(other_button otherButton) {
			this.otherButton = otherButton;
		}

		public order_button getOrderButton() {
			return orderButton;
		}

		public void setOrderButton(order_button orderButton) {
			this.orderButton = orderButton;
		}

		public change_button getChangeButton() {
			return changeButton;
		}

		public void setChangeButton(change_button changeButton) {
			this.changeButton = changeButton;
		}

		public appoint_button getAppointButton() {
			return appointButton;
		}

		public void setAppointButton(appoint_button appointButton) {
			this.appointButton = appointButton;
		}

		public cart_button getCartButton() {
			return cartButton;
		}

		public void setCartButton(cart_button cartButton) {
			this.cartButton = cartButton;
		}

	}

	/**
	 * 预约按钮
	 * */
	public class appoint_button {
		String is_show = "";
		String button_title = "";
		String appoint_status = "";

		public appoint_button() {
		}

		public String getIs_show() {
			return is_show;
		}

		public void setIs_show(String is_show) {
			this.is_show = is_show;
		}

		public String getAppoint_status() {
			return appoint_status;
		}

		public void setAppoint_status(String appoint_status) {
			this.appoint_status = appoint_status;
		}

		public String getButton_title() {
			return button_title;
		}

		public void setButton_title(String button_title) {
			this.button_title = button_title;
		}
		
		@Override
		public String toString() {
			return "appoint_button [is_show=" + is_show + ", button_title="
					+ button_title + ", appoint_status=" + appoint_status + "]";
		}

	}

	/**
	 * 购物车按钮
	 * */
	public class cart_button {
		String is_show = "";
		String button_title = "";

		public cart_button() {
		}

		public String getIs_show() {
			return is_show;
		}

		public void setIs_show(String is_show) {
			this.is_show = is_show;
		}

		public String getButton_title() {
			return button_title;
		}

		public void setButton_title(String button_title) {
			this.button_title = button_title;
		}

		@Override
		public String toString() {
			return "cart_button [is_show=" + is_show + ", button_title="
					+ button_title + "]";
		}

	}

	/**
	 * 更换按钮
	 * */
	public class change_button {
		String is_show = "";
		String button_title = "";

		public change_button() {
		}

		public String getIs_show() {
			return is_show;
		}

		public void setIs_show(String is_show) {
			this.is_show = is_show;
		}

		public String getButton_title() {
			return button_title;
		}

		public void setButton_title(String button_title) {
			this.button_title = button_title;
		}

		@Override
		public String toString() {
			return "change_button [is_show=" + is_show + ", button_title="
					+ button_title + "]";
		}

	}

	/**
	 * 下单按钮
	 * */
	public class order_button {
		String is_show = "";
		String button_title = "";

		public order_button() {
		}

		public String getIs_show() {
			return is_show;
		}

		public void setIs_show(String is_show) {
			this.is_show = is_show;
		}

		public String getButton_title() {
			return button_title;
		}

		public void setButton_title(String button_title) {
			this.button_title = button_title;
		}

		@Override
		public String toString() {
			return "order_button [is_show=" + is_show + ", button_title="
					+ button_title + "]";
		}
	}

	/**
	 * 更换按钮
	 * */
	public class other_button {
		String is_show = "";
		String button_title = "";

		public other_button() {
		}

		public String getIs_show() {
			return is_show;
		}

		public void setIs_show(String is_show) {
			this.is_show = is_show;
		}

		public String getButton_title() {
			return button_title;
		}

		public void setButton_title(String button_title) {
			this.button_title = button_title;
		}

		@Override
		public String toString() {
			return "other_button [is_show=" + is_show + ", button_title="
					+ button_title + "]";
		}

	}

	public ToysDetailBean02 fromJson(Context context, JSONObject json) {
		ToysDetailBean02 detai = null;
		try {
			detai = new ToysDetailBean02();
			detai.toysDetial.setWxPath(json.getJSONObject("detail").getString("share_path"));
			detai.toysDetial.setBusiness_id(json.getJSONObject("detail").getString("business_id"));
			detai.toysDetial.setImg_thumb(json.getJSONObject("detail").getString("share_img"));
			detai.toysDetial.setOrder_status(json.getJSONObject("detail").getString("order_status"));
			detai.toysDetial.setIs_cart_number(json.getJSONObject("detail").getString("is_cart_number"));
			detai.toysDetial.setShare_des(json.getJSONObject("detail").getString("share_des"));
			detai.toysDetial.setShare_title(json.getJSONObject("detail").getString("share_title"));
			detai.toysDetial.setPost_url(json.getJSONObject("detail").getString("post_url"));
			detai.toysDetial.setCart_number(json.getJSONObject("detail").getString("cart_number"));
			detai.toysDetial.setSpc_status(json.getJSONObject("detail").getString("collect_status"));
			ShoppingCartUtils.setCartNumber(context, Integer.parseInt(json.getJSONObject("detail").getString("cart_number")));

			detai.buttonInfo.appointButton.setIs_show(json
					.getJSONObject("button_info")
					.getJSONObject("appoint_button").getString("is_show"));
			detai.buttonInfo.appointButton.setAppoint_status(json
					.getJSONObject("button_info")
					.getJSONObject("appoint_button")
					.getString("appoint_status"));
			detai.buttonInfo.appointButton.setButton_title(json
					.getJSONObject("button_info")
					.getJSONObject("appoint_button").getString("button_title"));

			detai.buttonInfo.cartButton.setIs_show(json
					.getJSONObject("button_info").getJSONObject("cart_button")
					.getString("is_show"));
			detai.buttonInfo.cartButton.setButton_title(json
					.getJSONObject("button_info").getJSONObject("cart_button")
					.getString("button_title"));

			detai.buttonInfo.changeButton.setIs_show(json
					.getJSONObject("button_info")
					.getJSONObject("change_button").getString("is_show"));
			detai.buttonInfo.changeButton.setButton_title(json
					.getJSONObject("button_info")
					.getJSONObject("change_button").getString("button_title"));

			detai.buttonInfo.orderButton.setIs_show(json
					.getJSONObject("button_info").getJSONObject("order_button")
					.getString("is_show"));
			detai.buttonInfo.orderButton.setButton_title(json
					.getJSONObject("button_info").getJSONObject("order_button")
					.getString("button_title"));

			detai.buttonInfo.otherButton.setIs_show(json
					.getJSONObject("button_info").getJSONObject("other_button")
					.getString("is_show"));
			detai.buttonInfo.otherButton.setButton_title(json
					.getJSONObject("button_info").getJSONObject("other_button")
					.getString("button_title"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return detai;
	}

}

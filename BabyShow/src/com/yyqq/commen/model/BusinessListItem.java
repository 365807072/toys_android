package com.yyqq.commen.model;

import java.io.Serializable;

import org.json.JSONObject;

public class BusinessListItem implements Serializable{
	public String business_pic = "";
	public String business_id = "";
	public String business_title = "";
	public String subtitle = "";
	public long post_create_time;
	public String distance = "";
	public String business_market_price1 = "";
	public String business_babyshow_price1 = "";
	public String order_count = "";
	public String is_group = "";
	public String review_count;
	public String post_count;

//	public JSONObject json;

	public void fromJson(JSONObject json) {
		try {
			JSONObject info = null;
			if(json.has("img")){
				info = json.getJSONObject("img");
			}else{
				info = json;
			}
//			this.json = json;
			
			if (info.has("search_id")) {
				business_id = info.getString("search_id")+"";
			}
			if (info.has("search_word")) {
				business_title = info.getString("search_word");
			}
			if (info.has("img_description")) {
				subtitle = info.getString("search_word");
			}
			if (info.has("img_thumb")) {
				business_pic = info.getString("img_thumb");
			}
			if (info.has("is_group")) {
				is_group = info.getString("is_group")+"";
			}
			if (info.has("market_price")) {
				business_market_price1 = info.getString("market_price");
			}
			if (info.has("babyshow_price")) {
				business_babyshow_price1 = info.getString("babyshow_price");
			}
			if (info.has("review_count")) {
				review_count = info.getString("review_count");
			}
			if (info.has("post_count")) {
				post_count = info.getString("post_count");
			}
			if (info.has("business_id")) {
				business_id = info.getString("business_id") + "";
			}
			if (info.has("business_title")) {
				business_title = info.getString("business_title");
			}
			if (info.has("subtitle")) {
				subtitle = info.getString("subtitle");
			}
			if (info.has("post_create_time")) {
				post_create_time = info.getLong("post_create_time");
			}
			if (info.has("business_pic")) {
				business_pic = info.getString("business_pic");
			}
			if (info.has("distance")) {
				distance = info.getString("distance");
			}
			if (info.has("business_market_price1")) {
				business_market_price1 = info.getString("business_market_price1");
			}
			if (info.has("business_babyshow_price1")) {
				business_babyshow_price1 = info.getString("business_babyshow_price1");
			}
			if (info.has("order_count")) {
				order_count = info.getString("order_count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

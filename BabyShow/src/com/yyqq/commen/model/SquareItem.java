package com.yyqq.commen.model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class SquareItem {
	public String special_name = "";
	public String special_type = "";
	public String more_special = "";
	public String vice_special_name = "";
	public List<Img> listImg;

	public String type = "";
	public String is_click = "";
	// 专题
	public String cate_id = "";
	public String cate_name = "";
	// 群
	public String group_id = "";
	public String group_name = "";
	// 帖子
	public String img_id = "";
	// 值得买
	public String business_url = "";
	// 商家
	public String business_id = "";
	public String business_name = "";

	public class Img {
		public String img_thumb = "";
		public String img_id = "";
		public String user_id = "";
		public String img_title = "";
		public String business_market_price1 = ""; // 商家市场价
		public String business_babyshow_price1 = ""; // 商家秀秀价
		public String current_price = ""; // 值得买现价
	}

	public JSONObject json;

	public void fromJson(JSONObject json) {
		try {
			this.json = json;
			if (json.has("special_name")) {
				special_name = json.getString("special_name");
			}
			if (json.has("special_type")) {
				special_type = json.getString("special_type");
			}
			if (json.has("more_special")) {
				more_special = json.getString("more_special");
			}
			if (json.has("vice_special_name")) {
				vice_special_name = json.getString("vice_special_name");
			}
			// 广告位
			if (json.has("type")) {
				type = json.getString("type") + "";
			}
			if (json.has("is_click")) {
				is_click = json.getString("is_click") + "";
			}
			if (json.has("cate_id")) {
				cate_id = json.getInt("cate_id") + "";
			}
			if (json.has("cate_name")) {
				cate_name = json.getString("cate_name");
			}
			if (json.has("group_id")) {
				group_id = json.getInt("group_id") + "";
			}
			if (json.has("group_name")) {
				group_name = json.getString("group_name");
			}
			if (json.has("img_id")) {
				img_id = json.getInt("img_id") + "";
			}
			if (json.has("business_url")) {
				business_url = json.getString("business_url");
			}
			if (json.has("business_id")) {
				business_id = json.getString("business_id") + "";
			}
			if (json.has("business_name")) {
				business_name = json.getString("business_name");
			}

			// 图片数组
			JSONArray imgArray = json.getJSONArray("img");
			listImg = new ArrayList<SquareItem.Img>();
			for (int i = 0; i < imgArray.length(); i++) {
				Img arg0 = new Img();
				arg0.img_thumb = imgArray.getJSONObject(i).getString(
						"img_thumb");
				arg0.img_id = imgArray.getJSONObject(i).getString("img_id")
						+ "";
				arg0.user_id = imgArray.getJSONObject(i).getString("user_id")
						+ "";
				arg0.img_title = imgArray.getJSONObject(i).getString(
						"img_title");
				arg0.business_market_price1 = imgArray.getJSONObject(i)
						.getString("business_market_price1");
				arg0.business_babyshow_price1 = imgArray.getJSONObject(i)
						.getString("business_babyshow_price1");
				arg0.current_price = imgArray.getJSONObject(i).getString(
						"current_price");
				listImg.add(arg0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

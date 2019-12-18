package com.yyqq.commen.model;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BuyListItem {
	public String img_id = "";
	public String description = "";
	public String current_price = "";
	public String original_price = ""; // 原价
	public String is_postage = ""; // 动态
	public String reason = "";
	public String business = ""; // 商城
	public long post_create_time;
	public String post_url = "";
	public String between_time = "";
	public String good_type = "";
	public ArrayList<Image> ImageList = new ArrayList<Image>();

	public class Image {
		public String img_down = "";
		public String img = "";
		public String img_width = "";
		public String img_height = "";
		public String img_thumb = "";
		public String img_thumb_width = "";
		public String img_thumb_height = "";

		public void fromJson(JSONObject json) {
			try {
				img_down = json.getString("img_down");
				img = json.getString("img");
				img_width = json.getString("img_width");
				img_height = json.getString("img_height");
				img_thumb = json.getString("img_thumb");
				img_thumb_width = json.getString("img_thumb_width");
				img_thumb_height = json.getString("img_thumb_height");

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public JSONObject json;

	public void fromJson(JSONObject json) {
		try {
			JSONObject info = json.getJSONObject("img");
			this.json = json;
			if (info.has("img_id")) {
				img_id = info.getString("img_id");
			}
			if (info.has("description")) {
				description = info.getString("description");
			}
			if (info.has("current_price")) {
				current_price = info.getString("current_price") + "";
			}
			if (info.has("original_price")) {
				original_price = info.getString("original_price") + "";
			}
			if (info.has("is_postage")) {
				is_postage = info.getString("is_postage") + "";
			}
			if (info.has("reason")) {
				reason = info.getString("reason");
			}
			if (info.has("business")) {
				business = info.getString("business");
			}
			if (info.has("post_create_time")) {
				post_create_time = info.getLong("post_create_time");
			}
			if (info.has("post_url")) {
				post_url = info.getString("post_url");
			}
			if (info.has("between_time")) {
				between_time = info.getString("between_time");
			}
			if (info.has("good_type")) {
				good_type = info.getString("good_type") + "";
			}

			JSONArray imgArray = info.getJSONArray("img");
			for (int i = 0; i < imgArray.length(); i++) {
				Image mImage = new Image();
				mImage.fromJson(imgArray.optJSONObject(i));
				ImageList.add(mImage);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
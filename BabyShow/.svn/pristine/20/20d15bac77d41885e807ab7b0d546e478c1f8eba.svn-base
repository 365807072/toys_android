package com.yyqq.commen.model;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlazaItem {
	// 数据 img 中的参数
	public String img_id = "";
	public String user_id = "";
	public String user_name = "";
	public String avatar = "";
	public String level_img = "";

	// img数组中的数据
	public ArrayList<Img> imgs = new ArrayList<Img>();
	/* 以下是img数组中的数据 */
	public String img_down = "";
	public String img = "";
	public int img_width;
	public int img_height;
	public String img_thumb = "";
	public String img_thumb_width = "";
	public String img_thumb_height = "";

	/* 以上是img数组中的数据 */
	public String description = "";
	public long create_time;
	public String admire_count = "";
	public String review_count = "";
	public String img_cate = "";
	public String post_url = "";
	public String is_admire = "";

	// reviews数组
	public ArrayList<Review> reviews = new ArrayList<Review>();
	public ArrayList<String> imaStrings = new ArrayList<String>();
	public ArrayList<String> imaBig = new ArrayList<String>();
	public ArrayList<String> imaWid = new ArrayList<String>();
	public ArrayList<String> imaHed = new ArrayList<String>();

	public boolean isZan = false;
	public String tag = "MyShowItem";
	public JSONObject json;

	public void fromJson(JSONObject json) {
		try {
			JSONObject info = json.getJSONObject("img");
			this.json = json;
			if (info.has("img_id")) {
				img_id = info.getInt("img_id") + "";
			}
			if (info.has("user_id")) {
				user_id = info.getInt("user_id") + "";
			}
			if (info.has("user_name")) {
				user_name = info.getString("user_name");
			}
			if (info.has("avatar")) {
				avatar = info.getString("avatar");
			}
			if (info.has("level_img")) {
				level_img = info.getString("level_img");
			}
			JSONArray imgArray = info.getJSONArray("img");
			for (int i = 0; i < imgArray.length(); i++) {
				imaStrings
						.add(imgArray.getJSONObject(i).getString("img_thumb"));
				imaBig.add(imgArray.getJSONObject(i).getString("img"));
				imaWid.add(imgArray.getJSONObject(i).getString("img_width"));
				imaHed.add(imgArray.getJSONObject(i).getString("img_height"));
			}

			// 获取img数组里面的东西
			JSONArray imgsArray = info.getJSONArray("img");
			for (int i = 0; i < imgsArray.length(); i++) {
				Img img = new Img();
				img.fromJson(imgArray.getJSONObject(i));
				imgs.add(img);
			}

			if (info.has("description")) {
				description = info.getString("description");
			}
			if (info.has("admire_count")) {
				admire_count = info.getInt("admire_count") + "";
			}
			if (info.has("review_count")) {
				review_count = info.getString("review_count") + "";
			}
			if (info.has("create_time")) {
				create_time = info.getLong("create_time");
			}
			if (info.has("img_cate")) {
				img_cate = info.getString("img_cate");
			}
			if (info.has("post_url")) {
				post_url = info.getString("post_url");
			}
			if (info.has("is_admire")) {
				is_admire = info.getString("is_admire") + "";
			}

			// 回复数组里面的数据
			JSONArray reviewArray = json.getJSONArray("reviews");
			if (info.getInt("is_admire") == 0) {
				isZan = false;
			} else {
				isZan = true;
			}
			for (int i = 0; i < reviewArray.length(); i++) {
				Review review = new Review();
				review.fromJson(reviewArray.getJSONObject(i));
				reviews.add(review);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public class Img {
		/* 以下是img数组中的数据 */
		public String img_down = "";
		public String img = "";
		public String img_width;
		public String img_height;
		public String img_thumb = "";
		public String img_thumb_width = "";
		public String img_thumb_height = "";

		/* 以上是img数组中的数据 */

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

	public class Review {
		public String id = "";
		public String name = "";
		public String review = "";

		public void fromJson(JSONObject json) {
			try {
				id = json.getString("id");
				name = json.getString("user_name");
				review = json.getString("demand");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}

package com.yyqq.commen.model;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GrowDetailItem {
	public String img_id = "";
	public String img_title = "";
	public String user_id = "";
	public String user_name = "";
	public String avatar = "";
	public String img_description = "";
	public String diaryAdmire = "";
	public String diaryReview = "";
	public String is_admire = "";
	public String diaryTime = "";
	public String babys_idol_id = "";
	public String babys_count = "";
	public String is_type = "";
	public String tag_name = "";

	public ArrayList<Review> reviews = new ArrayList<GrowDetailItem.Review>();
	public ArrayList<Image> ImageList = new ArrayList<Image>();
	public ArrayList<Babys> babyList = new ArrayList<Babys>();

	public class Babys {
		public String baby_id = "";
		public String user_name = "";

		public void fromJson(JSONObject json) {
			try {
				baby_id = json.getString("baby_id");
				user_name = json.getString("user_name");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public class Image {
		public String img = "";
		public String img_down = "";
		public String img_width = "";
		public String img_height = "";
		public String img_thumb = "";
		public String img_thumb_width = "";
		public String img_thumb_height = "";

		public void fromJson(JSONObject json) {
			try {
				img = json.getString("img");
				img_down = json.getString("img_down");
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

	public boolean isZan = false;
	public JSONObject json;

	public void fromJson(JSONObject json, String tag) {
		try {
			JSONObject info = json.getJSONObject("img");
			this.json = json;
			if (info.has("img_title")) {
				img_title = info.getString("img_title");
			}
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
			if (info.has("img_description")) {
				img_description = info.getString("img_description");
			}
			if (info.has("diaryAdmire")) {
				diaryAdmire = info.getString("diaryAdmire");
			}
			if (info.has("diaryReview")) {
				diaryReview = info.getInt("diaryReview") + "";
			}
			if (info.has("is_admire")) {
				is_admire = info.getString("is_admire") + "";
			}
			if (info.has("diaryTime")) {
				diaryTime = info.getString("diaryTime") + "";
			}
			if (info.has("babys_count")) {
				babys_count = info.getString("babys_count") + "";
			}
			if (info.has("babys_idol_id")) {
				babys_idol_id = info.getString("babys_idol_id");
			}
			if (info.has("is_type")) {
				is_type = info.getString("is_type");
			}
			if (info.has("tag_name")) {
				tag_name = info.getString("tag_name");
			}

			JSONArray babyArray = info.getJSONArray("babys");
			for (int i = 0; i < babyArray.length(); i++) {
				Babys babys = new Babys();
				babys.fromJson(babyArray.optJSONObject(i));
				babyList.add(babys);
			}
			JSONArray imgArray = info.getJSONArray("img");
			for (int i = 0; i < imgArray.length(); i++) {
				Image mImage = new Image();
				mImage.fromJson(imgArray.optJSONObject(i));
				ImageList.add(mImage);
			}
			JSONArray reviewArray = info.getJSONArray("reviews");
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
}

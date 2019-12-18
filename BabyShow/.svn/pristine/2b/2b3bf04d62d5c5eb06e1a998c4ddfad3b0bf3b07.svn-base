package com.yyqq.commen.model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class SpecialDetailListItem {
	public String cate_name = "";
	public String user_id = "";
	public String img_id = "";
	public String img_description = "";
	public String avatar = "";
	public String nick_name = "";
	public String admire_count = "";
	public String review_count = "";
	public String is_admire = ""; // 是否赞
	public String is_focus = ""; // 是否关注
	public long create_time;
	public String interaction_count = ""; // 互动人数
	public String level_img = "";
	public String rank = "";
	public String rsort = "";
	public List<Img> listImg;
	public List<Avatars> listAvatar;

	public class Img {
		public String img = "";
		public String img_width = "";
		public String img_height = "";
		public String img_thumb = "";
		public String img_thumb_width = "";
		public String img_thumb_height = "";
	}

	public class Avatars {
		public String avatar = "";
	}

	public boolean isZan = false;
	public JSONObject json;

	public void fromJson(JSONObject json, String tag) {
		try {
			this.json = json;
			if (json.has("cate_name")) {
				cate_name = json.getString("cate_name");
			}
			if (json.has("user_id")) {
				user_id = json.getInt("user_id") + "";
			}
			if (json.has("img_id")) {
				img_id = json.getInt("img_id") + "";
			}
			if (json.has("img_description")) {
				img_description = json.getString("img_description");
			}
			if (json.has("avatar")) {
				avatar = json.getString("avatar");
			}
			if (json.has("nick_name")) {
				nick_name = json.getString("nick_name");
			}
			if (json.has("admire_count")) {
				admire_count = json.getInt("admire_count") + "";
			}
			if (json.has("review_count")) {
				review_count = json.getString("review_count") + "";
			}
			if (json.has("is_admire")) {
				is_admire = json.getString("is_admire") + "";
			}
			if (json.has("is_focus")) {
				is_focus = json.getString("is_focus") + "";
			}
			if (json.has("create_time")) {
				create_time = json.getLong("create_time");
			}
			if (json.has("interaction_count")) {
				interaction_count = json.getInt("interaction_count") + "";
			}
			if (json.has("level_img")) {
				level_img = json.getString("level_img");
			}
			if (json.has("rank")) {
				rank = json.getInt("rank") + "";
			}
			if (json.has("rsort")) {
				rsort = json.getInt("rsort") + "";
			}

			// 图片数组
			JSONArray imgArray = json.getJSONArray("imgs");
			listImg = new ArrayList<SpecialDetailListItem.Img>();
			for (int i = 0; i < imgArray.length(); i++) {
				Img arg0 = new Img();
				arg0.img = imgArray.getJSONObject(i).getString("img");
				arg0.img_thumb = imgArray.getJSONObject(i).getString(
						"img_thumb");
				arg0.img_width = imgArray.getJSONObject(i).getString(
						"img_width");
				arg0.img_height = imgArray.getJSONObject(i).getString(
						"img_height");
				arg0.img_thumb_width = imgArray.getJSONObject(i).getString(
						"img_thumb_width");
				arg0.img_thumb_height = imgArray.getJSONObject(i).getString(
						"img_thumb_height");
				listImg.add(arg0);
			}

			// 头像数组
			JSONArray avatarArray = json.getJSONArray("avatars");
			listAvatar = new ArrayList<SpecialDetailListItem.Avatars>();
			for (int i = 0; i < avatarArray.length(); i++) {
				Avatars avatar = new Avatars();
				avatar.avatar = avatarArray.getJSONObject(i)
						.getString("avatar");
				listAvatar.add(avatar);
			}

			if (json.getInt("is_admire") == 0) {
				isZan = false;
			} else {
				isZan = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

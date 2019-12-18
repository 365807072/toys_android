package com.yyqq.commen.model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class PostBarInterestItem {
	public String title = "";
	public String more_interest = "";
	public String interest_type = "";
	public List<Img> listImg;

	public class Img {
		public String img_thumb = "";
		public String img_id = "";
		public String user_id = "";
		public String img_title = "";
		public String description = "";
		public String review_count = ""; // 参与人数
		public String post_count = ""; // 帖子数量/观看人数
		public long create_time;
		public long post_create_time;
		public String group_id = "";
		public String group_name = "";
		public String is_group = ""; // is_group=1代表是群，is_group=0是话题
	}

	public JSONObject json;

	public void fromJson(JSONObject json) {
		try {
			this.json = json;
			if (json.has("title")) {
				title = json.getString("title");
			}
			if (json.has("more_interest")) {
				more_interest = json.getString("more_interest");
			}
			if (json.has("interest_type")) {
				interest_type = json.getString("interest_type") + "";
			}

			// 图片数组
			JSONArray imgArray = json.getJSONArray("img");
			listImg = new ArrayList<PostBarInterestItem.Img>();
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
				arg0.description = imgArray.getJSONObject(i).getString(
						"description");
				arg0.review_count = imgArray.getJSONObject(i).getString(
						"review_count")
						+ "";
				arg0.post_count = imgArray.getJSONObject(i).getString(
						"post_count")
						+ "";
				arg0.create_time = imgArray.getJSONObject(i).getLong(
						"create_time");
				arg0.post_create_time = imgArray.getJSONObject(i).getLong(
						"post_create_time");
				arg0.group_id = imgArray.getJSONObject(i).getString("group_id")
						+ "";
				arg0.group_name = imgArray.getJSONObject(i).getString(
						"group_name");
				arg0.is_group = imgArray.getJSONObject(i).getString("is_group")
						+ "";
				listImg.add(arg0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

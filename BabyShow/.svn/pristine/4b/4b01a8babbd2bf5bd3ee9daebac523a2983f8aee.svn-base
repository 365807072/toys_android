package com.yyqq.commen.model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class SpecialDetailGridItem {
	public String cate_name = "";
	public String user_id = "";
	public String img_id = "";
	public long create_time;
	public String rsort = "";
	public JSONObject json;
	public List<Img> listImg;

	public class Img {
		public String img = "";
		public String img_width = "";
		public String img_height = "";
		public String img_thumb = "";
		public String img_thumb_width = "";
		public String img_thumb_height = "";
	}

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
			if (json.has("create_time")) {
				create_time = json.getLong("create_time");
			}
			if (json.has("rsort")) {
				rsort = json.getInt("rsort") + "";
			}

			// 图片数组
			JSONArray imgArray = json.getJSONArray("imgs");
			listImg = new ArrayList<SpecialDetailGridItem.Img>();
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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package com.yyqq.commen.model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class HeadListItem {
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
	// 展播
	public List<Img> imgList = new ArrayList<Img>();
	// 值得买
	public String business_url = "";
	//商家
	public String business_id = "";
	public String business_name = "";
	public String video_url = "";
	public String title = "";
	
	
	public class Img {
		public String img_thumb = "";
		public String img_thumb_width = "";
		public String img_thumb_height = "";
	}
	
	@Override
	public String toString() {
		return "HeadListItem [type=" + type + ", is_click=" + is_click
				+ ", cate_id=" + cate_id + ", cate_name=" + cate_name
				+ ", group_id=" + group_id + ", group_name=" + group_name
				+ ", img_id=" + img_id + ", imgList=" + imgList
				+ ", business_url=" + business_url + ", business_id="
				+ business_id + ", business_name=" + business_name
				+ ", video_url=" + video_url + ", title=" + title + ", json="
				+ json + "]";
	}

	public JSONObject json;

	public void fromJson(JSONObject json, String tag) {
		try {
			if (json.has("video_url")) {
				video_url = json.getString("video_url") + "";
			}
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
			if (json.has("business_url_app")) {
				business_url = json.getString("business_url_app");
			}
			if (json.has("business_id")) {
				business_id = json.getString("business_id") + "";
			}
			if (json.has("business_name")) {
				business_name = json.getString("business_name");
			}
			if (json.has("title")) {
				title = json.getString("title");
			}
			JSONArray imgArray = json.getJSONArray("img");
			for (int i = 0; i < imgArray.length(); i++) {
				Img img = new Img();
				img.img_thumb = imgArray.getJSONObject(i)
						.getString("img_thumb");
				img.img_thumb_width = imgArray.getJSONObject(i).getString(
						"img_thumb_width");
				img.img_thumb_height = imgArray.getJSONObject(i).getString(
						"img_thumb_height");
				imgList.add(img);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
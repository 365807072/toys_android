package com.yyqq.commen.model;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class ReleaseItem {
	public String img_id = "";
	public String user_id = "";
	public String user_name = "";
	public String avatar = "";

	public String description = "";
	public String img_title = "";
	public String admire_count = "";
	public String review_count = "";
	public long create_time;
	public String img_cate = "";
	public String post_url = "";
	public String post_url_title = "";
	public String post_url_image = "";
	public String post_create_time = "";
	public String is_admire = "";
	public String come_from = "";

	public String img_down = "";
	public String img = "";
	public String img_width = "";
	public String img_height = "";
	public String img_thumb = "";
	public String img_thumb_width = "";
	public String img_thumb_height = "";
	public String video_url = "";
	public String video_image_url = "";
	public boolean isPlay = false;
	public String group_id = "";
	public String group_name = "";

	public String imageThumb = "";
	public ArrayList<String> imaStrings = new ArrayList<String>();
	public ArrayList<String> imaBig = new ArrayList<String>();
	public ArrayList<String> imaWid = new ArrayList<String>();
	public ArrayList<String> imaHed = new ArrayList<String>();

	public boolean isZan = false;
	public JSONObject json;

	public void fromJson(JSONObject json) {
		try {
			JSONObject info = json.getJSONObject("img");
			this.json = json;

			if (info.has("video_url")) {
				video_url = info.getString("video_url") + "";
			}
			if (info.has("group_id")) {
				group_id = info.getString("group_id") + "";
			}
			if (info.has("group_name")) {
				group_name = info.getString("group_name") + "";
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
			if (info.has("img_title")) {
				img_title = info.getString("img_title");
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
			if (info.has("post_url_title")) {
				post_url_title = info.getString("post_url_title");
			}
			if (info.has("post_url_image")) {
				post_url_image = info.getString("post_url_image");
			}
			if (info.has("is_admire")) {
				is_admire = info.getString("is_admire") + "";
			}
			if (info.has("post_create_time")) {
				post_create_time = info.getString("post_create_time") + "";
			}
			if (info.has("come_from")) {
				come_from = info.getString("come_from") + "";
			}

			JSONArray imgArray = info.getJSONArray("img");
			for (int i = 0; i < imgArray.length(); i++) {

				img_down = imgArray.getJSONObject(i).getString("img_down");
				img = imgArray.getJSONObject(i).getString("img");
				img_width = imgArray.getJSONObject(i).getString("img_width");
				img_height = imgArray.getJSONObject(i).getString("img_height");
				img_thumb = imgArray.getJSONObject(i).getString("img_thumb");
				img_thumb_width = imgArray.getJSONObject(i).getString(
						"img_thumb_width");
				img_thumb_height = imgArray.getJSONObject(i).getString(
						"img_thumb_height");

				imaStrings.add(img_thumb);
				imaBig.add(img);
				imaWid.add(img_width);
				imaHed.add(img_height);
			}

			if (info.getInt("is_admire") == 0) {
				isZan = false;
			} else {
				isZan = true;
			}

			JSONArray imgArr = info.getJSONArray("img");
			JSONObject imgObj = (JSONObject) imgArr.opt(0);

			if (imgObj.has("img_thumb")) {
				video_image_url = imgObj.getString("img_thumb");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "ReleaseItem [img_id=" + img_id + ", user_id=" + user_id
				+ ", user_name=" + user_name + ", avatar=" + avatar
				+ ", description=" + description + ", img_title=" + img_title
				+ ", admire_count=" + admire_count + ", review_count="
				+ review_count + ", create_time=" + create_time + ", img_cate="
				+ img_cate + ", post_url=" + post_url + ", post_url_title="
				+ post_url_title + ", post_url_image=" + post_url_image
				+ ", post_create_time=" + post_create_time + ", is_admire="
				+ is_admire + ", come_from=" + come_from + ", img_down="
				+ img_down + ", img=" + img + ", img_width=" + img_width
				+ ", img_height=" + img_height + ", img_thumb=" + img_thumb
				+ ", img_thumb_width=" + img_thumb_width
				+ ", img_thumb_height=" + img_thumb_height + ", video_url="
				+ video_url + ", video_image_url=" + video_image_url
				+ ", isPlay=" + isPlay + ", group_id=" + group_id
				+ ", group_name=" + group_name + ", imageThumb=" + imageThumb
				+ ", imaStrings=" + imaStrings + ", imaBig=" + imaBig
				+ ", imaWid=" + imaWid + ", imaHed=" + imaHed + ", isZan="
				+ isZan + ", json=" + json + "]";
	}

}
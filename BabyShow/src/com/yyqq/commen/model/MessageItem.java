package com.yyqq.commen.model;

import org.json.JSONObject;

public class MessageItem {
	public String user_id = "";
	public String avatar = "";
	public String nick_name = "";
	public String level_img = "";
	public String id = "";
	public String message = "";
	public String share_user_id = "";
	public long create_time = 0;
	// point
	// 值（11发秀秀、12赞秀秀、13评论秀秀、14人气宝宝；21热点发帖、22热点赞、23热点评论、24热点跟帖；31合并成长记录；41加为好友(关注)、42共享请求；
	// 61上传图片、62修改资料、63访问了你、64转发、65更新了共享相册）
	public String admire_count = "";
	public String review_count = "";
	// "is_agree": 0, 是否同意 共享请求 0 否 1是
	public int is_agree = 0;
	public String is_post = "";
	public String img_thumb = "";
	public String img_id = "";
	public String root_img_id = "";
	public String point = "";
	public int is_read = 0;
	public int relation = 0;
	public int is_combin = 0;
	public String video_url = "";

	// "relation": 2, 好友之间的关系 0 陌生 1 为 关注 2 为互相关注

	public void fromJson(JSONObject json) {
		try {
			if (json.has("user_id")) {
				user_id = json.getInt("user_id") + "";
			}
			if (json.has("avatar")) {
				avatar = json.getString("avatar");
			}
			if (json.has("nick_name")) {
				nick_name = json.getString("nick_name");
			}
			if (json.has("share_user_id")) {
				share_user_id = json.getInt("share_user_id") + "";
			}
			if (json.has("id")) {
				id = json.getString("id") + "";
			}
			if (json.has("message")) {
				message = json.getString("message");
			}
			if (json.has("create_time")) {
				create_time = json.getLong("create_time");
			}
			if (json.has("root_img_id")) {
				root_img_id = json.getString("root_img_id") + "";
			}
			if (json.has("admire_count")) {
				admire_count = json.getInt("admire_count") + "";
			}
			if (json.has("review_count")) {
				review_count = json.getInt("review_count") + "";
			}
			if (json.has("is_agree")) {
				is_agree = json.getInt("is_agree");
			}
			if (json.has("is_post")) {
				is_post = json.getString("is_post") + "";
			}
			if (json.has("img_thumb")) {
				img_thumb = json.getString("img_thumb");
			}
			if (json.has("img_id")) {
				img_id = json.getInt("img_id") + "";
			}
			if (json.has("point")) {
				point = json.getString("point");
			}
			if (json.has("is_read")) {
				is_read = json.getInt("is_read");
			}
			if (json.has("is_combin")) {
				is_combin = json.getInt("is_combin");
			}
			if (json.has("relation")) {
				relation = json.getInt("relation");
			}
			if (json.has("level_img")) {
				level_img = json.getString("level_img");
			}
			if (json.has("video_url")) {
				video_url = json.getString("video_url");
			}
		} catch (Exception e) {
		}
	}
}

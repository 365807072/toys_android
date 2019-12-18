package com.yyqq.commen.model;

import org.json.JSONObject;

public class MyShareListItem {
	public String user_id;
	public String baby_id;
	public String share_baby_id;
	public String nick_name;
	public String user_avatar;
	public String baby_name;
	public String is_common;

	public void fromJson(JSONObject json) {
		try {
			if (json.has("user_id")) {
				user_id = json.getInt("user_id") + "";
			}
			if (json.has("baby_id")) {
				baby_id = json.getInt("baby_id") + "";
			}
			if (json.has("share_baby_id")) {
				share_baby_id = json.getInt("share_baby_id") + "";
			}
			if (json.has("nick_name")) {
				nick_name = json.getString("nick_name");
			}
			if (json.has("user_avatar")) {
				user_avatar = json.getString("user_avatar");
			}
			if (json.has("baby_name")) {
				baby_name = json.getString("baby_name");
			}
			if (json.has("is_common")) {
				is_common = json.getInt("is_common") + "";
			}
		} catch (Exception e) {
		}
	}
}

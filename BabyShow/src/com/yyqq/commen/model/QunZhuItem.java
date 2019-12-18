package com.yyqq.commen.model;

import org.json.JSONException;
import org.json.JSONObject;

public class QunZhuItem {
	public String user_id;
	public String nick_name;
	public String avatar;
	public String group_name;
	public String review_count;
	public String post_count;
	public String is_share = "";
	JSONObject json;

	public void fromJson(JSONObject json) throws JSONException {
		this.json = json;
		if (json.has("user_id")) {
			user_id = json.getInt("user_id") + "";
		}
		if (json.has("nick_name")) {
			nick_name = json.getString("nick_name");
		}
		if (json.has("avatar")) {
			avatar = json.getString("avatar");
		}
		if (json.has("group_name")) {
			group_name = json.getString("group_name");
		}
		if (json.has("review_count")) {
			review_count = json.getInt("review_count") + "";
		}
		if (json.has("post_count")) {
			post_count = json.getInt("post_count") + "";
		}
		if (json.has("is_share")) {
			is_share = json.getInt("is_share") + "";
		}
	}
}
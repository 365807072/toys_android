package com.yyqq.commen.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ActiveCoverList {

	public String active_id;
	public String cover;
	public String user_id;
	public String baby_name;
	JSONObject json;

	public void fromJson(JSONObject json) throws JSONException {
		this.json = json;
		active_id = json.getInt("active_id") + "";
		cover = json.getString("cover");
		user_id = json.getString("user_id");
		baby_name = json.getString("user_name");
	}

}

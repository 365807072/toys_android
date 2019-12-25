package com.yyqq.commen.model;

import org.json.JSONException;
import org.json.JSONObject;

public class TagItem {
	public String id;
	public String tag_name;
	JSONObject json;

	public void fromJson(JSONObject json) throws JSONException {
		this.json = json;
		if(json.has("id")){
			id = json.getInt("id") + "";
		}
		if(json.has("tag_name")){
			tag_name = json.getString("tag_name");
		}
	}
}
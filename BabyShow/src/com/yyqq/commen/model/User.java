package com.yyqq.commen.model;

import org.json.JSONObject;


public class User extends People {
	public String tag = "User";

	public void fromJson(JSONObject json) {
		super.fromJson(json);
	}
}

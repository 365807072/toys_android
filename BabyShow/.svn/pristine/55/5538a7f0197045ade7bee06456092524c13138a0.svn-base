package com.yyqq.commen.model;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class PhotoItem {
	public String img_id = "";
	public String img_thumb = "";
	public String img_width = "";
	public String img_height = "";
	public ArrayList<String> imaWid = new ArrayList<String>();
	public ArrayList<String> imaHed = new ArrayList<String>();
	JSONObject json;

	public void fromJson(JSONObject json) throws JSONException {
		this.json = json;
		if (json.has("img_id")) {
			img_id = json.getString("img_id") + "";
		}
		if (json.has("img_thumb")) {
			img_thumb = json.getString("img_thumb");
		}
		if (json.has("img_width")) {
			img_width = json.getString("img_width");
		}
		if (json.has("img_height")) {
			img_height = json.getString("img_height");
		}
		imaWid.add(String.valueOf(img_width));
		imaHed.add(String.valueOf(img_height));

	}
}

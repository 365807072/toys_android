package com.yyqq.commen.model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;


public class ShaiXuanItem {
	public String id = "";
	public String city_name = "";
	public List<Children> listChildren;
	JSONObject json;

	public class Children {
		public String id = "";
		public String city_name = "";
	}

	public void fromJson(JSONObject json) {
		try {
			this.json = json;
			if (json.has("id")) {
				id = json.getString("id");
			}
			if (json.has("city_name")) {
				city_name = json.getString("city_name");
			}

			JSONArray childrenArray = json.getJSONArray("children");
			listChildren = new ArrayList<ShaiXuanItem.Children>();
			for (int i = 0; i < childrenArray.length(); i++) {
				Children ch = new Children();
				ch.id = childrenArray.getJSONObject(i).getString("id");
				ch.city_name = childrenArray.getJSONObject(i).getString(
						"city_name");
				listChildren.add(ch);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
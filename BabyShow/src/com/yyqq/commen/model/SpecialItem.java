package com.yyqq.commen.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class SpecialItem {
	public String cate_id = "";
	public String cate_name = "";
	public String renshu = "";
	public String user_id = "";
	public String image = "";
	public String click = "";
	// public String [] img_thumb ;
	public List<Img> listImg;
	public JSONObject json;
	public String rank = "";

	public class Img {
		public String img;
	}

	public void fromJson(JSONObject json, String tag) {
		try {
			this.json = json;
			if (json.has("user_id")) {
				user_id = json.getInt("user_id") + "";
			}
			if (json.has("cate_id")) {
				cate_id = json.getInt("cate_id") + "";
			}
			if (json.has("cate_name")) {
				cate_name = json.getString("cate_name");
			}
			if (json.has("renshu")) {
				renshu = json.getString("renshu") + "";
			}
			if (json.has("click")) {
				click = json.getInt("click") + "";
			}
			if (json.has("rank")) {
				rank = json.getInt("rank") + "";
			}
			if (json.has("image")) {
				image = json.getString("image");
			}
			JSONArray imgArray = json.getJSONArray("imgs");
			listImg = new ArrayList<SpecialItem.Img>();
			// img_thumb = new String [imgArray.length()];
			for (int i = 0; i < imgArray.length(); i++) {
				// img_thumb [i] =
				// imgArray.getJSONObject(i).getString("img_thumb");
				Img arg0 = new Img();
				arg0.img = imgArray.getJSONObject(i).getString("img_thumb");
				listImg.add(arg0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

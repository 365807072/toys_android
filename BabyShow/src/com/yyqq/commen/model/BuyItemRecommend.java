package com.yyqq.commen.model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class BuyItemRecommend {
	public String buy_list = "";
	public String current_price = "";
	public List<Img> listImg;
	public JSONObject json;

	public class Img {
		public String img_thumb;
	}

	public void fromJson(JSONObject json, String tag) {
		try {
			this.json = json;
			if (json.has("buy_list")) {
				buy_list = json.getInt("buy_list") + "";
			}
			if (json.has("current_price")) {
				current_price = json.getString("current_price");
			}
			JSONArray imgArray = json.getJSONArray("imgs");
			listImg = new ArrayList<BuyItemRecommend.Img>();
			for (int i = 0; i < imgArray.length(); i++) {
				Img arg0 = new Img();
				arg0.img_thumb = imgArray.getJSONObject(i).getString("img_thumb");
				listImg.add(arg0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

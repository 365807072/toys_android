package com.yyqq.commen.model;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class BuyItem {
	public String buy_list = "";
	public String cate_name = "";
	public List<Img> listImg;
	public JSONObject json;

	public class Img {
		public String img;
		public String current_price = "";    //现价
		public String original_price = "";   //原价
		public String img_description = "";
	}

	public void fromJson(JSONObject json, String tag) {
		try {
			this.json = json;
			if (json.has("buy_list")) {
				buy_list = json.getInt("buy_list") + "";
			}
			if (json.has("cate_name")) {
				cate_name = json.getString("cate_name");
			}
			JSONArray imgArray = json.getJSONArray("imgs");
			listImg = new ArrayList<BuyItem.Img>();
			for (int i = 0; i < imgArray.length(); i++) {
				Img arg0 = new Img();
				arg0.img = imgArray.getJSONObject(i).getString("img_thumb");
				arg0.current_price = imgArray.getJSONObject(i).getString(
						"current_price");
				arg0.original_price = imgArray.getJSONObject(i).getString(
						"original_price");
				arg0.img_description = imgArray.getJSONObject(i).getString(
						"img_description");
				listImg.add(arg0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

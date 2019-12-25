package com.yyqq.commen.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;

/**
 * 话题详情主体
 * */
public class MainItemBodyBean {
	private String img_id = "";
	private String user_id = "";
	private String user_name = "";
	private String avatar = "";
	private String level_img = "";
	private String post_url = "";
	private String create_time = "";
	private String group_id = "";
	private String group_name = "";
	private String img_title = "";
	private String description = "";
	private String is_admire = "";
	private String main_admire_count = "";
	private String is_idol = "";
	private String group_tag_id = "";
	private String group_tag_name = "";
	private String is_group_tag = "";
	private String business_id = "";
	private String is_link = "";
	private String is_save = "";
	private String tag_id = "";
	private String video_img_thumb = "";
	private String video_img_thumb_width = "";
	private String video_img_thumb_height = "";
	private String link_name = "";
	private String video_img_url = "";
	private ArrayList<MainItemBodyImgBean> img = new ArrayList<MainItemBodyImgBean>();
	

	public String getVideo_img_url() {
		return video_img_url;
	}

	public void setVideo_img_url(String video_img_url) {
		this.video_img_url = video_img_url;
	}

	public String getLink_name() {
		return link_name;
	}

	public void setLink_name(String link_name) {
		this.link_name = link_name;
	}


	public String getVideo_img_thumb() {
		return video_img_thumb;
	}


	public void setVideo_img_thumb(String video_img_thumb) {
		this.video_img_thumb = video_img_thumb;
	}


	public String getVideo_img_thumb_width() {
		return video_img_thumb_width;
	}


	public void setVideo_img_thumb_width(String video_img_thumb_width) {
		this.video_img_thumb_width = video_img_thumb_width;
	}


	public String getVideo_img_thumb_height() {
		return video_img_thumb_height;
	}


	public void setVideo_img_thumb_height(String video_img_thumb_height) {
		this.video_img_thumb_height = video_img_thumb_height;
	}

	public MainItemBodyBean() {
		super();
	}
	
	
	public String getTag_id() {
		return tag_id;
	}



	public void setTag_id(String tag_id) {
		this.tag_id = tag_id;
	}



	public String getIs_idol() {
		return is_idol;
	}



	public void setIs_idol(String is_idol) {
		this.is_idol = is_idol;
	}



	public String getGroup_tag_id() {
		return group_tag_id;
	}



	public void setGroup_tag_id(String group_tag_id) {
		this.group_tag_id = group_tag_id;
	}



	public String getGroup_tag_name() {
		return group_tag_name;
	}



	public void setGroup_tag_name(String group_tag_name) {
		this.group_tag_name = group_tag_name;
	}



	public String getIs_group_tag() {
		return is_group_tag;
	}



	public void setIs_group_tag(String is_group_tag) {
		this.is_group_tag = is_group_tag;
	}



	public String getBusiness_id() {
		return business_id;
	}



	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}



	public String getIs_link() {
		return is_link;
	}



	public void setIs_link(String is_link) {
		this.is_link = is_link;
	}



	public String getIs_save() {
		return is_save;
	}



	public void setIs_save(String is_save) {
		this.is_save = is_save;
	}



	public String getImg_id() {
		return img_id;
	}

	public void setImg_id(String img_id) {
		this.img_id = img_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}



	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}



	public String getAvatar() {
		return avatar;
	}



	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}



	public String getLevel_img() {
		return level_img;
	}



	public void setLevel_img(String level_img) {
		this.level_img = level_img;
	}



	public String getPost_url() {
		return post_url;
	}



	public void setPost_url(String post_url) {
		this.post_url = post_url;
	}



	public String getCreate_time() {
		return create_time;
	}



	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}



	public String getGroup_id() {
		return group_id;
	}



	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}



	public String getGroup_name() {
		return group_name;
	}



	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}



	public String getImg_title() {
		return img_title;
	}



	public void setImg_title(String img_title) {
		this.img_title = img_title;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getIs_admire() {
		return is_admire;
	}



	public void setIs_admire(String is_admire) {
		this.is_admire = is_admire;
	}



	public String getMain_admire_count() {
		return main_admire_count;
	}



	public void setMain_admire_count(String main_admire_count) {
		this.main_admire_count = main_admire_count;
	}



	public ArrayList<MainItemBodyImgBean> getImg() {
		return img;
	}



	public void setImg(ArrayList<MainItemBodyImgBean> img) {
		this.img = img;
	}



	@Override
	public String toString() {
		return "MainItemBodyBean [img_id=" + img_id + ", user_id=" + user_id
				+ ", user_name=" + user_name + ", avatar=" + avatar
				+ ", level_img=" + level_img + ", post_url=" + post_url
				+ ", create_time=" + create_time + ", group_id=" + group_id
				+ ", group_name=" + group_name + ", img_title=" + img_title
				+ ", description=" + description + ", is_admire=" + is_admire
				+ ", main_admire_count=" + main_admire_count + ", is_idol="
				+ is_idol + ", group_tag_id=" + group_tag_id
				+ ", group_tag_name=" + group_tag_name + ", is_group_tag="
				+ is_group_tag + ", business_id=" + business_id + ", is_link="
				+ is_link + ", is_save=" + is_save + ", tag_id=" + tag_id
				+ ", img=" + img + "]";
	}

	/**
	 * 解析数据，解析完成后需要调用initData()方法重新封装数据
	 * 
	 * @return
	 * 
	 * */
	@SuppressLint("NewApi")
	public static void fromJson(JSONArray jsonAll, MainItemBodyItemBean itemBean) {

		ArrayList<MainItemBodyBean> bodyList = new ArrayList<MainItemBodyBean>(); // 未处理的集合
		MainItemBodyBean bean;
		JSONObject json;

		for (int j = 0; j < jsonAll.length(); j++) {
			bean = new MainItemBodyBean();
			try {
				json = jsonAll.getJSONObject(j).getJSONObject("img");
				
				if (json.has("video_url")) {
					bean.setVideo_img_url(json.getString("video_url"));
				}
				
				if (json.has("link_name")) {
					bean.setLink_name(json.getString("link_name"));
				}
				
				if (json.has("video_img_thumb")) {
					bean.setVideo_img_thumb(json.getString("video_img_thumb"));
				}

				if (json.has("video_img_thumb_width")) {
					bean.setVideo_img_thumb_width(json.getString("video_img_thumb_width"));
				}

				if (json.has("video_img_thumb_height")) {
					bean.setVideo_img_thumb_height(json.getString("video_img_thumb_height"));
				}

				if (json.has("img_id")) {
					bean.setImg_id(json.getString("img_id"));
				}
				
				if (json.has("tag_id")) {
					bean.setTag_id(json.getString("tag_id"));
				}

				if (json.has("user_id")) {
					bean.setUser_id(json.getString("user_id"));
				}

				if (json.has("user_name")) {
					bean.setUser_name(json.getString("user_name"));
				}

				if (json.has("avatar")) {
					bean.setAvatar(json.getString("avatar"));
				}

				if (json.has("level_img")) {
					bean.setLevel_img(json.getString("level_img"));
				}

				if (json.has("post_url")) {
					bean.setPost_url(json.getString("post_url"));
				}
				if (json.has("create_time")) {
					bean.setCreate_time(json.getString("create_time"));
				}
				if (json.has("group_id")) {
					bean.setGroup_id(json.getString("group_id"));
				}
				if (json.has("group_name")) {
					bean.setGroup_name(json.getString("group_name"));
				}

				if (json.has("img_title")) {
					bean.setImg_title(json.getString("img_title"));
				}

				if (json.has("description")) {
					if (null != json.getString("description")
							&& !json.getString("description").isEmpty()
							&& !"null".equals(json.getString("description"))) {
						bean.setDescription(json.getString("description"));
					}
				}
				
				if (json.has("is_admire")) {
					bean.setIs_admire(json.getString("is_admire"));
				}
				
				if (json.has("main_admire_count")) {
					bean.setMain_admire_count(json.getString("main_admire_count"));
				}
				
				if (json.has("is_idol")) {
					bean.setIs_idol(json.getString("is_idol"));
				}
				
				if (json.has("group_tag_id")) {
					bean.setGroup_tag_id(json.getString("group_tag_id"));
				}
				
				if (json.has("group_tag_name")) {
					bean.setGroup_tag_name(json.getString("group_tag_name"));
				}
				
				if (json.has("is_group_tag")) {
					bean.setIs_group_tag(json.getString("is_group_tag"));
				}
				
				if (json.has("business_id")) {
					bean.setBusiness_id(json.getString("business_id"));
				}
				
				if (json.has("is_link")) {
					bean.setIs_link(json.getString("is_link"));
				}
				
				if (json.has("is_save")) {
					bean.setIs_save(json.getString("is_save"));
				}

				// 获取图片集合
				JSONArray jsonArray = json.getJSONArray("img");
				MainItemBodyImgBean imageBean;
				ArrayList<MainItemBodyImgBean> imageBeanList = new ArrayList<MainItemBodyImgBean>();
				if (jsonArray.length() != 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						imageBean = new MainItemBodyImgBean();
						imageBean.setImg_down(jsonArray.getJSONObject(i).getString("img_down"));
						imageBean.setImg(jsonArray.getJSONObject(i).getString("img"));
						imageBean.setImg_width(jsonArray.getJSONObject(i).getString("img_width"));
						imageBean.setImg_height(jsonArray.getJSONObject(i).getString("img_height"));
						imageBean.setImg_thumb(jsonArray.getJSONObject(i).getString("img_thumb"));
						imageBean.setImg_thumb_width(jsonArray.getJSONObject(i).getString("img_thumb_width"));
						imageBean.setImg_thumb_height(jsonArray.getJSONObject(i).getString("img_thumb_height"));
						imageBeanList.add(imageBean);
					}
					bean.setImg(imageBeanList);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			bodyList.add(bean);
		}
		initData(bodyList, itemBean);
	}

	/**
	 * 再次重新封装数据，以达到让适配更简单目的
	 * */
	public static void initData(ArrayList<MainItemBodyBean> bodyList,
			MainItemBodyItemBean itemBean) {

		itemBean.setImg_id(bodyList.get(0).getImg_id());
		itemBean.setUser_id(bodyList.get(0).getUser_id());
		itemBean.setUser_name(bodyList.get(0).getUser_name());
		itemBean.setAvatar(bodyList.get(0).getAvatar());
		itemBean.setLevel_img(bodyList.get(0).getLevel_img());
		itemBean.setMain_admire_count(bodyList.get(0).getMain_admire_count());
		itemBean.setPost_url(bodyList.get(0).getPost_url());
		itemBean.setCreate_time(bodyList.get(0).getCreate_time());
		itemBean.setGroup_id(bodyList.get(0).getGroup_id());
		itemBean.setGroup_name(bodyList.get(0).getGroup_name());
		itemBean.setImg_title(bodyList.get(0).getImg_title());
		itemBean.setDescription(bodyList.get(0).getDescription());
		itemBean.setIs_admire(bodyList.get(0).getIs_admire());
		itemBean.setMain_admire_count(bodyList.get(0).getMain_admire_count());
		itemBean.setFrom_url(bodyList.get(0).getPost_url());
		itemBean.setGroup_tag_id(bodyList.get(0).getGroup_tag_id());
		itemBean.setGroup_tag_name(bodyList.get(0).getGroup_tag_name());
		itemBean.setIs_group_tag(bodyList.get(0).getIs_group_tag());
		itemBean.setBusiness_id(bodyList.get(0).getBusiness_id());
		itemBean.setIs_link(bodyList.get(0).getIs_link());
		itemBean.setVideo_img_thumb(bodyList.get(0).getVideo_img_thumb());
		itemBean.setVideo_img_thumb_width(bodyList.get(0).getVideo_img_thumb_width());
		itemBean.setVideo_img_thumb_height(bodyList.get(0).getVideo_img_thumb_height());
		itemBean.setLink_name(bodyList.get(0).getLink_name());
		itemBean.setVideo_img_url(bodyList.get(0).getVideo_img_url());
		
		if(bodyList.get(0).getIs_idol().equals("0")){
			itemBean.setFriends(false);
		}else if(bodyList.get(0).getIs_idol().equals("1")){
			itemBean.setFriends(true);
		}else if(bodyList.get(0).getIs_idol().equals("2")){
			itemBean.setIs_idol("2");
		}else{
			itemBean.setIs_idol("2");
		}
		
		if(bodyList.get(0).getIs_save().equals("0")){
			itemBean.setIsSave(false);
		}else if(bodyList.get(0).getIs_save().equals("1")){
			itemBean.setIsSave(true);
		}else{
			itemBean.setIsSave(false);
		}

		MainItemBodyImgBean infoBean;
		for (int i = 0; i < bodyList.size(); i++) {
			// 添加文字
			if (!bodyList.get(i).getDescription().isEmpty()) {
				infoBean = new MainItemBodyImgBean();
				infoBean.setDescription(bodyList.get(i).getDescription());
				itemBean.getHandleList().add(infoBean);
			}
			for (int j = 0; j < bodyList.get(i).getImg().size(); j++) {
				// 添加图片信息
				itemBean.getHandleList().add(bodyList.get(i).getImg().get(j));
			}
		}
	}
}

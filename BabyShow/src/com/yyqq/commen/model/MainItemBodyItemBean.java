package com.yyqq.commen.model;

import java.util.ArrayList;

/**
 * 重新处理过的话题详情bean
 * */
public class MainItemBodyItemBean {

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
	private ArrayList<MainItemBodyImgBean> handleList = new ArrayList<MainItemBodyImgBean>();
	private String from_url = ""; // 外链地址
	private String is_idol = "";
	private boolean isFriends = false; // 是否是好友
	private boolean isSave = false; // 是否收藏
	private String group_tag_id = ""; // 群ID（或标签ID）
	private String group_tag_name = ""; // 群名（或标签名）
	private String is_group_tag = ""; // 群标签状态（0群和标签都没有、1群、2标签）
	private String business_id = ""; // 商家ID
	private String is_link = ""; // 外链状态（0没有链接、1外链出去、2商家）
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

	public ArrayList<MainItemBodyImgBean> getImg() {
		return img;
	}


	public void setImg(ArrayList<MainItemBodyImgBean> img) {
		this.img = img;
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

	public void setSave(boolean isSave) {
		this.isSave = isSave;
	}

	public void setIsSave(boolean isSave) {
		this.isSave = isSave;
	}
	
	public boolean isSave() {
		return isSave;
	}
	
	public String getFrom_url(){
		return from_url;
	}
	
	public void setFrom_url(String from_url){
		this.from_url = from_url;
	}
	
	public boolean isFriends() {
		return isFriends;
	}
	
	public void setFriends(boolean isFriends) {
		this.isFriends = isFriends;
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
	public ArrayList<MainItemBodyImgBean> getHandleList() {
		return handleList;
	}
	public void setHandleList(ArrayList<MainItemBodyImgBean> handleList) {
		this.handleList = handleList;
	}
	
	@Override
	public String toString() {
		return "MainItemBodyItemBean [img_id=" + img_id + ", user_id="
				+ user_id + ", user_name=" + user_name + ", avatar=" + avatar
				+ ", level_img=" + level_img + ", post_url=" + post_url
				+ ", create_time=" + create_time + ", group_id=" + group_id
				+ ", group_name=" + group_name + ", img_title=" + img_title
				+ ", description=" + description + ", is_admire=" + is_admire
				+ ", main_admire_count=" + main_admire_count + ", handleList="
				+ handleList + ", from_url=" + from_url + ", is_idol="
				+ is_idol + ", isFriends=" + isFriends + ", isSave=" + isSave
				+ ", group_tag_id=" + group_tag_id + ", group_tag_name="
				+ group_tag_name + ", is_group_tag=" + is_group_tag
				+ ", business_id=" + business_id + ", is_link=" + is_link
				+ ", tag_id=" + tag_id + "]";
	}

}
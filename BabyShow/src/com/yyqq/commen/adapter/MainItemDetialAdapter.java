package com.yyqq.commen.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.mob.MobSDK;
import com.yyqq.babyshow.R;
import com.yyqq.code.business.BusinessDetailActivity;
import com.yyqq.code.main.MainItemDetialActivity;
import com.yyqq.code.main.MainItemDetialWebActivity;
import com.yyqq.code.personal.PersonalCenterActivity;
import com.yyqq.code.personal.UserInfo;
import com.yyqq.code.postbar.PostBarActivity;
import com.yyqq.code.video.VideoDetialActivity;
import com.yyqq.commen.model.MainItemBodyImgBean;
import com.yyqq.commen.model.MainItemBodyItemBean;
import com.yyqq.commen.model.MainItemCommentBean;
import com.yyqq.commen.model.MainItemHintBean;
import com.yyqq.commen.model.PostBarTypeItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.GroupRelevantUtils;
import com.yyqq.commen.view.RecodeListView;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

@SuppressLint("NewApi")
public class MainItemDetialAdapter extends BaseAdapter {

	public static int clickPosition = 0;  // 点击的是第几个评论，用于回复评论时定位
	
	private Activity context = null;
	private LayoutInflater inflater = null;
	private MyApplication app = null;
	private MainItemBodyItemBean itemBean;
	private ArrayList<MainItemCommentBean> commentList;
	private ArrayList<MainItemHintBean> hintList;
	private AbHttpUtil ab;
	private EditText userInput;
	
	//分享的参数
	private String share_title = "";
	private String share_img = "";
	private String share_text = "";
	private String share_url = "";
	private String share_defaultImg = "";

	public MainItemDetialAdapter(Activity context, LayoutInflater inflater, MyApplication app, MainItemBodyItemBean itemBean,
			ArrayList<MainItemCommentBean> commentList, AbHttpUtil ab, ArrayList<MainItemHintBean> hintList, EditText userInput) {
		super();
		this.context = context;
		this.inflater = inflater;
		this.app = app;
		this.itemBean = itemBean;
		this.commentList = commentList;
		this.ab = ab;
		this.hintList = hintList;
		this.userInput = userInput;
	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public Object getItem(int position) {
		return itemBean.getHandleList().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		convertView = inflater.inflate(R.layout.item_main_detial01, null);

		// 头部 + 查看原文
		initTitle(convertView, itemBean);

		// 主体
		initBody(convertView, itemBean.getHandleList());

		// 点赞+分享+推荐列表
		initShared(convertView, itemBean.getMain_admire_count(), itemBean.getIs_admire());

		// 评论
		initComment(convertView, commentList, position);

		return convertView;
	}

	/**
	 * 标题部分
	 * */
	private void initTitle(View convertView, final MainItemBodyItemBean itemBean) {
		TextView item_detial_title = (TextView) convertView.findViewById(R.id.item_detial_title);
		TextView item_detial_username = (TextView) convertView.findViewById(R.id.item_detial_username);
		TextView item_detial_time = (TextView) convertView.findViewById(R.id.item_detial_time);
		TextView item_detial_from = (TextView) convertView.findViewById(R.id.item_detial_from);
		ImageView item_detial_icon = (ImageView) convertView.findViewById(R.id.item_detial_icon);
		final ImageView item_detial_adduser = (ImageView) convertView.findViewById(R.id.item_detial_adduser);
		RelativeLayout item_detial_to_from = (RelativeLayout) convertView.findViewById(R.id.item_detial_to_from);
		TextView item_detial_to_from_text = (TextView)convertView.findViewById(R.id.item_detial_to_from_text);
		ImageView item_detial_shared = (ImageView) convertView.findViewById(R.id.item_detial_shared);

		item_detial_shared.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showShare();
			}
		});
		
		/**
		 * 查看原文
		 * */
		if(itemBean.getIs_link().equals("0") || itemBean.getIs_link().isEmpty()){
			item_detial_to_from.setVisibility(View.GONE);
		}else{
			
			item_detial_to_from.setVisibility(View.VISIBLE);
			
			item_detial_to_from_text.setText(itemBean.getLink_name());
			
			item_detial_to_from.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					/**
					 * 外链
					 * */
					if(itemBean.getIs_link().equals("1")){
						Intent intent = new Intent(context, MainItemDetialWebActivity.class);
						intent.putExtra(MainItemDetialWebActivity.LINK_URL, itemBean.getFrom_url());
						context.startActivity(intent);
					}else{
						/**
						 * 商家详情
						 * */
						Intent intent = new Intent(context, BusinessDetailActivity.class);
						intent.putExtra("business_id", itemBean.getBusiness_id());
						context.startActivity(intent);
					}
				}
			});
		}
		
		/**
		 * 添加好友
		 * */
		if(itemBean.getIs_idol().equals("2") || null == itemBean.getUser_id()|| itemBean.getUser_id().equals(Config.getUser(context).uid+"")){
			item_detial_adduser.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_isadd));
			item_detial_adduser.setClickable(false);
		}else{
			item_detial_adduser.setVisibility(View.VISIBLE);
			
			if(itemBean.isFriends()){
				item_detial_adduser.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_isadd));
			}else{
				item_detial_adduser.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_noadd));
			}
			
			item_detial_adduser.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(itemBean.isFriends()){
						addToFriends(ServerMutualConfig.CancelFocus, itemBean, item_detial_adduser);
					}else{
						addToFriends(ServerMutualConfig.FocusOn, itemBean, item_detial_adduser);
					}
				}
			});
		}
		
		app.display(itemBean.getAvatar(), item_detial_icon, R.drawable.def_image);
		if (!"-999".equals(itemBean.getUser_id())) {
			item_detial_icon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					if (itemBean.getUser_id().equals(Config.getUser(context).uid)) {
						intent.setClass(context, PersonalCenterActivity.class);
						intent.putExtra("falg_return", true);
					} else {
						intent.setClass(context, UserInfo.class);
						intent.putExtra("uid", itemBean.getUser_id());
					}
					context.startActivity(intent);
				}
			});
		}
		
		item_detial_title.setText(itemBean.getImg_title());
		if(itemBean.getImg_title().isEmpty()){
			item_detial_title.setVisibility(View.GONE);
		}
		item_detial_username.setText(itemBean.getUser_name());
		item_detial_time.setText(itemBean.getCreate_time());

		if (TextUtils.isEmpty(itemBean.getGroup_tag_name()) || itemBean.getIs_group_tag().equals("0")) {
			item_detial_from.setVisibility(View.INVISIBLE);
		} else {
			item_detial_from.setVisibility(View.VISIBLE);
			item_detial_from.setText(itemBean.getGroup_tag_name());
			
			/**
			 * 文章来源
			 * 
			 * */
			item_detial_from.setOnClickListener(new OnClickListener() {
				
				private Object bean;

				@Override
				public void onClick(View arg0) {
					
					if(itemBean.getIs_group_tag().equals("1")){
						GroupRelevantUtils.CheckIntent(context, itemBean.getGroup_tag_id());
					}else if(itemBean.getIs_group_tag().equals("2")){
						Intent intent = new Intent();
						intent.setClass(context, PostBarActivity.class);
						intent.putExtra("img_title", itemBean.getImg_title());
						intent.putExtra("tag_id", itemBean.getGroup_tag_id());
						intent.putExtra("img_ids", itemBean.getImg_id());
						context.startActivity(intent);
					}else if(itemBean.getIs_group_tag().equals("3")){
						Intent intent = new Intent();
						intent.setClass(context, MainItemDetialActivity.class);
						intent.putExtra("img_id", itemBean.getGroup_tag_id());
						context.startActivity(intent);
					}else if(itemBean.getIs_group_tag().equals("4")){
						Intent intent = new Intent(context, VideoDetialActivity.class);
						PostBarTypeItem postBean = new PostBarTypeItem();
						postBean.setImg_id(itemBean.getImg_id());
						postBean.setUser_id(itemBean.getUser_id());
						postBean.setVideo_url(itemBean.getVideo_img_url());
						postBean.setImg(itemBean.getVideo_img_thumb());
						postBean.setImg_thumb_width(itemBean.getVideo_img_thumb_width());
						postBean.setImg_thumb_height(itemBean.getVideo_img_thumb_height());
						Bundle bun = new Bundle();
						bun.putSerializable(VideoDetialActivity.VIIDEO_INFO, postBean);
						intent.putExtras(bun);
						context.startActivity(intent);
					}
				}
			});
		}

	};

	/**
	 * 文章主体
	 * */
	private void initBody(View convertView,
			ArrayList<MainItemBodyImgBean> bodyList) {
		final RecodeListView main_item_02_list = (RecodeListView) convertView
				.findViewById(R.id.main_item_02_list);

		if (null != bodyList && bodyList.size() != 0) {

			ArrayList<String> imgList = new ArrayList<String>();

			for (int i = 0; i < bodyList.size(); i++) {
				if (null != bodyList.get(i).getImg_thumb()
						&& !bodyList.get(i).getImg_thumb().isEmpty()) {
					imgList.add(bodyList.get(i).getImg_thumb());
				}
			}

			MainItemBodyAdapter BodyAdapter = new MainItemBodyAdapter(context, inflater, app, bodyList, imgList);
			main_item_02_list.setAdapter(BodyAdapter);
		}

	};

	/**
	 * 分享+点赞
	 * */
	private void initShared(View convertView, String number, String is_admire) {
		ImageView item_shared_weixin = (ImageView) convertView.findViewById(R.id.item_shared_weixin);
		ImageView item_shared_pengyouquan = (ImageView) convertView.findViewById(R.id.item_shared_pengyouquan);
		LinearLayout item_is_zan = (LinearLayout) convertView.findViewById(R.id.item_is_zan); // 赞布局
		final ImageView item_is_zan_icon = (ImageView) convertView.findViewById(R.id.item_is_zan_icon); // 点赞图标
		TextView item_is_zan_number = (TextView) convertView.findViewById(R.id.item_is_zan_number);
		
		com.yyqq.commen.view.RecodeListView main_item_hint_list = (com.yyqq.commen.view.RecodeListView) convertView.findViewById(R.id.main_item_hint_list);
		MainItemHintAdapter hintAdapter = new MainItemHintAdapter(context, inflater, hintList);
		main_item_hint_list.setAdapter(hintAdapter);

		// 赞
		item_is_zan_number.setText(itemBean.getMain_admire_count());
		if ("1".equals(itemBean.getIs_admire())) {
			item_is_zan_icon.setBackgroundResource(R.drawable.comment_main_zan_yes);
		} else {
			item_is_zan_icon.setBackgroundResource(R.drawable.comment_main_zan_no);
		}

		item_is_zan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Config.showProgressDialog(context, false, null);
				if ("0".equals(itemBean.getIs_admire())) {
					AbRequestParams params = new AbRequestParams();
					params.put("login_user_id", Config.getUser(context).uid);
					params.put("admire_user_id", itemBean.getUser_id());
					params.put("img_id", itemBean.getImg_id());
					ab.post(ServerMutualConfig.PostAdmireNew, params,
							new AbStringHttpResponseListener() {
								@Override
								public void onSuccess(int statusCode,
										String content) {
									super.onSuccess(statusCode, content);
									Config.dismissProgress();
									itemBean.setMain_admire_count((Integer
											.parseInt(itemBean
													.getMain_admire_count()) + 1)
											+ "");
									itemBean.setIs_admire("1");
									item_is_zan_icon
											.setBackgroundResource(R.drawable.comment_main_zan_yes);
									notifyDataSetChanged();
								}

								@Override
								public void onFinish() {
									super.onFinish();
									Config.dismissProgress();
								}

								@Override
								public void onFailure(int statusCode,
										String content, Throwable error) {
									super.onFailure(statusCode, content, error);
									Config.showFiledToast(context);
								}
							});
				} else {
					AbRequestParams params = new AbRequestParams();
					params.put("login_user_id", Config.getUser(context).uid);
					params.put("admire_user_id", itemBean.getUser_id());
					params.put("img_id", itemBean.getImg_id());
					ab.post(ServerMutualConfig.CancelPostAdmireNew, params,
							new AbStringHttpResponseListener() {
								@Override
								public void onSuccess(int statusCode,
										String content) {
									super.onSuccess(statusCode, content);
									Config.dismissProgress();
									itemBean.setMain_admire_count((Integer
											.parseInt(itemBean
													.getMain_admire_count()) - 1)
											+ "");
									itemBean.setIs_admire("0");
									item_is_zan_icon
											.setBackgroundResource(R.drawable.comment_main_zan_no);
									notifyDataSetChanged();
								}

								@Override
								public void onFinish() {
									super.onFinish();
									Config.dismissProgress();
								}

								@Override
								public void onFailure(int statusCode,
										String content, Throwable error) {
									super.onFailure(statusCode, content, error);
									Config.showFiledToast(context);
								}
							});
				}
			}
		});

		item_shared_weixin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sharedWeixin();
			}
		});

		item_shared_pengyouquan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sharedPengyouquan();
			}
		});

	}

	/**
	 * 评论列表
	 * */
	private void initComment(View convertView, final ArrayList<MainItemCommentBean> commentList, final int index) {
		RecodeListView main_item_03_list = (RecodeListView) convertView.findViewById(R.id.main_item_03_list);

		if (null != commentList && commentList.size() != 0) {
			MainItemCommentAdapter CommentAdapte = new MainItemCommentAdapter(context, inflater, app, commentList, ab,userInput, index);
			main_item_03_list.setAdapter(CommentAdapte);
		}
	};

	private void sharedWeixin() {

		MobSDK.init(context);
		Wechat.ShareParams WechatSp = new Wechat.ShareParams();
		WechatSp.setShareType(Platform.SHARE_WEBPAGE);
		
		if(itemBean.getImg_title().isEmpty()){
			WechatSp.setTitle("自由环球租赁");
		}else{
			WechatSp.setTitle(itemBean.getImg_title());
		}
		
		if(itemBean.getDescription().isEmpty()){
			WechatSp.setText("精彩宝宝成长美一瞬间");
		}else{
			WechatSp.setText(itemBean.getDescription());
		}
		
		String share_img = "";
		
		for(int i = 0 ; i < itemBean.getHandleList().size() ; i++ ){
			if(null != itemBean.getHandleList().get(i).getImg_thumb() && !itemBean.getHandleList().get(i).getImg_thumb().equals("")){
				if(null == share_img || "".equals(share_img)){
					share_img = itemBean.getHandleList().get(i).getImg_thumb();
				}
			}
		}
		
		if(null == share_img){
			share_img = "";
		}
		
		if (TextUtils.isEmpty(share_img)) {
			WechatSp.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
		} else {
			WechatSp.setImageUrl(share_img);
		}
		WechatSp.setUrl("http://www.meimei.yihaoss.top/fenxiang/postbardetial.html?img_id=" + itemBean.getImg_id() + "&user_id=" + itemBean.getUser_id());
		Platform WechatSpShare = ShareSDK.getPlatform(Wechat.NAME);

		WechatSpShare.setPlatformActionListener(new PlatformActionListener() {

			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onComplete(Platform arg0, int arg1,
					HashMap<String, Object> arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		});
		WechatSpShare.share(WechatSp);
	}

	private void sharedPengyouquan() {

		MobSDK.init(context);
		WechatMoments.ShareParams wechatMomentsSp = new WechatMoments.ShareParams();
		wechatMomentsSp.setTitle(itemBean.getImg_title());
		// wechatMomentsSp.setTitleUrl("http://221.231.6.74:9000/shanghuo/app/xiazai.html");
		wechatMomentsSp.setText(itemBean.getHandleList().get(0)
				.getDescription());
		
		String share_img = "";
		
		for(int i = 0 ; i < itemBean.getHandleList().size() ; i++ ){
			if(null != itemBean.getHandleList().get(i).getImg_thumb() && !itemBean.getHandleList().get(i).getImg_thumb().equals("")){
				if(null == share_img || "".equals(share_img)){
					share_img = itemBean.getHandleList().get(i).getImg_thumb();
				}
			}
		}
		
		if(null == share_img){
			share_img = "";
		}
		
		if (TextUtils.isEmpty(share_img)) {
			wechatMomentsSp.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
		} else {
			wechatMomentsSp.setImageUrl(share_img);
		}
		
		wechatMomentsSp.setUrl("http://www.meimei.yihaoss.top/fenxiang/postbardetial.html?img_id=" + itemBean.getImg_id() + "&user_id=" + itemBean.getUser_id());
		wechatMomentsSp.setShareType(WechatMoments.SHARE_WEBPAGE);

		Platform wechatMomentsShare = ShareSDK.getPlatform(WechatMoments.NAME);

		wechatMomentsShare
				.setPlatformActionListener(new PlatformActionListener() {

					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onComplete(Platform arg0, int arg1,
							HashMap<String, Object> arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onCancel(Platform arg0, int arg1) {
						// TODO Auto-generated method stub

					}
				});
		wechatMomentsShare.share(wechatMomentsSp);
	}
	
	/**
	 * 添加或取消好友
	 * */
	private void addToFriends(String url, final MainItemBodyItemBean itemBean, final ImageView item_detial_adduser){
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
		params.put("idol_id", itemBean.getUser_id());
		ab.post(url, params, new AbStringHttpResponseListener() {
			@Override
			public void onSuccess(int statusCode,
					String content) {
				super.onSuccess(statusCode, content);
				try {
					JSONObject json = new JSONObject(content);
					if (json.getBoolean("success")) {
						if(itemBean.isFriends()){
							itemBean.setFriends(false);
						}else{
							itemBean.setFriends(true);
						}
					}
					if(itemBean.isFriends()){
						item_detial_adduser.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_isadd));
					}else{
						item_detial_adduser.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_noadd));
					}
					
					Toast.makeText(context,json.getString("reMsg"),Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				Config.dismissProgress();
			}
			
			@Override
			public void onFailure(int statusCode,
					String content, Throwable error) {
				super.onFailure(statusCode, content, error);
				Config.showFiledToast(context);
			}
		});
	}
	
	@SuppressLint("NewApi")
	private void showShare() {
		share_title = itemBean.getImg_title();
		
		for(int i = 0 ; i < itemBean.getHandleList().size() ; i++ ){
			if(null != itemBean.getHandleList().get(i).getImg_thumb() && !itemBean.getHandleList().get(i).getImg_thumb().equals("")){
				if(null == share_img || "".equals(share_img)){
					share_img = itemBean.getHandleList().get(i).getImg_thumb();
				}
			}
		}
		
		if(null == share_img){
			share_img = "";
		}
		
		share_text = itemBean.getHandleList().get(0).getDescription();
		share_url = "http://www.meimei.yihaoss.top/fenxiang/postbardetial.html?img_id="+ itemBean.getImg_id() + "&user_id=" + itemBean.getUser_id();
		share_defaultImg = "https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg";
		MobSDK.init(context);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		oks.setSilent(true);
//		oks.setNotification(R.drawable.icon, ((Context) context).getString(R.string.app_name)); // 分享时Notification的图标和文字
		// 去除注释，演示在九宫格设置自定义的图标
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {

			@Override
			public void onShare(Platform platform, ShareParams paramsToShare) {
				if ("Wechat".equals(platform.getName())) {
					paramsToShare.setShareType(Platform.SHARE_TEXT);
					if(share_title.isEmpty()){
						paramsToShare.setTitle("自由环球租赁");
					}else{
						paramsToShare.setTitle(share_title);
					}
					
					if(itemBean.getDescription().isEmpty()){
						paramsToShare.setText("精彩宝宝成长美一瞬间");
					}else{
						paramsToShare.setText(itemBean.getDescription());
					}
					
					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare.setImageUrl(share_defaultImg);
					} else {
						paramsToShare.setImageUrl(share_img);
					}
					paramsToShare.setUrl(share_url);
				} else if ("SinaWeibo".equals(platform.getName())) {
					paramsToShare.setText(share_title+ share_url);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare.setImageUrl(share_defaultImg);
					} else {
						paramsToShare.setImageUrl(share_img);
					}
				} else if ("QZone".equals(platform.getName())) {
					paramsToShare.setTitle(share_title); // title微信、QQ空间
					paramsToShare
							.setTitleUrl(share_url);
					paramsToShare.setSite(((Context) context).getString(R.string.app_name)); // site是分享此内容的网站名称，仅在QQ空间使用
					paramsToShare.setText(share_text);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare.setImageUrl(share_defaultImg);
					} else {
						paramsToShare.setImageUrl(share_img);
					}
					paramsToShare.setSiteUrl(share_url); // siteUrl是分享此内容的网站地址，仅在QQ空间使用
				} else if ("QQ".equals(platform.getName())) {
					paramsToShare.setTitle(share_title);
					paramsToShare.setTitleUrl(share_url);
					paramsToShare.setText(share_text);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare.setImageUrl(share_defaultImg);
					} else {
						paramsToShare.setImageUrl(share_img);
					}
				} else if ("WechatMoments".equals(platform.getName())) {
					paramsToShare.setShareType(Platform.SHARE_TEXT);
					paramsToShare.setTitle(share_title);
					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare.setImageUrl(share_defaultImg);
					} else {
						paramsToShare.setImageUrl(share_img);
					}
					paramsToShare.setUrl(share_url);
				}

			}
		});
		// 启动分享GUI
		oks.show(context);
	}
	
}
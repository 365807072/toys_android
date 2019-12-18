package com.yyqq.commen.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import com.mob.MobSDK;
import com.yyqq.babyshow.R;
import com.yyqq.code.group.SuperGroupActivity;


/**
 * 群相关utils 
 * 
 * 跳转 、分享
 * 
 * */
public class GroupRelevantUtils {

	/**
	 * 检查是否跳转至新版群首页
	 * 
	 * */
	public static void CheckIntent(final Context context, final String group_id){
		
		Intent intent = new Intent(context, SuperGroupActivity.class);
		intent.putExtra(SuperGroupActivity.GROUP_ID, group_id);
		context.startActivity(intent);
		
//		AbRequestParams params = new AbRequestParams();
//		params.put("group_id", group_id);
//		Config.showProgressDialog(context, false, null);
//		AbHttpUtil.getInstance(context).get(ServerMutualConfig.groupState + "&" + params.toString(), new AbStringHttpResponseListener() {
//					@Override
//					public void onSuccess(int statusCode,
//							String content) {
//						super.onSuccess(statusCode, content);
//						Config.dismissProgress();
//						try {
//							JSONObject json = new JSONObject(content);
//							if (json.getBoolean("success") && json.getJSONObject("data").getString("is_new").equals("1")) {
//								Intent intent = new Intent(context, SuperGroupActivity.class);
//								intent.putExtra(SuperGroupActivity.GROUP_ID, group_id);
//								context.startActivity(intent);
//							}else{
//								Intent intent = new Intent(context, GroupMainActivity.class);
//								intent.putExtra("group_id", group_id);
//								context.startActivity(intent);
//							}
//						} catch (JSONException e) {
//							Intent intent = new Intent(context, GroupMainActivity.class);
//							intent.putExtra("group_id", group_id);
//							context.startActivity(intent);
//						}
//					}
//
//					@Override
//					public void onFinish() {
//						super.onFinish();
//						Config.dismissProgress();
//					}
//
//					@Override
//					public void onFailure(int statusCode,
//							String content, Throwable error) {
//						super.onFailure(statusCode, content, error);
//						Config.dismissProgress();
//						Intent intent = new Intent(context, GroupMainActivity.class);
//						intent.putExtra("group_id", group_id);
//						context.startActivity(intent);
//					}
//				});
	}
	
	
	/**
	 * 群分享
	 * 
	 * */
	public static void showGroupShared(final Context context, String user_id, String group_id, String title, String description, String img_url){
		
		final String share_title = title;
		final String share_description = description;
		final String share_img = img_url;
		final String share_url = "http://www.meimei.yihaoss.top/groupShare/groupIndex.html?group_id="+ group_id + "&login_user_id=" + user_id;
		final String share_defaultImg = "https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg";
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
					
					if(share_description.isEmpty()){
						paramsToShare.setText("精彩宝宝成长美一瞬间");
					}else{
						paramsToShare.setText(share_description);
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
					paramsToShare.setTitleUrl(share_url);
					paramsToShare.setSite((context).getString(R.string.app_name)); // site是分享此内容的网站名称，仅在QQ空间使用
					paramsToShare.setText(share_description);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare.setImageUrl(share_defaultImg);
					} else {
						paramsToShare.setImageUrl(share_img);
					}
					paramsToShare.setSiteUrl(share_url); // siteUrl是分享此内容的网站地址，仅在QQ空间使用
				} else if ("QQ".equals(platform.getName())) {
					paramsToShare.setTitle(share_title);
					paramsToShare.setTitleUrl(share_url);
					paramsToShare.setText(share_description);
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

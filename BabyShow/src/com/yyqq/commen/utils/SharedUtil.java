package com.yyqq.commen.utils;

import android.app.Activity;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import com.yyqq.babyshow.R;

/**
 *  分享util(弃用于2016.08.24)
 * */
public class SharedUtil {
	
//	//分享的参数
//	
//	public static void showShare(Activity context, String share_title, String share_img, String share_text, String share_url, String share_defaultImg) {
//		share_url = "http://www.meimei.yihaoss.top/share_post.php?img_id="
//				+ itemId + "&user_id=" + userid;
//		share_defaultImg = "https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg";
//		ShareSDK.initSDK(this);
//		OnekeyShare oks = new OnekeyShare();
//		// 关闭sso授权
//		oks.disableSSOWhenAuthorize();
//		oks.setSilent(true);
//		oks.setNotification(R.drawable.icon, getString(R.string.app_name)); // 分享时Notification的图标和文字
//		// 去除注释，演示在九宫格设置自定义的图标
//		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
//
//			@Override
//			public void onShare(Platform platform, ShareParams paramsToShare) {
//				if ("Wechat".equals(platform.getName())) {
//					paramsToShare.setShareType(Platform.SHARE_TEXT);
//					paramsToShare.setText(share_title);
//					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
//					if (TextUtils.isEmpty(share_img)) {
//						paramsToShare.setImageUrl(share_defaultImg);
//					} else {
//						paramsToShare.setImageUrl(share_img);
//					}
//					paramsToShare.setUrl(share_url);
//				} else if ("SinaWeibo".equals(platform.getName())) {
//					paramsToShare.setText(share_title+ share_url);
//					if (TextUtils.isEmpty(share_img)) {
//						paramsToShare.setImageUrl(share_defaultImg);
//					} else {
//						paramsToShare.setImageUrl(share_img);
//					}
//				} else if ("QZone".equals(platform.getName())) {
//					paramsToShare.setTitle(share_title); // title微信、QQ空间
//					paramsToShare
//							.setTitleUrl(share_url);
//					paramsToShare.setSite(getString(R.string.app_name)); // site是分享此内容的网站名称，仅在QQ空间使用
//					paramsToShare.setText(share_text);
//					if (TextUtils.isEmpty(share_img)) {
//						paramsToShare.setImageUrl(share_defaultImg);
//					} else {
//						paramsToShare.setImageUrl(share_img);
//					}
//					paramsToShare.setSiteUrl(share_url); // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//				} else if ("QQ".equals(platform.getName())) {
//					paramsToShare.setTitle(share_title);
//					paramsToShare.setTitleUrl(share_url);
//					paramsToShare.setText(share_text);
//					if (TextUtils.isEmpty(share_img)) {
//						paramsToShare.setImageUrl(share_defaultImg);
//					} else {
//						paramsToShare.setImageUrl(share_img);
//					}
//				} else if ("WechatMoments".equals(platform.getName())) {
//					paramsToShare.setShareType(Platform.SHARE_TEXT);
//					paramsToShare.setTitle(share_title);
//					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
//					if (TextUtils.isEmpty(share_img)) {
//						paramsToShare.setImageUrl(share_defaultImg);
//					} else {
//						paramsToShare.setImageUrl(share_img);
//					}
//					paramsToShare.setUrl(share_url);
//				}
//
//			}
//		});
//		// 启动分享GUI
//		oks.show(this);
//	}
}

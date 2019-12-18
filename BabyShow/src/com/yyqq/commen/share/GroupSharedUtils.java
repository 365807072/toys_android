package com.yyqq.commen.share;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.mob.MobSDK;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainItemDetialWebActivity;
import com.yyqq.code.toyslease.ToysLeaseMainTabActivity;
import com.yyqq.commen.utils.Config;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * 分享
 */
public class GroupSharedUtils {

	public static String intentUrl = "";
	
	/**
	 * 分享Utils
	 * */
	public static void SharedGroup(final groupShareBean bean, String str) {
		intentUrl = str;
		GroupSharedUtils.SharedGroup(bean);
	}
	
	
	/**
	 * 分享Utils
	 * */
	public static void SharedGroup(final groupShareBean bean) {

		MobSDK.init(bean.getContext());
		OnekeyShare oks = new OnekeyShare();
		if(bean.isMore()){
			oks.disableSSOWhenAuthorize();
			oks.setSilent(true);
			oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
				
				@Override
				public void onShare(Platform platform, ShareParams paramsToShare) {
					if ("Wechat".equals(platform.getName())) {
						
						// 默认分享类型为微信小程序，如path为空转为分享html
						paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
						if(bean.getWxPath().equals("")){
							paramsToShare.setShareType(Platform.SHARE_WEBPAGE);	
						}else{
							paramsToShare.setShareType(Platform.SHARE_WXMINIPROGRAM);
							paramsToShare.setWxUserName("gh_740dc1537e7c");
							paramsToShare.setWxPath(bean.getWxPath());
						}
						
						paramsToShare.setUrl(bean.getShare_url());
						
						if (bean.getShare_title().equals("")) {
							paramsToShare.setTitle("自由环球租赁");
						} else {
							paramsToShare.setTitle(bean.getShare_title());
						}
						if (bean.getShare_text().equals("")) {
							paramsToShare.setText("精彩宝宝成长美一瞬间");
						} else {
							paramsToShare.setText(bean.getShare_text());
						}
						if (TextUtils.isEmpty(bean.getShare_img())) {
							paramsToShare.setImageUrl(bean.getShare_defaultImg());
						} else {
							paramsToShare.setImageUrl(bean.getShare_img());
						}
					} else if ("SinaWeibo".equals(platform.getName())) {
						paramsToShare.setText(bean.getShare_title() + bean.getShare_url());
						if (TextUtils.isEmpty(bean.getShare_img())) {
							paramsToShare.setImageUrl(bean.getShare_defaultImg());
						} else {
							paramsToShare.setImageUrl(bean.getShare_img());
						}
					} else if ("QZone".equals(platform.getName())) {
						paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
						paramsToShare.setTitle(bean.getShare_title()); // title微信、QQ空间
						paramsToShare.setTitleUrl(bean.getShare_url());
						paramsToShare.setSite((bean.getContext()).getString(R.string.app_name)); // site是分享此内容的网站名称，仅在QQ空间使用
						paramsToShare.setText(bean.getShare_text());
						if (TextUtils.isEmpty(bean.getShare_img())) {
							paramsToShare.setImageUrl(bean.getShare_defaultImg());
						} else {
							paramsToShare.setImageUrl(bean.getShare_img());
						}
						paramsToShare.setSiteUrl(bean.getShare_url()); // siteUrl是分享此内容的网站地址，仅在QQ空间使用
					} else if ("QQ".equals(platform.getName())) {
						paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
						paramsToShare.setTitle(bean.getShare_title());
						paramsToShare.setTitleUrl(bean.getShare_url());
						paramsToShare.setText(bean.getShare_text());
						if (TextUtils.isEmpty(bean.getShare_img())) {
							paramsToShare.setImageUrl(bean.getShare_defaultImg());
						} else {
							paramsToShare.setImageUrl(bean.getShare_img());
						}
					} else if ("WechatMoments".equals(platform.getName())) {
						paramsToShare.setTitle(bean.getShare_title());
						paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
						if (TextUtils.isEmpty(bean.getShare_img())) {
							paramsToShare.setImageUrl(bean.getShare_defaultImg());
						} else {
							paramsToShare.setImageUrl(bean.getShare_img());
						}
						paramsToShare.setUrl(bean.getShare_url());
					}
					
				}
			});
		}else{
			oks.addHiddenPlatform(QQ.NAME);
			oks.addHiddenPlatform(Wechat.NAME);
			oks.addHiddenPlatform(Wechat.NAME);
			oks.addHiddenPlatform(SinaWeibo.NAME);
			oks.addHiddenPlatform(QZone.NAME);
			oks.disableSSOWhenAuthorize();
			oks.setSilent(true);
			if (bean.getShare_title().equals("")) {
				oks.setTitle("自由环球租赁");
			} else {
				oks.setTitle(bean.getShare_title());
			}
			oks.setUrl(bean.getShare_url());
			if (bean.getShare_text().equals("")) {
				oks.setText("精彩宝宝成长美一瞬间");
			} else {
				oks.setText(bean.getShare_text());
			}
			if (TextUtils.isEmpty(bean.getShare_img())) {
				oks.setImageUrl(bean.getShare_defaultImg());
			} else {
				oks.setImageUrl(bean.getShare_img());
			}
			oks.setPlatform(WechatMoments.NAME);
		}
		
		if(!intentUrl.equals("")){
			oks.setCallback(new PlatformActionListener() {
				@Override
				public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
					Message msg = new Message();
					msg.what = 0;
					msg.obj = platform;
					new Handler(Looper.getMainLooper(), new Handler.Callback() {
						@Override
						public boolean handleMessage(Message msg) {
							Intent intent = new Intent(bean.getContext(), MainItemDetialWebActivity.class);
							intent.putExtra(MainItemDetialWebActivity.LINK_URL, intentUrl);
							bean.getContext().startActivity(intent);
							return false;
						}
					}).sendMessage(msg);
					
				}
				
				@Override
				public void onError(Platform platform, int i, final Throwable throwable) {
					new Handler(Looper.getMainLooper()).post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(bean.getContext(), "分享失败" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
					
				}
				
				@Override
				public void onCancel(Platform platform, int i) {
					new Handler(Looper.getMainLooper()).post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(bean.getContext(), "分享取消", Toast.LENGTH_SHORT).show();
							
						}
					});
					
				}
			});
		}
		
		if(ToysLeaseMainTabActivity.sharedCount){
			oks.setCallback(new PlatformActionListener() {
				@Override
				public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
					Message msg = new Message();
					msg.what = 0;
					msg.obj = platform;
					new Handler(Looper.getMainLooper(), new Handler.Callback() {
						@Override
						public boolean handleMessage(Message msg) {
							sendCount(bean);
							return false;
						}
					}).sendMessage(msg);
					
				}
				
				@Override
				public void onError(Platform platform, int i, final Throwable throwable) {
					new Handler(Looper.getMainLooper()).post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(bean.getContext(), "分享失败" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
					
				}
				
				@Override
				public void onCancel(Platform platform, int i) {
					new Handler(Looper.getMainLooper()).post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(bean.getContext(), "分享取消", Toast.LENGTH_SHORT).show();
							
						}
					});
					
				}
			});
		}
		
		
		// 启动分享GUI
		oks.show(bean.getContext());
	}
	
	/**
	 * 分享群
	 */
	public static void SharedGroupMore(final groupShareBean bean) {

		MobSDK.init(bean.getContext());
		OnekeyShare oks = new OnekeyShare();
		oks.disableSSOWhenAuthorize();
		oks.setSilent(true);
//		oks.setNotification(R.drawable.icon, (bean.getContext()).getString(R.string.app_name)); // 分享时Notification的图标和文字
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {

			@Override
			public void onShare(Platform platform, ShareParams paramsToShare) {
				if ("Wechat".equals(platform.getName())) {
					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
					if (bean.getShare_title().equals("")) {
						paramsToShare.setTitle("自由环球租赁");
					} else {
						paramsToShare.setTitle(bean.getShare_title());
					}

					if (bean.getShare_text().equals("")) {
						paramsToShare.setText("精彩宝宝成长美一瞬间");
					} else {
						paramsToShare.setText(bean.getShare_text());
					}

					if (TextUtils.isEmpty(bean.getShare_img())) {
						paramsToShare.setImageUrl(bean.getShare_defaultImg());
					} else {
						paramsToShare.setImageUrl(bean.getShare_img());
					}
					paramsToShare.setUrl(bean.getShare_url());
				} else if ("SinaWeibo".equals(platform.getName())) {
//					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
					paramsToShare.setText(bean.getShare_title() + bean.getShare_url());
					if (TextUtils.isEmpty(bean.getShare_img())) {
						paramsToShare.setImageUrl(bean.getShare_defaultImg());
					} else {
//						paramsToShare.setImageUrl(bean.getShare_defaultImg());
						paramsToShare.setImageUrl(bean.getShare_img());
					}
				} else if ("QZone".equals(platform.getName())) {
					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
					paramsToShare.setTitle(bean.getShare_title()); // title微信、QQ空间
					paramsToShare.setTitleUrl(bean.getShare_url());
					paramsToShare.setSite((bean.getContext()).getString(R.string.app_name)); // site是分享此内容的网站名称，仅在QQ空间使用
					paramsToShare.setText(bean.getShare_text());
					if (TextUtils.isEmpty(bean.getShare_img())) {
						paramsToShare.setImageUrl(bean.getShare_defaultImg());
					} else {
						paramsToShare.setImageUrl(bean.getShare_img());
					}
					paramsToShare.setSiteUrl(bean.getShare_url()); // siteUrl是分享此内容的网站地址，仅在QQ空间使用
				} else if ("QQ".equals(platform.getName())) {
					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
					paramsToShare.setTitle(bean.getShare_title());
					paramsToShare.setTitleUrl(bean.getShare_url());
					paramsToShare.setText(bean.getShare_text());
					if (TextUtils.isEmpty(bean.getShare_img())) {
						paramsToShare.setImageUrl(bean.getShare_defaultImg());
					} else {
						paramsToShare.setImageUrl(bean.getShare_img());
					}
				} else if ("WechatMoments".equals(platform.getName())) {
					paramsToShare.setTitle(bean.getShare_title());
					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
					if (TextUtils.isEmpty(bean.getShare_img())) {
						paramsToShare.setImageUrl(bean.getShare_defaultImg());
					} else {
						paramsToShare.setImageUrl(bean.getShare_img());
					}
					paramsToShare.setUrl(bean.getShare_url());
				}

			}
		});

		if(!intentUrl.equals("")){
			oks.setCallback(new PlatformActionListener() {
				@Override
				public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
					Message msg = new Message();
					msg.what = 0;
					msg.obj = platform;
					new Handler(Looper.getMainLooper(), new Handler.Callback() {
						@Override
						public boolean handleMessage(Message msg) {
//							Toast.makeText(bean.getContext(), "分享成功", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(bean.getContext(), MainItemDetialWebActivity.class);
							intent.putExtra(MainItemDetialWebActivity.LINK_URL, intentUrl);
							bean.getContext().startActivity(intent);
							return false;
						}
					}).sendMessage(msg);
					
				}
				
				@Override
				public void onError(Platform platform, int i, final Throwable throwable) {
					new Handler(Looper.getMainLooper()).post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(bean.getContext(), "分享失败" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
					
				}
				
				@Override
				public void onCancel(Platform platform, int i) {
					new Handler(Looper.getMainLooper()).post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(bean.getContext(), "分享取消", Toast.LENGTH_SHORT).show();
							
						}
					});
					
				}
			});
		}
		
		if(ToysLeaseMainTabActivity.sharedCount){
			oks.setCallback(new PlatformActionListener() {
				@Override
				public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
					Message msg = new Message();
					msg.what = 0;
					msg.obj = platform;
					new Handler(Looper.getMainLooper(), new Handler.Callback() {
						@Override
						public boolean handleMessage(Message msg) {
							sendCount(bean);
							return false;
						}
					}).sendMessage(msg);
					
				}
				
				@Override
				public void onError(Platform platform, int i, final Throwable throwable) {
					new Handler(Looper.getMainLooper()).post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(bean.getContext(), "分享失败" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
					
				}
				
				@Override
				public void onCancel(Platform platform, int i) {
					new Handler(Looper.getMainLooper()).post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(bean.getContext(), "分享取消", Toast.LENGTH_SHORT).show();
							
						}
					});
					
				}
			});
		}
		
		
		
		
		// 启动分享GUI
		oks.show(bean.getContext());
	}

	/**
	 * 分享成功计数
	 * */
	private static void sendCount(groupShareBean bean){
		
		if(!ToysLeaseMainTabActivity.sharedCount){
			return;
		}else{
			ToysLeaseMainTabActivity.sharedCount = false;
		}
		
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(bean.getContext()).uid);
		AbHttpUtil.getInstance(bean.getContext()).get(ServerMutualConfig.SEND_SHARED_COUNT + "&" + params.toString(), new AbStringHttpResponseListener() {
					
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
					}

					@Override
					public void onFinish() {
						super.onFinish();
						Config.dismissProgress();
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
						Config.dismissProgress();
					}
				});
	}
	
	
	/**
	 * 分享需要的参数
	 * 
	 */
	public static class groupShareBean {

		private Context context;
		private boolean isMore = true;
		private String share_title = "";
		private String share_img = "";
		private String share_text = "";
		private String share_url = "";
		private String wxPath = "";
		private String wxUsername = "";
		private String share_defaultImg = "https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg";

		public groupShareBean() {
		}

		public Context getContext() {
			return context;
		}
		
		public boolean isMore() {
			return isMore;
		}

		public void setMore(boolean isMore) {
			this.isMore = isMore;
		}

		public String getWxPath() {
			return wxPath;
		}

		public void setWxPath(String wxPath) {
			this.wxPath = wxPath;
		}

		public String getWxUsername() {
			return wxUsername;
		}

		public void setWxUsername(String wxUsername) {
			this.wxUsername = wxUsername;
		}

		public void setContext(Context context) {
			this.context = context;
		}

		public String getShare_title() {
			return share_title;
		}

		public void setShare_title(String share_title) {
			this.share_title = share_title;
		}

		public String getShare_img() {
			return share_img;
		}

		public void setShare_img(String share_img) {
			this.share_img = share_img;
		}

		public String getShare_text() {
			return share_text;
		}

		public void setShare_text(String share_text) {
			this.share_text = share_text;
		}

		public String getShare_url() {
			return share_url;
		}

		public void setShare_url(String share_url) {
			this.share_url = share_url;
		}

		public String getShare_defaultImg() {
			return share_defaultImg;
		}

		public void setShare_defaultImg(String share_defaultImg) {
			this.share_defaultImg = share_defaultImg;
		}

	}

}
package com.yyqq.commen.utils;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.yyqq.code.login.AddBaby;
import com.yyqq.code.login.RegisterBandPhoneActivity;
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.code.main.InitActivity;
import com.yyqq.code.main.MainTab;
import com.yyqq.commen.jpush.JpushUtil;
import com.yyqq.commen.service.SystemService;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

// 更新用户登录状态及其它登录相关接口实现
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class LoginUtils {
	
	public static boolean isReturnToMain = false;
	private static AbHttpUtil abhttp;

	// 更新用户名、密码（为空字符时表示未登录）
	public static void setUserLoginType(Context context, String userName, String Pwd){
		SharedPreferences sp = context.getSharedPreferences("babyshow_login",Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString("username", userName);
		edit.putString("password", Pwd);
		edit.commit();
	}
	
	// 获取登录状态
	public static boolean getUserLoginType(Context context){
		SharedPreferences sp = context.getSharedPreferences("babyshow_login",Context.MODE_PRIVATE);
		if(sp.getString("username", "").isEmpty() && sp.getString("password", "").isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
	// 获取用户名
	public static String getUserName(Context context){
		return context.getSharedPreferences("babyshow_login",Context.MODE_PRIVATE).getString("username", "");
	}
	
	// 获取密码
	public static String getUserPwd(Context context){
		return context.getSharedPreferences("babyshow_login",Context.MODE_PRIVATE).getString("password", "");
	}
	
	// 保存历史登录账号、密码
	public static void saveHistoryUserName(Context context, String userName, String userPwd, String userIconUrl){
		SharedPreferences sp = context.getSharedPreferences("babyshow_login_history",Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString("history_userName", userName);
		edit.putString("history_pwd", userPwd);
		setUserIconUrl(context, userIconUrl);
		edit.commit();
	}
	
	// 获取保存历史登录账号
	public static String getHistoryUserName(Context context){
		return context.getSharedPreferences("babyshow_login_history",Context.MODE_PRIVATE).getString("history_userName", "");
	}
	
	// 获取保存历史登录账号
	public static String getHistoryUserPwd(Context context){
		return context.getSharedPreferences("babyshow_login_history",Context.MODE_PRIVATE).getString("history_pwd", "");
	}
	
	// 保存头像地址
	public static void setUserIconUrl(Context context, String userIconUrl){
		SharedPreferences sp = context.getSharedPreferences("babyshow_login_history",Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString("iconUrl", userIconUrl);
		edit.commit();
	}
	
	// 获取头像地址
	public static String getUserIconUrl(Context context){
		return context.getSharedPreferences("babyshow_login_history",Context.MODE_PRIVATE).getString("iconUrl", "");
	}

	// 获取设备号
	private static String getDeviceId(Activity context){
		return ((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId() +"";
	}
	
	// 用户登录
	public static void userLogin(final Activity context, final String userName, final String pwd){
		Config.showProgressDialog(context, false, null);
		MyApplication.setVisitor("1");
		abhttp = AbHttpUtil.getInstance(context);
		abhttp.setDebug(Log.isDebug);
		AbRequestParams params = new AbRequestParams();
		params.put("user_name", userName);
		if(pwd.length() == 32){ // 加密后的密码为32位，当密码为32位的情况下可以看作此密码是本地存储的加密后的密码，故不需要再次加密
			params.put("password", pwd);
		}else{
			params.put("password", Config.MD5(pwd));
		}
		params.put("imin", getDeviceId(context));
		params.put("mapsign", GPSLocationUtils.getLatitude(context) + "," + GPSLocationUtils.getLongitude(context));
		abhttp.post(ServerMutualConfig.Login, params, new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Config.dismissProgress();
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								MyApplication.setVisitor("0");
								
								if(pwd.length() == 32){ // 加密后的密码为32位，当密码为32位的情况下可以看作此密码是本地存储的加密后的密码，故不需要再次加密
									LoginUtils.setUserLoginType(context, userName, pwd);
									LoginUtils.saveHistoryUserName(context, userName, pwd, json.getJSONObject("data").getString("avatar"));
								}else{
									LoginUtils.setUserLoginType(context, userName, Config.MD5(pwd));
									LoginUtils.saveHistoryUserName(context, userName, Config.MD5(pwd), json.getJSONObject("data").getString("avatar"));
								}
								SharedPreferences sp_user = context.getSharedPreferences("babyshow_user",Context.MODE_PRIVATE);
								Editor edit_user = sp_user.edit();
								edit_user.putString("user", content);
								edit_user.commit();
//								setAliasTag(context, json.getJSONObject("data").optString("user_id"), json.getJSONObject("data").optString("city", "北京"));
								SharedPreferences sf_check = context.getSharedPreferences("isFirst", 0);
								context.startService(new Intent(context,SystemService.class));
								
								// 通知html，登录成功
								if(WebLoginActivity.isSendMassage){
//									WebLoginActivity.webView.loadUrl("javascript:LoginSucessMassage('" + Config.getUser(context).uid + "')"); // 发送给页面用户信息
									WebLoginActivity.isSendMassage = false;
									WebLoginActivity.webLoginActivity.finish();
								}else{
									// 是否第一次进来了
									if (0 == sf_check.getInt("firstedit", 0)) {
										SharedPreferences sf = context.getSharedPreferences("isFirst", 0);
										Editor edit_sf = sf.edit();
										edit_sf.putInt("firstedit", 1);
										edit_sf.commit();
										context.startActivity(new Intent(context, InitActivity.class));
										WebLoginActivity.webLoginActivity.finish();
									}else{
//									MyApplication.getInstance().stopAll();
										if(isReturnToMain){
											Intent intent = new Intent();
											intent.setClass(context, MainTab.class);
											intent.putExtra("lat", GPSLocationUtils.getLatitude(context));
											intent.putExtra("lon", GPSLocationUtils.getLongitude(context));
											context.startActivity(intent);
										}
										WebLoginActivity.webLoginActivity.finish();
									}								
								}
								
							} else {
								LoginUtils.setUserLoginType(context,"","");
								Toast.makeText(context, json.getString("reMsg"), Toast.LENGTH_SHORT).show();
							}
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
						Config.dismissProgress();
					}
				});
	}
	
	// 用户注册
	public static void userRegister(final Context context, final String userName, final String pwd){
		Config.showProgressDialog(context, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("mobile", userName);
		params.put("password", Config.MD5(pwd));
		params.put("confirm_password", Config.MD5(pwd));
		params.put("mapsign", GPSLocationUtils.getLatitude(context) + "," + GPSLocationUtils.getLongitude(context));
		abhttp = AbHttpUtil.getInstance(context);
		abhttp.post(ServerMutualConfig.RegistMobile, params, new AbStringHttpResponseListener() {
					// 获取数据成功会调用这里
					@Override
					public void onSuccess(int statusCode, String content) {
						Config.dismissProgress();
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								MyApplication.setVisitor("0");
								if(pwd.length() == 32){ // 加密后的密码为32位，当密码为32位的情况下可以看作此密码是本地存储的加密后的密码，故不需要再次加密
									LoginUtils.setUserLoginType(context, userName, pwd);
									LoginUtils.saveHistoryUserName(context, userName, pwd, json.getJSONObject("data").getString("avatar"));
								}else{
									LoginUtils.setUserLoginType(context, userName, Config.MD5(pwd));
									LoginUtils.saveHistoryUserName(context, userName, Config.MD5(pwd), json.getJSONObject("data").getString("avatar"));
								}
								SharedPreferences sp = context.getSharedPreferences("babyshow_login", Context.MODE_PRIVATE);
								Editor edit = sp.edit();
								edit.putString("username", userName);
								edit.putString("password", Config.MD5(pwd));
								edit.commit();
								SharedPreferences sp_user = context .getSharedPreferences("babyshow_user", Context.MODE_PRIVATE);
								Editor edit_user = sp_user.edit();
								edit_user.putString("user", content);
								edit_user.commit();
								setAliasTag(context, json.getJSONObject("data").optString("user_id"), json.getJSONObject("data").optString("city", "北京"));
								
								// 通知html，登录成功
								if(WebLoginActivity.isSendMassage){
//									WebLoginActivity.webView.loadUrl("javascript:LoginSucessMassage('" + Config.getUser(context).uid+"" + "')"); // 发送给页面用户信息
									WebLoginActivity.isSendMassage = false;
									WebLoginActivity.webLoginActivity.finish();
								}else{
									context.startActivity(new Intent(context, AddBaby.class));
									WebLoginActivity.webLoginActivity.finish();
								}
							} else {
								MyApplication.setVisitor("1");
								Toast.makeText(context,json.getString("reMsg"),Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					};

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
	
	// 用户重置密码
	public static void userResetPwd(final Activity context, final String userName, final String pwd){
		Config.showProgressDialog(context, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("mobile", userName);
		params.put("password", Config.MD5(pwd));
		params.put("confirm_password", Config.MD5(pwd));
		abhttp = AbHttpUtil.getInstance(context);
		abhttp.post(ServerMutualConfig.EditPassword, params, new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						Config.dismissProgress();
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								LoginUtils.userLogin(context, userName, Config.MD5(pwd));
							} else {
								Toast.makeText(context, json.getString("reMsg"), Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					};

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
	
	 // 第三方登录
	private static long i = 0;
	private static Activity authorContext;
	private static String str = "";
	
	public static void authorUserLogin(Activity context, Platform plat){
//		Config.showProgressDialog(context, false, null);
		authorContext = context;
		i = System.currentTimeMillis();
		plat.setPlatformActionListener(authorUserLoginListener);

		plat.SSOSetting(false); // true是客户端，false是网页版
		plat.showUser(null);
	}

	// 第三方登录回调
	private static PlatformActionListener authorUserLoginListener = new PlatformActionListener() {

		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2) {
			Config.dismissProgress();
		}

		@Override
		public void onComplete(final Platform arg0, int arg1,
				HashMap<String, Object> arg2) {
			authorContext.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if(arg0.getName().toLowerCase().contains("sina")){
						str = "1";
						WBLogin(arg0.getDb().getUserId(), str, arg0.getDb().getUserName(), arg0.getDb().getUserIcon());
					}else{
						str = "2";
						WXLogin(arg0.getDb().exportData(), arg0.getDb().getUserName());
					}
				}
			});
		}

		@Override
		public void onCancel(Platform arg0, int arg1) {
			Config.dismissProgress();
		}

		// 微博登陆
		public void WBLogin(String uid, String user_type, final String user_name, String protrait) {
			Config.showProgressDialog(authorContext, false, null);
			MyApplication.setVisitor("1");
			AbRequestParams params = new AbRequestParams();
			params.put("uid", uid);
			params.put("user_type", user_type);
			params.put("client_type", "0");
			params.put("protrait", protrait);
			params.put("user_name", user_name);
			params.put("imin", LoginUtils.getDeviceId(authorContext));
			params.put("mapsign", GPSLocationUtils.getLatitude(authorContext) + "," + GPSLocationUtils.getLongitude(authorContext));
			abhttp = AbHttpUtil.getInstance(authorContext);
			abhttp.post(ServerMutualConfig.LoginV1, params, new AbStringHttpResponseListener() {

						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							Config.dismissProgress();
							try {
								JSONObject json = new JSONObject(content);
								if (json.getBoolean("success")) {
									SharedPreferences sp = authorContext.getSharedPreferences("babyshow_login",Context.MODE_PRIVATE);
									Editor edit = sp.edit();
									edit.putString("username", user_name);
									edit.putString("is_mobile", json.getJSONObject("data").getString("is_mobile"));
									edit.commit();
									SharedPreferences sp_user = authorContext.getSharedPreferences("babyshow_user",Context.MODE_PRIVATE);
									Editor edit_user = sp_user.edit();
									edit_user.putString("user", content);
									edit_user.putString("client_type", str);
									edit_user.commit();
//									setAliasTag(authorContext, json.getJSONObject("data").optString("user_id"), json.getJSONObject("data").optString("city", "北京"));
									authorContext.startService(new Intent(authorContext,SystemService.class));
									if(isReturnToMain){
										Intent intent = new Intent();
										intent.setClass(authorContext, MainTab.class);
										intent.putExtra("visitor", "0");
										intent.putExtra("lat", GPSLocationUtils.getLatitude(authorContext));
										intent.putExtra("lon", GPSLocationUtils.getLongitude(authorContext));
										authorContext.startActivity(intent);
									}
									if(json.getJSONObject("data").getString("is_mobile").equals("0")){
										Intent intent = new Intent(authorContext, RegisterBandPhoneActivity.class);
										authorContext.startActivity(intent);
										WebLoginActivity.webLoginActivity.finish();
									}else{
										MyApplication.setVisitor("0");
										WebLoginActivity.webLoginActivity.finish();
									}
								} else {
									MyApplication.setVisitor("1");
									Toast.makeText(authorContext, json.getString("reMsg"), Toast.LENGTH_SHORT).show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
								MyApplication.setVisitor("1");
							}
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
		
		// 微信登录
		public void WXLogin(String wxInfos, final String user_name) {
			Config.showProgressDialog(authorContext, false, null);
			MyApplication.setVisitor("1");
			AbRequestParams params = new AbRequestParams();
			params.put("source", "0"); // 来源 0 app[默认],1 H5
			params.put("client_type", "0"); // 手机类型 0 为 android ，1 为ios
			params.put("imin", LoginUtils.getDeviceId(authorContext));
			params.put("wxInfos", wxInfos);
			params.put("mapsign", GPSLocationUtils.getLatitude(authorContext) + "," + GPSLocationUtils.getLongitude(authorContext));
			abhttp = AbHttpUtil.getInstance(authorContext);
			abhttp.post(ServerMutualConfig.wxLogin, params, new AbStringHttpResponseListener() {

						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							Config.dismissProgress();
							try {
								JSONObject json = new JSONObject(content);
								if (json.getBoolean("success")) {
									SharedPreferences sp = authorContext.getSharedPreferences("babyshow_login",Context.MODE_PRIVATE);
									Editor edit = sp.edit();
									edit.putString("username", user_name);
									edit.putString("is_mobile", json.getJSONObject("data").getString("is_mobile"));
									edit.commit();
									SharedPreferences sp_user = authorContext.getSharedPreferences("babyshow_user",Context.MODE_PRIVATE);
									Editor edit_user = sp_user.edit();
									edit_user.putString("user", content);
									edit_user.putString("client_type", str);
									edit_user.commit();
//									setAliasTag(authorContext, json.getJSONObject("data").optString("user_id"), json.getJSONObject("data").optString("city", "北京"));
									authorContext.startService(new Intent(authorContext,SystemService.class));
									if(isReturnToMain){
										Intent intent = new Intent();
										intent.setClass(authorContext, MainTab.class);
										intent.putExtra("visitor", "0");
										intent.putExtra("lat", GPSLocationUtils.getLatitude(authorContext));
										intent.putExtra("lon", GPSLocationUtils.getLongitude(authorContext));
										authorContext.startActivity(intent);
									}
									if(json.getJSONObject("data").getString("is_mobile").equals("0")){
										Intent intent = new Intent(authorContext, RegisterBandPhoneActivity.class);
										authorContext.startActivity(intent);
										WebLoginActivity.webLoginActivity.finish();
									}else{
										MyApplication.setVisitor("0");
										WebLoginActivity.webLoginActivity.finish();
									}
								} else {
									MyApplication.setVisitor("1");
									Toast.makeText(authorContext, json.getString("reMsg"), Toast.LENGTH_SHORT).show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
								MyApplication.setVisitor("1");
							}
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
	};
	
	
	
	
	// 用户所在区域，便于消息推送相关
	public static void setAliasTag(Context context, String alias, String tag) {

		if (TextUtils.isEmpty(alias)) {
			return;
		}
		// ","隔开的多个 转换成 Set
		if (TextUtils.isEmpty(tag)) {
			tag = "北京";
		}
		String[] sArray = tag.split(",");

		Set<String> tagSet = new LinkedHashSet<String>();
		for (String sTagItme : sArray) {
			if (!JpushUtil.isValidTagAndAlias(sTagItme)) {
				return;
			}
			tagSet.add(sTagItme);
		}
		JPushInterface.setAliasAndTags(context, alias, tagSet,
				new TagAliasCallback() {

					@Override
					public void gotResult(int arg0, String arg1,
							Set<String> arg2) {
					}
				});
	}
	
}
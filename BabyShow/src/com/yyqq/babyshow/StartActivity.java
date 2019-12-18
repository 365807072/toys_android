package com.yyqq.babyshow;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.code.main.InitActivity;
import com.yyqq.code.main.MainTab;
import com.yyqq.commen.jpush.JpushUtil;
import com.yyqq.commen.service.SystemService;
import com.yyqq.commen.utils.CleanDataUtils;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.GPSLocationUtils;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.utils.LoginUtils;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class StartActivity extends Activity {
	private Context context;
	private AbHttpUtil abhttp;
	private String TAG = "fanfan_StartActivity";
	private String username, password;
	private LocationManager locationManager;  
	private MyApplication myMyApplication;
	private TextView forget_pwd;
	private String IMIN = "";
	private double latitude = 0.0;
	private double longitude = 0.0;
	private String userid = "";
	private String city = "";
	private static AlertDialog.Builder dialog;
    private boolean isIntent = false; // 是否已经跳转到设置界面过
    public static boolean isLogin = false; // 是否已经登录
    private Timer intentTimer = new Timer();
    private long sleepTime = 3000; // 本页面共显示时长
    
    // 弹窗每次启动只显示一次
    public static boolean mainAlertShow = true;
    public static boolean toysAlertShow = true;
    
    private WebView webView = null;


	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.start);
		startApp();
	}
	private void startApp(){
		// APP首页、玩具世界首页弹窗只显示一次
		mainAlertShow = true;
		toysAlertShow = true;

		// 是否清除过数据
		SharedPreferences sf_check = StartActivity.this.getSharedPreferences("isClean", 0);
		if (0 == sf_check.getInt("isClean", 0)) {
			CleanDataUtils.cleanApplicationData(StartActivity.this);
			Editor ed = sf_check.edit();
			ed.putInt("isClean", 1);
			ed.commit();
		}

		context = this;
		abhttp = AbHttpUtil.getInstance(context);
		abhttp.setDebug(Log.isDebug);

		File foder = new File(Config.HeadFile);
		File foder2 = new File(Config.ImageFile);
		File foder3 = new File(Config.DownFile);

		if (!foder.exists()) {
			foder.mkdirs();
		}
		if (!foder2.exists()) {
			foder2.mkdirs();
		}
		if (!foder3.exists()) {
			foder3.mkdirs();
		}
	}
	
	TimerTask intentTask = new TimerTask() {

		@Override
		public void run() {
			Message message = new Message();
			message.what = 0;
			handler.sendMessage(message);
		}
	};
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				SharedPreferences sf_check = context.getSharedPreferences("isFirst", 0);
				// 是否第一次进来了
				if (0 == sf_check.getInt("firstedit", 0)) {
					SharedPreferences sf = context.getSharedPreferences("isFirst", 0);
					sf = context.getSharedPreferences("isFirst", Context.MODE_PRIVATE);
					Editor edit_user = sf.edit();
					edit_user.putInt("firstedit", 1);
					edit_user.commit();
					startActivity(new Intent(StartActivity.this, InitActivity.class));
					StartActivity.this.finish();
				}else{
					MyApplication.getInstance().stopAll();
					startService(new Intent(context, SystemService.class));
					Intent intent = new Intent();
					intent.setClass(StartActivity.this, MainTab.class);
					intent.putExtra("group_id", "505");
					intent.putExtra("visitor", MyApplication.getVisitor());
					intent.putExtra("lat", GPSLocationUtils.getLatitude(context));
					intent.putExtra("lon", GPSLocationUtils.getLongitude(context));
					startActivity(intent);
					StartActivity.this.finish();
					
				}
				intentTimer.cancel();
				break;
			}
		}

	};
	
	public class JSCallback{}

	public static boolean deleteFoder(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				if (files != null) {
					for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
						deleteFoder(files[i]); // 把每个文件 用这个方法进行迭代
					}
				}
			}
			boolean isSuccess = file.delete();
			if (!isSuccess) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);

		if(!getGpsType() &&  !isIntent && 0 == context.getSharedPreferences("isFirst", 0).getInt("firstedit", 0)){ // 如果没有打开GPS,并且没有去设置过就跳转到GPS设置页
			this.showDialog(StartActivity.this);
		}else{ // 如果GPS已经打开，或者已经跳转到GPS设置页了就直接开始执行以下逻辑
			initIntent();	
		}
		JPushInterface.onResume(context);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		JPushInterface.onPause(context);
	}

	private void initUserInfo() {
		TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		SharedPreferences sp = context.getSharedPreferences("babyshow_login", Context.MODE_PRIVATE);
		username = sp.getString("username", "");
		password = sp.getString("password", "");
		if (username.isEmpty() && password.isEmpty()) { // 未登录过
			registTourist(TelephonyMgr.getDeviceId()+""); // 获取游客ID
		} else if(!username.isEmpty() && !password.isEmpty()){ // 账号密码登录
			updataUserInfo(username, password, TelephonyMgr.getDeviceId()+""); // 更新用户的数据
		}else{ // 第三方登录
			SharedPreferences sp_login = StartActivity.this.getSharedPreferences("babyshow_login",Context.MODE_PRIVATE);
			if(sp_login.getString("is_mobile", "").equals("0") || sp_login.getString("is_mobile", "").equals("")){
				isLogin = false;
				MyApplication.setVisitor("1");
			}else{
				isLogin = true;
				MyApplication.setVisitor("0");
			}
		}
	}
	
	private boolean getGpsType(){
		LocationManager alm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);  
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {  
            return true;
        }else{
        	return false;
        }
	}
	
	private void openGPSSettings() {  
        isIntent = true;
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
        startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面  
    }  
	
	// 更新地理位置信息
	private void updateLocationInfo(Location loc) {  
		if (loc != null) {  
			double lat = loc.getLatitude();  
			double lng = loc.getLongitude();  
			
			if(0.0 != lat && 0.0 != lng){
				GPSLocationUtils.setLatitude(context, lat); // 保存经度
				GPSLocationUtils.setLongitude(context, lng); // 保存纬度
			}
		}
	}  
	
	// 更新用户数据(重新登录)
	private void updataUserInfo(final String userName, final String pwd, final String imin){
		StartActivity.this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				AbRequestParams params = new AbRequestParams();
				params.put("user_name", userName);
				if(pwd.length() != 32){
					params.put("password", Config.MD5(pwd));
				}else{
					params.put("password", pwd);
				}
				params.put("imin", imin);
				params.put("mapsign", GPSLocationUtils.getLatitude(context) + "," + GPSLocationUtils.getLongitude(context));
				abhttp.post(ServerMutualConfig.Login, params,
						new AbStringHttpResponseListener() {
							@Override
							public void onSuccess(int statusCode, String content) {
								super.onSuccess(statusCode, content);
								try {
									JSONObject json = new JSONObject(content);
									if (json.getBoolean("success")) {
										isLogin = true;
										MyApplication.setVisitor("0");
										if(pwd.length() != 32){
											LoginUtils.saveHistoryUserName(context, userName, Config.MD5(pwd), json.getJSONObject("data").getString("avatar"));
											LoginUtils.setUserLoginType(context, userName, Config.MD5(pwd));
										}else{
											LoginUtils.saveHistoryUserName(context, userName, pwd, json.getJSONObject("data").getString("avatar"));
											LoginUtils.setUserLoginType(context, userName, pwd);
										}
										SharedPreferences sp_user = context.getSharedPreferences("babyshow_user",Context.MODE_PRIVATE);
										Editor edit_user = sp_user.edit();
										edit_user.putString("user", content);
										edit_user.commit();
										userid = json.getJSONObject("data").optString("user_id");
										city = json.getJSONObject("data").optString("city", "北京");
										setAliasTag(userid, city);
									} else {
										isLogin = false;
										MyApplication.setVisitor("1");
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
							}

							@Override
							public void onFailure(int statusCode,
									String content, Throwable error) {
								super.onFailure(statusCode, content, error);
							}
						});
			}
		});
	}
	
	// 注册游客身份
	private void registTourist(final String imin){
		StartActivity.this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				AbRequestParams params = new AbRequestParams();
				params.put("imin", imin);
				params.put("mapsign", GPSLocationUtils.getLatitude(context) + "," + GPSLocationUtils.getLongitude(context));
				abhttp.post(ServerMutualConfig.VisitorRegist, params,
						new AbStringHttpResponseListener() {

							@Override
							public void onSuccess(int statusCode, final String content) {
								super.onSuccess(statusCode, content);
								LoginUtils.setUserLoginType(context,"", "");
								MyApplication.setVisitor("1");
								JSONObject json = null;
								try {
									json = new JSONObject(content);
									if (json.getBoolean("success")) {
										isLogin = true;
										SharedPreferences sp_user = context.getSharedPreferences("babyshow_user",Context.MODE_PRIVATE);
										Editor edit_user = sp_user.edit();
										edit_user.putString("user", content);
										edit_user.commit();
										setAliasTag(json.getJSONObject("data").optString("user_id"), json.getJSONObject("data").optString("city", "北京"));
									}else{
										isLogin = false;
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onFinish() {
								super.onFinish();
							}

							@Override
							public void onFailure(int statusCode, String content,
									Throwable error) {
								super.onFailure(statusCode, content, error);
							}
						});
			}
		});
		
	}
	
	public void setAliasTag(String alias, String tag) {

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
//		JPushInterface.setAliasAndTags(this, alias, tagSet,
//				new TagAliasCallback() {
//
//					@Override
//					public void gotResult(int arg0, String arg1,
//							Set<String> arg2) {
//					}
//				});
	}
	
	private void showDialog(final Activity context){
		AlertDialog.Builder dialog = new Builder(context);
		dialog.setTitle("定位服务未开启：");
		dialog.setMessage("请启用GPS，以便于我们更好的服务于您。");
		dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				openGPSSettings();
			}
		});
		
		dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				initIntent();
			}
		});
		dialog.create().show();
	}
	
	private void initIntent(){
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);  
        String provider = LocationManager.GPS_PROVIDER;  
        Location location = locationManager.getLastKnownLocation(provider);
        updateLocationInfo(location); // 更新最新位置信息
        LocationListener ll = new LocationListener() {  
        	  
            @Override  
            public void onLocationChanged(Location location) {  
               updateLocationInfo(location);  
            }  
  
            @Override  
            public void onStatusChanged(String provider, int status,  
                    Bundle extras) {  
  
            }  
  
            @Override  
            public void onProviderEnabled(String provider) {  
  
            }  
  
            @Override  
            public void onProviderDisabled(String provider) {  
  
            }  
  
        };  
        locationManager.requestLocationUpdates(provider, 60000, 1, ll);  // 更新时间
        // 如果已保存有用户信息就去更新用户信息，若没有用户信息就获取一个游客ID
        new Thread(new Runnable() {
        	
        	@Override
        	public void run() {
        		initUserInfo();
        	}
        }).start();		
        
//        intentTimer.schedule(intentTask, sleepTime, sleepTime);
        new Thread(new Runnable() {
        	
        	@Override
        	public void run() {
        		try {
					Thread.sleep(sleepTime);
					StartActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							
							SharedPreferences sf_check = context.getSharedPreferences("isFirst", 0);
							// 是否第一次进来了
							if (0 == sf_check.getInt("firstedit", 0)) {
								SharedPreferences sf = context.getSharedPreferences("isFirst", 0);
								sf = context.getSharedPreferences("isFirst", Context.MODE_PRIVATE);
								Editor edit_user = sf.edit();
								edit_user.putInt("firstedit", 1);
								edit_user.commit();
								startActivity(new Intent(StartActivity.this, InitActivity.class));
								StartActivity.this.finish();
							}else{
								MyApplication.getInstance().stopAll();
								startService(new Intent(context, SystemService.class));
								Intent intent = new Intent();
								intent.setClass(StartActivity.this, MainTab.class);
								intent.putExtra("group_id", "505");
								intent.putExtra("visitor", MyApplication.getVisitor());
								intent.putExtra("lat", GPSLocationUtils.getLatitude(context));
								intent.putExtra("lon", GPSLocationUtils.getLongitude(context));
								startActivity(intent);
								StartActivity.this.finish();
							}
						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        	}
        }).start();	
	}



}

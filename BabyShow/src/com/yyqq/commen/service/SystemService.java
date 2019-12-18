package com.yyqq.commen.service;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import com.yyqq.babyshow.R;
import com.yyqq.babyshow.StartActivity;
import com.yyqq.code.main.MainTab;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.framework.application.ServerMutualConfig;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.IBinder;

public class SystemService extends Service {
	public static SystemService intances;
	Timer timer;
	Task task;
	public final long SLEEP = 20 * 1000;
	static boolean isRun = false;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		intances = this;
		if (task == null)
			task = new Task();
		if (timer == null)
			timer = new Timer();
		if (!isRun) {
			isRun = true;
			try {
				timer.schedule(task, 0, SLEEP);
			} catch (Exception e) {
			}
		}
		return START_STICKY;
	}

	public void getSystemMessage() {
		if (Config.getUser(SystemService.this).uid.length() > 0) {
			HttpResponse httpResponse = null;
			HttpGet httpGet = new HttpGet(ServerMutualConfig.NoticeList + "&"
					+ "user_id=" + Config.getUser(SystemService.this).uid);
			try {
				httpResponse = new DefaultHttpClient().execute(httpGet);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String result = EntityUtils.toString(httpResponse
							.getEntity());
					// 去掉返回结果中的"\r"字符，否则会在结果字符串后面显示一个小方格
					try {
						JSONObject json = new JSONObject(result.replaceAll(
								"\r", ""));
						Log.d("NoticeService",
								"notice msg = " + json.toString());
						if (json.getBoolean("success")) {
							JSONObject data = json.getJSONObject("data");
							SharedPreferences sp = SystemService.this
									.getSharedPreferences(
											"babyshow_system_count",
											Context.MODE_PRIVATE);
							Editor edit = sp.edit();
							if (data.getInt("latest_total_count") != 0
									&& data.getInt("latest_total_count") != sp
											.getInt("notice", 0)) {
								edit.putInt("notice",
										data.getInt("latest_total_count"));
								edit.commit();
								if (MainTab.instance != null) {
									handler.post(new Runnable() {
										@Override
										public void run() {
											MainTab.instance.showNew();
										}
									});
								}
								// Config.writeReadStatus(SystemService.this,"system","system",false);
								Config.ring(SystemService.this);
								if (Config.outApp(SystemService.this)) {
									showNotification(
											"自由环球租赁",
											"您有"
													+ data.getInt("latest_total_count")
													+ "个未读消息!");
								} else {
								}
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// stopService(new Intent(SystemService.this,SystemService.class));
		}
	}

	public NotificationManager mNotificationManager = null;

	/**
	 * @param title
	 * @param message
	 * @param uid
	 *            type+uid
	 */
	public void showNotification(String title, String message/* ,String uid */) {
		// 消息通知栏
		// 定义NotificationManager
		String ns = Context.NOTIFICATION_SERVICE;
		if (mNotificationManager != null) {
			mNotificationManager.cancelAll();
		}
		if (mNotificationManager == null)
			mNotificationManager = (NotificationManager) SystemService.this
					.getSystemService(ns);
		// 定义通知栏展现的内容信息
		int icon = R.drawable.icon;
		CharSequence tickerText = "自由环球租赁-未读消息";
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		// 定义下拉通知栏时要展现的内容信息
		Context context = SystemService.this.getApplicationContext();
		Class back = StartActivity.class;
		if (MainTab.instance != null) {
			back = MainTab.class;
			// handler.post(new Runnable()
			// {
			// @Override
			// public void run()
			// {
			// MainTab.instance.getTabHost().setCurrentTab(1);
			// }
			// });
		}
		// Intent notificationIntent = new Intent(service,back);
		// PendingIntent contentIntent =
		// PendingIntent.getActivity(service,0,notificationIntent,0);
		Intent intent = new Intent(this, back);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, 0);
		notification.setLatestEventInfo(context, title, message, contentIntent);
		// 用mNotificationManager的notify方法通知用户生成标题栏消息通知
		mNotificationManager.notify(1, notification);
	}

	Handler handler = new Handler();

	public class Task extends TimerTask {
		@Override
		public void run() {
			getSystemMessage();
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
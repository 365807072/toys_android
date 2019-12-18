package com.yyqq.commen.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.APKUpdataActivity;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * apk更新相关
 * */
public class APKUpdataUtils {
	
	
	/**
	 * 检查是否需要更新
	 * */
	public static void checkUpdata(final Context context){
		final AbRequestParams params = new AbRequestParams();
		params.put("version", getVersionCode(context)+"");
		AbHttpUtil.getInstance(context).get(ServerMutualConfig.APK_UPDATA_CHECK + "&" + params.toString(),new AbStringHttpResponseListener() {
			
			 @Override
			public void onSuccess(int statusCode, String content) {
				 super.onSuccess(statusCode, content);
				 try {
					 if(!content.isEmpty() && new JSONObject(content).getBoolean("success")){
						 // is_new=1, 需要更新
						 if(new JSONObject(content).getJSONObject("data").getString("is_new").equals("1")){
							 Intent intent = new Intent(context, APKUpdataActivity.class);
							 intent.putExtra("des", new JSONObject(content).getJSONObject("data").getString("description"));
							 intent.putExtra("down_url", new JSONObject(content).getJSONObject("data").getString("url"));
							 context.startActivity(intent);
						 }
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

	/**
	 * 获取当前版本号
	 * */
	public static int getVersionCode(Context mContext) {  
		int version = 0;
        try {  
        	PackageManager manager = mContext.getPackageManager();  
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);  
            version = info.versionCode;  
            return version;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return 0;  
        }  
    }  
	
	/**
	 * 下载最新安装包
	 * */
	public static void downLoadFile(Context con, String httpUrl, NotificationManager notificationManager, Notification notification) {
		final String fileName = "自由环球租赁.apk";
		File tmpFile = new File("/sdcard/");
		if (!tmpFile.exists()) {
			tmpFile.mkdir();
		}
		final File file = new File("/sdcard/" + fileName);
		
		try {
			URL url = new URL(httpUrl);
			try {
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				InputStream is = conn.getInputStream();
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buf = new byte[30*1024];
				conn.connect();
				double count = 0;
				if (conn.getResponseCode() >= 400) {
					Toast.makeText(con, "连接超时", Toast.LENGTH_SHORT).show();
				} else {
					while (count <= 100) {
						if (is != null) {
							int numRead = is.read(buf);
							if (numRead <= 0) {
								break;
							} else {
								fos.write(buf, 0, numRead);
							}
						} else {
							break;
						}
					}
				}
				conn.disconnect();
				fos.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		notificationManager.cancel(2);
		notification = new Notification(R.drawable.icon, "您的安装包已经下载完成！", System.currentTimeMillis());
		notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
		notification.setLatestEventInfo(con, "自由环球租赁", "您的安装包已经下载完成！", null);
		notificationManager.notify(3, notification);
		notificationManager.cancel(3);
		
		openFile(con, file);
	}
	
	/**
	 * 跳转至安装界面
	 * */
	public static void openFile(Context con, File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		con.startActivity(intent);
	}
	
}
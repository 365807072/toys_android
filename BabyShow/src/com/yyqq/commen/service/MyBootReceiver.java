package com.yyqq.commen.service;

import com.yyqq.commen.utils.Log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBootReceiver extends BroadcastReceiver {
	// 用于接收满足条件的 Broadcast（相应的 Broadcast 的注册信息详见 AndroidManifest.xml
	// ，当系统启动完毕后会调用这个广播接收器）
	// 当开机的时候就会启动这个Service
	@Override
	public void onReceive(Context context, Intent arg1) {
		Log.d("MyDebug", "开机,启动push服务");
		// 启动服务
		Intent service = new Intent(context, SystemService.class);
		service.putExtra("startPhone", true);
		context.startService(service);
	}
}

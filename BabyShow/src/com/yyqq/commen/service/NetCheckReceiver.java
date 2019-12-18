package com.yyqq.commen.service;

import com.yyqq.commen.utils.Config;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class NetCheckReceiver extends BroadcastReceiver {
	// android 中网络变化时所发的Intent的名字
	public static final String netACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	public static String TAG = "NetCheckReceiver";
	public static boolean isbreak;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(netACTION)) {
			// Log.e(TAG,"ACTION:" + intent.getAction());
			// Intent中ConnectivityManager.EXTRA_NO_CONNECTIVITY这个关键字表示着当前是否连接上了网络
			// true 代表网络断开 false 代表网络没有断开
			Config.isBreakNetWork = intent.getBooleanExtra(
					ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
			// // Log.e(TAG,"is break:" + isBreak);
			// if(isBreak)
			// {
			// isbreak = true;
			// }
			// else
			// {
			// if(isbreak)
			// {
			// new Thread()
			// {
			// public void run()
			// {
			//
			// // XMPPConfig.closeConnection();
			// XMPPConfig.getConnection();
			// CircleService.LoginXMPP(null);
			// }
			// }.start();
			// isbreak = false;
			// }
			// }
		}
	}
}
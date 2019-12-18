package com.yyqq.babyshow;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class WelcomeActivity extends FragmentActivity {

	private  int permissions_req = 102;
	private boolean check(){
		//判断是否已经获取相应权限                                                              对应权限
		if (ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
				&& ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
				&& ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
				&& ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

			return true;
		}
// 若没有获得相应权限，则弹出对话框获取
		else {
			//申请权限
			ActivityCompat.requestPermissions(WelcomeActivity.this,
					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
							Manifest.permission.ACCESS_FINE_LOCATION,
							Manifest.permission.READ_PHONE_STATE,
							Manifest.permission.CAMERA}, permissions_req);
			return false;
		}

	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if(requestCode == permissions_req){
			startApp();
		}
	}

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.start);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!check() &&  Build.VERSION.SDK_INT >= 23){

		}else {
			startApp();

		}
	}

	private void startApp() {
		startActivity(new Intent(this,StartActivity.class));
	}

}

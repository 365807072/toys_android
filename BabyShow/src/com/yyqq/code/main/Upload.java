package com.yyqq.code.main;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.yyqq.commen.utils.Config;

public class Upload extends Activity {
	public String Tag = Upload.class.getSimpleName();

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return Config.onKeyDown(keyCode, event, this);
	}

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		View view = new View(this);
		setContentView(view);
		view.setMinimumWidth(100);
		view.setMinimumHeight(100);
		view.setBackgroundColor(Color.RED);

	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}

package com.yyqq.code.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.yyqq.babyshow.R;
import com.yyqq.code.toyslease.ToysLeaseMainTabActivity;

/**
 * 点击首页添加按钮，由此跳进添加页面
 * */
public class MainToToysMainActivity extends Activity {
	
	public static boolean NEED_INTENT = true; // 是否需要打开全新页面
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_check);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(NEED_INTENT){
			Intent in = new Intent(MainToToysMainActivity.this,ToysLeaseMainTabActivity.class);
			startActivity(in);
			NEED_INTENT = false;
		}else{
			Intent in = new Intent(MainToToysMainActivity.this, MainTab.class);
			startActivity(in);
			NEED_INTENT = true;
		}
		
	}
}

package com.yyqq.code.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.yyqq.babyshow.R;
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.commen.utils.Config;
import com.yyqq.framework.application.MyApplication;

/**
 * 点击首页添加按钮，由此跳进添加页面
 * */
public class AddCheckActivity extends Activity {
	
	public static boolean isToMain = true; // 发布成功是否需要回到首页
	private boolean isIntent = false;
	public static boolean NEED_INTENT = true; // 是否需要打开全新页面
	public static AddCheckActivity addCheckActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_check);
		addCheckActivity = this;
	}
	
	@Override
	protected void onResume() {
//		if(!MyApplication.getVisitor().equals("0") && !isIntent || Config.getUser(AddCheckActivity.this).uid.equals("")){ // 没有登录并且没有进入过登录页
//			isIntent = true; // 已经进入过登录页 
//			Intent in = new Intent(AddCheckActivity.this, WebLoginActivity.class);
//			startActivity(in);
//		}else if(!MyApplication.getVisitor().equals("0")  && isIntent){ // 进入过登录页并且没有登录
//			Intent in = new Intent(AddCheckActivity.this, MainTab.class);
//			startActivity(in);
//		}else if(MyApplication.getVisitor().equals("0")){
			if(NEED_INTENT){
				Intent in = new Intent(AddCheckActivity.this, AddNewPostActivity.class);
				startActivity(in);
				NEED_INTENT = false;
			}else{
				Intent in = new Intent(AddCheckActivity.this, MainTab.class);
				startActivity(in);
				NEED_INTENT = true;
			}
//		}
		super.onResume();
	}
}

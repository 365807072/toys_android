package com.yyqq.code.personal;

import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainTab;
import com.yyqq.commen.service.SystemService;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Bind extends Activity {
	private String TAG = "fanfan_Bind";
	private Activity context;
	private AbHttpUtil abhttp;
	private EditText username, password;
	private Button bind;

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		setContentView(R.layout.bind);
		context = this;
		init();

		bind.setText("确认绑定");
		bind.setVisibility(View.VISIBLE);
	}

	private void init() {
		abhttp = AbHttpUtil.getInstance(context);
		abhttp.setDebug(Log.isDebug);
		bind = (Button) findViewById(R.id.bind);
		bind.setOnClickListener(bindClick);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		username.setOnFocusChangeListener(inputFocus);
		password.setOnFocusChangeListener(inputFocus);
	}

	public OnClickListener bindClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (password.getText().toString().trim().length() < 6
					|| password.getText().toString().trim().length() > 16) {
				Toast.makeText(context, "密码最少6位最多16位！", Toast.LENGTH_SHORT)
						.show();
			} else {
				Config.showProgressDialog(context, false, null);
				AbRequestParams params = new AbRequestParams();
				params.put("uid", Config.getUser(context).uid);
				params.put("user_type", getIntent().getStringExtra("user_type")); // 1微博
																					// 2微信
				params.put("user_name", username.getText().toString().trim());
				params.put("password",
						Config.MD5(password.getText().toString().trim()));
				abhttp.post(ServerMutualConfig.BindShowUser, params,
						new AbStringHttpResponseListener() {
							@Override
							public void onSuccess(int statusCode, String content) {
								super.onSuccess(statusCode, content);
								try {
									JSONObject json = new JSONObject(content);
									if (json.getBoolean("success")) {
										Toast.makeText(context, "绑定成功", 0)
												.show();
										SharedPreferences sp_user = context
												.getSharedPreferences(
														"babyshow_user",
														Context.MODE_PRIVATE);
										Editor edit_user = sp_user.edit();
										edit_user.putString("user", content);
										edit_user.commit();
										MyApplication.getInstance().stopAll();
										startService(new Intent(context,
												SystemService.class));
										startActivity(new Intent(context,
												MainTab.class));
									} else {
										Toast.makeText(context,
												json.getString("reMsg"),
												Toast.LENGTH_SHORT).show();
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
							}
						});
			}
		}

	};

	public OnFocusChangeListener inputFocus = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				v.setBackgroundResource(R.drawable.input_bg_sel);
			} else {
				v.setBackgroundResource(R.drawable.wait_input_bg);
			}
		}
	};

}

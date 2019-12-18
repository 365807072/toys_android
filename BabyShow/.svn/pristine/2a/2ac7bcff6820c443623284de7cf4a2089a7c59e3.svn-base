package com.yyqq.code.login;

import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.framework.application.ServerMutualConfig;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ResetPwd extends Activity {
	private TextView myCancel, mySure;
	private Activity context;
	private EditText new_pwd, sure_pwd;
	public static final String TAG = "ResetPwd";
	AbHttpUtil ab;
	private boolean flag = false;

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
		setContentView(R.layout.reset_pwd);
		context = this;

		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
		myCancel = (TextView) findViewById(R.id.cancel);
		mySure = (TextView) findViewById(R.id.sure);
		sure_pwd = (EditText) findViewById(R.id.sure_pwd);
		new_pwd = (EditText) findViewById(R.id.new_pwd);

		sure_pwd.setOnFocusChangeListener(inputFocus);
		new_pwd.setOnFocusChangeListener(inputFocus);

		myCancel.setOnClickListener(cancelClick);
		mySure.setOnClickListener(sureClick);

	}

	public OnClickListener sureClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			AbRequestParams params = new AbRequestParams();
			String mySurepwd = sure_pwd.getText().toString();
			params.put("do_type", 1 + "");
			params.put("new_passwd", Config.MD5(new_pwd.getText().toString()));
			params.put("email", getIntent().getStringExtra("email"));

			Log.i(TAG, "email = " + getIntent().getStringExtra("email"));
			ab.setTimeout(3 * 1000);
			ab.post(ServerMutualConfig.DoPasswd, params,
					new AbStringHttpResponseListener() {

						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							try {
								JSONObject json = new JSONObject(content);
//								Log.i(TAG, "json = " + json.toString());
								if (json.getBoolean("success")) {
									Intent intent = new Intent();
									intent.setClass(context, WebLoginActivity.class);
									startActivity(intent);
									Toast.makeText(context, "请重新登录", 0).show();
								} else {
									Log.i(TAG, "json = " + json.toString());
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					});
		}
	};

	public OnClickListener cancelClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	public OnFocusChangeListener inputFocus = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				v.setBackgroundResource(R.drawable.input_bg1);
			} else {
				v.setBackgroundResource(R.drawable.wait_input_bg);
			}
		}
	};
}

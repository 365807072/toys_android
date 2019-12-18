package com.yyqq.code.login;

import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.personal.UserSet;
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

public class ChangePwd extends Activity {

	private Activity context;
	private TextView myCancel, mySure;
	private EditText old_pwd, new_pwd, sure_pwd;
	private String mySurepwd;
	public static final String TAG = "ChangePwd";
	private String userid;
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
		setContentView(R.layout.change_pwd);
		context = this;

		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
		userid = getIntent().getStringExtra("user_id");
		myCancel = (TextView) findViewById(R.id.cancel);
		mySure = (TextView) findViewById(R.id.sure);
		old_pwd = (EditText) findViewById(R.id.old_pwd);
		new_pwd = (EditText) findViewById(R.id.new_pwd);
		sure_pwd = (EditText) findViewById(R.id.sure_pwd);

		old_pwd.setOnFocusChangeListener(inputFocus);
		new_pwd.setOnFocusChangeListener(inputFocus);
		sure_pwd.setOnFocusChangeListener(inputFocus);

		myCancel.setOnClickListener(cancelClick);
		mySure.setOnClickListener(sureClick);

	}

	public OnClickListener sureClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Config.showProgressDialog(ChangePwd.this, false, null);
			AbRequestParams params = new AbRequestParams();
			mySurepwd = sure_pwd.getText().toString();
			params.put("do_type", 0 + "");
			params.put("new_passwd",
					Config.MD5(new_pwd.getText().toString().trim()));
			params.put("old_passwd",
					Config.MD5(old_pwd.getText().toString().trim()));
			params.put("user_id", userid);
			Log.i(TAG, "params = " + params.toString());
			if (flag) {
				return;
			}
			flag = true;
			Log.i(TAG, "param = " + ServerMutualConfig.DoPasswd + params);
			ab.post(ServerMutualConfig.DoPasswd, params,
					new AbStringHttpResponseListener() {

						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							Config.dismissProgress();
							try {
								JSONObject json = new JSONObject(content);

								if (json.getBoolean("success")) {
									flag = false;
									Intent intent = new Intent();
									intent.setClass(context, UserSet.class);
									startActivity(intent);
									Toast.makeText(context, "修改成功", 0).show();
								} else {
									flag = false;
									Toast.makeText(context, "修改失败", 0).show();
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
				v.setBackgroundResource(R.drawable.input_bg_sel);
			} else {
				v.setBackgroundResource(R.drawable.wait_input_bg);
			}
		}
	};
}

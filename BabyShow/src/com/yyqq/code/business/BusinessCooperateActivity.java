package com.yyqq.code.business;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class BusinessCooperateActivity extends Activity {
	public String TAG = "Cooperate";
	private Activity context;
	private MyApplication myMyApplication;
	private AbHttpUtil ab;
	private EditText bs_name, bs_msg, num, emali, address;
	private Button store, upLine, isBs, isUser, commit, upaddress;
	private int isStore = 0, cooperateType = 0;

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
		setContentView(R.layout.cooperate);

		context = this;
		myMyApplication = (MyApplication) context.getApplication();
		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
		init();
		initControl();
	}

	private void init() {
		// TODO Auto-generated method stub
		upaddress = (Button) findViewById(R.id.upaddress);
		bs_name = (EditText) findViewById(R.id.bs_name);
		bs_msg = (EditText) findViewById(R.id.bs_msg);
		num = (EditText) findViewById(R.id.num);
		emali = (EditText) findViewById(R.id.email);
		address = (EditText) findViewById(R.id.address);

		store = (Button) findViewById(R.id.store); // 商家
		upLine = (Button) findViewById(R.id.upLine); // 淘宝商家
		isBs = (Button) findViewById(R.id.isBs); // 我是商家
		isUser = (Button) findViewById(R.id.isUser); // 我是用户
		commit = (Button) findViewById(R.id.commit);
	}

	private void initControl() {
		store.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				cooperateType = 0;
				store.setBackgroundResource(R.drawable.sel_bt);
				upLine.setBackgroundResource(R.drawable.sel_bt_no);
				upaddress.setBackgroundResource(R.drawable.sel_bt_no);
			}
		});

		upLine.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				cooperateType = 1;
				store.setBackgroundResource(R.drawable.sel_bt_no);
				upLine.setBackgroundResource(R.drawable.sel_bt);
				upaddress.setBackgroundResource(R.drawable.sel_bt_no);
			}
		});
		
		upaddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				cooperateType = 2;
				store.setBackgroundResource(R.drawable.sel_bt_no);
				upLine.setBackgroundResource(R.drawable.sel_bt_no);
				upaddress.setBackgroundResource(R.drawable.sel_bt);
			}
		});

		isBs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				isStore = 0;
				isBs.setBackgroundResource(R.drawable.sel_bt);
				isUser.setBackgroundResource(R.drawable.sel_bt_no);
			}
		});

		isUser.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				isStore = 1;
				isBs.setBackgroundResource(R.drawable.sel_bt_no);
				isUser.setBackgroundResource(R.drawable.sel_bt);
			}
		});

		commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CommitInfo();
			}
		});
	}

	public void CommitInfo() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("mobile", num.getText().toString().trim());
		params.put("cooperation_name", bs_name.getText().toString().trim());
		params.put("cooperation_description", bs_msg.getText().toString().trim());
		params.put("cooperation_state", cooperateType + "");
		params.put("user_state", isStore + "");
		params.put("email", emali.getText().toString().trim());
		params.put("address", address.getText().toString().trim());
		Log.e(TAG, params.toString());
		ab.post(ServerMutualConfig.AddCooperation, params,
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								Toast.makeText(context,
										json.getString("reMsg"),
										Toast.LENGTH_SHORT).show();
								finish();
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
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}
				});
	}

}
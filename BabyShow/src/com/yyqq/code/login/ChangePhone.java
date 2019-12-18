package com.yyqq.code.login;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.framework.application.ServerMutualConfig;

public class ChangePhone extends Activity {

	private Activity context;
	private Button commit;
	private EditText num;
	private TextView old_num;
	public static final String TAG = "fanfan_ChangePhone";
	private AbHttpUtil ab;
	private String myNum = "";
	public static ChangePhone changePhone;

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
		context = this;
		changePhone = this;
		setContentView(R.layout.exchange_phone);
		initView();
		getInfo();
	}

	public void getInfo() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		// android.util.Log.e(TAG,
		// ServerMutualConfig.MobileInfo + "&" + params.toString());
		ab.get(ServerMutualConfig.MobileInfo + "&" + params.toString(),
				new AbStringHttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							old_num.setText(json.getJSONObject("data")
									.getString("hidden_mobile"));

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

					@Override
					public void sendSuccessMessage(int statusCode,
							String content) {
						super.sendSuccessMessage(statusCode, content);
					}

				});
	}

	public void initView() {
		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
		old_num = (TextView) findViewById(R.id.old_num);
		num = (EditText) findViewById(R.id.num);
		commit = (Button) findViewById(R.id.commit);
		commit.setOnClickListener(sureClick);
	}

	public OnClickListener sureClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			myNum = num.getText().toString().trim();
			if (TextUtils.isEmpty(myNum)) {
				Toast.makeText(context, "要填写您的旧手机号哦", 0).show();
				return;
			}
			AbRequestParams params = new AbRequestParams();
			params.put("login_user_id", Config.getUser(context).uid);
			params.put("new_mobile", myNum);
			// android.util.Log.e(TAG, ServerMutualConfig.CheckMobile + "&"
			// + params.toString());
			ab.get(ServerMutualConfig.CheckMobile + "&" + params.toString(),
					new AbStringHttpResponseListener() {

						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							try {
								JSONObject json = new JSONObject(content);
								if (json.getBoolean("success")) {
									Config.dismissProgress();
									startActivity(new Intent(context,
											ChangePhone2.class));
								} else {
									Config.dismissProgress();
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

						@Override
						public void sendSuccessMessage(int statusCode,
								String content) {
							super.sendSuccessMessage(statusCode, content);
						}

					});
		}
	};
}

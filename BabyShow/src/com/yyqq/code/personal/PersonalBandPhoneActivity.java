package com.yyqq.code.personal;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.service.IdentifyMsm;
import com.yyqq.commen.service.IdentifyPhone;
import com.yyqq.commen.utils.Config;
import com.yyqq.framework.application.ServerMutualConfig;
import com.yyqq.framework.callback.CallBackCode;

/**
 * 个人中心-绑定手机号
 * */
public class PersonalBandPhoneActivity extends Activity {

	private Activity context;
	private String TAG = "fanfan_BandPhone";
	private AbHttpUtil ab;
	private RelativeLayout general_ly;

	private EditText inputPhone;
	private EditText inputCode;
	private Button commit;
	private Button getCode;

	private IdentifyPhone id;
	private IdentifyMsm idm;
	private String phone, code, formatedPhone;
	private TimeCount time;

	public void onResume() {
		id.registerEventHandler();
		idm.registerEventHandler();
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		id.unregisterEventHandler();
		idm.registerEventHandler();
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		context = this;
		setContentView(R.layout.band_phone);
		initView();
		initControl();
		id = new IdentifyPhone(context);
		idm = new IdentifyMsm(context);
		time = new TimeCount(60000, 1000);
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {// 计时完毕
			getCode.setText("获取验证码");
			getCode.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			getCode.setClickable(false);// 防止重复点击
			getCode.setText(millisUntilFinished / 1000 + "s后重新获取");
		}
	}

	private void initView() {
		// 返回
		general_ly = (RelativeLayout) findViewById(R.id.general_ly);
		general_ly.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				context.finish();
			}
		});
		ab = AbHttpUtil.getInstance(context);
		inputPhone = (EditText) findViewById(R.id.input_phone);
		inputCode = (EditText) findViewById(R.id.input_code);
		getCode = (Button) findViewById(R.id.getCode);
		commit = (Button) findViewById(R.id.commit);
	}

	private void initControl() {
		getCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				time.start();
				phone = inputPhone.getText().toString().trim()
						.replaceAll("\\s*", "");
				code = "86";
				id.showDialog(phone, code, new CallBackCode() {

					@Override
					public void execute(String phone, String code,
							String formatedPhone) {
					}

				});
			}
		});

		commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(inputPhone.getText().toString().trim())
						|| TextUtils.isEmpty(inputCode.getText().toString()
								.trim())) {
					Toast.makeText(context, "信息填写不完整", 0).show();
					return;
				}
				Config.showProgressDialog(context, false, null);
				// 提交验证码
				String verificationCode = inputCode.getText().toString().trim();
				phone = inputPhone.getText().toString().trim().replaceAll("\\s*", "");
				code = "86";
				idm.sendMsm(code, phone, verificationCode, new CallBackCode() {

					@Override
					public void execute(String code, String phone,
							String verificationCode) {
						Config.dismissProgress();
						if(code.contains("true")){
							BindPhone();
						}else{
							Toast.makeText(context, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}

		});
	}

	public void BindPhone() {
		// TODO 绑定手机号接口
		Config.showProgressDialog(context, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("mobile", phone);
		ab.get(ServerMutualConfig.EditMobile + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Config.dismissProgress();
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								Toast.makeText(context, json.getString("reMsg"), Toast.LENGTH_SHORT).show();
								finish();
							} else {
								if (3008 == json.getInt("reCode")) {
									AlertDialog.Builder dialog = new Builder(context);
									dialog.setTitle("自由环球租赁");
									dialog.setMessage("手机号已被注册，是否绑定");
									dialog.setPositiveButton(
											"取消",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
												}

											});
									dialog.setNegativeButton(
											"绑定",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													addPhone();
												}
											});
									dialog.create().show();
								} else {
									Toast.makeText(context,
											json.getString("reMsg"),
											Toast.LENGTH_SHORT).show();
								}
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
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
						Config.dismissProgress();
					}
				});
	}

	public void addPhone() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("mobile", phone);
		ab.get(ServerMutualConfig.AddMobile + "&" + params.toString(),
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
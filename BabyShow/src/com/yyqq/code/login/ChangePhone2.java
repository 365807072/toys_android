package com.yyqq.code.login;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
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
import com.yyqq.babyshow.StartActivity;
import com.yyqq.code.main.MainTab;
import com.yyqq.commen.service.IdentifyMsm;
import com.yyqq.commen.service.IdentifyPhone;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.GPSLocationUtils;
import com.yyqq.commen.utils.LoginUtils;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;
import com.yyqq.framework.callback.CallBackCode;

public class ChangePhone2 extends Activity {

	private Activity context;
	private String TAG = "ChangePhone2";
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
		setContentView(R.layout.exchange_phone2);
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
							changePhone();
						}else{
							Toast.makeText(context, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});
	}

	public void changePhone() {
		Config.showProgressDialog(context, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("mobile", phone);
		// Log.e(TAG, ServerMutualConfig.EditMobile + "&" + params.toString());
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
								TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
								SharedPreferences sp = context.getSharedPreferences("babyshow_login", Context.MODE_PRIVATE);
								updataUserInfo(phone, sp.getString("password", ""), TelephonyMgr.getDeviceId()+""); // 更新用户的数据
							} else {
								phone = "";
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
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
						Config.dismissProgress();
					}
				});
	}
	
	
	// 更新用户数据(重新登录)
	private void updataUserInfo(final String userName, final String pwd, final String imin){
//		StartActivity.this.runOnUiThread(new Runnable() {
			
//			@Override
//			public void run() {
				AbRequestParams params = new AbRequestParams();
				params.put("user_name", userName);
				if(pwd.length() != 32){
					params.put("password", Config.MD5(pwd));
				}else{
					params.put("password", pwd);
				}
				params.put("imin", imin);
				params.put("mapsign", GPSLocationUtils.getLatitude(context) + "," + GPSLocationUtils.getLongitude(context));
				ab.post(ServerMutualConfig.Login, params,
						new AbStringHttpResponseListener() {
							@Override
							public void onSuccess(int statusCode, String content) {
								super.onSuccess(statusCode, content);
								try {
									JSONObject json = new JSONObject(content);
									if (json.getBoolean("success")) {
										phone = "";
										MyApplication.setVisitor("0");
										if(pwd.length() != 32){
											LoginUtils.saveHistoryUserName(context, userName, Config.MD5(pwd), json.getJSONObject("data").getString("avatar"));
											LoginUtils.setUserLoginType(context, userName, Config.MD5(pwd));
										}else{
											LoginUtils.saveHistoryUserName(context, userName, pwd, json.getJSONObject("data").getString("avatar"));
											LoginUtils.setUserLoginType(context, userName, pwd);
										}
										SharedPreferences sp_user = context.getSharedPreferences("babyshow_user",Context.MODE_PRIVATE);
										Editor edit_user = sp_user.edit();
										edit_user.putString("user", content);
										edit_user.commit();
										
										if(null != ChangePhone.changePhone){
											ChangePhone.changePhone.finish();
										}
										Intent intent = new Intent(context, MainTab.class);
										intent.putExtra("tabid", 4);
										startActivity(intent);
										ChangePhone2.this.finish();
									} else {
										MyApplication.setVisitor("1");
										LoginUtils.setUserLoginType(context,"","");
										Toast.makeText(context, json.getString("reMsg"), Toast.LENGTH_SHORT).show();
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
							public void onFailure(int statusCode,
									String content, Throwable error) {
								super.onFailure(statusCode, content, error);
							}
						});
//			}
//		});
	}

}

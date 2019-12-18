package com.yyqq.code.login;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.babyshow.StartActivity;
import com.yyqq.code.toyslease.version_92.ToysLeaseSearchActivity;
import com.yyqq.commen.service.IdentifyMsm;
import com.yyqq.commen.service.IdentifyPhone;
import com.yyqq.commen.utils.Config;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;
import com.yyqq.framework.callback.CallBackCode;

/**
 * 注册-绑定手机号
 * */
public class RegisterBandPhoneActivity extends Activity {

	private Activity context;
	private String TAG = "fanfan_BandPhone";
	private AbHttpUtil ab;
	private RelativeLayout general_ly;

	private EditText inputPhone;
	private EditText inputCode;
	private Button commit;
	private Button getCode;
	private RelativeLayout band_intent;

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
		time = new TimeCount(30000, 1000);
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {// 计时完毕
			getCode.setText("获取验证码");
			getCode.setClickable(true);
			band_intent.setVisibility(View.VISIBLE);
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
		
		band_intent = (RelativeLayout) findViewById(R.id.band_intent);
		band_intent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// 登录成功但未绑定手机号
				MyApplication.setVisitor("0");
				SharedPreferences sp_login = RegisterBandPhoneActivity.this.getSharedPreferences("babyshow_login",Context.MODE_PRIVATE);
				Editor ed_login = sp_login.edit();
				ed_login.putString("is_mobile", "0");
				ed_login.commit();
				RegisterBandPhoneActivity.this.finish();
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
			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				if(!inputPhone.getText().toString().trim().isEmpty()){
					InputMethodManager inputMethodManager = (InputMethodManager) RegisterBandPhoneActivity.this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(inputPhone.getWindowToken(), 0);
					submitUserPhone();
					time.start();
					phone = inputPhone.getText().toString().trim().replaceAll("\\s*", "");
					code = "86";
					id.showDialog(phone, code, new CallBackCode() {
						
						@Override
						public void execute(String phone, String code,
								String formatedPhone) {
						}
					});
				}else{
					Toast.makeText(RegisterBandPhoneActivity.this, "请填写您的手机号", 2).show();
				}
			}
		});

		commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(inputPhone.getText().toString().trim()) || TextUtils.isEmpty(inputCode.getText().toString().trim())) {
					Toast.makeText(context, "请输入您的验证码", 0).show();
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
								
								// 修改登录标记
								MyApplication.setVisitor("0");
								SharedPreferences sp_login = RegisterBandPhoneActivity.this.getSharedPreferences("babyshow_login",Context.MODE_PRIVATE);
								Editor ed_login = sp_login.edit();
								ed_login.putString("is_mobile", "1");
								ed_login.commit();
								
								finish();
							} else {
								if (3008 == json.getInt("reCode")) {
									AlertDialog.Builder dialog = new Builder(context);
									dialog.setTitle("自由环球租赁");
									dialog.setMessage("手机号已被注册，是否绑定");
									dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// 修改登录标记
													MyApplication.setVisitor("0");
													SharedPreferences sp_login = RegisterBandPhoneActivity.this.getSharedPreferences("babyshow_login",Context.MODE_PRIVATE);
													Editor ed_login = sp_login.edit();
													ed_login.putString("is_mobile", "0");
													ed_login.commit();
												}

											});
									dialog.setNegativeButton("绑定", new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													addPhone();
												}
											});
									dialog.create().show();
								} else if(3009 == json.getInt("reCode")){
									AlertDialog.Builder dialog = new Builder(context);
									dialog.setTitle("自由环球租赁");
									dialog.setMessage("手机号已被绑定，请尝试手机号登陆");
									dialog.setPositiveButton("换手机号", new DialogInterface.OnClickListener() {

												@Override
												public void onClick(DialogInterface dialog, int which) {
													MyApplication.setVisitor("0");
													SharedPreferences sp_login = RegisterBandPhoneActivity.this.getSharedPreferences("babyshow_login",Context.MODE_PRIVATE);
													Editor ed_login = sp_login.edit();
													ed_login.putString("is_mobile", "0");
													ed_login.commit();
												}

											});
									dialog.setNegativeButton("去登陆", new DialogInterface.OnClickListener() {

												@Override
												public void onClick(DialogInterface arg0, int arg1) {
													MyApplication.setVisitor("1");
													SharedPreferences sp_login = RegisterBandPhoneActivity.this.getSharedPreferences("babyshow_login",Context.MODE_PRIVATE);
													Editor ed_login = sp_login.edit();
													ed_login.putString("is_mobile", "0");
													ed_login.commit();
													Intent in = new Intent(RegisterBandPhoneActivity.this, WebLoginActivity.class);
													RegisterBandPhoneActivity.this.startActivity(in);
													RegisterBandPhoneActivity.this.finish();
												}
											});
									dialog.create().show();
								}else{
									Toast.makeText(context, json.getString("reMsg"), Toast.LENGTH_SHORT).show();
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
								Toast.makeText(context, json.getString("reMsg"), Toast.LENGTH_SHORT).show();
								
								// 修改登录标记
								MyApplication.setVisitor("0");
								SharedPreferences sp_login = RegisterBandPhoneActivity.this.getSharedPreferences("babyshow_login",Context.MODE_PRIVATE);
								Editor ed_login = sp_login.edit();
								ed_login.putString("is_mobile", "1");
								ed_login.commit();
								
								finish();
							} else {
								
								// 修改登录标记
								MyApplication.setVisitor("0");
								SharedPreferences sp_login = RegisterBandPhoneActivity.this.getSharedPreferences("babyshow_login",Context.MODE_PRIVATE);
								Editor ed_login = sp_login.edit();
								ed_login.putString("is_mobile", "0");
								ed_login.commit();
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
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}
				});
	}

	/**
	 * 提交
	 * */
	private void submitUserPhone(){
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("mobile", inputPhone.getText().toString().trim());
		ab.post(ServerMutualConfig.SUBMIT_PHONE, params, new AbStringHttpResponseListener() {
			
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
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
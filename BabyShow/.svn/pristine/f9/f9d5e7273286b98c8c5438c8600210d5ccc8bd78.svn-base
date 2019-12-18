package com.yyqq.commen.service;

import org.json.JSONObject;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.UserInterruptException;
import cn.smssdk.utils.SMSLog;

import com.yyqq.commen.utils.Config;
import com.yyqq.framework.callback.CallBackCode;

public class IdentifyPhone {

	private OnSendMessageHandler osmHandler;
	private String phoneNum;
	private CallBackCode callback;
	private EventHandler handler;

	public IdentifyPhone(Activity context) {
		initHander(context);
	}

	/** 是否请求发送验证码，对话框 */
	private void initHander(final Activity context) {
		handler = new EventHandler() {
			public void afterEvent(final int event, final int result,
					final Object data) {
				context.runOnUiThread(new Runnable() {
					public void run() {
						if (result == SMSSDK.RESULT_COMPLETE) {
							if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
								// 请求验证码后，跳转到验证码填写页面
								boolean smart = (Boolean) data;
								afterVerificationCodeRequested(smart);
							}
						} else {
							Config.dismissProgress();
							if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE
									&& data != null
									&& (data instanceof UserInterruptException)) {
								// 由于此处是开发者自己决定要中断发送的，因此什么都不用做
								return;
							}

							int status = 0;
							// 根据服务器返回的网络错误，给toast提示
							try {
								((Throwable) data).printStackTrace();
								Throwable throwable = (Throwable) data;

								JSONObject object = new JSONObject(throwable
										.getMessage());
								String des = object.optString("detail");
								status = object.optInt("status");
								if (!TextUtils.isEmpty(des)) {
									Toast.makeText(context, des,
											Toast.LENGTH_SHORT).show();
									return;
								}
							} catch (Exception e) {
								SMSLog.getInstance().w(e);
							}
							// 如果木有找到资源，默认提示
							int resId = 0;
							if (status >= 400) {
//								resId = getStringRes(context,
//										"smssdk_error_desc_" + status);
							} else {
//								resId = getStringRes(context,
//										"smssdk_network_error");
							}

							if (resId > 0) {
								Toast.makeText(context, resId,
										Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
				SMSSDK.unregisterAllEventHandler();
			}
		};
	}

	public void registerEventHandler() {
	//	SMSSDK.registerEventHandler(handler);
	}

	/** 是否请求发送验证码，对话框 */
	public void showDialog(final String phone, final String code,
			CallBackCode callBackCode) {
		phoneNum = phone;
		this.callback = callBackCode;
		SMSSDK.registerEventHandler(handler);
		SMSSDK.getVerificationCode(code, phone.trim(), osmHandler);
	}

	/** 请求验证码后，跳转到验证码填写页面 */
	private void afterVerificationCodeRequested(boolean smart) {
		String phone = phoneNum.replaceAll("\\s*", "");
		String code = "86";
		if (code.startsWith("+")) {
			code = code.substring(1);
		}
		String formatedPhone = "+" + code + " " + splitPhoneNum(phone);
		// 验证码页面
		callback.execute(phone, code, formatedPhone);
//		android.util.Log.e("ffff smart false", phone + code + formatedPhone);

	}

	/** 分割电话号码 */
	private String splitPhoneNum(String phone) {
		StringBuilder builder = new StringBuilder(phone);
		builder.reverse();
		for (int i = 4, len = builder.length(); i < len; i += 5) {
			builder.insert(i, ' ');
		}
		builder.reverse();
		return builder.toString();
	}

	public void unregisterEventHandler() {
		//SMSSDK.unregisterEventHandler(handler);
	}
}

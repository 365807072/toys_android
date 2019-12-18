package com.yyqq.code.business;

import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.model.OrderItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OderDetail extends Activity {

	private Activity context;
	private String TAG = "fanfan_OderDetail";
	private AbHttpUtil ab;
	private MyApplication app;
	private OrderItem item;
	private RelativeLayout general_ly;
	private TextView general_title;
	private String order_id = "";
	private String business_id = "";
	private String myCombo1 = "";

	private TextView store_name; // 店铺名称
	private TextView pack; // 套餐详情
	private TextView time_state; // 过期时间
	private TextView address; // 商家地址
	private TextView order_num; // 订单号
	private TextView order_code; // 验证码
	private TextView order_time; // 下单时间
	private TextView payment; // 支付方式
	private TextView order_combo; // 套餐
	private TextView packet_price; // 红包价格
	private TextView price; // 价格
	private TextView num; // 数量
	private TextView state; // 消费状态
	private ImageView pay_bt; // 支付按钮
	private String isPay = "1"; // 支付按钮
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		setContentView(R.layout.oder_detail);

		context = this;
		ab = AbHttpUtil.getInstance(context);
		app = (MyApplication) context.getApplication();
		order_id = getIntent().getStringExtra("order_id");
		onRefresh();
		init();
	}

	private void init() {

		// 返回
		general_ly = (RelativeLayout) findViewById(R.id.general_ly);
		general_ly.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				context.finish();
			}
		});

		// 店铺名称
		store_name = (TextView) findViewById(R.id.store_name);
		// 套餐详情
		pack = (TextView) findViewById(R.id.pack);
		// 过期时间
		time_state = (TextView) findViewById(R.id.time_state);
		// 商家地址
		address = (TextView) findViewById(R.id.address);
		// 订单号
		order_num = (TextView) findViewById(R.id.oder_num);
		// 验证码
		order_code = (TextView) findViewById(R.id.oder_code);
		// 下单时间
		order_time = (TextView) findViewById(R.id.oder_time);
		// 支付方式
		payment = (TextView) findViewById(R.id.payment);
		// 套餐
		order_combo = (TextView) findViewById(R.id.oder_combo);
		// 红包价格
		packet_price = (TextView) findViewById(R.id.packet_price);
		// 价格
		price = (TextView) findViewById(R.id.price);
		// 数量
		num = (TextView) findViewById(R.id.num);
		// 消费状态
		state = (TextView) findViewById(R.id.state);
		// 支付按钮
		pay_bt = (ImageView) findViewById(R.id.pay_bt);
		pay_bt.setOnClickListener(payClick);
	}

	public View.OnClickListener payClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			AlertDialog.Builder builder = new Builder(context);
			if ("0".equals(isPay)) {
				builder.setMessage("退款将于1-3个工作日退回您的账户");
				builder.setPositiveButton("确认退款",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								AbRequestParams params = new AbRequestParams();
								params.put("login_user_id",
										Config.getUser(context).uid);
								params.put("order_id", order_id);
								// Log.e(TAG,
								// ServerMutualConfig.RefundOrder + "&"
								// + params.toString());
								ab.setTimeout(5000);
								// ab.get(ServerMutualConfig.WebRefundOrder +
								// "&"
								// + params.toString(),
								ab.get(ServerMutualConfig.RefundOrder + "&"
										+ params.toString(),
										new AbStringHttpResponseListener() {

											@Override
											public void onSuccess(
													int statusCode,
													String content) {
												super.onSuccess(statusCode,
														content);
												JSONObject json;
												try {
													json = new JSONObject(
															content);
													if (json.getBoolean("success")) {
														Toast.makeText(
																context,
																json.getString("reMsg"),
																Toast.LENGTH_SHORT)
																.show();
														onRefresh();
													} else {
														Toast.makeText(
																context,
																json.getString("reMsg"),
																Toast.LENGTH_SHORT)
																.show();
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
											public void onFailure(
													int statusCode,
													String content,
													Throwable error) {
												super.onFailure(statusCode,
														content, error);
											}

											@Override
											public void sendSuccessMessage(
													int statusCode,
													String content) {
												super.sendSuccessMessage(
														statusCode, content);
											}

										});
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();
			} else if ("3".equals(isPay)) {
				Intent intent = new Intent();
				intent.setClass(context, BusinessMsg.class);
				intent.putExtra("business_id", business_id);
				intent.putExtra("order_id", order_id);
				startActivity(intent);
			} else {
				Intent intent = new Intent();
				intent.setClass(context, PayActivity.class);
				intent.putExtra("business_id", business_id);
				// intent.putExtra("order_id", order_id);
				intent.putExtra("combo1", myCombo1);
				startActivity(intent);
			}
		}
	};

	public void onRefresh() {
		AbRequestParams params = new AbRequestParams();
		params.put("order_id", order_id);
		// Log.e(TAG, ServerMutualConfig.OrderDetailV1 + "&" +
		// params.toString());
		ab.setTimeout(5000);
		// ab.get(ServerMutualConfig.WebOrderDetail + "&" + params.toString(),
		ab.get(ServerMutualConfig.OrderDetailV1 + "&" + params.toString(),
				new AbStringHttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							item = new OrderItem();
							item.fromJson(json.getJSONObject("data"));
							getdate();

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

	private void getdate() {
		if (!TextUtils.isEmpty(item.business_title))
			store_name.setText(item.business_title);

		if (!TextUtils.isEmpty(item.business_package))
			pack.setText(item.business_package);

		if (!TextUtils.isEmpty(item.business_time))
			time_state.setText(item.business_time + " 过期");

		if (!TextUtils.isEmpty(item.address))
			address.setText(item.address);

		if (!TextUtils.isEmpty(item.order_num))
			order_num.setText(item.order_num);

		if (TextUtils.isEmpty(getIntent().getStringExtra("isBoss"))) {
			if (!TextUtils.isEmpty(item.verification))
				order_code.setText(item.verification);
		} else {
			order_code.setVisibility(View.GONE);
		}

		if (!TextUtils.isEmpty(item.order_time))
			order_time.setText(item.order_time);
		
		order_combo.setText(item.packageName);

		if (!TextUtils.isEmpty(item.packages)) {
			if ("1".equals(item.packages)) {
//				order_combo.setText("套餐一");
				myCombo1 = "1";
			} else if ("2".equals(item.packages)) {
//				order_combo.setText("套餐二");
				myCombo1 = "2";
			} else if ("3".equals(item.packages)) {
//				order_combo.setText("套餐三");
				myCombo1 = "3";
			} else {
//				order_combo.setText("套餐四");
				myCombo1 = "4";
			}
		}

		if (!TextUtils.isEmpty(item.packet_price)) {
			packet_price.setText("¥" + item.packet_price);
		}

		if (!TextUtils.isEmpty(item.payment_name)) {
			payment.setText(item.payment_name);
		}

		if (!TextUtils.isEmpty(item.price))
			price.setText("¥" + item.price);

		if (!TextUtils.isEmpty(item.number))
			num.setText(item.number);

		if (TextUtils.isEmpty(getIntent().getStringExtra("isBoss"))) {

			if (!TextUtils.isEmpty(item.status)) {
				if ("1".equals(item.status)) {
					state.setText("未消费");
					if ("5".equals(item.payment)) {
						pay_bt.setVisibility(View.GONE);
					} else {
						pay_bt.setBackgroundResource(R.drawable.refund);
					}
					isPay = "0";
				} else if ("2".equals(item.status)) {
					state.setText("未支付");
					pay_bt.setBackgroundResource(R.drawable.pay1);
					isPay = "1";
				} else if ("3".equals(item.status)) {
					state.setText("已完成");
					if ("0".equals(item.check_comment)) {
						pay_bt.setBackgroundResource(R.drawable.bt_ping_order);
					} else {
						pay_bt.setVisibility(View.GONE);
					}
					isPay = "3";
				} else if ("4".equals(item.status)) {
					state.setText("退款中");
					pay_bt.setVisibility(View.GONE);
				} else if ("5".equals(item.status)) {
					state.setText("已退款");
					pay_bt.setVisibility(View.GONE);
				} else {
					state.setText("待上门付款");
					pay_bt.setVisibility(View.GONE);
					isPay = "3";
				}
			}
		} else {
			pay_bt.setVisibility(View.GONE);
			if ("1".equals(item.status)) {
				state.setText("未消费");
				isPay = "0";
			} else if ("2".equals(item.status)) {
				state.setText("未支付");
				isPay = "1";
			} else if ("3".equals(item.status)) {
				state.setText("已完成");
				isPay = "3";
			} else if ("4".equals(item.status)) {
				state.setText("退款中");
			} else if ("5".equals(item.status)) {
				state.setText("已退款");
			} else {
				state.setText("待上门付款");
				isPay = "3";
			}
		}

		if (!TextUtils.isEmpty(item.business_id)) {
			business_id = item.business_id;
		}
	}

}

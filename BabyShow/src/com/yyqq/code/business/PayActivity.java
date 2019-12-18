package com.yyqq.code.business;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.GoodLife;
import com.yyqq.code.toyslease.ToysLeaseOrderConfirmActivity;
import com.yyqq.commen.model.OrderItem;
import com.yyqq.commen.model.PayResult;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.utils.MD5;
import com.yyqq.commen.utils.SignUtils;
import com.yyqq.commen.utils.SdcardUtil;
import com.yyqq.commen.utils.Util;
import com.yyqq.framework.application.Constants;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class PayActivity extends Activity {

	private Activity context;
	private String TAG = "fanfan_PayActivity";
	private AbHttpUtil ab;
	private MyApplication app;
	private OrderItem item;
	private RelativeLayout general_ly;
	private TextView general_title;
	private String business_id = "";
	private String combo1 = "";
	private String orderRole = "0";

	private TextView store_name; // 店铺名称
	private TextView price; // 价格
	private TextView oder_price; // 订单价格
	private Button pay_bt; // 支付按钮
	private TextView tv_packet;
	private TextView packet_price;
	private Button packet_pay;
	private RelativeLayout pay_zhifubao, pay_weixin, pay_yinlian, pay_shangmen; // 支付按钮
	private ImageView sel_pay1, sel_pay2, sel_pay3, sel_pay4;
	private String isSel = "1";
	private String packeySel = "0";
	private String pay_price, pay_name;
	private String myPayment = "";
	private String myPrice = ""; // 最终提交价格
	private String myPacket = "";
	private int allPacketPay = 0;  //全额红包付款
	PayReq req;
	IWXAPI msgApi;
	Map<String, String> resultunifiedorder;
	private int randomNum;

	// 商户PID
	public static final String PARTNER = "2088721930969145";
	// 商户收款账号  laozou@baobaoshowshow.com
	public static final String SELLER = "";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANRIycMm231D+OB2g6XK9h/7tS4ay1+KrucFVFheezfC/iLFsMNseHIdV11FzediiBA9X7aHxFSuSj0G4xD4ummmdOGvwNb7i5dpFPdjxdnAJqJC/iuM3toiOOutaXdNnaC9NFUgtSfY5IoMmti0/5nm8yRk4WV7YfJCwks4n1p7AgMBAAECgYBnh6AuttKwwuerwODviI6EhqOT+qlYzTADp0u9VUbOqSB8IOHWTR5ouPqUmKiUwi8NjIETah9MFTxLiwJOkp+GZ4HY4eLpww5Y+2YnVs8fpzdIFOZSYsOzThKGoW4ZL31Vd/tUt3So0A/atBCKGBUwSmbtUA6X/K478lQJQ/Ic8QJBAPKCkUj59lwpbjA+w2l0Uf9aWTYVIjSPK3wNjWKEgWbvljW+6AwBguOXB/Xhs3CmWoyI+U0Fywa6eAV/evVkcLUCQQDgF8ujUHvQrFAR9yoN5/H4+B54bT9J0DfiyWgIUEBDwyFo522KA5fZT+vAOgBWkWg8rHUTwfj5Rh/3tN9J4gxvAkBAlvP5GtI547L8WIsVWCzKtRaTp/dPRl6PkNB6T85jSyaXs/v7zp883Kn7HBz9wODXE1hK4mMbrKhw1m46U4ENAkBNOqYpoIErR1dI+b96j2crAIevxSa8j4/TDspVoyKit8r51lg/6kEY2ZxL4TFgpDgiQOUQbBccAXje62zQj6DtAkEA7AmWdzewdBbDxS8UoAjCDD2mosxll8J4pgSIxEv2OXLUDa+Y9u6fHZ/IkyLHSHVrRjN71z5MSAGgM5LsZhFCzg==";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDUSMnDJtt9Q/jgdoOlyvYf+7UuGstfiq7nBVRYXns3wv4ixbDDbHhyHVddRc3nYogQPV+2h8RUrko9BuMQ+LpppnThr8DW+4uXaRT3Y8XZwCaiQv4rjN7aIjjrrWl3TZ2gvTRVILUn2OSKDJrYtP+Z5vMkZOFle2HyQsJLOJ9aewIDAQAB";
	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	// 是否是第首次进入此页面
	public static int isOnrsum = 0;

	// 是否成功获取订单状态
	public static boolean isSuccess = false;

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		allPacketPay = 0;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		isOnrsum = 0; // 首次进入清零累计展示次数
		setContentView(R.layout.go_to_pay);
		context = this;
		msgApi = WXAPIFactory.createWXAPI(context, null);
		// 将该app注册到微信
		msgApi.registerApp(Constants.APP_ID);
		req = new PayReq();
		ab = AbHttpUtil.getInstance(context);
		app = (MyApplication) context.getApplication();
		business_id = getIntent().getStringExtra("business_id");
		orderRole = getIntent().getStringExtra("order_role");
		combo1 = getIntent().getStringExtra("combo1");
		onRefresh();
		init();
		randomNum = (int) (Math.random() * 100);

		GoodLife.getWechatApiKey(ab);
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
		// 价格
		price = (TextView) findViewById(R.id.price);
		oder_price = (TextView) findViewById(R.id.oder_price);
		// 支付方式
		pay_zhifubao = (RelativeLayout) findViewById(R.id.pay_zhifubao);
		pay_zhifubao.setOnClickListener(payClick1);
		pay_weixin = (RelativeLayout) findViewById(R.id.pay_weixin);
		pay_weixin.setOnClickListener(payClick2);
		pay_yinlian = (RelativeLayout) findViewById(R.id.pay_yinlian);
		pay_yinlian.setOnClickListener(payClick3);
		pay_shangmen = (RelativeLayout) findViewById(R.id.pay_shangmen);
		pay_shangmen.setOnClickListener(payClick4);
		// 教师角色的不支持在线支付
		if ("1".equals(orderRole)) {
			pay_shangmen.setVisibility(View.GONE);
		} else {
			pay_shangmen.setVisibility(View.VISIBLE);
		}
		// 是否支付
		sel_pay1 = (ImageView) findViewById(R.id.sel_pay1);
		sel_pay2 = (ImageView) findViewById(R.id.sel_pay2);
		sel_pay3 = (ImageView) findViewById(R.id.sel_pay3);
		sel_pay4 = (ImageView) findViewById(R.id.sel_pay4);
		// 支付按钮
		pay_bt = (Button) findViewById(R.id.pay_bt);
		pay_bt.setOnClickListener(payClick);
		// 红包
		packet_pay = (Button) findViewById(R.id.packet_pay);
		packet_pay.setOnClickListener(packetPayClick);
		packet_price = (TextView) findViewById(R.id.packet_price);
		tv_packet = (TextView) findViewById(R.id.tv_packet);
	}

	/**
	 * 支付宝回调
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT)
							.show();
					// toPay();
					Intent intent = new Intent();
					intent.setClass(context, UserOderList.class);
					intent.putExtra("showPacket", "showPacket");
					startActivity(intent);
					context.finish();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(PayActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(PayActivity.this, "暂不支持支付宝",
								Toast.LENGTH_SHORT).show();
					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(PayActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};

	public View.OnClickListener payClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			goToPay();
		}
	};
	private StringBuffer sb;

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

		// this.sb.append("sign str\n"+sb.toString()+"\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		// Log.e("orion", appSign);
		return appSign;
	}

	/**
	 * 支付宝支付按钮
	 */
	public View.OnClickListener payClick1 = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			isSel = "1";
			packet_pay.setClickable(true);
			sel_pay1.setBackgroundResource(R.drawable.sel_bt);
			sel_pay2.setBackgroundResource(R.drawable.sel_bt_no);
			sel_pay3.setBackgroundResource(R.drawable.sel_bt_no);
			sel_pay4.setBackgroundResource(R.drawable.sel_bt_no);
		}
	};
	/**
	 * 微信支付按钮
	 */
	public View.OnClickListener payClick2 = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			isSel = "2";
			packet_pay.setClickable(true);
			sel_pay1.setBackgroundResource(R.drawable.sel_bt_no);
			sel_pay2.setBackgroundResource(R.drawable.sel_bt);
			sel_pay3.setBackgroundResource(R.drawable.sel_bt_no);
			sel_pay4.setBackgroundResource(R.drawable.sel_bt_no);
		}
	};
	
	/**
	 * 银联支付按钮
	 */
	public View.OnClickListener payClick3 = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			isSel = "3";
			packet_pay.setClickable(true);
			sel_pay1.setBackgroundResource(R.drawable.sel_bt_no);
			sel_pay2.setBackgroundResource(R.drawable.sel_bt_no);
			sel_pay3.setBackgroundResource(R.drawable.sel_bt);
			sel_pay4.setBackgroundResource(R.drawable.sel_bt_no);
		}
	};
	/**
	 * 上门支付按钮
	 */
	public View.OnClickListener payClick4 = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			isSel = "4";
			sel_pay1.setBackgroundResource(R.drawable.sel_bt_no);
			sel_pay2.setBackgroundResource(R.drawable.sel_bt_no);
			sel_pay3.setBackgroundResource(R.drawable.sel_bt_no);
			sel_pay4.setBackgroundResource(R.drawable.sel_bt);
			packeySel = "0";
			oder_price.setText(item.price);
			myPrice = item.price;
			pay_price = myPrice;
			tv_packet.setText("上门支付不可用");
			packet_price.setVisibility(View.GONE);
			packet_pay.setBackgroundResource(R.drawable.sel_bt_no);
			packet_pay.setClickable(false);
		}
	};

	/**
	 * 选择红包支付按钮
	 */
	public View.OnClickListener packetPayClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (!"0".equals(myPacket)) {
				packet_pay.setClickable(true);
				if ("0".equals(packeySel)) {
					packet_pay.setBackgroundResource(R.drawable.sel_bt);
					tv_packet.setText("已自动选择最优红包 ");
					packet_price.setVisibility(View.VISIBLE);
					packet_price.setText(myPacket + "元");
					DecimalFormat df = new DecimalFormat("###.00");

					if ((Double.parseDouble(item.price) - Double
							.parseDouble(myPacket)) < 0 ||(Double.parseDouble(item.price) - Double
									.parseDouble(myPacket)) == 0) {
						oder_price.setText("¥0.00");
					} else {
						oder_price
								.setText("¥"
										+ df.format((Double
												.parseDouble(item.price) - Double
												.parseDouble(myPacket))) + "");
					}

					myPrice = df
							.format((Double.parseDouble(item.price) - Double
									.parseDouble(myPacket)));

					if (Double.parseDouble(myPrice) < 0 || Double.parseDouble(myPrice) == 0) {
						allPacketPay = 1;
						myPrice = "0.00";
					}

					pay_price = myPrice;
					packeySel = "1";
				} else {
					oder_price.setText("¥" + item.price);
					myPrice = item.price;
					pay_price = myPrice;
					packet_price.setVisibility(View.GONE);
					packet_pay.setBackgroundResource(R.drawable.sel_bt_no);
					tv_packet.setText("勾选可用红包支付 ");
					packeySel = "0";
				}
			} else {
				tv_packet.setText("暂无红包");
				packet_pay.setBackgroundResource(R.drawable.sel_bt_no);
				packet_pay.setClickable(false);
			}

		}
	};

	public void onRefresh() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("business_id", business_id);
		params.put("order_role", orderRole);
		params.put("package", combo1);
		// Log.e(TAG, ServerMutualConfig.PublicOrder + "&" + params.toString());
		ab.setTimeout(5000);
		// ab.get(ServerMutualConfig.WebPublicOrder + "&" + params.toString(),
		ab.get(ServerMutualConfig.PublicOrder + "&" + params.toString(),
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

	public void toPay() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("order_id", item.order_id);
		params.put("payment", myPayment);
		params.put("price", myPrice);
		// Log.e(TAG, ServerMutualConfig.PayOrder + "&" + params.toString());
		ab.setTimeout(5000);
		// ab.get(ServerMutualConfig.WebPayOrder + "&" + params.toString(),
		ab.get(ServerMutualConfig.PayOrder + "&" + params.toString(),
				new AbStringHttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Intent intent = new Intent();
						intent.setClass(context, UserOderList.class);
						startActivity(intent);
						context.finish();
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
		myPacket = item.packet_price;
		// 教师角色的不支持在线支付 （0正常、1线上、2上门）
		if ("1".equals(item.business_payment)) {
			pay_shangmen.setVisibility(View.GONE);
		} else if ("2".equals(item.business_payment)) {
			pay_zhifubao.setVisibility(View.GONE);
			pay_weixin.setVisibility(View.GONE);
		}

		// 有无红包提示
		if (!"0".equals(myPacket)) {
			tv_packet.setText("选择可用红包支付");
		} else {
			tv_packet.setText("暂无红包");
		}
		if (!TextUtils.isEmpty(item.business_title)) {
			store_name.setText(item.business_title);
			pay_name = item.business_title;
		}

		if (!TextUtils.isEmpty(item.need_price)) {
			price.setText(item.need_price);
		}

		if (!TextUtils.isEmpty(item.price)) {
			myPrice = item.price;
			oder_price.setText("¥" + myPrice);
		}

	}

	// 支付宝
	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		// orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";
		orderInfo += "&out_trade_no=" + "\"" + item.order_id + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + myPrice + "\"";

		// 服务器异步通知页面路径
		/**
		 * 正式环境
		 */
		orderInfo += "&notify_url=" + "\""
				+ "http://checkpic.meimei.yihaoss.top/BusPay/notifyurlorder"
				+ "\"";
		/**
		 * v17
		 */
		// orderInfo += "&notify_url=" + "\""
		// + "http://checkpic.meimei.yihaoss.top/Pay1/notifyurlorder2"
		// + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	// 微信
	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	private void winxinpay() {
		GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
		getPrepayId.execute();
	}

	private void genPayReq() {
		// 调起微信支付的关键代码
		req.appId = Constants.APP_ID;
		req.partnerId = Constants.MCH_ID;
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
		req.sign = genAppSign(signParams); // 签名
		// sb.append("sign\n"+req.sign+"\n\n");
		sendPayReq();
	}

	// 微信支付第一步
	private class GetPrepayIdTask extends
			AsyncTask<Void, Void, Map<String, String>> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onPostExecute(Map<String, String> result) {
			if (dialog != null) {
				dialog.dismiss();
			}
			Config.dismissProgress();
			// sb.append("prepay_id\n"+result.get("prepay_id")+"\n\n");
			// android.util.Log.v("fff", "di yi bu  :"+sb.toString());

			resultunifiedorder = result;
			genPayReq();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Config.dismissProgress();
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {

			String url = String
					.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = genProductArgs();

			// Log.e("fanfan_orion", entity);

			byte[] buf = Util.httpPost(url, entity);

			String content = new String(buf);
			// Log.e("fanfan_orion", content);
			Map<String, String> xml = decodeXml(content);

			return xml;
		}
	}

	// 微信支付第三步
	private void sendPayReq() {
		Constants.order_id = item.order_id;
		Constants.price = myPrice;
		msgApi.registerApp(Constants.APP_ID);
		msgApi.sendReq(req);
	}

	//
	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();

		try {
			String nonceStr = genNonceStr();
			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams
					.add(new BasicNameValuePair("appid", Constants.APP_ID));
			packageParams.add(new BasicNameValuePair("body", pay_name));
			packageParams
					.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			/**
			 * 正式环境
			 */
			packageParams.add(new BasicNameValuePair("notify_url",
					"http://checkpic.meimei.yihaoss.top/Wxpay/notifyurl"));
			packageParams.add(new BasicNameValuePair("out_trade_no",
					item.order_id + "a" + randomNum));
			/**
			 * v17
			 */
			// packageParams
			// .add(new BasicNameValuePair("notify_url",
			// "http://checkpic.meimei.yihaoss.top/Wxpay/notifyurlorder2"));
			//
			// packageParams.add(new BasicNameValuePair("out_trade_no",
			// item.order_id + "a" + randomNum + "b2"));

			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					"127.0.0.1"));
			double price = Double.valueOf(myPrice);
			long price1 = (long) (price * 100);
			packageParams.add(new BasicNameValuePair("total_fee", price1 + ""));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));

			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));

			String xmlstring = toXml(packageParams);
			return new String(xmlstring.toString().getBytes(), "ISO8859-1");

		} catch (Exception e) {
			Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
			return null;
		}

	}

	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");

		// Log.e("orion", sb.toString());
		return sb.toString();
	}

	/**
	 * 生成签名
	 */

	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		// Log.e("orion", packageSign);
		return packageSign;
	}

	private String genOutTradNo() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	public Map<String, String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:

					break;
				case XmlPullParser.START_TAG:

					if ("xml".equals(nodeName) == false) {
						// 实例化student对象
						xml.put(nodeName, parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion", e.toString());
		}
		return null;

	}

	@Override
	protected void onResume() {
		isOnrsum += 1; // 修改本页面重新吊起次数
		if (isOnrsum > 1) {
			checkOrderType(item.order_id, false); // 检查订单状态
		}
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	

	/**
	 * 检查订单状态
	 * */
	private void checkOrderType(String orderId, final boolean toPay) {
		pay_bt.setClickable(false);
		Config.showProgressDialog(context, false, null);
		AbHttpUtil abhttp = AbHttpUtil.getInstance(context);
		abhttp.setDebug(Log.isDebug);
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(PayActivity.this).uid);
		params.put("order_id", orderId);
		abhttp.get(ServerMutualConfig.CHECK_ORDERTYPE + "&" + params.toString(), new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						Config.dismissProgress();
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								isSuccess = true;
								if (json.getJSONObject("data")
										.getString("status").equals("2")) { // 订单未支付
									pay_bt.setClickable(true);
									if (toPay) {
										goToPay();
									}
								} else { // 其他状态直接进入订单详情页
									Toast.makeText(context, "此订单已完成支付",
											Toast.LENGTH_LONG).show();
									Intent intent = new Intent();
									intent.setClass(context, UserOderList.class);
									intent.putExtra("showPacket", "showPacket");
									startActivity(intent);
									context.finish();
								}
							} else {
								isSuccess = false;
							}
						} catch (JSONException e) {
							e.printStackTrace();
							isSuccess = false;
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						Config.dismissProgress();
						isSuccess = false;
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
						Config.dismissProgress();
						isSuccess = false;
					}
				});
	}

	/**
	 * 调起支付
	 * */
	@SuppressLint("NewApi")
	private void goToPay() {
		if (isOnrsum > 1 && !isSuccess) { // 如果非首次进入并且支付状态未能获取成功，就需要再次查询
			checkOrderType(item.order_id, true);
		} else {
			if (1 == allPacketPay) {
				myPayment = "6";
				toPay();
			} else if ("0.00".equals(myPrice) || "0.0".equals(myPrice)) {
				myPayment = "5";
				toPay();
			}else {
				if ("1".equals(isSel)) {
					Toast.makeText(this, "暂不支持支付宝", Toast.LENGTH_SHORT).show();
//					myPayment = "1";
//					// 订单
//					String orderInfo = getOrderInfo(pay_name, pay_name,
//							pay_price);
//
//					// 对订单做RSA 签名
//					String sign = sign(orderInfo);
//					try {
//						// 仅需对sign 做URL编码
//						sign = URLEncoder.encode(sign, "UTF-8");
//					} catch (UnsupportedEncodingException e) {
//						e.printStackTrace();
//					}
//
//					// 完整的符合支付宝参数规范的订单信息
//					final String payInfo = orderInfo + "&sign=\"" + sign
//							+ "\"&" + getSignType();
//
//					// android.util.Log.e("fanfan_orderInfo", orderInfo);
//					// android.util.Log.e("fanfan_payInfo", payInfo);
//					Runnable payRunnable = new Runnable() {
//
//						@Override
//						public void run() {
//							// 构造PayTask 对象
//							PayTask alipay = new PayTask(PayActivity.this);
//							// 调用支付接口，获取支付结果
//							String result = alipay.pay(payInfo);
//
//							Message msg = new Message();
//							msg.what = SDK_PAY_FLAG;
//							msg.obj = result;
//							mHandler.sendMessage(msg);
//						}
//					};
//					// 必须异步调用
//					Thread payThread = new Thread(payRunnable);
//					payThread.start();
				} else if ("2".equals(isSel)) {
					if(!Constants.API_KEY.isEmpty()){
						Config.showProgressDialog(context, false, null);
						winxinpay();
					}else{
						GoodLife.getWechatApiKey(ab);
					}
				} else if ("4".equals(isSel)) {
					myPayment = "4";
					toPay();
				}
			}
		}
	}
}

package com.yyqq.code.toyslease;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.babyshow.wxapi.WXPayEntryActivity;
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.code.main.GoodLife;
import com.yyqq.code.toyslease.h5.ToysLeaseHtmlInterstActivity;
import com.yyqq.code.toyslease.version_92.ToysLeaseCategoryActivity;
import com.yyqq.code.toyslease.version_92.ToysLeaseMainAllActivity;
import com.yyqq.code.toyslease.version_92.ToysLeaseMainShoppingCartActivity;
import com.yyqq.code.toyslease.version_92.ToysLeaseSearchActivity;
import com.yyqq.commen.adapter.ToysLeaseOrderInfoAdapter;
import com.yyqq.commen.model.OrderItem;
import com.yyqq.commen.model.PayResult;
import com.yyqq.commen.model.ToysLeaseOrderInfoBean;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.MD5;
import com.yyqq.commen.utils.SignUtils;
import com.yyqq.commen.utils.Util;
import com.yyqq.commen.view.RecodeListView;
import com.yyqq.commen.view.SelectTimeView;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.Constants;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 玩具世界-确认支付
 * */
@SuppressLint("NewApi")
public class ToysLeasePayConfirmActivity extends BaseActivity {

	private ImageView sel_pay1;
	private ImageView sel_pay2;
	private Button pay_bt;
	private RecodeListView lease_order_list;
	private ScrollView lease_order_sc;
	private LinearLayout cart_ntf;
	private TextView toys_pay_hint_text;
	private TextView lease_order_allMonery;
	private ImageView lease_pay_type_hint;
	private RelativeLayout lease_pay_all;
	
	public static String payFrom = "";
	public static String order_id = "";
	public static String combined_id = "";
	public static String ORDER_ID_KEY = "combined_id";
	private AbHttpUtil ab;
	private LayoutInflater inflater;
	public static ToysLeasePayConfirmActivity toysLeasePayConfirmActivity = null;
	public static boolean showShared = false;
	
	// 支付相关
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
	
	private PayReq req;
	private IWXAPI msgApi;
	private Map<String, String> resultunifiedorder;
	private int randomNum;
	private String isSel = "1";
	private OrderItem item;
	private String myPrice = ""; // 最终提交价格
	private String pay_name = "";
	private boolean isWZPay = true; // 是否需要微信/支付宝支付
	
	// 优惠券相关
	private RelativeLayout pay_packet;
	private TextView account_hint_text;
	private Button packet_pay;
	private String source = "0";
	private String is_balance = "0";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		setContentView(R.layout.activity_toys_lease_pay);
	}
	
	@Override
	protected void initView() {
		lease_pay_all = (RelativeLayout) findViewById(R.id.lease_pay_all);
		lease_pay_type_hint = (ImageView) findViewById(R.id.lease_pay_type_hint);
		lease_order_allMonery = (TextView) findViewById(R.id.lease_order_allMonery);
		cart_ntf = (LinearLayout) findViewById(R.id.cart_ntf);
		toys_pay_hint_text = (TextView) findViewById(R.id.toys_pay_hint_text);
		packet_pay = (Button) findViewById(R.id.packet_pay);
		account_hint_text = (TextView) findViewById(R.id.account_hint_text);
		pay_packet = (RelativeLayout) findViewById(R.id.pay_packet);
		sel_pay1 = (ImageView) findViewById(R.id.sel_pay1);
		sel_pay2 = (ImageView) findViewById(R.id.sel_pay2);
		pay_bt = (Button) findViewById(R.id.pay_bt);
		lease_order_list = (RecodeListView) findViewById(R.id.lease_order_list);
		lease_order_sc = (ScrollView) findViewById(R.id.lease_order_sc);
		lease_order_list.setFocusable(false); // 消除listview焦点
		lease_order_sc.smoothScrollTo(0,20); // scrollView置顶
		lease_order_sc.requestFocus();
	}

	@Override
	protected void initData() {
		toysLeasePayConfirmActivity = this;
		item = new OrderItem();
		item.order_id = getIntent().getStringExtra(ORDER_ID_KEY);
		msgApi = WXAPIFactory.createWXAPI(ToysLeasePayConfirmActivity.this, null);
		// 将该app注册到微信
		msgApi.registerApp(Constants.APP_ID);
		req = new PayReq();
		randomNum = (int) (Math.random() * 100);
		GoodLife.getWechatApiKey(ab);
		isOnrsum = 0; // 首次进入清零累计展示次数
		SelectTimeView view = new SelectTimeView(ToysLeasePayConfirmActivity.this);
		ab = AbHttpUtil.getInstance(ToysLeasePayConfirmActivity.this);
		inflater = LayoutInflater.from(ToysLeasePayConfirmActivity.this);
		
		// 隐藏软键盘
		lease_order_sc.requestFocus();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		
		// 获取待支付金额
		getToysOrderPrice();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void setListener() {
		
		packet_pay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				source = "1";
				if(is_balance.equals("0")){
					is_balance = "1";
				}else{
					is_balance = "0";
				}
				editBalanceType();
			}
		});
		
		// 支付宝支付
		sel_pay1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isSel = "1";
				sel_pay1.setBackgroundResource(R.drawable.sel_bt);
				sel_pay2.setBackgroundResource(R.drawable.sel_bt_no);
			}
		});
		
		// 微信支付
		sel_pay2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isSel = "2";
				sel_pay1.setBackgroundResource(R.drawable.sel_bt_no);
				sel_pay2.setBackgroundResource(R.drawable.sel_bt);
			}
		});
		
		findViewById(R.id.lease_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToysLeasePayConfirmActivity.this.finish();
			}
		});
		
		pay_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!MyApplication.getVisitor().equals("0")  || Config.getUser(ToysLeasePayConfirmActivity.this).uid.equals("")){
					Intent in = new Intent(ToysLeasePayConfirmActivity.this, WebLoginActivity.class);
					startActivity(in);
				}else{
					pay();
				}
			}
		});
	}
	
	/**
	 * 获取支付金额
	 * */
	private void getToysOrderPrice(){
		Config.showProgressDialog(ToysLeasePayConfirmActivity.this, false, null);
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(ToysLeasePayConfirmActivity.this).uid);
		params.put("combined_order_id", item.order_id);
		
		if(!ToysLeaseDetailActivity.invite_user_id.equals("") && !ToysLeaseDetailActivity.invite_user_id.equals("0")){
			params.put("invite_user_id", ToysLeaseDetailActivity.invite_user_id);
		}
		
		ab.get(ServerMutualConfig.GET_TOYS_ORDER_PRICE + "&" + params.toString(),new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Config.dismissProgress();
				initPage(content);
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
			}
		});
	}
	
	/**
	 * 是否使用优惠券
	 * */
	private void editBalanceType(){
		Config.showProgressDialog(ToysLeasePayConfirmActivity.this, false, null);
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(ToysLeasePayConfirmActivity.this).uid);
		params.put("combined_order_id", getIntent().getStringExtra(ORDER_ID_KEY));
		params.put("is_balance", is_balance);
		ab.get(ServerMutualConfig.EDIT_BALANCE_PRICE + "&" + params.toString(),new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Config.dismissProgress();
				initPage(content);
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
			}
		});
	}
	
	/**
	 * 初始化页面数据
	 * */
	private void initPage(String content){
		try {
			if(!content.isEmpty() && new JSONObject(content).getBoolean("success")){
				// 订单相关
				ToysLeaseDetailActivity.invite_user_id = "0";
				JSONObject json = new JSONObject(content);
				item.order_id = json.getJSONObject("data").getString("combined_order_id");
				combined_id = json.getJSONObject("data").getString("combined_order_id");
				myPrice = new JSONObject(content).getJSONObject("data").getString("pay_total_price");
//				myPrice = "0.01";
				lease_order_allMonery.setText(new JSONObject(content).getJSONObject("data").getString("total_title") + new JSONObject(content).getJSONObject("data").getString("total_price"));
				pay_name = new JSONObject(content).getJSONObject("data").getString("pay_title");
				order_id = new JSONObject(content).getJSONObject("data").getString("order_id");
				
				// 顶部提示语
				if(!new JSONObject(content).getJSONObject("data").getString("prompt_title").isEmpty()){
					cart_ntf.setVisibility(View.VISIBLE);
					toys_pay_hint_text.setText(new JSONObject(content).getJSONObject("data").getString("prompt_title"));
				}else{
					cart_ntf.setVisibility(View.GONE);
				}
				
				// 是否需要微信/支付宝支付
				if(new JSONObject(content).getJSONObject("data").getString("is_payment").equals("1")){
					isWZPay = true;
				}else{
					isWZPay = false;
				}
				
				// 是否显示优惠信息
				if(json.getJSONObject("data").getString("show_balance").equals("0")){
					pay_packet.setVisibility(View.GONE);
				}else{
					pay_packet.setVisibility(View.VISIBLE);
					account_hint_text.setText(json.getJSONObject("data").getString("balance_info"));
				}
				
				// 是否默认使用余额
				if(json.getJSONObject("data").getString("is_balance").equals("1")){
					packet_pay.setBackgroundResource(R.drawable.sel_bt);
					is_balance = "1";
				}else{
					packet_pay.setBackgroundResource(R.drawable.sel_bt_no);
					is_balance = "0";
				}
				
				// 付款明细列表
				ArrayList<ToysLeaseOrderInfoBean> bodyList = new ArrayList<ToysLeaseOrderInfoBean>();
				ToysLeaseOrderInfoBean bean = null;
				for(int i = 0 ; i < new JSONObject(content).getJSONObject("data").getJSONArray("price").length() ; i++){
					bean = new ToysLeaseOrderInfoBean();
					bean.setHint(new JSONObject(content).getJSONObject("data").getJSONArray("price").getJSONObject(i).getString("price_title"));
					bean.setNumber(new JSONObject(content).getJSONObject("data").getJSONArray("price").getJSONObject(i).getString("sell_price"));
					bodyList.add(bean);
				}
				ToysLeaseOrderInfoAdapter adapter = new ToysLeaseOrderInfoAdapter(ToysLeasePayConfirmActivity.this, inflater, bodyList);
				lease_order_list.setAdapter(adapter);
			}else{
				if(new JSONObject(content).getString("reCode").equals("3000")){ // 订单已支付
					finishOtherActivity(ToysLeasePayConfirmActivity.this);
				}else{ // 订单异常 
					Toast.makeText(ToysLeasePayConfirmActivity.this, new JSONObject(content).getString("reMsg"), 2).show();
					lease_pay_type_hint.setVisibility(View.VISIBLE);
					lease_pay_all.setVisibility(View.GONE);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(ToysLeasePayConfirmActivity.this, "未能成功获取支付金额！", 2).show();
			lease_pay_type_hint.setVisibility(View.VISIBLE);
			lease_pay_all.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 调起支付页面
	 * */
	private void pay(){
		if(isWZPay){
			if (isOnrsum > 1 && !isSuccess) { // 如果非首次进入并且支付状态未能获取成功，就需要再次查询
				getToysOrderPrice();
			} else {
//				Config.showProgressDialog(ToysLeasePayConfirmActivity.this, false, null);  
				if ("1".equals(isSel)) {
					Toast.makeText(ToysLeasePayConfirmActivity.this, "暂不支持支付宝", Toast.LENGTH_SHORT).show();
//					payFrom = "zhifubao";
//					// 订单
//					String orderInfo = getOrderInfo(pay_name, pay_name, myPrice);
//					// 对订单做RSA 签名
//					String sign = sign(orderInfo);
//					try {
//						// 仅需对sign 做URL编码
//						sign = URLEncoder.encode(sign, "UTF-8");
//					} catch (UnsupportedEncodingException e) {
//						e.printStackTrace();
//					}
//					// 完整的符合支付宝参数规范的订单信息
//					final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
//					Runnable payRunnable = new Runnable() {
//						
//						@Override
//						public void run() {
//							// 构造PayTask 对象
//							PayTask alipay = new PayTask(ToysLeasePayConfirmActivity.this);
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
						payFrom = "weixin";
						winxinpay();
					}else{
						GoodLife.getWechatApiKey(ab);
					}
				}
			}
		}else{
//			Config.showProgressDialog(ToysLeasePayConfirmActivity.this, false, null);
			final AbRequestParams params = new AbRequestParams();
			params.put("login_user_id", Config.getUser(ToysLeasePayConfirmActivity.this).uid);
			params.put("price", myPrice);
			params.put("combined_order_id", item.order_id);
			ab.get(ServerMutualConfig.PAY_TO_ORDER + "&" + params.toString(),new AbStringHttpResponseListener() {
				@Override
				public void onSuccess(int statusCode, String content) {
					super.onSuccess(statusCode, content);
					Config.dismissProgress();
					try {
						if(!content.isEmpty() && new JSONObject(content).getBoolean("success")){
							finishOtherActivity(ToysLeasePayConfirmActivity.this);
						}else{
							Toast.makeText(ToysLeasePayConfirmActivity.this, new JSONObject(content).getString("reMsg"), 2).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
						Toast.makeText(ToysLeasePayConfirmActivity.this, "支付失败！", 2).show();
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
	}
	
	/**
	 * 支付成功后
	 * */
	public static void finishOtherActivity(Context con){
		
		
		
		if(null != ToysLeasePayConfirmActivity.toysLeasePayConfirmActivity){
			ToysLeasePayConfirmActivity.toysLeasePayConfirmActivity.finish();
		}
		
		if(null != ToysLeaseOrderConfirmActivity.toysLeaseOrderConfirmActivity){
			ToysLeaseOrderConfirmActivity.toysLeaseOrderConfirmActivity.finish();
		}
		
		if(null != ToysLeaseOrderDetailActivity.toysLeaseOrderDetailActivity){
			ToysLeaseOrderDetailActivity.toysLeaseOrderDetailActivity.finish();
		}
		
		// 是否是批量支付
		if(ToysLeasePayConfirmActivity.order_id.isEmpty()){
			
			if(null != ToysLeaseHtmlInterstActivity.toysLeaseHtmlInterstActivity){
				ToysLeaseHtmlInterstActivity.toysLeaseHtmlInterstActivity.finish();
			}
			
			if(null != ToysLeaseMainShoppingCartActivity.toysLeaseMainShoppingCartActivity){
				ToysLeaseMainShoppingCartActivity.toysLeaseMainShoppingCartActivity.finish();
			}
			
			if(null != ToysLeaseCategoryActivity.toysLeaseCategoryActivity){
				ToysLeaseCategoryActivity.toysLeaseCategoryActivity.finish();
			}
			
			if(null != ToysLeaseMainAllActivity.toysLeaseMainActivity){
				ToysLeaseMainAllActivity.toysLeaseMainActivity.finish();
			}
			
			if(null != WXPayEntryActivity.wXPayEntryActivity){
				WXPayEntryActivity.wXPayEntryActivity.finish();
			}
			
			if(null != ToysLeaseDetailActivity.toysLeaseDetailActivity){
				ToysLeaseDetailActivity.toysLeaseDetailActivity.finish();
			}
			
			if(null != ToysLeaseMainTabActivity.toysLeaseMainTabActivity){
				ToysLeaseMainTabActivity.toysLeaseMainTabActivity.finish();
			}

			if(null != ToysLeaseSearchActivity.toysLeaseSearchActivity){
				ToysLeaseSearchActivity.toysLeaseSearchActivity.finish();
			}
			
			showShared = true;
			
			// 跳转到订单列表
			Intent intent = new Intent(con, ToysLeaseMainTabActivity.class);
			intent.putExtra("tabid", 3);
			con.startActivity(intent);
		}else{
			
			showShared = true;
			
			// 跳转到订单详情
			Intent intent = new Intent(con, ToysLeaseOrderDetailActivity.class);
			intent.putExtra(ToysLeaseOrderDetailActivity.ORDER_ID_KEY, ToysLeasePayConfirmActivity.order_id);
			con.startActivity(intent);
		}
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
					Toast.makeText(ToysLeasePayConfirmActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
					finishOtherActivity(ToysLeasePayConfirmActivity.this);
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(ToysLeasePayConfirmActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(ToysLeasePayConfirmActivity.this, "暂不支持支付宝", Toast.LENGTH_SHORT).show();
					
//						pay_bt.setClickable(true);
					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(ToysLeasePayConfirmActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};
	
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
		orderInfo += "&out_trade_no=" + "\"" + item.order_id + "d4\"";

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
			packageParams.add(new BasicNameValuePair("out_trade_no", item.order_id + "a" + randomNum+ "d4"));
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
//			Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
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
	
}
package com.yyqq.code.toyslease.h5;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.code.main.MainItemDetialActivity;
import com.yyqq.code.toyslease.InviteFriendActivity;
import com.yyqq.code.toyslease.ToysLeaseDepositActivity;
import com.yyqq.code.toyslease.ToysLeaseMainTabActivity;
import com.yyqq.code.toyslease.ToysLeaseOrderConfirmActivity;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.MyWebviewUtils;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.MyApplication;

public class ToysLeaseHtmlMainOrderActivity extends BaseActivity {

	private WebView webView = null;
	private ArrayList<String> urlList; // 加载过的url记录
	private RelativeLayout web_title;
	private TextView web_title_center;
	private ImageView web_title_left;
	
	public static ToysLeaseHtmlMainOrderActivity toysLeaseMainTextActivity = null;
	private String order_id = "";
	private boolean isIntent = false;
	private boolean isLoadUrl = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		setContentView(R.layout.activity_toys_lease_main_text);
	}

	@Override
	protected void initView() {
		web_title = (RelativeLayout) findViewById(R.id.web_title);
		web_title_center = (TextView) findViewById(R.id.web_title_center);
		web_title_left = (ImageView) findViewById(R.id.web_title_left);
		
		urlList = new ArrayList<String>();
		webView = (WebView) findViewById(R.id.toyslease_webview);
		MyWebviewUtils.initWebView(this, webView, new JSCallback(), true);
		// html加载结束再加载js方法，否则js可能会不起作用
		webView.setWebViewClient(new WebViewClient() { 
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				checkUrlHistory(url); // 加入url管理集合
				return true;
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				webView.setVisibility(View.VISIBLE);
				findViewById(R.id.toys_to_settings).setVisibility(View.GONE);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				showUpdatePage();
			}
			
		});
		
	}
		

	@Override
	protected void initData() {
		isLoadUrl = false;
		toysLeaseMainTextActivity = this;
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * url历史管理
	 * */
	private void checkUrlHistory(String url){
		if(!urlList.toString().contains(url)){
			urlList.add(url);
		}else{
			for(int i = 0 ; i < urlList.size() ; i++){
				if(urlList.get(i).equals(url)){
					urlList.remove(i);
				}
			}
			urlList.add(url);
		}
	}
	
	/**
	 * JS回调的原生方法
	 * */
	public class JSCallback{
		
		/**
		 * 进入登录页面
		 * */
		@JavascriptInterface
		public void callMethodToInterestPage(){
			Intent intent = new Intent(ToysLeaseHtmlMainOrderActivity.this, ToysLeaseHtmlInterstActivity.class);
			startActivity(intent);
		}
		
		/**
		 * 进入登录页面
		 * */
		@JavascriptInterface
		public void callMethodToLoginPage(){
			Intent intent = new Intent(ToysLeaseHtmlMainOrderActivity.this, WebLoginActivity.class);
			startActivity(intent);
		}
		
		/**
		 * 获取用户ID
		 * */
		@JavascriptInterface
		public String callMethodGetLoginUserId(){
			return Config.getUser(ToysLeaseHtmlMainOrderActivity.this).uid;
		}
		
		/**
		 * 判断用户是否已经登录
		 * */
		@JavascriptInterface
		public String callMethodIsLogin(){
			if(!MyApplication.getVisitor().equals("0")  || Config.getUser(ToysLeaseHtmlMainOrderActivity.this).uid.equals("")){
				return "0"; // 未登录
			}else{
				return "1"; // 已登陆
			}
		}
		
		
		/**
		 * 进入玩具筛选列表
		 * */
		@JavascriptInterface
		public void callMethodToToysList(String toys_type){
			if(null == toys_type || toys_type.equals("") || toys_type.equals("undefined")){
				Toast.makeText(ToysLeaseHtmlMainOrderActivity.this, "找不到这组玩具哦~", 3).show();
				return;
			}
//			Intent intent = new Intent(ToysLeaseMainTextActivity.this, ToysLeaseListActivity.class);
//			intent.putExtra(ToysLeaseListActivity.TOYS_TYPE, toys_type);
//			startActivity(intent);
		}
		
		/**
		 * 进入玩具详情
		 * */
		@JavascriptInterface
		public void callMethodToToysDetail(String toys_id){
			if(null == toys_id || toys_id.equals("") || toys_id.equals("undefined")){
				Toast.makeText(ToysLeaseHtmlMainOrderActivity.this, "找不到这个玩具哦~", 3).show();
				return;
			}
			Intent intent = new Intent(ToysLeaseHtmlMainOrderActivity.this, ToysLeaseHtmlDetailActivity.class);
			intent.putExtra(ToysLeaseHtmlDetailActivity.TOYS_DETAIL_KEY, toys_id);
			startActivity(intent);
		}
		
		/**
		 * 进入订单详情
		 * */
		@JavascriptInterface
		public void callMethodToToysOrderDetail(String order_id){
			if(null == order_id || order_id.equals("") || order_id.equals("undefined")){
				Toast.makeText(ToysLeaseHtmlMainOrderActivity.this, "找不到这个订单哦~", 3).show();
				return;
			}
//			String url = "http://www.meimei.yihaoss.top/H5/toy_count/orderAll_cout.html?login_user_id="+Config.getUser(ToysLeaseHtmlMainOrderActivity.this).uid+"&combined_order_id="+order_id;
			Intent intent = new Intent(ToysLeaseHtmlMainOrderActivity.this, ToysLeaseHtmlOrderToysDetailActivity.class);
			intent.putExtra(ToysLeaseHtmlOrderToysDetailActivity.ORDER_ID_KEY, order_id);
			ToysLeaseHtmlMainOrderActivity.this.startActivity(intent);
		}
		
		/**
		 * 进入会员卡详情
		 * */
		@JavascriptInterface
		public void callMethodToToysCartDetail(String order_id){
			if(null == order_id || order_id.equals("") || order_id.equals("undefined")){
				Toast.makeText(ToysLeaseHtmlMainOrderActivity.this, "找不到这个订单哦~", 3).show();
				return;
			}
//			String url = "http://www.meimei.yihaoss.top/H5/toy_count/order_cout.html?login_user_id="+Config.getUser(ToysLeaseHtmlMainOrderActivity.this).uid+"&order_id="+order_id;
			Intent intent = new Intent(ToysLeaseHtmlMainOrderActivity.this, ToysLeaseHtmlOrderCardDetailActivity.class);
			intent.putExtra(ToysLeaseHtmlOrderCardDetailActivity.ORDER_ID_KEY, order_id);
			ToysLeaseHtmlMainOrderActivity.this.startActivity(intent);
		}

		/**
		 * 进入话题详情
		 * */
		@JavascriptInterface
		public void callMethodToPostDetail(String img_id){
			if(null == img_id || img_id.equals("") || img_id.equals("undefined")){
				Toast.makeText(ToysLeaseHtmlMainOrderActivity.this, "找不到这个话题哦~", 3).show();
				return;
			}
			Intent intent = new Intent(ToysLeaseHtmlMainOrderActivity.this, MainItemDetialActivity.class);
			intent.putExtra("img_id", img_id);
			startActivity(intent);
		}
		
		
		/**
		 * 显示头部栏
		 * */
		@JavascriptInterface
		public void callMethodShowTitle(boolean showLeft, String centerText){
			web_title.setVisibility(View.VISIBLE);
			web_title_center.setText(centerText);
			if(showLeft){
				web_title_left.setVisibility(View.VISIBLE);
			}else{
				web_title_left.setVisibility(View.INVISIBLE);
			}
		}
		
		/**
		 * 隐藏头部栏
		 * */
		@JavascriptInterface
		public void callMethodHintTitle(boolean showLeft, String centerText){
			web_title.setVisibility(View.GONE);
		}
		
		/**
		 * JS调用的提示Toast方法
		 * */
		@JavascriptInterface
		public void showToast(String toastStr) {
			Toast.makeText(ToysLeaseHtmlMainOrderActivity.this, toastStr, Toast.LENGTH_LONG).show();
		}
		
		/**
		 * JS调用的跳转邀请页
		 * */
		@JavascriptInterface
		public void callMethodToInvite() {
			Intent intent = new Intent(ToysLeaseHtmlMainOrderActivity.this, InviteFriendActivity.class);
			startActivity(intent);
		}
		
		/**
		 * 退出浏览器
		 * */
		@JavascriptInterface
		public void callMethodFinish(){
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					ToysLeaseMainTabActivity.tabHost.setCurrentTab(0);
				}
			});
		}
		
		/**
		 * 跳转押金详情
		 * */
		@JavascriptInterface
		public void callMethodToToysDdpostit(){
			Intent intent = new Intent(ToysLeaseHtmlMainOrderActivity.this, ToysLeaseDepositActivity.class);
			startActivity(intent);
		}
		
		/**
		 * 跳转支付/确认订单
		 * */
		@JavascriptInterface
		public void callMethodToPayMoney(String source, String combined_order_id){
			if(source.equals("2")){
				ToysLeaseOrderConfirmActivity.createNewOrder(ToysLeaseHtmlMainOrderActivity.this, ToysLeaseOrderConfirmActivity.CONFIRM_COMBINED_ID, combined_order_id);
			}else{
				Intent intent = new Intent(ToysLeaseHtmlMainOrderActivity.this, ToysLeaseOrderConfirmActivity.class);
				intent.putExtra(ToysLeaseOrderConfirmActivity.CONFIRM_COMBINED_ID, combined_order_id);
				ToysLeaseHtmlMainOrderActivity.this.startActivity(intent);
			}
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			// 返回到上一个网页，没有可返回就退出此Activity
			if(urlList.size() > 1 ){
				if(urlList.get(urlList.size()-1).contains("http://www.meimei.yihaoss.top/H5/toy_count/orderAll.html")){
					 finishThisPage();
					 return super.onKeyDown(keyCode, event);
				}else{
					urlList.remove(urlList.size()-1);
					webView.loadUrl(urlList.get(urlList.size()-1));
					return true;
				}
			 }else{
				 finishThisPage();
				 return super.onKeyDown(keyCode, event);
			 }
		 }else{
			 finishThisPage();
			 return super.onKeyDown(keyCode, event);
		 }       
	}
	
	private void finishThisPage(){
		 ToysLeaseHtmlMainOrderActivity.this.finish();
	}
	
	@Override
	public void startActivity(Intent intent) {
	    super.startActivity(intent);
	    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
	    super.startActivityForResult(intent, requestCode);
	    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
	}

	@Override
	public void finish() {
		super.finish();
	    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		// 修改底部栏文字颜色
		ToysLeaseMainTabActivity.iconText.get(0).setTextColor(Color.parseColor("#000000"));
		ToysLeaseMainTabActivity.iconText.get(1).setTextColor(Color.parseColor("#000000"));
		ToysLeaseMainTabActivity.iconText.get(2).setTextColor(Color.parseColor("#000000"));
		ToysLeaseMainTabActivity.iconText.get(3).setTextColor(Color.parseColor("#fe6363"));
		
		if(isIntent && !MyApplication.getVisitor().equals("0")  || Config.getUser(ToysLeaseHtmlMainOrderActivity.this).uid.equals("")){
			if(null != ToysLeaseMainTabActivity.toysLeaseMainTabActivity){
				ToysLeaseMainTabActivity.toysLeaseMainTabActivity.finish();
			}
			Intent in = new Intent(ToysLeaseHtmlMainOrderActivity.this, ToysLeaseMainTabActivity.class);
			startActivity(in);
			this.finish();
		}else if(!isIntent && !MyApplication.getVisitor().equals("0")  || Config.getUser(ToysLeaseHtmlMainOrderActivity.this).uid.equals("")){
			isIntent = true;
			Intent in = new Intent(ToysLeaseHtmlMainOrderActivity.this, WebLoginActivity.class);
			startActivity(in);
		}else{
			if(isLoadUrl){
				String updateUrl = "javascript:getOrderequest("+Config.getUser(ToysLeaseHtmlMainOrderActivity.this).uid+")";
				webView.loadUrl(updateUrl);
			}else{
				webView.loadUrl("http://www.meimei.yihaoss.top/H5/toy_count/orderAll.html?login_user_id="+Config.getUser(ToysLeaseHtmlMainOrderActivity.this).uid);
				urlList.add("http://www.meimei.yihaoss.top/H5/toy_count/orderAll.html?login_user_id="+Config.getUser(ToysLeaseHtmlMainOrderActivity.this).uid);
				isLoadUrl = true;
			}
		}

	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	/**
	 * 展示重新加载页面
	 * */
	private void showUpdatePage(){
		webView.setVisibility(View.GONE);
		findViewById(R.id.toys_to_settings).setVisibility(View.VISIBLE);
		findViewById(R.id.toys_to_settings).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				webView.loadUrl("http://www.meimei.yihaoss.top/H5/toy_count/orderAll.html?login_user_id="+Config.getUser(ToysLeaseHtmlMainOrderActivity.this).uid);
				urlList.add("http://www.meimei.yihaoss.top/H5/toy_count/orderAll.html?login_user_id="+Config.getUser(ToysLeaseHtmlMainOrderActivity.this).uid);
			}
		});
		
	}
}

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
import com.yyqq.code.main.MainItemDetialWebActivity;
import com.yyqq.code.toyslease.InviteFriendActivity;
import com.yyqq.code.toyslease.ToysLeaseMainTabActivity;
import com.yyqq.code.toyslease.version_92.ToysLeaseMainShoppingCartActivity;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.MyWebviewUtils;
import com.yyqq.commen.utils.ShoppingCartUtils;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.MyApplication;

public class ToysLeaseHtmlCategoryActivity extends BaseActivity {

	private WebView webView = null;
	private ArrayList<String> urlList; // 加载过的url记录
	private RelativeLayout web_title;
	private TextView web_title_center;
	private ImageView web_title_left;
	public static ImageView search_to_cart;
	
	public static ToysLeaseHtmlCategoryActivity toysLeaseHtmlCategoryActivity = null;
	public static String CATEGORY_ID_KEY = "key";
	
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
		search_to_cart = (ImageView) findViewById(R.id.search_to_cart);
		
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
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				webView.setVisibility(View.VISIBLE);
				findViewById(R.id.toys_to_settings).setVisibility(View.GONE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
			
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				showUpdatePage();
			}
			
		});
		
		webView.loadUrl("http://www.meimei.yihaoss.top/H5/toy_count/all.html?login_user_id="+Config.getUser(ToysLeaseHtmlCategoryActivity.this).uid+"&category_id="+getIntent().getStringExtra(CATEGORY_ID_KEY));
		urlList.add("http://www.meimei.yihaoss.top/H5/toy_count/all.html?login_user_id="+Config.getUser(ToysLeaseHtmlCategoryActivity.this).uid+"&category_id="+getIntent().getStringExtra(CATEGORY_ID_KEY));		
	}

	@Override
	protected void initData() {
		toysLeaseHtmlCategoryActivity = this;
	}

	@Override
	protected void setListener() {
		
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
		 * 添加到购物车
		 * */
		@JavascriptInterface
		public void callMethodChangeNumber(String number){
			
			Toast.makeText(ToysLeaseHtmlCategoryActivity.this, "添加成功", 3).show();
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// 保存购物车数量
					ShoppingCartUtils.updateShoppingCartNumber(ToysLeaseHtmlCategoryActivity.this, true);
				}
			});
		}
		
		/**
		 * 进入登录页面
		 * */
		@JavascriptInterface
		public void callMethodToLoginPage(){
			Intent intent = new Intent(ToysLeaseHtmlCategoryActivity.this, WebLoginActivity.class);
			startActivity(intent);
		}
		
		/**
		 * 获取用户ID
		 * */
		@JavascriptInterface
		public String callMethodGetLoginUserId(){
			return Config.getUser(ToysLeaseHtmlCategoryActivity.this).uid;
		}
		
		/**
		 * 判断用户是否已经登录
		 * */
		@JavascriptInterface
		public String callMethodIsLogin(){
			if(!MyApplication.getVisitor().equals("0")  || Config.getUser(ToysLeaseHtmlCategoryActivity.this).uid.equals("")){
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
				Toast.makeText(ToysLeaseHtmlCategoryActivity.this, "找不到这组玩具哦~", 3).show();
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
				Toast.makeText(ToysLeaseHtmlCategoryActivity.this, "找不到这个玩具哦~", 3).show();
				return;
			}
			Intent intent = new Intent(ToysLeaseHtmlCategoryActivity.this, ToysLeaseHtmlDetailActivity.class);
			intent.putExtra(ToysLeaseHtmlDetailActivity.TOYS_DETAIL_KEY, toys_id);
			startActivity(intent);
		}
		
		/**
		 * 进入订单详情
		 * */
		@JavascriptInterface
		public void callMethodToToysOrderDetail(String order_id){
			if(null == order_id || order_id.equals("") || order_id.equals("undefined")){
				Toast.makeText(ToysLeaseHtmlCategoryActivity.this, "找不到这个订单哦~", 3).show();
				return;
			}
			Intent intent = new Intent(ToysLeaseHtmlCategoryActivity.this, ToysLeaseHtmlOrderToysDetailActivity.class);
			intent.putExtra(ToysLeaseHtmlOrderToysDetailActivity.ORDER_ID_KEY, order_id);
			startActivity(intent);
		}
		
		/**
		 * 进入话题详情
		 * */
		@JavascriptInterface
		public void callMethodToPostDetail(String img_id){
			if(null == img_id || img_id.equals("") || img_id.equals("undefined")){
				Toast.makeText(ToysLeaseHtmlCategoryActivity.this, "找不到这个话题哦~", 3).show();
				return;
			}
			Intent intent = new Intent(ToysLeaseHtmlCategoryActivity.this, MainItemDetialActivity.class);
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
			Toast.makeText(ToysLeaseHtmlCategoryActivity.this, toastStr, Toast.LENGTH_LONG).show();
		}
		
		/**
		 * JS调用的跳转搜索页
		 * */
		@JavascriptInterface
		public void callMethodToSearch() {
			Intent intent = new Intent(ToysLeaseHtmlCategoryActivity.this, ToysLeaseHtmlSearchActivity.class);
			startActivity(intent);
		}
		
		/**
		 * JS调用的跳转邀请页
		 * */
		@JavascriptInterface
		public void callMethodToInvite() {
			Intent intent = new Intent(ToysLeaseHtmlCategoryActivity.this, InviteFriendActivity.class);
			startActivity(intent);
		}
		
		/**
		 * 退出浏览器
		 * */
		@JavascriptInterface
		public void callMethodFinish(){
			ToysLeaseHtmlCategoryActivity.this.finish();
		}
		
		/**
		 * 跳转外链
		 * */
		@JavascriptInterface
		public void callMethodToWeb(String url){
			Intent intent = new Intent(ToysLeaseHtmlCategoryActivity.this, MainItemDetialWebActivity.class);
			intent.putExtra(MainItemDetialWebActivity.LINK_URL, url);
			startActivity(intent);
		}
		
		/**
		 * 跳转玩具分类列表
		 * */
		@JavascriptInterface
		public void callMethodToCategory(String category_id){
			Intent intent = new Intent(ToysLeaseHtmlCategoryActivity.this, ToysLeaseHtmlCategoryActivity.class);
			intent.putExtra(ToysLeaseHtmlCategoryActivity.CATEGORY_ID_KEY, category_id);
			startActivity(intent);
		}
		
		/**
		 * 获取分类id
		 * */
		@JavascriptInterface
		public String callMethodGetCategoryId(){
			return getIntent().getStringExtra(CATEGORY_ID_KEY);
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			// 返回到上一个网页，没有可返回就退出此Activity
			if(urlList.size() > 1 ){
				if(urlList.get(urlList.size()-1).contains("http://www.meimei.yihaoss.top/H5/toy_count/all.html")){
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
		 ToysLeaseHtmlCategoryActivity.this.finish();
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
		ToysLeaseMainTabActivity.iconText.get(0).setTextColor(Color.parseColor("#fe6363"));
		ToysLeaseMainTabActivity.iconText.get(1).setTextColor(Color.parseColor("#000000"));
		ToysLeaseMainTabActivity.iconText.get(2).setTextColor(Color.parseColor("#000000"));
		ToysLeaseMainTabActivity.iconText.get(3).setTextColor(Color.parseColor("#000000"));
		findViewById(R.id.search_to_cart).setVisibility(View.VISIBLE);
		ShoppingCartUtils.badge = null;
		ShoppingCartUtils.DrewNumberBitmap(this, findViewById(R.id.search_to_cart));
		findViewById(R.id.search_to_cart).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!MyApplication.getVisitor().equals("0")  || Config.getUser(v.getContext()).uid.equals("")){
					Intent in = new Intent(v.getContext(), WebLoginActivity.class);
					startActivity(in);
				}else{
					ToysLeaseMainShoppingCartActivity.isShowBack = true;
					Intent intent = new Intent(v.getContext(), ToysLeaseMainShoppingCartActivity.class);
					startActivity(intent);
				}
			}
		});

	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		findViewById(R.id.search_to_cart).setVisibility(View.GONE);
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
				webView.loadUrl("http://www.meimei.yihaoss.top/H5/toy_count/all.html?login_user_id="+Config.getUser(ToysLeaseHtmlCategoryActivity.this).uid+"&category_id="+getIntent().getStringExtra(CATEGORY_ID_KEY));
				urlList.add("http://www.meimei.yihaoss.top/H5/toy_count/all.html?login_user_id="+Config.getUser(ToysLeaseHtmlCategoryActivity.this).uid+"&category_id="+getIntent().getStringExtra(CATEGORY_ID_KEY));		
			}
		});
	}
}

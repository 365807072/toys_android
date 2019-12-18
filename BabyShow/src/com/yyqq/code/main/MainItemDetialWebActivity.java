package com.yyqq.code.main;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.yyqq.babyshow.R;
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.code.toyslease.ToysLeaseDetailActivity;
import com.yyqq.commen.share.GroupSharedUtils;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.MyWebviewUtils;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.MyApplication;

public class MainItemDetialWebActivity extends BaseActivity {
	
	private WebView main_item_web;
	private RelativeLayout title;
	private ImageView main_item_back;
	
	public static String LINK_URL = "link_url";
	private ArrayList<String> urlList; // 加载过的url记录
	private String to_url;
	private Context context;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_item_detial_web);
	}

	@Override
	protected void initView() {
		
		context = this;
		title = (RelativeLayout) findViewById(R.id.title);
		main_item_back = (ImageView) findViewById(R.id.main_item_back);
		
		to_url = getIntent().getStringExtra(LINK_URL);
		urlList = new ArrayList<String>();
		main_item_web = (WebView) findViewById(R.id.main_item_web);
		MyWebviewUtils.initWebView(this, main_item_web, new JSCallback(), true);		
		// html加载结束再加载js方法，否则js可能会不起作用
		main_item_web.setWebViewClient(new WebViewClient() { 
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
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				if(urlList.size() > 1){
					urlList.remove(urlList.size()-1);
					view.loadUrl(urlList.get(urlList.size()-1));
				}else{
					Toast.makeText(MainItemDetialWebActivity.this, "抱歉！找不到这个网页", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
		
		main_item_web.loadUrl(to_url);
//		main_item_web.loadUrl("https://api.meimei.yihaoss.top/H5/invite/ceshi.html");
		urlList.add(to_url);
				
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

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setListener() {
		main_item_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainItemDetialWebActivity.this.finish();
			}
		});
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if ((keyCode == KeyEvent.KEYCODE_BACK) && main_item_web.canGoBack()) {
			 
			// 返回到上一个网页，没有可返回就退出此Activity
			if(urlList.size() > 1 ){
				if(urlList.get(urlList.size()-1).equals(to_url)){
					 MainItemDetialWebActivity.this.finish();
					 return super.onKeyDown(keyCode, event);
				}else{
					urlList.remove(urlList.size()-1);
					main_item_web.loadUrl(urlList.get(urlList.size()-1));
					return true;
				}
			 }else{
				 MainItemDetialWebActivity.this.finish();
				 return super.onKeyDown(keyCode, event);
			 }
		 }else{
			 MainItemDetialWebActivity.this.finish();
			 return super.onKeyDown(keyCode, event);
		 }       
	}
	
	/**
	 * JS回调的原生方法
	 * */
	public class JSCallback{
		
		private String shared_img_app = "";
		private String shared_des_app = "";
		private String shared_title_app = "";
		private String business_wxPath_app = "";
		private String sharedUrl_app = "";
		
		/**
		 * 进入登录页面
		 * */
		@JavascriptInterface
		public void callMethodToLoginPage(){
			Intent intent = new Intent(MainItemDetialWebActivity.this, WebLoginActivity.class);
			intent.putExtra(WebLoginActivity.SEND_MASSAGE_KEY, true);
			startActivity(intent);
		}
		
		/**
		 * 获取用户ID
		 * */
		@JavascriptInterface
		public String callMethodGetLoginUserId(){
			return Config.getUser(MainItemDetialWebActivity.this).uid;
		}
		
		/**
		 * 判断用户是否已经登录
		 * */
		@JavascriptInterface
		public String callMethodIsLogin(){
			if(!MyApplication.getVisitor().equals("0")  || Config.getUser(MainItemDetialWebActivity.this).uid.equals("")){
				return "0"; // 未登录
			}else{
				return "1"; // 已登陆
			}
		}
		
		/**
		 * 进入玩具详情
		 * */
		@JavascriptInterface
		public void callMethodToToysDetail(String toys_id){
			if(null == toys_id || toys_id.equals("") || toys_id.equals("undefined")){
				Toast.makeText(MainItemDetialWebActivity.this, "找不到这个玩具哦~", 3).show();
				return;
			}
			Intent intent = new Intent(MainItemDetialWebActivity.this, ToysLeaseDetailActivity.class);
			intent.putExtra(ToysLeaseDetailActivity.TOYS_DETAIL_KEY, toys_id);
			startActivity(intent);
		}
		
		/**
		 * 进入话题详情
		 * */
		@JavascriptInterface
		public void callMethodToPostDetail(String img_id){
			if(null == img_id || img_id.equals("") || img_id.equals("undefined")){
				Toast.makeText(MainItemDetialWebActivity.this, "找不到这个话题哦~", 3).show();
				return;
			}
			Intent intent = new Intent(MainItemDetialWebActivity.this, MainItemDetialActivity.class);
			intent.putExtra("img_id", img_id);
			startActivity(intent);
		}
		
		/**
		 * 退出浏览器
		 * */
		@JavascriptInterface
		public void callMethodFinish(){
			MainItemDetialWebActivity.this.finish();
		}
		
		/**
		 * 隐藏导航栏
		 * */
		@JavascriptInterface
		public void callMethodHindTitle(String showType, String titleColor){
			
			if(showType.equals("0")){
				title.setVisibility(View.GONE);
				main_item_back.setVisibility(View.GONE);
			}else{
				title.setVisibility(View.VISIBLE);
				main_item_back.setVisibility(View.VISIBLE);
				
				if(!titleColor.equals("")){
					String colorStr = "#" + titleColor;
					title.setBackgroundColor(Color.parseColor(colorStr));
				}
			}
		}
		
		/**
		 * JS调用的提示Toast方法
		 * */
		@JavascriptInterface
		public void showToast(String toastStr) {
			Toast.makeText(MainItemDetialWebActivity.this, toastStr, Toast.LENGTH_LONG).show();
		}
		
		/**
		 * JS调用的展示分享按钮
		 * */
		@JavascriptInterface
		public void showShered(String showType) {
//			if(showType.equals("1")){
//				main_item_more.setVisibility(View.VISIBLE);
//				main_item_more.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						GroupSharedUtils.groupShareBean bean = new GroupSharedUtils.groupShareBean();
//						bean.setMore(true);
//						bean.setContext(MainItemDetialWebActivity.this);
//						bean.setShare_img(shared_img_app);
//						bean.setShare_text(shared_des_app);
//						bean.setShare_title(shared_title_app);
//						bean.setWxPath(business_wxPath_app);
//						bean.setShare_url(sharedUrl_app);
//						GroupSharedUtils.SharedGroup(bean);
//					}
//				});
//			}
		}
		
		/**
		 * JS调用的分享内容
		 * */
		@JavascriptInterface
		public void showSheredInfo(String shared_img, String shared_des, String shared_title, String business_wxPath, String sharedUrl ) {
			shared_img_app = shared_img;
			shared_des_app = shared_des;
			shared_title_app = shared_title;
			shared_img_app = shared_img;
			sharedUrl_app = sharedUrl;
			business_wxPath_app = business_wxPath;
			
			GroupSharedUtils.groupShareBean bean = new GroupSharedUtils.groupShareBean();
			bean.setMore(true);
			bean.setContext(MainItemDetialWebActivity.this);
			bean.setShare_img(shared_img_app);
			bean.setShare_text(shared_des_app);
			bean.setShare_title(shared_title_app);
			bean.setWxPath(business_wxPath_app);
			bean.setShare_url(sharedUrl_app);
			GroupSharedUtils.SharedGroup(bean);
		}
		
	}
	
}

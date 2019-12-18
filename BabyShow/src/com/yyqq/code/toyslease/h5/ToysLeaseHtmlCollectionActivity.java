package com.yyqq.code.toyslease.h5;

import java.util.ArrayList;

import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.code.main.MainItemDetialActivity;
import com.yyqq.code.main.MainItemDetialWebActivity;
import com.yyqq.code.toyslease.InviteFriendActivity;
import com.yyqq.code.toyslease.ToysLeaseDetailActivity;
import com.yyqq.code.toyslease.ToysLeaseMainTabActivity;
import com.yyqq.code.toyslease.version_92.ToysLeaseSearchActivity;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.MyWebviewUtils;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.MyApplication;

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

/**
 * 我的收藏
 * 
 * */
public class ToysLeaseHtmlCollectionActivity extends BaseActivity {

	private WebView webView = null;
	private ArrayList<String> urlList; // 加载过的url记录
	private RelativeLayout web_title;
	private TextView web_title_center;
	private ImageView web_title_left;

	public static ToysLeaseHtmlCollectionActivity toysLeaseHtmlInterstActivity = null;
	public static String CATEGORY_ID_KEY = "key";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		setContentView(R.layout.activity_toys_lease_main_text);

	}

	@Override
	protected void initView() {
		Config.showProgressDialog(ToysLeaseHtmlCollectionActivity.this, false, null);
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
				Config.dismissProgress();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				webView.setVisibility(View.VISIBLE);
				findViewById(R.id.toys_to_settings).setVisibility(View.GONE);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				showUpdatePage();
				Config.dismissProgress();
			}

		});
		
		
		webView.loadUrl("https://api.meimei.yihaoss.top/H5/toy_count/collection.html?login_user_id="
				+ Config.getUser(ToysLeaseHtmlCollectionActivity.this).uid);
		urlList.add("https://api.meimei.yihaoss.top/H5/toy_count/collection.html?login_user_id="
				+ Config.getUser(ToysLeaseHtmlCollectionActivity.this).uid);
	}

	@Override
	protected void initData() {
		
		if(null != toysLeaseHtmlInterstActivity){
			toysLeaseHtmlInterstActivity.finish();
		}
		toysLeaseHtmlInterstActivity = this;
		// if(getIntent().hasExtra("order_id")){
		// order_id = getIntent().getStringExtra("order_id");
		// }
		//
		// if(!order_id.equals("")){
		//
		// // 此处定位至订单按钮以及页面
		// Intent intent = new Intent(ToysLeaseMainHomeHtmlActivity.this,
		// ToysLeaseHtmlOrderDetailActivity.class);
		// intent.putExtra(ToysLeaseHtmlOrderDetailActivity.ORDER_ID_KEY,
		// getIntent().getStringExtra("order_id"));
		// ToysLeaseMainHomeHtmlActivity.this.startActivity(intent);
		// order_id = "";
		// }
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	/**
	 * url历史管理
	 */
	private void checkUrlHistory(String url) {
		if (!urlList.toString().contains(url)) {
			urlList.add(url);
		} else {
			for (int i = 0; i < urlList.size(); i++) {
				if (urlList.get(i).equals(url)) {
					urlList.remove(i);
				}
			}
			urlList.add(url);
		}
	}

	/**
	 * JS回调的原生方法
	 */
	public class JSCallback {

		/**
		 * 进入登录页面
		 */
		@JavascriptInterface
		public void callMethodToLoginPage() {
			Intent intent = new Intent(ToysLeaseHtmlCollectionActivity.this, WebLoginActivity.class);
			startActivity(intent);
		}

		/**
		 * 获取用户ID
		 */
		@JavascriptInterface
		public String callMethodGetLoginUserId() {
			return Config.getUser(ToysLeaseHtmlCollectionActivity.this).uid;
		}

		/**
		 * 判断用户是否已经登录
		 */
		@JavascriptInterface
		public String callMethodIsLogin() {
			if (!MyApplication.getVisitor().equals("0")
					|| Config.getUser(ToysLeaseHtmlCollectionActivity.this).uid.equals("")) {
				return "0"; // 未登录
			} else {
				return "1"; // 已登陆
			}
		}

		/**
		 * 进入玩具筛选列表
		 */
		@JavascriptInterface
		public void callMethodToToysList(String toys_type) {
			if (null == toys_type || toys_type.equals("") || toys_type.equals("undefined")) {
				Toast.makeText(ToysLeaseHtmlCollectionActivity.this, "找不到这组玩具哦~", 3).show();
				return;
			}
			// Intent intent = new Intent(ToysLeaseMainTextActivity.this,
			// ToysLeaseListActivity.class);
			// intent.putExtra(ToysLeaseListActivity.TOYS_TYPE, toys_type);
			// startActivity(intent);
		}
		
		/**
		 * 进入玩具详情
		 */
		@JavascriptInterface
		public void callMethodToToysDetail(String toysID) {
			if (null == toysID || toysID.equals("") || toysID.equals("undefined")) {
				Toast.makeText(ToysLeaseHtmlCollectionActivity.this, "找不到这件玩具哦~", 3).show();
				return;
			}
			 Intent intent = new Intent(ToysLeaseHtmlCollectionActivity.this, ToysLeaseDetailActivity.class);
			 intent.putExtra(ToysLeaseDetailActivity.TOYS_DETAIL_KEY, toysID);
			 startActivity(intent);
		}
		

		/**
		 * 进入话题详情
		 */
		@JavascriptInterface
		public void callMethodToPostDetail(String img_id) {
			if (null == img_id || img_id.equals("") || img_id.equals("undefined")) {
				Toast.makeText(ToysLeaseHtmlCollectionActivity.this, "找不到这个话题哦~", 3).show();
				return;
			}
			Intent intent = new Intent(ToysLeaseHtmlCollectionActivity.this, MainItemDetialActivity.class);
			intent.putExtra("img_id", img_id);
			startActivity(intent);
		}

		/**
		 * 显示头部栏
		 */
		@JavascriptInterface
		public void callMethodShowTitle(boolean showLeft, String centerText) {
			web_title.setVisibility(View.VISIBLE);
			web_title_center.setText(centerText);
			if (showLeft) {
				web_title_left.setVisibility(View.VISIBLE);
			} else {
				web_title_left.setVisibility(View.INVISIBLE);
			}
		}

		/**
		 * 隐藏头部栏
		 */
		@JavascriptInterface
		public void callMethodHintTitle(boolean showLeft, String centerText) {
			web_title.setVisibility(View.GONE);
		}

		/**
		 * JS调用的提示Toast方法
		 */
		@JavascriptInterface
		public void showToast(String toastStr) {
			Toast.makeText(ToysLeaseHtmlCollectionActivity.this, toastStr, Toast.LENGTH_LONG).show();
		}

		/**
		 * JS调用的跳转搜索页
		 */
		@JavascriptInterface
		public void callMethodToSearch() {
			Intent intent = new Intent(ToysLeaseHtmlCollectionActivity.this, ToysLeaseSearchActivity.class);
			startActivity(intent);
		}

		/**
		 * JS调用的跳转邀请页
		 */
		@JavascriptInterface
		public void callMethodToInvite() {
			Intent intent = new Intent(ToysLeaseHtmlCollectionActivity.this, InviteFriendActivity.class);
			startActivity(intent);
		}

		/**
		 * 退出浏览器
		 */
		@JavascriptInterface
		public void callMethodFinish() {
			ToysLeaseHtmlCollectionActivity.this.finish();
		}

		/**
		 * 跳转外链
		 */
		@JavascriptInterface
		public void callMethodToWeb(String url) {
			Intent intent = new Intent(ToysLeaseHtmlCollectionActivity.this, MainItemDetialWebActivity.class);
			intent.putExtra(MainItemDetialWebActivity.LINK_URL, url);
			startActivity(intent);
		}


	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			// 返回到上一个网页，没有可返回就退出此Activity
			if (urlList.size() > 1) {
				if (urlList.get(urlList.size() - 1)
						.contains("https://api.meimei.yihaoss.top/H5/toy_count/bespeak.html")) {
					finishThisPage();
					return super.onKeyDown(keyCode, event);
				} else {
					urlList.remove(urlList.size() - 1);
					webView.loadUrl(urlList.get(urlList.size() - 1));
					return true;
				}
			} else {
				finishThisPage();
				return super.onKeyDown(keyCode, event);
			}
		} else {
			finishThisPage();
			return super.onKeyDown(keyCode, event);
		}
	}

	private void finishThisPage() {
		ToysLeaseHtmlCollectionActivity.this.finish();
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

	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/**
	 * 展示重新加载页面
	 */
	private void showUpdatePage() {
		webView.setVisibility(View.GONE);
		findViewById(R.id.toys_to_settings).setVisibility(View.VISIBLE);
		findViewById(R.id.toys_to_settings).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				webView.loadUrl("https://api.meimei.yihaoss.top/H5/toy_count/bespeak.html?login_user_id="
						+ Config.getUser(ToysLeaseHtmlCollectionActivity.this).uid + "&category_id="
						+ getIntent().getStringExtra(CATEGORY_ID_KEY));
				urlList.add("https://api.meimei.yihaoss.top/H5/toy_count/bespeak.html?login_user_id="
						+ Config.getUser(ToysLeaseHtmlCollectionActivity.this).uid + "&category_id="
						+ getIntent().getStringExtra(CATEGORY_ID_KEY));
			}
		});

	}
}
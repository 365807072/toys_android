package com.yyqq.commen.utils;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import com.ab.http.AbHttpResponseListener;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.yyqq.framework.application.ServerMutualConfig;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressWarnings("deprecation")
@SuppressLint("SetJavaScriptEnabled")
public class MyWebviewUtils {
	
	// JS方法调用原生方法时使用的对象名称 ， 例如：window.call.method("param");
	public static String CALL_NAME = "call"; 
	
	// 是否不从缓存中获取html页面（每次都从服务器获取页面）
	private static boolean REFRESH = false;
	
	public static WebView initWebView(Context context, WebView webView, Object JsInteration, boolean isRefresh){
		REFRESH = isRefresh;
		return initWebView(context, webView, JsInteration);
	}
	
	// 初始化webview的配置项，参数相应为：上下文对象、需要被初始化的webview、实现JS回调方法的接口
	public static WebView initWebView(final Context context, WebView webView, Object JsInteration){
		if(null != JsInteration){
			// 添加JS方法的回调
			webView.addJavascriptInterface(JsInteration, MyWebviewUtils.CALL_NAME);
		}
		
		if(REFRESH){
			// 加载模式是：不使用缓存，只从网络获取数据
			webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		}
		
		// https加载容错处理
		webView.setWebViewClient(new WebViewClient(){
			
			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//				super.onReceivedSslError(view, handler, error);
				
		        AbRequestParams params = new AbRequestParams();
		        params.put("login_user_id", Config.getUser(context).uid);
		        params.put("app_model", getVersion(context));
		        params.put("title", error.toString());
		        AbHttpUtil.getInstance(context).post(ServerMutualConfig.CollectInfo , params, new AbHttpResponseListener(){});
				
			    if(error.getPrimaryError() == android.net.http.SslError.SSL_INVALID ){// 校验过程遇到了bug  
			        handler.proceed();  
			     }else{  
			        handler.cancel();  
			    }  
			}
		});
		
		// 防止不可以alert
		webView.setWebChromeClient(new WebChromeClient() {});
		webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		webView.setScrollBarStyle(0);
		webView.getSettings().setDefaultTextEncodingName("utf-8");  
		webView.getSettings().setJavaScriptEnabled(true);  
		webView.getSettings().setLoadsImagesAutomatically(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setSavePassword(true);
		webView.getSettings().setSaveFormData(true);
		webView.setHorizontalScrollBarEnabled(false);//设置水平滚动条  
		webView.setVerticalScrollBarEnabled(false);//设置竖直滚动条  
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setLoadWithOverviewMode(true);
		
		// 加载网页的进度回调
		webView.setWebChromeClient(new WebChromeClient() {
			
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
//					Toast.makeText(MyWebActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onShowCustomView(View view, CustomViewCallback callback) {
			}
			
			@Override
			public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
				return super.onConsoleMessage(consoleMessage);
			}
			
		});
		return webView;
	}
	
	
	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context context) {
	    try {
	        PackageManager manager = context.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
	        String version = info.versionName;
	        return version;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "获取失败";
	    }
	}

}

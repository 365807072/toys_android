package com.yyqq.code.toyslease;

import com.yyqq.babyshow.R;
import com.yyqq.commen.share.GroupSharedUtils;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.MyWebviewUtils;
import com.yyqq.framework.activity.BaseActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;

/**
 * 玩具世界
 * */
@SuppressLint("NewApi")
public class ToysLeaseDelayActivity extends BaseActivity {
	
	private ImageView find;
	private WebView delay_main_webview;
	
	public static final String URL_KEY = "url_key"; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		setContentView(R.layout.activity_toys_lease_delay);
	}

	@Override
	protected void initView() {
		find = (ImageView) findViewById(R.id.find);
		delay_main_webview = (WebView) findViewById(R.id.delay_main_webview);
	}

	@Override
	protected void initData() {
		MyWebviewUtils.initWebView(this, delay_main_webview, null);
		if((getIntent().hasExtra(URL_KEY))){
			delay_main_webview.loadUrl(getIntent().getStringExtra(URL_KEY));
			GroupSharedUtils.intentUrl = "";
		}else{
			this.finish();
		}
	}
	
	@Override
	protected void setListener() {
		find.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToysLeaseDelayActivity.this.finish();
			}
		});
		
	}
	
}

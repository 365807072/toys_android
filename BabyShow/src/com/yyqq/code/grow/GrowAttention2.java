package com.yyqq.code.grow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.business.PayActivity;
import com.yyqq.commen.utils.Config;

public class GrowAttention2 extends Activity implements OnClickListener {

	private Activity context;
	private RelativeLayout general_ly;
	private Button bt_sure;
	
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
		setContentView(R.layout.grow_attention2);

		context = this;
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
		bt_sure = (Button) findViewById(R.id.bt_sure);
		bt_sure.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {

		if (arg0.getId() == R.id.bt_sure) {
			Intent intent = new Intent();
			intent.setClass(context, PayActivity.class);
			intent.putExtra("business_id",
					getIntent().getStringExtra("babys_idol_id"));
			intent.putExtra("order_role", "1");
			startActivity(intent);

		}
	}
}

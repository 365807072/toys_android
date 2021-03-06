package com.yyqq.code.grow;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainTab;
import com.yyqq.commen.model.SearchItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class GrowAttention extends Activity implements OnClickListener {

	private Activity context;
	private AbHttpUtil ab;
	private MyApplication app;
	private RelativeLayout general_ly;
	private RelativeLayout ly1, ly2;
	private ImageView bt_attention1, bt_attention2;
	private EditText tv_msg;
	private Button bt_sure;
	private String type = "0";
	
	
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
		setContentView(R.layout.grow_attention);

		context = this;
		ab = AbHttpUtil.getInstance(context);
		app = (MyApplication) context.getApplication();
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
		tv_msg = (EditText) findViewById(R.id.tv_msg);
		bt_attention1 = (ImageView) findViewById(R.id.bt_attention);
		bt_attention2 = (ImageView) findViewById(R.id.bt_attention2);
		ly1 = (RelativeLayout) findViewById(R.id.ly1);
		ly1.setOnClickListener(this);
		ly2 = (RelativeLayout) findViewById(R.id.ly2);
		ly2.setOnClickListener(this);
		bt_sure = (Button) findViewById(R.id.bt_sure);
		bt_sure.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.ly1) {
			type = "0";
			bt_attention1.setBackgroundResource(R.drawable.sel_bt);
			bt_attention2.setBackgroundResource(R.drawable.sel_bt_no);
		}
		if (arg0.getId() == R.id.ly2) {
			type = "1";
			bt_attention1.setBackgroundResource(R.drawable.sel_bt_no);
			bt_attention2.setBackgroundResource(R.drawable.sel_bt);
		}

		if (arg0.getId() == R.id.bt_sure) {
			AbRequestParams params = new AbRequestParams();
			params.put("login_user_id", Config.getUser(context).uid);
			params.put("idol_user_id",
					getIntent().getStringExtra("idol_user_id"));
			// params.put("idol_type", type);
			params.put("baby_id", getIntent().getStringExtra("baby_id"));
			params.put("description", tv_msg.getText().toString().trim());
			// android.util.Log.e("fanfan", params.toString());
			// android.util.Log.e("fanfan", ServerMutualConfig.PublicBabysIdol +
			// "&" + params.toString());
			ab.post(ServerMutualConfig.PublicBabysIdol, params,
					new AbStringHttpResponseListener() {
						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							try {
								JSONObject json = new JSONObject(content);
								if (json.getBoolean("success")) {
									SearchItem item = new SearchItem();
									item.fromJson(json.getJSONObject("data"));
									if ("1".equals(type)) {
										Intent intent = new Intent();
										intent.setClass(context,
												GrowAttention2.class);
										intent.putExtra("babys_idol_id",
												item.babys_idol_id);
										startActivity(intent);
									} else {
										Intent intent = new Intent();
										intent.setClass(context, MainTab.class);
										intent.putExtra("tabid", 2);
										startActivity(intent);
									}
									Toast.makeText(context,
											json.getString("reMsg"),
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(context,
											json.getString("reMsg"),
											Toast.LENGTH_SHORT).show();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
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
							Config.showFiledToast(context);
						}
					});
		}
	}
}

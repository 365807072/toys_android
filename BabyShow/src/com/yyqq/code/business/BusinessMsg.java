package com.yyqq.code.business;

import org.json.JSONException;
import org.json.JSONObject;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.framework.application.ServerMutualConfig;

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

public class BusinessMsg extends Activity {

	private Activity context;
	private String TAG = "fanfan_BusinessMsg";
	private AbHttpUtil ab;
	private RelativeLayout back;
	private String businessId = "";
	private String orderId = "";
	private int grade = 0;

	private ImageView star1, star2, star3, star4, star5;
	private EditText user_msg; // 评价内容
	private Button commit; // 提交
	
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
		setContentView(R.layout.business_msg);

		context = this;
		ab = AbHttpUtil.getInstance(context);
		businessId = getIntent().getStringExtra("business_id");
		orderId = getIntent().getStringExtra("order_id");
		init();
	}

	private void init() {

		// 返回
		back = (RelativeLayout) findViewById(R.id.ly);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				context.finish();
			}
		});

		star1 = (ImageView) findViewById(R.id.star1);
		star2 = (ImageView) findViewById(R.id.star2);
		star3 = (ImageView) findViewById(R.id.star3);
		star4 = (ImageView) findViewById(R.id.star4);
		star5 = (ImageView) findViewById(R.id.star5);

		user_msg = (EditText) findViewById(R.id.user_msg);
		commit = (Button) findViewById(R.id.commit);

		commit.setOnClickListener(commitClick);
		star1.setOnClickListener(starClick1);
		star2.setOnClickListener(starClick2);
		star3.setOnClickListener(starClick3);
		star4.setOnClickListener(starClick4);
		star5.setOnClickListener(starClick5);

	}

	public View.OnClickListener commitClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			AbRequestParams params = new AbRequestParams();
			params.put("login_user_id", Config.getUser(context).uid); // 用户id
			params.put("business_id", businessId);
			params.put("like_level", grade + "");
			params.put("order_id", orderId);
			params.put("comment_description", user_msg.getText().toString()
					.trim());
			if (user_msg.getText().toString().trim().length() < 8) {
				Toast.makeText(context, "多写点吧，不能少于8个字哦", 0).show();
			} else if (grade == 0) {
				Toast.makeText(context, "您忘了给商家打分了哦", 0).show();
			} else {
				// Log.e("fanfan", ServerMutualConfig.PublicOrderComment + "&"
				// + params);
				ab.post(ServerMutualConfig.PublicOrderComment, params,
						new AbStringHttpResponseListener() {
							@Override
							public void onSuccess(int statusCode, String content) {
								super.onSuccess(statusCode, content);
								try {
									JSONObject json = new JSONObject(content);
									if (json.getBoolean("success")) {
										Toast.makeText(context,
												json.getString("reMsg"),
												Toast.LENGTH_SHORT).show();
										Intent intent = new Intent();
										intent.setClass(context,
												BusinessReviewList.class);
										intent.putExtra("business_id",
												businessId);
										startActivity(intent);
										context.finish();
									} else {
										Toast.makeText(context,
												json.getString("reMsg"),
												Toast.LENGTH_SHORT).show();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}

							@Override
							public void onFinish() {
								super.onFinish();
								Config.dismissProgress();
							}

							@Override
							public void onFailure(int statusCode,
									String content, Throwable error) {
								super.onFailure(statusCode, content, error);
								Config.showFiledToast(context);
							}
						});
			}
		}
	};

	public View.OnClickListener starClick1 = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			grade = 1;
			star1.setBackgroundResource(R.drawable.star);
			star2.setBackgroundResource(R.drawable.star_gray);
			star3.setBackgroundResource(R.drawable.star_gray);
			star4.setBackgroundResource(R.drawable.star_gray);
			star5.setBackgroundResource(R.drawable.star_gray);
		}
	};
	public View.OnClickListener starClick2 = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			grade = 2;
			star1.setBackgroundResource(R.drawable.star);
			star2.setBackgroundResource(R.drawable.star);
			star3.setBackgroundResource(R.drawable.star_gray);
			star4.setBackgroundResource(R.drawable.star_gray);
			star5.setBackgroundResource(R.drawable.star_gray);
		}
	};
	public View.OnClickListener starClick3 = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			grade = 3;
			star1.setBackgroundResource(R.drawable.star);
			star2.setBackgroundResource(R.drawable.star);
			star3.setBackgroundResource(R.drawable.star);
			star4.setBackgroundResource(R.drawable.star_gray);
			star5.setBackgroundResource(R.drawable.star_gray);
		}
	};
	public View.OnClickListener starClick4 = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			grade = 4;
			star1.setBackgroundResource(R.drawable.star);
			star2.setBackgroundResource(R.drawable.star);
			star3.setBackgroundResource(R.drawable.star);
			star4.setBackgroundResource(R.drawable.star);
			star5.setBackgroundResource(R.drawable.star_gray);
		}
	};
	public View.OnClickListener starClick5 = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			grade = 5;
			star1.setBackgroundResource(R.drawable.star);
			star2.setBackgroundResource(R.drawable.star);
			star3.setBackgroundResource(R.drawable.star);
			star4.setBackgroundResource(R.drawable.star);
			star5.setBackgroundResource(R.drawable.star);
		}
	};

}

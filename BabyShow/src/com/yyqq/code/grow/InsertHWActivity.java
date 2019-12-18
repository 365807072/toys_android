package com.yyqq.code.grow;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainTab;
import com.yyqq.commen.utils.Config;
import com.yyqq.framework.application.ServerMutualConfig;

import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InsertHWActivity extends Activity {
	private String TAG = "fanfan_InsertHWActivity";
	private Activity context;
	private AbHttpUtil ab;
	private EditText myHeight, myWeight;
	private String babyHeight, babyWeight;
	private Button fin;
	private TextView day;

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		setContentView(R.layout.insert_h_w);
		context = this;
		init();
	}

	private void init() {
		ab = AbHttpUtil.getInstance(context);
		fin = (Button) findViewById(R.id.fin);
		fin.setOnClickListener(finClick);
		babyHeight = getIntent().getStringExtra("baby_height");
		babyWeight = getIntent().getStringExtra("baby_weight");
		myHeight = (EditText) findViewById(R.id.my_height);
		myHeight.setOnFocusChangeListener(inputFocus);
		if (!TextUtils.isEmpty(babyHeight)) {
			myHeight.setCursorVisible(false);
			myHeight.setHint(babyHeight);
		}
		myWeight = (EditText) findViewById(R.id.my_weight);
		myWeight.setOnFocusChangeListener(inputFocus);
		if (!TextUtils.isEmpty(babyWeight)) {
			myWeight.setCursorVisible(false);
			myWeight.setHint(babyWeight);
		}
		day = (TextView) findViewById(R.id.day);
		// 24小时制
		SimpleDateFormat dateFormat24 = new SimpleDateFormat("yyyy年MM月dd日");
		day.setText(dateFormat24.format(Calendar.getInstance().getTime()));
	}

	public OnFocusChangeListener inputFocus = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				myHeight.setCursorVisible(true);
				myWeight.setCursorVisible(true);
			} else {
				myHeight.setCursorVisible(false);
				myWeight.setCursorVisible(false);
			}
		}
	};

	public OnClickListener finClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			AbRequestParams params = new AbRequestParams();
			params.put("login_user_id", Config.getUser(context).uid);
			params.put("baby_id", getIntent().getStringExtra("baby_id"));
			params.put("height", myHeight.getText().toString().trim());
			params.put("weight", myWeight.getText().toString().trim());
			// android.util.Log.e(TAG, ServerMutualConfig.PublicGrowth + "&"
			// + params.toString());
			ab.get(ServerMutualConfig.PublicGrowth + "&" + params.toString(),
					new AbStringHttpResponseListener() {
						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							try {
								JSONObject json = new JSONObject(content);
								if (json.getBoolean("success")) {
									Intent intent = new Intent();
									intent.setClass(context, MainTab.class);
									intent.putExtra("tabid", 2);
									startActivity(intent);
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
						public void onFailure(int statusCode, String content,
								Throwable error) {
							super.onFailure(statusCode, content, error);
							Config.showFiledToast(context);
						}
					});
		}

	};

}

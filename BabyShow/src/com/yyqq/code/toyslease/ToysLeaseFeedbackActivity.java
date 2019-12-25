package com.yyqq.code.toyslease;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.yyqq.babyshow.R;
import com.yyqq.commen.utils.Config;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * 订单详情-反馈建议
 * */
public class ToysLeaseFeedbackActivity extends BaseActivity {

	private ImageView lease_back;
	private Button confirm_adrs;
	private EditText feedback_user_input;
	
	private AbHttpUtil ab;
	public static final String ORDER_ID = "order_id";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_toys_lease_feedback);
	}

	@Override
	protected void initView() {
		lease_back = (ImageView) findViewById(R.id.lease_back);
		confirm_adrs = (Button) findViewById(R.id.confirm_adrs);
		feedback_user_input = (EditText) findViewById(R.id.feedback_user_input);
	}

	@Override
	protected void initData() {
		ab = AbHttpUtil.getInstance(ToysLeaseFeedbackActivity.this);
	}

	@Override
	protected void setListener() {
		lease_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToysLeaseFeedbackActivity.this.finish();
			}
		});
		
		confirm_adrs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				submitFeedback();
			}
		});
	}
	
	/**
	 * 提交反馈
	 * */
	private void submitFeedback(){
		
		if(feedback_user_input.getText().toString().trim().equals("")){
			Toast.makeText(ToysLeaseFeedbackActivity.this, "请填写反馈或者建议", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Config.showProgressDialog(ToysLeaseFeedbackActivity.this, false, null);
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(ToysLeaseFeedbackActivity.this).uid);
		params.put("suggest", feedback_user_input.getText().toString().trim());
		params.put("order_id", getIntent().getStringExtra(ORDER_ID));
		
		ab.post(ServerMutualConfig.USER_FEEDBACK, params,new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, final String content) {
				super.onSuccess(statusCode, content);
				Config.dismissProgress();
				try {
					if(!content.equals("") && new JSONObject(content).getBoolean("success")){
						ToysLeaseFeedbackActivity.this.finish();
					}
					Toast.makeText(ToysLeaseFeedbackActivity.this, new JSONObject(content).getString("reMsg"), 2).show();
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
			}
		});
		
	}
}

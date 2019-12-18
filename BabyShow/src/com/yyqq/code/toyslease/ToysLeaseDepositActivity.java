package com.yyqq.code.toyslease;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.utils.Config;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * 押金详情
 * */
public class ToysLeaseDepositActivity extends BaseActivity {

	private TextView toys_deposit_all;
	private TextView toys_deposit_title;
	private TextView toys_deposit_monery;
	private TextView toys_deposit_detail;
	private Button toys_deposit_submit;
	
	private AbHttpUtil ab;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		setContentView(R.layout.activity_toys_lease_deposit);
	}

	@Override
	protected void initView() {
		toys_deposit_all = (TextView) findViewById(R.id.toys_deposit_all);
		toys_deposit_title = (TextView) findViewById(R.id.toys_deposit_title);
		toys_deposit_monery = (TextView) findViewById(R.id.toys_deposit_monery);
		toys_deposit_detail = (TextView) findViewById(R.id.toys_deposit_detail);
		toys_deposit_submit = (Button) findViewById(R.id.toys_deposit_submit);
	}

	@Override
	protected void initData() {
		ab = AbHttpUtil.getInstance(ToysLeaseDepositActivity.this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		getDepositDetail();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void setListener() {
		
		findViewById(R.id.lease_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToysLeaseDepositActivity.this.finish();
			}
		});
		
		toys_deposit_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				postSetDeposit();
			}
		});
		
		toys_deposit_all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ToysLeaseDepositActivity.this, ToysLeaseDepositDetailActivity.class);
//				intent.putExtra("deposit_id", "0");
				startActivity(intent);
			}
		});
	}
	
	/**
	 * 获取押金详情
	 * */
	private void getDepositDetail(){
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(ToysLeaseDepositActivity.this).uid);
		params.put("order_id", "-10");
		ab.get(ServerMutualConfig.GET_DEPOSIT_DETAIL + "&" + params.toString(),new AbStringHttpResponseListener() {
			@Override
			public void onSuccess(int statusCode, final String content) {
				super.onSuccess(statusCode, content);
				try {
					if(!content.isEmpty() && new JSONObject(content).getBoolean("success")){
						toys_deposit_all.setText( new JSONObject(content).getJSONObject("data").getString("deposit_detail"));
						toys_deposit_title.setText( new JSONObject(content).getJSONObject("data").getString("deposit_title"));
						toys_deposit_monery.setText( new JSONObject(content).getJSONObject("data").getString("price"));
						toys_deposit_detail.setText( new JSONObject(content).getJSONObject("data").getString("total_deposit_detail"));
						
						if(new JSONObject(content).getJSONObject("data").getString("is_refund").equals("1")){
							toys_deposit_submit.setVisibility(View.VISIBLE);
						}else{
							toys_deposit_submit.setVisibility(View.GONE);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
			}
			
			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				super.onFailure(statusCode, content, error);
			}
		});
	}
	
	
	/**
	 * 申请退押金
	 * */
	private void postSetDeposit(){
		
		toys_deposit_submit.setVisibility(View.GONE);
		
		AlertDialog.Builder dialog = new Builder(ToysLeaseDepositActivity.this);
		dialog.setTitle("自由环球租赁");
		dialog.setMessage("退掉押金后，您下次租玩具时需要再次支付，确定要申请退押金吗？");
		dialog.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
					}

				});
		dialog.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						final AbRequestParams params = new AbRequestParams();
						params.put("login_user_id", Config.getUser(ToysLeaseDepositActivity.this).uid);
						params.put("order_id", "-10");
						params.put("source", "0");
						params.put("refund_status", "11");
						ab.get(ServerMutualConfig.POST_DEPOSIT + "&" + params.toString(),new AbStringHttpResponseListener() {
							@Override
							public void onSuccess(int statusCode, final String content) {
								super.onSuccess(statusCode, content);
								getDepositDetail();
								try {
									Toast.makeText(ToysLeaseDepositActivity.this, new JSONObject(content).getString("reMsg"), 2).show();
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							
							@Override
							public void onFinish() {
								super.onFinish();
							}
							
							@Override
							public void onFailure(int statusCode, String content,
									Throwable error) {
								super.onFailure(statusCode, content, error);
							}
						});
					}
				});
		dialog.create().show();
		
		
	}
	
	
}

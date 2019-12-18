package com.yyqq.code.toyslease;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
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
 * 账户余额提现
 * */
public class AccountWithdrawalsActivity extends BaseActivity {

	private TextView lease_yue;
	private TextView lease_yue_hint;
	private Button lease_tixian_bt;
	
	private AbHttpUtil ab;
	private String is_apply = "0";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		setContentView(R.layout.activity_account_withdrawals);
	}

	@Override
	protected void initView() {
		lease_yue = (TextView) findViewById(R.id.lease_yue);
		lease_yue_hint = (TextView) findViewById(R.id.lease_yue_hint);
		lease_tixian_bt = (Button) findViewById(R.id.least_tixian_bt);
	}

	@Override
	protected void initData() {
		ab = AbHttpUtil.getInstance(AccountWithdrawalsActivity.this);
		
		initMonery();
	}

	@Override
	protected void setListener() {
		
		findViewById(R.id.main_item_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AccountWithdrawalsActivity.this.finish();
			}
		});
		
		lease_tixian_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialog = new Builder(AccountWithdrawalsActivity.this);
				dialog.setTitle("自由环球租赁");
				dialog.setMessage("下次租玩具时还可以继续使用，确认申请提现吗？");
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
								Withdrawals();
							}
						});
				dialog.create().show();
			}
		});
	}
	
	/**
	 * 获取账户余额
	 * */
	private void initMonery(){
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(AccountWithdrawalsActivity.this).uid);
		ab.get(ServerMutualConfig.GET_USER_PRICE + "&" + params.toString(),new AbStringHttpResponseListener() {
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					if(!content.isEmpty() && new JSONObject(content).getBoolean("success")){
						lease_yue.setText(new JSONObject(content).getJSONObject("data").getString("account_price"));
						lease_yue_hint.setText(new JSONObject(content).getJSONObject("data").getString("description"));
						is_apply = new JSONObject(content).getJSONObject("data").getString("is_apply");
						if(is_apply.equals("0")){
							lease_tixian_bt.setVisibility(View.GONE);
							lease_tixian_bt.setClickable(false);
						}
					}else{
						Toast.makeText(AccountWithdrawalsActivity.this, new JSONObject(content).getString("reMsg"), 2).show();
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
	 * 申请提现
	 * */
	private void Withdrawals(){
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(AccountWithdrawalsActivity.this).uid);
		params.put("source", "1");
		ab.get(ServerMutualConfig.GET_USER_REFUND + "&" + params.toString(),new AbStringHttpResponseListener() {
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					if(!content.isEmpty() && new JSONObject(content).getBoolean("success")){
						Toast.makeText(AccountWithdrawalsActivity.this, "提交申请成功", 2).show();
						initMonery();
					}else{
						Toast.makeText(AccountWithdrawalsActivity.this, new JSONObject(content).getString("reMsg"), 2).show();
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
	
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
}
package com.yyqq.code.group;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.AddCheckActivity;
import com.yyqq.code.main.AddNewPostActivity;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.GroupRelevantUtils;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.ServerMutualConfig;


/**
 * 创建新群
 * */
public class AddNewGroupActivity extends BaseActivity {
	
	private TextView title_left;
	private TextView title_right;
	private EditText group_title;
	private EditText group_des;
	
	private AbHttpUtil http;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_group);
	}

	@Override
	protected void initView() {
		title_left = (TextView) findViewById(R.id.title_left);
		title_right = (TextView) findViewById(R.id.title_right);
		group_title = (EditText) findViewById(R.id.group_title);
		group_des = (EditText) findViewById(R.id.group_des);
	}

	@Override
	protected void initData() {
		http = AbHttpUtil.getInstance(AddNewGroupActivity.this);
	}

	@Override
	protected void setListener() {
		title_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cerateNewGroup();
			}
		});
		
		title_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AddNewGroupActivity.this.finish();
			}
		});
	}
	
	/**
	 * 创建新群
	 * */
	private void cerateNewGroup(){
		
		if(group_title.getText().toString().trim().isEmpty()){
			Toast.makeText(AddNewGroupActivity.this, "请输入您的群名称", 3).show();
			return;
		}
		
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(AddNewGroupActivity.this).uid);
		params.put("group_title", group_title.getText().toString().trim());
		params.put("description", group_des.getText().toString().trim());
		
		http.post(ServerMutualConfig.createGroup, params, new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					JSONObject json = new JSONObject(content);
					if(json.getBoolean("success")) {
						Toast.makeText(AddNewGroupActivity.this, json.getString("reMsg"), Toast.LENGTH_SHORT).show();
						
						Intent intent = new Intent(AddNewGroupActivity.this, SuperGroupActivity.class);
						intent.putExtra(SuperGroupActivity.GROUP_ID, json.getJSONObject("data").getString("group_id"));
						AddNewGroupActivity.this.startActivity(intent);
						
						AddNewGroupActivity.this.finish();
						if(null != AddNewPostActivity.showshowAddActivity){
							AddNewPostActivity.showshowAddActivity.finish();
						}
						if(null != AddCheckActivity.addCheckActivity){
							AddCheckActivity.addCheckActivity.finish();
						}
					}else{
						Toast.makeText(AddNewGroupActivity.this, json.getString("reMsg"), Toast.LENGTH_SHORT).show();
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

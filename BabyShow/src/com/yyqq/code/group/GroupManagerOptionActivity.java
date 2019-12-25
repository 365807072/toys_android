package com.yyqq.code.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.framework.activity.BaseActivity;

/**
 * 
 * 选择管理模块
 * 
 * @author lyl
 * 
 * */
public class GroupManagerOptionActivity extends BaseActivity {
	
	private RelativeLayout group_select_all;
	private RelativeLayout group_select_head;
	private RelativeLayout group_select_category;
	private RelativeLayout group_select_topic;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_manager_option);
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	}

	@Override
	protected void initView() {
		group_select_all = (RelativeLayout) findViewById(R.id.group_select_all);
		group_select_head = (RelativeLayout) findViewById(R.id.group_select_head);
		group_select_category = (RelativeLayout) findViewById(R.id.group_select_category);
		group_select_topic = (RelativeLayout) findViewById(R.id.group_select_topic);
	}

	@Override
	protected void initData() {
		
	}

	@Override
	protected void setListener() {
		group_select_all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				group_select_all.setVisibility(View.GONE);
				GroupManagerOptionActivity.this.finish();
			}
		});
		
		group_select_head.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				group_select_all.setVisibility(View.GONE);
				GroupManagerOptionActivity.this.finish();
				Intent intent = new Intent(GroupManagerOptionActivity.this, GroupManagerHeadActivity.class);
				intent.putExtra(SuperGroupActivity.GROUP_ID, getIntent().getStringExtra(SuperGroupActivity.GROUP_ID));
				startActivity(intent);
			}
		});
		
		group_select_category.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				group_select_all.setVisibility(View.GONE);
				GroupManagerOptionActivity.this.finish();
				Intent intent = new Intent(GroupManagerOptionActivity.this, GroupManagerCategoryActivity.class);
				intent.putExtra(SuperGroupActivity.GROUP_ID, getIntent().getStringExtra(SuperGroupActivity.GROUP_ID));
				startActivity(intent);
			}
		});
		
		group_select_topic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				group_select_all.setVisibility(View.GONE);
				GroupManagerOptionActivity.this.finish();
				Intent intent = new Intent(GroupManagerOptionActivity.this, GroupManagerTopicActivity.class);
				intent.putExtra(SuperGroupActivity.GROUP_ID, getIntent().getStringExtra(SuperGroupActivity.GROUP_ID));
				startActivity(intent);
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

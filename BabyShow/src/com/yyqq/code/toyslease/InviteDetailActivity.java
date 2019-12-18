package com.yyqq.code.toyslease;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yyqq.babyshow.R;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.view.MyHorizontialListView;
import com.yyqq.commen.view.MyListView;
import com.yyqq.framework.activity.BaseActivity;

/**
 * 邀请活动规则
 * */
public class InviteDetailActivity extends BaseActivity {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		setContentView(R.layout.activity_invite_friend);
	}
	
	@Override
	protected void initView() {
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setListener() {
	}
	
}

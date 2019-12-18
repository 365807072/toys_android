package com.yyqq.commen.utils;

import com.yyqq.babyshow.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

public class Rank extends Activity {
	
	private ImageView img;
	
	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		setContentView(R.layout.rank);
		img = (ImageView) findViewById(R.id.img);
		if(!TextUtils.isEmpty(getIntent().getStringExtra("business"))){
			img.setBackgroundResource(R.drawable.business_pay);
		}
	}

}

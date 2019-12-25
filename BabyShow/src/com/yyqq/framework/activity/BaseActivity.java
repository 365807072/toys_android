package com.yyqq.framework.activity;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;

import com.yyqq.commen.model.CityModel;
import com.yyqq.commen.model.DistrictModel;
import com.yyqq.commen.model.ProvinceModel;

/**
 * 抽象父类，其余页面全部继承该类实现
 * 
 * @author 李勇利
 */
public abstract class BaseActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public void setContentView(int layoutResID) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.setContentView(layoutResID);
		initView();
		initData();
		setListener();
	}

	@Override
	public void setContentView(View view) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.setContentView(view);
		initView();
		initData();
		setListener();
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.setContentView(view, params);
		initView();
		initData();
		setListener();
	}

	protected abstract void initView();

	protected abstract void initData();

	protected abstract void setListener();
	
}

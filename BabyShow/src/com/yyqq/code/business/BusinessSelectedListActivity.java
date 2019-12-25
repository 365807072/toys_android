package com.yyqq.code.business;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainItemDetialWebActivity;
import com.yyqq.commen.adapter.BusinessSelectedListAdapter;
import com.yyqq.commen.adapter.BusinessSelectedMoreAdapter;
import com.yyqq.commen.model.BusinessSelectedListBean;
import com.yyqq.commen.model.BusinessSelectedMoreBean;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;


/**
 * 精选商家列表
 * */
public class BusinessSelectedListActivity extends BaseActivity implements OnPullDownListener{

	// 区域ID key
	public static final String CITY_ID = "city_id";
	// 区域ID value
	private String city_id = "1";
	// title名称 key
	public static final String CITY_NAME = "city_name";
	
	// 全局
	private MyApplication app;
	private AbHttpUtil ab;
	private Activity context;
	private LayoutInflater inflater;
	private ImageView general_back;
	private TextView bus_title_hint_text;

	// 主列表展示
	private PullDownView mPullDownView;
	private ListView listView;
	private BusinessSelectedListAdapter mainAdapter;
	private ArrayList<BusinessSelectedListBean> mainData = new ArrayList<BusinessSelectedListBean>();
	
	// 查看其他区域
	private ImageView bs_sl_rgt_more;
	private RelativeLayout bn_sc_all;
	private ListView bn_sc_list;
	private BusinessSelectedMoreAdapter moreAdapter;
	private ArrayList<BusinessSelectedMoreBean> moreData = new ArrayList<BusinessSelectedMoreBean>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business_selected_list);
	}
	
	@Override
	protected void initView() {
		bus_title_hint_text = (TextView) findViewById(R.id.bus_title_hint_text);
		bn_sc_list = (ListView) findViewById(R.id.bn_sc_list);
		bn_sc_all = (RelativeLayout) findViewById(R.id.bn_sc_all);
		bs_sl_rgt_more = (ImageView) findViewById(R.id.bs_sl_rgt_more);
		general_back = (ImageView) findViewById(R.id.general_back);
		mPullDownView = (PullDownView) findViewById(R.id.list);
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.setShowHeader();// 显示并且可以使用头部刷新
	}

	@SuppressLint("NewApi")
	@Override
	protected void initData() {
		context = this;
		DisplayMetrics DM = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(DM);
		app = (MyApplication) context.getApplication();
		ab = AbHttpUtil.getInstance(context);
		inflater = LayoutInflater.from(context);
		
		mainAdapter = new BusinessSelectedListAdapter(context, inflater, app, mainData, DM.widthPixels);
		listView = mPullDownView.getListView();
		listView.setDivider(null);
		listView.setAdapter(mainAdapter);
		
		moreAdapter = new BusinessSelectedMoreAdapter(context, inflater, app, moreData);
		bn_sc_list.setAdapter(moreAdapter);
		
		// 获取区域名称
		if(getIntent().hasExtra(CITY_NAME) && !getIntent().getStringExtra(CITY_NAME).isEmpty()){
			bus_title_hint_text.setText(getIntent().getStringExtra(CITY_NAME));
		}
		
		// 获取区域ID
		if(getIntent().hasExtra(CITY_ID) && !getIntent().getStringExtra(CITY_ID).isEmpty()){
			city_id = getIntent().getStringExtra(CITY_ID);
		}
		onRefresh();
		
		getSelectedData();
	}

	@Override
	protected void setListener() {
		
		bn_sc_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(moreData.get(position).getStatus().equals("1")){
					bn_sc_all.setVisibility(View.GONE);
					bus_title_hint_text.setText(moreData.get(position).getTitle_center());
					city_id = moreData.get(position).getCity_id();
					onRefresh();
				}else{
					Toast.makeText(context, moreData.get(position).getTitle(), 3).show();
				}
			}
		});
		
		bs_sl_rgt_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				bn_sc_all.setVisibility(View.VISIBLE);
			}
		});

		bn_sc_all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				bn_sc_all.setVisibility(View.GONE);
			}
		});
		
		general_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				context.finish();
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				// 最后一条提示图
				if(mainData.get(position-1).getIs_last().equals("1")){
					return;
				}
				
				if(mainData.get(position-1).getPost_url().isEmpty()){
					Intent in = new Intent(context, BusinessDetailActivity.class);
					in.putExtra("business_id", mainData.get(position-1).getBusiness_id());
					startActivity(in);
				}else{
					Intent intent = new Intent(context, MainItemDetialWebActivity.class);
					intent.putExtra(MainItemDetialWebActivity.LINK_URL, mainData.get(position-1).getPost_url());
					startActivity(intent);
				}
				
			}
		});
	}

	@Override
	public void onRefresh() {
		getMainData(false);
	}

	@Override
	public void onMore() {
		getMainData(true);
	}
	
	// 获取主列表数据
	private void getMainData(final boolean isMore){
		
		// 判断网络是否正常
		if(!Config.isConn(context)){
			Toast.makeText(context, "请检查您的网络", 3).show();
			return;
		}
		
		final AbRequestParams paramsHot = new AbRequestParams();
		paramsHot.put("login_user_id", Config.getUser(context).uid);
		paramsHot.put("city_id", city_id);
		if(mainData.size() != 0 && isMore) {
			paramsHot.put("post_create_time", mainData.get(mainData.size()-1).getPost_create_time());
		}
		
		if(isMore && mainData.size() != 0 && mainData.get(mainData.size()-1).getPost_create_time().equals("0")){
			mPullDownView.setHideFooter();
			return;
		}
		
		ab.get(ServerMutualConfig.GET_SELECTED_BUSINESS_LIST + "&" + paramsHot.toString(), new AbStringHttpResponseListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				if (!content.isEmpty()) {
					
					if(!isMore){
						mainData.clear();
					}
					
					try {
						JSONObject json = new JSONObject(content);
						BusinessSelectedListBean item = new BusinessSelectedListBean();
						for (int i = 0; i < json.getJSONArray("data").length(); i++) {
							mainData.add(item.fromJson(json.getJSONArray("data").getJSONObject(i)));
						}
						mainAdapter.notifyDataSetChanged();
						
						if(!isMore && mainData.size() < 10){
							mPullDownView.setHideFooter();
						}else{
							mPullDownView.setShowFooter();
						}
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(context, "没有更多了", 3).show();
				}
				
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
				mPullDownView.notifyDidMore();
			}
			
			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				super.onFailure(statusCode, content, error);
			}
		});
	}
	
	// 获取其它选项
	private void getSelectedData(){
		// 判断网络是否正常
		if(!Config.isConn(context)){
			Toast.makeText(context, "请检查您的网络", 3).show();
			return;
		}
		
		final AbRequestParams paramsHot = new AbRequestParams();
		paramsHot.put("login_user_id", Config.getUser(context).uid);
		
		ab.get(ServerMutualConfig.GET_SELECTED_LIST + "&" + paramsHot.toString(), new AbStringHttpResponseListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				if (!content.isEmpty()) {
					
					try {
						JSONObject json = new JSONObject(content);
						BusinessSelectedMoreBean item = new BusinessSelectedMoreBean();
						for (int i = 0; i < json.getJSONArray("data").length(); i++) {
							moreData.add(item.fromJson(json.getJSONArray("data").getJSONObject(i)));
						}
						moreAdapter.notifyDataSetChanged();
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(context, "没有更多了", 3).show();
				}
				
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
				mPullDownView.notifyDidMore();
			}
			
			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				super.onFailure(statusCode, content, error);
			}
		});
	}

}

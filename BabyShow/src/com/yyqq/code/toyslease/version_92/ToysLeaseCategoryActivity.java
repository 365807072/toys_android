package com.yyqq.code.toyslease.version_92;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.code.main.MainItemDetialWebActivity;
import com.yyqq.code.toyslease.InviteFriendActivity;
import com.yyqq.code.toyslease.ToysLeaseDetailActivity;
import com.yyqq.code.toyslease.ToysLeaseOrderDetailActivity;
import com.yyqq.commen.adapter.ToysLeaseAllAdapter;
import com.yyqq.commen.model.ScrollOverListView;
import com.yyqq.commen.model.ToysLeaseMainListBean;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.ShoppingCartUtils;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;


/**
 * 分类玩具首页
 * 
 * */
public class ToysLeaseCategoryActivity extends BaseActivity implements OnPullDownListener{

	private ImageView lease_back;
	private LinearLayout lease_wait;
	private ImageView lease_wait_img;
	private ImageView lease_wait_point;
	private LinearLayout lease_complete;
	private ImageView lease_complete_img;
	private ImageView lease_complete_point;
	private LinearLayout lease_myself;
	private ImageView lease_myself_img;
	private ImageView lease_myself_point;
	private PullDownView mPullDownView;
	private ListView listview;
	private ImageView add_lease;
	private ImageView lease_main_right;
	private LinearLayout main_head_search;
	public static RelativeLayout search_all;
	public static ImageView search_to_cart;
	
	public static String CATEGORY_ID_KEY = "category_id_key";
	private AbHttpUtil ab;
	private LayoutInflater inflater;
	private ToysLeaseAllAdapter waitAdapter = null;
	private ToysLeaseAllAdapter completeAdapter = null;
	private ToysLeaseAllAdapter myselfAdapter = null;
	private ArrayList<ToysLeaseMainListBean> data = new ArrayList<ToysLeaseMainListBean>();
	private int showPage = 0; // 0 = 待租 ，1 = 已租，-1 = 自己
	private boolean isFirse = true;
	private int index = -2; 
	private String order_id = "";
	public static ToysLeaseCategoryActivity toysLeaseCategoryActivity = null;
	private TextView lease_title;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		setContentView(R.layout.activity_toys_lease_main);
	}

	@Override
	protected void initView() {
		search_all = (RelativeLayout) findViewById(R.id.toys_lease_main_all);
		search_to_cart = (ImageView) findViewById(R.id.search_to_cart);
		search_to_cart.setVisibility(View.VISIBLE);
		ShoppingCartUtils.badge = null;
		ShoppingCartUtils.DrewNumberBitmap(this, search_to_cart);
		lease_title = (TextView) findViewById(R.id.lease_title);
		lease_main_right = (ImageView) findViewById(R.id.lease_main_right);
//		add_lease = (ImageView) findViewById(R.id.add_lease);
		mPullDownView = (PullDownView) findViewById(R.id.lease_main_list);
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.setShowHeader();// 显示并且可以使用头部刷新
		mPullDownView.RefreshComplete();
		mPullDownView.notifyDidMore();
		mPullDownView.requestFocus();
		lease_back = (ImageView) findViewById(R.id.lease_back);
		lease_back.setVisibility(View.VISIBLE);
		lease_wait = (LinearLayout) findViewById(R.id.lease_wait);
		lease_wait_img = (ImageView) findViewById(R.id.lease_wait_img);
		lease_wait_point = (ImageView) findViewById(R.id.lease_wait_point);
		lease_complete = (LinearLayout) findViewById(R.id.lease_complete);
		lease_complete_img = (ImageView) findViewById(R.id.lease_complete_img);
		lease_complete_point = (ImageView) findViewById(R.id.lease_complete_point);
		lease_myself = (LinearLayout) findViewById(R.id.lease_myself);
		lease_myself_img = (ImageView) findViewById(R.id.lease_myself_img);
		lease_myself_point = (ImageView) findViewById(R.id.lease_myself_point);
		main_head_search = (LinearLayout) findViewById(R.id.main_head_search);
		main_head_search.setVisibility(View.GONE);
	}

	@Override
	protected void initData() {
		ShoppingCartUtils.ADD_FROM = "TOYS_CATEGORY";
		lease_title.setText("玩具");
		toysLeaseCategoryActivity = this;
		if(getIntent().hasExtra("order_id")){
			order_id = getIntent().getStringExtra("order_id");
		}
		ScrollOverListView.canRefleash = false;
		ab = AbHttpUtil.getInstance(ToysLeaseCategoryActivity.this);
		inflater = LayoutInflater.from(ToysLeaseCategoryActivity.this);
		
		// 加载右上角图片
		getRightIcon();		
		
		listview = mPullDownView.getListView();
		listview.setDivider(null);
		
		waitAdapter = new ToysLeaseAllAdapter(ToysLeaseCategoryActivity.this, inflater, MyApplication.getInstance(), data, 0, ab);
		completeAdapter = new ToysLeaseAllAdapter(ToysLeaseCategoryActivity.this, inflater, MyApplication.getInstance(), data, 1, ab);
		myselfAdapter = new ToysLeaseAllAdapter(ToysLeaseCategoryActivity.this, inflater, MyApplication.getInstance(), data, -1, ab);
		
		listview.setAdapter(waitAdapter);
		mPullDownView.enableAutoFetchMore(true, 1);// 设置可以自动获取更多 滑到最后一个自动获取
	}

	@Override
	protected void setListener() {
		
	search_to_cart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!MyApplication.getVisitor().equals("0")  || Config.getUser(ToysLeaseCategoryActivity.this).uid.equals("")){
					Intent in = new Intent(ToysLeaseCategoryActivity.this, WebLoginActivity.class);
					startActivity(in);
				}else{
//					ToysLeaseMainTabActivity.tabHost.setCurrentTab(2);
					ToysLeaseMainShoppingCartActivity.isShowBack = true;
					Intent intent = new Intent(ToysLeaseCategoryActivity.this, ToysLeaseMainShoppingCartActivity.class);
					startActivity(intent);
//					intent.putExtra("tabid", 2);
				}
			}
		});
		
		lease_main_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 // 检查登录状态
				if(!MyApplication.getVisitor().equals("0")  || Config.getUser(ToysLeaseCategoryActivity.this).uid.equals("")){
					Intent in = new Intent(ToysLeaseCategoryActivity.this, WebLoginActivity.class);
					startActivity(in);
				}else{
					Intent intent = new Intent(ToysLeaseCategoryActivity.this, InviteFriendActivity.class);
					startActivity(intent);
				}
			}
		});
		
		findViewById(R.id.lease_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToysLeaseCategoryActivity.this.finish();
			}
		});
		
//		add_lease.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				 // 检查登录状态
//				if(!MyApplication.getVisitor().equals("0")  || Config.getUser(ToysLeaseCategoryActivity.this).uid.equals("")){
//					Intent in = new Intent(ToysLeaseCategoryActivity.this, WebLoginActivity.class);
//					startActivity(in);
//				}else{
//					Intent intent = new Intent(ToysLeaseCategoryActivity.this, AddNewToysLeaseActivity.class);
//					startActivity(intent);
//				}
//			}
//		});
		
		lease_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToysLeaseCategoryActivity.this.finish();
			}
		});
		
		lease_wait.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showPage = 0;
				initWaitData(false);
			}
		});
		
		lease_complete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showPage = 1;
				initWaitData(false);
			}
		});

		lease_myself.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!MyApplication.getVisitor().equals("0")  || Config.getUser(ToysLeaseCategoryActivity.this).uid.equals("")){
					Intent in = new Intent(ToysLeaseCategoryActivity.this, WebLoginActivity.class);
					startActivity(in);
				}else{
					showPage = -1;
					initWaitData(false);
				}
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(data.get(position-1).getIs_jump().equals("0")){ // 玩具详情
					Intent intent = new Intent(ToysLeaseCategoryActivity.this, ToysLeaseDetailActivity.class);
					intent.putExtra(ToysLeaseDetailActivity.TOYS_DETAIL_KEY, data.get(position-1).getBusiness_id());
					startActivity(intent);
				}else if(data.get(position-1).getIs_jump().equals("2")){
					Intent intent = new Intent(ToysLeaseCategoryActivity.this, MainItemDetialWebActivity.class);
					intent.putExtra(MainItemDetialWebActivity.LINK_URL, data.get(position-1).getPost_url());
					ToysLeaseCategoryActivity.this.startActivity(intent);
				}
			}
		});
	}

	@Override
	public void onRefresh() {
		initWaitData(false);
	}

	@Override
	public void onMore() {
		initWaitData(true);
	}
	
	/**
	 * 初始化列表
	 * */
	private void initWaitData(final boolean isMore){
		Config.showProgressDialog(ToysLeaseCategoryActivity.this, false, null);
		if(!isMore){
			data.clear();
			if(showPage == 0){
				waitAdapter.notifyDataSetChanged();
			}else if(showPage == 1){
				completeAdapter.notifyDataSetChanged();
			}else if(showPage == -1){
				myselfAdapter.notifyDataSetChanged();
			}
		}
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(ToysLeaseCategoryActivity.this).uid);
		params.put("category_id", getIntent().getStringExtra(CATEGORY_ID_KEY));
		if (isMore && data.size() != 0) {
			params.put("post_create_time", data.get(data.size() - 1).getPostcreatetime());
		}
		ab.get(ServerMutualConfig.GET_CATEGORY_LIST + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Config.dismissProgress();
						try {
							if(!content.isEmpty() && new JSONObject(content).getBoolean("success")){
								JSONObject json = new JSONObject(content);
								if(!isMore){
									data.clear();
									if(json.getJSONArray("data").length() < 10){
										mPullDownView.setHideFooter();
									}else{
										mPullDownView.setShowFooter();
									}
								}else{
									if(json.getJSONArray("data").length() == 0){
										Toast.makeText(ToysLeaseCategoryActivity.this, "没有更多了", 2).show();
										mPullDownView.setHideFooter();
										return;
									}else{
										mPullDownView.setShowFooter();
									}
								}
								
								for (int i = 0; i < json.getJSONArray("data").length(); i++) {
									ToysLeaseMainListBean item = new ToysLeaseMainListBean();
									data.add(item.fromJson(json.getJSONArray("data").getJSONObject(i)));
								}
								
								if(index != -2){
									changeTitlePage(index);
									if(index == 0){
										showPage = 0;
										listview.setAdapter(waitAdapter);
										waitAdapter.notifyDataSetChanged();
									}else if(index == 1){
										showPage = 1;
										listview.setAdapter(completeAdapter);
										completeAdapter.notifyDataSetChanged();
									}else if(index == -1){
										showPage = -1;
										listview.setAdapter(myselfAdapter);
										myselfAdapter.notifyDataSetChanged();
									}
									index = -2;
								}else{
									changeTitlePage(showPage);
									if(isMore){
										if(showPage == 0){
											waitAdapter.notifyDataSetChanged();
										}else if(showPage == 1){
											completeAdapter.notifyDataSetChanged();
										}else if(showPage == -1){
											myselfAdapter.notifyDataSetChanged();
										}
									}else{
										if(showPage == 0){
											listview.setAdapter(waitAdapter);
											waitAdapter.notifyDataSetChanged();
										}else if(showPage == 1){
											listview.setAdapter(completeAdapter);
											completeAdapter.notifyDataSetChanged();
										}else if(showPage == -1){
											listview.setAdapter(myselfAdapter);
											myselfAdapter.notifyDataSetChanged();
										}
									}
								}
							}else{
								Toast.makeText(ToysLeaseCategoryActivity.this, new JSONObject(content).getString("reMsg"), 2).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
						if(!order_id.isEmpty()){
							Intent intent = new Intent(ToysLeaseCategoryActivity.this, ToysLeaseOrderDetailActivity.class);
							intent.putExtra(ToysLeaseOrderDetailActivity.ORDER_ID_KEY, getIntent().getStringExtra("order_id"));
							ToysLeaseCategoryActivity.this.startActivity(intent);
							order_id = "";
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						Config.dismissProgress();
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
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		isFirse = true;
		if(getIntent().hasExtra("index")){
			index = getIntent().getIntExtra("index", -2);
		}
		
		if(data.size()==0){
			onRefresh();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	/**
	 * 改变导航栏锁定按钮的状态
	 * */
	private void changeTitlePage(int clickIndex){
		switch (clickIndex) {
		case 0: // 待租
			lease_wait_point.setVisibility(View.VISIBLE);
			lease_complete_point.setVisibility(View.INVISIBLE);
			lease_myself_point.setVisibility(View.INVISIBLE);
			lease_wait_img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lease_d_down));
			lease_complete_img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lease_y_up));
			lease_myself_img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lease_z_up));
			break;
			
		case 1: // 已租
			lease_wait_point.setVisibility(View.INVISIBLE);
			lease_complete_point.setVisibility(View.VISIBLE);
			lease_myself_point.setVisibility(View.INVISIBLE);
			lease_wait_img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lease_d_up));
			lease_complete_img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lease_y_down));
			lease_myself_img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lease_z_up));
			break;

		case -1: // 自己
			lease_wait_point.setVisibility(View.INVISIBLE);
			lease_complete_point.setVisibility(View.INVISIBLE);
			lease_myself_point.setVisibility(View.VISIBLE);
			lease_wait_img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lease_d_up));
			lease_complete_img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lease_y_up));
			lease_myself_img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lease_z_down));
			break;
		}
	}
	
	/**
	 * 获取右上角图标
	 * */
	private void getRightIcon(){
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(this).uid);
		ab.get(ServerMutualConfig.GET_RIGHT_ICON + "&" + params.toString(), new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					if(new JSONObject(content).getBoolean("success")){
						// 加载右上角图片
						MyApplication.getInstance().display(new JSONObject(content).getJSONObject("data").getString("icon_img"), lease_main_right, R.drawable.def_image);
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
}
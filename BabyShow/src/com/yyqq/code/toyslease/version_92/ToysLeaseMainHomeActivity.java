package com.yyqq.code.toyslease.version_92;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.business.BusinessDetailActivity;
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.code.main.MainItemDetialActivity;
import com.yyqq.code.main.MainItemDetialWebActivity;
import com.yyqq.code.main.MainTab;
import com.yyqq.code.main.SpecialDetailList;
import com.yyqq.code.photo.ChangePhotoSize;
import com.yyqq.code.toyslease.InviteFriendActivity;
import com.yyqq.code.toyslease.ToysLeaseDetailActivity;
import com.yyqq.code.toyslease.ToysLeaseMainTabActivity;
import com.yyqq.code.video.VideoDetialActivity;
import com.yyqq.commen.adapter.ToysLeaseBannerApapter;
import com.yyqq.commen.adapter.ToysLeaseHomeHeadListApapter;
import com.yyqq.commen.adapter.ToysLeaseHomeMainListApapter;
import com.yyqq.commen.model.HeadListItem;
import com.yyqq.commen.model.MainListItemBean;
import com.yyqq.commen.model.PostBarTypeItem;
import com.yyqq.commen.model.ScrollOverListView;
import com.yyqq.commen.model.ToysLeaseHomeHeadBean;
import com.yyqq.commen.model.ToysLeaseHomeMainBean;
import com.yyqq.commen.model.ToysLeaseHomeMainItemBean;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.GPSLocationUtils;
import com.yyqq.commen.utils.GroupRelevantUtils;
import com.yyqq.commen.utils.UGallery;
import com.yyqq.commen.view.HorizontialListView;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;


/**
 * 玩具世界首页
 * */
public class ToysLeaseMainHomeActivity extends BaseActivity implements OnPullDownListener{

	private UGallery mGallery;
	private LinearLayout mGroup;
	private RelativeLayout mRelativeLayout;
	private ImageView mPointImg;
	private PullDownView mPullDownView;
	private ListView listview;
	private HorizontialListView toys_head_list;
	private ImageView lease_main_right;
	private TextView toys_search_text;
	private LinearLayout main_head_search;
	
	private AbHttpUtil ab;
	private LayoutInflater inflater;
	private int windowWidth;
	private int showIndex = 0;
	private ImageView[] myPoints;
	private ToysLeaseBannerApapter mImageAdapter;
	private ToysLeaseHomeHeadListApapter headListAdapter = null;
	private ToysLeaseHomeMainListApapter mainListAdapter = null;
	private ArrayList<HeadListItem> BannerList = new ArrayList<HeadListItem>();
	private ArrayList<ToysLeaseHomeHeadBean> heanList = new ArrayList<ToysLeaseHomeHeadBean>();
	private ArrayList<ToysLeaseHomeMainBean> mainList = new ArrayList<ToysLeaseHomeMainBean>();
	public int ADD_POINT = -1;
	private ArrayList<MainListItemBean> data = new ArrayList<MainListItemBean>();
	private SharedPreferences sp_main_list;
	private Editor ed_main_list;
	private SharedPreferences sp_image_list;
	private Editor ed_image_list;
	private Timer videoTimer = new Timer();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		setContentView(R.layout.activity_toys_lease_home);
	}

	@Override
	protected void initView() {
		inflater = LayoutInflater.from(this);
		
		// 初始化轮播图
		initHeadbar();
		
		mPullDownView = (PullDownView) findViewById(R.id.list);
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.setShowHeader();// 显示并且可以使用头部刷新
		listview = mPullDownView.getListView();
		listview.setDivider(null);
		if (mainListAdapter == null) {
			mainListAdapter = new ToysLeaseHomeMainListApapter(this, inflater, mainList);
		}
		listview.setAdapter(mainListAdapter);
		mPullDownView.enableAutoFetchMore(true, 1);// 设置可以自动获取更多 滑到最后一个自动获取
		mPullDownView.setShowHeaderLayout(mRelativeLayout);
		lease_main_right = (ImageView) findViewById(R.id.lease_main_right);
		toys_search_text = (TextView) findViewById(R.id.toys_search_text);
		main_head_search = (LinearLayout) findViewById(R.id.main_head_search);
	}

	@SuppressLint("NewApi")
	@Override
	protected void initData() {
		
		ab = AbHttpUtil.getInstance(this);
		
		// 防止触碰屏幕时，页面再次加载
		ScrollOverListView.canRefleash = false;
		
		// 主列表缓存对象
		sp_main_list = getSharedPreferences("toys_main_list", Context.MODE_PRIVATE);
		ed_main_list = sp_main_list.edit();
		
		// banner列表缓存对象
		sp_image_list = getSharedPreferences("imageList", Context.MODE_PRIVATE);
		ed_image_list = sp_image_list.edit();
		
		// 获取缓存，初始化主列表
		if(!sp_main_list.getString("toys_main_list", "").isEmpty()){
			initMainList(sp_main_list.getString("toys_main_list", ""), true);
		}else{
			getMainData(false);
		}
		
		// 获取缓存，初始化轮播图
		if (!sp_image_list.getString("imageList", "").isEmpty()) { 
			updateImageView(sp_image_list.getString("imageList", ""), true);
		}else{
			getHeadList();
		}
		
		// 获取右上角图标
		getRightIcon();
		
		// 获取搜索框提示语
		getSearchText();
		
		// 轮播图定时更换
		videoTimer.schedule(imageTask, 1, 1500);
	}

	@Override
	protected void setListener() {
		
		main_head_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ToysLeaseMainHomeActivity.this, ToysLeaseSearchActivity.class);
				ToysLeaseMainHomeActivity.this.startActivity(intent);
			}
		});
		
		lease_main_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 // 检查登录状态
				if(!MyApplication.getVisitor().equals("0")  || Config.getUser(ToysLeaseMainHomeActivity.this).uid.equals("")){
					Intent in = new Intent(ToysLeaseMainHomeActivity.this, WebLoginActivity.class);
					startActivity(in);
				}else{
					Intent intent = new Intent(ToysLeaseMainHomeActivity.this, InviteFriendActivity.class);
					startActivity(intent);
				}
			}
		});
		
		findViewById(R.id.find).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToysLeaseMainHomeActivity.this.finish();
			}
		});
		
		// 轮播图点击事件
		mGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				imagesClick(position);
			}
		});

		mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				showIndex = position;
				if (myPoints != null && myPoints.length > 0) {
					for (int i = 0; i < myPoints.length; i++) {
						if (position == i) {
							myPoints[i].setBackgroundResource(R.drawable.dian_hong);
						} else {
							myPoints[i].setBackgroundResource(R.drawable.dian_hui);
						}
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}
	
	@Override
	public void onRefresh() {
		getSearchText();
		getHeadList();
		getMainData(false);
	}

	@Override
	public void onMore() {
		getMainData(true);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		
		// 修改底部栏文字颜色
		ToysLeaseMainTabActivity.iconText.get(0).setTextColor(Color.parseColor("#fe6363"));
		ToysLeaseMainTabActivity.iconText.get(1).setTextColor(Color.parseColor("#000000"));
		ToysLeaseMainTabActivity.iconText.get(2).setTextColor(Color.parseColor("#000000"));
		ToysLeaseMainTabActivity.iconText.get(3).setTextColor(Color.parseColor("#000000"));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	
	/**
	 * 初始化轮播图
	 * */
	private void initHeadbar() {
		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		windowWidth = DM.widthPixels;
		mRelativeLayout = (RelativeLayout) inflater.inflate(R.layout.toys_home_head, null);

		mGallery = (UGallery) mRelativeLayout.findViewById(R.id.gallery);
		mGroup = (LinearLayout) mRelativeLayout.findViewById(R.id.viewGroup);
		mGroup.setVisibility(View.GONE);
		mGroup.setGravity(Gravity.CENTER);
		mImageAdapter = new ToysLeaseBannerApapter(this, BannerList, windowWidth);
		mGallery.setAdapter(mImageAdapter);
		
		toys_head_list = (HorizontialListView) mRelativeLayout.findViewById(R.id.toys_head_list);
//		toys_head_list.setDividerHeight(10);
	}
	
	/**
	 * 添加小圆点图片
	 */
	private void addPoint() {
		myPoints = new ImageView[BannerList.size()];
		for (int i = 0; i < BannerList.size(); i++) {
			mPointImg = new ImageView(ToysLeaseMainHomeActivity.this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(7, 0, 7, 0);
			mPointImg.setLayoutParams(params);
			myPoints[i] = mPointImg;
			if (i == 0) {
				myPoints[i].setBackgroundResource(R.drawable.dian_hong);
			} else {
				myPoints[i].setBackgroundResource(R.drawable.dian_hui);
			}
			mGroup.addView(myPoints[i]);
		}
	}

	/**
	 * 获取轮播图地址
	 * */
	public void getHeadList() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(this).uid);
		ab.get(ServerMutualConfig.GET_TOYS_BANNER_list + "&" + params.toString(), new AbStringHttpResponseListener() {
			
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						updateImageView(content, false);
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
	
	TimerTask imageTask = new TimerTask() {

		@Override
		public void run() {
			Message message = new Message();
			message.what = 0;
			handler.sendMessage(message);
		}
	};

	
	/**
	 * 开一个线程执行耗时操作
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2:
//				adapter.notifyDataSetChanged();
				break;
			case -1:
				mGroup.removeAllViews();
				addPoint();
				break;
			case 0:
				if (BannerList.size() == showIndex) {
					showIndex = 0;
				}
				mGallery.setSelection(showIndex);
				showIndex += 1;
				break;
			}
		}

	};
	
	/**
	 * 轮播图点击跳转判断
	 * */
	private void imagesClick(int position) {
		if("2".equals(BannerList.get(position).type)){
			GroupRelevantUtils.CheckIntent(this, BannerList.get(position).group_id);
		}else{
			Intent intent = new Intent();
			if ("1".equals(BannerList.get(position).is_click)) {
				if ("1".equals(BannerList.get(position).type)) { // 专题
					intent.setClass(this, SpecialDetailList.class);
					intent.putExtra("cate_id", BannerList.get(position).cate_id);
					intent.putExtra("cate_name", BannerList.get(position).cate_name);
				} else if ("3".equals(BannerList.get(position).type)) { // 帖子
					intent.setClass(this, MainItemDetialActivity.class);
					intent.putExtra("img_id", BannerList.get(position).img_id);
				} else if ("4".equals(BannerList.get(position).type)) { // 相册播放
					intent.setClass(this, ChangePhotoSize.class);
					ArrayList<String> imgBig = new ArrayList<String>();
					ArrayList<String> imaHed = new ArrayList<String>();
					ArrayList<String> imaWid = new ArrayList<String>();
					for (int i = 0; i < BannerList.get(position).imgList.size(); i++) {
						imgBig.add(BannerList.get(position).imgList.get(i).img_thumb);
						imaHed.add(BannerList.get(position).imgList.get(i).img_thumb_height);
						imaWid.add(BannerList.get(position).imgList.get(i).img_thumb_width);
					}
					intent.putStringArrayListExtra("imgList", imgBig);
					intent.putStringArrayListExtra("imaWid", imaWid);
					intent.putStringArrayListExtra("imaHed", imaHed);
					intent.putExtra("aotuplay", true);
				} else if ("5".equals(BannerList.get(position).type)) { // 值得买
					intent.setClass(this, MainItemDetialWebActivity.class);
					intent.putExtra(MainItemDetialWebActivity.LINK_URL, BannerList.get(position).business_url);
				} else if ("6".equals(BannerList.get(position).type)) { // 商家
					intent.setClass(this, BusinessDetailActivity.class);
					intent.putExtra("business_id", BannerList.get(position).business_id);
					intent.putExtra("business_name", BannerList.get(position).business_name);
				} else if ("8".equals(BannerList.get(position).type)) { // 视频
					PostBarTypeItem bean = new PostBarTypeItem();
					bean.setImg_id(BannerList.get(position).img_id);
					bean.setVideo_url(BannerList.get(position).video_url);
					bean.setImg(BannerList.get(position).imgList.get(0).img_thumb);
					bean.setImg_thumb_width(BannerList.get(position).imgList.get(0).img_thumb_width);
					bean.setImg_thumb_height(BannerList.get(position).imgList.get(0).img_thumb_height);
					
					intent.setClass(this, VideoDetialActivity.class);
					Bundle bun = new Bundle();
					bun.putSerializable(VideoDetialActivity.VIIDEO_INFO, bean);
					intent.putExtras(bun);
				} else if ("9".equals(BannerList.get(position).type)) { // 玩具列表
					intent.setClass(this, ToysLeaseCategoryActivity.class);
					intent.putExtra(ToysLeaseCategoryActivity.CATEGORY_ID_KEY, BannerList.get(position).img_id);
				} else if ("10".equals(BannerList.get(position).type)) { // 玩具详情
					intent.setClass(this, ToysLeaseDetailActivity.class);
					intent.putExtra(ToysLeaseDetailActivity.TOYS_DETAIL_KEY, BannerList.get(position).img_id);
				} else {// 商家列表
					intent.setClass(this, MainTab.class);
					intent.putExtra("tabid", 1);
					intent.putExtra("lat", GPSLocationUtils.getLatitude(this));
					intent.putExtra("lon", GPSLocationUtils.getLongitude(this));
				}
				startActivity(intent);
			}
		}
	}

	/**
	 * 初始化banner
	 * */
	@SuppressLint("NewApi")
	private void updateImageView(String content, boolean isSave) {
		try {
			
			if(new JSONObject(content).getBoolean("success")){
				
				// banner无需考虑分页，每次都可清空数据
				BannerList.clear();
				mGroup.removeAllViews();
				
				// 更新缓存
				ed_image_list.putString("imageList", content);
				ed_image_list.commit();
				
				JSONObject json = new JSONObject(content);
				for (int i = 0; i < json.getJSONArray("data").length(); i++) {
					HeadListItem item = new HeadListItem();
					item.fromJson(json.getJSONArray("data").getJSONObject(i), content);
					BannerList.add(item);
				}
				mImageAdapter.notifyDataSetChanged();
				handler.sendEmptyMessage(ADD_POINT);
				
				if(json.has("data1")){
					heanList.clear();
					ToysLeaseHomeHeadBean item = null;
					for (int i = 0; i < json.getJSONArray("data1").length(); i++) {
						item = new ToysLeaseHomeHeadBean();
						item.setId(json.getJSONArray("data1").getJSONObject(i).getString("business_id"));
						item.setImgUrl(json.getJSONArray("data1").getJSONObject(i).getString("img_thumb"));
						item.setIs_jump(json.getJSONArray("data1").getJSONObject(i).getString("is_jump"));
						item.setPost_url(json.getJSONArray("data1").getJSONObject(i).getString("post_url"));
						heanList.add(item);
					}
					headListAdapter = new ToysLeaseHomeHeadListApapter(this , heanList, windowWidth, inflater);
					toys_head_list.setAdapter(headListAdapter);
					
					toys_head_list.setOnItemClickListener(new OnItemClickListener() {
						
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							if(heanList.get(position).getIs_jump().equals("0")){
								Intent intent = new Intent(ToysLeaseMainHomeActivity.this, ToysLeaseDetailActivity.class);
								intent.putExtra(ToysLeaseDetailActivity.TOYS_DETAIL_KEY, heanList.get(position).getId());
								ToysLeaseMainHomeActivity.this.startActivity(intent);
							}else{
								Intent intent = new Intent(ToysLeaseMainHomeActivity.this, MainItemDetialWebActivity.class);
								intent.putExtra(MainItemDetialWebActivity.LINK_URL, heanList.get(position).getPost_url());
								ToysLeaseMainHomeActivity.this.startActivity(intent);
							}
						}
					});
					
					// 如果是从缓存去的数据，需要重新加载
					if(isSave && Config.isConn(this)){
						getHeadList();
					}
				}
			} 
		}catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取列表数据
	 * */
	private void getMainData(final boolean isMore){
		
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(this).uid);
		
		if(isMore && mainList.size() != 0){
			params.put("post_create_time", mainList.get(mainList.size()-1).getPost_create_time());
		}
		
		ab.get(ServerMutualConfig.GET_TOYS_MAIN_LIST + "&" + params.toString(),
				new AbStringHttpResponseListener() {
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				if(!isMore){
					mainList.clear();
				}
				initMainList(content, false);
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				mPullDownView.RefreshComplete();
				mPullDownView.notifyDidMore();
			}
			
			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				super.onFailure(statusCode, content, error);
			}
		});
	}
	
	
	/**
	 * 初始化主列表数据
	 * */
	private void initMainList(String content, boolean isSave){
		try {
			if(new JSONObject(content).getBoolean("success")){
				
				ed_main_list.putString("toys_main_list", content);
				ed_main_list.commit();
				
				JSONArray jsonList = new JSONObject(content).getJSONArray("data");
				ToysLeaseHomeMainBean bean = null;
				for(int i = 0 ; i < new JSONObject(content).getJSONArray("data").length(); i++){
					bean = new ToysLeaseHomeMainBean();
					bean.setCate_title(jsonList.getJSONObject(i).getString("cate_title"));
					bean.setMore_title(jsonList.getJSONObject(i).getString("more_title"));
					bean.setCategory_id(jsonList.getJSONObject(i).getString("category_id"));
					bean.setStyle(jsonList.getJSONObject(i).getString("style"));
					bean.setType(jsonList.getJSONObject(i).getString("type"));
					bean.setPost_create_time(jsonList.getJSONObject(i).getString("post_create_time"));
					
					ToysLeaseHomeMainItemBean itemBean = null;
					for(int j = 0 ; j < jsonList.getJSONObject(i).getJSONArray("toys_info").length() ; j++ ){
						itemBean = new ToysLeaseHomeMainItemBean();
						itemBean.setBusiness_id(jsonList.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("business_id"));
						itemBean.setBusiness_title(jsonList.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("business_title"));
						itemBean.setImg_thumb(jsonList.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("img_thumb"));
						itemBean.setImg_thumb_height(jsonList.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("img_thumb_height"));
						itemBean.setImg_thumb_width(jsonList.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("img_thumb_width"));
						itemBean.setMarket_price(jsonList.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("market_price"));
						itemBean.setSell_price(jsonList.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("sell_price"));
						itemBean.setPost_url(jsonList.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("post_url"));
						itemBean.setCategory_id(jsonList.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("category_id"));
						itemBean.setStyle(jsonList.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("style"));
						itemBean.setType(jsonList.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("type"));
						itemBean.setIcon_url(jsonList.getJSONObject(i).getJSONArray("toys_info").getJSONObject(j).getString("new_size_img_thumb"));
						bean.getItemBean().add(itemBean);
					}
					mainList.add(bean);
				}
				mainListAdapter.notifyDataSetChanged();
				
				// 如果是从缓存取得数据，需要重新加载
				if(isSave && Config.isConn(this)){
					getMainData(false);
				}
				
				if(new JSONObject(content).getJSONArray("data").length() < 10){
					mPullDownView.setHideFooter();
				}else{
					mPullDownView.setShowFooter();
				}
			}
		} catch (Exception e) {
		}
	}
	
	/**
	 * 获取搜索提示语
	 * */
	private void getSearchText(){
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(this).uid);
		ab.get(ServerMutualConfig.GET_TOYS_HINT_TEXT + "&" + params.toString(), new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					if(new JSONObject(content).getBoolean("success")){
						toys_search_text.setText("   " + new JSONObject(content).getJSONObject("data").getString("search_name"));
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

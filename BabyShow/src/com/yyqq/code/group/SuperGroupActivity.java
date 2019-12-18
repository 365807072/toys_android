package com.yyqq.code.group;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.yyqq.code.business.BusinessDetailActivity;
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.code.main.AddCheckActivity;
import com.yyqq.code.main.AddNewPostActivity;
import com.yyqq.code.main.MainTab;
import com.yyqq.code.main.PhotoManager;
import com.yyqq.code.personal.UserInfo;
import com.yyqq.code.toyslease.ToysLeaseMainTabActivity;
import com.yyqq.commen.adapter.GroupDetialCatagoryAdapter;
import com.yyqq.commen.adapter.MainInfoListAdapter;
import com.yyqq.commen.model.MainListItemBean;
import com.yyqq.commen.share.GroupSharedUtils;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * 
 * 超级社区首页
 * 
 * @author lyl
 * 
 * */
public class SuperGroupActivity extends BaseActivity implements OnPullDownListener{

	public static String GROUP_ID = "GROUP_ID";
	private boolean isAdmin = false;
	
	private ImageView group_main_bgimg;
	private PullDownView mPullDownView;
	private ListView listview;
	private LinearLayout mRelativeLayout;
	private TextView main_item_title;
	private ImageView group_main_finish;
	private ImageView group_main_edit;
	private TextView group_main_businessName;
	private ImageView group_main_businessIcon;
	private TextView group_main_detialText;
	private ImageView group_main_add;
	private TextView group_main_addNumber;
	private RelativeLayout group_main_headAll;
	private ListView group_detail_option;
	private RelativeLayout group_detail_option_hint;
	private ImageView add_new;
	private ImageView to_record;
	private ImageView to_photo;
	private ImageView to_business;
	private ImageView to_business_list;
	private ImageView to_toys_list;
	private ImageView group_main_shared;
	private LinearLayout super_show_more;
	private TextView main_group_title;
	private TextView main_group_title_02;
	
	private String shared_img = "";
	private String shared_title = "";
	private String shared_des = "";
	private String group_id = "";
	private String business_id = "";
	private String group_class_id = "0";
	private String group_state = "0";
	private String send_user_id = "";
	private boolean isFollow = false;
	private LayoutInflater inflater;
	private AbHttpUtil ab;
	private MainInfoListAdapter adapter;
	private GroupDetialCatagoryAdapter cateGroryAdapter;
	private ArrayList<MainListItemBean> data = new ArrayList<MainListItemBean>();
	private ArrayList<String> cateGoryList = new ArrayList<String>();
	private ArrayList<String> cateGoryIdList = new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		setContentView(R.layout.activity_super_group);
		
	}

	@Override
	protected void initView() {
		inflater = LayoutInflater.from(this);
		mRelativeLayout = (LinearLayout) inflater.inflate(R.layout.super_group_head, null);
		group_main_shared = (ImageView) mRelativeLayout.findViewById(R.id.group_main_shared);
		to_record = (ImageView) mRelativeLayout.findViewById(R.id.to_record);
		to_photo = (ImageView) mRelativeLayout.findViewById(R.id.to_photo);
		to_business_list = (ImageView) mRelativeLayout.findViewById(R.id.to_businessList);
		to_toys_list = (ImageView) mRelativeLayout.findViewById(R.id.to_toyList);
		to_business = (ImageView) mRelativeLayout.findViewById(R.id.to_business);
		group_main_bgimg = (ImageView) mRelativeLayout.findViewById(R.id.group_main_bgimg);
		group_main_bgimg.setLayoutParams(MyApplication.getVideoPlayerParams(-1, 2));
		group_main_finish = (ImageView) mRelativeLayout.findViewById(R.id.group_main_finish);
		group_main_edit = (ImageView) mRelativeLayout.findViewById(R.id.group_main_edit);
		group_main_businessName = (TextView) mRelativeLayout.findViewById(R.id.group_main_businessName);
		group_main_businessIcon = (ImageView) mRelativeLayout.findViewById(R.id.group_main_businessIcon);
		group_main_detialText = (TextView) mRelativeLayout.findViewById(R.id.group_main_detialText);
		group_main_add = (ImageView) mRelativeLayout.findViewById(R.id.group_main_add);
		group_main_addNumber = (TextView) mRelativeLayout.findViewById(R.id.group_main_addNumber);
		group_main_headAll = (RelativeLayout) mRelativeLayout.findViewById(R.id.group_main_headAll);
		main_group_title = (TextView) mRelativeLayout.findViewById(R.id.group_main_title);
		main_group_title_02 = (TextView) mRelativeLayout.findViewById(R.id.group_main_title_02);
		group_main_headAll.setLayoutParams(MyApplication.getImageViewParams(2));
		group_detail_option = (ListView) findViewById(R.id.group_detail_option);
		group_detail_option_hint = (RelativeLayout) findViewById(R.id.group_detail_option_hint);
		super_show_more = (LinearLayout) findViewById(R.id.super_show_more);
		mPullDownView = (PullDownView) findViewById(R.id.list);
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.setShowHeader();// 显示并且可以使用头部刷新
		listview = mPullDownView.getListView();
		listview.setDivider(null);
		add_new = (ImageView) findViewById(R.id.add_new);
		
	}

	@Override
	protected void initData() {
		
		group_id = getIntent().getStringExtra(GROUP_ID);
		if(getIntent().hasExtra("isAdmin")){
			isAdmin = getIntent().getBooleanExtra("isAdmin", false);
		}
		
		ab = AbHttpUtil.getInstance(this);
		if (adapter == null) {
			adapter = new MainInfoListAdapter(SuperGroupActivity.this, data, false , true);
		}
		listview.setAdapter(adapter);
		mPullDownView.enableAutoFetchMore(true, 1);// 设置可以自动获取更多 滑到最后一个自动获取
		mPullDownView.setShowHeaderLayout(mRelativeLayout);
		
		onRefresh();
	}

	@Override
	protected void setListener() {
		
		to_toys_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SuperGroupActivity.this, ToysLeaseMainTabActivity.class));
			}
		});
		
		super_show_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(group_detail_option_hint.isShown()){
					group_detail_option_hint.setVisibility(View.GONE);
				}else{
					group_detail_option_hint.setVisibility(View.VISIBLE);
				}
			}
		});
		
		group_main_shared.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GroupSharedUtils.groupShareBean bean = new GroupSharedUtils.groupShareBean();
				bean.setContext(SuperGroupActivity.this);
				bean.setShare_img(shared_img);
				bean.setShare_text(shared_des);
				bean.setShare_title(shared_title);
				bean.setShare_url("http://www.meimei.yihaoss.top/groupShare/groupIndex.html?"+"login_user_id="+Config.getUser(SuperGroupActivity.this).uid+"&group_id="+group_id);
				GroupSharedUtils.SharedGroupMore(bean);
			}
		});
		
		to_record.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), MainTab.class);
				intent.putExtra("tabid", 3);
				startActivity(intent);
			}
		});
		
		to_business.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), BusinessDetailActivity.class);
				intent.putExtra("business_id", business_id);
				startActivity(intent);
			}
		});

		to_photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), PhotoManager.class);
				startActivity(intent);
			}
		});
		
		add_new.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if("1".equals(MyApplication.getVisitor())){ // 没有登录
					Intent in = new Intent(SuperGroupActivity.this, WebLoginActivity.class);
					startActivity(in);
				}else{
					AddCheckActivity.isToMain = false;
					Intent intent = new Intent(v.getContext(), AddNewPostActivity.class);
					intent.putExtra("group_id", group_id);
					startActivity(intent);
				}
			}
		});
		
		group_detail_option_hint.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				group_detail_option_hint.setVisibility(View.GONE);
			}
		});
		
		group_main_finish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SuperGroupActivity.this.finish();
			}
		});
		
		group_main_edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SuperGroupActivity.this, GroupManagerOptionActivity.class);
				intent.putExtra(SuperGroupActivity.GROUP_ID, group_id);
				SuperGroupActivity.this.startActivity(intent);
			}
		});
		
		group_main_headAll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = null;
				if(group_state.equals("0")){
					intent = new Intent(SuperGroupActivity.this, UserInfo.class);
					intent.putExtra("uid", send_user_id);
				}else{
					intent = new Intent(SuperGroupActivity.this, BusinessDetailActivity.class);
					intent.putExtra("business_id", business_id);
				}
				SuperGroupActivity.this.startActivity(intent);
			}
		});
		
		group_main_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateFollowType();
			}
		});
		
		group_detail_option.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == 0){ // 全部
					group_detail_option_hint.setVisibility(View.GONE);
					group_class_id = "0";
					initMainListInfo(false);
				}else{
					if(isAdmin){
						if(position == cateGoryList.size()-1){
							group_detail_option_hint.setVisibility(View.GONE);
							Intent intent = new Intent(SuperGroupActivity.this, GroupManagerCategoryActivity.class);
							intent.putExtra(SuperGroupActivity.GROUP_ID, group_id);
							SuperGroupActivity.this.startActivity(intent);
						}else{
							group_detail_option_hint.setVisibility(View.GONE);
							group_class_id = cateGoryIdList.get(position);
							initMainListInfo(false);
						}
					}else{
						group_detail_option_hint.setVisibility(View.GONE);
						group_class_id = cateGoryIdList.get(position);
						initMainListInfo(false);
					}
				}
			}
		});
		
	}
	
	/**
	 * 获取滚动距离
	 * */
	public int getScrollY() {
		View c = listview.getChildAt(0);
		if (c == null) {
			return 0;
		}
		int firstVisiblePosition = listview.getFirstVisiblePosition();
		int top = c.getTop();
		int headerHeight = 0;

		if (firstVisiblePosition >= 1) {
			headerHeight = listview.getHeight();
		}

		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}


	@Override
	public void onRefresh() {
		MobclickAgent.onResume(this);
		initGroupHeanInfo();
		initMainListInfo(false);
		initCategoryList();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onMore() {
		initMainListInfo(true);
	}
	
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
	
	/**
	 * 获取主列表数据
	 * */
	private void initMainListInfo(final boolean getMore){
		Config.showProgressDialog(this, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(this).uid);
		params.put("group_id", group_id);
		params.put("group_class_id", group_class_id);
		if(getMore && data.size() != 0){
			params.put("post_create_time", data.get(data.size() - 1).getImgList().get(data.get(data.size() - 1).getImgList().size() - 1).getPost_create_time());
		}
		ab.get(ServerMutualConfig.superGroup + "&" + params.toString(), new AbStringHttpResponseListener() {
			
					@SuppressLint("NewApi")
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if(!getMore){
							data.clear();
						}
						if (!content.isEmpty()) {
							try {
								
								JSONObject json = new JSONObject(content);
								if(json.getJSONArray("data").length() == 0 && getMore){
									mPullDownView.setHideFooter();
									Toast.makeText(SuperGroupActivity.this, "没有更多了", 3).show();
									return;
								}else{
									mPullDownView.setShowFooter();
								}
								
								for (int i = 0; i < json.getJSONArray("data").length(); i++) {
									MainListItemBean item = new MainListItemBean();
									data.add(item.fromJson(json.getJSONArray("data").getJSONObject(i)));
								}
								SuperGroupActivity.this.runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										adapter.notifyDataSetChanged();
									}
								});
							} catch (JSONException e) {
								e.printStackTrace();
							}
						} else {
							Config.dismissProgress();
							Toast.makeText(SuperGroupActivity.this, "没有更多了", 3).show();
						}
						Config.dismissProgress();
					}

					@Override
					public void onFinish() {
						super.onFinish();
						mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
						mPullDownView.notifyDidMore();
						Config.dismissProgress();
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}
				});
	}
	
	/**
	 * 获取头部信息
	 * */
	private void initGroupHeanInfo(){
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(this).uid);
		params.put("group_id", group_id);
		ab.get(ServerMutualConfig.groupHeadInfo + "&" + params.toString(), new AbStringHttpResponseListener() {
			
					@SuppressLint("NewApi")
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (!content.isEmpty()) {
							try {
								JSONObject json = new JSONObject(content);
								
								shared_img = json.getJSONObject("data").getString("cover");
								shared_title = json.getJSONObject("data").getString("group_name");
								shared_des = json.getJSONObject("data").getString("description");
								
								send_user_id = json.getJSONObject("data").getString("user_id");
								group_state = json.getJSONObject("data").getString("group_state");
								business_id = json.getJSONObject("data").getString("business_id");
								
								main_group_title_02.setText(json.getJSONObject("data").getString("recommend_title"));
								main_group_title.setText(json.getJSONObject("data").getString("recommend_title"));
								group_main_businessName.setText(json.getJSONObject("data").getString("group_name"));
								group_main_detialText.setText(json.getJSONObject("data").getString("description"));
								group_main_addNumber.setText(json.getJSONObject("data").getString("idol_count") + "人已关注");
								
								MyApplication.getInstance().display(json.getJSONObject("data").getString("cover"), group_main_bgimg, R.drawable.def_image);
								if(json.getJSONObject("data").getString("is_idol").equals("1")){
									group_main_add.setImageBitmap(BitmapFactory.decodeResource(SuperGroupActivity.this.getResources(), R.drawable.group_main_no));
									isFollow = true;
								}
								
								if(json.getJSONObject("data").getString("group_state").equals("0")){
									group_main_businessIcon.setVisibility(View.GONE);
								}
								
								if(json.getJSONObject("data").getString("is_business").equals("1")){
									to_business.setVisibility(View.VISIBLE);
								}
								
								if(json.getJSONObject("data").getString("is_toys").equals("1")){
									to_toys_list.setVisibility(View.VISIBLE);
								}
								
								if(json.getJSONObject("data").getString("is_growth").equals("1")){
									to_record.setVisibility(View.VISIBLE);
								}
								
								if(json.getJSONObject("data").getString("is_album").equals("1")){
									to_photo.setVisibility(View.VISIBLE);
								}
								
								if(send_user_id.equals(Config.getUser(SuperGroupActivity.this).uid)){
									isAdmin = true;
									main_group_title_02.setVisibility(View.GONE);
									main_group_title.setVisibility(View.GONE);
									
								}else{
									main_group_title_02.setVisibility(View.GONE);
									main_group_title.setVisibility(View.GONE);
								}
								
								if(isAdmin){
									group_main_edit.setVisibility(View.VISIBLE);
								}
								
								if(json.getJSONObject("data").getString("color_index").equals("1")){
									group_main_businessName.setTextColor(Color.RED);
									group_main_detialText.setTextColor(Color.RED);
									group_main_addNumber.setTextColor(Color.RED);
								}else if(json.getJSONObject("data").getString("color_index").equals("2")){
									group_main_businessName.setTextColor(Color.YELLOW);
									group_main_detialText.setTextColor(Color.YELLOW);
									group_main_addNumber.setTextColor(Color.YELLOW);
								}else if(json.getJSONObject("data").getString("color_index").equals("3")){
									group_main_businessName.setTextColor(Color.WHITE);
									group_main_detialText.setTextColor(Color.WHITE);
									group_main_addNumber.setTextColor(Color.WHITE);
								}else if(json.getJSONObject("data").getString("color_index").equals("4")){
									group_main_businessName.setTextColor(Color.BLACK);
									group_main_detialText.setTextColor(Color.BLACK);
									group_main_addNumber.setTextColor(Color.BLACK);
								}else if(json.getJSONObject("data").getString("color_index").equals("5")){
									group_main_businessName.setTextColor(Color.BLUE);
									group_main_detialText.setTextColor(Color.BLUE);
									group_main_addNumber.setTextColor(Color.BLUE);
								}
								
							} catch (JSONException e) {
								e.printStackTrace();
							}
						} else {
							Config.dismissProgress();
							Toast.makeText(SuperGroupActivity.this, "没有更多了", 3).show();
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
						mPullDownView.notifyDidMore();
						Config.dismissProgress();
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}
				});
	}
	
	/**
	 * 更新关注状态
	 * */
	private void updateFollowType(){
		
		String url = "";
		if(isFollow){
			url = ServerMutualConfig.CancelPostIdol;
		}else{
			url = ServerMutualConfig.PostIdol;
		}
		
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(this).uid);
		params.put("group_id", group_id);
		ab.post(url, params, new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode,
							String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								Toast.makeText(SuperGroupActivity.this,json.getString("reMsg"),Toast.LENGTH_SHORT).show();
								if(isFollow){
									group_main_add.setImageBitmap(BitmapFactory.decodeResource(SuperGroupActivity.this.getResources(), R.drawable.group_main_add));
									isFollow = false;
								}else{
									group_main_add.setImageBitmap(BitmapFactory.decodeResource(SuperGroupActivity.this.getResources(), R.drawable.group_main_no));
									isFollow = true;
								}
								initGroupHeanInfo();
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
					public void onFailure(int statusCode,
							String content, Throwable error) {
						super.onFailure(statusCode, content, error);
					}
				});
	}
	
	/**
	 * 获取分类列表
	 * */
	private void initCategoryList(){
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(this).uid);
		params.put("group_id", group_id);
		ab.get(ServerMutualConfig.getGroupCategoryV1 + "&" + params.toString(), new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode,
					String content) {
				super.onSuccess(statusCode, content);
				cateGoryList.clear();
				cateGoryIdList.clear();
				cateGoryList.add("全部");
				cateGoryIdList.add("0");
				try {
					JSONObject json = new JSONObject(content);
					for(int i = 0 ; i < json.getJSONArray("data").length() ; i++){
						cateGoryList.add(json.getJSONArray("data").getJSONObject(i).getString("title"));
						cateGoryIdList.add(json.getJSONArray("data").getJSONObject(i).getString("group_class_id"));
					}
					
					if(isAdmin){
						cateGoryList.add("+添加分类");
					}
					
					cateGroryAdapter = new GroupDetialCatagoryAdapter(SuperGroupActivity.this, inflater, cateGoryList);
					group_detail_option.setAdapter(cateGroryAdapter);
				
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onFailure(int statusCode,
					String content, Throwable error) {
				super.onFailure(statusCode, content, error);
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
//		onRefresh();
	}
}

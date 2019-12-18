package com.yyqq.code.toyslease.version_93;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
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
import com.yyqq.code.toyslease.ToysLeaseMainTabActivity;
import com.yyqq.code.toyslease.version_92.ToysLeaseMainShoppingCartActivity;
import com.yyqq.code.toyslease.version_92.ToysLeaseSearchActivity;
import com.yyqq.commen.adapter.ToysLeaseAllItemAdapter;
import com.yyqq.commen.adapter.ToysLeaseMainAllAdapter;
import com.yyqq.commen.adapter.ToysSelectHintApapter;
import com.yyqq.commen.adapter.ToysSelectHintScreenApapter;
import com.yyqq.commen.model.ScrollOverListView;
import com.yyqq.commen.model.ToysAllSelect01Bean;
import com.yyqq.commen.model.ToysAllSelect01ItemBean;
import com.yyqq.commen.model.ToysLeaseMainListBean;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.ShoppingCartUtils;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * 全部玩具首页
 * 
 * */
public class SearchResultActivity extends BaseActivity implements OnPullDownListener {

	public static RelativeLayout toys_lease_main_all;
	public static ImageView search_to_cart;
	private PullDownView mPullDownView;
	private ListView listview;
	private TextView lease_main_right_text;
	private EditText search_main_ed;
	private ImageView toys_search_no_hint_img;
	private RelativeLayout toys_search_no_hint_all;
	
	public static String CATEGORY_ID_KEY = "category_id";
	private AbHttpUtil ab;
	private LayoutInflater inflater;
	private ToysLeaseMainAllAdapter waitAdapter = null;
	private ArrayList<ToysLeaseMainListBean> data = new ArrayList<ToysLeaseMainListBean>();
	public static SearchResultActivity searchResultMainActivity = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		setContentView(R.layout.activity_toys_lease_search_result);
	}

	@Override
	protected void initView() {
		toys_search_no_hint_img = (ImageView) findViewById(R.id.toys_search_no_hint_img);
		toys_search_no_hint_all = (RelativeLayout) findViewById(R.id.toys_search_no_hint_all);
		search_main_ed = (EditText) findViewById(R.id.search_main_ed);
		lease_main_right_text = (TextView) findViewById(R.id.lease_main_right_text);
		search_to_cart = (ImageView) findViewById(R.id.search_to_cart);
		search_to_cart.setVisibility(View.VISIBLE);
		toys_lease_main_all = (RelativeLayout) findViewById(R.id.toys_lease_main_all);
		mPullDownView = (PullDownView) findViewById(R.id.lease_main_list);
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.setShowHeader();// 显示并且可以使用头部刷新
		mPullDownView.setHideFooter();
	}

	@Override
	protected void initData() {

		DisplayMetrics am = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(am);
		
		// 无搜索结果提示图
		LayoutParams paramsImg = toys_search_no_hint_img.getLayoutParams();
		paramsImg.width = (int) (am.widthPixels * 0.5);
		paramsImg.height = (int) (am.widthPixels * 0.5);
		toys_search_no_hint_img.setLayoutParams(paramsImg);
		
		// 防止触碰屏幕时，页面再次加载
		ScrollOverListView.canRefleash = false;

		// 初始化页面组件
		ab = AbHttpUtil.getInstance(SearchResultActivity.this);
		inflater = LayoutInflater.from(SearchResultActivity.this);
		listview = mPullDownView.getListView();
		listview.setDivider(null);
		waitAdapter = new ToysLeaseMainAllAdapter(SearchResultActivity.this, inflater, MyApplication.getInstance(), data, 0, ab, (int) ((int) am.widthPixels * 0.45));
		listview.setAdapter(waitAdapter);
		mPullDownView.enableAutoFetchMore(true, 1);// 设置可以自动获取更多 滑到最后一个自动获取

		search_main_ed.setText(getIntent().getStringExtra(CATEGORY_ID_KEY));
		search_main_ed.setSelection(search_main_ed.getText().toString().trim().length());
		initWaitData(false);
	}

	@Override
	protected void setListener() {
		
		search_main_ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {   
			public boolean onEditorAction(TextView v, int actionId,KeyEvent event)  {                            
				if (actionId==EditorInfo.IME_ACTION_SEND ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER))   
				{     
//					Toast.makeText(SearchResultActivity.this, "请稍后···", Toast.LENGTH_SHORT).show();
					initWaitData(false);
				}                 
				return false;             
			}         
		});  
		
		findViewById(R.id.title_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SearchResultActivity.this.finish();
			}
		});
		
		lease_main_right_text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Toast.makeText(SearchResultActivity.this, "请稍后···", Toast.LENGTH_SHORT).show();
				initWaitData(false);
			}
		});
		
		search_to_cart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ToysLeaseMainShoppingCartActivity.isShowBack = true;
				Intent intent = new Intent(SearchResultActivity.this, ToysLeaseMainShoppingCartActivity.class);
				startActivity(intent);
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
	private void initWaitData(final boolean isMore) {
		
		if(search_main_ed.getText().toString().trim().equals("")){
			Toast.makeText(SearchResultActivity.this, "请输入查询内容", Toast.LENGTH_SHORT).show();
			return;
		}
		
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(SearchResultActivity.this).uid);
		params.put("search_title", search_main_ed.getText().toString().trim());
		
		if (isMore && data.size() != 0) {
			params.put("post_create_time", data.get(data.size() - 1).getPostcreatetime());
		}
		
		ab.get(ServerMutualConfig.GET_SEARCH_LIST_DATA + "&" + params.toString(), new AbStringHttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (!isMore) {
							data.clear();
						}
						initMainList(content, isMore, false);
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

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/**
	 * 初始化列表
	 * */
	private void initMainList(String content, boolean isMore, boolean isSave) {
		try {
			if (!content.equals("") && new JSONObject(content).getBoolean("success")) {
				JSONObject json = new JSONObject(content);
				if (!isMore) {
					data.clear();
					mPullDownView.setShowFooter();
					if (json.getJSONArray("data").length() != 20) {
						Toast.makeText(SearchResultActivity.this, "已显示所有符合条件的玩具", 2).show();
						mPullDownView.setHideFooter();
					}else{
						mPullDownView.setShowFooter();
					}
				} else {
					if (json.getJSONArray("data").length() != 20) {
						Toast.makeText(SearchResultActivity.this, "已显示所有符合条件的玩具", 2).show();
						mPullDownView.setHideFooter();
					} else {
						mPullDownView.setShowFooter();
					}
				}
				
				
				for (int i = 0; i < json.getJSONArray("data").length(); i++) {
					ToysLeaseMainListBean item = new ToysLeaseMainListBean();
					data.add(item.fromJson(json.getJSONArray("data").getJSONObject(i)));
				}
				
				waitAdapter.notifyDataSetChanged();

				toys_search_no_hint_all.setVisibility(View.GONE);
				if(!isMore && data.size() == 0){
					toys_search_no_hint_all.setVisibility(View.VISIBLE);
				}
				
			}
			if (isSave && Config.isConn(this)) {
				initWaitData(false);
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
	}

}

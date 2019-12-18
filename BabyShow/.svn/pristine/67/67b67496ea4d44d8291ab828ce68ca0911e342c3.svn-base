package com.yyqq.code.main;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.adapter.MainInfoListAdapter;
import com.yyqq.commen.model.MainListItemBean;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * 
 * 个人中心 —> 我关注的群列表页面
 * 
 * @author lyl
 * 
 * */
public class MyFollowGroupListActivity extends Activity implements OnPullDownListener {

	public static final String USER_ID = "user_id";
	private Activity context;
	private AbHttpUtil ab;
	private PullDownView mPullDownView;
	private ListView listview;
	private MainInfoListAdapter hotAdapter;
	private ArrayList<MainListItemBean> hotData = new ArrayList<MainListItemBean>();
	private MyApplication app;
	private TextView post_title;
	private static int indexitem = 0;// 记录选中的图片位置
	private boolean indexitemfla;// 记录选中的图片位置
	private String uid = "";
	private TextView fride_title;

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		context = this;
		setContentView(R.layout.activity_group_my_follow);
		init();
	}

	private void init() {
		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		ab = AbHttpUtil.getInstance(context);
		app = (MyApplication) context.getApplication();
		post_title = (TextView) findViewById(R.id.post_title);
		fride_title = (TextView) findViewById(R.id.fride_title);
		if(getIntent().hasExtra("img_title")){
			post_title.setText(getIntent().getStringExtra("img_title"));
			post_title.setVisibility(View.VISIBLE);
		}
		if(getIntent().hasExtra(USER_ID)){
			fride_title.setText("TA关注的群");
			uid = getIntent().getStringExtra(USER_ID);
		}else{
			uid = "";
		}
		
		mPullDownView = (PullDownView) findViewById(R.id.list);
		mPullDownView.setOnPullDownListener(this);
		listview = mPullDownView.getListView();
		listview.setDivider(null);
		if (hotAdapter == null) {
			hotAdapter = new MainInfoListAdapter(MyFollowGroupListActivity.this, hotData, true);
		}
		listview.setAdapter(hotAdapter);
		// 设置可以自动获取更多 滑到最后一个自动获取 改成false将禁用自动获取更多
		mPullDownView.enableAutoFetchMore(true, 1);
		// 隐藏 并禁用尾部
		// mPullDownView.setHideFooter();
		// 显示并启用自动获取更多
		mPullDownView.setShowFooter();
		// 隐藏并且禁用头部刷新
		// mPullDownView.setHideHeader();
		// listview.addHeaderView(mGallery);
		// 显示并且可以使用头部刷新
		mPullDownView.setShowHeader();
		// mPullDownView.setPadding(0, 0, 0, 0);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		onRefresh();
	}

	@Override
	public void onRefresh() {
		Config.showProgressDialog(context, false, null);
		AbRequestParams params = new AbRequestParams();
		if(uid.isEmpty()){
			params.put("login_user_id", Config.getUser(context).uid);
		}else{
			params.put("login_user_id", uid);
		}
		ab.get(ServerMutualConfig.getIdolGroupList + "&" + params.toString(),
				new AbStringHttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							hotData.clear();
							JSONObject json = new JSONObject(content);
							
							if(json.getJSONArray("data").length() == 0 && hotData.size() == 0){
								if(uid.isEmpty()){
									Toast.makeText(context, "您还没有已关注的群", 3).show();
								}else{
									Toast.makeText(context, "TA还没有已关注的群", 3).show();
								}
								hotAdapter.notifyDataSetChanged();
								mPullDownView.setHideFooter();
								return;
							}else{
								mPullDownView.setShowFooter();
							}
							
							for (int i = 0; i < json.getJSONArray("data").length(); i++) {
								MainListItemBean item = new MainListItemBean();
								hotData.add(item.fromJson(json.getJSONArray("data").getJSONObject(i)));
							}
							hotAdapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish() {
						Config.dismissProgress();
						super.onFinish();
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}

					@Override
					public void sendSuccessMessage(int statusCode,
							String content) {
						super.sendSuccessMessage(statusCode, content);
						Config.dismissProgress();
						mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
						mPullDownView.notifyDidMore();
					}

				});
	}

	@Override
	public void onMore() {
		if (hotData.size() == 0) {
			mPullDownView.notifyDidMore();
			mPullDownView.RefreshComplete();
			return;
		}
		AbRequestParams params = new AbRequestParams();
		if(uid.isEmpty()){
			params.put("login_user_id", Config.getUser(context).uid);
		}else{
			params.put("login_user_id", uid);
		}
		params.put("post_create_time", hotData.get(hotData.size() - 1).getPost_create_time() + "");
		ab.get(ServerMutualConfig.getIdolGroupList + "&" + params.toString(),
				new AbStringHttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Config.dismissProgress();
						try {
							JSONObject json = new JSONObject(content);
							if(json.getJSONArray("data").length() == 0){
								Toast.makeText(context, "没有更多了", 3).show();
								mPullDownView.setHideFooter();
								return;
							}else{
								mPullDownView.setShowFooter();
							}
							
							for (int i = 0; i < json.getJSONArray("data").length(); i++) {
								MainListItemBean item = new MainListItemBean();
								hotData.add(item.fromJson(json.getJSONArray("data").getJSONObject(i)));
							}
							hotAdapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void sendSuccessMessage(int statusCode,
							String content) {
						super.sendSuccessMessage(statusCode, content);
						Config.dismissProgress();
						mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
						mPullDownView.notifyDidMore();
					}

				});
	}

}
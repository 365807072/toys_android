package com.yyqq.code.group;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.adapter.GroupManagerTopicAdapter;
import com.yyqq.commen.model.GroupManagerTopicBean;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * 
 * 微社区话题or帖子管理
 * 
 * @author lyl
 * 
 * */

public class GroupManagerTopicActivity extends BaseActivity implements OnPullDownListener{

	private String group_id = "";
	private AbHttpUtil ab;
	private LayoutInflater inflater;
	private GroupManagerTopicAdapter groupManagerTopicAdapter;
	private ArrayList<GroupManagerTopicBean> groupManagerTopicBeanList = new ArrayList<GroupManagerTopicBean>();
	
	private PullDownView mPullDownView;
	private ListView listView;
	private ImageView title_cancel;
	private LinearLayout group_topic_edit;
	private ListView group_topic_edit_list;
	private LinearLayout group_topic_edit_essence;
	private ListView group_topic_edit_list_essence;
	private TextView group_topic_edit_list_send;
	private TextView group_topic_edit_list_cancel;
	private TextView group_topic_edit_list_essence_send;
	private TextView group_topic_edit_list_essence_cancel;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_manager_topic);
	}

	@Override
	protected void initView() {
		mPullDownView = (PullDownView) findViewById(R.id.list);
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.setShowHeader();// 显示并且可以使用头部刷新
		listView = mPullDownView.getListView();
		listView.setDivider(null);
		title_cancel = (ImageView) findViewById(R.id.title_cancel);
		group_topic_edit = (LinearLayout) findViewById(R.id.group_topic_edit);
		group_topic_edit_list = (ListView) findViewById(R.id.group_topic_edit_list);
		group_topic_edit_essence = (LinearLayout) findViewById(R.id.group_topic_edit_essence);
		group_topic_edit_list_essence = (ListView) findViewById(R.id.group_topic_edit_list_essence);
		group_topic_edit_list_send = (TextView) findViewById(R.id.group_topic_edit_list_send);
		group_topic_edit_list_cancel = (TextView) findViewById(R.id.group_topic_edit_list_cancel);
		group_topic_edit_list_essence_send = (TextView) findViewById(R.id.group_topic_edit_list_essence_send);
		group_topic_edit_list_essence_cancel = (TextView) findViewById(R.id.group_topic_edit_list_essence_cancel);
	}

	@Override
	protected void initData() {
		group_id = getIntent().getStringExtra(SuperGroupActivity.GROUP_ID);
		ab = AbHttpUtil.getInstance(GroupManagerTopicActivity.this);
		inflater = LayoutInflater.from(this);
		
		if(null == groupManagerTopicAdapter){
			groupManagerTopicAdapter = new GroupManagerTopicAdapter(group_id, GroupManagerTopicActivity.this, inflater, groupManagerTopicBeanList, ab, group_topic_edit_list, group_topic_edit, group_topic_edit_list_essence, group_topic_edit_essence);
		}
		listView.setAdapter(groupManagerTopicAdapter);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		onRefresh();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void setListener() {
		
		group_topic_edit_list_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				editCategory();
			}
		});
		
		group_topic_edit_list_essence_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				editEssence();
			}
		});
		
		group_topic_edit_list_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				group_topic_edit.setVisibility(View.GONE);
			}
		});
		
		group_topic_edit_list_essence_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				group_topic_edit_essence.setVisibility(View.GONE);
			}
		});
		
		title_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GroupManagerTopicActivity.this.finish();
			}
		});
		
		group_topic_edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(group_topic_edit.isShown()){
					group_topic_edit.setVisibility(View.GONE);
				}else{
					group_topic_edit.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	public void onRefresh() {
		groupManagerTopicBeanList.clear();
		initGroupManagerInfo();
	}

	@Override
	public void onMore() {
		initGroupManagerInfo();
	}
	
	/**
	 * 获取帖子列表
	 * */
	private void initGroupManagerInfo(){
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(this).uid);
		params.put("group_id", group_id);
		if(groupManagerTopicBeanList.size() != 0){
			params.put("post_create_time", groupManagerTopicBeanList.get(groupManagerTopicBeanList.size()-1).getPost_create_time());
		}
		ab.get(ServerMutualConfig.getGroupManage + "&" + params.toString(), new AbStringHttpResponseListener() {
			
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (!content.isEmpty()) {
							try {
								JSONObject json = new JSONObject(content);
								for (int i = 0; i < json.getJSONArray("data").length(); i++) {
									GroupManagerTopicBean item = new GroupManagerTopicBean();
									groupManagerTopicBeanList.add(item.fromJson(json.getJSONArray("data").getJSONObject(i)));
								}
								groupManagerTopicAdapter.notifyDataSetChanged();
							} catch (JSONException e) {
								e.printStackTrace();
							}
						} else {
							Config.dismissProgress();
							Toast.makeText(GroupManagerTopicActivity.this, "没有更多了", 3).show();
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
	 * 修改帖子分类
	 * */
	private void editCategory(){
		Config.showProgressDialog(GroupManagerTopicActivity.this, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(this).uid);
		params.put("img_id", groupManagerTopicBeanList.get(GroupManagerTopicAdapter.index).getImg_id());
		
		String group_class_idsStr = "";
		for(int i = 0 ; i < groupManagerTopicBeanList.get(GroupManagerTopicAdapter.index).getCategoryList().size() ; i++){
			if(groupManagerTopicBeanList.get(GroupManagerTopicAdapter.index).getCategoryList().get(i).getGroup_class_state().equals("1")){
				group_class_idsStr += groupManagerTopicBeanList.get(GroupManagerTopicAdapter.index).getCategoryList().get(i).getGroup_class_id() + ",";
			}
		}
		params.put("group_class_ids", group_class_idsStr.substring(0, group_class_idsStr.length() - 1));
		ab.post(ServerMutualConfig.editCategoryListing, params, new AbStringHttpResponseListener() {
			
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Config.dismissProgress();
						if (!content.isEmpty()) {
							try {
								onRefresh();
								JSONObject json = new JSONObject(content);
								group_topic_edit.setVisibility(View.GONE);
								Toast.makeText(GroupManagerTopicActivity.this,json.getString("reMsg"),Toast.LENGTH_SHORT).show();
							} catch (JSONException e) {
								e.printStackTrace();
							}
						} 
					}

					@Override
					public void onFinish() {
						super.onFinish();
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
	 * 修改精华设置
	 * */
	private void editEssence(){
		Config.showProgressDialog(GroupManagerTopicActivity.this, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(this).uid);
		params.put("group_id", group_id);
		params.put("img_id", groupManagerTopicBeanList.get(GroupManagerTopicAdapter.index).getImg_id());
		
		for(int i = 0 ; i < groupManagerTopicBeanList.get(GroupManagerTopicAdapter.index).getEditList().size() ; i++){
			if(groupManagerTopicBeanList.get(GroupManagerTopicAdapter.index).getEditList().get(i).getEssence_state().equals("1")){
				params.put("is_cancle", groupManagerTopicBeanList.get(GroupManagerTopicAdapter.index).getEditList().get(i).getEssence_id());
			}
		}
		ab.post(ServerMutualConfig.editEssenceListing, params, new AbStringHttpResponseListener() {
			
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Config.dismissProgress();
						if (!content.isEmpty()) {
							try {
								onRefresh();
								JSONObject json = new JSONObject(content);
								group_topic_edit_essence.setVisibility(View.GONE);
								Toast.makeText(GroupManagerTopicActivity.this,json.getString("reMsg"),Toast.LENGTH_SHORT).show();
							} catch (JSONException e) {
								e.printStackTrace();
							}
						} 
					}

					@Override
					public void onFinish() {
						super.onFinish();
						Config.dismissProgress();
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}
				});
	}
	
}

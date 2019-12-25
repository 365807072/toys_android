package com.yyqq.code.group;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.adapter.GroupManagerCategoryAdapter;
import com.yyqq.commen.utils.Config;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * 
 * 微社区分类管理
 * 
 * @author lyl
 * 
 * */
public class GroupManagerCategoryActivity extends BaseActivity {
	
	private ImageView title_cancel;
	private ListView group_category_list;
	private TextView group_category_add;
	
	private String group_id = "";
	private AbHttpUtil ab;
	private LayoutInflater inflater;
	private GroupManagerCategoryAdapter cateGroryAdapter;
	private ArrayList<String> cateGoryList = new ArrayList<String>();
	private ArrayList<String> cateGoryIdList = new ArrayList<String>();

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_category_manager);
	}

	@Override
	protected void initView() {
		title_cancel = (ImageView) findViewById(R.id.title_cancel);
		group_category_list = (ListView) findViewById(R.id.group_category_list);
		group_category_add = (TextView) findViewById(R.id.group_category_add);
	}

	@Override
	protected void initData() {
		ab = AbHttpUtil.getInstance(this);
		inflater = LayoutInflater.from(this);
		group_id = getIntent().getStringExtra(SuperGroupActivity.GROUP_ID);
		
		if(null == cateGroryAdapter){
			cateGroryAdapter = new GroupManagerCategoryAdapter(group_id, GroupManagerCategoryActivity.this, inflater, cateGoryList, cateGoryIdList, ab);
		}
		group_category_list.setAdapter(cateGroryAdapter);
		group_category_list.setDivider(null);
		
		initCategoryList();
	}

	@Override
	protected void setListener() {
		title_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GroupManagerCategoryActivity.this.finish();
			}
		});
		
		group_category_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showAddView();
			}
		});
		
	}
	
	private void showAddView(){
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("请输入新分类名称");
		final EditText edit_name = new EditText(this);
		edit_name.setSingleLine(true);
		edit_name.setMaxEms(10);
		edit_name.setImeOptions(EditorInfo.IME_ACTION_SEND);
		builder.setView(edit_name);
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(edit_name.getText().toString().trim().length() > 10){
					Toast.makeText(GroupManagerCategoryActivity.this, "分类名称过长，最多10个字",Toast.LENGTH_SHORT).show();
					return;
				}
				arg0.dismiss();
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit_name.getWindowToken(), 0);
				addCategory(edit_name.getText().toString().trim());
			}
		});
		
		builder.setNeutralButton("取消",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit_name.getWindowToken(), 0);
			}
		});
		
		final AlertDialog dialog = builder.create();
		dialog.show();
		
		edit_name.setOnEditorActionListener(new OnEditorActionListener(){

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				dialog.dismiss();
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit_name.getWindowToken(), 0);
				addCategory(edit_name.getText().toString().trim());
				return false;
			}
		});
		
		edit_name.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
		            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		       }
			}
		});
	}
	
	/**
	 * 添加分类
	 * */
	private void addCategory(String categoryStr){
		Config.showProgressDialog(GroupManagerCategoryActivity.this, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(GroupManagerCategoryActivity.this).uid);
		params.put("group_id", group_id);
		params.put("title", categoryStr);
		ab.post(ServerMutualConfig.editGroupCategory, params, new AbStringHttpResponseListener() {
			
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Config.dismissProgress();
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								initCategoryList();
								Toast.makeText(GroupManagerCategoryActivity.this,json.getString("reMsg"),Toast.LENGTH_SHORT).show();
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
		Config.showProgressDialog(GroupManagerCategoryActivity.this, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(this).uid);
		params.put("group_id", group_id);
		ab.get(ServerMutualConfig.getGroupCategory + "&" + params.toString(), new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode,
					String content) {
				super.onSuccess(statusCode, content);
				Config.dismissProgress();
				cateGoryList.clear();
				cateGoryIdList.clear();
				try {
					JSONObject json = new JSONObject(content);
					for(int i = 0 ; i < json.getJSONArray("data").length() ; i++){
						cateGoryList.add(json.getJSONArray("data").getJSONObject(i).getString("title"));
						cateGoryIdList.add(json.getJSONArray("data").getJSONObject(i).getString("group_class_id"));
					}
					cateGroryAdapter.notifyDataSetChanged();
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
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
}

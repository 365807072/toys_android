package com.yyqq.code.toyslease.version_93;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.yyqq.babyshow.R;
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.code.toyslease.version_92.ToysLeaseMainShoppingCartActivity;
import com.yyqq.commen.adapter.SearchHint02Apapter;
import com.yyqq.commen.adapter.SearchHintApapter;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * 商品搜索页
 * */
@SuppressLint("NewApi")
public class SearchMainActivity extends BaseActivity implements OnPullDownListener{

	private Activity context;
	private TextView lease_main_right;
	private EditText search_main_ed;
	private GridView search_main_hint;
	private ListView search_context_list;
	private ImageView search_main_delete;
	private TextView search_main_hot_hint;
	private TextView search_main_own_hint;
	private RelativeLayout search_hint_all;
	private ImageView search_to_cart;
	private RelativeLayout search_main_hot_hint_all;
	
	private AbHttpUtil ab;
	private MyApplication app;
	private SharedPreferences baocun;
	private String key="context";
	private ArrayList<String> hotText;
	private ArrayList<String> historyText;
	private boolean UPDATESEARCH = false;
	
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		context = this;
		setContentView(R.layout.activity_toys_lease_search);
	}
	
	@Override
	protected void initView() {
		search_main_hot_hint_all = (RelativeLayout) findViewById(R.id.search_main_hot_hint_all);
		search_to_cart = (ImageView) findViewById(R.id.search_to_cart);
		search_main_own_hint = (TextView) findViewById(R.id.search_main_own_hint);
		search_context_list = (ListView) this.findViewById(R.id.search_context_list);
		search_main_hot_hint = (TextView) findViewById(R.id.search_main_hot_hint);
		search_hint_all = (RelativeLayout) findViewById(R.id.search_hint_all);
		search_main_delete = (ImageView) findViewById(R.id.toys_search_main_delete);
		search_main_hint = (GridView) findViewById(R.id.search_main_hint);
		search_main_ed = (EditText) findViewById(R.id.search_main_ed);
		lease_main_right = (TextView) findViewById(R.id.lease_main_right);
	}

	@Override
	protected void initData() {
		app = (MyApplication) context.getApplication();
		ab = AbHttpUtil.getInstance(context);
		UPDATESEARCH = false;
		
		getSearchHint();
	}

	/**
	 * 搜索的TextView的添加事件
	 * */
	@Override
	protected void setListener() {
		
		search_main_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder dialog = new Builder(SearchMainActivity.this);
				dialog.setTitle("确认提示");
				dialog.setMessage("确定要清空记录吗？");
				dialog.setNegativeButton("取消", new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
					
				});
				dialog.setPositiveButton("确定", new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						deleteHint();
					}
				});
				dialog.create().show();
				
			}
		});
		
		search_to_cart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToysLeaseMainShoppingCartActivity.isShowBack = true;
				Intent in = new Intent(SearchMainActivity.this, ToysLeaseMainShoppingCartActivity.class);
				startActivity(in);
			}
		});
		
		search_main_ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {   
			public boolean onEditorAction(TextView v, int actionId,KeyEvent event)  {                            
				if (actionId==EditorInfo.IME_ACTION_SEND ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER))   
				{                  
					saveSearText();
				}                 
				return false;             
			}         
		});  
		
		findViewById(R.id.title_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SearchMainActivity.this.finish();
			}
		});
		
		search_main_hint.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				search_main_ed.setText(historyText.get(position));
				searchBusiness();
			}
		});
		
		search_context_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				search_main_ed.setText(hotText.get(position));
				searchBusiness();
			}
		});
		
		
		lease_main_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveSearText();
			}
		});
	}

	@Override
	public void onRefresh() {
		searchBusiness();
	}

	@Override
	public void onMore() {
		searchBusiness();
	}
	
	/**
	 * 获取搜索推荐
	 * */
	@SuppressLint("NewApi")
	private void getSearchHint(){
		
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(SearchMainActivity.this).uid);
		
		ab.get(ServerMutualConfig.GET_SEARCH_LIST + "&" + params.toString() , new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				if (!content.isEmpty()) {
					try {
						if(!content.isEmpty() && new JSONObject(content).getBoolean("success")){
							hotText = new ArrayList<String>();
							historyText = new ArrayList<String>();
							
							JSONObject json = new JSONObject(content);
							search_main_own_hint.setText(json.getJSONObject("data").getJSONObject("hot").getString("search_title_name"));
							search_main_hot_hint.setText(json.getJSONObject("data").getJSONObject("own").getString("search_title_name"));
							
							for(int i = 0 ; i < json.getJSONObject("data").getJSONObject("hot").getJSONArray("search_info").length(); i++){
								hotText.add(json.getJSONObject("data").getJSONObject("hot").getJSONArray("search_info").getJSONObject(i).getString("search_title"));
							}
							
							for(int i = 0 ; i < json.getJSONObject("data").getJSONObject("own").getJSONArray("search_info").length(); i++){
								historyText.add(json.getJSONObject("data").getJSONObject("own").getJSONArray("search_info").getJSONObject(i).getString("search_title"));
							}
							

							// 初始化历史记录
							if(historyText.size() != 0){
								SearchHintApapter hintAdapter = new SearchHintApapter(SearchMainActivity.this, historyText);
								search_main_hint.setAdapter(hintAdapter);
								search_main_hint.setVisibility(View.VISIBLE);
								search_main_hot_hint.setVisibility(View.VISIBLE);
								search_main_hot_hint_all.setVisibility(View.VISIBLE);
								findViewById(R.id.search_hint_line).setVisibility(View.VISIBLE);
							}else{
								search_main_hint.setVisibility(View.GONE);
								search_main_hot_hint.setVisibility(View.GONE);
								search_main_hot_hint_all.setVisibility(View.GONE);
								findViewById(R.id.search_hint_line).setVisibility(View.GONE);
							}
							
							// 初始化热门搜索
							if(hotText.size() != 0){
								SearchHint02Apapter hint02Adapter = new SearchHint02Apapter(SearchMainActivity.this, hotText);
								search_context_list.setAdapter(hint02Adapter);
								search_context_list.setVisibility(View.VISIBLE);
								search_main_own_hint.setVisibility(View.VISIBLE);
								search_hint_all.setVisibility(View.VISIBLE);
							}else{
								search_hint_all.setVisibility(View.GONE);
							}
							
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
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
	 * 获取搜索推荐
	 * */
	@SuppressLint("NewApi")
	private void deleteHint(){
		
		final AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(SearchMainActivity.this).uid);
		
		ab.get(ServerMutualConfig.DELETE_SEARCH_LIST_DATA + "&" + params.toString() , new AbStringHttpResponseListener() {
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				if (!content.isEmpty()) {
					try {
						if(!content.isEmpty() && new JSONObject(content).getBoolean("success")){
							getSearchHint();
						}else{
							Toast.makeText(context, "清空失败", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
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
	 * 搜索数据
	 * */
	@SuppressLint("NewApi")
	private void searchBusiness(){
		saveSearText();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		getSearchHint();
	}

	/**
	 * 保存搜索数据
	 * */
	private void saveSearText(){
		
		if(search_main_ed.getText().toString().trim().isEmpty()){
			Toast.makeText(context, "请输入查询内容", Toast.LENGTH_SHORT).show();
			return;
		}
		
		search_main_ed.setSelection(search_main_ed.getText().toString().trim().length());
		Intent in = new Intent(SearchMainActivity.this, SearchResultActivity.class);
		in.putExtra(SearchResultActivity.CATEGORY_ID_KEY, search_main_ed.getText().toString().trim());
		startActivity(in);
	}
}

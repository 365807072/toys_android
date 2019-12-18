package com.yyqq.code.grow;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.commen.model.SearchItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GrowSearch extends Activity implements OnPullDownListener {

	private Activity context;
	private String TAG = "fanfan_GrowSearch";
	private AbHttpUtil ab;
	private EditText search;
	private ImageView search_btn, search_cancle;
	private MyApplication myMyApplication;
	private PullDownView mPullDownView;
	private ListView listview;
	private MyAdapter adapter;
	public static String SearchWord = "";
	private ArrayList<SearchItem> data = new ArrayList<SearchItem>();
	private JSONArray data_json;
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.grow_search);
		myMyApplication = (MyApplication) context.getApplication();
		ab = AbHttpUtil.getInstance(context);
		init();
	}

	private void init() {
		mPullDownView = (PullDownView) findViewById(R.id.list);
		mPullDownView.setOnPullDownListener(this);
		listview = mPullDownView.getListView();
		listview.setDivider(null);
		if (adapter == null) {
			adapter = new MyAdapter();
		}

		listview.setAdapter(adapter);
		// 设置可以自动获取更多 滑到最后一个自动获取 改成false将禁用自动获取更多
		// mPullDownView.enableAutoFetchMore(true, 1);
		// 隐藏 并禁用尾部
		mPullDownView.setHideFooter();
		// 显示并启用自动获取更多
		// mPullDownView.setShowFooter();
		// 隐藏并且禁用头部刷新
		mPullDownView.setHideHeader();
		// listview.addHeaderView(mGallery);
		// 显示并且可以使用头部刷新
		// mPullDownView.setShowHeader();
		search_btn = (ImageView) findViewById(R.id.search_btn);
		search_btn.setOnClickListener(searchClick);
		search_cancle = (ImageView) findViewById(R.id.search_cancle);
		search_cancle.setOnClickListener(searchCancleClick);
		search = (EditText) findViewById(R.id.search);
		search.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				if (search.length() < 1) {
					search_cancle.setVisibility(View.VISIBLE);
					search_btn.setVisibility(View.GONE);
				} else {
					search_cancle.setVisibility(View.GONE);
					search_btn.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				if (search.length() < 1) {
					search_cancle.setVisibility(View.VISIBLE);
					search_btn.setVisibility(View.GONE);
				} else {
					search_cancle.setVisibility(View.GONE);
					search_btn.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (search.length() < 1) {
					search_cancle.setVisibility(View.VISIBLE);
					search_btn.setVisibility(View.GONE);
				} else {
					search_cancle.setVisibility(View.GONE);
					search_btn.setVisibility(View.VISIBLE);
				}
			}

		});
	}

	@Override
	public void onRefresh() {
		Config.showProgressDialog(context, true, null);
		AbRequestParams params = new AbRequestParams();
		SearchWord = search.getText().toString().trim();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("search_word", SearchWord);
		data.clear();
//		Log.e(TAG,
//				ServerMutualConfig.SearchKindergartenUser + "&"
//						+ params.toString());
		ab.get(ServerMutualConfig.SearchKindergartenUser + "&"
				+ params.toString(), new AbStringHttpResponseListener() {
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					JSONObject json = new JSONObject(content);
					if (json.getBoolean("success")) {
						data_json = new JSONArray();
						for (int i = 0; i < json.getJSONArray("data").length(); i++) {
							SearchItem item = new SearchItem();
							item.fromJson(json.getJSONArray("data")
									.getJSONObject(i));
							data_json.put(json.getJSONArray("data")
									.getJSONObject(i));
							data.add(item);
						}
						adapter.notifyDataSetChanged();
					} else {
						Toast.makeText(context, json.getString("reMsg"),
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
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
				Config.showFiledToast(context);
			}
		});
	}

	@Override
	public void onMore() {
		// TODO Auto-generated method stub

	}

	public OnClickListener searchClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			onRefresh();
		}
	};

	public OnClickListener searchCancleClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			search.setText("");
			InputMethodManager inputMethodManager = (InputMethodManager) context
					.getApplicationContext().getSystemService(
							Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(search.getWindowToken(),
					0);
		}
	};

	class ViewHolder {
		TextView userName;
		TextView babyName;
		ImageView attention;
		ImageView head;
	}

	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int arg0) {
			return data.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int index, View convertView, ViewGroup arg2) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.grow_search_item, null);
			ViewHolder holder = null;
			holder = new ViewHolder();
			holder.userName = (TextView) convertView
					.findViewById(R.id.userName);
			holder.babyName = (TextView) convertView
					.findViewById(R.id.babyName);
			holder.attention = (ImageView) convertView
					.findViewById(R.id.attention);
			holder.head = (ImageView) convertView.findViewById(R.id.head);
			final SearchItem item = data.get(index);
			holder.userName.setText(item.nick_name);
			holder.babyName.setText(item.baby_name);
			MyApplication.getInstance().display(item.avatar, holder.head,
					R.drawable.def_head);
			if ("1".equals(item.idol_type)) {
				holder.attention.setVisibility(View.GONE);
			} else {
				holder.attention.setVisibility(View.VISIBLE);
			}
			holder.attention.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(context, GrowAttention.class);
					intent.putExtra("idol_user_id", item.user_id);
					intent.putExtra("baby_id", item.baby_id);
					startActivity(intent);
				}
			});
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			return convertView;
		}

	}

}

package com.yyqq.code.main;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.view.listener.AbOnListViewListener;
import com.ab.view.pullview.AbPullGridView;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.code.photo.ImageDetail;
import com.yyqq.commen.model.SearchItem;
import com.yyqq.commen.model.SpecialDetailGridItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.ImageDownloader;
import com.yyqq.commen.utils.Log;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;

public class SpecialDetailGrid extends Activity implements AbOnListViewListener {
	private String TAG = "fanfan_SpecialDetailGrid";
	private Activity context;
	private AbPullGridView mAbPullGridView = null;
	private GridView mGridView = null;
	private ArrayList<SpecialDetailGridItem> data = new ArrayList<SpecialDetailGridItem>();
	private MyAdapter adapter;
	private int windowWidth;
	private MyApplication myMyApplication;
	private String cate_id, cateName;
	private ImageView listBt, gridBt;
	private AbHttpUtil ab;
	private TextView tt, visit;
	private int pageSize;

	/**
	 * 搜索
	 */
	private EditText search;
	private ImageView search_btn, search_cancle;
	private TextView find;
	public static String SearchWord = "";
	private ArrayList<SearchItem> searchData = new ArrayList<SearchItem>();
	private JSONArray data_json;
	private RelativeLayout title, ly_title;
	private LinearLayout search_root;
	private int sss; // s=0,表示搜索布局在
	private int isSearch = 0;
	private String userId = "";
	private String isPic = "";

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (search_root.getVisibility() == View.VISIBLE) {
				title.setVisibility(View.VISIBLE);
				search_root.setVisibility(View.GONE);
				return true;
			} else {
				this.finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		onRefresh();
	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		}

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		context = this;
		myMyApplication = (MyApplication) context.getApplication();
		setContentView(R.layout.special_grid);
		cate_id = getIntent().getStringExtra("cate_id");
		listBt = (ImageView) findViewById(R.id.listBt);
		listBt.setOnClickListener(listClick);
		gridBt = (ImageView) findViewById(R.id.gridBt);
		gridBt.setOnClickListener(gridClick);
		tt = (TextView) findViewById(R.id.tt);
		visit = (TextView) findViewById(R.id.visit);
		visit.setOnClickListener(visitClick);
		ly_title = (RelativeLayout) findViewById(R.id.ly_title);
		search_root = (LinearLayout) findViewById(R.id.search_root);
		title = (RelativeLayout) findViewById(R.id.title);
		find = (TextView) findViewById(R.id.find);
		find.setOnClickListener(findClick);
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
		DisplayMetrics DM = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(DM);
		windowWidth = DM.widthPixels;
		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
		mAbPullGridView = (AbPullGridView) findViewById(R.id.mPhotoGridView);
		// 开关默认打开
		mAbPullGridView.setPullRefreshEnable(true);
		mAbPullGridView.setPullLoadEnable(true);
		// 设置进度条的样式
		mAbPullGridView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullGridView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mGridView = mAbPullGridView.getGridView();
		mGridView.setNumColumns(3);
		adapter = new MyAdapter();
		mAbPullGridView.setAdapter(adapter);
		// 设置两种查询的事件
		mAbPullGridView.setAbOnListViewListener(this);
		onRefresh();
	}

	public OnClickListener visitClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if ("1".equals(myMyApplication.getVisitor())) {
				myMyApplication.setVisitor("");
				Intent intent = new Intent();
				intent.setClass(context, WebLoginActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent();
				intent.setClass(context, AddNewPostActivity.class);
				intent.putExtra("cate_id", cate_id);
				startActivity(intent);
			}

		}
	};
	public OnClickListener listClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, SpecialDetailList.class);
			intent.putExtra("cate_id", cate_id);
			startActivity(intent);
			context.overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			context.finish();
		}
	};
	public OnClickListener gridClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, SpecialDetailGrid.class);
			intent.putExtra("cate_id", cate_id);
			// intent.putExtra("cate_name", cate_name);
			startActivity(intent);
			context.overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			context.finish();
		}
	};

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onRefresh() {
		pageSize = 0;
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("cate_id", cate_id);
		params.put("page", pageSize + "");
		if (0 != isSearch) {
			params.put("user_id", userId);
			params.put("is_pic", isPic);
		}
		// android.util.Log.e("fanfan", ServerMutualConfig.SpecialDetailGridV2
		// + "&" + params.toString());
		ab.get(ServerMutualConfig.SpecialDetailGridV2 + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						mAbPullGridView.stopRefresh();
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								data.clear();
								for (int i = 0; i < json.getJSONArray("data")
										.length(); i++) {
									SpecialDetailGridItem item = new SpecialDetailGridItem();
									item.fromJson(json.getJSONArray("data")
											.getJSONObject(i), content);
									data.add(item);
								}
								tt.setText(data.get(0).cate_name);
								cateName = data.get(0).cate_name;
								adapter.notifyDataSetChanged();
								isSearch = 0;
							} else {
								Toast.makeText(context,
										json.getString("reMsg"),
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
					}
				});
	}

	@Override
	public void onLoadMore() {
		if (data.size() == 0) {
			mAbPullGridView.stopLoadMore();
			return;
		}
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("cate_id", cate_id);
		params.put("page", (++pageSize) + "");
		ab.get(ServerMutualConfig.SpecialDetailGridV2 + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								for (int i = 0; i < json.getJSONArray("data")
										.length(); i++) {
									SpecialDetailGridItem item = new SpecialDetailGridItem();
									item.fromJson(json.getJSONArray("data")
											.getJSONObject(i), content);
									data.add(item);
								}
								adapter.notifyDataSetChanged();
								mAbPullGridView.stopLoadMore();
							} else {
								Toast.makeText(context,
										json.getString("reMsg"),
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
					}
				});
	}

	@SuppressLint("ResourceAsColor")
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
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ViewHolder holder = null;
			convertView = inflater.inflate(R.layout.special_detail_grid_item,
					null);
			holder = new ViewHolder();
			final SpecialDetailGridItem item = data.get(position);
			holder.img = (ImageView) convertView.findViewById(R.id.photo);
			holder.num = (TextView) convertView.findViewById(R.id.num);
			// 计算图片尺寸
			int hi = (windowWidth - Config.getSize("20")) / 3;
			holder.img.getLayoutParams().height = hi;
			holder.img.getLayoutParams().width = hi;
			holder.img.setScaleType(ScaleType.CENTER_CROP);
			MyApplication.getInstance().display(item.listImg.get(0).img_thumb,
					holder.img, R.drawable.def_image);
			holder.num.setText(item.rsort);
			convertView.setTag(holder);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(context, MainItemDetialActivity.class);
					intent.putExtra("img_id", item.img_id);
					intent.putExtra("cate_id", cate_id);
					intent.putExtra("cate_name", cateName);
					intent.putExtra("rsort", item.rsort);
					startActivity(intent);
				}
			});
			return convertView;
		}

	}

	class ViewHolder {
		TextView num;
		ImageView img;
	}

	/**
	 * 搜索
	 */
	public OnClickListener findClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			sss = 0;
			search_root.setVisibility(View.VISIBLE);
			title.setVisibility(View.GONE);
		}

	};

	public OnClickListener searchClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Config.showProgressDialog(context, false, null);
			AbRequestParams params = new AbRequestParams();
			SearchWord = search.getText().toString().trim();
			params.put("login_user_id", Config.getUser(context).uid);
			params.put("search_word", SearchWord);
			params.put("cate_id", cate_id);
			searchData.clear();
			// android.util.Log.e("fanfan", ServerMutualConfig.FindSpecialUser
			// + "&" + params.toString());
			ab.get(ServerMutualConfig.FindSpecialUser + "&" + params.toString(),
					new AbStringHttpResponseListener() {
						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							try {
								JSONObject json = new JSONObject(content);
								if (json.getBoolean("success")) {
									data_json = new JSONArray();
									if (json.getJSONArray("data").length() == 0) {
										Toast.makeText(context,
												"对不起哦，没有找到你想要的耶",
												Toast.LENGTH_SHORT).show();
									} else {
										for (int i = 0; i < json.getJSONArray(
												"data").length(); i++) {
											SearchItem item = new SearchItem();
											item.fromJson(json.getJSONArray(
													"data").getJSONObject(i));
											data_json.put(json.getJSONArray(
													"data").getJSONObject(i));
											searchData.add(item);
										}
										new PopupWindows(context, ly_title);
										InputMethodManager inputMethodManager = (InputMethodManager) context
												.getApplicationContext()
												.getSystemService(
														Context.INPUT_METHOD_SERVICE);
										inputMethodManager.hideSoftInputFromWindow(
												search.getWindowToken(), 0);
										search.setText("");
										search_btn.setVisibility(View.GONE);
										search_cancle
												.setVisibility(View.VISIBLE);
									}
								} else {
									Toast.makeText(context,
											json.getString("reMsg"),
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
	};

	public OnClickListener searchCancleClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			search.setText("");
			search_root.setVisibility(View.GONE);
			title.setVisibility(View.VISIBLE);
			InputMethodManager inputMethodManager = (InputMethodManager) context
					.getApplicationContext().getSystemService(
							Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(search.getWindowToken(),
					0);
		}
	};

	ListView popList;
	ImageDownloader mDownloader;

	public class PopupWindows extends PopupWindow {
		public PopupWindows(Context mContext, View parent) {
			View view = View.inflate(mContext, R.layout.plaza_popupwindows,
					null);
			popList = (ListView) view.findViewById(R.id.popList);
			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAsDropDown(parent);
			popList.setAdapter(new PopupAdapter());
			popList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					search.setText("");
					isSearch = 1;
					dismiss();
					onRefresh();
				}
			});
			update();
		}

		@Override
		public void dismiss() {
			super.dismiss();
		}

	}

	class PopupAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return searchData.size();
		}

		@Override
		public Object getItem(int position) {
			return searchData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			ViewHolder1 holder1 = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.search_listview_item,
						null);
				holder1 = new ViewHolder1();
				holder1.img = (ImageView) convertView.findViewById(R.id.head);
				holder1.name = (TextView) convertView.findViewById(R.id.name);
				convertView.setTag(holder1);

			} else {
				holder1 = (ViewHolder1) convertView.getTag();
			}
			holder1.name.setText(searchData.get(position).nick_name);
			holder1.img.setTag(searchData.get(position).avatar);
			userId = searchData.get(position).id;
			isPic = searchData.get(position).is_pic;
			// 头像
			MyApplication.getInstance().display(
					searchData.get(position).avatar, holder1.img,
					R.drawable.def_head);

			return convertView;
		}

	}

	class ViewHolder1 {
		ImageView img;
		TextView name;
	}

}

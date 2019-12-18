package com.yyqq.code.postbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.yyqq.code.main.AddNewPostActivity;
import com.yyqq.code.main.MainItemDetialActivity;
import com.yyqq.commen.adapter.MainInfoListAdapter;
import com.yyqq.commen.model.MainListItemBean;
import com.yyqq.commen.model.SearchItem;
import com.yyqq.commen.model.ShaiXuanItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.GroupRelevantUtils;
import com.yyqq.commen.utils.ImageDownloader;
import com.yyqq.commen.utils.OnImageDownload;
import com.yyqq.commen.utils.VideoComperssUtils;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class PostBarActivity extends Activity implements OnPullDownListener {

	private Activity context;
	private String TAG = "fanfan_PostBarActivity";
	private AbHttpUtil ab;
	private PullDownView mPullDownView;
	private ListView listview;
	private MainInfoListAdapter hotAdapter;
	private ArrayList<MainListItemBean> hotData = new ArrayList<MainListItemBean>();
	private TextView send_post;
	private TextView post_title;
	private MyApplication app;
	private int width;
	private String fileName = "PostBarActivity.txt";
	private MyApplication myMyApplication;
	private ArrayList<ShaiXuanItem> data = new ArrayList<ShaiXuanItem>();
	/**
	 * 搜索
	 */
	private EditText search;
	private ImageView search_btn, search_cancle, find;
	public static String SearchWord = "";
	private ArrayList<SearchItem> SearchData = new ArrayList<SearchItem>();
	private JSONArray data_json;
	private RelativeLayout title, ly_title;
	private LinearLayout search_root;
	private int sss; // s=0,表示搜索布局在

	/** 头部view **/
	private RelativeLayout mRelativeLayout;
	private LayoutInflater inflater;
	private Gallery mGallery;
	private TextView post_titile;
	private ArrayList<ActiveCoverList> dataCoverList = new ArrayList<ActiveCoverList>();
	private int index = 0;// 记录选中的图片位置
	private static int indexitem = 0;// 记录选中的图片位置
	private boolean indexitemfla;// 记录选中的图片位置
	private ImageAdapter mImageAdapter;
	private LinearLayout type1, type2, type3, type4, type5, type6;
	private ImageView[] myPoints;
	public int ADD_POINT = -1;
	public static ImageView upload_icon;

	private String tag_id = "";
	private String img_ids = "";
	private String type = "";
	private boolean groupType = false;

	public void onResume() {
		super.onResume();

		PostBarActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				upload_icon = (ImageView) findViewById(R.id.upload_icon);
				if (VideoComperssUtils.postuploadIcon) {
					upload_icon.setVisibility(View.VISIBLE);
				} else {
					upload_icon.setVisibility(View.GONE);
				}
			}
		});

		MobclickAgent.onResume(this);
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
		setContentView(R.layout.postbar);
		init();

	}

	private void init() {

		upload_icon = (ImageView) findViewById(R.id.upload_icon);
		if (VideoComperssUtils.postuploadIcon) {
			upload_icon.setVisibility(View.VISIBLE);
		} else {
			upload_icon.setVisibility(View.GONE);
		}

		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		width = DM.widthPixels;
		ab = AbHttpUtil.getInstance(context);
		initView();

		app = (MyApplication) this.getApplication();
		mPullDownView = (PullDownView) findViewById(R.id.list);
		mPullDownView.setOnPullDownListener(this);
		listview = mPullDownView.getListView();
		// mPullDownView.setShowHeaderLayout(mRelativeLayout);
		listview.setDivider(null);
		if (hotAdapter == null) {
			hotAdapter = new MainInfoListAdapter(PostBarActivity.this, hotData, true);
		}

		listview.setAdapter(hotAdapter);
		// 设置可以自动获取更多 滑到最后一个自动获取 改成false将禁用自动获取更多
		mPullDownView.enableAutoFetchMore(false, 1);
		// 隐藏 并禁用尾部
		// mPullDownView.setHideFooter();
		// 显示并启用自动获取更多
		mPullDownView.setShowFooter();
		// 隐藏并且禁用头部刷新
		mPullDownView.setHideHeader();
		// listview.addHeaderView(mGallery);
		// 显示并且可以使用头部刷新
		mPullDownView.setShowHeader();
		// mPullDownView.setPadding(0, 0, 0, 0);
//		if (!Config.isConn(context)) {
//			try {
//				getlistData(Config.read(context, fileName));
//			} catch (Exception e) {
//			}
//		}
		onRefresh();

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			indexitemfla = true;
			listview.setSelection(indexitem);
		}
	}

	private void initView() {
		post_title = (TextView) findViewById(R.id.post_title);

		post_title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PostBarActivity.this.finish();
			}
		});
		
		Intent intent = getIntent();
		if (intent.hasExtra("img_title")) {
			post_title.setVisibility(View.VISIBLE);
			post_title.setText(intent.getStringExtra("img_title"));
		} else {
			post_title.setVisibility(View.GONE);
		}

		// post_title.setText("热点");
		send_post = (TextView) findViewById(R.id.send_post);
		send_post.setVisibility(View.GONE);
		inflater = LayoutInflater.from(context);
		ly_title = (RelativeLayout) findViewById(R.id.ly_title);
		search_root = (LinearLayout) findViewById(R.id.search_root);
		title = (RelativeLayout) findViewById(R.id.title);
		find = (ImageView) findViewById(R.id.find);
		// find.setVisibility(View.VISIBLE);
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
		/** 头部的view **/
		// initHeadbar();
		send_post.setOnClickListener(new OnClickListener() {

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
					startActivity(intent);
				}
			}
		});
	}

	private LinearLayout mGroup;
	private ImageView mPointImg;

	/**
	 * 头部的view
	 */
	@SuppressLint("ResourceAsColor")
	private void initHeadbar() {
		mRelativeLayout = (RelativeLayout) inflater.inflate(
				R.layout.head_bar_post, null);
		mGallery = (Gallery) mRelativeLayout.findViewById(R.id.gallery);
		post_titile = (TextView) mRelativeLayout.findViewById(R.id.post_titile);
		mGroup = (LinearLayout) mRelativeLayout.findViewById(R.id.viewGroup);
		type1 = (LinearLayout) mRelativeLayout.findViewById(R.id.type1);
		type2 = (LinearLayout) mRelativeLayout.findViewById(R.id.type2);
		type3 = (LinearLayout) mRelativeLayout.findViewById(R.id.type3);
		type4 = (LinearLayout) mRelativeLayout.findViewById(R.id.type4);
		type5 = (LinearLayout) mRelativeLayout.findViewById(R.id.type5);
		type6 = (LinearLayout) mRelativeLayout.findViewById(R.id.type6);
		type2.setOnClickListener(typeClick1); // 成长活动
		type3.setOnClickListener(typeClick2); // 育儿知识
		type4.setOnClickListener(typeClick3); // 时尚情感
		type5.setOnClickListener(typeClick4); // 辣妈厨房
		type6.setOnClickListener(typeClick5); // 视频
		// getAlbumList();
		mImageAdapter = new ImageAdapter(this, dataCoverList);
		mGallery.setAdapter(mImageAdapter);
		mGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					if ("0".equals(dataCoverList.get(position).is_group)) {
						Intent intent = new Intent(context,
								MainItemDetialActivity.class);
						intent.putExtra("user_id",
								dataCoverList.get(position).user_id);
						intent.putExtra("img_id",
								dataCoverList.get(position).img_id);
						context.startActivityForResult(intent, 1);
					} else if ("1".equals(dataCoverList.get(position).is_group)) {
						GroupRelevantUtils.CheckIntent(context, dataCoverList.get(position).group_id);
					} else {
						Intent intent = new Intent(context,
								BusinessDetailActivity.class);
						intent.putExtra("business_id",
								dataCoverList.get(position).img_id);
						context.startActivityForResult(intent, 1);
					}
				} catch (Exception e) {
				}

			}
		});
		mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (myPoints != null && myPoints.length > 0) {
					if ("1".equals(dataCoverList.get(position).is_group)) {
						post_titile.setText(dataCoverList.get(position).group_name);
					} else {
						post_titile.setText(dataCoverList.get(position).post_title);
					}
					for (int i = 0; i < myPoints.length; i++) {
						if (position == i) {
							myPoints[i]
									.setBackgroundResource(R.drawable.dian_hong);
						} else {
							myPoints[i]
									.setBackgroundResource(R.drawable.dian_hui);
						}
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	private void addPoint() {
		myPoints = new ImageView[dataCoverList.size()];
		if (dataCoverList.size() > 0)
			post_titile.setText(dataCoverList.get(0).post_title);
		/**
		 * 添加小圆点图片
		 */
		for (int i = 0; i < dataCoverList.size(); i++) {
			mPointImg = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
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

	public OnClickListener typeClick1 = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, PostBarType.class);
			intent.putExtra("post_class", "1");
			startActivity(intent);
			context.finish();
		}

	};

	public OnClickListener typeClick2 = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, PostBarType.class);
			intent.putExtra("post_class", "2");
			startActivity(intent);
			context.finish();
		}

	};

	public OnClickListener typeClick3 = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, PostBarType.class);
			intent.putExtra("post_class", "3");
			startActivity(intent);
			context.finish();
		}

	};

	public OnClickListener typeClick4 = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, PostBarType.class);
			intent.putExtra("post_class", "4");
			startActivity(intent);
			context.finish();
		}

	};

	public OnClickListener typeClick5 = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, PostBarType.class);
			intent.putExtra("post_class", "5");
			startActivity(intent);
			context.finish();
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
				hotAdapter.notifyDataSetChanged();
				break;
			case -1:
				mGroup.removeAllViews();
				addPoint();
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void onRefresh() {
		// getAlbumList();

		Config.showProgressDialog(context, false, null);
		Intent intent = getIntent();

		if (intent.hasExtra("tag_id")) {
			tag_id = intent.getStringExtra("tag_id");
		}

		if (intent.hasExtra("img_ids")) {
			img_ids = intent.getStringExtra("img_ids");
		}

		if (intent.hasExtra("type")) {
			type = intent.getStringExtra("type");
		}
		
		if (intent.hasExtra("groupType")) {
			groupType = intent.getBooleanExtra("groupType", false);
		}

		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("tag_id", tag_id + "");
		params.put("img_ids", img_ids + "");
		params.put("type", type + "");
		
		String url = "";
		if(groupType){
			url = ServerMutualConfig.GET_GROUP_LIST;
		}else{
			url = ServerMutualConfig.getListingHotList;
		}
		
		ab.get(url + "&" + params.toString(),
				new AbStringHttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Config.dismissProgress();
						hotData.clear();
						if (!content.isEmpty()) {
							try {
								JSONObject json = new JSONObject(content);
								for (int i = 0; i < json.getJSONArray("data").length(); i++) {
									MainListItemBean item = new MainListItemBean();
									hotData.add(item.fromJson(json.getJSONArray("data").getJSONObject(i)));
								}
								hotAdapter.notifyDataSetChanged();
							}catch (JSONException e) {
								e.printStackTrace();
							}
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
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("tag_id", tag_id + "");
		params.put("img_ids", img_ids + "");
		params.put("type", type + "");
		params.put(
				"post_create_time",
				hotData.get(hotData.size() - 1).getPost_create_time()
						+ "");
		// Log.e(TAG,
		// ServerMutualConfig.PostMyInterestListV3 + "&" + params.toString());
		String url = "";
		if(groupType){
			url = ServerMutualConfig.GET_GROUP_LIST;
		}else{
			url = ServerMutualConfig.getListingHotList;
		}
		ab.get(url + "&" + params.toString(),
				new AbStringHttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Config.dismissProgress();
						try {
							JSONObject json = new JSONObject(content);
							
							if(json.getJSONArray("data").length() == 0){
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
						}catch (JSONException e) {
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

	public void getAlbumList() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		// android.util.Log.e(TAG, ServerMutualConfig.PostCoverListV1 + "&"
		// + params.toString());
		ab.get(ServerMutualConfig.PostCoverListV1 + "&" + params.toString(),
				new AbStringHttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						JSONObject json = null;
						dataCoverList.clear();
						try {
							json = new JSONObject(content);
							if (json.getBoolean("success")) {
								for (int i = 0; i < json.getJSONArray("data")
										.length(); i++) {
									ActiveCoverList item = new ActiveCoverList();
									item.fromJson(json.getJSONArray("data")
											.getJSONObject(i));
									dataCoverList.add(item);
								}
								mImageAdapter.notifyDataSetChanged();
								handler.sendEmptyMessage(ADD_POINT);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				});
	}

	public class ActiveCoverList {
		public String id;
		public String post_album;
		public String img_id;
		public String user_id;
		public String is_save;
		public String post_title;
		public String is_group;
		public String group_id;
		public String group_name;
		public String is_bj_city;

		JSONObject json;

		public void fromJson(JSONObject json) throws JSONException {
			this.json = json;
			if (json.has("id")) {
				id = json.getString("id");
			}
			if (json.has("post_album")) {
				post_album = json.getString("post_album");
			}
			if (json.has("img_id")) {
				img_id = json.getString("img_id");
			}
			if (json.has("user_id")) {
				user_id = json.getString("user_id");
			}
			if (json.has("is_save")) {
				is_save = json.getString("is_save");
			}
			if (json.has("post_title")) {
				post_title = json.getString("post_title");
			}
			if (json.has("is_group")) {
				is_group = json.getString("is_group");
			}
			if (json.has("group_id")) {
				group_id = json.getString("group_id");
			}
			if (json.has("group_name")) {
				group_name = json.getString("group_name");
			}
			if (json.has("is_bj_city")) {
				is_bj_city = json.getString("is_bj_city") + "";
			}
		}
	}

	public class ImageAdapter extends BaseAdapter {
		private Context context;
		ArrayList<ActiveCoverList> dataCoverList;

		public ImageAdapter(Context context,
				ArrayList<ActiveCoverList> dataCoverList) {
			this.context = context;
			this.dataCoverList = dataCoverList;
		}

		@Override
		public int getCount() {
			return dataCoverList.size() == 0 ? 5 : dataCoverList.size();// 实现循环显示
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ImageView imageView = new ImageView(context);
			if (dataCoverList.size() > 0) {
				MyApplication.getInstance().display(
						dataCoverList.get(position).post_album, imageView,
						R.drawable.def_image);
			} else {
				imageView.setImageResource(R.drawable.def_image);
			}
			int newH = (int) (width / 3);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setLayoutParams(new Gallery.LayoutParams(
					Gallery.LayoutParams.FILL_PARENT, newH));
			RelativeLayout borderImg = new RelativeLayout(context);
			borderImg.addView(imageView);

			return borderImg;
		}
	}

	/**
	 * 搜索
	 */
	public OnClickListener findClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			PostBarActivity.this.finish();
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
			SearchData.clear();
			// Log.e(TAG, ServerMutualConfig.SearchGroup + "&" +
			// params.toString());
			ab.get(ServerMutualConfig.SearchGroup + "&" + params.toString(),
					new AbStringHttpResponseListener() {
						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							try {
								JSONObject json = new JSONObject(content);
								if (json.getBoolean("success")) {
									data_json = new JSONArray();
									if (json.getJSONArray("data").length() == 0) {
										Toast.makeText(context, "没有找到哦",
												Toast.LENGTH_SHORT).show();
									} else {
										for (int i = 0; i < json.getJSONArray(
												"data").length(); i++) {
											SearchItem item = new SearchItem();
											item.fromJson(json.getJSONArray(
													"data").getJSONObject(i));
											data_json.put(json.getJSONArray(
													"data").getJSONObject(i));
											SearchData.add(item);
										}
										new PopupWindows(context, ly_title);
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
					Intent intent = new Intent();
					if ("1".equals(SearchData.get(arg2).is_group)) {
						GroupRelevantUtils.CheckIntent(context, SearchData.get(arg2).group_id);
					} else {
						intent.setClass(context, MainItemDetialActivity.class);
						intent.putExtra("img_id", SearchData.get(arg2).group_id);
					}
					sss = 0;
					search_root.setVisibility(View.GONE);
					title.setVisibility(View.VISIBLE);
					startActivity(intent);

				}
			});
			update();
		}

	}

	class PopupAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return SearchData.size();
		}

		@Override
		public Object getItem(int position) {
			return SearchData.get(position);
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
			holder1.name.setText(SearchData.get(position).group_name);
			if ("1".equals(SearchData.get(position).is_group)) {
				holder1.img.setBackgroundResource(R.drawable.postbar_qun_icon);
			} else {
				holder1.img.setBackgroundResource(R.drawable.postbar_tie_icon);
			}

			if (mDownloader == null) {
				mDownloader = new ImageDownloader();
			}
			mDownloader.imageDownload(SearchData.get(position).avatar,
					holder1.img, "/Download/cache_files", context,
					new OnImageDownload() {

						@Override
						public void onDownloadSucc(Bitmap bitmap, String c_url,
								ImageView imageView) {
							ImageView imageView1 = (ImageView) popList
									.findViewWithTag(c_url);
							if (imageView1 != null) {
								imageView1.setImageBitmap(bitmap);
								imageView1.setTag("");
							}

						}
					});

			return convertView;
		}

	}

	class ViewHolder1 {
		ImageView img;
		TextView name;
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
package com.yyqq.code.grow;

import java.util.ArrayList;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.mob.MobSDK;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainTab;
import com.yyqq.code.main.PhotoManager;
import com.yyqq.code.photo.CropImage;
import com.yyqq.code.photo.PhoneAlbumActivity;
import com.yyqq.commen.model.GrowEditItem;
import com.yyqq.commen.model.TagItem;
import com.yyqq.commen.utils.BimpUtil;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class GrowEditActivity extends Activity implements OnPullDownListener {
	private String TAG = "GrowEditActivity";
	private PullDownView mPullDownView;
	private ListView listview;
	private Activity context;
	private ArrayList<GrowEditItem> dataGrowEdit = new ArrayList<GrowEditItem>();
	private MyAdapter adapter;
	private int width, height;
	private AbHttpUtil ab;
	private String uid = "";
	private TextView finish;
	private int page;
	private MyApplication app;
	private RelativeLayout mRelativeLayout;
	private LayoutInflater inflater;
	private TextView title; // 时间
	private EditText list_title; // 标题
	private RelativeLayout add_img;
	private ImageView grow_clear;
	private mPopWindowData mPopWindowData;
	private ListView popList;
	private ArrayList<TagItem> data = new ArrayList<TagItem>();
	private JSONArray data_json;
	private TextView tagName;
	private LinearLayout delete, privacy;
	private String album_id = "";
	private String myTitle = "";
	public static int maxSize = 20;
	private String mTag_name;
	private static String album_desc;
	private String diary_cate;
	private TextView tv_grivacy;
	private ImageView img_grivacy;
	private HorizontalScrollView scrollview;
	private ImageView grow_guide_biaoqian;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			context.finish();
			return false;
		}
		return false;
	}
	
	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		MobSDK.init(this);
		context = this;
		app = (MyApplication) context.getApplication();
		setContentView(R.layout.grow_edit);

		init();
		CheckIsFirst();
		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
		inflater = LayoutInflater.from(context);
		mTag_name = getIntent().getStringExtra("mTag_name");
		/** 头部的view **/
		initHeadbar();
		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		width = DM.widthPixels;
		height = DM.heightPixels;
		mPullDownView = (PullDownView) findViewById(R.id.list);
		mPullDownView.setOnPullDownListener(this);
		listview = mPullDownView.getListView();
		listview.setDivider(null);
		mPullDownView.setShowHeaderLayout(mRelativeLayout);

		if (adapter == null) {
			adapter = new MyAdapter();
		}
		listview.setAdapter(adapter);
		// 设置可以自动获取更多 滑到最后一个自动获取 改成false将禁用自动获取更多
		mPullDownView.enableAutoFetchMore(false, 1);
		// 隐藏 并禁用尾部
		// mPullDownView.setHideFooter();
		// 显示并启用自动获取更多
		mPullDownView.setShowFooter();
		// 隐藏并且禁用头部刷新
		mPullDownView.setHideHeader();
		// 显示并且可以使用头部刷新
		// mPullDownView.setShowHeader();
		dataGrowEdit.clear();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// 清除标题
		grow_clear.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!TextUtils.isEmpty(album_desc)
						|| !"添加标题".equals(list_title)) {
					list_title.setHint("");
					list_title.setText("");
				}
			}
		});
		// 上传照片
		add_img.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new PopupWindowsPicture(context, add_img);
			}
		});
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		context = this;
		album_desc = list_title.getText().toString();
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
//		album_desc = list_title.getText().toString();
	}

	private void init() {
		context = this;
		uid = "";
		if (getIntent().hasExtra("user_id")) {
			uid = getIntent().getStringExtra("user_id");
		}
		album_id = getIntent().getStringExtra("album_id");
		tv_grivacy = (TextView) findViewById(R.id.tv_grivacy);
		img_grivacy = (ImageView) findViewById(R.id.img_grivacy);
		finish = (TextView) findViewById(R.id.finish);
		finish.setOnClickListener(finishClick);
		delete = (LinearLayout) findViewById(R.id.delete);
		delete.setOnClickListener(deleteClick);
		privacy = (LinearLayout) findViewById(R.id.privacy);
		privacy.setOnClickListener(privacyClick);
		grow_guide_biaoqian = (ImageView) findViewById(R.id.guide_biaoqian);
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		findViewById(R.id.guide_biaoqian).startAnimation(shake);
		grow_guide_biaoqian.setOnClickListener(guideClick);
	}

	public OnClickListener guideClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			grow_guide_biaoqian.setVisibility(View.GONE);
		}

	};

	private void CheckIsFirst() {
		// 是否第一次进来了
		SharedPreferences sf = this.getSharedPreferences("isFirst",
				this.MODE_PRIVATE);
		if (0 == sf.getInt("firstedit", 0)) {
			grow_guide_biaoqian.setVisibility(View.VISIBLE);
			sf = context.getSharedPreferences("isFirst", Context.MODE_PRIVATE);
			Editor edit_user = sf.edit();
			edit_user.putInt("firstedit", 1);
			edit_user.commit();
		} else {
			grow_guide_biaoqian.setVisibility(View.GONE);
		}

	}

	/**
	 * 头部的view
	 */
	@SuppressLint("ResourceAsColor")
	private void initHeadbar() {
		mRelativeLayout = (RelativeLayout) inflater.inflate(
				R.layout.grow_edit_header, null);
		scrollview = (HorizontalScrollView) mRelativeLayout
				.findViewById(R.id.tag);
		add_img = (RelativeLayout) mRelativeLayout.findViewById(R.id.add_img);
		grow_clear = (ImageView) mRelativeLayout.findViewById(R.id.grow_clear);
		list_title = (EditText) mRelativeLayout.findViewById(R.id.list_title);
		title = (TextView) mRelativeLayout.findViewById(R.id.title1);
		if (TextUtils.isEmpty(getIntent().getStringExtra("grow_detail_title"))) {
			title.setText("时间未知");
		} else {
			title.setText(getIntent().getStringExtra("grow_detail_title"));
		}

		title.setOnClickListener(titleClick);
		onRefresh();
		getTag();
	}

	public void getTag() {
		Config.showProgressDialog(context, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
		params.put("baby_id", getIntent().getStringExtra("baby_id"));
		params.put("album_id", album_id);
		// android.util.Log.e("fanfan", ServerMutualConfig.DiaryTagListV1 + "&"
		// + params.toString());
		data.clear();
		ab.get(ServerMutualConfig.DiaryTagListV1 + "&" + params.toString(),
				new AbStringHttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								data_json = new JSONArray();
								for (int i = 0; i < json.getJSONArray("data")
										.length(); i++) {
									TagItem item = new TagItem();
									item.fromJson(json.getJSONArray("data")
											.getJSONObject(i));
									data_json.put(json.getJSONArray("data")
											.getJSONObject(i));
									data.add(item);
								}
								LinearLayout imgLinear = (LinearLayout) scrollview
										.findViewById(R.id.tag_layout1);
								addTag(scrollview, imgLinear, data);
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

	int tagCheck = 0;

	@SuppressLint("ResourceAsColor")
	private void addTag(HorizontalScrollView scrollview,
			LinearLayout imgLinear, final ArrayList<TagItem> data) {

		for (int i = 0; i < data.size(); i++) {
			LinearLayout layout = (LinearLayout) inflater.inflate(
					R.layout.grow_tag, null);
			layout.setId(i);
			tagClick(data, i, layout, imgLinear);
			tagName = (TextView) layout.findViewById(R.id.tag_text);
			tagName.setText(data.get(i).tag_name);
			if (i == 1 && !TextUtils.isEmpty(mTag_name)) {
				tagName.setBackgroundResource(R.drawable.grow_biao_bg);
			}
			imgLinear.addView(layout);
		}

	}

	private void tagClick(final ArrayList<TagItem> data, final int positon,
			final LinearLayout layout, final LinearLayout imgLinear) {
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if ("自定义".equals(data.get(positon).tag_name)) {
					AlertDialog.Builder builder = new Builder(context);
					builder.setTitle("自定义标签");
					final EditText edit_name = new EditText(context);
					edit_name.setSingleLine(true);
					edit_name.setMaxEms(10);
					builder.setView(edit_name);
					builder.setNegativeButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									checkTag(positon, layout, imgLinear);
									mTag_name = edit_name.getText().toString()
											.trim();
									addTag(data, imgLinear);
								}

								private void addTag(
										final ArrayList<TagItem> data,
										final LinearLayout imgLinear) {
									imgLinear.removeAllViews();
									for (int i = 0; i < data.size(); i++) {
										LinearLayout layout = (LinearLayout) inflater
												.inflate(R.layout.grow_tag,
														null);
										layout.setId(i);
										tagClick(data, i, layout, imgLinear);
										tagName = (TextView) layout
												.findViewById(R.id.tag_text);
										tagName.setText(data.get(i).tag_name);
										imgLinear.addView(layout);
										if (i == 0) {
											LinearLayout layout1 = (LinearLayout) inflater
													.inflate(R.layout.grow_tag,
															null);
											layout.setId(i);
											tagClick(data, i, layout1,
													imgLinear);
											tagName = (TextView) layout1
													.findViewById(R.id.tag_text);
											tagName.setText(mTag_name);
											tagName.setBackgroundResource(R.drawable.grow_biao_bg);
											imgLinear.addView(layout1);
											tagCheck = 1;
										}
									}
								}
							});

					builder.setNeutralButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});
					builder.create().show();
				} else {
					checkTag(positon, layout, imgLinear);
					mTag_name = data.get(positon).tag_name;
				}

			}

			private void checkTag(int positon, final LinearLayout layout,
					final LinearLayout imgLinear) {
				positon = positon + tagCheck;
				for (int i = 0; i < imgLinear.getChildCount(); i++) {
					if (positon == i) {
						layout.getChildAt(0).setBackgroundResource(
								R.drawable.grow_biao_bg);
					} else {
						((ViewGroup) imgLinear.getChildAt(i))
								.getChildAt(0)
								.setBackgroundResource(R.drawable.grow_biao_hui);
					}
				}
			}
		});
	}

	@Override
	public void onRefresh() {
		page = 1;
		Config.showProgressDialog(context, true, null);
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
		params.put("baby_id", getIntent().getStringExtra("baby_id"));
		params.put("album_id", album_id);
		params.put("page", page + "");
//		 android.util.Log.e("fanfan", ServerMutualConfig.UpdateDiaryList + "&"
//		 + params.toString());
		ab.get(ServerMutualConfig.UpdateDiaryList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						dataGrowEdit.clear();
						try {
							JSONObject json = new JSONObject(content)
									.optJSONObject("data");
							JSONArray jsonArray = json.optJSONArray("img");
							GrowEditItem item;
							for (int i = 0; i < jsonArray.length(); i++) {
								item = new GrowEditItem();
								item.fromJson(jsonArray.optJSONObject(i), "");
								dataGrowEdit.add(item);
							}
							mTag_name = json.optString("tag_name");
							diary_cate = json.optString("diary_cate");
							if (TextUtils.isEmpty(album_desc)){
								album_desc = json.optString("album_desc");
							}
							pushHead();
							adapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						mPullDownView.notifyDidMore();
						mPullDownView.RefreshComplete();
						Config.dismissProgress();
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
		if (dataGrowEdit.size() == 0) {
			mPullDownView.notifyDidMore();
			mPullDownView.RefreshComplete();
			return;
		}
		Config.showProgressDialog(context, true, null);
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
		params.put("baby_id", getIntent().getStringExtra("baby_id"));
		params.put("album_id", album_id);
		params.put("page", (++page) + "");
		ab.get(ServerMutualConfig.UpdateDiaryList + "&" + params.toString(),
				new AbStringHttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
						mPullDownView.notifyDidMore();
						try {
							JSONObject json = new JSONObject(content);
							JSONArray jsonArray = json.optJSONObject("data")
									.optJSONArray("img");
							GrowEditItem item;
							for (int i = 0; i < jsonArray.length(); i++) {
								item = new GrowEditItem();
								item.fromJson(jsonArray.optJSONObject(i), "");
								dataGrowEdit.add(item);
							}
							if (TextUtils.isEmpty(album_desc))
								album_desc = json.optString("album_desc");
							mTag_name = json.optString("tag_name");
							pushHead();
							adapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						mPullDownView.notifyDidMore();
						mPullDownView.RefreshComplete();
						Config.dismissProgress();
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}
				});
	}

	// 完成
	public OnClickListener finishClick = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			AbRequestParams params = new AbRequestParams();
			params.put("baby_id", getIntent().getStringExtra("baby_id"));
			params.put("user_id", Config.getUser(context).uid);
			params.put("tag_name", mTag_name);
			params.put("album_desc", list_title.getText().toString().trim());
			params.put("album_id", album_id);
			JSONObject jsonObject;
			JSONArray jsonArray = new JSONArray();
			for (int i = 0; i < dataGrowEdit.size(); i++) {
				jsonObject = new JSONObject();
				try {
					jsonObject.put("img_id", dataGrowEdit.get(i).img_id);
					jsonObject.put("img_description",
							dataGrowEdit.get(i).img_description);
					jsonObject.put("diary_time", dataGrowEdit.get(i).img_title);
					jsonArray.put(jsonObject);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			params.put("album_name", myTitle); // 页面顶部标题
			params.put("diary_imgs", jsonArray.toString());
			// android.util.Log.e("fanfan", params.toString());
			ab.post(ServerMutualConfig.UpDiary, params,
					new AbStringHttpResponseListener() {
						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							Intent intent = new Intent();
							intent.setClass(context, MainTab.class);
							intent.putExtra("tabid", 2);
							album_desc = "";
							startActivity(intent);
						}

						@Override
						public void onFinish() {
							super.onFinish();
							album_desc = "";
							Config.dismissProgress();
						}

						@Override
						public void onFailure(int statusCode, String content,
								Throwable error) {
							super.onFailure(statusCode, content, error);
							album_desc = "";
							Config.showFiledToast(context);
						}
					});

		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void pushHead() {
		/**
		 * head
		 */
		// 权限
		if ("0".equals(diary_cate)) {
			tv_grivacy.setText("设为隐私");
			img_grivacy.setBackgroundResource(R.drawable.grow_open);
		} else {
			tv_grivacy.setText("设为公开");
			img_grivacy.setBackgroundResource(R.drawable.grow_privacy);
		}
		// 标题
		if (TextUtils.isEmpty(album_desc)) {
			list_title.setHint("添加描述");
		} else {
			list_title.setText(album_desc);
		}
	}

	// 详情里的GridView

	class ViewHolder {
		RelativeLayout ly;
		ImageView img;
		TextView msg;
		TextView album_name;
	}

	// 详情的adapter
	@SuppressLint("ResourceAsColor")
	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dataGrowEdit.size();
		}

		@Override
		public Object getItem(int arg0) {
			return dataGrowEdit.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ViewHolder holder = null;

			convertView = inflater.inflate(R.layout.grow_edit_item, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.msg = (TextView) convertView.findViewById(R.id.description);

			holder.album_name = (TextView) convertView
					.findViewById(R.id.album_name);
			holder.ly = (RelativeLayout) convertView.findViewById(R.id.ly);
			convertView.setTag(holder);

			final GrowEditItem item = dataGrowEdit.get(position);

			/**
			 * item
			 */
			// 图片
			String string = "";
			if (item.ImageList.size() != 0)
				string = item.ImageList.get(0).img_thumb;
			app.display(string, holder.img, R.drawable.tutu);
			holder.img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

				}
			});

			// 相册名称
			holder.album_name.setText(item.img_title);
			holder.ly.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					mPopWindowData = new mPopWindowData(context, arg0, item);
				}

			});

			// 描述
			if (TextUtils.isEmpty(item.img_description)) {
				holder.msg.setText("点击添加图片描述");
			} else {
				holder.msg.setText(item.img_description);
			}

			// holder.msg.setTag(position);
			holder.msg.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// int position = (Integer) arg0.getTag();
					AlertDialog.Builder builder = new Builder(context);
					builder.setTitle("编辑描述");
					final EditText edit_name = new EditText(context);
					edit_name.setSingleLine(true);
					edit_name.setMaxEms(10);
					edit_name.setText(item.img_description);
					builder.setView(edit_name);
					builder.setNegativeButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									item.img_description = edit_name.getText()
											.toString().trim();
									adapter.notifyDataSetChanged();
								}
							});

					builder.setNeutralButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});
					builder.create().show();
				}

			});

			convertView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

				}
			});
			return convertView;
		}
	}

	EditText sui, month, day;
	Button ok, cancle;

	public class mPopWindowData extends PopupWindow {
		private String time;
		private ImageView before;
		private ImageView after;

		public mPopWindowData(Context mContext, View parent,
				final GrowEditItem item) {
			View view = View
					.inflate(mContext, R.layout.data_popupwindows, null);

			sui = (EditText) view.findViewById(R.id.sui);
			sui.setOnFocusChangeListener(inputFocus);
			month = (EditText) view.findViewById(R.id.month);
			month.setOnFocusChangeListener(inputFocus);
			month.addTextChangedListener(watcher);
			day = (EditText) view.findViewById(R.id.day);
			day.setOnFocusChangeListener(inputFocus);
			day.addTextChangedListener(watcher);
			ok = (Button) view.findViewById(R.id.ok);

			before = (ImageView) view.findViewById(R.id.before);
			after = (ImageView) view.findViewById(R.id.after);
			before.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					before.setBackgroundResource(R.drawable.before_sel);
					after.setBackgroundResource(R.drawable.after);
					time = "出生前";
				}
			});
			after.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					before.setBackgroundResource(R.drawable.before);
					after.setBackgroundResource(R.drawable.after_sel);
					time = "";
				}
			});
			if (parent == title) {
				ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						String str = getTimeString(sui.getText().toString(),
								month.getText().toString(), day.getText()
										.toString());
						if (!TextUtils.isEmpty(str)) {
							if (null == time) {
								title.setText(str);
								myTitle = str;
							} else {
								title.setText(time + str);
								myTitle = time + str;
							}
						}
						mPopWindowData.dismiss();

					}
				});
			} else {
				ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						String str = getTimeString(sui.getText().toString(),
								month.getText().toString(), day.getText()
										.toString());
						// if (!TextUtils.isEmpty(str)) {
						// item.img_title = time + str;
						// myTitle = time + str;
						// adapter.notifyDataSetChanged();
						// }
						if (!TextUtils.isEmpty(str)) {
							if (null == time) {
								item.img_title = str;
								myTitle = str;
							} else {
								item.img_title = time + str;
								myTitle = time + str;
							}
							adapter.notifyDataSetChanged();
						}
						mPopWindowData.dismiss();
					}

				});
			}
			cancle = (Button) view.findViewById(R.id.cancle);
			cancle.setOnClickListener(cancleClick);
			setWidth(LayoutParams.WRAP_CONTENT);
			setHeight(LayoutParams.WRAP_CONTENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			// showAsDropDown(parent);
			showAtLocation(parent, Gravity.CENTER, 0, 0);
		}

		private String getTimeString(String year, String month, String day) {
			if (!TextUtils.isEmpty(year))
				year = Integer.valueOf(year) == 0 ? "" : year + "岁";
			if (!TextUtils.isEmpty(day))
				day = Integer.valueOf(day) == 0 ? "" : day + "天";
			if (!TextUtils.isEmpty(month))
				month = Integer.valueOf(month) == 0 ? "" : month + "个月";
			if (TextUtils.isEmpty(month) && day.equals("0"))
				month = "";
			return year + month + day;

		}

	}

	private TextWatcher watcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (!TextUtils.isEmpty(month.getText().toString())
					&& Integer.valueOf(month.getText().toString()) > 11) {
				Toast.makeText(context, "请输入0~11", Toast.LENGTH_SHORT).show();
				month.setText("");
			}
			if (!TextUtils.isEmpty(day.getText().toString())
					&& Integer.valueOf(day.getText().toString()) > 31) {
				Toast.makeText(context, "请输入0~31", Toast.LENGTH_SHORT).show();
				day.setText("");
			}
		}
	};

	public OnFocusChangeListener inputFocus = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				v.setBackgroundResource(R.drawable.input_bg_sel);
			} else {
				v.setBackgroundResource(R.drawable.wait_input_bg);
			}
		}
	};

	public OnClickListener cancleClick = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mPopWindowData.dismiss();
		}
	};

	public OnClickListener titleClick = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mPopWindowData = new mPopWindowData(context, title, null);
		}
	};

	// 设置隐私
	public OnClickListener privacyClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Config.showProgressDialog(context, false, null);
			AjaxParams params = new AjaxParams();
			params.put("login_user_id", Config.getUser(context).uid);
			params.put("album_id", album_id);
			FinalHttp fh = new FinalHttp();
			// android.util.Log.e("fanfan", params.toString());
			String Re = "";
			if ("0".equals(diary_cate)) {
				Re = ServerMutualConfig.DiaryCate;
			} else {
				Re = ServerMutualConfig.CancelDiaryCate;
			}
			fh.post(Re, params, new AjaxCallBack<String>() {
				@Override
				public void onFailure(Throwable t, String strMsg) {
					super.onFailure(t, strMsg);
					Config.dismissProgress();
					Config.showFiledToast(context);
				}

				@Override
				public void onSuccess(String t) {
					super.onSuccess(t);
					Config.dismissProgress();
					try {
						JSONObject json = new JSONObject(t);
						Toast.makeText(context, json.getString("reMsg"),
								Toast.LENGTH_SHORT).show();
						onRefresh();
					} catch (Exception e) {
					}
				}
			});
		}
	};

	// 删除全部
	public OnClickListener deleteClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Builder builder = new Builder(context);
			builder.setMessage("确认要删除吗?");
			builder.setNegativeButton("删除",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Config.showProgressDialog(context, false, null);
							AjaxParams params = new AjaxParams();
							params.put("user_id", Config.getUser(context).uid);
							params.put("album_id", album_id);
							FinalHttp fh = new FinalHttp();
							fh.post(ServerMutualConfig.DelDiaryAlbum, params,
									new AjaxCallBack<String>() {
										@Override
										public void onFailure(Throwable t,
												String strMsg) {
											super.onFailure(t, strMsg);
											Config.dismissProgress();
											Config.showFiledToast(context);
										}

										@Override
										public void onSuccess(String t) {
											super.onSuccess(t);
											Config.dismissProgress();
											try {
												JSONObject json = new JSONObject(
														t);
												Toast.makeText(
														context,
														json.getString("reMsg"),
														Toast.LENGTH_SHORT)
														.show();
												onRefresh();
											} catch (Exception e) {
											}
										}
									});
						}
					});
			builder.setNeutralButton("取消", null);
			builder.create().show();
		}
	};

	/**
	 * 添加图片
	 * 
	 * @author Administrator
	 * 
	 */
	public class PopupWindowsPicture extends PopupWindow {
		public PopupWindowsPicture(Context mContext, View parent) {
			Button album, Photo, camera, cancel;
			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_in));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_1));
			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();
			camera = (Button) view.findViewById(R.id.item_popupwindows_camera);
			Photo = (Button) view.findViewById(R.id.item_popupwindows_Photo);
			cancel = (Button) view.findViewById(R.id.item_popupwindows_cancel);
			album = (Button) view.findViewById(R.id.item_popupwindows_album);
			/**
			 * 拍摄一张
			 */
			camera.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (isFallBit()) {
						photo();
					} else {
						Toast.makeText(context, "已达到上限", 0).show();
					}
					dismiss();
				}
			});
			/**
			 * 从手机相册里选
			 */
			Photo.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (isFallBit()) {
						Intent intent = new Intent(context,
								PhoneAlbumActivity.class);
						intent.putExtra("tag", "isGrowEdit");
						intent.putExtra("album_id", album_id);
						intent.putExtra("baby_id",
								getIntent().getStringExtra("baby_id"));
						intent.putExtra("grow_detail_title", getIntent()
								.getStringExtra("grow_detail_title"));
						intent.putExtra(Config.SELECTNAME, maxSize);
						startActivity(intent);
						context.finish();
					} else {
						Toast.makeText(context, "已达到上限", 1).show();
					}
					dismiss();
				}
			});

			/**
			 * 取消
			 */
			cancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});
			/**
			 * 从秀秀相册里选
			 */
			album.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (isFallBit()) {
						Intent intent = new Intent(context, PhotoManager.class);
						intent.putExtra("isAlbum", "isAlbum");
						intent.putExtra("tag", "isGrowEdit");
						intent.putExtra("album_id1", album_id);
						intent.putExtra("baby_id",
								getIntent().getStringExtra("baby_id"));
						intent.putExtra("grow_detail_title", getIntent()
								.getStringExtra("grow_detail_title"));
						Config.SELECTEDIMAGECOUNT = maxSize;
						startActivity(intent);
						context.finish();
					} else {
						Toast.makeText(context, "已达到上限", 1).show();
					}
					dismiss();
				}
			});
		}
	}

	private boolean isFallBit() {

		if (BimpUtil.drr.size() >= maxSize) {
			return false;
		}
		return true;
	}

	private static final int TAKE_PICTURE = 0x000000;
	private static final int IMAGE_CROP_RETURN = 4;
	private String path = "";
	private static int imgNum;

	public void photo() {
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		imgNum++;
		path = "temp_image_" + imgNum + ".jpg";
		Uri imageUri = Uri.parse("file:///sdcard/yyqq/babyshow/image/" + path);
		i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(i, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			Intent intent2 = new Intent();
			intent2.setClass(context, CropImage.class);
			intent2.putExtra("tag", "isGrowEdit");
			intent2.putExtra("path", Config.ImageFile + path);
			intent2.putExtra("album_id", album_id);
			intent2.putExtra("baby_id", getIntent().getStringExtra("baby_id"));
			startActivityForResult(intent2, IMAGE_CROP_RETURN);
			break;
		case IMAGE_CROP_RETURN:
			break;
		}
	}

}
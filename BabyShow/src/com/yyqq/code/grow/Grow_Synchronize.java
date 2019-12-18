package com.yyqq.code.grow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainTab;
import com.yyqq.code.personal.PersonalCenterActivity;
import com.yyqq.code.personal.UserInfo;
import com.yyqq.code.photo.ChangePhotoSize;
import com.yyqq.commen.model.GrowDetailItem;
import com.yyqq.commen.model.GrowDetailItem.Image;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.FaceConversionUtil;
import com.yyqq.commen.utils.ImageDownloader;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.utils.OnImageDownload;
import com.yyqq.commen.utils.RoundAngleImageView;
import com.yyqq.commen.view.MyGridView;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class Grow_Synchronize extends Activity implements OnPullDownListener {
	private String TAG = "Grow_Synchronize";
	private PullDownView mPullDownView;
	private ListView listview;
	private TextView title;
	private Activity context;
	private ArrayList<GrowDetailItem> dataGrowShow = new ArrayList<GrowDetailItem>();
	private MyAdapter adapter;
	private int width;
	private AbHttpUtil ab;
	private String grow_title = "";
	private int page;
	private ImageView back;
	private String uid;
	private ImageView share, edit;
	private TextView Sync;
	private LinearLayout ly_bottom;
	private ImageView sel_all;
	private Map<String, Boolean> mIsSyn = new HashMap<String, Boolean>();
	private boolean isAll = false;
	private String imgId = "";
	private String babyId = "";
	private mPopWindowData mPopWindowData;

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		setContentView(R.layout.grow_detail);

		init();
		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		width = DM.widthPixels;
		mPullDownView = (PullDownView) findViewById(R.id.list);
		mPullDownView.setOnPullDownListener(this);
		listview = mPullDownView.getListView();
		listview.setDivider(null);
		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
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
		dataGrowShow.clear();
		onRefresh();
	}

	@Override
	protected void onStart() {
		super.onStart();
		ly_bottom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isAll == false) {
					sel_all.setBackgroundResource(R.drawable.sel_bt);
					isAll = true;
					adapter.notifyDataSetChanged();
				} else {
					sel_all.setBackgroundResource(R.drawable.sel_bt_no);
					adapter.notifyDataSetChanged();
					isAll = false;
				}
			}
		});
	}

	public void onResume() {
		super.onResume();
		context = this;
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void init() {
		context = this;
		uid = "";
		if (getIntent().hasExtra("user_id")) {
			uid = getIntent().getStringExtra("user_id");
		}
		share = (ImageView) findViewById(R.id.share);
		share.setVisibility(View.GONE);
		edit = (ImageView) findViewById(R.id.edit);
		edit.setVisibility(View.GONE);
		Sync = (TextView) findViewById(R.id.Sync);
		Sync.setVisibility(View.VISIBLE);
		Sync.setOnClickListener(synClick);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(backClick);
		title = (TextView) findViewById(R.id.title);
		title.setText(getIntent().getStringExtra("grow_detail_title"));
		ly_bottom = (LinearLayout) findViewById(R.id.ly_bottom);
		ly_bottom.setVisibility(View.VISIBLE);
		sel_all = (ImageView) findViewById(R.id.sel_all);
	}

	@Override
	public void onRefresh() {
		page = 1;
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
		params.put("baby_id", getIntent().getStringExtra("baby_id"));
		params.put("album_id", getIntent().getStringExtra("album_id"));
		params.put("page", page + "");
		// android.util.Log.e("fanfan", ServerMutualConfig.DiaryImgsList + "&"
		// + params.toString());
		ab.get(ServerMutualConfig.DiaryImgsList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Config.dismissProgress();
						dataGrowShow.clear();
						try {
							JSONObject json = new JSONObject(content);
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								GrowDetailItem item = new GrowDetailItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i), "");
								dataGrowShow.add(item);
							}
							if (dataGrowShow.isEmpty()) {
								mPullDownView.setVisibility(View.GONE);
								Intent intent = new Intent();
								intent.setClass(context, MainTab.class);
								intent.putExtra("tabid", 2);
								startActivity(intent);
								context.finish();
							}
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
		if (dataGrowShow.size() == 0) {
			mPullDownView.notifyDidMore();
			mPullDownView.RefreshComplete();
			return;
		}
		Config.showProgressDialog(context, true, null);
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
		params.put("baby_id", getIntent().getStringExtra("baby_id"));
		params.put("album_id", getIntent().getStringExtra("album_id"));
		params.put("page", (++page) + "");
		ab.get(ServerMutualConfig.DiaryImgsList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
						mPullDownView.notifyDidMore();
						try {
							JSONObject json = new JSONObject(content);
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								GrowDetailItem item = new GrowDetailItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i), "");
								dataGrowShow.add(item);
							}
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

	// 同步
	public OnClickListener synClick = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (isAll == getChek()) {
				Builder builder = new Builder(context);
				builder.setMessage("请选择需要同步的照片");
				builder.setNegativeButton("好的",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				builder.create().show();
				return;
			}

			if ("0".equals(dataGrowShow.get(0).is_type)) {
				Intent intent = new Intent();
				intent.setClass(context, GrowAttention2.class);
				startActivity(intent);
			} else {
				if (Integer.parseInt(dataGrowShow.get(0).babys_count) > 1
						&& TextUtils.isEmpty(babyId)) {
					mPopWindowData = new mPopWindowData(context, arg0);
				} else {// 同步接口
					if (TextUtils.isEmpty(babyId)) {
						babyId = dataGrowShow.get(0).babyList.get(0).baby_id;
					}
					AbRequestParams params = new AbRequestParams();
					params.put("login_user_id", Config.getUser(context).uid);
					params.put("baby_id", babyId);
					params.put("album_id",
							getIntent().getStringExtra("album_id"));
					imgId = getImgIdList();
					params.put("img_id", imgId);
					// android.util.Log.e(
					// "fanfan",
					// ServerMutualConfig.BabysIdolCombine + "&"
					// + params.toString());
					ab.get(ServerMutualConfig.BabysIdolCombine + "&"
							+ params.toString(),
							new AbStringHttpResponseListener() {
								@Override
								public void onSuccess(int statusCode,
										String content) {
									super.onSuccess(statusCode, content);
									try {
										JSONObject json = new JSONObject(
												content);
										Toast.makeText(context,
												json.getString("reMsg"),
												Toast.LENGTH_SHORT).show();
										Intent intent = new Intent();
										intent.setClass(context, MainTab.class);
										intent.putExtra("tabid", 2);
										imgId = "";
										startActivity(intent);
									} catch (Exception e) {
									}
								}

							});
				}
			}
		}
	};

	private String getImgIdList() {
		if (isAll || mIsSyn.size() <= 0)// 选择全部同步
			return "";
		for (String key : mIsSyn.keySet()) {
			if (mIsSyn.get(key)) {
				imgId += key + ",";
			}
		}

		return TextUtils.isEmpty(imgId) ? imgId : imgId.substring(0,
				imgId.length() - 1).toString();
	}

	private boolean getChek() {
		if (mIsSyn.size() <= 0)// 选择全部同步
			return false;
		for (String key : mIsSyn.keySet()) {
			if (mIsSyn.get(key)) {
				return true;
			}
		}
		return false;
	}

	// 返回
	public OnClickListener backClick = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			finish();
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// 详情里的GridView
	class GridAdapter extends BaseAdapter {
		MyGridView myGridView;
		ArrayList<GrowDetailItem.Image> ImageList;
		private ImageDownloader mDownloader;

		public GridAdapter(ArrayList<GrowDetailItem.Image> ImageList,
				MyGridView myGridView) {
			this.ImageList = ImageList;
			this.myGridView = myGridView;
		}

		@Override
		public int getCount() {

			return ImageList.size();
		}

		@Override
		public Object getItem(int position) {
			return ImageList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater
						.inflate(R.layout.myshow_grida_item, null);
				holder = new ViewHolder();
				holder.img = (ImageView) convertView
						.findViewById(R.id.item_grida_image);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			GrowDetailItem.Image item = ImageList.get(position);
			if (mDownloader == null) {
				mDownloader = new ImageDownloader();
			}

			holder.img.setTag(ImageList.get(position).img_thumb);
			if (mDownloader == null) {
				mDownloader = new ImageDownloader();
			}
			if (mDownloader != null) {
				mDownloader.imageDownload(ImageList.get(position).img_thumb,
						holder.img, "/Download/cache_files/", context,
						new OnImageDownload() {

							@Override
							public void onDownloadSucc(Bitmap bitmap,
									String c_url, ImageView imageView) {
								ImageView imageView1 = (ImageView) myGridView
										.findViewWithTag(c_url);
								if (imageView1 != null) {
									imageView1.setImageBitmap(bitmap);
									imageView1.setTag("");
								}

							}
						});
			}
			// 大图
			int newW = 0, newH = 0;
			if (item.img_thumb_width == item.img_thumb_height) {
				newW = width;
				newH = width;
			} else if (Float.parseFloat(item.img_thumb_width) > Float
					.parseFloat(item.img_thumb_height)) {
				float h = 0;
				float sx = (float) ((float) width / (float) (Integer
						.parseInt(item.img_thumb_width)));
				h = (float) ((float) Float.parseFloat(item.img_thumb_height) * (float) sx);
				newW = width;
				newH = (int) h;
			} else {
				float sx = (float) (width * 0.72)
						/ (Float.parseFloat(item.img_thumb_width));
				newW = (int) ((float) (((Float.parseFloat(item.img_thumb_width)) * sx)));
				newH = (int) ((float) ((Float.parseFloat(item.img_thumb_height)) * sx));
			}
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					newW, newH);
			params.addRule(RelativeLayout.BELOW, R.id.head_root);
			holder.img.setLayoutParams(params);
			holder.img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ChangePhotoSize.class);

					ArrayList<String> imgBig = new ArrayList<String>();
					ArrayList<String> imaHed = new ArrayList<String>();
					ArrayList<String> imaWid = new ArrayList<String>();
					for (int i = 0; i < ImageList.size(); i++) {
						imgBig.add(ImageList.get(i).img);
						imaHed.add(ImageList.get(i).img_height);
						imaWid.add(ImageList.get(i).img_width);
					}
					intent.putStringArrayListExtra("img_downList", imgBig);
					intent.putStringArrayListExtra("imgList", imgBig);
					intent.putStringArrayListExtra("imaWid", imaWid);
					intent.putStringArrayListExtra("imaHed", imaHed);
					intent.putExtra("listIndex", position);
					context.startActivity(intent);
				}
			});
			return convertView;
		}

	}

	class ViewHolder {
		TextView growTitle;
		ImageView img;
		MyGridView mGridView;
		TextView name;
		RoundAngleImageView head;
		ImageView sel;
		TextView msg;
	}

	// 详情的adapter
	@SuppressLint("ResourceAsColor")
	public class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return dataGrowShow.size();
		}

		@Override
		public Object getItem(int arg0) {
			return dataGrowShow.get(arg0);
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
			final ViewHolder holder = new ViewHolder();

			convertView = inflater.inflate(R.layout.grow_synchroniz_item, null);

			holder.growTitle = (TextView) convertView
					.findViewById(R.id.grow_title);

			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.mGridView = (MyGridView) convertView
					.findViewById(R.id.n_img);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.sel = (ImageView) convertView.findViewById(R.id.sel);
			holder.msg = (TextView) convertView.findViewById(R.id.msg);
			holder.head = (RoundAngleImageView) convertView
					.findViewById(R.id.head);
			convertView.setTag(holder);

			final GrowDetailItem item = dataGrowShow.get(position);
			ScrollView scrollview = (ScrollView) convertView
					.findViewById(R.id.img_ly);
			if (item.ImageList.size() == 0) {
				scrollview.setVisibility(View.GONE);
			}
			LinearLayout imgLinear = new LinearLayout(context);
			addImg(scrollview, imgLinear, item.ImageList, position);

			// 标题
			if (TextUtils.isEmpty(item.img_title)) {
				holder.growTitle.setVisibility(View.GONE);
			} else {
				grow_title = item.img_title;
				holder.growTitle.setText(grow_title);
			}

			holder.name.setText(item.user_name);

			holder.head.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					if (item.user_id.equals(Config.getUser(context).uid)) {
						intent.setClass(context, PersonalCenterActivity.class);
						intent.putExtra("falg_return", true);
					} else {
						intent.setClass(context, UserInfo.class);
						intent.putExtra("uid", item.user_id);
					}
					startActivity(intent);
				}
			});
			MyApplication.getInstance().display(item.avatar, holder.head,
					R.drawable.def_head);

			// 描述
			if (item.img_description.equals("")) {
				holder.msg.setVisibility(View.GONE);
			} else {
				SpannableString spannableString = FaceConversionUtil
						.getInstace().getExpressionString(context,
								item.img_description);
				holder.msg.setText(spannableString);
			}

			if (isAll == true) {
				holder.sel.setVisibility(View.GONE);
			} else {
				setSyn(holder, item);
			}

			return convertView;
		}

		/**
		 * 
		 * @param holder
		 * @param item
		 */
		private void setSyn(final ViewHolder holder, final GrowDetailItem item) {
			if (mIsSyn.containsKey(item.img_id)) {
				if (mIsSyn.get(item.img_id))
					holder.sel.setBackgroundResource(R.drawable.sel_bt);
				else
					holder.sel.setBackgroundResource(R.drawable.sel_bt_no);
			} else {
				mIsSyn.put(item.img_id, false);
				holder.sel.setBackgroundResource(R.drawable.sel_bt_no);
			}
			holder.sel.setTag(item.img_id);
			holder.sel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String sid = (String) arg0.getTag();
					if (!mIsSyn.get(sid)) {
						mIsSyn.put(item.img_id, true);
						holder.sel.setBackgroundResource(R.drawable.sel_bt);
					} else {
						mIsSyn.put(item.img_id, false);
						holder.sel.setBackgroundResource(R.drawable.sel_bt_no);
					}
				}
			});
		}

		private void addImg(ScrollView scrollview, LinearLayout imgLinear,
				final ArrayList<Image> imageList, final int position) {
			imgLinear.setOrientation(LinearLayout.VERTICAL);
			imgLinear.setGravity(Gravity.CENTER_HORIZONTAL);
			imgLinear.setPadding(0, 5, 0, 5);
			for (int i = 0; i < imageList.size(); i++) {
				final ImageView img = new ImageView(context);
				img.setId(i);
				img.setPadding(0, 5, 0, 5);
				MyApplication.getInstance().display(imageList.get(i).img_thumb,
						img, R.drawable.def_image);

				// 大图
				int newW = 0, newH = 0;
				if (imageList.size() > 0) {
					if (imageList.get(i).img_thumb_width == imageList.get(i).img_thumb_height) {
						newW = width;
						newH = width;
					} else if (Float
							.parseFloat(imageList.get(i).img_thumb_width) > Float
							.parseFloat(imageList.get(i).img_thumb_height)) {
						float h = 0;
						float sx = (float) ((float) width / (float) (Integer
								.parseInt(imageList.get(i).img_thumb_width)));
						h = (float) ((float) Float
								.parseFloat(imageList.get(i).img_thumb_height) * (float) sx);
						newW = width;
						newH = (int) h;
					} else {
						float sx = (float) (width * 0.72)
								/ (Float.parseFloat(imageList.get(i).img_thumb_width));
						newW = (int) ((float) (((Float.parseFloat(imageList
								.get(i).img_thumb_width)) * sx)));
						newH = (int) ((float) ((Float.parseFloat(imageList
								.get(i).img_thumb_height)) * sx));
					}
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
							newW, newH);
					params.addRule(RelativeLayout.BELOW, R.id.head_root);
					img.setLayoutParams(params);
					img.setScaleType(ScaleType.FIT_XY);
					imgLinear.addView(img);
					img.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context,
									ChangePhotoSize.class);

							ArrayList<String> imgBig = new ArrayList<String>();
							ArrayList<String> imaHed = new ArrayList<String>();
							ArrayList<String> imaWid = new ArrayList<String>();
							for (int j = 0; j < imageList.size(); j++) {
								imgBig.add(imageList.get(j).img_down);
								imaHed.add(imageList.get(j).img_height);
								imaWid.add(imageList.get(j).img_width);
							}
							intent.putStringArrayListExtra("imgList", imgBig);
							intent.putStringArrayListExtra("img_downList",
									imgBig);
							intent.putStringArrayListExtra("imaWid", imaWid);
							intent.putStringArrayListExtra("imaHed", imaHed);
							intent.putExtra("listIndex", img.getId());
							context.startActivity(intent);
						}
					});
				}
			}
			scrollview.addView(imgLinear);
		}

		/* 单图显示 */
		private void getImage(String imagePath,
				final ArrayList<GrowDetailItem.Image> imgList,
				final ImageView view, final int positon, final View convertView) {
			view.setTag(imagePath);
			ImageDownloader mDownloader = null;
			if (mDownloader == null) {
				mDownloader = new ImageDownloader();
			}
			if (mDownloader != null) {
				mDownloader.imageDownload(imagePath, view,
						"/Download/cache_files/", context,
						new OnImageDownload() {
							@Override
							public void onDownloadSucc(Bitmap bitmap,
									String c_url, ImageView imageView) {
								ImageView imageView1 = (ImageView) convertView
										.findViewWithTag(c_url);
								if (imageView1 != null) {
									imageView1.setImageBitmap(bitmap);
									imageView1.setTag("");
								}

							}
						});
			}

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ChangePhotoSize.class);
					ArrayList<String> list = new ArrayList<String>();
					list.add(imgList.get(0).img);
					intent.putExtra("imgList", list);
					context.startActivity(intent);
				}
			});
		}
	}

	Button ok, cancle;

	public class mPopWindowData extends PopupWindow {
		private LinearLayout babyRoot;

		public mPopWindowData(Context mContext, View parent) {
			View view = View.inflate(mContext, R.layout.grow_baby_popupwindows,
					null);
			babyRoot = (LinearLayout) view.findViewById(R.id.baby_root);

			for (int i = 0; i < dataGrowShow.get(0).babyList.size(); i++) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view1 = inflater.inflate(R.layout.grow_sel_baby, null);
				TextView baby = (TextView) view1.findViewById(R.id.name);
				baby.setText(dataGrowShow.get(0).babyList.get(i).user_name);
				view1.setTag(i);
				view1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ImageView img = (ImageView) v
								.findViewById(R.id.jiantou);
						img.setBackgroundResource(R.drawable.sel_bt);
						int item = (Integer) v.getTag();
						change(item);
						babyId = dataGrowShow.get(0).babyList.get(item).baby_id;
					}
				});
				babyRoot.addView(view1);
			}
			ok = (Button) view.findViewById(R.id.ok);
			ok.setOnClickListener(okClick);
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

		public void change(int item) {
			for (int i = 0; i < babyRoot.getChildCount(); i++) {
				if (i != item) {
					ImageView img = (ImageView) babyRoot.getChildAt(i)
							.findViewById(R.id.jiantou);
					img.setBackgroundResource(R.drawable.sel_bt_no);
				}
			}
		}

	}

	public OnClickListener okClick = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mPopWindowData.dismiss();
		}
	};
	public OnClickListener cancleClick = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mPopWindowData.dismiss();
		}
	};

}
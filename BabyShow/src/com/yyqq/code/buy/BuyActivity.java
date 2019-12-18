package com.yyqq.code.buy;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.business.BusinessDetailActivity;
import com.yyqq.commen.model.BuyItem;
import com.yyqq.commen.model.BuyItemRecommend;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.utils.WebViewActivity;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BuyActivity extends Activity implements OnPullDownListener {

	private final String TAG = "BuyActivity";
	private PullDownView mPullDownView;
	private ListView listview;
	private MyAdapter adapter;
	private AbHttpUtil ab;
	private Activity context;
	private ArrayList<BuyItem> data = new ArrayList<BuyItem>();
	private ArrayList<BuyItemRecommend> data1 = new ArrayList<BuyItemRecommend>();
	private MyApplication app;
	private String fileName = "BuyActivity.txt";
	private int windowWidth;
	private ImageView send_buy;
	private MyApplication myMyApplication;
	/**
	 * 头部的view
	 */
	private LayoutInflater inflater;
	private RelativeLayout mRelativeLayout;
	// private Gallery mGallery;
	private ImageAdapter mImageAdapter;
	private ArrayList<ActiveCoverList> dataCoverList = new ArrayList<ActiveCoverList>();
	Timer timer;
	private boolean isRun;
	private int index = 0;// 记录选中的图片位置
	private String imgId = "";
	private LinearLayout type1, type2, type3, type4, type5, type6, type7,
			type8;
	private FrameLayout ly1, ly2, ly3;
	private ImageView ig1, ig2, ig3;
	private TextView tv1, tv2, tv3;;
	private LinearLayout right;
	
	
	public void onResume() {
		super.onResume();
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
		setContentView(R.layout.buy);
		myMyApplication = (MyApplication) context.getApplication();
		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		windowWidth = DM.widthPixels;
		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
		app = (MyApplication) this.getApplication();
		inflater = LayoutInflater.from(context);
		app = (MyApplication) this.getApplication();
		mPullDownView = (PullDownView) findViewById(R.id.list);
		send_buy = (ImageView) findViewById(R.id.send_buy);
		send_buy.setOnClickListener(sendClick);
		/** 头部的view **/
		initHeadbar();
		init();
	}

	private void init() {
		mPullDownView.setOnPullDownListener(this);
		listview = mPullDownView.getListView();
		listview.setDivider(null);
		if (adapter == null) {
			adapter = new MyAdapter();
		}
		listview.setAdapter(adapter);
		// 设置可以自动获取更多 滑到最后一个自动获取 改成false将禁用自动获取更多
		mPullDownView.enableAutoFetchMore(true, 1);
		// 隐藏 并禁用尾部
		// mPullDownView.setHideFooter();
		// 显示并启用自动获取更多
		mPullDownView.setShowFooter();
		// 隐藏并且禁用头部刷新
		// mPullDownView.setHideHeader();
		// 显示并且可以使用头部刷新
		mPullDownView.setShowHeader();
		mPullDownView.setShowHeaderLayout(mRelativeLayout);
		if (!Config.isConn(context)) {
			try {
				getlistData(Config.read(context, fileName));
			} catch (Exception e) {
			}
		}
		onRefresh();
	}

	public OnClickListener sendClick = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if ("1".equals(myMyApplication.getVisitor())) {
				Intent intent = new Intent();
				intent.setClass(context, WebViewActivity.class);
				startActivity(intent);
//				AlertDialog.Builder dialog = new Builder(context);
//				dialog.setTitle("宝宝秀秀");
//				dialog.setMessage("游客不能使用这里哦，请登录");
//				dialog.setNegativeButton("取消",
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//							}
//
//						});
//				dialog.setPositiveButton("登录",
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface arg0, int arg1) {
//								myMyApplication.visitor = "";
//								Intent intent = new Intent();
//								intent.setClass(context, Login.class);
//								startActivity(intent);
//							}
//						});
//				dialog.create().show();
			} else {
				Intent intent = new Intent();
				intent.setClass(context, BuyShow.class);
				startActivity(intent);
			}
		}

	};

	private void initHeadbar() {
		inflater = LayoutInflater.from(context);
		mRelativeLayout = (RelativeLayout) inflater.inflate(
				R.layout.buy_head_bar, null);
		// mGallery = (Gallery) mRelativeLayout.findViewById(R.id.gallery);
		// mImageAdapter = new ImageAdapter(context, dataCoverList);
		// mGallery.setAdapter(mImageAdapter);
		// 八大分类
		// buy_type 1代表0-1岁；2代表1-3岁；3代表3-6岁；4代表6-12岁 5代表辣妈
		// buy_list 5代表家居 6代表孕妈，7代表海淘
		type1 = (LinearLayout) mRelativeLayout.findViewById(R.id.type1);
		type2 = (LinearLayout) mRelativeLayout.findViewById(R.id.type2);
		type3 = (LinearLayout) mRelativeLayout.findViewById(R.id.type3);
		type4 = (LinearLayout) mRelativeLayout.findViewById(R.id.type4);
		type5 = (LinearLayout) mRelativeLayout.findViewById(R.id.type5);
		type6 = (LinearLayout) mRelativeLayout.findViewById(R.id.type6);
		type7 = (LinearLayout) mRelativeLayout.findViewById(R.id.type7);
		type8 = (LinearLayout) mRelativeLayout.findViewById(R.id.type8);
		type1.setOnClickListener(fragmentClick1);
		type2.setOnClickListener(fragmentClick2);
		type3.setOnClickListener(fragmentClick3);
		type4.setOnClickListener(fragmentClick4);
		type6.setOnClickListener(fragmentClick6);
		/*
		 * 没有标签
		 */
		type5.setOnClickListener(fragmentClick5);
		type7.setOnClickListener(fragmentClick7);
		type8.setOnClickListener(fragmentClick8);

		if (timer == null)
			timer = new Timer();
		if (task != null && !isRun) {
			isRun = true;
			timer.schedule(task, 5000, 5000);
		}

		/**
		 * 今日推荐
		 */
		// 计算左右两边宽度
		int hi = (windowWidth - Config.getSize("15")) / 5;
		int leftWidth = hi * 3;
		int rightWidth = hi * 2;

		// 右边整体布局宽高
		right = (LinearLayout) mRelativeLayout.findViewById(R.id.right);
		right.getLayoutParams().height = leftWidth;
		right.getLayoutParams().width = rightWidth;
		// 左边整体布局宽高
		ly1 = (FrameLayout) mRelativeLayout.findViewById(R.id.ly1);
		ly1.getLayoutParams().height = leftWidth;
		ly1.getLayoutParams().width = leftWidth;
		// 右边图片高度
		ly2 = (FrameLayout) mRelativeLayout.findViewById(R.id.ly2);
		ly2.getLayoutParams().height = -1;
		ly3 = (FrameLayout) mRelativeLayout.findViewById(R.id.ly3);
		ly3.getLayoutParams().height = -1;

		ig1 = (ImageView) mRelativeLayout.findViewById(R.id.ig1);
		ig2 = (ImageView) mRelativeLayout.findViewById(R.id.ig2);
		ig3 = (ImageView) mRelativeLayout.findViewById(R.id.ig3);

		ig1.setScaleType(ScaleType.CENTER_CROP);
		ig2.setScaleType(ScaleType.CENTER_CROP);
		ig3.setScaleType(ScaleType.CENTER_CROP);

		tv1 = (TextView) mRelativeLayout.findViewById(R.id.tv1);
		tv2 = (TextView) mRelativeLayout.findViewById(R.id.tv2);
		tv3 = (TextView) mRelativeLayout.findViewById(R.id.tv3);

		// getAlbumList();
		// getRemenList();
	}

	/**
	 * 图片轮播
	 */
	private TimerTask task = new TimerTask() {

		@Override
		public void run() {
			Message message = new Message();
			message.what = 2;
			// index = mGallery.getSelectedItemPosition();
			if (index == dataCoverList.size() - 1) {
				index = 0;
			} else {
				index++;
			}
			handler.sendMessage(message);
		}
	};

	public OnClickListener fragmentClick1 = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, BuyDetailFragment.class);
			intent.putExtra("buy_type", "1");
			intent.putExtra("tabTitle", "童装, 童鞋配饰, 书本玩具, 婴幼用品");
			startActivity(intent);
		}

	};
	public OnClickListener fragmentClick2 = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, BuyDetailFragment.class);
			intent.putExtra("buy_type", "2");
			intent.putExtra("tabTitle", "童装, 童鞋配饰, 书本玩具, 婴幼用品");
			startActivity(intent);
		}

	};
	public OnClickListener fragmentClick3 = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, BuyDetailFragment.class);
			intent.putExtra("buy_type", "3");
			intent.putExtra("tabTitle", "童装, 童鞋配饰, 书本玩具, 婴幼用品");
			startActivity(intent);
		}

	};
	public OnClickListener fragmentClick4 = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, BuyDetailFragment.class);
			intent.putExtra("buy_type", "4");
			intent.putExtra("tabTitle", "童装, 童鞋配饰, 书本玩具, 婴幼用品");
			startActivity(intent);
		}

	};
	public OnClickListener fragmentClick5 = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, BuyDetailList.class);
			intent.putExtra("buy_list", "6");
			startActivity(intent);
		}

	};
	public OnClickListener fragmentClick6 = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, BuyDetailFragment.class);
			intent.putExtra("buy_type", "5");
			intent.putExtra("tabTitle", "服装,美妆,鞋包配饰,辣妈日用");
			startActivity(intent);
		}

	};
	public OnClickListener fragmentClick7 = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, BuyDetailList.class);
			intent.putExtra("buy_list", "7");
			startActivity(intent);
		}

	};
	public OnClickListener fragmentClick8 = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, BuyDetailList.class);
			intent.putExtra("buy_list", "5");
			startActivity(intent);
		}

	};

	public void getAlbumList() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		// android.util.Log.e("fanfan", ServerMutualConfig.BusinessListV6 + "&"
		// + params.toString());
		ab.get(ServerMutualConfig.BusinessListV6 + "&" + params.toString(),
				new AbStringHttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						JSONObject json = null;
						try {
							json = new JSONObject(content);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						try {
							if (json.getBoolean("success")) {
								if (json.getJSONArray("data").length() == 0) {
									Toast.makeText(context, "网络连接错误",
											Toast.LENGTH_SHORT).show();
								} else {
									for (int i = 0; i < json.getJSONArray(
											"data").length(); i++) {
										ActiveCoverList item = new ActiveCoverList();
										item.fromJson(json.getJSONArray("data")
												.getJSONObject(i));
										dataCoverList.add(item);
										mImageAdapter.notifyDataSetChanged();
									}
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				});
	}

	/**
	 * 开一个线程执行耗时操作
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2:
				adapter.notifyDataSetChanged();
				// mGallery.setSelection(index);
				break;
			default:
				break;
			}
		}

	};

	public class ActiveCoverList {
		public String album_img = "";
		public String business_url = "";
		public String img_id = "";
		public String business_id = "";
		JSONObject json;

		public void fromJson(JSONObject json) throws JSONException {
			this.json = json;
			if (json.has("img_id")) {
				img_id = json.getString("img_id");
			}
			if (json.has("business_id")) {
				business_id = json.getString("business_id");
			}
			if (json.has("album_img")) {
				album_img = json.getString("album_img");
			}
			if (json.has("business_url")) {
				business_url = json.getString("business_url");
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
			return dataCoverList.size();// 实现循环显示
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ImageView imageView = new ImageView(context);
			if (dataCoverList.size() > 0) {
				MyApplication.getInstance().display(
						dataCoverList.get(position).album_img, imageView,
						R.drawable.hot_banner);
			} else {
				imageView.setImageResource(R.drawable.hot_banner);
			}
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setLayoutParams(new Gallery.LayoutParams(
					Gallery.LayoutParams.FILL_PARENT,
					Gallery.LayoutParams.MATCH_PARENT));
			RelativeLayout borderImg = new RelativeLayout(context);
			borderImg.addView(imageView);
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					imgId = dataCoverList.get(position).img_id;
					if (!"0".equals(dataCoverList.get(position).business_id)) {
						Intent intent = new Intent();
						intent.setClass(context, BusinessDetailActivity.class);
						intent.putExtra("business_id",
								dataCoverList.get(position).business_id);
						startActivity(intent);
					} else {

						if (!TextUtils.isEmpty(dataCoverList.get(position).business_url)) {
							String strUri;
							strUri = dataCoverList.get(position).business_url;
							if (!strUri.startsWith("http")) {
								strUri = "http://" + strUri;
							}

							Intent intent = new Intent(context,
									WebViewActivity.class);
							intent.putExtra("webviewurl", strUri);
							intent.putExtra("msg", "");
							intent.putExtra("img",
									dataCoverList.get(0).album_img);
							intent.putExtra("webBuy", "webBuy");
							startActivity(intent);
						} else {
						}
					}
				}
			});
			return borderImg;
		}

	}

	private void getValue() {
		app.display(data1.get(0).listImg.get(0).img_thumb, ig1,
				R.drawable.def_image);
		app.display(data1.get(1).listImg.get(0).img_thumb, ig2,
				R.drawable.def_image);
		app.display(data1.get(2).listImg.get(0).img_thumb, ig3,
				R.drawable.def_image);
		ig1.setOnClickListener(igClick);
		ig2.setOnClickListener(igClick);
		ig3.setOnClickListener(igClick);
		tv1.setText("现价¥" + data1.get(0).current_price);
		tv2.setText("现价¥" + data1.get(1).current_price);
		tv3.setText("现价¥" + data1.get(2).current_price);
	}

	public OnClickListener igClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, BuyDetailList.class);
			intent.putExtra("buy_list", data1.get(0).buy_list);
			startActivity(intent);
		}
	};

	private void getRemenList() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		// Log.e("fanfan",
		// ServerMutualConfig.ShowBuyNewList + "&" + params.toString());
		ab.setTimeout(5000);
		ab.get(ServerMutualConfig.ShowBuyNewList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						data1.clear();
						try {
							JSONObject json = new JSONObject(content);
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								BuyItemRecommend item = new BuyItemRecommend();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i), content);
								data1.add(item);
							}
							getValue();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
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
	public void onRefresh() {
		// getAlbumList();
		getRemenList();
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		// Log.e("fanfan", ServerMutualConfig.BuyNewList + "&" +
		// params.toString());
		// ab.setTimeout(5000);
		ab.get(ServerMutualConfig.BuyNewList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						data.clear();
						Config.save(context, content, fileName);
						getlistData(content);
					}

					@Override
					public void onFinish() {
						super.onFinish();
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
	public void onMore() {
		if (data.size() == 0) {
			mPullDownView.notifyDidMore();
			mPullDownView.RefreshComplete();
			return;
		}
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("last_id", data.get(data.size() - 1).buy_list);
		// Log.e("fanfan_fan",
		// ServerMutualConfig.BuyNewList + "&" + params.toString());
		ab.get(ServerMutualConfig.BuyNewList + "&" + params.toString(),
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
								BuyItem item = new BuyItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i), content);
								data.add(item);
							}
							adapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
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

	class ViewHolder {
		TextView theme_name;
		TextView theme_renshu;
		LinearLayout theme_img;
		LinearLayout ly1;
		ImageView img1, img2, img3, img4;
		TextView msg1, msg2, msg3;
		TextView current_price1, current_price2, current_price3; // 现价
		TextView original_price1, original_price2, original_price3; // 原价
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

			convertView = inflater.inflate(R.layout.buy_item, null);
			holder = new ViewHolder();
			holder.theme_name = (TextView) convertView
					.findViewById(R.id.theme_name);
			holder.theme_renshu = (TextView) convertView
					.findViewById(R.id.theme_renshu);
			holder.theme_img = (LinearLayout) convertView
					.findViewById(R.id.theme_img);
			// 图片
			holder.img1 = (ImageView) convertView.findViewById(R.id.img1);
			holder.img2 = (ImageView) convertView.findViewById(R.id.img2);
			holder.img3 = (ImageView) convertView.findViewById(R.id.img3);

			int hi = (windowWidth - Config.getSize("20")) / 3;
			holder.img1.getLayoutParams().height = hi;
			holder.img1.getLayoutParams().width = hi;
			holder.img2.getLayoutParams().height = hi;
			holder.img2.getLayoutParams().width = hi;
			holder.img3.getLayoutParams().height = hi;
			holder.img3.getLayoutParams().width = hi;
			// LinearLayout.LayoutParams param = new
			// LinearLayout.LayoutParams(hi,
			// hi);
			// ((MarginLayoutParams) param).rightMargin = Config.getSize("3");
			// holder.theme_img.addView(holder.img1, param);
			// holder.theme_img.addView(holder.img2, param);
			// holder.theme_img.addView(holder.img3, param);
			// convertView.setTag(holder);

			// 描述
//			holder.msg1 = (TextView) convertView.findViewById(R.id.msg1);
//			holder.msg2 = (TextView) convertView.findViewById(R.id.msg2);
//			holder.msg3 = (TextView) convertView.findViewById(R.id.msg3);
			// 现价
			holder.current_price1 = (TextView) convertView
					.findViewById(R.id.current_price1);
			holder.current_price2 = (TextView) convertView
					.findViewById(R.id.current_price2);
			holder.current_price3 = (TextView) convertView
					.findViewById(R.id.current_price3);
			// 原价
			holder.original_price1 = (TextView) convertView
					.findViewById(R.id.original_price1);
			holder.original_price1.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG); // 删除线
			holder.original_price2 = (TextView) convertView
					.findViewById(R.id.original_price2);
			holder.original_price2.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG); // 删除线
			holder.original_price3 = (TextView) convertView
					.findViewById(R.id.original_price3);
			holder.original_price3.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG); // 删除线
			convertView.setTag(holder);

			final BuyItem item = data.get(position);
			// 主题名称
			holder.theme_name.setText(item.cate_name);
			// 图片
			app.display(item.listImg.get(0).img, holder.img1,
					R.drawable.deft_color);
			app.display(item.listImg.get(1).img, holder.img2,
					R.drawable.deft_color);
			app.display(item.listImg.get(2).img, holder.img3,
					R.drawable.deft_color);
			holder.img1.setScaleType(ScaleType.CENTER_CROP);
			holder.img2.setScaleType(ScaleType.CENTER_CROP);
			holder.img3.setScaleType(ScaleType.CENTER_CROP);
			holder.theme_renshu.setText("查看更多");
			// 描述
//			if (!TextUtils.isEmpty(item.listImg.get(0).img_description)) {
//				holder.msg1.setText(item.listImg.get(0).img_description);
//			}
//			if (!TextUtils.isEmpty(item.listImg.get(1).img_description)) {
//				holder.msg2.setText(item.listImg.get(1).img_description);
//			}
//			if (!TextUtils.isEmpty(item.listImg.get(2).img_description)) {
//				holder.msg3.setText(item.listImg.get(2).img_description);
//			}

			// 现价
			if (!TextUtils.isEmpty(item.listImg.get(0).current_price)) {
				holder.current_price1.setText("¥"
						+ item.listImg.get(0).current_price);
			}
			if (!TextUtils.isEmpty(item.listImg.get(1).current_price)) {
				holder.current_price2.setText("¥"
						+ item.listImg.get(1).current_price);
			}
			if (!TextUtils.isEmpty(item.listImg.get(2).current_price)) {
				holder.current_price3.setText("¥"
						+ item.listImg.get(2).current_price);
			}

			// 原价
			if (!TextUtils.isEmpty(item.listImg.get(0).original_price)) {
				holder.original_price1.setText("¥"
						+ item.listImg.get(0).original_price);
			}
			if (!TextUtils.isEmpty(item.listImg.get(1).original_price)) {
				holder.original_price2.setText("¥"
						+ item.listImg.get(1).original_price);
			}
			if (!TextUtils.isEmpty(item.listImg.get(2).original_price)) {
				holder.original_price3.setText("¥"
						+ item.listImg.get(2).original_price);
			}

			// 点击事件
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(context, BuyDetailList.class);
					intent.putExtra("buy_list", item.buy_list);
					intent.putExtra("cate_name", item.cate_name);
					startActivity(intent);
				}
			});
			return convertView;
		}
	}

	private void getlistData(String content) {
		try {
			JSONObject json = new JSONObject(content);
			for (int i = 0; i < json.getJSONArray("data").length(); i++) {
				BuyItem item = new BuyItem();
				item.fromJson(json.getJSONArray("data").getJSONObject(i),
						content);
				data.add(item);
			}
			if (data.isEmpty()) {
				mPullDownView.setVisibility(View.GONE);
			}
			adapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}

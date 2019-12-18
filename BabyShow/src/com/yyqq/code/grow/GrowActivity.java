package com.yyqq.code.grow;

import java.io.File;
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
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.mob.MobSDK;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.login.AddBaby;
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.code.main.MainTab;
import com.yyqq.code.photo.CropImage;
import com.yyqq.code.photo.PhoneAlbumActivity;
import com.yyqq.commen.model.BabyDataItem;
import com.yyqq.commen.model.BabyItem;
import com.yyqq.commen.model.DiaryItem;
import com.yyqq.commen.utils.BimpUtil;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class GrowActivity extends Activity implements OnPullDownListener {

	private Activity context;
	private String TAG = "fanfan_GrowActivity";
	private AbHttpUtil ab;
	private PullDownView mPullDownView;
	private ListView listview;
	private MyAdapter adapter;
	private ImageView add_img;
	private ArrayList<DiaryItem> dataDiaryList = new ArrayList<DiaryItem>();
	private MyApplication app;
	private String fileName = "GrowActivity.txt";
	private MyApplication myMyApplication;
	private ArrayList<BabyItem> data = new ArrayList<BabyItem>();
	private JSONArray data_json;
	private LinearLayout title_lay;
	private ImageView kid_head;
	private TextView kid_name;
	private TextView kid_birth;
	private ListView popList;
	private String Baby_id = "";
	private int page;
	public static int maxSize = 20;
	private int index = 0;// 记录选中的图片位置
	private static int indexitem = 0;// 记录选中的图片位置
	private boolean indexitemfla;// 记录选中的图片位置
	private PopupWindowsPicture mPopWindowPicture;
	private PopupWindows mPopWindowShaixuan;
	private Dialog dialog;
	Bitmap image;
	private static int mBabynum;
	private boolean isVistor;
	private RelativeLayout none;
	private ImageView login_regist;
	private ImageView share;
	private String babyName = "";
	private String babyBirth = "";
	private String post_img = "";
	private ImageView bg_img;
	private int windowWidth;
	private int hi;
	private int wline;
	private int hline;
	private RelativeLayout title;

	/** 头部view **/
	private RelativeLayout mRelativeLayout;
	private LayoutInflater inflater;
	private FrameLayout frameLayout;
	private TextView bt_width, bt_height;
	private TextView remind_info;
	private Button bt_inset;
	private TextView scale1, scale2, scale3, scale4, scale5;
	private BabyDataItem item;
	private TextView Line;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return Config.onKeyDown(keyCode, event, this);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		
		// 修改底部栏文字颜色
		MainTab.iconText.get(0).setTextColor(Color.parseColor("#666666"));
		MainTab.iconText.get(1).setTextColor(Color.parseColor("#666666"));
		MainTab.iconText.get(2).setTextColor(Color.parseColor("#fe6363"));
		MainTab.iconText.get(3).setTextColor(Color.parseColor("#666666"));
		
		isVistor = "1".equals(myMyApplication.getVisitor());
		if (isVistor || Config.getUser(context).uid.equals("")) {
			setContentView(R.layout.grow1);
		} else {
			setContentView(R.layout.grow);
			init();
		}
		
		if (!isVistor) {
			getBaby();
			if (indexitemfla) {
				indexitemfla = false;
				adapter.notifyDataSetChanged();
				listview.setSelection(indexitem);
			}
		} else {
			isVistor = isVistor();
		}
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		context = this;
		MobSDK.init(this);
		myMyApplication = (MyApplication) context.getApplication();
		isVistor = "1".equals(myMyApplication.getVisitor());
		if (isVistor || Config.getUser(context).uid.equals("")) {
			setContentView(R.layout.grow1);
		} else {
			setContentView(R.layout.grow);
			init();
		}
	}

	AlertDialog.Builder dialogBuilder;

	private boolean isVistor() {
		none = (RelativeLayout) findViewById(R.id.none);
		login_regist = (ImageView) findViewById(R.id.login_regist);
		login_regist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				myMyApplication.setVisitor("");
				Intent intent = new Intent();
				intent.setClass(context, WebLoginActivity.class);
				startActivity(intent);
			}
		});
		return true;
	}

	private void init() {
		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		windowWidth = DM.widthPixels;
		ab = AbHttpUtil.getInstance(context);
		initView();
		title = (RelativeLayout) findViewById(R.id.title);
		title.setBackgroundResource(R.color.title_bg);
		bg_img = (ImageView) findViewById(R.id.bg_img);
		bg_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context,
						AddBaby.class);
				intent.putExtra("grow",
						"grow");
				startActivity(intent);
			}
		});
		app = (MyApplication) this.getApplication();
		mPullDownView = (PullDownView) findViewById(R.id.list);
		mPullDownView.setOnPullDownListener(this);
		listview = mPullDownView.getListView();
		mPullDownView.setShowHeaderLayout(mRelativeLayout);
		listview.setDivider(null);
		listview.setDividerHeight(20);
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
		// listview.addHeaderView(mGallery);
		// 显示并且可以使用头部刷新
		mPullDownView.setShowHeader();
		// mPullDownView.setPadding(0, 0, 0, 0);
		if (!Config.isConn(context)) {
			try {
				getlistData(Config.read(context, fileName));
			} catch (Exception e) {
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TAKE_PICTURE:
			Intent intent2 = new Intent();
			intent2.setClass(context, CropImage.class);
			intent2.putExtra("tag", "isGrow");
			intent2.putExtra("baby_id", Baby_id);
			intent2.putExtra("path", Config.ImageFile + path);
			startActivityForResult(intent2, IMAGE_CROP_RETURN);
			break;
		case 1:
			indexitemfla = true;
			listview.setSelection(indexitem);
			// Intent intent = new Intent();
			// intent.setClass(context, MainTab.class);
			// intent.putExtra("tabid", 1);
			// startActivity(intent);
			// context.finish();
			break;
		case IMAGE_CROP_RETURN:
			break;
		default:
			break;
		}
		if (requestCode == Config.IMAGE_IMAGE_RESULT) {
			if (data == null)
				return;
			Uri uri = data.getData();
			Config.cropImageUri(context, uri, 640, 640, Config.CROP_BIG_PICTURE);
			// image = BitmapFactory.decodeFile(Config.ImageFile
			// + "temp_image.jpg");
		} else if (requestCode == Config.IMAGE_CAMERA_RESULT) {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			int imgWidth = 0;
			int imgHeight = 0;
			Bitmap b = BitmapFactory.decodeFile(Config.ImageFile
					+ "temp_image.jpg", opts);
			imgWidth = opts.outWidth;
			imgHeight = opts.outHeight;
			opts = null;
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			Config.setNewopts(context, newOpts, imgWidth, imgHeight);
			b = BitmapFactory.decodeFile(Config.ImageFile + "temp_image.jpg",
					newOpts);
			if (b == null)
				return;
			Config.cropImageUri(
					context,
					Uri.parse(MediaStore.Images.Media.insertImage(
							context.getContentResolver(), b, null, null)), 640,
					640, Config.CROP_BIG_PICTURE);
		} else if (requestCode == Config.CROP_BIG_PICTURE) {
			Bitmap image = BitmapFactory.decodeFile(Config.ImageFile
					+ "temp_image.jpg");
			kid_head.setImageBitmap(image);
			String head = Config.ImageFile + "temp_image.jpg";
			upHead(head);
		}
	}

	// 上传头像的请求
	private void upHead(String head) {
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
		params.put("baby_id", Baby_id);
		params.put("avatar", new File(head));
		// android.util.Log.e("fanfan", params.toString());
		ab.post(ServerMutualConfig.AddBabyAvatar, params,
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						getBaby();
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

	private void initView() {
		inflater = LayoutInflater.from(context);
		/** 头部的view **/
		initHeadbar();
		title_lay = (LinearLayout) findViewById(R.id.title_lay);
		title_lay.setOnClickListener(babyClick);
		kid_head = (ImageView) findViewById(R.id.kid_head);
		kid_name = (TextView) findViewById(R.id.kid_name);
		kid_birth = (TextView) findViewById(R.id.kid_birth);
		add_img = (ImageView) findViewById(R.id.add_img);
		share = (ImageView) findViewById(R.id.share);
		share.setOnClickListener(ShareClick);
		add_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (data.isEmpty()) {
					checkChild();
				}else{
					mPopWindowPicture = new PopupWindowsPicture(context, add_img);
				}
			}
		});
	}

	// 分享
	public OnClickListener ShareClick = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (data.isEmpty()) {
				checkChild();
			}else{
				showShare();
			}
		}
	};

	public OnClickListener addHeadClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = new Builder(context);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View convertView = inflater.inflate(R.layout.image_dialog, null);
			builder.setTitle("添加头像");
			builder.setView(convertView);
			TextView image = (TextView) convertView.findViewById(R.id.image);
			TextView camera = (TextView) convertView.findViewById(R.id.camera);
			image.setOnClickListener(selectImageClick);
			camera.setOnClickListener(selectCameraClick);
			dialog = builder.create();
			dialog.show();
		}
	};

	/**
	 * 上传头像
	 */
	public OnClickListener selectImageClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (dialog != null && dialog.isShowing()) {
				Config.deleteFile(Config.ImageFile + "temp_image.jpg");
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, Config.IMAGE_IMAGE_RESULT);
				dialog.dismiss();
			}
		}
	};
	public OnClickListener selectCameraClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (dialog != null && dialog.isShowing()) {
				Config.deleteFile(Config.ImageFile + "temp_image.jpg");
				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
						Config.imageUri);
				startActivityForResult(i, Config.IMAGE_CAMERA_RESULT);
				dialog.dismiss();
			}
		}
	};

	/**
	 * 获取宝宝列表
	 */
	public void getBaby() {
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
		// Log.e("fanfan", ServerMutualConfig.GetBabys + "&" +
		// params.toString());
		ab.get(ServerMutualConfig.GetBabys + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								data_json = new JSONArray();
								data.clear();
								for (int i = 0; i < json.getJSONArray("data")
										.length(); i++) {
									BabyItem item = new BabyItem();
									item.fromJson(json.getJSONArray("data")
											.getJSONObject(i));
									data.add(item);
								}
								if (data.isEmpty()) {
									mPullDownView.setVisibility(View.GONE);
									bg_img.setVisibility(View.VISIBLE);
									title.setBackgroundResource(R.drawable.title_bg_tou);
									MyApplication.getInstance().display("",
											kid_head, R.drawable.icon);
									// kid_name.setText("");
									// kid_birth.setText("");
									dataDiaryList.clear();
									adapter.notifyDataSetChanged();
								} else {
									if (mBabynum < data.size()) {
										MyApplication.getInstance().display(
												data.get(mBabynum).avatar,
												kid_head, R.drawable.icon);
										post_img = data.get(mBabynum).avatar;
										kid_name.setText(data.get(mBabynum).baby_name);
										babyName = data.get(mBabynum).baby_name;
										kid_birth.setText(data.get(mBabynum).age);
										babyBirth = data.get(mBabynum).age;
										Baby_id = data.get(mBabynum).id;
										onRefresh();
									}
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

	public OnClickListener babyClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (data.isEmpty()) {
				checkChild();
			}else{
				mPopWindowShaixuan = new PopupWindows(context, title_lay);
			}
		}
	};

	public class PopupWindows extends PopupWindow {
		public PopupWindows(Context mContext, View parent) {
			View view = View
					.inflate(mContext, R.layout.baby_popupwindows, null);
			popList = (ListView) view.findViewById(R.id.popList);
			setWidth(LayoutParams.WRAP_CONTENT);
			setHeight(LayoutParams.WRAP_CONTENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAsDropDown(parent);
			popList.setAdapter(new PopupAdapter());
			update();
		}

	}

	class PopupAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return data.size() + 3;
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			ViewHolder1 holder1 = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.grow_baby_item, null);
				holder1 = new ViewHolder1();
				holder1.baby_name = (TextView) convertView
						.findViewById(R.id.baby_name);
				convertView.setTag(holder1);

			} else {
				holder1 = (ViewHolder1) convertView.getTag();
			}

			if (position == getCount() - 3) {
				holder1.baby_name.setText("添加宝宝");
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(context, AddBaby.class);
						intent.putExtra("grow", "grow");
						startActivity(intent);
						mPopWindowShaixuan.dismiss();
					}
				});
				return convertView;
			}

			if (position == getCount() - 2) {
				holder1.baby_name.setText("和另外一成长记录同步");
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(context, MyShareList.class);
						intent.putExtra("baby_id", Baby_id);
						startActivity(intent);
						mPopWindowShaixuan.dismiss();
					}
				});
				return convertView;
			}

			if (position == getCount() - 1) {
				holder1.baby_name.setText("添加关注");
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(context, GrowSearch.class);
						startActivity(intent);
						mPopWindowShaixuan.dismiss();
					}
				});
				return convertView;
			}

			holder1.baby_name.setText(data.get(position).baby_name);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					MyApplication.getInstance().display(
							data.get(position).avatar, kid_head,
							R.drawable.icon);
					kid_name.setText(data.get(position).baby_name);
					kid_birth.setText(data.get(position).age);
					Baby_id = data.get(position).id;
					mBabynum = position;
					mPopWindowShaixuan.dismiss();
					Config.showProgressDialog(context, false, null);
					onRefresh();
				}
			});

			return convertView;
		}

	}

	class ViewHolder1 {
		TextView baby_name;
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
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void onRefresh() {
		Config.showProgressDialog(context, true, null);
		if ("1".equals(data.get(mBabynum).idol_type)) {
			share.setVisibility(View.GONE);
			add_img.setVisibility(View.GONE);
			kid_head.setOnClickListener(null);
		} else {
			share.setVisibility(View.VISIBLE);
			add_img.setVisibility(View.VISIBLE);
			kid_head.setOnClickListener(null);
			kid_head.setOnClickListener(addHeadClick);
		}
		getBabyData();
		page = 1;
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
		params.put("baby_id", Baby_id);
		params.put("page", page + "");
		// Log.e("fanfan",
		// ServerMutualConfig.DiaryAlbumList + "&" + params.toString());
		ab.setTimeout(5000);
		ab.get(ServerMutualConfig.DiaryAlbumList + "&" + params.toString(),
				new AbStringHttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Config.dismissProgress();
						Config.save(context, content, fileName);
						getlistData(content);
					}

					@Override
					public void sendSuccessMessage(int statusCode,
							String content) {
						super.sendSuccessMessage(statusCode, content);
						mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
						mPullDownView.notifyDidMore();
					}

				});
	}

	@Override
	public void onMore() {
		if (dataDiaryList.size() == 0) {
			mPullDownView.notifyDidMore();
			mPullDownView.RefreshComplete();
			return;
		}
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
		params.put("baby_id", Baby_id);
		params.put("page", (++page) + "");
		ab.get(ServerMutualConfig.DiaryAlbumList + "&" + params.toString(),
				new AbStringHttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						Config.dismissProgress();
						try {
							JSONObject json = new JSONObject(content);
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								DiaryItem item = new DiaryItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i), content);
								dataDiaryList.add(item);
							}
							adapter.notifyDataSetChanged();
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

	class ViewHolder {
		LinearLayout View0;
		LinearLayout View1;
		LinearLayout View2;
		LinearLayout View3;
		TextView view1_msg;
		TextView view2_msg;
		TextView birth;
		TextView kid_birth;
		ImageView grow_jie;
		ImageView grow_suo;
		TextView time;
		TextView biao;
		ImageView grow_img;
		TextView grow_biao_num;
		TextView msg;
	}

	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dataDiaryList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return dataDiaryList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int index, View convertView, ViewGroup arg2) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.grow_item, null);
			ViewHolder holder = null;
			holder = new ViewHolder();
			final DiaryItem item = dataDiaryList.get(index);
			holder.View0 = (LinearLayout) convertView.findViewById(R.id.View0);
			holder.View1 = (LinearLayout) convertView.findViewById(R.id.View1);
			holder.View2 = (LinearLayout) convertView.findViewById(R.id.View2);
			holder.View3 = (LinearLayout) convertView.findViewById(R.id.View3);
			holder.view1_msg = (TextView) convertView
					.findViewById(R.id.view1_msg);
			holder.view2_msg = (TextView) convertView
					.findViewById(R.id.view2_msg);
			holder.birth = (TextView) convertView.findViewById(R.id.birth);
			holder.kid_birth = (TextView) convertView
					.findViewById(R.id.kid_birth);
			holder.grow_jie = (ImageView) convertView
					.findViewById(R.id.grow_jie);
			holder.grow_suo = (ImageView) convertView
					.findViewById(R.id.grow_suo);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.biao = (TextView) convertView.findViewById(R.id.biao);
			holder.grow_img = (ImageView) convertView
					.findViewById(R.id.grow_img);
			holder.grow_biao_num = (TextView) convertView
					.findViewById(R.id.grow_biao_num);
			holder.msg = (TextView) convertView.findViewById(R.id.msg);
			// 样式显示
			if ("1".equals(item.tag_type)) {
				holder.View0.setVisibility(View.GONE);
				holder.View1.setVisibility(View.VISIBLE);
				holder.View2.setVisibility(View.GONE);
				holder.View3.setVisibility(View.GONE);
			} else if ("2".equals(item.tag_type)) {
				holder.View0.setVisibility(View.GONE);
				holder.View1.setVisibility(View.GONE);
				holder.View2.setVisibility(View.VISIBLE);
				holder.View3.setVisibility(View.GONE);
			} else if ("3".equals(item.tag_type)) {
				holder.View0.setVisibility(View.GONE);
				holder.View1.setVisibility(View.GONE);
				holder.View2.setVisibility(View.GONE);
				holder.View3.setVisibility(View.VISIBLE);
				holder.birth.setText(item.album_name);
				holder.kid_birth.setText(item.album_description);
			} else if ("0".equals(item.tag_type)) { // 小节点
				holder.View0.setVisibility(View.VISIBLE);
				holder.View1.setVisibility(View.GONE);
				holder.View2.setVisibility(View.GONE);
				holder.View3.setVisibility(View.GONE);
				holder.grow_jie
						.setBackgroundResource(R.drawable.grow_jie_small);
				holder.biao.setVisibility(View.GONE);
			} else if ("4".equals(item.tag_type)) { // 大节点
				holder.View0.setVisibility(View.VISIBLE);
				holder.View1.setVisibility(View.GONE);
				holder.View2.setVisibility(View.GONE);
				holder.View3.setVisibility(View.GONE);
				holder.grow_jie.setBackgroundResource(R.drawable.grow_jie_big);
				holder.biao.setText(item.tag_name);
			}
			holder.view1_msg.setText(item.album_name);
			holder.view2_msg.setText(item.album_name);
			// 权限
			if ("1".equals(item.diary_cate)) {
				holder.grow_suo.setVisibility(View.VISIBLE);
			}
			// 时间
			holder.time.setText(item.album_name);
			// 图片
			String string = "";
			if (item.imaStrings.size() == 0) {
				holder.grow_img.setVisibility(View.GONE);
				holder.grow_biao_num.setVisibility(View.GONE);
				holder.msg.setVisibility(View.GONE);
			} else if (item.imaStrings.size() > 0) {
				string = item.imaStrings.get(0);
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						if ("1".equals(data.get(mBabynum).idol_type)) {
							intent.setClass(context, Grow_Synchronize.class);
						} else {
							intent.setClass(context, Grow_Detail.class);
						}
						intent.putExtra("baby_id", Baby_id);
						intent.putExtra("album_id", item.album_id);
						intent.putExtra("user_id", Config.getUser(context).uid);
						intent.putExtra("grow_detail_title", item.album_name);
						intent.putExtra("mTag_name", item.tag_name);
						indexitem = listview.getFirstVisiblePosition();
						context.startActivityForResult(intent, 12345);
					}
				});

			}
			app.display(string, holder.grow_img, R.drawable.tutu);
			holder.grow_biao_num.setText(item.img_count + "张");

			// 描述
			if (TextUtils.isEmpty(item.album_description)) {
				holder.msg.setVisibility(View.GONE);
			} else {
				Config.getLinkString(holder.msg, item.album_description, null,
						context);
			}
			return convertView;
		}

	}

	/* 选择图片的pop */
	public class PopupWindowsPicture extends PopupWindow {
		public PopupWindowsPicture(Context mContext, View parent) {
			Button Photo, camera, cancel;
			View view = View.inflate(mContext, R.layout.grow_item_popupwindows,
					null);
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
						intent.putExtra("tag", "isGrow");
						intent.putExtra("baby_id", Baby_id);
						intent.putExtra(Config.SELECTNAME, maxSize);
						startActivity(intent);
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
		}
	}

	private static int imgNum;
	private String path = "";
	private static final int TAKE_PICTURE = 0x000000;
	private static final int IMAGE_CROP_RETURN = 4;

	public void photo() {
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		imgNum++;
		path = "temp_image_" + imgNum + ".jpg";
		Uri imageUri = Uri.parse("file:///sdcard/yyqq/babyshow/image/" + path);
		i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(i, TAKE_PICTURE);
	}

	private boolean isFallBit() {

		if (BimpUtil.drr.size() >= maxSize) {
			return false;
		}
		return true;
	}

	private void showShare() {
		MobSDK.init(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		oks.setSilent(true);
//		oks.setNotification(R.drawable.icon, getString(R.string.app_name)); // 分享时Notification的图标和文字
		// 去除注释，演示在九宫格设置自定义的图标
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				Config.showProgressDialog(context, false, null);
				FinalHttp fh = new FinalHttp();
				AjaxParams params = new AjaxParams();
				params.put("album_id", getIntent().getStringExtra("album_id"));
				// android.util.Log.e("fanfan", params.toString());
				fh.post(ServerMutualConfig.ShareToListing, params,
						new AjaxCallBack<String>() {

							@Override
							public void onFailure(Throwable t, String strMsg) {
								Config.dismissProgress();
								Config.showFiledToast(context);
								super.onFailure(t, strMsg);
							}

							@Override
							public void onSuccess(String t) {
								super.onSuccess(t);
								Config.dismissProgress();
								Toast.makeText(context, "已分享到话题", 0).show();
								String str = "";
								try {
									JSONObject json = new JSONObject(t);
									str = json.getString("reMsg");
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

						});
			}
		};
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {

			@Override
			public void onShare(Platform platform, ShareParams paramsToShare) {
				if ("Wechat".equals(platform.getName())) {
					paramsToShare.setShareType(Platform.SHARE_TEXT);
					paramsToShare.setText(babyName + "现在" + babyBirth
							+ "了，宝宝秀秀记录了" + babyName + "的成长轨迹 ");
					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
					if (TextUtils.isEmpty(post_img)) {
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(post_img);
					}
					paramsToShare
							.setUrl("http://www.meimei.yihaoss.top/anim/share_diary.php?user_id="
									+ Config.getUser(context).uid
									+ "&baby_id="
									+ Baby_id);
				} else if ("SinaWeibo".equals(platform.getName())) {
					paramsToShare
							.setText(babyName
									+ "现在"
									+ babyBirth
									+ "了，宝宝秀秀记录了"
									+ babyName
									+ "的成长轨迹 "
									+ "http://www.meimei.yihaoss.top/anim/share_diary.php?user_id="
									+ Config.getUser(context).uid + "&baby_id="
									+ Baby_id);
					if (TextUtils.isEmpty(post_img)) {
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(post_img);
					}
					// android.util.Log.e("fanfan",
					// "http://www.meimei.yihaoss.top/anim/share_diary.php?user_id="
					// + Config.getUser(context).uid + "&baby_id="
					// + Baby_id);
				} else if ("QZone".equals(platform.getName())) {
					paramsToShare.setTitle(""); // title微信、QQ空间
					paramsToShare
							.setTitleUrl("http://www.meimei.yihaoss.top/anim/share_diary.php?user_id="
									+ Config.getUser(context).uid
									+ "&baby_id="
									+ Baby_id);
					paramsToShare.setSite(getString(R.string.app_name)); // site是分享此内容的网站名称，仅在QQ空间使用
					paramsToShare.setText(babyName + "现在" + babyBirth
							+ "了，宝宝秀秀记录了" + babyName + "的成长轨迹 ");
					if (TextUtils.isEmpty(post_img)) {
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(post_img);
					}
					paramsToShare
							.setSiteUrl("http://www.meimei.yihaoss.top/anim/share_diary.php?user_id="
									+ Config.getUser(context).uid
									+ "&baby_id="
									+ Baby_id); // siteUrl是分享此内容的网站地址，仅在QQ空间使用
				} else if ("QQ".equals(platform.getName())) {
					paramsToShare.setTitle("");
					paramsToShare
							.setTitleUrl("http://www.meimei.yihaoss.top/anim/share_diary.php?user_id="
									+ Config.getUser(context).uid
									+ "&baby_id="
									+ Baby_id);
					paramsToShare.setText(babyName + "现在" + babyBirth
							+ "了，宝宝秀秀记录了" + babyName + "的成长轨迹 ");
					if (TextUtils.isEmpty(post_img)) {
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(post_img);
					}
				} else if ("WechatMoments".equals(platform.getName())) {
					paramsToShare.setShareType(Platform.SHARE_TEXT);
					paramsToShare.setTitle(babyName + "现在" + babyBirth
							+ "了，宝宝秀秀记录了" + babyName + "的成长轨迹 ");
					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
					if (TextUtils.isEmpty(post_img)) {
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(post_img);
					}
					paramsToShare
							.setUrl("http://www.meimei.yihaoss.top/anim/share_diary.php?user_id="
									+ Config.getUser(context).uid
									+ "&baby_id="
									+ Baby_id);
				}
			}
		});
		// 启动分享GUI
		oks.show(this);
	}

	private void getlistData(String content) {
		try {
			dataDiaryList.clear();
			JSONObject json = new JSONObject(content);
			for (int i = 0; i < json.getJSONArray("data").length(); i++) {
				DiaryItem item = new DiaryItem();
				item.fromJson(json.getJSONArray("data").getJSONObject(i),
						content);
				dataDiaryList.add(item);
			}
			adapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 头部的view
	 */
	@SuppressLint("NewApi")
	private void initHeadbar() {
		mRelativeLayout = (RelativeLayout) inflater.inflate(
				R.layout.head_bar_grow, null);
		remind_info = (TextView) mRelativeLayout.findViewById(R.id.remind_info);
		frameLayout = (FrameLayout) mRelativeLayout
				.findViewById(R.id.frameLayout);
		hi = windowWidth / 2;
		frameLayout.getLayoutParams().height = hi;
		int wi = (int) (windowWidth / 2.58997);
		int w4 = (int) (windowWidth / 1.54242999);
		int w5 = (int) (windowWidth / 1.1486910);
		int hi1 = (int) (hi / 7.1157);
		int hi2 = (int) (hi / 2.1381);
		int hi3 = (int) (hi / 1.35199999);
		int hi4 = (int) (hi / 2.374988997);
		int hi5 = (int) (hi / 1.8119999);
		scale1 = (TextView) mRelativeLayout.findViewById(R.id.scale1);
		scale2 = (TextView) mRelativeLayout.findViewById(R.id.scale2);
		scale3 = (TextView) mRelativeLayout.findViewById(R.id.scale3);
		scale4 = (TextView) mRelativeLayout.findViewById(R.id.scale4);
		scale5 = (TextView) mRelativeLayout.findViewById(R.id.scale5);
		Line = (TextView) mRelativeLayout.findViewById(R.id.line_xu);
		scale1.setPadding(wi, hi1, 0, 0);
		scale2.setPadding(wi, hi2, 0, 0);
		scale3.setPadding(wi, hi3, 0, 0);
		scale4.setPadding(w4, hi4, 0, 0);
		scale5.setPadding(w5, hi5, 0, 0);
		bt_inset = (Button) mRelativeLayout.findViewById(R.id.bt_inset);
		bt_inset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(context, InsertHWActivity.class);
				intent.putExtra("baby_id", Baby_id);
				intent.putExtra("baby_height", item.height);
				intent.putExtra("baby_weight", item.weight);
				startActivity(intent);
			}
		});
		bt_width = (TextView) mRelativeLayout.findViewById(R.id.bt_width);
		bt_width.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				scale1.setText("(" + item.h950 + "cm)");
				scale2.setText("(" + item.h_mid + "cm)");
				scale3.setText("(" + item.h50 + "cm)");
				scale4.setText(item.h_cal);
				scale5.setText(item.height);
				remind_info.setText(item.remind_info);
				if (!TextUtils.isEmpty(item.h_posi)) {
					Line.setVisibility(View.VISIBLE);
					hline = (int) (hi * Float.parseFloat(item.h_posi) - Config
							.getSize("10.5"));
					Line.setPadding(wline, hline, 0, 0);
				} else {
					Line.setVisibility(View.GONE);
				}
				frameLayout.setBackgroundResource(R.drawable.grow_height_bg);
				bt_width.setBackgroundResource(R.drawable.grow_weight_bt_sel);
				bt_width.setTextColor(Color.parseColor("#ffffff"));
				bt_height.setBackgroundResource(R.drawable.grow_weight_bt);
				bt_height.setTextColor(Color.parseColor("#333333"));
			}
		});
		bt_height = (TextView) mRelativeLayout.findViewById(R.id.bt_height);
		bt_height.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				scale1.setText("(" + item.w950 + "kg)");
				scale2.setText("(" + item.w_mid + "kg)");
				scale3.setText("(" + item.w50 + "kg)");
				scale4.setText(item.w_cal);
				scale5.setText(item.weight);
				remind_info.setText(item.remind_info);
				if (!TextUtils.isEmpty(item.w_posi)) {
					Line.setVisibility(View.VISIBLE);
					hline = (int) (hi * Float.parseFloat(item.w_posi) - Config
							.getSize("10.5"));
					Line.setPadding(wline, hline, 0, 0);
				} else {
					Line.setVisibility(View.GONE);
				}

				frameLayout.setBackgroundResource(R.drawable.grow_weight_bg);
				bt_height.setBackgroundResource(R.drawable.grow_weight_bt_sel);
				bt_height.setTextColor(Color.parseColor("#ffffff"));
				bt_width.setBackgroundResource(R.drawable.grow_weight_bt);
				bt_width.setTextColor(Color.parseColor("#333333"));
			}
		});
	}

	public void getBabyData() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("baby_id", Baby_id);
		// android.util.Log.e(TAG,
		// ServerMutualConfig.GrowthList + "&" + params.toString());
		ab.get(ServerMutualConfig.GrowthList + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							item = new BabyDataItem();
							item.fromJson(json.getJSONObject("data"));
							scale1.setText("(" + item.h950 + "cm)");
							scale2.setText("(" + item.h_mid + "cm)");
							scale3.setText("(" + item.h50 + "cm)");
							scale4.setText(item.h_cal);
							scale5.setText(item.height);
							remind_info.setText(item.remind_info);
							// 线
							wline = (int) (hi / 1.477661157);
							if (!TextUtils.isEmpty(item.h_posi)) {
								Line.setVisibility(View.VISIBLE);
								hline = (int) (hi
										* Float.parseFloat(item.h_posi) - Config
										.getSize("10.5"));
								Line.setPadding(wline, hline, 0, 0);
							} else {
								Line.setVisibility(View.GONE);
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
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
						Config.showFiledToast(context);
					}
				});
	}
	
	public void checkChild(){
		mPullDownView.setVisibility(View.GONE);
		bg_img.setVisibility(View.VISIBLE);
		MyApplication.getInstance().display("",
				kid_head, R.drawable.icon);
		// kid_name.setText("");
		// kid_birth.setText("");
		dataDiaryList.clear();
		adapter.notifyDataSetChanged();
		Builder builder = new Builder(context);
		builder.setMessage("您还没有添加宝宝信息，去添加一下吧！");
		builder.setNegativeButton(
				"添加宝宝信息",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(
							DialogInterface dialog,
							int which) {
						Intent intent = new Intent();
						intent.setClass(context,
								AddBaby.class);
						intent.putExtra("grow",
								"grow");
						startActivity(intent);
					}
				});
		builder.setNeutralButton("稍后", null);
		builder.create().show();
	}

}

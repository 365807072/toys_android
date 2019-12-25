package com.yyqq.code.postbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.mob.MobSDK;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.PhotoManager;
import com.yyqq.code.personal.PersonalCenterActivity;
import com.yyqq.code.personal.UserInfo;
import com.yyqq.code.photo.BitmapCache;
import com.yyqq.code.photo.ChangePhotoSize;
import com.yyqq.code.photo.CropImage;
import com.yyqq.code.photo.PhoneAlbumActivity;
import com.yyqq.code.photo.PhotoEdit;
import com.yyqq.commen.model.PostShowItem;
import com.yyqq.commen.model.PostShowItem.Image;
import com.yyqq.commen.model.PostShowItem.Review;
import com.yyqq.commen.utils.BimpUtil;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.FaceConversionUtil;
import com.yyqq.commen.utils.FileUtils;
import com.yyqq.commen.utils.ImageDownloader;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.utils.OnImageDownload;
import com.yyqq.commen.utils.Rank;
import com.yyqq.commen.utils.RoundAngleImageView;
import com.yyqq.commen.utils.WebViewActivity;
import com.yyqq.commen.view.FaceRelativeLayout;
import com.yyqq.commen.view.MyGridView;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * （弃用于2016.08.24）
 * */
public class PostBarDetail extends Activity implements OnPullDownListener {
	private String TAG = "PostShow_Detail";
	private PullDownView mPullDownView;
	private ListView listview;
	private RelativeLayout title;
	private Activity context;
	private ArrayList<PostShowItem> dataPostShow = new ArrayList<PostShowItem>();
	private MyAdapter adapter;
	private int width, height;
	private AbHttpUtil ab;
	private String uid = "";
	private String is_save = "";
	private ImageView collection;
	private boolean collectionCheck;
	private String root_img_id = "";
	private String back = "";
	private ImageView louzhu;
	private boolean isLouzhu;
	private String mUrlString;
	private ImageButton faceBtn, picture;
	private FaceRelativeLayout custom_face_layout;
	private EditText edit_review;
	private ImageButton reviewBtn;
	private GridView pictureGrideView;
	private PictureAdapter pictureAdapter;
	private String imgId;
	private String userId;
	public static String msgString = "";
	private int babyShow = 1;
	private MyApplication myMyApplication;
	private RelativeLayout bottom_root;
	private int is_sort = 0;
	private ImageView sort;
	private ImageView post_more;
	private String post_title = "";
	private String post_img = "";
	private String post_text = "";

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((custom_face_layout.view.getVisibility() == View.VISIBLE)
				|| pictureGrideView.getVisibility() == View.VISIBLE) {
			custom_face_layout.hideFaceView();
			pictureGrideView.setVisibility(View.GONE);
			return false;
		} else if ("MyCollection".equals(getIntent().getStringExtra(
				"MyCollection"))) {
			context.finish();
			return false;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			context.finish();
			return false;
		}
		return false;
	}
	
	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		MobSDK.init(this);
		setContentView(R.layout.postshow_detail);

		init();
		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		width = DM.widthPixels;
		height = DM.heightPixels;
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
		mPullDownView.enableAutoFetchMore(true, 1);
		// 隐藏 并禁用尾部
		// mPullDownView.setHideFooter();
		// 显示并启用自动获取更多
		mPullDownView.setShowFooter();
		// 隐藏并且禁用头部刷新
		// mPullDownView.setHideHeader();
		// 显示并且可以使用头部刷新
		mPullDownView.setShowHeader();
		Config.showProgressDialog(context, false, null);
		dataPostShow.clear();
		onRefresh();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if ("tan".equals(this.getIntent().getStringExtra("tan"))) {

			if (pictureGrideView.getVisibility() == View.VISIBLE) {
				pictureGrideView.setVisibility(View.GONE);
			} else {
				pictureGrideView.setVisibility(View.VISIBLE);
				if (custom_face_layout.view.getVisibility() == View.VISIBLE) {
					custom_face_layout.hideFaceView();
				}
			}

		}
		edit_review.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (pictureGrideView.getVisibility() == View.VISIBLE) {
					pictureGrideView.setVisibility(View.GONE);
				}
				if (custom_face_layout.view.getVisibility() == View.VISIBLE) {
					custom_face_layout.hideFaceView();
				}
			}
		});
		edit_review.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if (arg1) {
					if (pictureGrideView.getVisibility() == View.VISIBLE) {
						pictureGrideView.setVisibility(View.GONE);
					}
					if (custom_face_layout.view.getVisibility() == View.VISIBLE) {
						custom_face_layout.hideFaceView();
					}
				}
			}
		});
		pictureGrideView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 0) {
					if (arg2 == 4) {
						Toast.makeText(context, "最多6张", 1).show();
					} else {
						new PopupWindowsPicture(PostBarDetail.this,
								pictureGrideView);
						InputMethodManager inputMethodManager = (InputMethodManager) context
								.getApplicationContext().getSystemService(
										Context.INPUT_METHOD_SERVICE);
						inputMethodManager.hideSoftInputFromWindow(
								edit_review.getWindowToken(), 0);
					}

				}

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TAKE_PICTURE:
			Intent intent2 = new Intent();
			intent2.setClass(context, CropImage.class);
			intent2.putExtra("tag", "gen");
			intent2.putExtra("user_id", uid);
			intent2.putExtra("img_id", root_img_id);
			intent2.putExtra("is_save", is_save);
			intent2.putExtra("path", Config.ImageFile + path);
			startActivityForResult(intent2, IMAGE_CROP_RETURN);
			break;
		case IMAGE_CROP_RETURN:
			break;
		case 1:
			dataPostShow.clear();
			onRefresh();
			listview.setSelection(dataPostShow.size());
			adapter.notifyDataSetChanged();
			break;
		}

	}

	public void onResume() {
		super.onResume();
		context = this;
		edit_review.setText(msgString);
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void init() {
		uid = "";
		bottom_root = (RelativeLayout) findViewById(R.id.bottom_root);
		if (getIntent().hasExtra("user_id")) {
			uid = getIntent().getStringExtra("user_id");
		}
		if (getIntent().hasExtra("back")) {
			back = getIntent().getStringExtra("back");
		}
		louzhu = (ImageView) findViewById(R.id.louzhu);
		louzhu.setOnClickListener(louzhuClick);
		root_img_id = getIntent().getStringExtra("img_id");
		context = this;
		myMyApplication = (MyApplication) context.getApplication();

		title = (RelativeLayout) findViewById(R.id.title);
		collection = (ImageView) findViewById(R.id.collecton);
		post_more = (ImageView) findViewById(R.id.post_more);
		post_more.setOnClickListener(moreClick);
		is_save = getIntent().getStringExtra("is_save");

		if (!TextUtils.isEmpty(is_save)) {
			if ("0".equals(is_save)) {
				collection.setBackgroundResource(R.drawable.collet);
				collectionCheck = false;
			} else {
				collection.setBackgroundResource(R.drawable.collect_select);
				collectionCheck = true;
			}
		}
		faceBtn = (ImageButton) findViewById(R.id.face);
		faceBtn.setOnClickListener(faceClick);
		picture = (ImageButton) findViewById(R.id.picture);
		picture.setOnClickListener(pictureClick);
		edit_review = (EditText) findViewById(R.id.text);
		edit_review.setOnKeyListener(myKeyListener);
		reviewBtn = (ImageButton) findViewById(R.id.send);
		reviewBtn.setOnClickListener(reviewClick);
		custom_face_layout = (FaceRelativeLayout) findViewById(R.id.FaceRelativeLayout);
		custom_face_layout.setEditText(edit_review);
		// collection.setOnClickListener(collectionClick);
		sort = (ImageView) findViewById(R.id.sort);
		sort.setOnClickListener(sortClick);

		pictureGrideView = (GridView) findViewById(R.id.pictureGrideView);
		pictureAdapter = new PictureAdapter(this);
		pictureGrideView.setAdapter(pictureAdapter);

		if ("1".equals(myMyApplication.getVisitor())) {
			bottom_root.setVisibility(View.GONE);
		} else {
			bottom_root.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onRefresh() {
		AbRequestParams params = new AbRequestParams();

		if (isLouzhu == true) {
			mUrlString = ServerMutualConfig.PostMasterV1;
			params.put("user_id", uid);
			params.put("login_user_id", Config.getUser(context).uid);
			params.put("root_img_id", root_img_id);
		} else {
			mUrlString = ServerMutualConfig.PostReplyListV1;
			params.put("login_user_id", Config.getUser(context).uid);
			params.put("img_id", root_img_id);
			params.put("is_sort", is_sort + "");
		}
		// android.util.Log.e("fanfan", mUrlString + "&" + params.toString());
		ab.get(mUrlString + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						dataPostShow.clear();
						try {
							JSONObject json = new JSONObject(content);

							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								PostShowItem item = new PostShowItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i), "");
								dataPostShow.add(item);
							}
							if (dataPostShow.isEmpty()) {
								mPullDownView.setVisibility(View.GONE);
								// Intent intent = new Intent();
								// intent.setClass(context,
								// PostBarActivity.class);
								// startActivity(intent);
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
		if (dataPostShow.size() == 0) {
			mPullDownView.notifyDidMore();
			mPullDownView.RefreshComplete();
			return;
		}
		AbRequestParams params = new AbRequestParams();
		if (isLouzhu == true) {
			mUrlString = ServerMutualConfig.PostMasterV1;
			params.put("user_id", uid);
			params.put("login_user_id", Config.getUser(context).uid);
			params.put("root_img_id", root_img_id);
			params.put("last_id",
					dataPostShow.get(dataPostShow.size() - 1).img_id);
		} else {
			mUrlString = ServerMutualConfig.PostReplyListV1;
			params.put("login_user_id", Config.getUser(context).uid);
			params.put("img_id", root_img_id);
			params.put("last_id",
					dataPostShow.get(dataPostShow.size() - 1).img_id);
			params.put("is_sort", is_sort + "");
		}
		ab.get(mUrlString + "&" + params.toString(),
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
								PostShowItem item = new PostShowItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i), "");
								dataPostShow.add(item);
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

	// more
	public OnClickListener moreClick = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			showShare();
		}
	};

	private void showShare() {
		MobSDK.init(context);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		oks.setSilent(true);
//		oks.setNotification(R.drawable.icon, getString(R.string.app_name)); // 分享时Notification的图标和文字
		// 去除注释，演示在九宫格设置自定义的图标
		Bitmap collect = BitmapFactory.decodeResource(getResources(),
				R.drawable.collet);
		Bitmap collectSel = BitmapFactory.decodeResource(getResources(),
				R.drawable.collect_select);
		String label = getResources().getString(R.string.collect);
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				Config.showProgressDialog(context, false, null);
				final FinalHttp fh = new FinalHttp();
				final AjaxParams params = new AjaxParams();
				params.put("login_user_id", Config.getUser(context).uid);
				params.put("post_id", getIntent().getStringExtra("img_id"));

				fh.post(collectionCheck ? ServerMutualConfig.CancelPost
						: ServerMutualConfig.SavePost, params,
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
								String str = "";
								try {
									JSONObject json = new JSONObject(t);
									str = json.getString("reMsg");
								} catch (JSONException e) {
									e.printStackTrace();
								}
								if (str.contains("取消")) {
									collectionCheck = false;
									Toast.makeText(context, "已取消收藏", 0).show();
								} else {
									collectionCheck = true;
									Toast.makeText(context, "收藏成功", 0).show();
								}
								Config.dismissProgress();
							}

						});
			}
		};
		oks.setCustomerLogo(collect, label, listener);
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {

			@Override
			public void onShare(Platform platform, ShareParams paramsToShare) {
				if ("Wechat".equals(platform.getName())) {
					paramsToShare.setShareType(Platform.SHARE_TEXT);
					paramsToShare.setText(post_title);
					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
					if (TextUtils.isEmpty(post_img)) {
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(post_img);
					}
					paramsToShare
							.setUrl("http://www.meimei.yihaoss.top/share_post.php?img_id="
									+ root_img_id + "&user_id=" + uid);
				} else if ("SinaWeibo".equals(platform.getName())) {
					paramsToShare
							.setText(post_title
									+ "http://www.meimei.yihaoss.top/share_post.php?img_id="
									+ root_img_id + "&user_id=" + uid);
					if (TextUtils.isEmpty(post_img)) {
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(post_img);
					}
				} else if ("QZone".equals(platform.getName())) {
					paramsToShare.setTitle(post_title); // title微信、QQ空间
					paramsToShare
							.setTitleUrl("http://www.meimei.yihaoss.top/share_post.php?img_id="
									+ root_img_id + "&user_id=" + uid);
					paramsToShare.setSite(getString(R.string.app_name)); // site是分享此内容的网站名称，仅在QQ空间使用
					paramsToShare.setText(post_text);
					if (TextUtils.isEmpty(post_img)) {
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(post_img);
					}
					paramsToShare
							.setSiteUrl("http://www.meimei.yihaoss.top/share_post.php?img_id="
									+ root_img_id + "&user_id=" + uid); // siteUrl是分享此内容的网站地址，仅在QQ空间使用
				} else if ("QQ".equals(platform.getName())) {
					paramsToShare.setTitle(post_title);
					paramsToShare
							.setTitleUrl("http://www.meimei.yihaoss.top/share_post.php?img_id="
									+ root_img_id + "&user_id=" + uid);
					paramsToShare.setText(post_text);
					if (TextUtils.isEmpty(post_img)) {
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(post_img);
					}
				} else if ("WechatMoments".equals(platform.getName())) {
					paramsToShare.setShareType(Platform.SHARE_TEXT);
					paramsToShare.setTitle(post_title);
					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
					if (TextUtils.isEmpty(post_img)) {
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(post_img);
					}
					paramsToShare
							.setUrl("http://www.meimei.yihaoss.top/share_post.php?img_id="
									+ root_img_id + "&user_id=" + uid);
				}

			}
		});
		// 启动分享GUI
		oks.show(this);
	}

	// 楼主
	public OnClickListener louzhuClick = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (isLouzhu == false) {
				louzhu.setBackgroundResource(R.drawable.louzhu);
				dataPostShow.clear();
				isLouzhu = true;
				onRefresh();
				louzhu.setBackgroundResource(R.drawable.louzhu_select);
				sort.setVisibility(View.GONE);
			} else {
				louzhu.setBackgroundResource(R.drawable.louzhu_select);
				dataPostShow.clear();
				isLouzhu = false;
				onRefresh();
				louzhu.setBackgroundResource(R.drawable.louzhu);
			}
		}
	};

	// 排序
	public OnClickListener sortClick = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (is_sort == 0) {
				sort.setBackgroundResource(R.drawable.sort_zheng);
				is_sort = 1;
			} else {
				sort.setBackgroundResource(R.drawable.sort_dao);
				is_sort = 0;
			}
			onRefresh();

		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		edit_review.setHint("");
	}

	// 详情里的GridView
	class GridAdapter extends BaseAdapter {
		MyGridView myGridView;
		ArrayList<PostShowItem.Image> ImageList;
		private ImageDownloader mDownloader;

		public GridAdapter(ArrayList<PostShowItem.Image> ImageList,
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
			PostShowItem.Image item = ImageList.get(position);
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

	// 跟帖
	public String reviewID = "";
	AjaxParams params = new AjaxParams();
	public OnClickListener reviewClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if ("".equals(edit_review.getText().toString().trim())
					&& BimpUtil.drr.size() == 0) {
				Toast.makeText(context, "亲，什么都没写哦", 0).show();
			} else {
				Config.showProgressDialog(context, false, null);
				AlertDialog.Builder builder = new Builder(context);
				builder.setTitle("同步到秀秀");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO 同时发布到秀秀操作
								babyShow = 2;
								new Thread(new Runnable() {

									@Override
									public void run() {
										genTie();
									}
								}).start();
							}
						});
				builder.setNegativeButton("不了",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								new Thread(new Runnable() {

									@Override
									public void run() {
										genTie();
									}
								}).start();
							}
						});
				builder.create().show();
			}
		}

	};

	private void genTie() {
		if (BimpUtil.drr.size() == 0 && PhotoEdit.imgUris == null) {
			handler.sendEmptyMessage(0);
		} else {
			msgString = edit_review.getText().toString().trim();
			params.put("user_id", Config.getUser(context).uid);
			params.put("img_desc", msgString);
			File mFile = null;
			int filecount = 0;
			if (BimpUtil.drr.size() > 0) {
				for (int i = 0; i < BimpUtil.drr.size(); i++) {
					if (!BimpUtil.drr.get(i).startsWith("http")) {
						filecount++;
						if (BimpUtil.drr.size() == 1) {
							mFile = BitmapCache.getimageyang(BimpUtil.drr.get(i),
									"img_" + i + ".jpg", "dan");
						} else {
							mFile = BitmapCache.getimageyang(BimpUtil.drr.get(i),
									"img_" + i + ".jpg", "duo");
						}
						try {
							params.put("img" + (i + 1), mFile);

						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}

			params.put("file_count", filecount + "");
			params.put("root_img_id", root_img_id);
			params.put("type", babyShow + "");
			if (!TextUtils.isEmpty(PhotoEdit.imgUris)) {
				StringBuilder imgUri = new StringBuilder(PhotoEdit.imgUris);
				imgUri.deleteCharAt(imgUri.length() - 1);
				imgUri.append("]");
				params.put("img_urls", imgUri.toString());
			}

			handler.sendEmptyMessage(1);
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			FinalHttp fh = new FinalHttp();
			fh.post(ServerMutualConfig.PostImageNew, params,
					new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, String strMsg) {
							Config.dismissProgress();
							super.onFailure(t, strMsg);
							Toast.makeText(context, strMsg, 1).show();
						}

						@Override
						public void onSuccess(String t) {
							Config.dismissProgress();
							super.onSuccess(t);
							BimpUtil.drr.clear();
							BimpUtil.time.clear();
							BimpUtil.bmp.clear();
							PhotoEdit.imgUris = "";
							edit_review.setText("");
							msgString = "";
							onMore();
							((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
									.hideSoftInputFromWindow(
											PostBarDetail.this
													.getCurrentFocus()
													.getWindowToken(),
											InputMethodManager.HIDE_NOT_ALWAYS);
							custom_face_layout.hideFaceView();
							pictureGrideView.setVisibility(View.GONE);
						}
					});
			super.handleMessage(msg);

		}
	};

	/**
	 * 表情
	 */
	public OnClickListener faceClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (custom_face_layout.view.getVisibility() == View.VISIBLE) {
				custom_face_layout.hideFaceView();
			} else {
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(PostBarDetail.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				custom_face_layout.showFaceView();
				if (pictureGrideView.getVisibility() == View.VISIBLE) {
					pictureGrideView.setVisibility(View.GONE);
				}
			}
		}
	};

	/**
	 * 添加图片
	 */
	public OnClickListener pictureClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (pictureGrideView.getVisibility() == View.VISIBLE) {
				pictureGrideView.setVisibility(View.GONE);
			} else {
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(PostBarDetail.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				pictureGrideView.setVisibility(View.VISIBLE);
				if (custom_face_layout.view.getVisibility() == View.VISIBLE) {
					custom_face_layout.hideFaceView();
				}
			}
		}
	};

	// 跟帖的添加图片
	public class PictureAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public PictureAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			return (BimpUtil.drr.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final int coord = position;
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.item_postbar_detail_grida, parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				holder.delete = (ImageView) convertView
						.findViewById(R.id.delete);
				holder.delete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						BimpUtil.drr.remove(position - 1);
						pictureAdapter.notifyDataSetChanged();
					}
				});
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position == 0) {
				holder.delete.setVisibility(View.GONE);
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.postbar_add_picture));
			} else {
				if (BimpUtil.drr.get(position - 1).startsWith("http")) {
					MyApplication.getInstance().display(
							BimpUtil.drr.get(position - 1), holder.image,
							R.drawable.def_image);
				} else {
					String path = BimpUtil.drr.get(position - 1);
					try {
						Bitmap bm = BitmapCache.revitionImageSize(null, path);
						holder.image.setImageBitmap(bm);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
			public ImageView delete;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (BimpUtil.max == BimpUtil.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = BimpUtil.drr.get(BimpUtil.max);
								Bitmap bm = BitmapCache.revitionImageSize(null,
										path);
								BimpUtil.bmp.add(bm);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								FileUtils.saveBitmap(bm, "" + newStr);
								BimpUtil.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	// 发送
	boolean isClearReview = false;
	private final OnKeyListener myKeyListener = new OnKeyListener() {
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_DEL) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (msgString.length() == 0) {
						isClearReview = true;
					}
				} else if (event.getAction() == KeyEvent.ACTION_UP) {
					if (isClearReview) {
						msgString = "";
						edit_review.setHint("");
						isClearReview = false;
					}
				}
				return false;
			}
			return false;
		}
	};

	class ViewHolder {
		TextView postTitle;
		ImageView more;
		ImageView img;
		MyGridView mGridView;
		TextView name;
		RoundAngleImageView head;
		TextView time;
		TextView msg;
		TextView review;
		TextView zan;
		TextView check_reviewall;
		ImageView img_link;
		TextView msg_link;
		LinearLayout ly_link;
		ImageView rank;
		LinearLayout ly_rank;
	}

	// 详情的adapter
	@SuppressLint("ResourceAsColor")
	public class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return dataPostShow.size();
		}

		@Override
		public Object getItem(int arg0) {
			return dataPostShow.get(arg0);
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

			convertView = inflater.inflate(R.layout.postshow_item, null);
			holder = new ViewHolder();
			holder.postTitle = (TextView) convertView
					.findViewById(R.id.post_title);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.mGridView = (MyGridView) convertView
					.findViewById(R.id.n_img);
			holder.more = (ImageView) convertView.findViewById(R.id.more);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.msg = (TextView) convertView.findViewById(R.id.msg);
			holder.ly_link = (LinearLayout) convertView
					.findViewById(R.id.ly_link);
			holder.img_link = (ImageView) convertView
					.findViewById(R.id.img_link);
			holder.msg_link = (TextView) convertView
					.findViewById(R.id.msg_link);
			holder.head = (RoundAngleImageView) convertView
					.findViewById(R.id.head);
			holder.rank = (ImageView) convertView.findViewById(R.id.rank);
			holder.ly_rank = (LinearLayout) convertView
					.findViewById(R.id.ly_rank);
			holder.review = (TextView) convertView.findViewById(R.id.review);
			holder.check_reviewall = (TextView) convertView
					.findViewById(R.id.check_reviewall);
			convertView.setTag(holder);

			final PostShowItem item = dataPostShow.get(position);
			ScrollView scrollview = (ScrollView) convertView
					.findViewById(R.id.img_ly);
			if (item.ImageList.size() == 0) {
				scrollview.setVisibility(View.GONE);
			}
			LinearLayout imgLinear = new LinearLayout(context);
			addImg(scrollview, imgLinear, item.ImageList, position);

			userId = item.user_id;
			imgId = item.img_id;

			// 标题
			if (TextUtils.isEmpty(item.img_title)) {
				holder.postTitle.setVisibility(View.GONE);
			} else {
				post_title = item.img_title;
				holder.postTitle.setText(post_title);
			}

			// 等级
			if (!TextUtils.isEmpty(item.level_img)) {
				MyApplication.getInstance().display(item.level_img,
						holder.rank, position);
				holder.ly_rank.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setClass(context, Rank.class);
						startActivity(intent);
					}
				});
			}

			// 更多操作
			if (item.user_id.equals(Config.getUser(context).uid)) {
				holder.more.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Builder builder = new Builder(context);
						builder.setMessage("确认要删除吗?");
						builder.setNegativeButton("删除",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Config.showProgressDialog(context,
												false, null);
										AjaxParams params = new AjaxParams();
										params.put("user_id",
												Config.getUser(context).uid);
										params.put("img_ids", item.img_id);
										FinalHttp fh = new FinalHttp();
										fh.post(ServerMutualConfig.DelListingShow,
												params,
												new AjaxCallBack<String>() {
													@Override
													public void onFailure(
															Throwable t,
															String strMsg) {
														super.onFailure(t,
																strMsg);
														Config.dismissProgress();
														Config.showFiledToast(context);
													}

													@Override
													public void onSuccess(
															String t) {
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
				});
			} else {
				holder.more.setVisibility(View.GONE);
			}

			// 链接
			if (!TextUtils.isEmpty(item.post_url)) {
				holder.ly_link.setVisibility(View.VISIBLE);
				MyApplication.getInstance().display(item.post_url_image,
						holder.img_link, R.drawable.fabu_default_link);
				holder.msg_link.setText(item.post_url_title);
			} else {
				holder.ly_link.setVisibility(View.GONE);
			}
			holder.ly_link.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String strUri;
					if (!TextUtils.isEmpty(item.post_url)) {
						strUri = item.post_url;
						if (!strUri.startsWith("http")) {
							strUri = "http://" + strUri;
						}
						Intent intent = new Intent(context,
								WebViewActivity.class);
						intent.putExtra("webviewurl", strUri);
						startActivity(intent);
					} else {
						Toast.makeText(context, "错误的网页", 0).show();
					}

				}

			});
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

			long dateTime = Config.getDateDays(
					sdf.format(new Date(System.currentTimeMillis())),
					sdf.format(new Date(item.create_time)));
			if (dateTime < 0) {
				holder.time.setText(sdf.format(new Date(item.create_time)));
			}
			/**
			 * 超过一天
			 */
			else if (dateTime > 1000 * 60 * 60 * 24) {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				holder.time.setText(sdf1.format(new Date(item.create_time)));
			}
			/**
			 * 大于1天
			 */
			else if (dateTime > 1000 * 60 * 60) {
				holder.time.setText((dateTime / 1000 / 60 / 60) + "小时前");
			} else if (dateTime > 1000 * 60) {
				holder.time.setText((dateTime / 1000 / 60) + "分钟前");
			} else {
				holder.time.setText((dateTime / 1000) + "秒前");
			}

			// 描述
			if (item.description.equals("")) {
				holder.msg.setVisibility(View.GONE);
			} else {
				SpannableString spannableString = FaceConversionUtil
						.getInstace().getExpressionString(context,
								item.description);
				holder.msg.setText(spannableString);
				post_text = item.description;
			}

			// 赞
			final TextView zan = (TextView) convertView.findViewById(R.id.zan);
			zan.setText(item.admire_count);
			if (item.isZan) {
				zan.setBackgroundResource(R.drawable.fabu_zan_sel);
			} else {
				zan.setBackgroundResource(R.drawable.fabu_zan);
			}
			zan.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Config.showProgressDialog(context, false, null);
					if (!item.isZan) {
						AbRequestParams params = new AbRequestParams();
						params.put("user_id", Config.getUser(context).uid);
						params.put("admire_id", item.user_id);
						params.put("img_id", item.img_id);
						ab.post(ServerMutualConfig.PostAdmireNew, params,
								new AbStringHttpResponseListener() {
									@Override
									public void onSuccess(int statusCode,
											String content) {
										super.onSuccess(statusCode, content);
										item.admire_count = (Integer
												.parseInt(item.admire_count) + 1)
												+ "";
										zan.setText(item.admire_count);
										item.isZan = true;
										zan.setBackgroundResource(R.drawable.fabu_zan_sel);
									}

									@Override
									public void onFinish() {
										super.onFinish();
										Config.dismissProgress();
									}

									@Override
									public void onFailure(int statusCode,
											String content, Throwable error) {
										super.onFailure(statusCode, content,
												error);
										Config.showFiledToast(context);
									}
								});
					} else {
						AbRequestParams params = new AbRequestParams();
						params.put("user_id", Config.getUser(context).uid);
						params.put("admire_id", item.user_id);
						params.put("img_id", item.img_id);
						// android.util.Log.e("fanfan",
						// ServerMutualConfig.CancelPostAdmireNew +
						// params.toString());
						ab.post(ServerMutualConfig.CancelPostAdmireNew, params,
								new AbStringHttpResponseListener() {
									@Override
									public void onSuccess(int statusCode,
											String content) {
										super.onSuccess(statusCode, content);
										item.admire_count = (Integer
												.parseInt(item.admire_count) - 1)
												+ "";
										item.isZan = false;
										zan.setBackgroundResource(R.drawable.fabu_zan);
										zan.setText(item.admire_count);
									}

									@Override
									public void onFinish() {
										super.onFinish();
										Config.dismissProgress();
									}

									@Override
									public void onFailure(int statusCode,
											String content, Throwable error) {
										super.onFailure(statusCode, content,
												error);
										Config.showFiledToast(context);
									}
								});
					}
				}
			});

			// 回复
			holder.review.setText(item.review_count);
			holder.review.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					Intent intent = new Intent(context, PostReviewList.class);
					intent.putExtra("img_id", item.img_id);
					intent.putExtra("owner_id", item.user_id);
					intent.putExtra("ismy",
							item.user_id.equals(Config.getUser(context).uid));
					startActivityForResult(intent, 1);
				}
			});

			if (item.reviews.size() > 0) {
				LinearLayout reviews_root[] = new LinearLayout[4];
				reviews_root[0] = (LinearLayout) convertView
						.findViewById(R.id.review_1_root);
				reviews_root[1] = (LinearLayout) convertView
						.findViewById(R.id.review_2_root);
				reviews_root[2] = (LinearLayout) convertView
						.findViewById(R.id.review_3_root);
				reviews_root[3] = (LinearLayout) convertView
						.findViewById(R.id.review_4_root);
				reviews_root[0].setVisibility(View.GONE);
				reviews_root[1].setVisibility(View.GONE);
				reviews_root[2].setVisibility(View.GONE);
				reviews_root[3].setVisibility(View.GONE);
				if (item.reviews.size() >= 4) {
					for (int i = 0; i < item.reviews.size(); i++) {
						Review reviewitem = item.reviews.get(i);
						SpannableString spannableString = FaceConversionUtil
								.getInstace().getExpressionString(context,
										reviewitem.review);
						((TextView) (reviews_root[i].getChildAt(0)))
								.setText(reviewitem.name);
						((TextView) (reviews_root[i].getChildAt(1)))
								.setText(spannableString);
						reviews_root[i].setVisibility(View.VISIBLE);
					}
				} else if (item.reviews.size() < 4) {
					holder.check_reviewall.setVisibility(View.GONE);
					for (int i = 0; i < item.reviews.size(); i++) {
						Review reviewitem = item.reviews.get(i);
						SpannableString spannableString = FaceConversionUtil
								.getInstace().getExpressionString(context,
										reviewitem.review);
						((TextView) (reviews_root[i].getChildAt(0)))
								.setText(reviewitem.name);
						((TextView) (reviews_root[i].getChildAt(1)))
								.setText(spannableString);
						reviews_root[i].setVisibility(View.VISIBLE);
					}
				}

				holder.check_reviewall.setText("查看所有" + item.review_count
						+ "评论");
				holder.check_reviewall
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(context,
										PostReviewList.class);
								intent.putExtra("img_id", item.img_id);
								intent.putExtra("ismy", item.user_id
										.equals(Config.getUser(context).uid));
								startActivity(intent);
							}
						});
			} else {
				RelativeLayout review_root = (RelativeLayout) convertView
						.findViewById(R.id.review_root);
				review_root.setVisibility(View.GONE);
			}

			return convertView;
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
					post_img = imageList.get(0).img_thumb;
					img.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context,
									ChangePhotoSize.class);

							ArrayList<String> imgBig = new ArrayList<String>();
							ArrayList<String> imaHed = new ArrayList<String>();
							ArrayList<String> imaWid = new ArrayList<String>();
							for (int j = 0; j < imageList.size(); j++) {
								imgBig.add(imageList.get(j).img);
								imaHed.add(imageList.get(j).img_height);
								imaWid.add(imageList.get(j).img_width);
							}
							intent.putStringArrayListExtra("imgList", imgBig);
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
				final ArrayList<PostShowItem.Image> imgList,
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

	public void showButton() {
		if (title.getVisibility() != View.VISIBLE) {
			TranslateAnimation anim = new TranslateAnimation(0, 0,
					-title.getHeight(), 0);
			anim.setDuration(200);
			title.startAnimation(anim);
			anim.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					int left = title.getLeft();
					int top = title.getTop() + (int) (title.getHeight());
					int width = title.getWidth();
					int height = title.getHeight();
					title.setVisibility(View.VISIBLE);
					// title.layout(left,top,left + width,top + height);//
					// 重新设置view的大小
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					title.clearAnimation(); // 清除动画
				}
			});
		}
	}

	boolean isShowingAnim = false;

	public void hideButton() {
		if (title.getVisibility() != View.GONE && !isShowingAnim) {
			TranslateAnimation anim = new TranslateAnimation(0, 0, 0,
					-title.getHeight());
			anim.setDuration(200);
			anim.setInterpolator(new LinearInterpolator());// 设置插入器
			anim.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					isShowingAnim = true;
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					isShowingAnim = false;
					title.clearAnimation(); // 清除动画
					// topButton_root.layout(left,top,left + width,top +
					// height);// 重新设置view的大小
					title.setVisibility(View.GONE);
				}
			});
			title.startAnimation(anim);
		}
	}

	/* 选择图片的pop */
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
			msgString = edit_review.getText().toString().trim();
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
						intent.putExtra("tag", "gen");
						intent.putExtra("user_id", uid);
						intent.putExtra("img_id", root_img_id);
						intent.putExtra("is_save", is_save);
						intent.putExtra(Config.SELECTNAME, 6);
						startActivity(intent);
					} else {
						Toast.makeText(context, "最多选6张哦", 0).show();
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
						intent.putExtra("tag", "gen");
						intent.putExtra("user_id", uid);
						intent.putExtra("img_id", root_img_id);
						intent.putExtra("is_save", is_save);
						Config.SELECTEDIMAGECOUNT = 6;
						startActivity(intent);
					} else {
						Toast.makeText(context, "已达到上限", 1).show();
					}
					dismiss();
				}
			});
		}
	}

	private boolean isFallBit() {

		if (BimpUtil.drr.size() >= 6) {
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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("path", path);
		outState.putInt("imgNum", imgNum);
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}

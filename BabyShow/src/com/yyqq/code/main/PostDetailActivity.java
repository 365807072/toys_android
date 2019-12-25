package com.yyqq.code.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.mob.MobSDK;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.photo.BitmapCache;
import com.yyqq.code.photo.CropImage;
import com.yyqq.code.photo.PhoneAlbumActivity;
import com.yyqq.code.photo.PhotoEdit;
import com.yyqq.commen.adapter.PostDetailAdapter;
import com.yyqq.commen.adapter.PostDetailPictureAdapter;
import com.yyqq.commen.model.PostShowItem;
import com.yyqq.commen.utils.BimpUtil;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.view.FaceRelativeLayout;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * 帖子详情页(启用于2016.08.24)
 * */
public class PostDetailActivity extends BaseActivity implements OnPullDownListener{
	
	private PullDownView mPullDownView;
	private ListView listview;
	private RelativeLayout title;
	private Activity context;
	private ImageView collection;
	private ImageView louzhu;
	private ImageButton faceBtn, picture;
	private FaceRelativeLayout custom_face_layout;
	private EditText edit_review;
	private ImageButton reviewBtn;
	private GridView pictureGrideView;
	private PostDetailPictureAdapter pictureAdapter;
	private RelativeLayout bottom_root;
	private ImageView sort;
	private ImageView post_more;
	private PostDetailAdapter adapter;
	
	private static final int TAKE_PICTURE = 0x000000;
	private static final int IMAGE_CROP_RETURN = 4;
	private String path = "";
	private static int imgNum;
	
	// 发送
	boolean isClearReview = false;
	// 跟帖
	public String reviewID = "";
	AjaxParams params = new AjaxParams();
	private String TAG = "PostShow_Detail";
	private ArrayList<PostShowItem> dataPostShow = new ArrayList<PostShowItem>();
	private int width, height;
	private AbHttpUtil ab;
	private String uid = "";
	private String is_save = "";
	private boolean collectionCheck;
	private String root_img_id = "";
	private String back = "";
	private boolean isLouzhu;
	private String mUrlString;
	private String imgId;
	private String userId;
	public static String msgString = "";
	private int babyShow = 1;
	private MyApplication myMyApplication;
	private int is_sort = 0;
	private String post_title = "";
	private String post_img = "";
	private String post_text = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		MobSDK.init(context);
		setContentView(R.layout.postshow_detail);
	}
	
	@Override
	protected void initView() {
		mPullDownView = (PullDownView) findViewById(R.id.list);
		mPullDownView.setOnPullDownListener(this);
		listview = mPullDownView.getListView();
		
		bottom_root = (RelativeLayout) findViewById(R.id.bottom_root);
		louzhu = (ImageView) findViewById(R.id.louzhu);
		title = (RelativeLayout) findViewById(R.id.title);
		collection = (ImageView) findViewById(R.id.collecton);
		post_more = (ImageView) findViewById(R.id.post_more);
		
		faceBtn = (ImageButton) findViewById(R.id.face);
		picture = (ImageButton) findViewById(R.id.picture);
		edit_review = (EditText) findViewById(R.id.text);
		reviewBtn = (ImageButton) findViewById(R.id.send);
		custom_face_layout = (FaceRelativeLayout) findViewById(R.id.FaceRelativeLayout);
		custom_face_layout.setEditText(edit_review);
		// collection.setOnClickListener(collectionClick);
		sort = (ImageView) findViewById(R.id.sort);

		if ("1".equals(myMyApplication.getVisitor())) {
			bottom_root.setVisibility(View.GONE);
		} else {
			bottom_root.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void initData() {
		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		width = DM.widthPixels;
		height = DM.heightPixels;
		
		listview.setDivider(null);
		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
		if (adapter == null) {
			adapter = new PostDetailAdapter();
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
		
		uid = "";
		if (getIntent().hasExtra("user_id")) {
			uid = getIntent().getStringExtra("user_id");
		}
		if (getIntent().hasExtra("back")) {
			back = getIntent().getStringExtra("back");
		}
		
		root_img_id = getIntent().getStringExtra("img_id");
		context = this;
		myMyApplication = (MyApplication) context.getApplication();

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
		
		pictureGrideView = (GridView) findViewById(R.id.pictureGrideView);
		pictureAdapter = new PostDetailPictureAdapter(this);
		pictureGrideView.setAdapter(pictureAdapter);
		
		onRefresh();
	}

	@Override
	protected void setListener() {
		louzhu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
		});
		post_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showShare();
			}
		});
		faceBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (custom_face_layout.view.getVisibility() == View.VISIBLE) {
					custom_face_layout.hideFaceView();
				} else {
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(PostDetailActivity.this
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
					custom_face_layout.showFaceView();
					if (pictureGrideView.getVisibility() == View.VISIBLE) {
						pictureGrideView.setVisibility(View.GONE);
					}
				}
			}
		});
		picture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (pictureGrideView.getVisibility() == View.VISIBLE) {
					pictureGrideView.setVisibility(View.GONE);
				} else {
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(PostDetailActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
					pictureGrideView.setVisibility(View.VISIBLE);
					if (custom_face_layout.view.getVisibility() == View.VISIBLE) {
						custom_face_layout.hideFaceView();
					}
				}
			}
		});
		edit_review.setOnKeyListener(new OnKeyListener() {
			
			@Override
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
		});
		reviewBtn.setOnClickListener(new OnClickListener() {
			
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
		});
		
		sort.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (is_sort == 0) {
					sort.setBackgroundResource(R.drawable.sort_zheng);
					is_sort = 1;
				} else {
					sort.setBackgroundResource(R.drawable.sort_dao);
					is_sort = 0;
				}
				onRefresh();
			}
		});
		
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
											PostDetailActivity.this
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
	
	public void photo() {
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		imgNum++;
		path = "temp_image_" + imgNum + ".jpg";
		Uri imageUri = Uri.parse("file:///sdcard/yyqq/babyshow/image/" + path);
		i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(i, TAKE_PICTURE);
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
		MobclickAgent.onResume(this);
		context = this;
		edit_review.setText(msgString);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		edit_review.setHint("");
	}
	
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
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("path", path);
		outState.putInt("imgNum", imgNum);
	}
	
	public static void setonRefresh(){
		PostDetailActivity post = new PostDetailActivity();
		post.onRefresh();
	}

}

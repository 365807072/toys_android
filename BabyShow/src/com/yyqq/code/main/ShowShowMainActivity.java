package com.yyqq.code.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
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
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.code.photo.ChangePhotoSize;
import com.yyqq.commen.adapter.MainFollowListAdapter;
import com.yyqq.commen.model.PictureItem;
import com.yyqq.commen.model.ScrollOverListView;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.ImageDownloader;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.utils.OnImageDownload;
import com.yyqq.commen.utils.RoundAngleImageView;
import com.yyqq.commen.utils.VideoComperssUtils;
import com.yyqq.commen.view.MyGridView;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.commen.view.myVideoPlayerView;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;


/**
 * 秀秀列表页
 * */
@SuppressLint("NewApi")
public class ShowShowMainActivity extends Activity implements OnPullDownListener{

	// 进入或返回后本页面后，是否需要再次刷新
	public static boolean isRefresh = false;
	
	public static String TAG_ID_KEY = "tag_id_key";
	public static String TITLE_TEXT_KEY = "titlt_text_key";
	private final String TAG = "fanfan_PictureZuiXin";
	private PullDownView mPullDownView;
	private ListView listview;
//	private MyAdapter adapter;
	private MainFollowListAdapter adapter;
	private AbHttpUtil ab;
	private Activity context;
	private ArrayList<PictureItem> data = new ArrayList<PictureItem>();
	private MyApplication app;
	private int width;
	private ImageDownloader mDownloader;
	private String fileName = "PictureZuiXin.txt";
	private ImageView send_show;
	private ImageView showshow_back;
	private TextView showshow_title;
	
	// 分享的4个参数
	private String share_title = "";
	private String share_img = "";
	private String share_imgId = "";
	private String share_userId = "";

	private TextView toShow, toSpecial;
	private ImageView back;
	
	public static ShowShowMainActivity showShow = null;
	public static ImageView upload_icon;
	private AlertDialog.Builder dialog;
	private WindowManager windowsManager = null;
	private String tag_id = "'";
	
	@Override
	protected void onResume() {
		
		super.onResume();
		MobclickAgent.onResume(this);
		if(isRefresh){
			onRefresh();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		context = this;
		MobSDK.init(context);
		windowsManager = this.getWindowManager();
		setContentView(R.layout.show_show);
		showShow = this;
		init();
	}
	
	private void init() {
		
		ScrollOverListView.canRefleash = false;
		
		if(getIntent().hasExtra(TAG_ID_KEY)){
			tag_id = getIntent().getStringExtra(TAG_ID_KEY);
		}
		
		showshow_back = (ImageView) findViewById(R.id.showshow_back);
		showshow_title = (TextView) findViewById(R.id.showshow_title);
		
		if(getIntent().hasExtra(TITLE_TEXT_KEY)){
			showshow_title.setText(getIntent().getStringExtra(TITLE_TEXT_KEY));
		}
		
		showshow_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ShowShowMainActivity.this.finish();
			}
		});
		
		upload_icon = (ImageView) findViewById(R.id.upload_icon);
		if(VideoComperssUtils.uploadIcon){
			upload_icon.setVisibility(View.VISIBLE);
		}else{
			upload_icon.setVisibility(View.GONE);
		}
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(backClick);
		toShow = (TextView) findViewById(R.id.toShow);
		toSpecial = (TextView) findViewById(R.id.toSpecial);
		toShow.setOnClickListener(showClick);
		toSpecial.setOnClickListener(specialClick);

		send_show = (ImageView) findViewById(R.id.send_show);
		send_show.setOnClickListener(xiuClick);
		mPullDownView = (PullDownView) findViewById(R.id.list);
		DisplayMetrics DM = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(DM);
		width = DM.widthPixels;
		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
		app = (MyApplication) context.getApplication();

		mPullDownView.setOnPullDownListener(this);
		listview = mPullDownView.getListView();
		listview.setDivider(null);
		
		if (adapter == null) {
			adapter = new MainFollowListAdapter(ShowShowMainActivity.this, data, ab);
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
		// mPullDownView.setShowHeaderLayout(mRelativeLayout);
		
		if(!Config.read(context, fileName).trim().isEmpty()){
			getlistData(Config.read(context, fileName));
		}
		
		if (Config.isConn(context)) {
			onRefresh();
		}
	}

	public OnClickListener backClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			context.finish();
		}
	};
	public OnClickListener showClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, ShowShowMainActivity.class);
			startActivity(intent);
			context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			context.finish();
		}
	};
	public OnClickListener specialClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, SpecialActivity.class);
			startActivity(intent);
			context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			context.finish();
		}
	};

	@Override
	public void onRefresh() {
		Config.showProgressDialog(context, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("tag_id", tag_id);
		ab.get(ServerMutualConfig.GET_SHOUSHOU_LIST + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						data.clear();
						Config.dismissProgress();
						Config.save(context, content, fileName);
						getlistData(content);
					}

					@Override
					public void onFinish() {
						super.onFinish();
						Config.dismissProgress();
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
		params.put("tag_id", tag_id);
		params.put("post_create_time", data.get(data.size() - 1).post_create_time + "");
		ab.get(ServerMutualConfig.GET_SHOUSHOU_LIST + "&" + params.toString(),
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
								PictureItem item = new PictureItem();
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
						Config.dismissProgress();
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}
				});
	}

	class ViewHolder {
		ImageView more;
		ImageView img;
		MyGridView mGridView;
		TextView name;
		RoundAngleImageView head;
		TextView msg;
		TextView review;
		TextView zan;
		ImageView permission_relation;
		ImageView img_link;
		TextView msg_link;
		LinearLayout ly_link;
		ImageView attention;
		RelativeLayout ly_time;
		TextView picture_size;
		LinearLayout ly_picture_page;
		RelativeLayout ly_time_1;
		TextView time;
		RelativeLayout ly_time_2;
		TextView month;
		TextView year;
		TextView day;
		ImageView rank;
		LinearLayout ly_rank;
		
		// 视频播放
		myVideoPlayerView video_layout;
		ImageView video_start;
		ImageView video_stop;
		ImageView iv_video_thumb;
		ImageView iv_video_bg;
		RelativeLayout video_player_all;
		LinearLayout video_play_layout;
		ImageView iv_video_bg_up;
		ProgressBar iv_video_loading;
		ImageView iv_video_bg_hint;
		ImageView iv_video_bg_hint02;
		ImageView iv_to_full;
		
		public int indexPosition;
		public int position;
		private int duration;
		private Button video_type;
		private SeekBar skbProgress;
		private TextView video_new_time;
		private TextView video_all_length;
		private LinearLayout video_play_console;
		private boolean noWifiPlay = false;
	}
	
	protected Activity getActivity() {
		return this;
	}


	class ImageAdapter extends BaseAdapter {
		ArrayList<String> imaStrings = null;
		MyGridView myGridView;
		ArrayList<String> imgBig = null;
		ArrayList<String> ImageList = null;
		ArrayList<String> imaWid = null;
		ArrayList<String> imaHed = null;

		public ImageAdapter(ArrayList<String> imaStrings,
				MyGridView myGridView, ArrayList<String> imgBig,
				ArrayList<String> imaWid, ArrayList<String> imaHed) {
			this.imaStrings = imaStrings;
			this.myGridView = myGridView;
			this.imgBig = imgBig;
			this.imaWid = imaWid;
			this.imaHed = imaHed;
		}

		@Override
		public int getCount() {

			return imaStrings.size() < 3 ? imaStrings.size() : 2;
		}

		@Override
		public Object getItem(int position) {
			return imaStrings.get(position);
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
				convertView = inflater.inflate(R.layout.new_grida_item, null);
				holder = new ViewHolder();
				holder.img = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				holder.picture_size = (TextView) convertView
						.findViewById(R.id.picture_size);
				holder.ly_picture_page = (LinearLayout) convertView
						.findViewById(R.id.ly_picture_page);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// 图片处理
			if (!TextUtils.isEmpty(imaWid.get(position))) {
				int newW = width / 2;
				int newH = width / 2;
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						newW, newH);
				holder.img.setLayoutParams(params);
			}

			holder.img.setTag(imaStrings.get(position));
			if (mDownloader == null) {
				mDownloader = new ImageDownloader();
			}
			if (mDownloader != null && position != 3) {
				mDownloader.imageDownload(imaStrings.get(position), holder.img,
						"/Download/cache_files", context,
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
			if (position == 1) {
				holder.ly_picture_page.setVisibility(View.VISIBLE);
				holder.picture_size.setText(imaStrings.size() + "张");
			}
			holder.img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ChangePhotoSize.class);
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

	private void getlistData(String content) {
		try {
			JSONObject json = new JSONObject(content);
			for (int i = 0; i < json.getJSONArray("data").length(); i++) {
				PictureItem item = new PictureItem();
				item.fromJson(json.getJSONArray("data").getJSONObject(i),content);
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

	private void showShare(final String img_id, final String isVideo) {
		MobSDK.init(context);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		oks.setSilent(true);
//		oks.setNotification(R.drawable.icon, getString(R.string.app_name)); // 分享时Notification的图标和文字
		// 去除注释，演示在九宫格设置自定义的图标
		Bitmap collect = BitmapFactory.decodeResource(getResources(),
				R.drawable.logo_delete);
		Bitmap collectSel = BitmapFactory.decodeResource(getResources(),
				R.drawable.logo_delete);
		String label = getResources().getString(R.string.delete);
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				deleteItem(img_id, isVideo);
			}
		};
		oks.setCustomerLogo(collect, label, listener);
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {

			@Override
			public void onShare(Platform platform, ShareParams paramsToShare) {
				if ("Wechat".equals(platform.getName())) {
					paramsToShare.setShareType(Platform.SHARE_TEXT);
					paramsToShare.setText(share_title);
					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(share_img);
					}
					paramsToShare
							.setUrl("http://www.meimei.yihaoss.top/share.php?"
									+ "img_id=" + share_imgId + "&user_id="
									+ share_userId);
				} else if ("SinaWeibo".equals(platform.getName())) {
					paramsToShare.setText(share_title
							+ "http://www.meimei.yihaoss.top/share.php?"
							+ "img_id=" + share_imgId + "&user_id="
							+ share_userId);
					// android.util.Log.e("fanfan",
					// "http://www.meimei.yihaoss.top/share.php?"
					// + "img_id=" + share_imgId + "&user_id="
					// + share_userId);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(share_img);
					}
				} else if ("QZone".equals(platform.getName())) {
					paramsToShare.setTitle(""); // title微信、QQ空间
					paramsToShare
							.setTitleUrl("http://www.meimei.yihaoss.top/share.php?"
									+ "img_id="
									+ share_imgId
									+ "&user_id="
									+ share_userId);
					paramsToShare.setSite(getString(R.string.app_name)); // site是分享此内容的网站名称，仅在QQ空间使用
					paramsToShare.setText(share_title);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(share_img);
					}
					paramsToShare
							.setSiteUrl("http://www.meimei.yihaoss.top/share.php?"
									+ "img_id="
									+ share_imgId
									+ "&user_id="
									+ share_userId); // siteUrl是分享此内容的网站地址，仅在QQ空间使用
				} else if ("QQ".equals(platform.getName())) {
					paramsToShare.setTitle("");
					paramsToShare
							.setTitleUrl("http://www.meimei.yihaoss.top/share.php?"
									+ "img_id="
									+ share_imgId
									+ "&user_id="
									+ share_userId);
					paramsToShare.setText(share_title);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(share_img);
					}
				} else if ("WechatMoments".equals(platform.getName())) {
					paramsToShare.setShareType(Platform.SHARE_TEXT);
					paramsToShare.setTitle(share_title);
					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
					if (TextUtils.isEmpty(share_img)) {
						paramsToShare
								.setImageUrl("https://api.meimei.yihaoss.top/static/defaultimg/0000000_thumb_640.jpg");
					} else {
						paramsToShare.setImageUrl(share_img);
					}
					paramsToShare
							.setUrl("http://www.meimei.yihaoss.top/share.php?"
									+ "img_id=" + share_imgId + "&user_id="
									+ share_userId);
				}

			}
		});
		// 启动分享GUI
		oks.show(context);
	}
	
	/**
	 * 删除条目
	 * */
	private void deleteItem(final String img_id, final String isVideo){
		if (share_userId.equals(Config.getUser(context).uid)) {
			Builder builder = new Builder(context);
			builder.setMessage("确认要删除吗?");
			builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Config.showProgressDialog(context, false, null);
					AjaxParams params = new AjaxParams();
					params.put("user_id", Config.getUser(context).uid);
					params.put("img_ids", img_id);
					params.put("is_video", isVideo);
					FinalHttp fh = new FinalHttp();
//					ServerMutualConfig.DelShow
					fh.post( "", params, new AjaxCallBack<String>() {
						@Override
						public void onFailure( Throwable t, String strMsg) {
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
								Toast.makeText(context, json.getString("reMsg"), Toast.LENGTH_SHORT).show();
								onRefresh();
							} catch (Exception e) {
							}
						}
					});
				}
			});
			builder.setNeutralButton("取消", null);
			builder.create().show();
		} else {
			Toast.makeText(context, "不可以删除别人的哦", 0).show();
		}
		
	}

	public OnClickListener xiuClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if ("1".equals(app.getVisitor())) {
				app.setVisitor("");
				Intent intent = new Intent();
				intent.setClass(context, WebLoginActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent();
				intent.setClass(context, AddNewPostActivity.class);
				intent.putExtra(AddNewPostActivity.GET_TAG_ID_KEY, tag_id);
				startActivity(intent);
			}
		}
	};
	
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			ShowShowMainActivity.this.finish();
		}
		return false;
	}
	
	/** 
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
	 */  
	public static int dip2px(Context context, float dpValue) {  
		final float scale = context.getResources().getDisplayMetrics().density;  
		return (int) (dpValue * scale + 0.5f);  
	}  
	
	/** 
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
	 */  
	public static int px2dip(Context context, float pxValue) {  
		final float scale = context.getResources().getDisplayMetrics().density;  
		return (int) (pxValue / scale + 0.5f);  
	}   
}
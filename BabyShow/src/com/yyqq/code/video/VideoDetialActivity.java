package com.yyqq.code.video;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
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
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbAppUtil;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainItemDetialActivity;
import com.yyqq.code.main.PhotoManager;
import com.yyqq.code.personal.PersonalCenterActivity;
import com.yyqq.code.personal.UserInfo;
import com.yyqq.code.photo.BitmapCache;
import com.yyqq.code.photo.ChangePhotoSize;
import com.yyqq.code.photo.CropImage;
import com.yyqq.code.photo.PhoneAlbumActivity;
import com.yyqq.code.photo.PhotoEdit;
import com.yyqq.code.postbar.PostBarActivity;
import com.yyqq.code.postbar.PostReviewList;
import com.yyqq.commen.adapter.MainItemCommentAdapter;
import com.yyqq.commen.adapter.MainItemHintAdapter;
import com.yyqq.commen.model.MainItemBodyBean;
import com.yyqq.commen.model.MainItemCommentBean;
import com.yyqq.commen.model.MainItemHintBean;
import com.yyqq.commen.model.PostBarTypeItem;
import com.yyqq.commen.model.PostShowItem;
import com.yyqq.commen.model.PostShowItem.Image;
import com.yyqq.commen.model.PostShowItem.Review;
import com.yyqq.commen.utils.BimpUtil;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.FaceConversionUtil;
import com.yyqq.commen.utils.FileUtils;
import com.yyqq.commen.utils.GroupRelevantUtils;
import com.yyqq.commen.utils.ImageDownloader;
import com.yyqq.commen.utils.OnImageDownload;
import com.yyqq.commen.utils.Rank;
import com.yyqq.commen.utils.RoundAngleImageView;
import com.yyqq.commen.utils.WebViewActivity;
import com.yyqq.commen.view.FaceRelativeLayout;
import com.yyqq.commen.view.MyGridView;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.commen.view.myVideoPlayerView;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class VideoDetialActivity extends BaseActivity implements OnPullDownListener,OnLayoutChangeListener{
	
	public static int sendIndex = 0; // 0=直接评论，1=回复评论or跟评
	public static String indexId = ""; // 被选中的评论的id
	public static boolean isEditType = true; // 是否切换评论/跟评状态
	public static String VIIDEO_INFO = "video_info"; // 视频相关信息
	
	private ListView listview;
	private ImageButton faceBtn;
	private ImageButton picture;
	private FaceRelativeLayout custom_face_layout;
	private EditText edit_review;
	private GridView pictureGrideView;
	private LinearLayout bottom_root;
	private RelativeLayout title;
	private ImageButton reviewBtn;
	private PullDownView pullDownView;
	private TextView video_title;
	private RoundAngleImageView item_detial_icon;
	private TextView item_detial_username;
	private TextView item_detial_time;
	private TextView video_play_count;
	private RelativeLayout mRelativeLayout;
	private ImageView video_finish;
	private ImageView item_shared_weixin;
	private ImageView item_shared_pengyouquan;
	private LinearLayout item_is_zan;
	private ImageView item_is_zan_icon;
	private TextView item_is_zan_number;
	private ImageButton save_post;
	private RelativeLayout relativeLayout1;
	
	private MainItemCommentAdapter commentAdapte;
	public static boolean isStart = true; // 播放状态
	private AlertDialog.Builder dialog;
	private PostBarTypeItem bean = new PostBarTypeItem();
	private boolean bodyEnd;
	private boolean commentEnd;
	private AbHttpUtil ab;
	private Activity context;
	private MyApplication app;
	private String itemId = "";
	private String userid = "";
	private LayoutInflater inflater;
	private ArrayList<MainItemBodyBean> bodyList = new ArrayList<MainItemBodyBean>(); // 文章主体
	private ArrayList<MainItemCommentBean> commentList = new ArrayList<MainItemCommentBean>(); // 评论集合
	private ArrayList<PostShowItem> dataPostShow = new ArrayList<PostShowItem>();
	private ArrayList<MainItemHintBean> hintList = new ArrayList<MainItemHintBean>(); // 推荐集合
	public static final String ITEM_ID = "item_id";
	private String post_create_time = "0";
	private PictureAdapter pictureAdapter;
	public static String msgString = "";
	boolean isClearReview = false;
	private String uid = "";
	private String is_save = "";
	private String root_img_id = "";
	private String back = "";
	private int babyShow = 1;
	private String post_title = "";
	private String post_img = "";
	private String post_text = "";
	private MyAdapter adapter;
	private String imgId;
	private String userId;
	private int width;
	private int height;
	private boolean isZan = false;
	private String zanNumber = "0";
	private String user_id = "";
	private boolean collectionCheck;
	public static VideoDetialActivity videoDetialActivity = null;
	public static String VIDEO_URL = "";
	private MainItemHintAdapter hintAdapter;
    //Activity最外层的Layout视图  
    private View activityRootView;  
    //屏幕高度  
    private int screenHeight = 0;  
    //软件盘弹起后所占高度阀值  
    private int keyHeight = 0;  
	
	// 视频播放相关
	private myVideoPlayerView video_layout; // 视频播放控件
	private ImageView video_start; // 开始播放按钮
	private ImageView video_stop; // 停止播放按钮
	private LinearLayout video_player_all; // 包含全部视频播放相关组件
	private ImageView iv_video_thumb; // 背景图
	private Timer videoTimer = new Timer();
	private ImageView iv_video_bg;
	private LinearLayout video_play_layout;
	private ImageView iv_video_bg_up;
	private ProgressBar iv_video_loading;
	private ImageView iv_video_bg_hint;
	private RelativeLayout item_layout;
	private ImageView add_useer;
	private ImageView video_detial_shared;
	private TextView item_detial_from;
	private ImageView video_to_fullScreen;
	
	public int position;
	private int duration;
	private int indexPosition = -1;
	private Button video_type;
	private SeekBar skbProgress;
	private TextView video_new_time;
	private TextView video_all_length;
	private LinearLayout video_play_console;
	private TextView video_info;
	private TextView item_comment_title;
	private com.yyqq.commen.view.RecodeListView main_item_hint_list;
	private WindowManager windowsManager = null;
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		windowsManager = this.getWindowManager();
		setContentView(R.layout.activity_video_detail);
		context = this;
	}

	/**
	 * 初始化个人信息
	 * */
	private void initUserInfo() {
		inflater = LayoutInflater.from(VideoDetialActivity.this);
		mRelativeLayout = (RelativeLayout) inflater.inflate(R.layout.video_detial_head, null);
		relativeLayout1 = (RelativeLayout)mRelativeLayout.findViewById(R.id.relativeLayout1);
		video_title = (TextView) mRelativeLayout.findViewById(R.id.video_title);
		item_detial_icon = (RoundAngleImageView) mRelativeLayout.findViewById(R.id.item_detial_icon);
		item_detial_username = (TextView) mRelativeLayout.findViewById(R.id.item_detial_username);
		item_detial_time = (TextView) mRelativeLayout.findViewById(R.id.item_detial_time);
		video_play_count = (TextView) mRelativeLayout.findViewById(R.id.video_play_count);
		item_shared_weixin = (ImageView) mRelativeLayout .findViewById(R.id.item_shared_weixin);
		item_shared_pengyouquan = (ImageView) mRelativeLayout .findViewById(R.id.item_shared_pengyouquan);
		item_is_zan = (LinearLayout) mRelativeLayout .findViewById(R.id.item_is_zan); // 赞布局
		item_is_zan_icon = (ImageView) mRelativeLayout .findViewById(R.id.item_is_zan_icon); // 点赞图标
		item_is_zan_number = (TextView) mRelativeLayout.findViewById(R.id.item_is_zan_number);
		video_info = (TextView) mRelativeLayout.findViewById(R.id.video_info);
		item_comment_title = (TextView) mRelativeLayout.findViewById(R.id.item_comment_title);
		add_useer = (ImageView) mRelativeLayout .findViewById(R.id.add_useer);
		item_detial_from = (TextView) mRelativeLayout.findViewById(R.id.item_detial_from);
		main_item_hint_list = (com.yyqq.commen.view.RecodeListView) mRelativeLayout.findViewById(R.id.main_item_hint_list);
		
		mRelativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
			}
		});
	}

	
	@Override
	protected void initView() {
		
		/**
		 * 视频及相关信息
		 * */ 
		initUserInfo();
		initVideoConsole();
		activityRootView = findViewById(R.id.main_item_all);
		video_finish = (ImageView) findViewById(R.id.video_finish);
		pullDownView = (PullDownView) findViewById(R.id.video_comment);
		video_start = (ImageView) findViewById(R.id.iv_video_play);
		video_stop = (ImageView) findViewById(R.id.iv_video_stop);
		video_player_all = (LinearLayout)findViewById(R.id.video_player_all);
		video_start = (ImageView) findViewById(R.id.iv_video_play);
		video_stop = (ImageView) findViewById(R.id.iv_video_stop);
		iv_video_thumb = (ImageView) findViewById(R.id.iv_video_thumb);
		iv_video_bg = (ImageView) findViewById(R.id.iv_video_bg);
		video_play_layout = (LinearLayout) findViewById(R.id.video_play_layout);
		iv_video_bg_up = (ImageView) findViewById(R.id.iv_video_bg_up);
		iv_video_loading = (ProgressBar) findViewById(R.id.iv_video_loading);
		iv_video_bg_hint = (ImageView) findViewById(R.id.iv_video_bg_hint);
		save_post = (ImageButton) findViewById(R.id.save_post);
		video_detial_shared = (ImageView) findViewById(R.id.video_detial_shared);
		video_to_fullScreen = (ImageView) findViewById(R.id.video_to_fullScreen);
		 
		/**
		 * 添加部分
		 * */ 
		pictureGrideView = (GridView) findViewById(R.id.pictureGrideView);
		faceBtn = (ImageButton) findViewById(R.id.face);
		picture = (ImageButton) findViewById(R.id.picture);
		edit_review = (EditText) findViewById(R.id.text);
		custom_face_layout = (FaceRelativeLayout) findViewById(R.id.FaceRelativeLayout);
		custom_face_layout.setEditText(edit_review);
		bottom_root = (LinearLayout) findViewById(R.id.bottom_root);
		title = (RelativeLayout) findViewById(R.id.title);
		reviewBtn = (ImageButton) findViewById(R.id.send);
		bottom_root.setVisibility(View.VISIBLE);
	}

	@SuppressLint("NewApi")
	@Override
	protected void initData() {
		
		inflater = LayoutInflater.from(VideoDetialActivity.this);
		
		/**
		 * 视频详情、用户评论部分
		 * */
		Intent in = getIntent();
		if(null != in.getSerializableExtra(VIIDEO_INFO)){
			bean = (PostBarTypeItem) in.getSerializableExtra(VIIDEO_INFO);
			root_img_id = bean.img_id;
			if(!bean.video_url.isEmpty()){
				VIDEO_URL = bean.video_url;
			}

			MyApplication.getInstance().display(bean.img, iv_video_bg, R.drawable.def_image);
			iv_video_bg.setVisibility(View.VISIBLE);
			
			MyApplication.getInstance().display(bean.img, iv_video_bg_up, R.drawable.def_image);
			
		}else if(null != in.getStringExtra("img_id")){
			root_img_id = in.getStringExtra("img_id");
			bean.setImg_id(root_img_id);
		}
		app = (MyApplication) VideoDetialActivity.this.getApplication();
		ab = AbHttpUtil.getInstance(VideoDetialActivity.this);
		app = (MyApplication) getApplication();
		videoDetialActivity = this;
		
		pullDownView.setOnPullDownListener(VideoDetialActivity.this);
		listview = pullDownView.getListView();
		commentAdapte = new MainItemCommentAdapter(VideoDetialActivity.this, inflater, app, commentList, ab, edit_review, -1);
		listview.setAdapter(commentAdapte);
		listview.setDivider(null);
		listview.setScrollBarSize(0);
		pullDownView.setShowFooter();
		pullDownView.setHideHeader();
		pullDownView.setShowHeaderLayout(mRelativeLayout);
		
		pictureGrideView = (GridView) findViewById(R.id.pictureGrideView);
		pictureAdapter = new PictureAdapter(this);
		pictureGrideView.setAdapter(pictureAdapter);
		
		hintAdapter = new MainItemHintAdapter(VideoDetialActivity.this, inflater, hintList);
		main_item_hint_list.setAdapter(hintAdapter);
		
		onRefresh();
		
	}
	
	@Override
	@SuppressLint("NewApi")
	protected void setListener() {
		
		if (!"-999".equals(bean.getUser_id())) {
			item_detial_icon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					if (bean.getUser_id().equals(Config.getUser(context).uid)) {
						intent.setClass(context, PersonalCenterActivity.class);
						intent.putExtra("falg_return", true);
					} else {
						intent.setClass(context, UserInfo.class);
						intent.putExtra("uid", bean.getUser_id());
					}
					context.startActivity(intent);
				}
			});
		}
		
		video_to_fullScreen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
//				 RelativeLayout.LayoutParams layoutParams=  new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
//				 layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//				 layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//				 layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//				 layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//				 video_layout.setLayoutParams(layoutParams);
				
//				VideoFullScreenActivity.VIDEO_TIME = video_layout.getCurrentPosition();
//				VideoFullScreenActivity.VIDEO_URL = VIDEO_URL;
				
				Intent intent = new Intent(VideoDetialActivity.this, VideoFullScreenActivity.class);
				intent.putExtra(VideoFullScreenActivity.IMG_ID_KEY, bean.img_id);
				VideoDetialActivity.this.startActivity(intent);
			}
		});
		
		item_detial_from.setOnClickListener(new OnClickListener() {
				
			@Override
			public void onClick(View arg0) {
				if(bean.is_group_tag.equals("1")){
					if(!bean.group_tag_id.isEmpty()){
						GroupRelevantUtils.CheckIntent(context, bean.group_tag_id);
					}else{
						GroupRelevantUtils.CheckIntent(context, bean.group_id);
					}
				}else if(bean.is_group_tag.equals("2")){
					Intent intent = new Intent();
					intent.setClass(context, PostBarActivity.class);
					intent.putExtra("img_title", bean.img_title);
					if(bean.group_id.isEmpty()){
						intent.putExtra("tag_id", bean.group_tag_id);
					}else{
						intent.putExtra("tag_id", bean.group_id);
					}
					intent.putExtra("img_ids", bean.img_id);
					context.startActivity(intent);
				}else if(bean.is_group_tag.equals("3")){
					Intent intent = new Intent(VideoDetialActivity.this, MainItemDetialActivity.class);
					intent.putExtra("img_id", bean.img_id);
					context.startActivity(intent);
				}else if(bean.is_group_tag.equals("4")){
					Intent intent = new Intent(VideoDetialActivity.this, VideoDetialActivity.class);
					PostBarTypeItem postBean = new PostBarTypeItem();
					postBean.setImg_id(bean.img_id);
					postBean.setUser_id(bean.user_id);
					postBean.setVideo_url(bean.video_url);
					postBean.setImg(bean.img);
					postBean.setImg_thumb_width(bean.img_thumb_width);
					postBean.setImg_thumb_height(bean.img_thumb_height);
					Bundle bun = new Bundle();
					bun.putSerializable(VideoDetialActivity.VIIDEO_INFO, postBean);
					intent.putExtras(bun);
					context.startActivity(intent);
				}
			}
		});

		
		video_detial_shared.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showShare();
			}
		});
		
		add_useer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(bean.isFriends){
					addToFriends(ServerMutualConfig.CancelFocus);
				}else{
					addToFriends(ServerMutualConfig.FocusOn);
				}
			}
		});
		
		save_post.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(bean.isSavePost){
					savePost(ServerMutualConfig.CANCEL_COLLECTION);
				}else{
					savePost(ServerMutualConfig.ADD_COLLECTION);
				}
			}
		});
		
		video_finish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				VideoDetialActivity.this.finish();
			}
		});
		
		video_play_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(video_stop.isShown() || video_start.isShown()){
					video_stop.setVisibility(View.GONE);
					video_start.setVisibility(View.GONE);
					return;
				}
				
				if(video_layout.isPlaying()){
					video_stop.setVisibility(View.VISIBLE);
					video_start.setVisibility(View.GONE);
				}else{
					video_stop.setVisibility(View.GONE);
					video_start.setVisibility(View.VISIBLE);
				}
			}
		});
		
		video_start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(video_layout.isPlaying()){
					video_start.setVisibility(View.GONE);
				}else{
					video_type.setBackground(getResources().getDrawable(R.drawable.video_stop));
					video_layout.start();
					video_start.setVisibility(View.GONE);
				}
			}
		});
		
		video_stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(video_layout.isPlaying()){
					video_layout.pause();
					video_stop.setVisibility(View.GONE);
				}else{
					video_stop.setVisibility(View.GONE);
				}
			}
		});
		
		video_layout.setOnPreparedListener(new OnPreparedListener() {  
			
			@Override  
			public void onPrepared(MediaPlayer mediaPlayer) { 
				indexPosition = -1;
				if(Integer.parseInt(bean.img_thumb_width) > Integer.parseInt(bean.img_thumb_height)){
					ViewTreeObserver vto2 =  video_layout.getViewTreeObserver();    
					vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
						@Override    
						public void onGlobalLayout() {  
							video_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);    
							video_layout.setLayoutParams(MyApplication.getVideoPlayerParams(R.id.item_layout));
						}    
					});
				}
				
				video_play_console.setVisibility(View.VISIBLE);
				iv_video_bg_up.setVisibility(View.GONE);
				iv_video_loading.setVisibility(View.GONE);
				iv_video_bg_hint.setVisibility(View.GONE);
			}  
		});  
		
		video_layout.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				indexPosition = -1;
				video_start.setVisibility(View.VISIBLE);
				video_type.setBackground(getResources().getDrawable(R.drawable.video_start));
			}
		});
		
		video_player_all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(video_layout.isPlaying()){
					video_start.setVisibility(View.GONE);
					video_stop.setVisibility(View.VISIBLE);
				}else{
					video_start.setVisibility(View.VISIBLE);
					video_stop.setVisibility(View.GONE);
				}
			}
		});
		
		faceBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (custom_face_layout.view.getVisibility() == View.VISIBLE) {
					custom_face_layout.hideFaceView();
				} else {
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(VideoDetialActivity.this
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
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(VideoDetialActivity.this
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
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
					if(sendIndex == 0){
						genTie();
					}else{
						addComment02();
					}
				}
			}
		});
	}

	@Override  
    public void onLayoutChange(View v, int left, int top, int right,  
            int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {  
          
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起  
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){
        	if( isEditType){
        		if(sendIndex == 0){
        			picture.setVisibility(View.VISIBLE);
        		}else{
        			picture.setVisibility(View.INVISIBLE);
        		}	
        	}else{
        		 isEditType = true;
        	}
        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){  
        	if( isEditType){
        		 sendIndex = 0;
        		picture.setVisibility(View.VISIBLE);
        	}else{
        		 isEditType = true;
        	}
        }  
	}
	
	
	@Override
	public void onRefresh() {
		
		/**
		 * 获取评论列表
		 * */
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(this).uid);
		params.put("user_id", bean.getUser_id());
		params.put("img_id", root_img_id);
//		params.put("post_create_time", post_create_time);
		ab.get(ServerMutualConfig.POST_COMMENT_DETIAL + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						pullDownView.RefreshComplete();
						pullDownView.notifyDidMore();
						try {
							commentList.clear();
							MainItemCommentBean.fromJson(new JSONObject(content).getJSONArray("data"), commentList);
							commentAdapte.notifyDataSetChanged();
							
							if(commentList.size() == 0){
								pullDownView.setHideFooter();
								item_comment_title.setText("评论抢走第一个沙发吧！");
							}else{
								pullDownView.setShowFooter();
								item_comment_title.setText("评论");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Config.dismissProgress();
					}

					@Override
					public void onFinish() {
						super.onFinish();
						pullDownView.RefreshComplete();
						pullDownView.notifyDidMore();
						Config.dismissProgress();
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}
				});
		
		
		/**
		 * 获取视频详情
		 * */
		ab.get(ServerMutualConfig.VIDEO_DETIAL + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@SuppressLint("NewApi")
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject jsonCon = new JSONObject(content).getJSONObject("data");
							
							bean.is_group_tag = jsonCon.getString("is_group_tag");
							bean.group_tag_id = jsonCon.getString("group_tag_id");
							bean.group_name = jsonCon.getString("group_tag_name");
							bean.img_title = jsonCon.getString("img_title");
							bean.img = jsonCon.getString("img_thumb");
							bean.description = jsonCon.getString("img_description");
							
							app.display(jsonCon.getString("avatar"), item_detial_icon, R.drawable.deft_color);
							video_title.setText(jsonCon.getString("img_title"));
							video_info.setText(jsonCon.getString("img_description"));
							item_detial_username.setText(jsonCon.getString("user_name"));
							item_detial_time.setText(jsonCon.getString("create_time"));
							video_play_count.setText(jsonCon.getString("post_count") + "次播放");
							user_id = jsonCon.getString("user_id");
							zanNumber = jsonCon.getString("admire_count");
							
							// 是否收藏本帖
							if(jsonCon.getString("is_save").equals("1")){
								bean.isSavePost = true;
							}
							
							// 是否关注了此人
							if(jsonCon.getString("is_idol").equals("1")){
								bean.isFriends = true;
							}else if(jsonCon.getString("is_idol").equals("2")){
								bean.isFriends = true;
							}
							
							if(bean.img_thumb_height.isEmpty() || bean.img_thumb_width.isEmpty()){
								bean.img_thumb_height = jsonCon.getString("img_thumb_height");
								bean.img_thumb_width = jsonCon.getString("img_thumb_width");
								if(AbAppUtil.isWifi(VideoDetialActivity.this)){
									videoPlay();
								}else{
									showHintDialog();
								};
							}
							
							// 是否点赞
							if(jsonCon.getString("is_admire").equals("0")){
								isZan = false;
							}else{
								isZan = true;
							}
							
							if(video_title.getText().toString().trim().isEmpty()){
								video_title.setVisibility(View.GONE);
							}
							
							if(video_info.getText().toString().trim().isEmpty()){
								video_info.setVisibility(View.GONE);
							}
							
							if(bean.isFriends){
								if(jsonCon.getString("is_idol").equals("2")){
									add_useer.setClickable(false);
								}
								add_useer.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_isadd));									
							}else{
								add_useer.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_noadd));
							}
							
							if(bean.isSavePost){
								save_post.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_savepost_down));
							}else{
								save_post.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_savepost_up));
							}
							
							if(bean.is_group_tag.equals("0")){
								item_detial_from.setVisibility(View.GONE);
							}else{
								item_detial_from.setText(bean.group_name);
							}
							
							
							item_detial_from.setText(bean.group_name);
							
							initZanInfo(); // 初始化点赞模块
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Config.dismissProgress();
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
		
		ab.get(ServerMutualConfig.getRecommendList  + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							hintList.clear();
							for(int i = 0 ; i < new JSONObject(content).getJSONArray("data").length() ; i++){
								MainItemHintBean.fromJson(new JSONObject(content).getJSONArray("data").getJSONObject(i), hintList);
							}
							hintAdapter.notifyDataSetChanged();
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

	@Override
	public void onMore() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(this).uid);
		params.put("img_id", root_img_id);
		
		if(null != commentList && commentList.size() != 0){
			params.put("post_create_time", commentList.get(commentList.size()-1).getPost_create_time());
		}else{
			params.put("post_create_time", post_create_time);
		}
		
		ab.post(ServerMutualConfig.POST_COMMENT_DETIAL + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						pullDownView.RefreshComplete();
						pullDownView.notifyDidMore();
						try {
							MainItemCommentBean.fromJson(new JSONObject(content).getJSONArray("data"), commentList);
							commentAdapte.notifyDataSetChanged();
							Config.dismissProgress();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						pullDownView.RefreshComplete();
						pullDownView.notifyDidMore();
						Config.dismissProgress();
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}
				});
	}
	
	/**
	 * 提示网络情况
	 * */
	private void showHintDialog(){
		
		dialog = new Builder(VideoDetialActivity.this);
		dialog.setTitle("温馨提示");
		dialog.setMessage("您当前并未连接WIFI，继续播放将使用手机流量，是否继续？");
		dialog.setNegativeButton("继续",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						videoPlay();
					}

				});
		dialog.setPositiveButton("返回",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						VideoDetialActivity.this.finish();
					}
				});
		dialog.create().show();
	}
	
	/**
	 * 开始播放
	 * */
	@SuppressLint("NewApi")
	private void videoPlay(){
		
		iv_video_thumb.setVisibility(View.GONE);
		video_start.setVisibility(View.GONE);
		iv_video_bg_hint.setVisibility(View.VISIBLE);
		iv_video_loading.setVisibility(View.VISIBLE);
		
		if(bean.video_url.isEmpty()){
			bean.video_url = VIDEO_URL;
		}
		Uri uri = Uri.parse(bean.video_url);
		MediaController mc = new MediaController(getActivity());
		mc.setVisibility(View.INVISIBLE); // 不使用原生的控制按钮和进度条
		video_layout.setMediaController(mc);
		video_layout.setVideoURI(uri);
		video_layout.requestFocus();
		video_layout.start();
	}


	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		activityRootView.addOnLayoutChangeListener(this);
		
		Intent in = getIntent();
		if(null != in.getSerializableExtra(VIIDEO_INFO)){
			bean = (PostBarTypeItem) in.getSerializableExtra(VIIDEO_INFO);
			root_img_id = bean.img_id;
			if(!bean.video_url.isEmpty()){
				VIDEO_URL = bean.video_url;
			}
		}else if(null != in.getStringExtra("img_id")){
			root_img_id = in.getStringExtra("img_id");
			bean.setImg_id(root_img_id);
		}
		
		if(!bean.img_thumb_height.isEmpty() && !bean.img_thumb_width.isEmpty()){
			VideoDetialActivity.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					if(AbAppUtil.isWifi(VideoDetialActivity.this)){
						videoPlay();
					}else{
						showHintDialog();
					};
				}
			});
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		edit_review.setHint("");
	}

	
	protected Activity getActivity() {
		return this;
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
						new PopupWindowsPicture(VideoDetialActivity.this,
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
							
							 sendIndex = 0; // 0=直接评论，1=回复评论or跟评
							 indexId = ""; // 被选中的评论的id
							 isEditType = true; // 是否切换评论/跟评状态
							
							BimpUtil.drr.clear();
							BimpUtil.time.clear();
							BimpUtil.bmp.clear();
							PhotoEdit.imgUris = "";
							edit_review.setText("");
							msgString = "";
							((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
									.hideSoftInputFromWindow(
											VideoDetialActivity.this
													.getCurrentFocus()
													.getWindowToken(),
											InputMethodManager.HIDE_NOT_ALWAYS);
							custom_face_layout.hideFaceView();
							pictureGrideView.setVisibility(View.GONE);
							
							onRefresh();
						}
					});
			super.handleMessage(msg);

		}
	};
	
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
						intent.putExtra("tag", "video");
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
						intent.putExtra("tag", "video");
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TAKE_PICTURE:
			Intent intent2 = new Intent();
			intent2.setClass(context, CropImage.class);
			intent2.putExtra("tag", "video");
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

		/**
		 * 跟帖
		 * 
		 * */
		private void genTie() {
			if (BimpUtil.drr.size() == 0 && PhotoEdit.imgUris == null) {
				handler.sendEmptyMessage(0);
			} else {
				msgString = edit_review.getText().toString().trim();
				params.put("login_user_id", Config.getUser(context).uid);
				params.put("img_id", root_img_id);
				params.put("user_id", Config.getUser(context).uid);
				params.put("demand", msgString);
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
		
		/**
		 * 添加跟评
		 * 
		 * */
		private void addComment02(){
			AbRequestParams params = new AbRequestParams();
			params.put("login_user_id", Config.getUser(context).uid); // 用户id
			params.put("review_id",  indexId); 
			params.put("demand", edit_review.getText().toString().trim()); // 评论或者回复评论的内容
			ab.post(ServerMutualConfig.PostReviewNew, params,
					new AbStringHttpResponseListener() {
				
						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							Config.dismissProgress();
							try {
								JSONObject json = new JSONObject(content);
								if (json.getBoolean("success")) {
									
									sendIndex = 0; // 0=直接评论，1=回复评论or跟评
									indexId = ""; // 被选中的评论的id
									isEditType = true; // 是否切换评论/跟评状态
									
									MainItemDetialActivity.sendIndex = 0;
									MainItemDetialActivity.indexId = "";
									 
									BimpUtil.drr.clear();
									BimpUtil.time.clear();
									BimpUtil.bmp.clear();
									PhotoEdit.imgUris = "";
									edit_review.setText("");
									msgString = "";
									((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(VideoDetialActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
									custom_face_layout.hideFaceView();
									pictureGrideView.setVisibility(View.GONE);
									initCommentList();
									Toast.makeText(context, json.getString("reMsg"), Toast.LENGTH_SHORT).show();
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
						public void onFailure(int statusCode,
								String content, Throwable error) {
							super.onFailure(statusCode, content, error);
							Config.showFiledToast(context);
						}
					});
		}

		
		/**
		 * 获取评论列表
		 * 
		 * */
		private void initCommentList(){
			AbRequestParams params = new AbRequestParams();
			params.put("login_user_id", Config.getUser(this).uid);
			params.put("user_id", bean.getUser_id());
			params.put("img_id", root_img_id);
			ab.get(ServerMutualConfig.POST_COMMENT_DETIAL + "&" + params.toString(),
					new AbStringHttpResponseListener() {
						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							pullDownView.RefreshComplete();
							pullDownView.notifyDidMore();
							try {
								commentList.clear();
								MainItemCommentBean.fromJson(new JSONObject(content).getJSONArray("data"), commentList);
								commentAdapte.notifyDataSetChanged();
								
								if(commentList.size() == 0){
									pullDownView.setHideFooter();
									item_comment_title.setText("评论抢走第一个沙发吧！");
								}else{
									pullDownView.setShowFooter();
									item_comment_title.setText("评论");
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							Config.dismissProgress();
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
				convertView.setOnClickListener(null);
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
		
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 点赞相关
	 * */
	private void initZanInfo(){
		
		item_is_zan_number.setText(zanNumber);
		zanNumber = item_is_zan_number.getText().toString();
		if (isZan) {
			item_is_zan_icon.setBackgroundResource(R.drawable.comment_main_zan_yes);
		} else {
			item_is_zan_icon.setBackgroundResource(R.drawable.comment_main_zan_no);
		}
		
		item_is_zan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Config.showProgressDialog(context, false, null);
				if (!isZan) {
					AbRequestParams params = new AbRequestParams();
					params.put("login_user_id", Config.getUser(context).uid);
					params.put("admire_user_id", user_id);
					params.put("img_id", bean.getImg_id());
					ab.post(ServerMutualConfig.PostAdmireNew, params,
							new AbStringHttpResponseListener() {
								@Override
								public void onSuccess(int statusCode, String content) {
									super.onSuccess(statusCode, content);
									Config.dismissProgress();
									try {
										if(new JSONObject(content).getBoolean("success")){
											isZan = true;
											zanNumber = (Integer.parseInt(zanNumber) + 1) + "";
											item_is_zan_number.setText(zanNumber);
											item_is_zan_icon.setBackgroundResource(R.drawable.comment_main_zan_yes);
										}
										Toast.makeText(context, new JSONObject(content).getString("reMsg"), 3).show();
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
								public void onFailure(int statusCode,
										String content, Throwable error) {
									super.onFailure(statusCode, content, error);
									Config.showFiledToast(context);
								}
							});
				} else {
					AbRequestParams params = new AbRequestParams();
					params.put("login_user_id", Config.getUser(context).uid);
					params.put("admire_user_id", user_id);
					params.put("img_id", bean.getImg_id());
					ab.post(ServerMutualConfig.CancelPostAdmireNew, params,
							new AbStringHttpResponseListener() {
								@Override
								public void onSuccess(int statusCode,
										String content) {
									super.onSuccess(statusCode, content);
									Config.dismissProgress();
									try {
										if(new JSONObject(content).getBoolean("success")){
											isZan = false;
											zanNumber = (Integer.parseInt(zanNumber) - 1) + "";
											item_is_zan_number.setText(zanNumber);
											item_is_zan_icon.setBackgroundResource(R.drawable.comment_main_zan_no);
										}
										Toast.makeText(context, new JSONObject(content).getString("reMsg"), 3).show();
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
								public void onFailure(int statusCode,
										String content, Throwable error) {
									super.onFailure(statusCode, content, error);
									Config.showFiledToast(context);
								}
							});
				}
			}
		});
		
	}
	
	/**
	 * 视频操控区初始化
	 * */
	private void initVideoConsole(){
		
		video_layout = (myVideoPlayerView) findViewById(R.id.video_layout);
		video_type = (Button) findViewById(R.id.video_type);
		skbProgress = (SeekBar) findViewById(R.id.skbProgress);
		video_all_length = (TextView) findViewById(R.id.video_all_length);
		video_new_time = (TextView) findViewById(R.id.video_new_time);
		video_play_console = (LinearLayout) findViewById(R.id.video_play_console);
		video_play_console.setVisibility(View.GONE);
		item_layout = (RelativeLayout) findViewById(R.id.item_layout);
		
		item_layout.setLayoutParams(MyApplication.getVideoPlayerParams(-1));
		
		video_type.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if(video_layout.isPlaying()){
					video_layout.pause();
					video_type.setBackground(getResources().getDrawable(R.drawable.video_start));
				}else{
					video_layout.start();
					video_start.setVisibility(View.GONE);
					video_type.setBackground(getResources().getDrawable(R.drawable.video_stop));
				}
			}
		});
		
		skbProgress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			int progress;
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				this.progress = progress * video_layout.getDuration() / seekBar.getMax();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				video_layout.seekTo(progress);
			}
		});
		
		Timer videoPlayerTimer = new Timer();
		videoPlayerTimer.schedule(mTimerTask, 0, 50);
		
	}
	
	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			if (video_layout == null){
				return;
			}
			if (video_layout.isPlaying() && skbProgress.isPressed() == false) {
				handleProgress.sendEmptyMessage(0);
			}
		}
	};
	
	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {

			position = video_layout.getCurrentPosition();
			duration = video_layout.getDuration();

			VideoDetialActivity.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Date date = new Date(duration);
					SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
					String dateStr = sdf.format(date);
					
					date = new Date(position);
					SimpleDateFormat sdf02 = new SimpleDateFormat("mm:ss");
					String dateStr02 = sdf02.format(date);
					
					if (duration > 0 && position > indexPosition) {
						video_all_length.setText(dateStr);
						video_new_time.setText(dateStr02 + "");
						long pos = skbProgress.getMax() * position / duration;
						skbProgress.setProgress((int) pos);
						indexPosition = position;
					}
				}
			});
		};
	};
	
	/**
	 * 添加收藏
	 * */
	private void savePost(String url){
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("img_id", root_img_id);
		ab.post( url, params, new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode,
							String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							if(json.getBoolean("success") || json.getString("success").equals("true")){
								
								if(bean.isSavePost){
									bean.isSavePost = false;
								}else{
									bean.isSavePost = true;
								}
								
								if(bean.isSavePost){
									save_post.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_savepost_down));
								}else{
									save_post.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_savepost_up));
								}
								
								Toast.makeText(context, json.getString("reMsg"), Toast.LENGTH_SHORT).show();
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
					public void onFailure(int statusCode,
							String content, Throwable error) {
						super.onFailure(statusCode, content,
								error);
						Config.showFiledToast(context);
					}
				});
	}
	
	/**
	 * 添加或取消好友
	 * */
	private void addToFriends(String url){
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
		params.put("idol_id", bean.getUser_id());
		ab.post(url, params, new AbStringHttpResponseListener() {
			@Override
			public void onSuccess(int statusCode,
					String content) {
				super.onSuccess(statusCode, content);
				try {
					JSONObject json = new JSONObject(content);
					if (json.getBoolean("success")) {
						if(bean.isFriends){
							bean.isFriends = false;
						}else{
							bean.isFriends = true;
						}
					}
					if(bean.isFriends){
						add_useer.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_isadd));
					}else{
						add_useer.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_noadd));
					}
					
					Toast.makeText(context,json.getString("reMsg"),Toast.LENGTH_SHORT).show();
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
			public void onFailure(int statusCode,
					String content, Throwable error) {
				super.onFailure(statusCode, content, error);
				Config.showFiledToast(context);
			}
		});
	}

	private void showShare() {
		// ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.setSilent(true);
//		oks.setNotification(R.drawable.icon, getString(R.string.app_name)); // 分享时Notification的图标和文字
		// 去除注释，演示在九宫格设置自定义的图标
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {

			@Override
			public void onShare(Platform platform, ShareParams paramsToShare) {
				if ("Wechat".equals(platform.getName())) {
					paramsToShare.setShareType(Platform.SHARE_TEXT);
					if(bean.img_title.isEmpty()){
						paramsToShare.setTitle("自由环球租赁");
					}else{
						paramsToShare.setTitle(bean.img_title);
					}
					if(bean.description.isEmpty()){
						paramsToShare.setText("精彩宝宝成长美一瞬间");
					}else{
						paramsToShare.setText(bean.description);
					}
					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
					paramsToShare.setImageUrl(bean.img);
					paramsToShare.setUrl("http://www.meimei.yihaoss.top/fenxiang/video.html?" + "img_id=" + bean.img_id + "&user_id=" + bean.user_id);
				} else if ("SinaWeibo".equals(platform.getName())) {
					paramsToShare.setText(bean.img_title + "http://www.meimei.yihaoss.top/fenxiang/video.html?" + "img_id=" + bean.img_id + "&user_id=" + bean.user_id);
					paramsToShare.setImageUrl(bean.img);
				} else if ("QZone".equals(platform.getName())) {
					paramsToShare.setTitle(bean.img_title);
					paramsToShare.setTitleUrl("http://www.meimei.yihaoss.top/fenxiang/video.html?" + "img_id=" + bean.img_id + "&user_id=" + bean.user_id);
					paramsToShare.setText(bean.description);
					paramsToShare.setImageUrl(bean.img);
					paramsToShare.setSite(getString(R.string.app_name));
					paramsToShare.setSiteUrl("http://www.meimei.yihaoss.top/fenxiang/video.html?" + "img_id=" + bean.img_id + "&user_id=" + bean.user_id);
				} else if ("QQ".equals(platform.getName())) {
					paramsToShare.setTitle("");
					paramsToShare.setTitleUrl("http://www.meimei.yihaoss.top/fenxiang/video.html?" + "img_id=" + bean.img_id + "&user_id=" + bean.user_id);
					paramsToShare.setText(bean.img_title);
					paramsToShare.setImageUrl(bean.img);
				} else if ("WechatMoments".equals(platform.getName())) {
					paramsToShare.setShareType(Platform.SHARE_TEXT);
					paramsToShare.setTitle(bean.img_title);
					paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
					paramsToShare.setImageUrl(bean.img);
					paramsToShare.setUrl("http://www.meimei.yihaoss.top/fenxiang/video.html?" + "img_id=" + bean.img_id + "&user_id=" + bean.user_id);
				}
			}
		});
		// 启动分享GUI
		oks.show(this);
	}

	
}
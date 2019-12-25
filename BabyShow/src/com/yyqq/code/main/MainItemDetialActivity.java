package com.yyqq.code.main;

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
import android.view.View.OnLayoutChangeListener;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.mob.MobSDK;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.personal.PersonalCenterActivity;
import com.yyqq.code.personal.UserInfo;
import com.yyqq.code.photo.BitmapCache;
import com.yyqq.code.photo.ChangePhotoSize;
import com.yyqq.code.photo.CropImage;
import com.yyqq.code.photo.PhoneAlbumActivity;
import com.yyqq.code.photo.PhotoEdit;
import com.yyqq.code.postbar.PostReviewList;
import com.yyqq.code.video.VideoDetialActivity;
import com.yyqq.commen.adapter.MainItemDetialAdapter;
import com.yyqq.commen.model.MainItemBodyBean;
import com.yyqq.commen.model.MainItemBodyItemBean;
import com.yyqq.commen.model.MainItemCommentBean;
import com.yyqq.commen.model.MainItemHintBean;
import com.yyqq.commen.model.PostShowItem;
import com.yyqq.commen.model.PostShowItem.Image;
import com.yyqq.commen.model.PostShowItem.Review;
import com.yyqq.commen.utils.BimpUtil;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.FaceConversionUtil;
import com.yyqq.commen.utils.FileUtils;
import com.yyqq.commen.utils.ImageDownloader;
import com.yyqq.commen.utils.OnImageDownload;
import com.yyqq.commen.utils.Rank;
import com.yyqq.commen.utils.RoundAngleImageView;
import com.yyqq.commen.utils.WebViewActivity;
import com.yyqq.commen.view.FaceRelativeLayout;
import com.yyqq.commen.view.MyGridView;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.activity.BaseActivity;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class MainItemDetialActivity extends BaseActivity implements OnPullDownListener,OnLayoutChangeListener{
	
	public static int sendIndex = 0; // 0=直接评论，1=回复评论or跟评
	public static String indexId = ""; // 被选中的评论的id
	public static boolean isEditType = true; // 是否切换评论/跟评状态
	
	private ImageView main_item_back;
	private ImageView main_item_more;
	private PullDownView main_item_list;
	private ListView listview;
	private ImageButton faceBtn;
	private ImageButton picture;
	private FaceRelativeLayout custom_face_layout;
	private EditText edit_review;
	private GridView pictureGrideView;
	private LinearLayout bottom_root;
	private RelativeLayout title;
	private ImageButton reviewBtn;
	private ImageButton save_post;
	private ImageView main_item_update;
	
	private boolean bodyEnd = false;
	private boolean commentEnd = false;
	private boolean hintEnd = false;
	private AbHttpUtil ab;
	private Activity context;
	private MyApplication app;
	private String itemId = "";
//	private String userid = "";
	private MainItemDetialAdapter mainItemAdapter;
	private LayoutInflater inflater;
	private ArrayList<MainItemBodyBean> bodyList = new ArrayList<MainItemBodyBean>(); // 文章主体
	private ArrayList<MainItemCommentBean> commentList = new ArrayList<MainItemCommentBean>(); // 评论集合
	private ArrayList<MainItemHintBean> hintList = new ArrayList<MainItemHintBean>(); // 推荐集合
	private ArrayList<PostShowItem> dataPostShow = new ArrayList<PostShowItem>();
	private MainItemBodyItemBean itemBean = new MainItemBodyItemBean();
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
	private boolean collectionCheck;
	public static MainItemDetialActivity mainItemDetialActivity = null;
	  
    //Activity最外层的Layout视图  
    private View activityRootView;  
    //屏幕高度  
    private int screenHeight = 0;  
    //软件盘弹起后所占高度阀值  
    private int keyHeight = 0;  
	
	//分享的参数
	private String share_title = "";
	private String share_img = "";
	private String share_text = "";
	private String share_url = "";
	private String share_defaultImg = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		setContentView(R.layout.main_item_detail);
	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void initView() {
		
		activityRootView = findViewById(R.id.main_item_all);  
		//获取屏幕高度  
		screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();  
		//阀值设置为屏幕高度的1/3  
		keyHeight = screenHeight/3;  
	        
		main_item_update = (ImageView) findViewById(R.id.main_item_update);
		main_item_back = (ImageView) findViewById(R.id.main_item_back);
		main_item_more = (ImageView) findViewById(R.id.main_item_more);
		main_item_list = (PullDownView) findViewById(R.id.main_item_list);
		
		main_item_list.setOnPullDownListener(this);
		listview = main_item_list.getListView();
		
		faceBtn = (ImageButton) findViewById(R.id.face);
		picture = (ImageButton) findViewById(R.id.picture);
		edit_review = (EditText) findViewById(R.id.text);
		custom_face_layout = (FaceRelativeLayout) findViewById(R.id.FaceRelativeLayout);
		custom_face_layout.setEditText(edit_review);
		bottom_root = (LinearLayout) findViewById(R.id.bottom_root);
		title = (RelativeLayout) findViewById(R.id.title);
		reviewBtn = (ImageButton) findViewById(R.id.send);
		save_post = (ImageButton) findViewById(R.id.save_post);
		bottom_root.setVisibility(View.VISIBLE);
	}

	@Override
	protected void initData() {
		
		uid = "";
		bottom_root = (LinearLayout) findViewById(R.id.bottom_root);
		if (getIntent().hasExtra("user_id")) {
			uid = getIntent().getStringExtra("user_id");
		}
		
		if (getIntent().hasExtra("back")) {
			back = getIntent().getStringExtra("back");
		}
		
		if (getIntent().hasExtra("is_save")) {
			is_save = getIntent().getStringExtra("is_save");
		}
		
		root_img_id = getIntent().getStringExtra("img_id");
		
		context = this;
		MobSDK.init(context);
		app = (MyApplication) context.getApplication();
		ab = AbHttpUtil.getInstance(context);
		inflater = LayoutInflater.from(context);
		mainItemDetialActivity = this;
		
		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		width = DM.widthPixels;
		height = DM.heightPixels;
		
		if (mainItemAdapter == null) {
			mainItemAdapter = new MainItemDetialAdapter(MainItemDetialActivity.this, inflater, app, itemBean, commentList, ab, hintList, edit_review);
		}
		listview.setDivider(null);
		listview.setAdapter((ListAdapter) mainItemAdapter);
		main_item_list.enableAutoFetchMore(true, 1);// 设置可以自动获取更多 滑到最后一个自动获取
		main_item_list.setHideHeader();// 显示并且可以使用头部刷新
		
		itemId = getIntent().getStringExtra("img_id"); 
//		userid = getIntent().getStringExtra("user_id"); 
		
		pictureGrideView = (GridView) findViewById(R.id.pictureGrideView);
		pictureAdapter = new PictureAdapter(this);
		pictureGrideView.setAdapter(pictureAdapter);
		
//		onRefresh();
	}

	@Override
	protected void setListener() {
		
		main_item_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, AddNewPostContinueActivity.class);
				intent.putExtra("root_img_id", root_img_id);
				startActivity(intent);
			}
		});
		
		save_post.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(itemBean.isSave()){
					savePost(ServerMutualConfig.CANCEL_COLLECTION);
				}else{
					savePost(ServerMutualConfig.ADD_COLLECTION);
				}
			}
		});
		
		// 返回
		main_item_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainItemDetialActivity.this.finish();
			}
		});
		
		// 分享
		main_item_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				showShare();
			}
		});
		
		faceBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (custom_face_layout.view.getVisibility() == View.VISIBLE) {
					custom_face_layout.hideFaceView();
				} else {
					isEditType = false;
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(MainItemDetialActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
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
							.hideSoftInputFromWindow(MainItemDetialActivity.this
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
	public void onRefresh() {
		
		bodyEnd = false;
		commentEnd = false;
		hintEnd = false;
		
		Config.showProgressDialog(context, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("img_id", itemId);
		ab.get(ServerMutualConfig.POSTBAR_DETIAL + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						main_item_list.RefreshComplete();
						main_item_list.notifyDidMore();
						bodyEnd = true;
						try {
							itemBean.getHandleList().clear();
							
							if(!new JSONObject(content).getBoolean("success")){
								MainItemDetialActivity.this.finish();
							}
							
							MainItemBodyBean.fromJson(new JSONObject(content).getJSONArray("data"), itemBean);
							mainItemAdapter.notifyDataSetChanged();
							
							// 显示编辑按钮
							if(itemBean.getUser_id().equals(Config.getUser(context).uid+"")){
								main_item_update.setVisibility(View.VISIBLE);
							}
							
							// 是否可收藏
							if(itemBean.isSave()){
								save_post.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_savepost_down));
							}else{
								save_post.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_savepost_up));
							}
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if(bodyEnd && commentEnd && hintEnd){
							Config.dismissProgress();
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						bodyEnd = true;
						if(bodyEnd && commentEnd && hintEnd){
							Config.dismissProgress();
						}
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}
				});
		
		ab.get(ServerMutualConfig.POST_COMMENT_DETIAL + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						main_item_list.RefreshComplete();
						main_item_list.notifyDidMore();
						commentEnd = true;
						try {
							if(new JSONObject(content).getBoolean("success")){
								commentList.clear();
								MainItemCommentBean.fromJson(new JSONObject(content).getJSONArray("data"), commentList);
								mainItemAdapter.notifyDataSetChanged();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if(bodyEnd && commentEnd && hintEnd){
							Config.dismissProgress();
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						main_item_list.RefreshComplete();
						main_item_list.notifyDidMore();
						commentEnd = true;
						if(bodyEnd && commentEnd && hintEnd){
							Config.dismissProgress();
						}
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}
				});
		
		ab.get(ServerMutualConfig.getRecommendList  + "&" + params.toString(), new AbStringHttpResponseListener() {
			
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						main_item_list.RefreshComplete();
						main_item_list.notifyDidMore();
						hintEnd = true;
						try {
							hintList.clear();
							if(new JSONObject(content).getBoolean("success")){
								for(int i = 0 ; i < new JSONObject(content).getJSONArray("data").length() ; i++){
									MainItemHintBean.fromJson(new JSONObject(content).getJSONArray("data").getJSONObject(i), hintList);
								}
								mainItemAdapter.notifyDataSetChanged();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if(bodyEnd && commentEnd && hintEnd){
							Config.dismissProgress();
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						main_item_list.RefreshComplete();
						main_item_list.notifyDidMore();
						hintEnd = true;
						if(bodyEnd && commentEnd && hintEnd){
							Config.dismissProgress();
						}
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
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("img_id", itemId);
		
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
						main_item_list.RefreshComplete();
						main_item_list.notifyDidMore();
						try {
							MainItemCommentBean.fromJson(new JSONObject(content).getJSONArray("data"), commentList);
							mainItemAdapter.notifyDataSetChanged();
							Config.dismissProgress();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						main_item_list.RefreshComplete();
						main_item_list.notifyDidMore();
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
	 * 获取评论列表
	 * 
	 * */
	private void initCommentList(){
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("img_id", itemId);
		ab.get(ServerMutualConfig.POST_COMMENT_DETIAL + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						main_item_list.RefreshComplete();
						main_item_list.notifyDidMore();
						commentEnd = true;
						try {
							if(new JSONObject(content).getBoolean("success")){
								commentList.clear();
								MainItemCommentBean.fromJson(new JSONObject(content).getJSONArray("data"), commentList);
								mainItemAdapter.notifyDataSetChanged();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Config.dismissProgress();
					}

					@Override
					public void onFinish() {
						super.onFinish();
						main_item_list.RefreshComplete();
						main_item_list.notifyDidMore();
						commentEnd = true;
						if(bodyEnd && commentEnd && hintEnd){
							Config.dismissProgress();
						}
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						super.onFailure(statusCode, content, error);
					}
				});
	}
	
	@Override  
    public void onLayoutChange(View v, int left, int top, int right,  
            int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {  
          
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起  
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){
        	if(isEditType){
        		if(sendIndex == 0){
        			picture.setVisibility(View.VISIBLE);
        		}else{
        			picture.setVisibility(View.INVISIBLE);
        		}	
        	}else{
        		isEditType = true;
        	}
        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){  
        	if(isEditType){
        		sendIndex = 0;
        		picture.setVisibility(View.VISIBLE);
        	}else{
        		isEditType = true;
        	}
        }  
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//添加layout大小发生改变监听器  
        activityRootView.addOnLayoutChangeListener(this);  
        
		MobclickAgent.onResume(this);
		context = this;
		edit_review.setText(msgString);
		
		onRefresh();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		edit_review.setHint("");
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
						new PopupWindowsPicture(MainItemDetialActivity.this,
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
							onMore();
							((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
									.hideSoftInputFromWindow(
											MainItemDetialActivity.this
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((custom_face_layout.view.getVisibility() == View.VISIBLE) || pictureGrideView.getVisibility() == View.VISIBLE) {
			custom_face_layout.hideFaceView();
			pictureGrideView.setVisibility(View.GONE);
			return false;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			context.finish();
			return false;
		}
		return false;
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
		 * 添加评论
		 * 
		 * */
		private void genTie() {
			if (BimpUtil.drr.size() == 0 && PhotoEdit.imgUris == null) {
				handler.sendEmptyMessage(0);
			} else {
				msgString = edit_review.getText().toString().trim();
				params.put("login_user_id", Config.getUser(context).uid);
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
				params.put("img_id", root_img_id);
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
			params.put("review_id", indexId); 
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
									
									VideoDetialActivity.sendIndex = 0;
									VideoDetialActivity.indexId = "";
									
									BimpUtil.drr.clear();
									BimpUtil.time.clear();
									BimpUtil.bmp.clear();
									PhotoEdit.imgUris = "";
									edit_review.setText("");
									msgString = "";
									((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(MainItemDetialActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
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
								
								if(itemBean.isSave()){
									itemBean.setIsSave(false);
								}else{
									itemBean.setIsSave(true);
								}
								
								if(itemBean.isSave()){
									save_post.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_savepost_down));
								}else{
									save_post.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.main_item_savepost_up));
								}
							}
							Toast.makeText(context, json.getString("reMsg"), Toast.LENGTH_SHORT).show();
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
}

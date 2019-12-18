package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.MainItemDetialActivity;
import com.yyqq.code.personal.PersonalCenterActivity;
import com.yyqq.code.personal.UserInfo;
import com.yyqq.code.postbar.PostReviewList;
import com.yyqq.code.video.VideoDetialActivity;
import com.yyqq.commen.model.MainItemCommentBean;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.FaceConversionUtil;
import com.yyqq.commen.view.RecodeListView;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

public class MainItemCommentAdapter extends BaseAdapter {

	private Activity context = null;
	private LayoutInflater inflater = null;
	private MyApplication app = null;
	private ArrayList<MainItemCommentBean> commentList;
	private AbHttpUtil ab;
	private EditText userInput;
	private int index = 0;
	
	public MainItemCommentAdapter(Activity context, LayoutInflater inflater,
			MyApplication app, ArrayList<MainItemCommentBean> commentList, AbHttpUtil ab, EditText userInput, int index) {
		super();
		this.context = context;
		this.inflater = inflater;
		this.app = app;
		this.commentList = commentList;
		this.ab = ab;
		this.userInput = userInput;
		this.index = index;
	}
	
	@Override
	public int getCount() {
		return commentList.size() == 0 ? 0 : commentList.size();
	}

	@Override
	public Object getItem(int position) {
		return commentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_main_detial03_item, null);
		
		ImageView item_comment_icon = (ImageView) convertView.findViewById(R.id.item_comment_icon); // 头像
		LinearLayout item_zan_ly = (LinearLayout) convertView.findViewById(R.id.item_zan_ly); // 赞布局
		LinearLayout item_comment_ly = (LinearLayout) convertView.findViewById(R.id.item_comment_ly); // 评论布局
		final ImageView item_comment_zan_icon = (ImageView) convertView.findViewById(R.id.item_comment_zan_icon); // 点赞图标
		TextView item_comment_username = (TextView) convertView.findViewById(R.id.item_comment_username);
		TextView item_comment_time = (TextView) convertView.findViewById(R.id.item_comment_time);
		final TextView item_comment_number = (TextView) convertView.findViewById(R.id.item_comment_number);
		final TextView item_comment_zan_number = (TextView) convertView.findViewById(R.id.item_comment_zan_number);
		TextView item_detial03_comment = (TextView) convertView.findViewById(R.id.item_detial03_comment);
		TextView main_item_02_lookAll = (TextView) convertView.findViewById(R.id.main_item_02_lookAll);
		GridView item_detial03_photolist = (GridView) convertView.findViewById(R.id.item_detial03_photolist);
		
		app.display(commentList.get(position).getAvatar(), item_comment_icon, R.drawable.def_image);
		item_comment_username.setText(commentList.get(position).getUser_name());
		item_comment_time.setText(commentList.get(position).getPost_time());
		SpannableString spannableString = FaceConversionUtil.getInstace().getExpressionString(context, commentList.get(position).getDemand());
		item_detial03_comment.setText(spannableString);
		
		RecodeListView main_item_04_list = (RecodeListView) convertView.findViewById(R.id.main_item_03_list); // 跟评
		if(null != commentList.get(position).getReviewData() && commentList.get(position).getReviewData().size() != 0){
			MainItemComment02Adapter comment02Adapter = new MainItemComment02Adapter(context, inflater, app, commentList.get(position).getReviewData(), ab, null);
			main_item_04_list.setAdapter(comment02Adapter);
			
			main_item_04_list.setOnItemClickListener(new OnItemClickListener() {
			
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int arg02, long id) {
					MainItemDetialActivity.sendIndex = 1;
					MainItemDetialActivity.indexId = commentList.get(position).getReview_id();
					
					VideoDetialActivity.sendIndex = 1;
					VideoDetialActivity.indexId = commentList.get(position).getReview_id();
					
					userInput.requestFocus();
					InputMethodManager imm = (InputMethodManager) userInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED); 
				}
			});
		}else{
			main_item_04_list.setVisibility(View.GONE);
		}
		
		item_comment_icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				if (commentList.get(position).getUser_id().equals(Config.getUser(context).uid)) {
					intent.setClass(context, PersonalCenterActivity.class);
					intent.putExtra("falg_return", true);
				} else {
					intent.setClass(context, UserInfo.class);
					intent.putExtra("uid", commentList.get(position).getUser_id());
				}
				context.startActivity(intent);
			}
		});
		
		if(commentList.get(position).getImg().size() != 0){
			MainItemCommentPhotoAdapter photoAdapter = new MainItemCommentPhotoAdapter(context, commentList.get(position), app);
			item_detial03_photolist.setAdapter(photoAdapter);
			item_detial03_photolist.setVisibility(View.VISIBLE);
		}
		
		item_comment_number.setText(commentList.get(position).getReview_count());
		item_comment_zan_number.setText(commentList.get(position).getAdmire_count());
		
		if(commentList.get(position).getIs_review().equals("1")){
			main_item_02_lookAll.setVisibility(View.VISIBLE);
			main_item_02_lookAll.setText("查看全部"+commentList.get(position).getReview_count()+"条评论   >>");
			main_item_02_lookAll.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, PostReviewList.class);
					intent.putExtra("img_id", commentList.get(position).getImg_id());
					intent.putExtra("owner_id", commentList.get(position).getReview_id());
					context.startActivity(intent);
				}
			});
		}else{
			main_item_02_lookAll.setVisibility(View.GONE);
		}
		
		if ("1".equals(commentList.get(position).getIs_admire())){
			item_comment_zan_icon.setBackgroundResource(R.drawable.comment_zan_yes);
		} else {
			item_comment_zan_icon.setBackgroundResource(R.drawable.comment_zan_no);
		}
		
		item_zan_ly.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Config.showProgressDialog(context, false, null);
				if ("0".equals(commentList.get(position).getIs_admire())) {
					AbRequestParams params = new AbRequestParams();
					params.put("login_user_id", Config.getUser(context).uid);
					params.put("review_id", commentList.get(position).getReview_id());
					params.put("admire_user_id", commentList.get(position).getUser_id());
					ab.post(ServerMutualConfig.PublicListingReviewAdmire, params,
							new AbStringHttpResponseListener() {
								@Override
								public void onSuccess(int statusCode,
										String content) {
									super.onSuccess(statusCode, content);
									Config.dismissProgress();
									commentList.get(position).setAdmire_count((Integer.parseInt(commentList.get(position).getAdmire_count())+1+""));
									commentList.get(position).setIs_admire("1");
									item_comment_zan_icon.setBackgroundResource(R.drawable.comment_zan_yes);
									notifyDataSetChanged();
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
					params.put("login_user_id", Config.getUser(context).uid);
					params.put("review_id", commentList.get(position).getReview_id());
					params.put("admire_user_id", commentList.get(position).getUser_id());
					ab.post(ServerMutualConfig.CancelListingReviewAdmire, params,
							new AbStringHttpResponseListener() {
								@Override
								public void onSuccess(int statusCode,
										String content) {
									super.onSuccess(statusCode, content);
									Config.dismissProgress();
									commentList.get(position).setAdmire_count((Integer.parseInt(commentList.get(position).getAdmire_count())-1) + "");
									commentList.get(position).setIs_admire("0");
									item_comment_zan_icon.setBackgroundResource(R.drawable.comment_zan_no);
									notifyDataSetChanged();
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
		
		item_comment_ly.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, PostReviewList.class);
				intent.putExtra("img_id", commentList.get(position).getImg_id());
				intent.putExtra("owner_id", commentList.get(position).getReview_id());
//				intent.putExtra("ismy",
//						commentList.get(position).getReview_id().equals(Config.getUser(context).uid));
				context.startActivity(intent);
			}
		});
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainItemDetialActivity.sendIndex = 1;
				MainItemDetialActivity.indexId = commentList.get(position).getReview_id();
				VideoDetialActivity.sendIndex = 1;
				VideoDetialActivity.indexId = commentList.get(position).getReview_id();
				
				userInput.requestFocus();
				InputMethodManager imm = (InputMethodManager) userInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED); 
			}
		});
		
		return convertView;
	}

	
}

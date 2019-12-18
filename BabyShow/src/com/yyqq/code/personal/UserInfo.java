package com.yyqq.code.personal;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.main.MyFollowGroupListActivity;
import com.yyqq.code.main.Release;
import com.yyqq.code.photo.PeoplePhotoManager;
import com.yyqq.commen.model.MessageItem;
import com.yyqq.commen.model.People;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.utils.Rank;
import com.yyqq.commen.utils.RoundAngleImageView;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfo extends Activity {
	public String tag = "UserInfo";
	public Activity context;
	public String uid;
	private ArrayList<MessageItem> messageData = new ArrayList<MessageItem>();
	private AbHttpUtil ab;
	private ImageView focus;
	private LinearLayout myalbum, myShow/* 我发布的 */, myIdol/* 好友 */, myShare,
			my_collection, my_note;
	private RelativeLayout my_msg, my_friend_msg;
	private ImageView album;
	private RoundAngleImageView head;
	private TextView name;
	private LinearLayout name_root;
	People people;
	private ImageView rank;
	private LinearLayout ly_rank;
	private LinearLayout my_add_group;
	
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
		setContentView(R.layout.user_info);

		context = this;
		uid = getIntent().getStringExtra("uid");
		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
		init();
	}

	public void init() {
		my_add_group = (LinearLayout) findViewById(R.id.my_add_group);
		ly_rank = (LinearLayout) findViewById(R.id.ly_rank);
		ly_rank.setOnClickListener(rankClick);
		head = (RoundAngleImageView) findViewById(R.id.head);
		name = (TextView) findViewById(R.id.name);
		rank = (ImageView) findViewById(R.id.rank);
		name_root = (LinearLayout) findViewById(R.id.name_root);
		focus = (ImageView) findViewById(R.id.focus);
		myShow = (LinearLayout) findViewById(R.id.mysend);
		myShow.setOnClickListener(myShowClick);

		// 我关注的
		myIdol = (LinearLayout) findViewById(R.id.my_follow);
		myIdol.setOnClickListener(myIdolClick);
		/**
		 * shared
		 */
		myShare = (LinearLayout) findViewById(R.id.my_shares);
		my_collection = (LinearLayout) findViewById(R.id.my_collection);
		// 相册
		myalbum = (LinearLayout) findViewById(R.id.myalbum);
		album = (ImageView) findViewById(R.id.album);
		// 消息
		my_msg = (RelativeLayout) findViewById(R.id.my_msg);
		// 好友动态
		my_friend_msg = (RelativeLayout) findViewById(R.id.my_friend_msg);
		
		my_add_group.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserInfo.this, MyFollowGroupListActivity.class);
				intent.putExtra(MyFollowGroupListActivity.USER_ID, uid);
				startActivity(intent);
			}
		});
		
		getUserInfo();
	}

	public OnClickListener rankClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!TextUtils.isEmpty(people.level_img)) {
				Intent intent = new Intent();
				intent.setClass(context, Rank.class);
				startActivity(intent);
			}
		}
	};

	public OnClickListener imagesClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context, PeoplePhotoManager.class);
			intent.putExtra("uid", people.uid);
			intent.putExtra("friend", true);
			startActivity(intent);
		}
	};
	public OnClickListener myFansClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(context, FriendList.class);
			intent.putExtra("uid", people.uid);
			intent.putExtra("isFocus", "0");
			startActivity(intent);
		}
	};
	public OnClickListener myShowClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(context, Release.class);
			intent.putExtra("uid", uid);
			intent.putExtra("user", "user");
			startActivity(intent);
		}
	};
	public OnClickListener myIdolClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(context, FriendList.class);
			intent.putExtra("uid", people.uid);
			intent.putExtra("isFocus", "1");
			startActivity(intent);
		}
	};


	public void setFocusButton() {
		switch (people.relation) {
		case 0:
		case 3:
			focus.setBackgroundResource(R.drawable.user_info_add_friend);
			focus.setOnClickListener(addFriend);
			break;
		case 1:
			focus.setBackgroundResource(R.drawable.user_info_remove_firend);
			focus.setOnClickListener(removeFriend);
			break;
		case 2:
			focus.setBackgroundResource(R.drawable.user_info_remove_firend);
			focus.setOnClickListener(removeFriend);
			break;
		}
		if (people.relation == 3 || people.relation == 2) {
			myalbum.setOnClickListener(imagesClick);
		} else {
			myalbum.setOnClickListener(null);
			album.setBackgroundResource(R.drawable.index_album);
		}
	}

	public OnClickListener addFriend = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Config.showProgressDialog(context, false, null);
			AbRequestParams params = new AbRequestParams();
			params.put("user_id", Config.getUser(context).uid);
			params.put("idol_id", people.uid);
			ab.post(ServerMutualConfig.FocusOn, params,
					new AbStringHttpResponseListener() {
						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							try {
								JSONObject json = new JSONObject(content);
								if (json.getBoolean("success")) {
									people.relation = 2;
									setFocusButton();
								}
								Toast.makeText(context,
										json.getString("reMsg"),
										Toast.LENGTH_SHORT).show();
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
	};
	public OnClickListener removeFriend = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Config.showProgressDialog(context, false, null);
			AbRequestParams params = new AbRequestParams();
			params.put("user_id", Config.getUser(context).uid);
			params.put("idol_id", people.uid);
			ab.post(ServerMutualConfig.CancelFocus, params,
					new AbStringHttpResponseListener() {
						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							try {
								JSONObject json = new JSONObject(content);
								if (json.getBoolean("success")) {
									people.relation = 0;
									setFocusButton();
									// finish();
								}
								Toast.makeText(context,
										json.getString("reMsg"),
										Toast.LENGTH_SHORT).show();
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
	};

	public void getUserInfo() {
		Config.showProgressDialog(context, false, null);
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
		params.put("idol_id", uid);
		params.put("is_idoled", "1");
		// android.util.Log.e("fanfan", ServerMutualConfig.UserInfo + "&" +
		// params.toString());
		ab.get(ServerMutualConfig.UserInfo + "&" + params.toString(),
				new AbStringHttpResponseListener() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							if (json.getBoolean("success")) {
								people = new People();
								people.fromJson(json);
								setFocusButton();
								if (name_root.getChildCount() > 2) {
									name_root.removeViews(1,
											name_root.getChildCount() - 2);
								}
								for (int i = 0; i < people.signs.size(); i++) {
									TextView baby = new TextView(context);
									baby.setText(people.signs.get(i));
									baby.setTextColor(Color
											.parseColor("#818181"));
									baby.setTextSize(13);
									name_root.addView(baby,
											name_root.getChildCount() - 1);
								}
								MyApplication.getInstance().display(
										people.level_img, rank, statusCode);
								name.setText(people.nickname);
								MyApplication.getInstance().display(
										people.head_thumb, head,
										R.drawable.def_head);
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
}
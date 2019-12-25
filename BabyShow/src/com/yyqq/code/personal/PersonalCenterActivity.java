package com.yyqq.code.personal;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.business.BusinessCooperateActivity;
import com.yyqq.code.business.BusinessOderList;
import com.yyqq.code.business.RedPacket;
import com.yyqq.code.business.UserOderList;
import com.yyqq.code.login.AddBaby;
import com.yyqq.code.login.ChangePhone;
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.code.main.MainTab;
import com.yyqq.code.main.MyFollowGroupListActivity;
import com.yyqq.code.main.PhotoManager;
import com.yyqq.code.main.Release;
import com.yyqq.code.toyslease.AccountWithdrawalsActivity;
import com.yyqq.code.toyslease.ToysLeaseMainTabActivity;
import com.yyqq.commen.model.People;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.Log;
import com.yyqq.commen.utils.Rank;
import com.yyqq.commen.utils.RoundAngleImageView;
import com.yyqq.commen.view.MyCollection;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

/**
 * 个人中心
 * */
public class PersonalCenterActivity extends Activity {
	public String TAG = "MyIndex";
	private Activity context;
	private LinearLayout ly_set, ly_cooperate;
	private MyApplication myMyApplication;
	private AbHttpUtil ab;
	private LinearLayout myalbum, myShow/* 我发布的 */, myIdol/* 好友 */, myShare,
			my_collection, my_note, my_packet, my_phone;
	private RelativeLayout my_msg, my_friend_msg, my_grow;
	private TextView new_msg, new_f_msg, new_grow_msg;
	private RoundAngleImageView head;
	private TextView name;
	private TextView user_id;
	private ImageView rank;
	private LinearLayout name_root;
	private ImageView add_baby;
	private LinearLayout ly_rank;
	private TextView phoneT, tv_phone;
	private LinearLayout my_add_group;
	People people;
	
	private int isBoss = 0;
	private LinearLayout myindex_lease_order;
	private LinearLayout myindex_lease_monery;
	private View my_monery_line;
	
	private boolean isIntent = false;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (context.getIntent().getBooleanExtra("falg_return", false)) {
			context.finish();
			return false;
		} else {
			return Config.onKeyDown(keyCode, event, this);
		}
	}

	public void onCreate(Bundle b) {
		super.onCreate(b);
		Config.setActivityState(this);
		setContentView(R.layout.myindex);

		context = this;
		myMyApplication = (MyApplication) context.getApplication();
		my_monery_line = (View)findViewById(R.id.my_monery_line);
		ly_set = (LinearLayout) findViewById(R.id.ly_set);
		ly_cooperate = (LinearLayout) findViewById(R.id.ly_cooperate);
		ly_set.setOnClickListener(setClick);
		ly_cooperate.setOnClickListener(cooperateClick);
		ab = AbHttpUtil.getInstance(context);
		ab.setDebug(Log.isDebug);
		init();
	}

	public void init() {
		
		// 账户余额提现
		myindex_lease_monery = (LinearLayout) findViewById(R.id.my_monery);
		myindex_lease_monery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(null == people){
					return;
				}else{
					Intent intent = new Intent(PersonalCenterActivity.this, AccountWithdrawalsActivity.class);
					startActivity(intent);
				}
			}
		});
		
		// 我的玩具订单
		myindex_lease_order = (LinearLayout) findViewById(R.id.my_lease_order);
		myindex_lease_order.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(null == people){
					return;
				}else{
					Intent intent = new Intent(PersonalCenterActivity.this, ToysLeaseMainTabActivity.class);
					intent.putExtra("tabid", 3);
					startActivity(intent);
				}
			}
		});
		
		new_msg = (TextView) findViewById(R.id.new_msg);
		new_f_msg = (TextView) findViewById(R.id.new_f_msg);
		new_grow_msg = (TextView) findViewById(R.id.new_grow_msg);
		ly_rank = (LinearLayout) findViewById(R.id.ly_rank);
		ly_rank.setOnClickListener(rankClick);
		add_baby = (ImageView) findViewById(R.id.add_baby);
		head = (RoundAngleImageView) findViewById(R.id.head);
		name = (TextView) findViewById(R.id.name);
		user_id = (TextView) findViewById(R.id.user_id);
		rank = (ImageView) findViewById(R.id.rank);
		name_root = (LinearLayout) findViewById(R.id.name_root);
		// 我发布的
		myShow = (LinearLayout) findViewById(R.id.mysend);
		myShow.setOnClickListener(myShowClick);

		phoneT = (TextView) findViewById(R.id.phoneT);
		tv_phone = (TextView) findViewById(R.id.tv_phone);

		// 我关注的
		myIdol = (LinearLayout) findViewById(R.id.my_follow);
		myIdol.setOnClickListener(myIdolClick);
		add_baby.setOnClickListener(addBabyClick);
		// 共享人
		myShare = (LinearLayout) findViewById(R.id.my_shares);
		myShare.setOnClickListener(myShareClick);
		// 收藏
		my_collection = (LinearLayout) findViewById(R.id.my_collection);
		my_collection.setOnClickListener(myCollectionClick);
		// 相册
		myalbum = (LinearLayout) findViewById(R.id.myalbum);
		myalbum.setOnClickListener(myalbumClick);
		// 订单
		my_note = (LinearLayout) findViewById(R.id.my_note);
		my_note.setOnClickListener(mynoteClick);
		// 红包
		my_packet = (LinearLayout) findViewById(R.id.my_packet);
		my_packet.setOnClickListener(myPacketClick);
		// 手机号
		my_phone = (LinearLayout) findViewById(R.id.my_phone);
		my_phone.setOnClickListener(myPhoneClick);
		// 消息
		my_msg = (RelativeLayout) findViewById(R.id.my_msg);
		my_msg.setOnClickListener(myMsgClick);
		// 好友动态
		my_friend_msg = (RelativeLayout) findViewById(R.id.my_friend_msg);
		my_friend_msg.setOnClickListener(myFriendMsgClick);
		// 成长记录关注
		my_grow = (RelativeLayout) findViewById(R.id.my_grow);
		my_grow.setOnClickListener(myGrowClick);
		
		// 我关注的群
		my_add_group = (LinearLayout) findViewById(R.id.my_add_group);
		my_add_group.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(null == people){
					return;
				}
				PersonalCenterActivity.this.startActivity(new Intent(PersonalCenterActivity.this, MyFollowGroupListActivity.class));
			}
		});
		
		getUserInfo();
	}

	// 我要合作
	public OnClickListener cooperateClick = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(context, BusinessCooperateActivity.class);
			startActivity(intent);
		}
	};

	// 设置
	public OnClickListener setClick = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if ("1".equals(myMyApplication.getVisitor())) {
				Intent intent = new Intent();
				intent.setClass(context, WebLoginActivity.class);
				startActivity(intent);
			} else {
				startActivity(new Intent(context, UserSet.class));
			}
		}
	};

	public OnClickListener addBabyClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if ("1".equals(myMyApplication.getVisitor())) {
				myMyApplication.setVisitor("");
				Intent intent = new Intent();
				intent.setClass(context, WebLoginActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent();
				intent.setClass(context, AddBaby.class);
				intent.putExtra("from_index", "from_index");
				startActivity(intent);
			}
		}
	};
	public OnClickListener myGrowClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(context, GrowList.class);
			setSysMessage();
			if (View.VISIBLE == new_grow_msg.getVisibility()) {
				new_grow_msg.setVisibility(View.GONE);
			}
			startActivity(intent);
		}
	};

	public OnClickListener myMsgClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(context, MessageList.class);
			setSysMessage();
			if (View.VISIBLE == new_msg.getVisibility()) {
				new_msg.setVisibility(View.GONE);
			}
			startActivity(intent);
		}
	};
	public OnClickListener myFriendMsgClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(context, MessageFriendList.class);
			setSysMessage();
			if (View.VISIBLE == new_f_msg.getVisibility()) {
				new_f_msg.setVisibility(View.GONE);
			}
			startActivity(intent);
		}
	};
	public OnClickListener myFansClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(context, FriendList.class);
			intent.putExtra("uid", Config.getUser(context).uid);
			intent.putExtra("isFocus", "0");
			startActivity(intent);
		}
	};
	public OnClickListener myShowClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(context, Release.class);
			intent.putExtra("uid", Config.getUser(context).uid);
			startActivity(intent);
		}
	};

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

	public OnClickListener myIdolClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(context, FriendList.class);
			intent.putExtra("uid", Config.getUser(context).uid);
			intent.putExtra("isFocus", "1");
			startActivity(intent);
		}
	};

	/**
	 * share 的点击事件
	 */

	public OnClickListener myShareClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(context, SharedList.class);
			intent.putExtra("uid", Config.getUser(context).uid);
			intent.putExtra("isShareMan", "0");
			startActivity(intent);
		}
	};

	public OnClickListener myalbumClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(context, PhotoManager.class);
			startActivity(intent);
		}
	};

	public OnClickListener myPacketClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(context, RedPacket.class);
			startActivity(intent);
		}
	};
	public OnClickListener mynoteClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			if(null == people){
				return;
			}
			
			Intent intent = new Intent();
			if (1 == isBoss) {
				intent.setClass(context, BusinessOderList.class);
			} else {
				intent.setClass(context, UserOderList.class);
			}
			startActivity(intent);
		}
	};
	public OnClickListener myPhoneClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if ("1".equals(myMyApplication.getVisitor())) {
				myMyApplication.setVisitor("");
				Intent intent = new Intent();
				intent.setClass(context, WebLoginActivity.class);
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
				if (TextUtils.isEmpty(people.mobile)) {
					intent.setClass(context, PersonalBandPhoneActivity.class);
				} else {
					intent.setClass(context, ChangePhone.class);
				}
				startActivity(intent);
			}
		}
	};

	public OnClickListener mySharedMeClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(context, SharedList.class);
			intent.putExtra("uid", Config.getUser(context).uid);
			intent.putExtra("isShareMan", "1");
			startActivity(intent);
		}
	};

	public OnClickListener myCollectionClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(context, MyCollection.class);
			startActivity(intent);
		}
	};

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		
		// 修改底部栏文字颜色
		MainTab.iconText.get(0).setTextColor(Color.parseColor("#666666"));
		MainTab.iconText.get(1).setTextColor(Color.parseColor("#666666"));
		MainTab.iconText.get(2).setTextColor(Color.parseColor("#666666"));
		MainTab.iconText.get(3).setTextColor(Color.parseColor("#fe6363"));
		
		if(!MyApplication.getVisitor().equals("0") && !isIntent || Config.getUser(context).uid.equals("")){ // 没有登录并且没有进入过登录页
			isIntent = true; // 已经进入过登录页 
			Intent in = new Intent(PersonalCenterActivity.this, WebLoginActivity.class);
			startActivity(in);
		}else if(!MyApplication.getVisitor().equals("0")  && isIntent){ // 进入过登录页并且没有登录
			Intent in = new Intent(PersonalCenterActivity.this, MainTab.class);
			in.putExtra("tabid", 0);
			startActivity(in);
		}
		
		if(MyApplication.getVisitor().equals("0") ){
			getUserInfo();
		}
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void getUserInfo() {
		AbRequestParams params = new AbRequestParams();
		params.put("user_id", Config.getUser(context).uid);
		params.put("idol_id", Config.getUser(context).uid);
		params.put("is_idoled", "0");
		AbHttpUtil ab = AbHttpUtil.getInstance(context);
		// android.util.Log.e("fanfan",
		// ServerMutualConfig.UserInfo + "&" + params.toString());
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
								if (name_root.getChildCount() > 2) {
									name_root.removeViews(1,
											name_root.getChildCount() - 2);
								}
								
								if(!people.is_account.isEmpty()){
									if(people.is_account.equals("0")){
										myindex_lease_monery.setVisibility(View.GONE);
										my_monery_line.setVisibility(View.GONE);
									}else{
										myindex_lease_monery.setVisibility(View.VISIBLE);
										my_monery_line.setVisibility(View.VISIBLE);
									}
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
								
								// 用户等级
								MyApplication.getInstance().display(
										people.level_img, rank, statusCode);
								// 用户昵称
								name.setText(people.nickname);
								user_id.setText("ID:" + people.uid);
								// 用户头像
								MyApplication.getInstance().display(
										people.head_thumb, head,
										R.drawable.def_head);
								// 手机号显示
								if (TextUtils.isEmpty(people.mobile)) {
									phoneT.setText("绑定手机号");
								} else {
									phoneT.setText("更换手机号");
									tv_phone.setText(people.mobile);
								}
								// 消息数量
								if (0 != Integer
										.parseInt(people.my_message_count)) {
									new_msg.setVisibility(View.VISIBLE);
									new_msg.setText("+"
											+ people.my_message_count);
								}
								if (0 != Integer
										.parseInt(people.friends_message_count)) {
									new_f_msg.setVisibility(View.VISIBLE);
									new_f_msg.setText("+"
											+ people.friends_message_count);
								}
								if (0 != Integer
										.parseInt(people.babys_idol_count)) {
									new_grow_msg.setVisibility(View.VISIBLE);
									new_grow_msg.setText("+"
											+ people.babys_idol_count);
								}

								// 用户种类
								isBoss = Integer.parseInt(people.user_role);
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

	private void setSysMessage() {
		SharedPreferences sp = context.getSharedPreferences(
				"babyshow_system_count", Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt("notice", 0);
		edit.commit();
	}

}

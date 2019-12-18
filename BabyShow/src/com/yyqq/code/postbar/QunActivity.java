package com.yyqq.code.postbar;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.business.BusinessDetailActivity;
import com.yyqq.code.login.WebLoginActivity;
import com.yyqq.code.main.AddNewPostActivity;
import com.yyqq.code.main.MainItemDetialActivity;
import com.yyqq.code.main.MainTab;
import com.yyqq.code.video.VideoDetialActivity;
import com.yyqq.commen.model.PostBarTypeItem;
import com.yyqq.commen.model.QunZhuItem;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.view.PullDownView;
import com.yyqq.commen.view.PullDownView.OnPullDownListener;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;


/**
 * 弃用
 * 
 * 2016.0923
 * */
public class QunActivity extends Activity implements OnPullDownListener {

	private Activity context;
	private String TAG = "QunActivity";
	private AbHttpUtil ab;
	private PullDownView mPullDownView;
	private ListView listview;
	private MyAdapter adapter;
	private TextView send_post;
	ArrayList<PostBarTypeItem> dataPostInterestList = new ArrayList<PostBarTypeItem>();
	private MyApplication app;
	private int width;
	private MyApplication myMyApplication;
	private TextView title;
	private String group_id, group_name;
	private ImageView back;
	private int index = 0;// 记录选中的图片位置
	private static int indexitem = 0;// 记录选中的图片位置
	private boolean indexitemfla;// 记录选中的图片位置

	/** 头部view **/
	private RelativeLayout mRelativeLayout;
	private LayoutInflater inflater;
	private TextView qun_zhu, visit_num, post_num;
	private ImageView qun_head, attention;
	private ArrayList<QunZhuItem> dataQunZhu = new ArrayList<QunZhuItem>();

	public void onResume() {
		super.onResume();
		onRefresh();
		if (indexitemfla) {
			indexitemfla = false;
			adapter.notifyDataSetChanged();
			listview.setSelection(indexitem);
		}
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
		myMyApplication = (MyApplication) context.getApplication();
		setContentView(R.layout.postbar_list);
		init();
	}

	private void init() {
		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		width = DM.widthPixels;
		ab = AbHttpUtil.getInstance(context);
		initView();

		app = (MyApplication) this.getApplication();
		mPullDownView = (PullDownView) findViewById(R.id.list);
		mPullDownView.setOnPullDownListener(this);
		listview = mPullDownView.getListView();
		mPullDownView.setShowHeaderLayout(mRelativeLayout);

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
		// listview.addHeaderView(mGallery);
		// 显示并且可以使用头部刷新
		mPullDownView.setShowHeader();
		// mPullDownView.setPadding(0, 0, 0, 0);
		onRefresh();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			indexitemfla = true;
			listview.setSelection(indexitem);
		}
	}

	private void initView() {
		group_id = getIntent().getStringExtra("group_id");
		inflater = LayoutInflater.from(context);
		/** 头部的view **/
		initHeadbar();
		send_post.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if ("1".equals(myMyApplication.getVisitor())) {
					myMyApplication.setVisitor("");
					Intent intent = new Intent();
					intent.setClass(context, WebLoginActivity.class);
					startActivity(intent);
//					AlertDialog.Builder dialog = new Builder(context);
//					dialog.setTitle("宝宝秀秀");
//					dialog.setMessage("游客不能使用这里哦，请登录");
//					dialog.setNegativeButton("取消",
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//								}
//
//							});
//					dialog.setPositiveButton("登录",
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface arg0,
//										int arg1) {
//									myMyApplication.visitor = "";
//									Intent intent = new Intent();
//									intent.setClass(context, WebLoginActivity.class);
//									startActivity(intent);
//								}
//							});
//					dialog.create().show();
				} else {
					Intent intent = new Intent();
					intent.setClass(context, AddNewPostActivity.class);
					intent.putExtra("group_id", group_id);
					startActivity(intent);
//					context.finish();
				}
			}
		});
	}

	// 返回
	public OnClickListener backClick = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			context.finish();
		}
	};

	/**
	 * 头部的view
	 */
	@SuppressLint("ResourceAsColor")
	private void initHeadbar() {
		mRelativeLayout = (RelativeLayout) inflater.inflate(
				R.layout.head_qunzhu, null);
		title = (TextView) mRelativeLayout.findViewById(R.id.title);
		back = (ImageView) mRelativeLayout.findViewById(R.id.back);
		back.setOnClickListener(backClick);
		send_post = (TextView) mRelativeLayout.findViewById(R.id.send_post);
		qun_head = (ImageView) mRelativeLayout.findViewById(R.id.qun_head);
		qun_zhu = (TextView) mRelativeLayout.findViewById(R.id.qun_zhu);
		visit_num = (TextView) mRelativeLayout.findViewById(R.id.visit_num);
		post_num = (TextView) mRelativeLayout.findViewById(R.id.post_num);
		attention = (ImageView) mRelativeLayout.findViewById(R.id.attention);
		getAlbumList();
		attention.setOnClickListener(attentionClick);

	}

	public OnClickListener attentionClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			AbRequestParams params = new AbRequestParams();
			params.put("login_user_id", Config.getUser(context).uid);
			params.put("group_id", group_id);
			if (!dataQunZhu.get(0).user_id.equals(Config.getUser(context).uid)) {
				if ("0".equals(dataQunZhu.get(0).is_share)) { // 1表示关注过了
					ab.post(ServerMutualConfig.PostIdol, params,
							new AbStringHttpResponseListener() {
								@Override
								public void onSuccess(int statusCode,
										String content) {
									super.onSuccess(statusCode, content);
									try {
										JSONObject json = new JSONObject(
												content);
										if (json.getBoolean("success")) {
											Toast.makeText(context,
													json.getString("reMsg"),
													Toast.LENGTH_SHORT).show();
											getAlbumList();
											// attention
											// .setBackgroundResource(R.drawable.cancle_attention);
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
								public void onFailure(int statusCode,
										String content, Throwable error) {
									super.onFailure(statusCode, content, error);
								}
							});
				} else {
					ab.post(ServerMutualConfig.CancelPostIdol, params,
							new AbStringHttpResponseListener() {
								@Override
								public void onSuccess(int statusCode,
										String content) {
									super.onSuccess(statusCode, content);
									try {
										JSONObject json = new JSONObject(
												content);
										if (json.getBoolean("success")) {
											Toast.makeText(context,
													json.getString("reMsg"),
													Toast.LENGTH_SHORT).show();
											getAlbumList();
											// attention
											// .setBackgroundResource(R.drawable.attention_qun);
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
								public void onFailure(int statusCode,
										String content, Throwable error) {
									super.onFailure(statusCode, content, error);
								}
							});
				}
			} else {
				Intent intent = new Intent(context, QunManageActivity.class);
				intent.putExtra("group_id", group_id);
				intent.putExtra("group_name", group_name);
				startActivity(intent);
			}
		}
	};

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
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("group_id", group_id);
//		Log.e("fanfan",
//				ServerMutualConfig.GroupDetailListing + "&" + params.toString());
		ab.setTimeout(5000);
		ab.get(ServerMutualConfig.GroupDetailListing + "&" + params.toString(),
				new AbStringHttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							dataPostInterestList.clear();
							JSONObject json = new JSONObject(content);
							for (int i = 0; i < json.getJSONArray("data").length(); i++) {
								PostBarTypeItem item = new PostBarTypeItem();
								item.fromJson(json.getJSONArray("data").getJSONObject(i));
								dataPostInterestList.add(item);
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
		if (dataPostInterestList.size() == 0) {
			mPullDownView.notifyDidMore();
			mPullDownView.RefreshComplete();
			return;
		}
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put(
				"post_create_time",
				dataPostInterestList.get(dataPostInterestList.size() - 1).post_create_time
						+ "");
		params.put("group_id", group_id);
		// Log.e("fanfan", ServerMutualConfig.GroupDetailListing + "&" +
		// params.toString());
		ab.get(ServerMutualConfig.GroupDetailListing + "&" + params.toString(),
				new AbStringHttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							JSONObject json = new JSONObject(content);
							for (int i = 0; i < json.getJSONArray("data")
									.length(); i++) {
								PostBarTypeItem item = new PostBarTypeItem();
								item.fromJson(json.getJSONArray("data")
										.getJSONObject(i));
								dataPostInterestList.add(item);
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
						mPullDownView.RefreshComplete();// 这个事线程安全的 可看源代码
						mPullDownView.notifyDidMore();
					}

				});
	}

	class ViewHolder {
		RelativeLayout linearLayout1;
		RelativeLayout tu;
		ImageView img, img2, jingxuan,video_hint;
		View v1;
		TextView post_title;
		TextView visit_num;
		TextView see_num;
		TextView msg, msg2;
		// 公告/精华
		LinearLayout linearLayout2;
		TextView tv_type, tv_title;
		ImageView icon_tu;
		View v2;
	}

	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dataPostInterestList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return dataPostInterestList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int index, View convertView, ViewGroup arg2) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.postbar_list_item, null);
			ViewHolder holder = null;
			holder = new ViewHolder();
			holder.linearLayout1 = (RelativeLayout) convertView
					.findViewById(R.id.linearLayout1);
			holder.v1 = (View) convertView.findViewById(R.id.v1);
			holder.tu = (RelativeLayout) convertView.findViewById(R.id.tu);
			holder.img = (ImageView) convertView.findViewById(R.id.post_img);
			holder.img2 = (ImageView) convertView.findViewById(R.id.post_img2);
			holder.video_hint = (ImageView) convertView
					.findViewById(R.id.video_hint);
			holder.jingxuan = (ImageView) convertView
					.findViewById(R.id.jingxuan);
			holder.post_title = (TextView) convertView
					.findViewById(R.id.post_title);
			holder.visit_num = (TextView) convertView
					.findViewById(R.id.visit_num);
			holder.see_num = (TextView) convertView.findViewById(R.id.see_num);
			holder.msg = (TextView) convertView.findViewById(R.id.msg);
			holder.msg2 = (TextView) convertView.findViewById(R.id.msg2);
			// 公告或者精华
			holder.linearLayout2 = (LinearLayout) convertView
					.findViewById(R.id.linearLayout2);
			holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			holder.icon_tu = (ImageView) convertView.findViewById(R.id.icon_tu);
			holder.v2 = (View) convertView.findViewById(R.id.v2);

			final PostBarTypeItem item = dataPostInterestList.get(index);
			if ("1".equals(item.distinguish)) { // 公告
				holder.linearLayout1.setVisibility(View.GONE);
				holder.v1.setVisibility(View.GONE);
				holder.linearLayout2.setVisibility(View.VISIBLE);
				holder.v2.setVisibility(View.VISIBLE);
				holder.icon_tu
						.setBackgroundResource(R.drawable.post_qun_gonggao);
				holder.tv_type.setText("公告：");
				holder.tv_type.setTextColor(Color.parseColor("#779efd"));
				holder.tv_title.setText(item.img_title);
			} else if ("2".equals(item.distinguish)) {
				holder.linearLayout1.setVisibility(View.GONE);
				holder.v1.setVisibility(View.GONE);
				holder.linearLayout2.setVisibility(View.VISIBLE);
				holder.v2.setVisibility(View.VISIBLE);
				holder.icon_tu
						.setBackgroundResource(R.drawable.post_qun_jinghua);
				holder.tv_type.setText("精华：");
				holder.tv_type.setTextColor(Color.parseColor("#fe7676"));
				holder.tv_title.setText(item.img_title);
			}

			// 图片
			if (!TextUtils.isEmpty(item.img)) {
				app.display(item.img, holder.img, R.drawable.deft_color);
			} else {
				holder.tu.setVisibility(View.GONE);
			}
			// 精选
			if ("1".equals(item.is_recommend)) {
				holder.jingxuan.setVisibility(View.VISIBLE);
			}
			// 标题
			if (!TextUtils.isEmpty(item.img_title))
				holder.post_title.setText(item.img_title);
			// 参与人数
			if (!TextUtils.isEmpty(item.review_count))
				holder.visit_num.setText("参与" + item.review_count + "人");
			// 观看/帖子人数
			if (!TextUtils.isEmpty(item.post_count)) {
				holder.see_num.setText("观看" + item.post_count + "人");
			}
			// 描述
			if (!TextUtils.isEmpty(item.description)) {
				holder.msg.setText(item.description);
			}
			
			if("5".equals(item.is_group)){
				holder.video_hint.setVisibility(View.VISIBLE);
			}
			
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!"0".equals(item.distinguish)) {
						Intent intent = new Intent(context,
								PostGongGaoActivity.class);
						intent.putExtra("distinguish", item.distinguish);
						intent.putExtra("group_id", group_id);
						indexitem = listview.getFirstVisiblePosition();
						context.startActivityForResult(intent, 1);
					} else {
						if ("0".equals(item.is_group)) {
							Intent intent = new Intent(context,
									MainItemDetialActivity.class);
							intent.putExtra("user_id", item.user_id);
							intent.putExtra("img_id", item.img_id);
							indexitem = listview.getFirstVisiblePosition();
							context.startActivityForResult(intent, 1);
						} else if ("4".equals(item.is_group)) {
							Intent intent = new Intent(context,MainTab.class);
							intent.putExtra("tabid", 1);
							indexitem = listview.getFirstVisiblePosition();
							context.startActivityForResult(intent, 1);
						} else if ("5".equals(item.is_group)) {
							Intent intent = new Intent(context,
									VideoDetialActivity.class);
							Bundle bundel = new Bundle();
							bundel.putSerializable(VideoDetialActivity.VIIDEO_INFO,
									(Serializable) item);
							intent.putExtras(bundel);
							indexitem = listview.getFirstVisiblePosition();
							context.startActivityForResult(intent, 1);
						} else {
							Intent intent = new Intent(context, BusinessDetailActivity.class);
							intent.putExtra("business_id", item.img_id);
							indexitem = listview.getFirstVisiblePosition();
							context.startActivityForResult(intent, 1);
						}
					}
				}
			});

			return convertView;
		}

	}

	public void getAlbumList() {
		AbRequestParams params = new AbRequestParams();
		params.put("login_user_id", Config.getUser(context).uid);
		params.put("group_id", group_id);
//		android.util.Log.e("fanfan", ServerMutualConfig.GetGroupHead + "&"
//				+ params.toString());
		ab.get(ServerMutualConfig.GetGroupHead + "&" + params.toString(),
				new AbStringHttpResponseListener() {

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						JSONObject json = null;
						dataQunZhu.clear();
						try {
							json = new JSONObject(content);
							if (json.getBoolean("success")) {
								for (int i = 0; i < json.getJSONArray("data")
										.length(); i++) {
									QunZhuItem item = new QunZhuItem();
									item.fromJson(json.getJSONArray("data")
											.getJSONObject(i));
									dataQunZhu.add(item);
								}
								MyApplication.getInstance().display(
										dataQunZhu.get(0).avatar, qun_head,
										R.drawable.def_head);
								title.setText(dataQunZhu.get(0).group_name);
								group_name = dataQunZhu.get(0).group_name;
								qun_zhu.setText(dataQunZhu.get(0).nick_name);
								visit_num.setText("参与"
										+ dataQunZhu.get(0).review_count + "人");
								post_num.setText("帖子"
										+ dataQunZhu.get(0).post_count + "个");
								if (!dataQunZhu.get(0).user_id.equals(Config
										.getUser(context).uid)) {
									if ("1".equals(dataQunZhu.get(0).is_share)) { // 1表示关注过了
										attention
												.setBackgroundResource(R.drawable.cancle_attention);
									} else {
										attention
												.setBackgroundResource(R.drawable.attention_qun);
									}
								} else {
									attention
											.setBackgroundResource(R.drawable.qun_manage);
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				});
	}

}

package com.yyqq.code.buy;

import java.io.IOException;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.umeng.analytics.MobclickAgent;
import com.yyqq.babyshow.R;
import com.yyqq.code.photo.BitmapCache;
import com.yyqq.commen.utils.BimpUtil;
import com.yyqq.commen.utils.Config;
import com.yyqq.commen.utils.FileUtils;
import com.yyqq.commen.utils.MyAnimations;
import com.yyqq.framework.application.MyApplication;
import com.yyqq.framework.application.ServerMutualConfig;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class BuyShow extends Activity {
	public final String TAG = "BuyShow";
	private GridAdapter adapter;
	private Button submit;
	private EditText msg, link;
	private Activity context;
	private String msgString = "", linkString = "";
	private int babyShow = 0; // 标记是否同时发布到秀秀 跟帖的图片所属组，0代表秀秀，3代表帖子和秀秀

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			context.finish();
		}
		return false;
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Config.setActivityState(this);
		setContentView(R.layout.buy_show);

		context = this;
		MyAnimations.initOffset(this);
		isZhineng = getIntent().getBooleanExtra("iszhineng", false);
		Init();
	}

	@Override
	protected void onResume() {
		context = this;
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void Init() {
		link = (EditText) findViewById(R.id.link);
		msg = (EditText) findViewById(R.id.msg);
		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(submitClick);
	}

	AjaxParams params = new AjaxParams();
	public OnClickListener submitClick = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Config.showProgressDialog(context, false, null);
			msgString = msg.getText().toString().trim();
			linkString = link.getText().toString().trim();
			params.put("user_id", Config.getUser(context).uid);
			params.put("good_url", linkString);
			params.put("reason", msgString);
			handler.sendEmptyMessage(1);
		}
	};
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (TextUtils.isEmpty(msgString) || TextUtils.isEmpty(linkString)) {
				Config.dismissProgress();
				Toast.makeText(context, "链接和描述不能为空", 0).show();
			} else {
				FinalHttp fh = new FinalHttp();
				// Log.e("fanfan", ServerMutualConfig.BuyImageNew + "&" +
				// params.toString());
				fh.post(ServerMutualConfig.BuyImageNew, params,
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
								Intent intent = new Intent();
								intent.setClass(context, BuyActivity.class);
								startActivity(intent);
								context.finish();
								Toast.makeText(context, "后台会尽快审核", 0).show();
							}

						});
				super.handleMessage(msg);
			}
		}
	};

	public boolean isZhineng = false;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		msgString = "";
		linkString = "";
	}

	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			return (BimpUtil.drr.size());
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
		public View getView(int position, View convertView, ViewGroup parent) {
			final int coord = position;
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (BimpUtil.drr.get(position).startsWith("http")) {
				MyApplication.getInstance().display(BimpUtil.drr.get(position),
						holder.image, R.drawable.def_image);
			} else {
				String path = BimpUtil.drr.get(position);
				try {
					Bitmap bm = BitmapCache.revitionImageSize(null, path);
					holder.image.setImageBitmap(bm);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
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

	protected void onRestart() {
		super.onRestart();
	}

}

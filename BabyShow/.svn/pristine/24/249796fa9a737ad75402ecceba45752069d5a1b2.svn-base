package com.yyqq.commen.adapter;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yyqq.babyshow.R;
import com.yyqq.code.photo.BitmapCache;
import com.yyqq.code.postbar.PostBarDetail.PictureAdapter;
import com.yyqq.commen.utils.BimpUtil;
import com.yyqq.commen.utils.FileUtils;
import com.yyqq.framework.application.MyApplication;

public class PostDetailPictureAdapter extends BaseAdapter {
	private LayoutInflater inflater; // 视图容器
	private int selectedPosition = -1;// 选中的位置
	private boolean shape;
	private PictureAdapter pictureAdapter;
	private Activity context;
	private PostDetailAdapter adapter;

	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		this.shape = shape;
	}

	public PostDetailPictureAdapter(Activity context) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	public PostDetailPictureAdapter(LayoutInflater inflater,
			int selectedPosition, boolean shape, PictureAdapter pictureAdapter,
			Activity context, PostDetailAdapter adapter, Handler handler) {
		super();
		this.selectedPosition = selectedPosition;
		this.shape = shape;
		this.pictureAdapter = pictureAdapter;
		this.context = context;
		this.adapter = adapter;
		this.handler = handler;
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
					context.getResources(), R.drawable.postbar_add_picture));
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
package com.yyqq.commen.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.yyqq.babyshow.R;
import com.yyqq.commen.model.HeadListItem;
import com.yyqq.framework.application.MyApplication;

@SuppressWarnings("deprecation")
public class MainImageListApapter extends BaseAdapter {

	private Activity context;
	private int windowWidth;
	private ArrayList<HeadListItem> headData;
	
	public MainImageListApapter(Activity context, ArrayList<HeadListItem> headData, int windowWidth) {
		super();
		this.context = context;
		this.headData = headData;
		this.windowWidth = windowWidth;
	}

	@Override
	public int getCount() {
		return headData.size() == 0 ? 3 : headData.size();// 实现循环显示
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(context);
		if (headData.size() > 0) {
			MyApplication.getInstance().display(headData.get(position).imgList.get(0).img_thumb, imageView, R.drawable.def_image);
		} else {
			imageView.setImageResource(R.drawable.def_image);
		}
		int newH = (int) (windowWidth * 0.4);
		imageView.setScaleType(ScaleType.CENTER_CROP);
		imageView.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.FILL_PARENT, newH));
		RelativeLayout borderImg = new RelativeLayout(context);
		borderImg.addView(imageView);
		return borderImg;
	}
}
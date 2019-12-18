package com.yyqq.commen.view;

import com.yyqq.babyshow.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

/**
 * @类名:LoadMorePullToreshView
 * @描述:TODO(加载更多和下拉刷新的View)
 * @作者:zhaohao
 * @时间 2013-4-23 上午9:39:42
 */
public class LoadMoreGridViewPullToreshView extends LinearLayout {
	private PullToRefreshView pullToRefreshView = null;
	private GridView gridView = null;

	public GridView getGridView() {
		return gridView;
	}

	public void setGridView(GridView gridView) {
		this.gridView = gridView;
	}

	public PullToRefreshView getPullToRefreshView() {
		return pullToRefreshView;
	}

	public void setPullToRefreshView(PullToRefreshView pullToRefreshView) {
		this.pullToRefreshView = pullToRefreshView;
	}

	Context mContext = null;

	public LoadMoreGridViewPullToreshView(Context context) {
		super(context);
		mContext = context;
	}

	public LoadMoreGridViewPullToreshView(Context context,
			AttributeSet attributeSet) {
		super(context, attributeSet);
		mContext = context;
		initWidget(context);
	}

	/**
	 * 初始化布局
	 * 
	 * @param mcontext
	 */
	public void initWidget(Context mcontext) {
		LayoutInflater inflater = (LayoutInflater) mcontext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.grid_loadmore_pulltorefresh,
				null);
		// 添加布局参数
		this.addView(layout, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		pullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		gridView = (GridView) findViewById(R.id.gvIndexData);
		// 设置周围没有
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}
}

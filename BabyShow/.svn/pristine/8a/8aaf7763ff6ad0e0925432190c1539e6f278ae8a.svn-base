package com.yyqq.commen.view;

import com.yyqq.babyshow.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * @类名:LoadMoreListView
 * @描述:TODO(加载更多listview)
 * @作者:zhaohao
 * @时间 2013-5-12 下午1:18:27
 */
public class LoadMoreListView extends ListView implements OnClickListener {
	public LoadMoreListView(Context context) {
		super(context);
	}

	public LoadMoreListView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		initWidget();
	}

	public void initWidget() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// 加载更多
		View loadMoreView = inflater.inflate(R.layout.load_more_layout, null);
		this.addFooterView(loadMoreView, null, true);
		btnLoadMore = (Button) findViewById(R.id.btnLoadMore);
		btnLoadMore.setOnClickListener(this);
		// this.setFooterDividersEnabled(false);
	}

	private Button btnLoadMore;
	private IClickloadMoreBtnCallBack clickloadMoreBtnCallBack;

	@Override
	public void onClick(View v) {
		if (null != clickloadMoreBtnCallBack) {
			clickloadMoreBtnCallBack.loadMore();
		} else {
			Toast.makeText(getContext(), "请实现点击加载更多回调函数", 3).show();
		}
	}

	public interface IClickloadMoreBtnCallBack {
		public void loadMore();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}

	public void setClickloadMoreBtnCallBack(
			IClickloadMoreBtnCallBack clickloadMoreBtnCallBack) {
		this.clickloadMoreBtnCallBack = clickloadMoreBtnCallBack;
	}

	public void leftMargin(int leftMargin, int rightMargin) {
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.leftMargin = leftMargin;
		layoutParams.rightMargin = rightMargin;
		if (null != btnLoadMore) {
			btnLoadMore.setLayoutParams(layoutParams);
		}
	}

	public Button getBtnLoadMore() {
		return btnLoadMore;
	}

	public void showLoadMore() {
		if (null != btnLoadMore) {
			this.setFooterDividersEnabled(true);
			// btnLoadMore.setEnabled(true);
			btnLoadMore.setEnabled(false);
			btnLoadMore.setText("上拉加载更多");
			// 加载完成，更换加载更多按钮
			btnLoadMore.setVisibility(View.VISIBLE);
			((View) (btnLoadMore.getParent())).setVisibility(View.VISIBLE);
		}
	}

	public void hideLoadMore() {
		if (null != btnLoadMore) {
			btnLoadMore.setVisibility(View.GONE);
			// ((View) btnLoadMore.getParent()).setVisibility(View.GONE);
			// 禁用最后listView项的最后一条线
			this.setFooterDividersEnabled(false);
		}
	}

	public void enableLoadMore() {
		if (null != btnLoadMore) {
			// btnLoadMore.setEnabled(true);
			btnLoadMore.setEnabled(false);
		}
	}

	public void fillLoadMore(int currentPage) {
		if (null != btnLoadMore) {
			btnLoadMore.setEnabled(false);
			if (currentPage == 1)
				btnLoadMore.setVisibility(View.GONE);
			else
				btnLoadMore.setText(getResources().getString(
						R.string.hasloading_moreValues));
		}
	}
}

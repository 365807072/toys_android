package com.yyqq.commen.view;


public interface ScrollViewListener {

	void onScrollChanged(PullDownView pullDownView, int x, int y, int oldx, int oldy);

	void onScrollChanged(PullToRefreshView pullToRefreshView, int l, int t, int oldl, int oldt);  
}

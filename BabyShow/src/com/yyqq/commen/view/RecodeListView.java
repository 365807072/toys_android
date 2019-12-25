package com.yyqq.commen.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class RecodeListView extends ListView {

	public RecodeListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

		MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);

	}

}

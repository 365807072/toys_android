package com.yyqq.commen.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yyqq.babyshow.R;

/**
 * 简介: 可拖动展示的控件
 * <p/>
 * Created by ChenGuang on 2014/4/28 0028
 */
public class DragView extends RelativeLayout {
	private static final int CONTENT_VIEW_ID = 0x1000001;
	private static final int HANDLER_VIEW_ID = 0x1000002;
	private static final int MAGNETPADDING = 30;
	private RelativeLayout mContentView = null;
	private ImageView mHandlerView = null;
	private float mDownY = 0;
	private float mLastDragY = -1000;
	private boolean isUpDirection = false;
	private int mBackgroundColor = Color.parseColor("#50000000");
	private int mHandleImgId;
	private boolean isNeedMagnetAction = false;
	private LayoutInflater inflater;
	
	
	/**
	 * 手柄位置
	 */
	private ViewPositions mPosition = ViewPositions.BOTTOM;

	public enum ViewPositions {
		BOTTOM, TOP
	}

	/**
	 * 获取当前位置
	 * 
	 * @return 返回位置
	 */
	public ViewPositions getCurrentPosition() {
		return mPosition;
	}

	/**
	 * 设定位置
	 * 
	 * @param pos
	 *            指定位置
	 */
	public void setCurrentPosition(ViewPositions pos) {
		if (pos.equals(ViewPositions.BOTTOM)) {
			isUpDirection = false;
			animate(isUpDirection);
		} else if (pos.equals(ViewPositions.TOP)) {
			isUpDirection = true;
			animate(isUpDirection);
		}
	}

	/**
	 * 设置容器的背景色
	 * 
	 * @param color
	 *            颜色
	 */
	public void setContentBackground(int color) {
		mBackgroundColor = color;
	}

	/**
	 * 设置容器的View
	 * 
	 * @param viewLayout
	 */
	public void setContentView(View viewLayout) {
		if (mContentView != null) {
			mContentView.addView(viewLayout);
		}
	}

	public DragView(Context context, LayoutInflater inflater) {
		super(context);
		this.inflater = inflater;
		initView(context);
	}

	public DragView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.RoundAngleImageView);
		mHandleImgId = ta.getResourceId(R.styleable.CircleProgressBar_fill,
				R.drawable.ic_launcher);
		ta.recycle();
		initView(context);
	}

	public DragView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
		mHandlerView = new ImageView(context);
		mHandlerView.setId(HANDLER_VIEW_ID);
		mHandlerView.setScaleType(ImageView.ScaleType.FIT_XY);
		mHandlerView.setImageResource(mHandleImgId);
		LayoutParams params = new LayoutParams(-2, -2);
		params.addRule(CENTER_HORIZONTAL);
		params.addRule(ALIGN_PARENT_BOTTOM);
		mHandlerView.setLayoutParams(params);
		mHandlerView.setOnTouchListener(mHandlerTouchListener);
		mHandlerView.setClickable(true);
		addView(mHandlerView);

		mContentView = (RelativeLayout) inflater.inflate(R.layout.main_goodleft_hint_page, null);
		mContentView.setId(CONTENT_VIEW_ID);
		params = new LayoutParams(-1, -1);
		params.addRule(ABOVE, HANDLER_VIEW_ID);
		mContentView.setLayoutParams(params);
		mContentView.setBackgroundColor(mBackgroundColor);
		mContentView.setClickable(true);
		
		
		addView(mContentView);
	}

	private OnTouchListener mHandlerTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int action = event.getAction();
			if (action == MotionEvent.ACTION_DOWN) {
				isNeedMagnetAction = false;
				mDownY = event.getRawY();
			} else if (action == MotionEvent.ACTION_MOVE) {
				if (mLastDragY != -1000) {
					isUpDirection = mLastDragY > event.getRawY();
				}
				mLastDragY = event.getRawY();
				int offsetY = (int) (mDownY - event.getRawY());
				// 手柄位置
				int top = v.getTop() - offsetY;
				setViewPosition(top);
				// 更新当前Y位置
				mDownY = (int) event.getRawY();
			} else if (action == MotionEvent.ACTION_UP) {
				isNeedMagnetAction = true;
				animate(isUpDirection);
			}
			return false;
		}
	};

	private void setViewPosition(int handlerTop) {
		setHandlerViewPosition(handlerTop);
		setContentViewPosition(handlerTop - mContentView.getHeight());
	}

	/**
	 * 设置手柄位置
	 * 
	 * @param top
	 */
	private void setHandlerViewPosition(int top) {
		int MagnetPadding = isNeedMagnetAction ? MAGNETPADDING : 0;
		int bottom = top + mHandlerView.getHeight();
		if (top < MagnetPadding) {
			top = 0;
			bottom = mHandlerView.getHeight();
		}
		if (bottom > getHeight() - MagnetPadding) {
			bottom = getHeight();
			top = bottom - mHandlerView.getHeight();
		}
		mHandlerView.layout(mHandlerView.getLeft(), top,
				mHandlerView.getRight(), bottom);
	}

	/**
	 * 设置容器位置
	 * 
	 * @param top
	 */
	private void setContentViewPosition(int top) {
		int MagnetPadding = isNeedMagnetAction ? MAGNETPADDING : 0;
		int bottom = top + mContentView.getHeight();
		if (bottom > mContentView.getHeight()) {
			mContentView.layout(mContentView.getLeft(), 0,
					mContentView.getRight(), mContentView.getHeight());
			return;
		}
		if (bottom >= MagnetPadding && top <= -1 * MagnetPadding) {
			mContentView.layout(mContentView.getLeft(), top,
					mContentView.getRight(), bottom);
		} else {
			if (bottom < MagnetPadding) {
				mContentView.layout(mContentView.getLeft(),
						-mContentView.getHeight(), mContentView.getRight(), 0);
			} else {
				mContentView.layout(mContentView.getLeft(), 0,
						mContentView.getRight(), mContentView.getHeight());
			}
		}
		mContentView.layout(mContentView.getLeft(), top,
				mContentView.getRight(), bottom);
	}

	/**
	 * 完成移动的补间动画
	 * 
	 * @param isUp
	 *            是否向上移动
	 */
	private void animate(boolean isUp) {
		SyncAnimation animation = new SyncAnimation();
		if (isUp) {
			mPosition = ViewPositions.TOP;
			animation.execute(mHandlerView.getTop(), 1);
		} else {
			mPosition = ViewPositions.BOTTOM;
			animation.execute(mHandlerView.getTop(), 0);
		}
	}

	private class SyncAnimation extends AsyncTask<Integer, Integer, String> {
		boolean isUp = false;

		@Override
		protected String doInBackground(Integer... params) {
			int distance = params[0];
			isUp = params[1] == 1;
			int step;
			if (isUp) {
				step = distance / 5;
			} else {
				step = mContentView.getHeight() / 5;
			}
			for (int i = 0; i < 6; i++) {
				if (isUp) {
					distance = distance - step;
				} else {
					distance = distance + step;
				}
				publishProgress(distance);
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {

				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			setHandlerViewPosition(values[0]);
			setContentViewPosition(values[0] - mContentView.getHeight());
			super.onProgressUpdate(values);
		}
	}

}
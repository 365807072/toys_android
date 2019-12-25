package com.yyqq.commen.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyqq.babyshow.R;
import com.yyqq.framework.callback.ViewBaseAction;

public class ExpandTabView extends LinearLayout implements OnDismissListener {

	private ArrayList<RelativeLayout> mViewArray = new ArrayList<RelativeLayout>();
	private Context mContext;
	private final int SMALL = 0;
	private int displayWidth;
	private int displayHeight;
	public static PopupWindow popupWindow;
	private int selectPosition;
	private static ViewMiddle mviewMiddle;
	LocationManager locationManager;

	public static EditText editText;
	public ExpandTabView(Context context) {
		super(context);
		init(context);
	}

	public ExpandTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * 设置tabitem的个数和初始值
	 * 
	 * @param viewMiddle
	 */
	public void setValue(ViewMiddle viewMiddle) {
		mviewMiddle = viewMiddle;
		if (mContext == null) {
			return;
		}
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		final RelativeLayout r = new RelativeLayout(mContext);
		int maxHeight = (int) (displayHeight * 0.7);
		RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT, maxHeight);
		rl.leftMargin = 10;
		rl.rightMargin = 10;
		r.addView(viewMiddle, rl);
		mViewArray.add(r);
		r.setTag(SMALL);
		TextView tButton = (TextView) inflater.inflate(R.layout.shaixuan, this,
				false);
		addView(tButton);
		View line = new TextView(mContext);
		line.setBackgroundResource(R.drawable.choosebar_line);

		r.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onPressBack();
			}
		});

		r.setBackgroundColor(mContext.getResources().getColor(
				R.color.popup_main_background));
		tButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(null != ViewMiddle.search_root){
					ViewMiddle.search_root.setVisibility(View.VISIBLE);
				}
				startAnimation();
			}
		});
	}

	private void startAnimation() {
		if (popupWindow == null) {
			popupWindow = new PopupWindow(mViewArray.get(selectPosition),
					displayWidth, displayHeight);
			popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
			popupWindow.setOutsideTouchable(true);
			//设置可以获取焦点，否则弹出菜单中的EditText是无法获取输入的
			popupWindow.setFocusable(true);
			//防止虚拟软键盘被弹出菜单遮住
			popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		}

		if (!popupWindow.isShowing()) {
			showPopup(selectPosition);
		} else {
			popupWindow.setOnDismissListener(this);
			popupWindow.dismiss();
			hideView();
		}
	}

	private void showPopup(int position) {
		View tView = mViewArray.get(selectPosition).getChildAt(0);
		if (tView instanceof ViewBaseAction) {
			ViewBaseAction f = (ViewBaseAction) tView;
			f.show();
		}
		if (popupWindow.getContentView() != mViewArray.get(position)) {
			popupWindow.setContentView(mViewArray.get(position));
		}
		popupWindow.showAsDropDown(this, 0, 0);
	}

	/**
	 * 如果菜单成展开状态，则让菜单收回去
	 */
	public static boolean onPressBack() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			hideView();
			return true;
		} else {
			return false;
		}

	}

	public static void hideView() {
		View tView = mviewMiddle;
		if (tView instanceof ViewBaseAction) {
			ViewBaseAction f = (ViewBaseAction) tView;
			f.hide();
		}
	}

	private void init(Context context) {
		mContext = context;
		displayWidth = ((Activity) mContext).getWindowManager()
				.getDefaultDisplay().getWidth();
		displayHeight = ((Activity) mContext).getWindowManager()
				.getDefaultDisplay().getHeight();
		setOrientation(LinearLayout.HORIZONTAL);
	}

	@Override
	public void onDismiss() {
		popupWindow.setOnDismissListener(null);
	}

	private OnButtonClickListener mOnButtonClickListener;

	/**
	 * 设置tabitem的点击监听事件
	 */
	public void setOnButtonClickListener(OnButtonClickListener l) {
		mOnButtonClickListener = l;
	}

	/**
	 * 自定义tabitem点击回调接口
	 */
	public interface OnButtonClickListener {
		public void onClick(int selectPosition);
	}
	
	public static void hindPupWindow(Context context){
		if (!popupWindow.isShowing()) {
//			popupWindow.setOnDismissListener(context);
			popupWindow.dismiss();
		}
	}

}

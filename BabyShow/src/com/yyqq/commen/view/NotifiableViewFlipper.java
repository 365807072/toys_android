package com.yyqq.commen.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

public class NotifiableViewFlipper extends ViewFlipper {
	private OnFlipListener onFlipListener;
    public boolean isShow = true;
	public static interface OnFlipListener {
		public void onShowPrevious(NotifiableViewFlipper flipper);

		public void onShowNext(NotifiableViewFlipper flipper);
	}

	public void setOnFlipListener(OnFlipListener onFlipListener) {
		this.onFlipListener = onFlipListener;
	}

	public NotifiableViewFlipper(Context context) {
		super(context);
	}

	public NotifiableViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void showPrevious() {
		if (isShow)
		super.showPrevious();
		if (hasFlipListener()) {
			onFlipListener.onShowPrevious(this);
		}
	}

	@Override
	public void showNext() {
		if (isShow)
		super.showNext();
		if (hasFlipListener()) {
			onFlipListener.onShowNext(this);
		}
	}

	private boolean hasFlipListener() {
		return onFlipListener != null;
	}
}

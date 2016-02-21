package com.instirepo.app.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class ViewPagerHorizontalScrolling extends ViewPager {

	private GestureDetector mGestureDetector;
	private boolean mIsLockOnHorizontalAxis = false;

	public ViewPagerHorizontalScrolling(Context context) {
		super(context);
		mGestureDetector = new GestureDetector(context,
				new CustomScrollDetector());
	}

	public ViewPagerHorizontalScrolling(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(context,
				new CustomScrollDetector());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!mIsLockOnHorizontalAxis)
			mIsLockOnHorizontalAxis = mGestureDetector.onTouchEvent(event);

		if (event.getAction() == MotionEvent.ACTION_UP)
			mIsLockOnHorizontalAxis = false;

		getParent().requestDisallowInterceptTouchEvent(mIsLockOnHorizontalAxis);
		return super.onTouchEvent(event);
	}

	private class CustomScrollDetector extends SimpleOnGestureListener {

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return Math.abs(distanceX) > Math.abs(distanceY);
		}
	}
}

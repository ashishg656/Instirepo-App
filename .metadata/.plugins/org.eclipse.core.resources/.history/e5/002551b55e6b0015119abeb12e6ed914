package com.bookncart.app.widgets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class RecyclerViewHorizontalScrolling extends RecyclerView {

	private GestureDetector mGestureDetector;
	private boolean mIsLockOnHorizontalAxis = false;

	public RecyclerViewHorizontalScrolling(Context context) {
		super(context);
		mGestureDetector = new GestureDetector(context,
				new CustomScrollDetector());
	}

	public RecyclerViewHorizontalScrolling(Context context, AttributeSet attrs) {
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

	public RecyclerViewHorizontalScrolling(Context context, AttributeSet arg1,
			int arg2) {
		super(context, arg1, arg2);
		mGestureDetector = new GestureDetector(context,
				new CustomScrollDetector());
	}

	private class CustomScrollDetector extends SimpleOnGestureListener {

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return Math.abs(distanceX) > Math.abs(distanceY);
		}
	}
}

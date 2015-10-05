package com.bookncart.app.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

@SuppressLint("NewApi")
public class ObservableScrollView extends ScrollView {

	ObservableScrollViewListener listener;

	private Runnable scrollerTask;
	private int initialPosition;
	private int newCheckDelayTime = 100;

	public ObservableScrollView(Context context) {
		super(context);
		setScrollerData();
	}

	public ObservableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setScrollerData();
	}

	public ObservableScrollView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setScrollerData();
	}

	public ObservableScrollView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		setScrollerData();
	}

	public void setScrollListnerer(ObservableScrollViewListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		listener.onScroll(l, t, oldl, oldt);
		super.onScrollChanged(l, t, oldl, oldt);
	}

	public void startScrollerTask() {
		initialPosition = getScrollY();
		ObservableScrollView.this.postDelayed(scrollerTask, newCheckDelayTime);
	}

	private void setScrollerData() {
		scrollerTask = new Runnable() {

			public void run() {
				int newPosition = getScrollY();
				if (initialPosition - newPosition == 0) {
					if (listener != null) {
						listener.onScrollStopped();
					}
				} else {
					initialPosition = getScrollY();
					ObservableScrollView.this.postDelayed(scrollerTask,
							newCheckDelayTime);
				}
			}
		};
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			startScrollerTask();
		}
		return super.onTouchEvent(ev);
	}
}
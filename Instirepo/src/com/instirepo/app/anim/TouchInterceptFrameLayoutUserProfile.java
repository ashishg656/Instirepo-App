package com.instirepo.app.anim;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.instirepo.app.R;
import com.instirepo.app.widgets.ObservableScrollView;

public class TouchInterceptFrameLayoutUserProfile extends FrameLayout {

	public ObservableScrollView scrollView;
	float initialY, initialTranslation;

	public TouchInterceptFrameLayoutUserProfile(Context context,
			AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public TouchInterceptFrameLayoutUserProfile(Context context,
			AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TouchInterceptFrameLayoutUserProfile(Context context) {
		this(context, null);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (scrollView.getScrollY() == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			initialY = event.getRawY();
			initialTranslation = getTranslationY();
			// scrollView.requestDisallowInterceptTouchEvent(false);
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			float dy = initialY - event.getRawY();
			if (dy > 0 && getTranslationY() == 0) {
				getParent().requestDisallowInterceptTouchEvent(true);
				scrollView.scrollTo(0, (int) dy);
				;// onTouchEvent(event);
				return true;
			} else {
				float trans = initialTranslation - dy;
				setTranslationY(trans);
				// scrollView.requestDisallowInterceptTouchEvent(false);
				return true;
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {

		}
		return super.onTouchEvent(event);
	}
}

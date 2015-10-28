package com.instirepo.app.extras;

import com.instirepo.app.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ScrollView;

@SuppressLint("NewApi")
public class UserProfileFrameLayout extends FrameLayout {

	Context context;

	public UserProfileFrameLayout(Context context) {
		this(context, null);
	}

	public UserProfileFrameLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public UserProfileFrameLayout(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.w("as", "onintercept touch event");

		ScrollView scrollView = (ScrollView) findViewById(R.id.scrollview);
		if (scrollView != null && scrollView.getTranslationY() == 0) {
			if (ev.getHistorySize() > 0) {
				float dy = ev.getY() - ev.getHistoricalY(0);
				if (scrollView.getScrollY() == 0 && dy > 0) {
					return true;
				}
			}
		} else if (scrollView != null && scrollView.getTranslationY() != 0) {
			return true;
		}
		return false;
	}
}

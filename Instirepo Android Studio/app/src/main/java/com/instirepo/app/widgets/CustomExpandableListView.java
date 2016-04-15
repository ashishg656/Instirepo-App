package com.instirepo.app.widgets;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class CustomExpandableListView extends ExpandableListView {

	int intGroupPosition, intChildPosition, intGroupid;

	public CustomExpandableListView(Context context) {
		super(context);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// widthMeasureSpec = MeasureSpec
		// .makeMeasureSpec(960, MeasureSpec.AT_MOST);
		// heightMeasureSpec = MeasureSpec.makeMeasureSpec(600,
		// MeasureSpec.AT_MOST);
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
		ViewGroup.LayoutParams params = getLayoutParams();
		params.height = getMeasuredHeight();
	}
}
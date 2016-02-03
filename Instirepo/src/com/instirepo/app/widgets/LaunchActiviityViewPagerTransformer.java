package com.instirepo.app.widgets;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.instirepo.app.R;

public class LaunchActiviityViewPagerTransformer implements
		ViewPager.PageTransformer {

	int thetaChange = 45;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void transformPage(View view, float position) {
		int tag = Integer.parseInt(String.valueOf(view.getTag()));
		if (position < -1) { // [-Infinity,-1)
			// This page is way off-screen to the left.
		} else if (position < 1) { // [-1,1]
			
		} else { // (1,+Infinity]
			// This page is way off-screen to the right.
		}

		if (view.findViewById(R.id.framelayoutfragment3launch) != null) {
			FrameLayout frame = (FrameLayout) view
					.findViewById(R.id.framelayoutfragment3launch);
			frame.setRotation(360 * position + thetaChange);
		}

		antiRotateThirdFragmentImage(R.id.firstimage, view, position);
		antiRotateThirdFragmentImage(R.id.secondimage, view, position);
		antiRotateThirdFragmentImage(R.id.thirdimage, view, position);
		antiRotateThirdFragmentImage(R.id.fourthimage, view, position);
	}

	private void antiRotateThirdFragmentImage(int imageID, View view,
			float position) {
		if (view.findViewById(imageID) != null) {
			LinearLayout image = (LinearLayout) view.findViewById(imageID);
			image.setRotation(-360 * position - thetaChange);
		}
	}
}

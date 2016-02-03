package com.instirepo.app.widgets;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.instirepo.app.R;

public class LaunchActiviityViewPagerTransformer implements
		ViewPager.PageTransformer {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void transformPage(View view, float position) {
		// if (view.findViewById(R.id.firstimage) != null) {
		// LinearLayout image = (LinearLayout) view
		// .findViewById(R.id.firstimage);
		// if (position < -1) {
		// image.setTranslationX(0);
		// image.setTranslationY(0);
		// } else if (position <= 1) {
		// Log.w("as", "pos " + position);
		// image.setTranslationX(position * 100);
		// image.setTranslationY(position * 100);
		// } else {
		// image.setTranslationX(0);
		// image.setTranslationY(0);
		// }
		// }

		if (view.findViewById(R.id.framelayoutfragment3launch) != null) {
			FrameLayout frame = (FrameLayout) view
					.findViewById(R.id.framelayoutfragment3launch);
			frame.setRotation(360 * position);
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
			image.setRotation(-360 * position);
		}
	}
}

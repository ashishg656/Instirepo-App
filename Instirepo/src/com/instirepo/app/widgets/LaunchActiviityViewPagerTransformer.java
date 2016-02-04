package com.instirepo.app.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.instirepo.app.R;

public class LaunchActiviityViewPagerTransformer implements
		ViewPager.PageTransformer {

	int thetaChange = 45;
	int deviceWidth;
	Context context;

	public LaunchActiviityViewPagerTransformer(Context context) {
		deviceWidth = context.getResources().getDisplayMetrics().widthPixels;
		this.context = context;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void transformPage(View view, float position) {
		if (view.findViewById(R.id.framelayoutfragment3launch) != null) {
			FrameLayout frame = (FrameLayout) view
					.findViewById(R.id.framelayoutfragment3launch);
			frame.setRotation(180 * position + thetaChange);
		}

		antiRotateThirdFragmentImage(R.id.firstimage, view, position);
		antiRotateThirdFragmentImage(R.id.secondimage, view, position);
		antiRotateThirdFragmentImage(R.id.thirdimage, view, position);
		antiRotateThirdFragmentImage(R.id.fourthimage, view, position);

		// right side fragment 1
		animationForFirstFragmentImages(R.id.shoppingimage, view, position,
				2 * deviceWidth);
		animationForFirstFragmentImages(R.id.readimage, view, position,
				0.5f * deviceWidth);
		animationForFirstFragmentImages(R.id.eventimage, view, position,
				0.5f * deviceWidth);

		// left side fragment 1
		animationForFirstFragmentImages(R.id.appicon, view, position, -0.5f
				* deviceWidth);
		animationForFirstFragmentImages(R.id.newsimage, view, position, -0.5f
				* deviceWidth);
		animationForFirstFragmentImages(R.id.shareimage, view, position, -0.5f
				* deviceWidth);
		animationForFirstFragmentImages(R.id.bulbimage, view, position, -1f
				* deviceWidth);
	}

	private void animationForFirstFragmentImages(int id, View view,
			float position, float i) {
		if (view.findViewById(id) != null) {
			View layout = view.findViewById(id);
			layout.setTranslationX(position * i);
		}
	}

	private void antiRotateThirdFragmentImage(int imageID, View view,
			float position) {
		if (view.findViewById(imageID) != null) {
			View image = view.findViewById(imageID);
			image.setRotation(-180 * position - thetaChange);
		}
	}
}

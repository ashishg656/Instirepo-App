package com.instirepo.app.fragments;

import com.instirepo.app.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class LaunchScreen3Fragment extends BaseFragment {

	FrameLayout iconsContainer;

	public static LaunchScreen3Fragment newInstance(Bundle b) {
		LaunchScreen3Fragment frg = new LaunchScreen3Fragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.launch_screen_fragment_3_layout,
				container, false);

		rootView = v;

		iconsContainer = (FrameLayout) v
				.findViewById(R.id.framelayoutfragment3launch);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		int deviceWidth = getResources().getDisplayMetrics().widthPixels;
		LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) iconsContainer
				.getLayoutParams();
		p.height = deviceWidth;
		iconsContainer.setLayoutParams(p);
	}

}

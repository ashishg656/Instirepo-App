package com.instirepo.app.fragments;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.instirepo.app.R;

public class LaunchScreen1Fragment extends BaseFragment {

	public static LaunchScreen1Fragment newInstance(Bundle b) {
		LaunchScreen1Fragment frg = new LaunchScreen1Fragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.launch_screen_fragment_1_layout,
				container, false);

		rootView = v;

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		GradientDrawable gd = (GradientDrawable) rootView.findViewById(
				R.id.bulbimage).getBackground();
		gd.setColor(getActivity().getResources().getColor(
				R.color.z_red_color_primary));

		gd = (GradientDrawable) rootView.findViewById(R.id.eventimage)
				.getBackground();
		gd.setColor(getActivity().getResources().getColor(R.color.orange_post));

		gd = (GradientDrawable) rootView.findViewById(R.id.newsimage)
				.getBackground();
		gd.setColor(getActivity().getResources().getColor(R.color.orange_post));

		gd = (GradientDrawable) rootView.findViewById(R.id.shoppingimage)
				.getBackground();
		gd.setColor(getActivity().getResources().getColor(R.color.purple_post));

		gd = (GradientDrawable) rootView.findViewById(R.id.shareimage)
				.getBackground();
		gd.setColor(getActivity().getResources().getColor(
				R.color.PrimaryDarkColor));

		gd = (GradientDrawable) rootView.findViewById(R.id.readimage)
				.getBackground();
		gd.setColor(getActivity().getResources().getColor(
				R.color.home_viewpager_color_1));
	}

}

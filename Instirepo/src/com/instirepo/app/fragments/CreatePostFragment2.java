package com.instirepo.app.fragments;

import java.util.HashMap;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.instirepo.app.R;
import com.instirepo.app.activities.CreatePostActivity;
import com.instirepo.app.widgets.CustomFlowLayout;
import com.instirepo.app.widgets.CustomFlowLayout.LayoutParams;

public class CreatePostFragment2 extends BaseFragment {

	TabLayout tabLayout;
	MyPagerAdapter pagerAdapter;
	ViewPager viewPager;
	HashMap<Integer, Fragment> fragmentHashMap;

	public static CreatePostFragment2 newInstance(Bundle b) {
		CreatePostFragment2 frg = new CreatePostFragment2();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public void onResume() {
		((CreatePostActivity) getActivity()).isFirstFragmentVisible = false;
		super.onResume();
	}

	@Override
	public void onPause() {
		((CreatePostActivity) getActivity()).isFirstFragmentVisible = true;
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.create_post_fragment_2_layout,
				container, false);

		tabLayout = (TabLayout) v.findViewById(R.id.indicator);
		viewPager = (ViewPager) v.findViewById(R.id.pager_launch);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fragmentHashMap = new HashMap<>();

		pagerAdapter = new MyPagerAdapter(getChildFragmentManager());
		viewPager.setAdapter(pagerAdapter);
		tabLayout.setupWithViewPager(viewPager);
	}

	class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {
			Bundle bundle = new Bundle();

			Fragment fragment = null;
			if (pos == 1)
				fragment = CreatePostFragment2FragmentCollection
						.newInstance(bundle);
			else
				fragment = CreatePostFragment2FragmentSelectNew
						.newInstance(bundle);

			fragmentHashMap.put(pos, fragment);

			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position == 0)
				return "Select New";
			return "Saved Visibilities";
		}
	}

	public void updateToTextBoxInFragment2() {
		((CreatePostFragment2FragmentSelectNew) fragmentHashMap.get(0)).customFlowLayout
				.removeAllViews();

		// branches
		for (int i = 0; i < ((CreatePostActivity) getActivity()).branchesArrayString
				.size(); i++) {
			TextView tv = (TextView) LayoutInflater.from(getActivity())
					.inflate(R.layout.textview_post_selected_flow, null, false);
			tv.setText(((CreatePostActivity) getActivity()).branchesArrayString
					.get(i));
			CustomFlowLayout.LayoutParams params = new CustomFlowLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			int pixels2 = getActivity().getResources().getDimensionPixelSize(
					R.dimen.z_margin_supermini);
			params.setMargins(pixels2, pixels2, pixels2, pixels2);
			tv.setLayoutParams(params);

			GradientDrawable categoryBg = (GradientDrawable) tv.getBackground();
			categoryBg.setColor(getActivity().getResources().getColor(
					R.color.z_red_color_primary));
			((CreatePostFragment2FragmentSelectNew) fragmentHashMap.get(0)).customFlowLayout
					.addView(tv);
		}

		// years
		for (int i = 0; i < ((CreatePostActivity) getActivity()).yearArrayString
				.size(); i++) {
			TextView tv = (TextView) LayoutInflater.from(getActivity())
					.inflate(R.layout.textview_post_selected_flow, null, false);
			tv.setText(((CreatePostActivity) getActivity()).yearArrayString
					.get(i));
			CustomFlowLayout.LayoutParams params = new CustomFlowLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			int pixels2 = getActivity().getResources().getDimensionPixelSize(
					R.dimen.z_margin_supermini);
			params.setMargins(pixels2, pixels2, pixels2, pixels2);
			tv.setLayoutParams(params);

			GradientDrawable categoryBg = (GradientDrawable) tv.getBackground();
			categoryBg.setColor(getActivity().getResources().getColor(
					R.color.purple_post));
			((CreatePostFragment2FragmentSelectNew) fragmentHashMap.get(0)).customFlowLayout
					.addView(tv);
		}

		// batches
		for (String batch : ((CreatePostActivity) getActivity()).batchArrayString) {
			TextView tv = (TextView) LayoutInflater.from(getActivity())
					.inflate(R.layout.textview_post_selected_flow, null, false);
			tv.setText(batch);
			CustomFlowLayout.LayoutParams params = new CustomFlowLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			int pixels2 = getActivity().getResources().getDimensionPixelSize(
					R.dimen.z_margin_supermini);
			params.setMargins(pixels2, pixels2, pixels2, pixels2);
			tv.setLayoutParams(params);

			GradientDrawable categoryBg = (GradientDrawable) tv.getBackground();
			categoryBg.setColor(getActivity().getResources().getColor(
					R.color.orange_post));
			((CreatePostFragment2FragmentSelectNew) fragmentHashMap.get(0)).customFlowLayout
					.addView(tv);
		}

		// teachers
		for (String teacher : ((CreatePostActivity) getActivity()).teacherArrayString) {
			TextView tv = (TextView) LayoutInflater.from(getActivity())
					.inflate(R.layout.textview_post_selected_flow, null, false);
			tv.setText(teacher);
			CustomFlowLayout.LayoutParams params = new CustomFlowLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			int pixels2 = getActivity().getResources().getDimensionPixelSize(
					R.dimen.z_margin_supermini);
			params.setMargins(pixels2, pixels2, pixels2, pixels2);
			tv.setLayoutParams(params);

			GradientDrawable categoryBg = (GradientDrawable) tv.getBackground();
			categoryBg.setColor(getActivity().getResources().getColor(
					R.color.PrimaryDarkColor));
			((CreatePostFragment2FragmentSelectNew) fragmentHashMap.get(0)).customFlowLayout
					.addView(tv);
		}

		int count = ((CreatePostActivity) getActivity())
				.getCountForSelectedVisibilities();
		if (count > 1) {
			((CreatePostFragment2FragmentSelectNew) fragmentHashMap.get(0)).saveVisibility
					.setVisibility(View.VISIBLE);
		} else {
			((CreatePostFragment2FragmentSelectNew) fragmentHashMap.get(0)).saveVisibility
					.setVisibility(View.GONE);
		}
	}

	public void callHideSaveButtonFunction() {
		((CreatePostFragment2FragmentSelectNew) fragmentHashMap.get(0)).saveVisibility
				.setVisibility(View.GONE);
	}
}

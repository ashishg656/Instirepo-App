package com.instirepo.app.fragments;

import com.instirepo.app.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LaunchScreen4Fragment extends BaseFragment {

	public static LaunchScreen4Fragment newInstance(Bundle b) {
		LaunchScreen4Fragment frg = new LaunchScreen4Fragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.launch_screen_fragment_4_layout,
				container, false);

		rootView = v;

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}

package com.instirepo.app.fragments;

import com.instirepo.app.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LaunchScreen2Fragment extends BaseFragment {

	public static LaunchScreen2Fragment newInstance(Bundle b) {
		LaunchScreen2Fragment frg = new LaunchScreen2Fragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.launch_screen_fragment_2_layout,
				container, false);
		
		rootView = v;

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}

package com.instirepo.app.fragments;

import com.instirepo.app.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CreatePostFragment2FragmentCollection extends BaseFragment {

	public static CreatePostFragment2FragmentCollection newInstance(Bundle b) {
		CreatePostFragment2FragmentCollection frg = new CreatePostFragment2FragmentCollection();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.create_post_fragment_2_fragment_collection, container,
				false);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}

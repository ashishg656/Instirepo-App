package com.instirepo.app.fragments;

import com.instirepo.app.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UserProfileEditProfileFragment extends BaseFragment {

	public static UserProfileEditProfileFragment newInstance(Bundle v) {
		UserProfileEditProfileFragment frg = new UserProfileEditProfileFragment();
		frg.setArguments(v);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.abc_action_bar_title_item,
				container, false);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}

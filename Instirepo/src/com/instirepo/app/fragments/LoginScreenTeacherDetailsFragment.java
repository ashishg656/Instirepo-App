package com.instirepo.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.instirepo.app.R;

public class LoginScreenTeacherDetailsFragment extends BaseFragment {

	public static LoginScreenTeacherDetailsFragment newInstance(Bundle b) {
		LoginScreenTeacherDetailsFragment frg = new LoginScreenTeacherDetailsFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.login_screen_fragment_2_teacher_details_layout,
				container, false);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}

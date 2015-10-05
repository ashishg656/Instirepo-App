package com.instirepo.app.fragments;

import com.instirepo.app.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoginScreenStudentDetailsFragment extends BaseFragment {

	public static LoginScreenStudentDetailsFragment newInstance(Bundle b) {
		LoginScreenStudentDetailsFragment frg = new LoginScreenStudentDetailsFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.login_screen_fragment_2_student_details_layout,
				container, false);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}

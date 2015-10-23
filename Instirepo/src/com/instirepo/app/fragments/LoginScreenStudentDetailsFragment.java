package com.instirepo.app.fragments;

import com.instirepo.app.R;
import com.instirepo.app.activities.ZLoginActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class LoginScreenStudentDetailsFragment extends BaseFragment implements
		OnClickListener {

	FrameLayout nextButton;

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

		nextButton = (FrameLayout) v.findViewById(R.id.nextbutton);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		nextButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.nextbutton) {
			((ZLoginActivity) getActivity()).switchToHomeActivity();
		}
	}

}

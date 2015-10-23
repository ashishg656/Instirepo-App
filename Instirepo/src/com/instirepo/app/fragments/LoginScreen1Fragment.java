package com.instirepo.app.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.instirepo.app.R;
import com.instirepo.app.activities.ZLoginActivity;
import com.instirepo.app.widgets.NothingSelectedSpinnerAdapter;

public class LoginScreen1Fragment extends BaseFragment {

	Spinner universitySpinner, collegeSpinner;
	FrameLayout nextButton;
	CheckBox isTeacherOrNotCheckBox;

	public static LoginScreen1Fragment newInstance(Bundle b) {
		LoginScreen1Fragment frg = new LoginScreen1Fragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.login_screen_fragment_1_layout,
				container, false);

		universitySpinner = (Spinner) v.findViewById(R.id.niversityspiner);
		collegeSpinner = (Spinner) v.findViewById(R.id.collegespinner);
		isTeacherOrNotCheckBox = (CheckBox) v.findViewById(R.id.checkbox);
		nextButton = (FrameLayout) v.findViewById(R.id.nextbutton);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isTeacherOrNotCheckBox.isChecked()) {
					((ZLoginActivity) getActivity())
							.setSecondFragmentForTeacherDetails();
				} else {
					((ZLoginActivity) getActivity())
							.setSecondFragmentForStudentDetails();
				}
			}
		});

		// university spinner
		List<String> objects = new ArrayList<>();
		objects.add("1");
		objects.add("1");
		objects.add("1");
		objects.add("1");
		ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
				android.R.layout.simple_spinner_dropdown_item, objects);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		NothingSelectedSpinnerAdapter adapterNothingSelected = new NothingSelectedSpinnerAdapter(
				adapter, R.layout.spinner_item_nothing_selected, getActivity(),
				"Select University");
		universitySpinner.setAdapter(adapterNothingSelected);
		universitySpinner.setPrompt("Select your university");

		// college spinner
		List<String> objFake = new ArrayList<>();
		ArrayAdapter<String> adapterCollege = new ArrayAdapter<>(getActivity(),
				android.R.layout.simple_spinner_dropdown_item, objFake);
		adapterCollege
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		NothingSelectedSpinnerAdapter adapterNothingSelectedCollege = new NothingSelectedSpinnerAdapter(
				adapterCollege, R.layout.spinner_item_nothing_selected,
				getActivity(), "Select University First");
		collegeSpinner.setAdapter(adapterNothingSelectedCollege);
	}
}

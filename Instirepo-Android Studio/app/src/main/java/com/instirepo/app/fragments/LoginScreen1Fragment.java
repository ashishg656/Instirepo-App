package com.instirepo.app.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.activities.LoginActivity;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.LoginScreenFragment1Object;
import com.instirepo.app.widgets.NothingSelectedSpinnerAdapter;

public class LoginScreen1Fragment extends BaseFragment implements ZUrls {

	Spinner universitySpinner, collegeSpinner;
	FrameLayout nextButton;
	CheckBox isTeacherOrNotCheckBox;
	LoginScreenFragment1Object mData;

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

		setProgressLayoutVariablesAndErrorVariables(v);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (chechWhetherDetailsFilled()) {
					((LoginActivity) getActivity()).collegeIDSelected = mData
							.getColleges_list()
							.get(collegeSpinner.getSelectedItemPosition() - 1)
							.getId();
					((LoginActivity) getActivity()).universityID = mData
							.getUniversity_list()
							.get(universitySpinner.getSelectedItemPosition() - 1)
							.getId();
					if (isTeacherOrNotCheckBox.isChecked()) {
						((LoginActivity) getActivity()).isProfessor = true;
						((LoginActivity) getActivity())
								.setSecondFragmentForTeacherDetails();
					} else {
						((LoginActivity) getActivity()).isProfessor = false;
						((LoginActivity) getActivity())
								.setSecondFragmentForStudentDetails();
					}
				}
			}
		});

		loadData();
	}

	protected boolean chechWhetherDetailsFilled() {
		if (universitySpinner.getSelectedItemPosition() < 1) {
			makeToast("Select University");
			return false;
		} else if (collegeSpinner.getSelectedItemPosition() < 1) {
			makeToast("Select College");
			return false;
		}
		return true;
	}

	private void loadData() {
		showLoadingLayout();
		hideErrorLayout();

		StringRequest req = new StringRequest(getAllCollegesAndUniversities,
				new Listener<String>() {
					@Override
					public void onResponse(String res) {
						mData = new Gson().fromJson(res,
								LoginScreenFragment1Object.class);
						setAdapterData();

						hideErrorLayout();
						hideLoadingLayout();
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						hideLoadingLayout();
						showErrorLayout();
					}
				});
		ZApplication.getInstance().addToRequestQueue(req,
				getAllCollegesAndUniversities);
	}

	protected void setAdapterData() {
		// university spinner
		List<String> objects = new ArrayList<>();
		for (int i = 0; i < mData.getUniversity_list().size(); i++) {
			objects.add(mData.getUniversity_list().get(i).getName());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
				android.R.layout.simple_spinner_dropdown_item, objects);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		NothingSelectedSpinnerAdapter adapterNothingSelected = new NothingSelectedSpinnerAdapter(
				adapter, R.layout.spinner_item_nothing_selected, getActivity(),
				"Select University");
		universitySpinner.setAdapter(adapterNothingSelected);
		universitySpinner.setPrompt("Select your university");

		universitySpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {
						if (pos != 0) {
							List<String> objFake = new ArrayList<>();
							for (int i = 0; i < mData.getColleges_list().size(); i++) {
								if (mData.getColleges_list().get(i)
										.getUniversity_id() == mData
										.getUniversity_list().get(pos - 1)
										.getId()) {
									objFake.add(mData.getColleges_list().get(i)
											.getName());
								}
							}
							ArrayAdapter<String> adapterCollege = new ArrayAdapter<>(
									getActivity(),
									android.R.layout.simple_spinner_dropdown_item,
									objFake);
							adapterCollege
									.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							NothingSelectedSpinnerAdapter adapterNothingSelectedCollege = new NothingSelectedSpinnerAdapter(
									adapterCollege,
									R.layout.spinner_item_nothing_selected,
									getActivity(), "Select College");
							collegeSpinner
									.setAdapter(adapterNothingSelectedCollege);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
	}
}

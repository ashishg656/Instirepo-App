package com.instirepo.app.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.activities.HomeActivity;
import com.instirepo.app.activities.LoginActivity;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.LoginScreenFragment2Object;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.widgets.NothingSelectedSpinnerAdapter;

public class LoginScreenTeacherDetailsFragment extends BaseFragment implements
		OnClickListener, ZUrls {

	FrameLayout nextButton;
	Spinner branchSpinner;
	EditText enrollmentNumber;

	LoginScreenFragment2Object mData;
	ProgressDialog progressDialog;
	LinearLayout checkBoxContainer;

	public static LoginScreenTeacherDetailsFragment newInstance(Bundle b) {
		LoginScreenTeacherDetailsFragment frg = new LoginScreenTeacherDetailsFragment();
		frg.setArguments(b);
		return frg;
	}

	/*
	 * <CheckBox android:layout_width="wrap_content"
	 * android:layout_height="wrap_content" android:layout_marginTop="8dp"
	 * android:text="IT" android:textColor="@color/z_text_color_medium_dark" />
	 */

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.login_screen_fragment_2_teacher_details_layout,
				container, false);

		nextButton = (FrameLayout) v.findViewById(R.id.nextbutton);
		enrollmentNumber = (EditText) v.findViewById(R.id.enrollmentnumebr);
		branchSpinner = (Spinner) v.findViewById(R.id.branchspinner);
		setProgressLayoutVariablesAndErrorVariables(v);
		checkBoxContainer = (LinearLayout) v
				.findViewById(R.id.brancheslayoutinflate);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		nextButton.setOnClickListener(this);

		loadData();
	}

	private void loadData() {
		showLoadingLayout();
		hideErrorLayout();

		StringRequest req = new StringRequest(Method.POST,
				userRegistrationStep1Url, new Listener<String>() {
					@Override
					public void onResponse(String res) {
						mData = new Gson().fromJson(res,
								LoginScreenFragment2Object.class);
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
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("college_id",
						((LoginActivity) getActivity()).collegeIDSelected + "");
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req,
				userRegistrationStep1Url);
	}

	protected void setAdapterData() {
		List<String> objects = new ArrayList<>();
		for (int i = 0; i < mData.getBranches_list().size(); i++) {
			objects.add(mData.getBranches_list().get(i).getBranch_name());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
				android.R.layout.simple_spinner_dropdown_item, objects);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		NothingSelectedSpinnerAdapter adapterNothingSelected = new NothingSelectedSpinnerAdapter(
				adapter, R.layout.spinner_item_nothing_selected, getActivity(),
				"Select Department");
		branchSpinner.setAdapter(adapterNothingSelected);
		branchSpinner.setPrompt("Select your university");

		checkBoxContainer.removeAllViews();
		for (String branch : objects) {
			CheckBox checkBox = new CheckBox(getActivity());
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			params.topMargin = getActivity().getResources()
					.getDimensionPixelSize(R.dimen.z_margin_small);
			checkBox.setTextColor(getActivity().getResources().getColor(
					R.color.z_text_color_medium_dark));
			checkBox.setText(branch);

			checkBoxContainer.addView(checkBox);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.nextbutton) {
			if (checkifAllFieldsComplete()) {
				makeRequestToRegisterUser();
			}
		}
	}

	private void makeRequestToRegisterUser() {
		if (progressDialog != null)
			progressDialog.dismiss();
		progressDialog = ProgressDialog.show(getActivity(), null,
				"Registering Details");

		StringRequest req = new StringRequest(Method.POST,
				userRegistrationStep2UrlForStudent, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						if (progressDialog != null)
							progressDialog.dismiss();

						ZPreferences.setIsUserLogin(getActivity(), true);
						Intent i = new Intent(getActivity(), HomeActivity.class);
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
								| Intent.FLAG_ACTIVITY_CLEAR_TASK
								| Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(i);
						getActivity().finish();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						if (progressDialog != null)
							progressDialog.dismiss();

						makeToast("Some Error Occured.Please try again");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> p = new HashMap<>();
				p.put("user_id", ZPreferences.getUserProfileID(getActivity()));
				p.put("university_id",
						((LoginActivity) getActivity()).universityID + "");
				p.put("college_id",
						((LoginActivity) getActivity()).collegeIDSelected + "");
				p.put("enrollment_number", enrollmentNumber.getText()
						.toString().trim());
				p.put("branch_id",
						mData.getBranches_list()
								.get(branchSpinner.getSelectedItemPosition() - 1)
								.getBranch_id()
								+ "");
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req,
				userRegistrationStep2UrlForStudent);
	}

	private boolean checkifAllFieldsComplete() {
		if (enrollmentNumber.getText().toString().trim().length() < 1) {
			makeToast("Enter Enrollment number");
			return false;
		} else if (branchSpinner.getSelectedItemPosition() < 1) {
			makeToast("Select Department");
			return false;
		}
		return true;
	}

}

package com.instirepo.app.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
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

public class LoginScreenStudentDetailsFragment extends BaseFragment implements
		OnClickListener, ZUrls {

	FrameLayout nextButton;

	Spinner branchSpinner, batchSpinner, yearSpinner;
	EditText enrollmentNumber;

	LoginScreenFragment2Object mData;

	List<LoginScreenFragment2Object.Years> yearsTemp;
	List<LoginScreenFragment2Object.Batches> batchesTemp;

	ProgressDialog progressDialog;

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
		enrollmentNumber = (EditText) v.findViewById(R.id.enrollmentnumebr);
		branchSpinner = (Spinner) v.findViewById(R.id.branchspinner);
		batchSpinner = (Spinner) v.findViewById(R.id.batchspinnerF);
		yearSpinner = (Spinner) v.findViewById(R.id.yearsFspinnerF);

		setProgressLayoutVariablesAndErrorVariables(v);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		yearsTemp = new ArrayList<>();
		batchesTemp = new ArrayList<LoginScreenFragment2Object.Batches>();

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
				Log.w("aS", "co "
						+ ((LoginActivity) getActivity()).collegeIDSelected);
				p.put("college_id",
						((LoginActivity) getActivity()).collegeIDSelected + "");
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req,
				getAllCollegesAndUniversities);
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
				"Select Branch");
		branchSpinner.setAdapter(adapterNothingSelected);
		branchSpinner.setPrompt("Select your university");

		branchSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				if (pos != 0) {
					List<String> objFake = new ArrayList<>();
					yearsTemp = new ArrayList<>();
					for (int i = 0; i < mData.getYears_list().size(); i++) {
						if (mData.getYears_list().get(i).getBranch_id() == mData
								.getBranches_list().get(pos - 1).getBranch_id()) {
							LoginScreenFragment2Object.Years year = mData
									.getYears_list().get(i);
							String yearName = year.getYear_name() + " ("
									+ year.getAdmission_year() + "-"
									+ year.getPassout_year() + ")";
							objFake.add(yearName);

							yearsTemp.add(year);
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
							getActivity(), "Select Academic Year");
					yearSpinner.setAdapter(adapterNothingSelectedCollege);

					batchSpinner.setAdapter(null);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		yearSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int posInt, long arg3) {
				if (posInt != 0) {
					List<String> objFake = new ArrayList<>();
					batchesTemp = new ArrayList<LoginScreenFragment2Object.Batches>();
					for (int i = 0; i < mData.getBatches_list().size(); i++) {
						if (mData.getBatches_list().get(i).getYear_id() == yearsTemp
								.get(posInt - 1).getYear_id()) {
							objFake.add(mData.getBatches_list().get(i)
									.getBatch_name());

							batchesTemp.add(mData.getBatches_list().get(i));
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
							getActivity(), "Select Batch");
					batchSpinner.setAdapter(adapterNothingSelectedCollege);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
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
				p.put("year_id",
						yearsTemp
								.get(yearSpinner.getSelectedItemPosition() - 1)
								.getYear_id()
								+ "");
				p.put("batch_id",
						mData.getBatches_list()
								.get(batchSpinner.getSelectedItemPosition() - 1)
								.getBatch_id()
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
			makeToast("Select Branch");
			return false;
		} else if (yearSpinner.getSelectedItemPosition() < 1) {
			makeToast("Select Year");
			return false;
		} else if (batchSpinner.getSelectedItemPosition() < 1) {
			makeToast("Select Batch");
			return false;
		}
		return true;
	}

}

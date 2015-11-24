package com.instirepo.app.fragments;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.activities.CreatePostActivity;
import com.instirepo.app.adapters.CreatePostSelectBranchListAdapter;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.LoginScreenFragment2Object;
import com.instirepo.app.preferences.ZPreferences;

public class CreatePostSelectBranchFragment extends BaseFragment implements
		AppConstants, ZUrls, OnClickListener {

	LoginScreenFragment2Object mData;
	ListView listView;
	TextView okButton;

	public static CreatePostSelectBranchFragment newInstance(Bundle b) {
		CreatePostSelectBranchFragment frg = new CreatePostSelectBranchFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.create_post__select_branch_batch_year_fragment_layout,
				container, false);

		listView = (ListView) v.findViewById(R.id.postsbyreachersrecyclef);
		setProgressLayoutVariablesAndErrorVariables(v);
		okButton = (TextView) v.findViewById(R.id.okbuttonseen);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		okButton.setOnClickListener(this);

		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		loadData();
	}

	private void loadData() {
		fetchBranches();
	}

	private void fetchBranches() {
		showLoadingLayout();
		hideErrorLayout();

		StringRequest req = new StringRequest(Method.POST,
				userRegistrationStep1Url, new Listener<String>() {
					@Override
					public void onResponse(String res) {
						mData = new Gson().fromJson(res,
								LoginScreenFragment2Object.class);

						if (getActivity() != null) {
							CreatePostSelectBranchListAdapter adapter = new CreatePostSelectBranchListAdapter(
									getActivity(), mData.getBranches_list());
							listView.setAdapter(adapter);
						}

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
				p.put("user_id", ZPreferences.getUserProfileID(getActivity()));
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req,
				userRegistrationStep1Url);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.okbuttonseen:
			((CreatePostActivity) getActivity()).updateBranchesList(
					listView.getCheckedItemIds(), mData);
			break;

		default:
			break;
		}
	}

}

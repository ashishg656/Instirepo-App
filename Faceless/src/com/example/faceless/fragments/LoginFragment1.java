package com.example.faceless.fragments;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.faceless.R;
import com.example.faceless.activities.LoginActivity;
import com.example.faceless.application.ZApplication;
import com.example.faceless.extras.RequestTags;
import com.example.faceless.objects.ZCheckTeamObject;
import com.google.gson.Gson;

public class LoginFragment1 extends BaseFragment implements RequestTags {

	EditText teamName;
	Button continueButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.signup_fragment_1_layout, container,
				false);

		teamName = (EditText) v.findViewById(R.id.teamname);
		continueButton = (Button) v.findViewById(R.id.continueb);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = teamName.getText().toString();
				if (name.trim().length() > 0) {
					checkForTeamNameFromServer(name.trim());
				} else {
					makeToast("Enter team name");
				}
			}
		});
	}

	protected void checkForTeamNameFromServer(final String name) {
		((LoginActivity) getActivity()).teamName = name;
		String url = "check_team_exist/";
		StringRequest request = new StringRequest(Method.POST,
				ZApplication.getBaseUrl() + url, new Listener<String>() {

					@Override
					public void onResponse(String res) {
						ZCheckTeamObject obj = new Gson().fromJson(res,
								ZCheckTeamObject.class);
						if (obj.isExist()) {
							((LoginActivity) getActivity()).setSecondFragment();
						} else {
							makeToast("Team do not exist");
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						makeToast("Some error occured");
					}
				}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> params = new HashMap<>();
				params.put("team_name", name);
				return params;
			}
		};
		ZApplication.getInstance().addToRequestQueue(request,
				Z_CHECK_TEAM_EXIST_OR_NOT);
	}

}

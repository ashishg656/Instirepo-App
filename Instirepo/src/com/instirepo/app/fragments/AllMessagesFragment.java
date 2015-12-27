package com.instirepo.app.fragments;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.adapters.AllMessageListAdapter;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.AllMessagesListObject;
import com.instirepo.app.preferences.ZPreferences;

public class AllMessagesFragment extends BaseFragment implements ZUrls,
		AppConstants {

	ListView listView;
	AllMessageListAdapter adapter;

	public static AllMessagesFragment newInstance(Bundle b) {
		AllMessagesFragment frg = new AllMessagesFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.all_messages_fragment_layout,
				container, false);

		listView = (ListView) v.findViewById(R.id.allmessgaes);
		setProgressLayoutVariablesAndErrorVariables(v);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		loadData();
	}

	private void loadData() {
		showLoadingLayout();
		hideErrorLayout();

		StringRequest req = new StringRequest(Method.POST, getAllMessagesList,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						hideErrorLayout();
						hideLoadingLayout();

						AllMessagesListObject obj = new Gson().fromJson(arg0,
								AllMessagesListObject.class);
						setAdapterData(obj);
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
		ZApplication.getInstance().addToRequestQueue(req, getAllMessagesList);
	}

	protected void setAdapterData(AllMessagesListObject obj) {
		adapter = new AllMessageListAdapter(obj.getNames(), getActivity());
		listView.setAdapter(adapter);
	}
}

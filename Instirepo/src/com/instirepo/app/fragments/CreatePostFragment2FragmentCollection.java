package com.instirepo.app.fragments;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.adapters.CreatePostSavedPostVisibilityCollectionAdapter;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.SavedPostVisibilityCollectionObject;
import com.instirepo.app.preferences.ZPreferences;

public class CreatePostFragment2FragmentCollection extends BaseFragment
		implements ZUrls {

	RecyclerView recyclerView;
	CreatePostSavedPostVisibilityCollectionAdapter adapter;
	GridLayoutManager layoutManager;

	public static CreatePostFragment2FragmentCollection newInstance(Bundle b) {
		CreatePostFragment2FragmentCollection frg = new CreatePostFragment2FragmentCollection();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.create_post_fragment_2_fragment_collection, container,
				false);

		recyclerView = (RecyclerView) v
				.findViewById(R.id.postsbyreachersrecyclef);
		setProgressLayoutVariablesAndErrorVariables(v);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		layoutManager = new GridLayoutManager(getActivity(), 3);
		recyclerView.setLayoutManager(layoutManager);

		loadData();
	}

	public void loadData() {
		showLoadingLayout();
		hideErrorLayout();

		StringRequest req = new StringRequest(Method.POST,
				getSavedPostVisibilities, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						hideErrorLayout();
						hideLoadingLayout();

						SavedPostVisibilityCollectionObject obj = new Gson()
								.fromJson(
										arg0,
										SavedPostVisibilityCollectionObject.class);

						if (getActivity() == null)
							return;
						adapter = new CreatePostSavedPostVisibilityCollectionAdapter(
								getActivity(), obj);
						recyclerView.setAdapter(adapter);
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
				getSavedPostVisibilities);
	}

}

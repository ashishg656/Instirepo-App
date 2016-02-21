package com.instirepo.app.fragments;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.instirepo.app.adapters.CreatePostSelectTeacherListAdapter;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.TeachersListObject;
import com.instirepo.app.preferences.ZPreferences;

public class CreatePostSelectTeacherFragment extends BaseFragment implements
		OnClickListener, ZUrls {

	RecyclerView recyclerView;
	LinearLayoutManager layoutManager;
	CreatePostSelectTeacherListAdapter adapter;
	TextView okButton, dialogHeading;

	boolean isRequestRunning;
	boolean isMoreAllowed = true;
	Integer nextPage = 1;

	public static CreatePostSelectTeacherFragment newInstance(Bundle b) {
		CreatePostSelectTeacherFragment frg = new CreatePostSelectTeacherFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.seen_by_poeple_dialog_fragment_layout, container,
				false);

		recyclerView = (RecyclerView) v
				.findViewById(R.id.postsbyreachersrecyclef);
		okButton = (TextView) v.findViewById(R.id.okbuttonseen);
		setProgressLayoutVariablesAndErrorVariables(v);
		dialogHeading = (TextView) v
				.findViewById(R.id.seenbydialogheadingofrdelectteacher);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);

		layoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(layoutManager);

		dialogHeading.setText(getActivity().getResources().getString(
				R.string.z_select_teacher));

		okButton.setOnClickListener(this);

		recyclerView.addOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				if (recyclerView.getAdapter() != null) {
					int lastitem = layoutManager.findLastVisibleItemPosition();
					int totalitems = recyclerView.getAdapter().getItemCount();
					int diff = totalitems - lastitem;
					if (diff < 5 && !isRequestRunning && isMoreAllowed) {
						loadData();
					}
				}
				super.onScrolled(recyclerView, dx, dy);
			}
		});

		loadData();
	}

	void loadData() {
		isRequestRunning = true;
		if (adapter == null) {
			showLoadingLayout();
			hideErrorLayout();
		}
		String url = getAllTeachersList + "?pagenumber=" + nextPage;
		StringRequest req = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String res) {
						hideErrorLayout();
						hideLoadingLayout();

						isRequestRunning = false;
						TeachersListObject obj = new Gson().fromJson(res,
								TeachersListObject.class);
						setAdapterData(obj);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						isRequestRunning = false;
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
		ZApplication.getInstance().addToRequestQueue(req, getAllTeachersList);
	}

	void setAdapterData(TeachersListObject obj) {
		nextPage = obj.getNext_page();
		if (nextPage == null)
			isMoreAllowed = false;

		if (adapter == null) {
			adapter = new CreatePostSelectTeacherListAdapter(getActivity(),
					obj.getTeachers(), isMoreAllowed);
			recyclerView.setAdapter(adapter);
		} else {
			adapter.addData(obj.getTeachers(), isMoreAllowed);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.okbuttonseen:
			try {
				((CreatePostActivity) getActivity()).updateTeachersList(
						adapter.teacherIds, adapter.teacherName);
			} catch (Exception e) {
			}
			break;

		default:
			break;
		}
	}
}

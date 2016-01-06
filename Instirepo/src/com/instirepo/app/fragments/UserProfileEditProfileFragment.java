package com.instirepo.app.fragments;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.instirepo.app.R;
import com.instirepo.app.activities.UserProfileActivity;
import com.instirepo.app.adapters.UserProfileEditProfileListAdapter;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.UserProfileEditProfileObject;
import com.instirepo.app.preferences.ZPreferences;

public class UserProfileEditProfileFragment extends UserProfileBaseFragment
		implements ZUrls {

	boolean canScrollViewPagerHeader;

	public static UserProfileEditProfileFragment newInstance(Bundle v) {
		UserProfileEditProfileFragment frg = new UserProfileEditProfileFragment();
		frg.setArguments(v);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.user_profile_edit_profile_fragment_layout, container,
				false);

		recyclerView = (RecyclerView) v
				.findViewById(R.id.postsbyreachersrecyclef);
		progressSuperContainer = (LinearLayout) v
				.findViewById(R.id.progressupercontainer);
		setProgressLayoutVariablesAndErrorVariables(v);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		layoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(layoutManager);

		recyclerView.setPadding(0, 0, 0, 0);

		recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView,
					int newState) {
				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					int pos = layoutManager.findFirstVisibleItemPosition();
					if (pos == 0) {
						((UserProfileActivity) getActivity())
								.setToolbarTranslation(recyclerView
										.getChildAt(0));
					} else
						((UserProfileActivity) getActivity())
								.scrollToolbarAfterTouchEnds();
				}
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView r, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if (canScrollViewPagerHeader) {
					((UserProfileActivity) getActivity()).scrollToolbarBy(-dy);

					((UserProfileActivity) getActivity())
							.scrollFragmentRecycler(layoutManager
									.findFirstVisibleItemPosition(),
									recyclerView.getChildAt(0).getTop(), dy,
									recyclerView.getScrollY());
				} else {
					canScrollViewPagerHeader = true;
				}
			}
		});

		loadData();
	}

	private void loadData() {
		if (adapter == null) {
			showLoadingLayout();
			hideErrorLayout();
		}
		String url = userProfileViewedByHimself;
		StringRequest req = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String res) {
						setAdapterData(null);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError err) {

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
				userProfileViewedByHimself);
	}

	void setAdapterData(UserProfileEditProfileObject obj) {
		hideLoadingLayout();
		hideErrorLayout();
		adapter = new UserProfileEditProfileListAdapter(getActivity(), obj);
		recyclerView.setAdapter(adapter);
	}

}

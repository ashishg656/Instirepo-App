package com.instirepo.app.fragments;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.activities.HomeActivity;
import com.instirepo.app.adapters.PostsByTeachersListAdapter;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.gcm.RegistrationIntentService;
import com.instirepo.app.objects.PostsListObject;
import com.instirepo.app.preferences.ZPreferences;

public class PostsByTeachersFragment extends BaseFragment implements ZUrls,
		OnRefreshListener {

	RecyclerView recyclerView;
	LinearLayoutManager layoutManager;
	PostsByTeachersListAdapter adapter;
	SwipeRefreshLayout swipeRefreshLayout;

	boolean isRequestRunning;
	Integer nextPage = 1;
	boolean isMoreAllowed = true;

	public static PostsByTeachersFragment newInstance(Bundle b) {
		PostsByTeachersFragment frg = new PostsByTeachersFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.posts_by_teacher_fragment_layout,
				container, false);

		recyclerView = (RecyclerView) v
				.findViewById(R.id.postsbyreachersrecyclef);
		setProgressLayoutVariablesAndErrorVariables(v);
		swipeRefreshLayout = (SwipeRefreshLayout) v
				.findViewById(R.id.swipe_refresh_layout);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		layoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(layoutManager);

		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorSchemeResources(R.color.z_red_color_primary,
				R.color.PrimaryDarkColor, R.color.z_green_color_primary,
				R.color.purple_post);
		swipeRefreshLayout.setProgressViewOffset(
				false,
				getResources().getDimensionPixelSize(
						R.dimen.z_swipe_refresh_start_pos),
				getResources().getDimensionPixelSize(
						R.dimen.z_swipe_refresh_rest_pos));

		recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView,
					int newState) {
				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					int pos = layoutManager.findFirstVisibleItemPosition();
					if (pos == 0) {
						((HomeActivity) getActivity())
								.setToolbarTranslation(recyclerView
										.getChildAt(0));
					} else
						((HomeActivity) getActivity())
								.scrollToolbarAfterTouchEnds();
				}
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				((HomeActivity) getActivity()).scrollToolbarBy(-dy);
				if (recyclerView.getAdapter() != null) {
					int lastitem = layoutManager.findLastVisibleItemPosition();
					int totalitems = recyclerView.getAdapter().getItemCount();
					int diff = totalitems - lastitem;
					if (diff < 6 && !isRequestRunning && isMoreAllowed) {
						loadData();
					}
				}
				super.onScrolled(recyclerView, dx, dy);
			}
		});

		loadData();
	}

	private void loadData() {
		isRequestRunning = true;
		if (adapter == null) {
			showLoadingLayout();
			hideErrorLayout();
		}
		String url = teacherPostsUrl + "?pagenumber=" + nextPage;
		StringRequest req = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String res) {
						isRequestRunning = false;
						if (adapter == null) {
							hideErrorLayout();
							hideLoadingLayout();

							if (ZPreferences.isUserLogIn(getActivity())) {
								Intent intent = new Intent(getActivity(),
										RegistrationIntentService.class);
								getActivity().startService(intent);
							}
						}
						PostsListObject obj = new Gson().fromJson(res,
								PostsListObject.class);
						setAdapterData(obj);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError err) {
						isRequestRunning = false;
						System.out.print(err.networkResponse);
						if (adapter == null) {
							showErrorLayout();
							hideLoadingLayout();
						}
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("user_id", ZPreferences.getUserProfileID(getActivity()));
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, teacherPostsUrl);
	}

	protected void setAdapterData(PostsListObject obj) {
		nextPage = obj.getNext_page();
		if (nextPage == null) {
			isMoreAllowed = false;
		}
		if (adapter == null) {
			adapter = new PostsByTeachersListAdapter(getActivity(),
					obj.getPosts(), isMoreAllowed);
			recyclerView.setAdapter(adapter);
		} else {
			adapter.addData(obj.getPosts(), isMoreAllowed);
		}
	}

	@Override
	public void onRefresh() {
		swipeRefreshLayout.setRefreshing(true);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						makeToast("Content Refreshed");
						swipeRefreshLayout.setRefreshing(false);
					}
				});
			}
		}, 2000);
	}
}

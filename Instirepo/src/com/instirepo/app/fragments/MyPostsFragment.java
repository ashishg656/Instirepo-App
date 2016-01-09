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
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.activities.UserProfileActivity;
import com.instirepo.app.adapters.MyPostsTeacherListAdapter;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.PostsListObject;
import com.instirepo.app.preferences.ZPreferences;

public class MyPostsFragment extends UserProfileBaseFragment implements ZUrls {

	boolean isRequestRunning;
	Integer nextPage = 1;
	boolean isMoreAllowed = true;

	boolean canScrollViewPagerHeader;

	public static MyPostsFragment newInstance(Bundle v) {
		MyPostsFragment frg = new MyPostsFragment();
		frg.setArguments(v);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.my_posts_user_profile_fragment_layout, container,
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
					if (recyclerView.getAdapter() != null) {
						int lastitem = layoutManager
								.findLastVisibleItemPosition();
						int totalitems = recyclerView.getAdapter()
								.getItemCount();
						int diff = totalitems - lastitem;
						if (diff < 6 && !isRequestRunning && isMoreAllowed) {
							loadData();
						}
					}

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
		isRequestRunning = true;
		if (adapter == null) {
			showLoadingLayout();
			hideErrorLayout();
		}
		String url = getPostsPostedByUser + "?pagenumber=" + nextPage;
		StringRequest req = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String res) {
						isRequestRunning = false;
						PostsListObject obj = new Gson().fromJson(res,
								PostsListObject.class);
						setAdapterData(obj);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError err) {
						isRequestRunning = false;
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
			hideErrorLayout();
			hideLoadingLayout();
			adapter = new MyPostsTeacherListAdapter(getActivity(),
					obj.getPosts(), isMoreAllowed, false);
			recyclerView.setAdapter(adapter);
		} else {
			((MyPostsTeacherListAdapter) adapter).addData(obj.getPosts(),
					isMoreAllowed);
		}
	}

}

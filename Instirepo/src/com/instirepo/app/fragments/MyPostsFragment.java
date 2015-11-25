package com.instirepo.app.fragments;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import com.instirepo.app.activities.HomeActivity;
import com.instirepo.app.adapters.MyPostsTeacherListAdapter;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.PostsListObject;
import com.instirepo.app.preferences.ZPreferences;

public class MyPostsFragment extends BaseFragment implements ZUrls {

	RecyclerView recyclerView;
	LinearLayoutManager layoutManager;
	RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;

	boolean isRequestRunning;
	Integer nextPage = 1;
	boolean isMoreAllowed = true;

	public static MyPostsFragment newInstance(Bundle v) {
		MyPostsFragment frg = new MyPostsFragment();
		frg.setArguments(v);
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
		if (adapter == null) {
			showLoadingLayout();
			hideErrorLayout();
		}
		String url = teacherPostsUrl + "?pagenumber=" + nextPage;
		StringRequest req = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String res) {
						if (adapter == null) {
							hideErrorLayout();
							hideLoadingLayout();
						}
						PostsListObject obj = new Gson().fromJson(res,
								PostsListObject.class);
						setAdapterData(obj);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError err) {
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
			adapter = new MyPostsTeacherListAdapter(getActivity(),
					obj.getPosts(), isMoreAllowed);
			recyclerView.setAdapter(adapter);
		} else {
			((MyPostsTeacherListAdapter) adapter).addData(obj.getPosts(),
					isMoreAllowed);
		}
	}
}

package com.instirepo.app.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.adapters.SeenByPeopleListAdapter;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.SeenByPeopleObject;

public class SeenByPeopleFragment extends BaseFragment implements
		OnClickListener, ZUrls {

	RecyclerView recyclerView;
	LinearLayoutManager layoutManager;
	SeenByPeopleListAdapter adapter;
	TextView okButton;
	int postId;

	boolean isRequestRunning;
	boolean isMoreAllowed = true;
	Integer nextPage = 1;

	public static SeenByPeopleFragment newInstance(Bundle b) {
		SeenByPeopleFragment frg = new SeenByPeopleFragment();
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

		return v;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);

		layoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(layoutManager);

		okButton.setOnClickListener(this);
		postId = getArguments().getInt("postid");

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
		final String url = getPeopleWhoSawPost + "?pagenumber=" + nextPage
				+ "&post_id=" + postId;
		StringRequest req = new StringRequest(Method.GET, url,
				new Listener<String>() {

					@Override
					public void onResponse(String res) {
						isRequestRunning = false;
						SeenByPeopleObject obj = new Gson().fromJson(res,
								SeenByPeopleObject.class);
						setAdapterData(obj);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						isRequestRunning = false;
						try {
							Cache cache = ZApplication.getInstance()
									.getRequestQueue().getCache();
							Entry entry = cache.get(url);
							String data = new String(entry.data, "UTF-8");
							SeenByPeopleObject obj = new Gson().fromJson(data,
									SeenByPeopleObject.class);
							setAdapterData(obj);
						} catch (Exception e) {
							if (adapter == null) {
								hideLoadingLayout();
								showErrorLayout();
							}
						}
					}
				});
		ZApplication.getInstance().addToRequestQueue(req, getPeopleWhoSawPost);
	}

	void setAdapterData(SeenByPeopleObject obj) {
		nextPage = obj.getNext_page();
		if (nextPage == null)
			isMoreAllowed = false;

		if (adapter == null) {
			hideErrorLayout();
			hideLoadingLayout();

			adapter = new SeenByPeopleListAdapter(getActivity(),
					obj.getSeens(), isMoreAllowed);
			recyclerView.setAdapter(adapter);
		} else {
			adapter.addData(obj.getSeens(), isMoreAllowed);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.okbuttonseen:
			getActivity().onBackPressed();
			break;

		default:
			break;
		}
	}
}

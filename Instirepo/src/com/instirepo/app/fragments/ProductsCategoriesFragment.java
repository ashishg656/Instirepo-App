package com.instirepo.app.fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.adapters.ProductCategoriesListAdapter;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.ProductCategoriesListObject;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.serverApi.AppRequestListener;
import com.instirepo.app.serverApi.CustomStringRequest;

public class ProductsCategoriesFragment extends BaseFragment implements ZUrls,
		AppRequestListener {

	RecyclerView recyclerView;
	GridLayoutManager layoutManager;

	ProductCategoriesListAdapter adapter;
	String url;
	public ProductCategoriesListObject mData;

	public static ProductsCategoriesFragment newInstance(Bundle b) {
		ProductsCategoriesFragment frg = new ProductsCategoriesFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.product_categories_fragment_layout,
				container, false);

		recyclerView = (RecyclerView) v
				.findViewById(R.id.postsbyreachersrecyclef);
		setProgressLayoutVariablesAndErrorVariables(v);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		layoutManager = new GridLayoutManager(getActivity(), 2);
		layoutManager.setSpanSizeLookup(new SpanSizeLookup() {

			@Override
			public int getSpanSize(int pos) {
				if (adapter.getItemViewType(pos) == ProductCategoriesListAdapter.ITEM_CATEGORY) {
					return 1;
				} else
					return 2;
			}
		});
		recyclerView.setLayoutManager(layoutManager);

		loadData();
	}

	private void loadData() {
		url = productCategoriesAndTrendingProducts + "user_id="
				+ ZPreferences.getUserProfileID(getActivity());

		CustomStringRequest req = new CustomStringRequest(Method.POST, url,
				productCategoriesAndTrendingProducts, this, null);
		ZApplication.getInstance().addToRequestQueue(req,
				productCategoriesAndTrendingProducts);
	}

	@Override
	public void onRequestStarted(String requestTag) {
		if (requestTag.equals(productCategoriesAndTrendingProducts)) {
			showLoadingLayout();
			hideErrorLayout();
		}
	}

	@Override
	public void onRequestFailed(String requestTag, VolleyError error) {
		if (requestTag.equals(productCategoriesAndTrendingProducts)) {
			try {
				Cache cache = ZApplication.getInstance().getRequestQueue()
						.getCache();
				Entry entry = cache.get(url);
				String data = new String(entry.data, "UTF-8");
				ProductCategoriesListObject obj = new Gson().fromJson(data,
						ProductCategoriesListObject.class);
				setAdapterData(obj);
			} catch (Exception e) {
				hideLoadingLayout();
				showErrorLayout();
			}
		}
	}

	@Override
	public void onRequestCompleted(String requestTag, String response) {
		if (requestTag.equals(productCategoriesAndTrendingProducts)) {
			ProductCategoriesListObject obj = new Gson().fromJson(response,
					ProductCategoriesListObject.class);
			setAdapterData(obj);
		}
	}

	private void setAdapterData(ProductCategoriesListObject obj) {
		mData = obj;
		hideLoadingLayout();
		hideErrorLayout();

		adapter = new ProductCategoriesListAdapter(obj, getActivity());
		recyclerView.setAdapter(adapter);
	}

}

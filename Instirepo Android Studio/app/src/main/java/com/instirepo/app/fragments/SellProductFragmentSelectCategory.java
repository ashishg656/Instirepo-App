package com.instirepo.app.fragments;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.adapters.SellProductSelectCategoryFragmentListAdapter;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.ProductCategoriesListObject;
import com.instirepo.app.serverApi.AppRequestListener;
import com.instirepo.app.serverApi.CustomStringRequest;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SellProductFragmentSelectCategory extends BaseFragment implements ZUrls, AppRequestListener {

	RecyclerView recyclerView;
	LinearLayoutManager layoutManager;

	SellProductSelectCategoryFragmentListAdapter adapter;

	public static SellProductFragmentSelectCategory newInstance(Bundle b) {
		SellProductFragmentSelectCategory frg = new SellProductFragmentSelectCategory();
		frg.setArguments(b);
		return frg;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.sell_product_fragment_select_category, container, false);

		setProgressLayoutVariablesAndErrorVariables(v);
		recyclerView = (RecyclerView) v.findViewById(R.id.postsbyreachersrecyclef);

		return v;
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		layoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(layoutManager);

		loadData();
	}

	void loadData() {
		CustomStringRequest request = new CustomStringRequest(Method.GET, getAllProductCategories,
				getAllProductCategories, this, null);
		ZApplication.getInstance().addToRequestQueue(request, getAllProductCategories);
	}

	@Override
	public void onRequestStarted(String requestTag) {
		if (requestTag.equals(getAllProductCategories)) {
			hideErrorLayout();
			showLoadingLayout();
		}
	}

	@Override
	public void onRequestFailed(String requestTag, VolleyError error) {
		if (requestTag.equals(getAllProductCategories)) {
			try {
				Cache cache = ZApplication.getInstance().getRequestQueue().getCache();
				Entry entry = cache.get(getAllProductCategories);
				String data = new String(entry.data, "UTF-8");
				ProductCategoriesListObject obj = new Gson().fromJson(data, ProductCategoriesListObject.class);
				setAdapterData(obj);
			} catch (Exception e) {
				hideLoadingLayout();
				showErrorLayout();
			}
		}
	}

	private void setAdapterData(ProductCategoriesListObject obj) {
		hideLoadingLayout();
		hideErrorLayout();

		adapter = new SellProductSelectCategoryFragmentListAdapter(obj.getCategories(), getActivity());
		recyclerView.setAdapter(adapter);
	}

	@Override
	public void onRequestCompleted(String requestTag, String response) {
		if (requestTag.equals(getAllProductCategories)) {
			ProductCategoriesListObject obj = new Gson().fromJson(response, ProductCategoriesListObject.class);
			setAdapterData(obj);
		}
	}
}

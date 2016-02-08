package com.instirepo.app.activities;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView.OnScrollListener;

import com.android.volley.Cache;
import com.android.volley.VolleyError;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.adapters.ProductListingListAdapter;
import com.instirepo.app.adapters.SeenByPeopleListAdapter;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.ProductsListingListObject;
import com.instirepo.app.objects.SeenByPeopleObject;
import com.instirepo.app.preferences.ZPreferences;

public class ProductsListingActivity extends BaseActivity implements
		AppConstants, ZUrls {

	int typeOfListing;
	int categoryId;

	RecyclerView recyclerView;
	StaggeredGridLayoutManager layoutManager;

	Integer nextPage = 1;
	boolean isRequestRunning, isMoreAllowed = true;
	ProductListingListAdapter adapter;

	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.products_listing_activity_layout);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Products");

		setProgressLayoutVariablesAndErrorVariables();

		recyclerView = (RecyclerView) findViewById(R.id.productlist);

		layoutManager = new StaggeredGridLayoutManager(2,
				StaggeredGridLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);

		typeOfListing = getIntent().getExtras().getInt("typeoflisting");
		if (typeOfListing == LISTING_BY_CATEGORY) {
			categoryId = getIntent().getExtras().getInt("categoryid");
			url = productListingByCategory + "user_id="
					+ ZPreferences.getUserProfileID(this) + "&category_id="
					+ categoryId;
		}

		recyclerView.addOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				if (recyclerView.getAdapter() != null) {
					int last[] = new int[2];
					layoutManager.findFirstVisibleItemPositions(last);
					int lastitem = last[1];
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
		url = url + "&page_number=" + nextPage;
		StringRequest req = new StringRequest(Method.GET, url,
				new Listener<String>() {

					@Override
					public void onResponse(String res) {
						isRequestRunning = false;
						ProductsListingListObject obj = new Gson().fromJson(
								res, ProductsListingListObject.class);
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
							ProductsListingListObject obj = new Gson()
									.fromJson(data,
											ProductsListingListObject.class);
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

	void setAdapterData(ProductsListingListObject obj) {
		nextPage = obj.getNext_page();
		if (nextPage == null)
			isMoreAllowed = false;
		else
			isMoreAllowed = true;

		if (adapter == null && obj.getProducts().size() == 0) {
			// showEmptyListView("No Seens on this post yet", false);
		}

		if (adapter == null) {
			hideErrorLayout();
			hideLoadingLayout();

			adapter = new ProductListingListAdapter(this, obj.getProducts(),
					isMoreAllowed);
			recyclerView.setAdapter(adapter);
		} else {
			adapter.addData(obj.getProducts(), isMoreAllowed);
		}
	}
}

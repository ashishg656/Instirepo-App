package com.instirepo.app.fragments;

import com.instirepo.app.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProductsCategoriesFragment extends BaseFragment {

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

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}

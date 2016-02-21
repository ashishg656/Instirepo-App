package com.instirepo.app.fragments;

import com.instirepo.app.R;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SellProductFragmentSelectCategory extends BaseFragment {

	RecyclerView recyclerView;
	GridLayoutManager layoutManager;

	public static SellProductFragmentSelectCategory newInstance(Bundle b) {
		SellProductFragmentSelectCategory frg = new SellProductFragmentSelectCategory();
		frg.setArguments(b);
		return frg;
	}

	public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.sell_product_fragment_select_category, container, false);

		setProgressLayoutVariablesAndErrorVariables(v);
		recyclerView = (RecyclerView) v.findViewById(R.id.postsbyreachersrecyclef);

		return v;
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		layoutManager = new GridLayoutManager(getActivity(), 2);
		recyclerView.setLayoutManager(layoutManager);
	}
}

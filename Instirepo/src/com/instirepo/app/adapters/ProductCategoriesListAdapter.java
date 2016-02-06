package com.instirepo.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import com.instirepo.app.objects.ProductCategoriesListObject;

public class ProductCategoriesListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {

	Context context;
	ProductCategoriesListObject mData;

	@Override
	public int getItemCount() {
		return 2;
	}

	@Override
	public void onBindViewHolder(ViewHolder arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}

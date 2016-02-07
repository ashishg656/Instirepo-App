package com.instirepo.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import com.instirepo.app.objects.ProductCategoriesListObject;

public class ProductCategoriesListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {

	Context context;
	ProductCategoriesListObject mData;

	@Override
	public int getItemCount() {
		return 3;
	}

	@Override
	public void onBindViewHolder(ViewHolder arg0, int arg1) {

	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		return null;
	}

	class TrendingProductsHolder extends RecyclerView.ViewHolder {

		public TrendingProductsHolder(View v) {
			super(v);
		}
	}

	class RecentProductsHolder extends RecyclerView.ViewHolder {

		public RecentProductsHolder(View v) {
			super(v);
		}
	}

	class CategoriesHolder extends RecyclerView.ViewHolder {

		public CategoriesHolder(View v) {
			super(v);
		}
	}

}

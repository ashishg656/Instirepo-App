package com.instirepo.app.adapters;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.instirepo.app.R;
import com.instirepo.app.objects.ProductCategoriesListObject;
import com.instirepo.app.objects.ProductObjectSingle;
import com.instirepo.app.widgets.RoundedImageView;

public class ProductCategoriesListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {

	Context context;
	ProductCategoriesListObject mData;

	int ITEM_CATEGORY = 0;
	int ITEM_RECENTLY_VIEWED = 1;
	int ITEM_TRENDING = 2;

	public ProductCategoriesListAdapter(ProductCategoriesListObject obj,
			Context activity) {
		this.context = activity;
		this.mData = obj;
	}

	@Override
	public int getItemCount() {
		if (mData.getRecently_viewed().size() == 0) {
			return mData.getCategories().size() + 1;
		} else {
			return mData.getCategories().size() + 2;
		}
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return ITEM_TRENDING;
		} else if (position == mData.getCategories().size() + 1) {
			return ITEM_RECENTLY_VIEWED;
		} else
			return ITEM_CATEGORY;
	}

	@Override
	public void onBindViewHolder(ViewHolder holderCOm, int pos) {
		if (getItemViewType(pos) == ITEM_CATEGORY) {

		} else if (getItemViewType(pos) == ITEM_RECENTLY_VIEWED) {

		} else if (getItemViewType(pos) == ITEM_TRENDING) {

		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
		if (type == ITEM_CATEGORY) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.product_categories_list_item_category, parent,
					false);
			CategoriesHolder holder = new CategoriesHolder(v);
			return holder;
		} else if (type == ITEM_RECENTLY_VIEWED || type == ITEM_TRENDING) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.product_categories_list_trending_products, parent,
					false);
			TrendingProductsHolder holder = new TrendingProductsHolder(v);
			return holder;
		}
		return null;
	}

	class TrendingProductsHolder extends RecyclerView.ViewHolder {

		public TrendingProductsHolder(View v) {
			super(v);
		}
	}

	class CategoriesHolder extends RecyclerView.ViewHolder {

		RoundedImageView image;
		TextView name;

		public CategoriesHolder(View v) {
			super(v);
			image = (RoundedImageView) v.findViewById(R.id.categoryimage);
			name = (TextView) v.findViewById(R.id.categoryname);
		}
	}

	class TrendingProductsListAdapter extends
			RecyclerView.Adapter<RecyclerView.ViewHolder> {

		List<ProductObjectSingle> mData;

		public TrendingProductsListAdapter(List<ProductObjectSingle> data) {
			this.mData = data;
		}

		@Override
		public int getItemCount() {
			return mData.size();
		}

		@Override
		public void onBindViewHolder(ViewHolder arg0, int arg1) {

		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
			View v = LayoutInflater
					.from(context)
					.inflate(
							R.layout.product_categories_list_trending_products_list_item,
							arg0, false);
			ProductHolder holder = new ProductHolder(v);
			return holder;
		}

		class ProductHolder extends RecyclerView.ViewHolder {

			public ProductHolder(View v) {
				super(v);
			}
		}
	}
}

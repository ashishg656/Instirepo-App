package com.instirepo.app.adapters;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.instirepo.app.R;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.objects.ProductCategoriesListObject;
import com.instirepo.app.objects.ProductObjectSingle;
import com.instirepo.app.serverApi.ImageRequestManager;
import com.instirepo.app.widgets.RoundedImageView;

public class ProductCategoriesListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {

	Context context;
	ProductCategoriesListObject mData;

	public static int ITEM_CATEGORY = 0;
	public static int ITEM_RECENTLY_VIEWED = 1;
	public static int ITEM_TRENDING = 2;

	int widthOfProduct, heightOfCategory;

	public ProductCategoriesListAdapter(ProductCategoriesListObject obj,
			Context activity) {
		this.context = activity;
		this.mData = obj;
		heightOfCategory = context.getResources().getDisplayMetrics().widthPixels
				/ 2
				- context.getResources().getDimensionPixelSize(
						R.dimen.z_margin_small);
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
			pos = pos - 1;
			CategoriesHolder holder = (CategoriesHolder) holderCOm;
			ImageRequestManager.get(context).requestImage(
					context,
					holder.image,
					ZApplication.getImageUrl(mData.getCategories().get(pos)
							.getImage()), 0);
			holder.name.setText(mData.getCategories().get(pos).getName());

			GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) holder.container
					.getLayoutParams();
			params.height = heightOfCategory;
			holder.container.setLayoutParams(params);
		} else if (getItemViewType(pos) == ITEM_RECENTLY_VIEWED) {
			TrendingProductsHolder holder = (TrendingProductsHolder) holderCOm;
			holder.recyclerView.setLayoutManager(new LinearLayoutManager(
					context, LinearLayoutManager.HORIZONTAL, false));
			holder.recyclerView.setAdapter(new TrendingProductsListAdapter(
					mData.getRecently_viewed()));

			holder.heading.setText("Recently Viewed Products");

			holder.shopByCategoriesText.setVisibility(View.GONE);
		} else if (getItemViewType(pos) == ITEM_TRENDING) {
			TrendingProductsHolder holder = (TrendingProductsHolder) holderCOm;
			holder.recyclerView.setLayoutManager(new LinearLayoutManager(
					context, LinearLayoutManager.HORIZONTAL, false));
			holder.recyclerView.setAdapter(new TrendingProductsListAdapter(
					mData.getTrending_products()));

			holder.heading.setText("Trending Products");

			holder.shopByCategoriesText.setVisibility(View.VISIBLE);
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

		RecyclerView recyclerView;
		LinearLayout viewMore;
		TextView heading, shopByCategoriesText;

		public TrendingProductsHolder(View v) {
			super(v);
			recyclerView = (RecyclerView) v
					.findViewById(R.id.postsbyreachersrecyclef);
			viewMore = (LinearLayout) v.findViewById(R.id.viewmorelayout);
			heading = (TextView) v.findViewById(R.id.viewmorehading);
			shopByCategoriesText = (TextView) v
					.findViewById(R.id.shopbyCategories);
		}
	}

	class CategoriesHolder extends RecyclerView.ViewHolder {

		RoundedImageView image;
		TextView name;
		FrameLayout container;

		public CategoriesHolder(View v) {
			super(v);
			image = (RoundedImageView) v.findViewById(R.id.categoryimage);
			name = (TextView) v.findViewById(R.id.categoryname);
			container = (FrameLayout) v.findViewById(R.id.containergridselect);
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
		public void onBindViewHolder(ViewHolder holderCOm, int pos) {
			ProductHolder holder = (ProductHolder) holderCOm;
			ImageRequestManager.get(context).requestImage(context,
					holder.image,
					ZApplication.getImageUrl(mData.get(pos).getImage()), -1);
			holder.name.setText(mData.get(pos).getName());
			holder.price.setText("₹ " + mData.get(pos).getPrice());
			holder.mrp.setText("₹ " + mData.get(pos).getMrp());
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

			TextView name, mrp, price;
			RoundedImageView image;

			public ProductHolder(View v) {
				super(v);
				name = (TextView) v.findViewById(R.id.productname);
				mrp = (TextView) v.findViewById(R.id.productmrp);
				price = (TextView) v.findViewById(R.id.productprice);
				image = (RoundedImageView) v.findViewById(R.id.productimage);
			}
		}
	}
}

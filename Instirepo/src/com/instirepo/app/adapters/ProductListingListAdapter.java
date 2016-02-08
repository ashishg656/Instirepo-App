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
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.objects.ProductObjectSingle;
import com.instirepo.app.widgets.CircularImageView;

public class ProductListingListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

	Context context;
	boolean isMoreAllowed;
	List<ProductObjectSingle> mData;

	public ProductListingListAdapter(Context context,
			List<ProductObjectSingle> mData, boolean isMoreAllowed) {
		super();
		this.context = context;
		this.mData = mData;
		this.isMoreAllowed = isMoreAllowed;
	}

	public void addData(List<ProductObjectSingle> objs, boolean isMoreAllowed2) {
		mData.addAll(objs);
		this.isMoreAllowed = isMoreAllowed2;
		notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position) {
		if (position > mData.size() - 1)
			return Z_RECYCLER_VIEW_ITEM_LOADING;
		return Z_RECYCLER_VIEW_ITEM_NORMAL;
	}

	@Override
	public int getItemCount() {
		if (isMoreAllowed)
			return mData.size() + 1;
		return mData.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holderCommom, int pos) {
		pos = holderCommom.getAdapterPosition();
		if (getItemViewType(pos) == Z_RECYCLER_VIEW_ITEM_NORMAL) {
			ProductHolder holder = (ProductHolder) holderCommom;

		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int type) {
		if (type == Z_RECYCLER_VIEW_ITEM_NORMAL) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.seen_by_people_list_item_layout, arg0, false);
			ProductHolder holder = new ProductHolder(v);
			return holder;
		} else if (type == Z_RECYCLER_VIEW_ITEM_LOADING) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.loading_more, arg0, false);
			PostHolderLoading holder = new PostHolderLoading(v);
			return holder;
		}
		return null;
	}

	class PostHolderLoading extends RecyclerView.ViewHolder {

		public PostHolderLoading(View v) {
			super(v);
		}
	}

	class ProductHolder extends RecyclerView.ViewHolder {
		TextView name, time;
		CircularImageView image;

		public ProductHolder(View v) {
			super(v);
			name = (TextView) v.findViewById(R.id.seenbyname);
			image = (CircularImageView) v.findViewById(R.id.seenbyimage);
			time = (TextView) v.findViewById(R.id.seenbytime);
		}
	}

}

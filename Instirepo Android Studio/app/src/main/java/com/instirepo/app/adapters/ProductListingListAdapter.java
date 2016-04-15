package com.instirepo.app.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.instirepo.app.R;
import com.instirepo.app.activities.BaseActivity;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.objects.ProductObjectSingle;
import com.instirepo.app.serverApi.ImageRequestManager;
import com.instirepo.app.widgets.RoundedImageView;

public class ProductListingListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

	Context context;
	boolean isMoreAllowed;
	List<ProductObjectSingle> mData;
	MyClickListener clickListener;

	public ProductListingListAdapter(Context context,
			List<ProductObjectSingle> mData, boolean isMoreAllowed) {
		super();
		this.context = context;
		this.mData = mData;
		this.isMoreAllowed = isMoreAllowed;
		clickListener = new MyClickListener();
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
			ImageRequestManager.get(context).requestImage(context,
					holder.image,
					ZApplication.getImageUrl(mData.get(pos).getImage()), -1);
			holder.name.setText(mData.get(pos).getName());
			holder.price.setText("₹ " + mData.get(pos).getPrice());
			holder.mrp.setText("₹ " + mData.get(pos).getMrp());

			holder.mrp.setPaintFlags(holder.mrp.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);

			holder.containerLayout.setTag(R.integer.z_select_post_tag_position,
					pos);
			holder.containerLayout.setOnClickListener(clickListener);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int type) {
		if (type == Z_RECYCLER_VIEW_ITEM_NORMAL) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.products_listing_activity_list_item, arg0, false);
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

		TextView name, mrp, price;
		RoundedImageView image;
		FrameLayout containerLayout;

		public ProductHolder(View v) {
			super(v);
			name = (TextView) v.findViewById(R.id.productname);
			mrp = (TextView) v.findViewById(R.id.productmrp);
			price = (TextView) v.findViewById(R.id.productprice);
			image = (RoundedImageView) v.findViewById(R.id.productimage);
			containerLayout = (FrameLayout) v
					.findViewById(R.id.productcontainerlayout);
		}
	}

	class MyClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.productcontainerlayout:
				int pos = (int) v.getTag(R.integer.z_select_post_tag_position);
				((BaseActivity) context).openProductDetailActivity(mData.get(
						pos).getId());
				break;

			default:
				break;
			}
		}
	}
}

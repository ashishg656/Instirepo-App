package com.instirepo.app.adapters;

import java.util.List;

import com.instirepo.app.R;
import com.instirepo.app.activities.SellProductActivity;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.objects.AllPostCategoriesObject.PostCategorySingle;
import com.instirepo.app.serverApi.ImageRequestManager;
import com.instirepo.app.widgets.RoundedImageView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class SellProductSelectCategoryFragmentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	List<PostCategorySingle> mData;
	Context context;
	MyClickListener clickListener;

	public SellProductSelectCategoryFragmentListAdapter(List<PostCategorySingle> mData, Context context) {
		super();
		this.mData = mData;
		this.context = context;
		clickListener = new MyClickListener();
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holderc, int pos) {
		CategoryHolder holder = (CategoryHolder) holderc;

		holder.name.setText(mData.get(pos).getName());
		ImageRequestManager.get(context).requestImage(context, holder.image,
				ZApplication.getImageUrl(mData.get(pos).getImage()), -1);

		holder.container.setTag(pos);
		holder.container.setOnClickListener(clickListener);
	}

	class CategoryHolder extends RecyclerView.ViewHolder {

		RoundedImageView image;
		TextView name;
		FrameLayout container;

		public CategoryHolder(View v) {
			super(v);
			image = (RoundedImageView) v.findViewById(R.id.image);
			name = (TextView) v.findViewById(R.id.name);
			container = (FrameLayout) v.findViewById(R.id.categorycontainer);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
		View v = LayoutInflater.from(context).inflate(R.layout.sell_product_select_category_list_item_layout, parent,
				false);
		CategoryHolder holder = new CategoryHolder(v);
		return holder;
	}

	class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.categorycontainer) {
				int pos = (int) v.getTag();
				((SellProductActivity) context).switchToSecondFragment(mData.get(pos).getId());
			}
		}
	}
}

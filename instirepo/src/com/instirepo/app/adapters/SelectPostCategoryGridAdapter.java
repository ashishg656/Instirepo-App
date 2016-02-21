package com.instirepo.app.adapters;

import java.util.List;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.instirepo.app.R;
import com.instirepo.app.activities.CreatePostActivity;
import com.instirepo.app.activities.HomeActivity;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.objects.AllPostCategoriesObject;
import com.instirepo.app.serverApi.ImageRequestManager;
import com.instirepo.app.widgets.RoundedImageView;

public class SelectPostCategoryGridAdapter extends BaseAdapter implements
		AppConstants {

	List<AllPostCategoriesObject.PostCategorySingle> mData;
	Context context;
	int imageHeight;
	MyClickListener clickListener;

	public SelectPostCategoryGridAdapter(
			List<AllPostCategoriesObject.PostCategorySingle> mData,
			Context context) {
		super();
		this.mData = mData;
		this.context = context;
		imageHeight = context.getResources().getDisplayMetrics().widthPixels
				/ 2
				- context.getResources().getDimensionPixelSize(
						R.dimen.select_post_category_grid_image_margin);
		clickListener = new MyClickListener();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int pos) {
		return mData.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		PostCategoryHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.select_post_category_grid_item_layout, parent,
					false);
			holder = new PostCategoryHolder(convertView);
			convertView.setTag(R.integer.z_select_post_tag_holder, holder);
		} else
			holder = (PostCategoryHolder) convertView
					.getTag(R.integer.z_select_post_tag_holder);

		AbsListView.LayoutParams params = (AbsListView.LayoutParams) holder.containerLayout
				.getLayoutParams();
		params.height = imageHeight;
		holder.containerLayout.setLayoutParams(params);

		holder.containerLayout
				.setTag(R.integer.z_select_post_tag_position, pos);
		holder.containerLayout.setOnClickListener(clickListener);

		holder.name.setText(mData.get(pos).getName());
		ImageRequestManager.get(context).requestImage(context, holder.image,
				ZApplication.getImageUrl(mData.get(pos).getImage()), -1);

		return convertView;
	}

	class PostCategoryHolder {

		FrameLayout containerLayout;
		RoundedImageView image;
		TextView name;

		public PostCategoryHolder(View v) {
			containerLayout = (FrameLayout) v
					.findViewById(R.id.containergridselect);
			image = (RoundedImageView) v.findViewById(R.id.categoryimage);
			name = (TextView) v.findViewById(R.id.categoryname);
		}
	}

	class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.containergridselect:
				int pos = (int) v.getTag(R.integer.z_select_post_tag_position);
				Intent i = new Intent(context, CreatePostActivity.class);
				int location[] = new int[2];
				v.getLocationInWindow(location);
				i.putExtra("touchx", location[0] + v.getWidth() / 2);
				i.putExtra("touchy", location[1] + v.getHeight() / 2);
				i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				((HomeActivity) context).overridePendingTransition(0, 0);

				i.putExtra("catname", mData.get(pos).getName());
				i.putExtra("cattype", mData.get(pos).getType());
				i.putExtra("catid", mData.get(pos).getId());

				context.startActivity(i);
				break;

			default:
				break;
			}
		}
	}

}

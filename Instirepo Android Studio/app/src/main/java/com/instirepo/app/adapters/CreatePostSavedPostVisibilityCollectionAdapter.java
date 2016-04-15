package com.instirepo.app.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.instirepo.app.R;
import com.instirepo.app.activities.CreatePostActivity;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.objects.SavedPostVisibilityCollectionObject;

public class CreatePostSavedPostVisibilityCollectionAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

	Context context;
	SavedPostVisibilityCollectionObject mData;
	ArrayList<Integer> colors;
	MyClickListener clickListener;
	int imageHeight;

	public CreatePostSavedPostVisibilityCollectionAdapter(Context context,
			SavedPostVisibilityCollectionObject mData) {
		super();
		this.context = context;
		this.mData = mData;
		colors = new ArrayList<>();
		clickListener = new MyClickListener();
		if (context == null)
			return;
		colors.add(context.getResources().getColor(
				R.color.z_green_color_primary));
		colors.add(context.getResources().getColor(R.color.z_red_color_primary));
		colors.add(context.getResources().getColor(R.color.PrimaryDarkColor));
		colors.add(context.getResources().getColor(R.color.purple_post));
		colors.add(context.getResources().getColor(R.color.orange_post));
		colors.add(context.getResources().getColor(
				R.color.home_viewpager_color_1));
		imageHeight = context.getResources().getDisplayMetrics().widthPixels
				/ 3
				- 2
				* context.getResources().getDimensionPixelSize(
						R.dimen.z_margin_large);
	}

	@Override
	public int getItemCount() {
		return mData.getVisibilities().size() + 1;
	}

	@Override
	public void onBindViewHolder(ViewHolder holdercc, int pos) {
		pos = holdercc.getAdapterPosition();
		SavdPostVisibilityHolder holder = (SavdPostVisibilityHolder) holdercc;
		if (pos == 0) {
			holder.name.setText("Create New");
			holder.image.setImageResource(R.drawable.fab_add);
		} else {
			holder.name.setText(mData.getVisibilities().get(pos - 1).getName());
			holder.image.setImageResource(R.drawable.ic_people);
		}

		GradientDrawable gd = (GradientDrawable) holder.image.getBackground();
		int x = pos % colors.size();
		Log.w("aS", "x = " + x);
		gd.setColor(colors.get(x));

		holder.mainLayout.setTag(holder);
		holder.mainLayout.setOnClickListener(clickListener);

		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.image
				.getLayoutParams();
		params.height = imageHeight;
		holder.image.setLayoutParams(params);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View v = LayoutInflater
				.from(context)
				.inflate(
						R.layout.create_post_select_post_visibility_collection_saved_list_item,
						arg0, false);
		SavdPostVisibilityHolder holder = new SavdPostVisibilityHolder(v);
		return holder;
	}

	class SavdPostVisibilityHolder extends RecyclerView.ViewHolder {

		ImageView image;
		TextView name;
		LinearLayout mainLayout;

		public SavdPostVisibilityHolder(View v) {
			super(v);
			image = (ImageView) v.findViewById(R.id.batchimage);
			name = (TextView) v.findViewById(R.id.savename);
			mainLayout = (LinearLayout) v.findViewById(R.id.mainlayoutsavevis);
		}
	}

	class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.mainlayoutsavevis) {
				SavdPostVisibilityHolder holder = (SavdPostVisibilityHolder) v
						.getTag();
				int pos = holder.getAdapterPosition();
				if (pos == 0) {
					((CreatePostActivity) context)
							.clickOnCreateNewCollectionButtonFromAllCollectionsFragment();
				} else {
					((CreatePostActivity) context)
							.showDialogConfirmationBeforeSendingPost(
									Z_CREATE_POST_SAVED_COLLECTION, mData
											.getVisibilities().get(pos + 1)
											.getId());
				}
			}
		}
	}

}

package com.instirepo.app.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.instirepo.app.R;
import com.instirepo.app.activities.HomeActivity;
import com.instirepo.app.bottomsheet.BottomSheet;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.objects.PostsListObject;

public class PostsByTeachersListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

	Context context;
	PostsListObject mData;
	MyClickListener clickListener;

	public PostsByTeachersListAdapter(Context context, PostsListObject mData) {
		super();
		this.context = context;
		this.mData = mData;
		clickListener = new MyClickListener();
	}

	@Override
	public int getItemCount() {
		return mData.getPosts().size();
	}

	@Override
	public int getItemViewType(int position) {
		return Z_RECYCLER_VIEW_ITEM_NORMAL;
	}

	@Override
	public void onBindViewHolder(ViewHolder holderCom, int pos) {
		pos = holderCom.getAdapterPosition();
		if (getItemViewType(pos) == Z_RECYCLER_VIEW_ITEM_NORMAL) {
			PostsHolderNormal holder = (PostsHolderNormal) holderCom;
			holder.overflowIcon.setOnClickListener(clickListener);

			holder.seenByContainerLayout.setOnClickListener(clickListener);

			holder.commentsLayout.setOnClickListener(clickListener);

			holder.openUserProfile.setOnClickListener(clickListener);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int type) {
		if (type == Z_RECYCLER_VIEW_ITEM_NORMAL) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.post_by_teacher_list_item_layout, arg0, false);
			PostsHolderNormal holder = new PostsHolderNormal(v);
			return holder;
		}
		return null;
	}

	class PostsHolderNormal extends RecyclerView.ViewHolder {

		LinearLayout overflowIcon;
		FrameLayout seenByContainerLayout, openUserProfile;
		ImageView commentsLayout;

		public PostsHolderNormal(View v) {
			super(v);
			overflowIcon = (LinearLayout) v.findViewById(R.id.overflowiconpost);
			seenByContainerLayout = (FrameLayout) v
					.findViewById(R.id.seenbycontainer);
			commentsLayout = (ImageView) v
					.findViewById(R.id.commentsviewconatiner);
			openUserProfile = (FrameLayout) v
					.findViewById(R.id.openuserprofilepost);
		}
	}

	class MyClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.overflowiconpost:
				showOverflowIconContent("Post Heading", 0);
				break;
			case R.id.seenbycontainer:
				showSeenByPeople();
				break;
			case R.id.commentsviewconatiner:
				showCommentsFragment();
				break;
			case R.id.openuserprofilepost:
				openUserProfileFragment();
				break;
			default:
				break;
			}
		}

	}

	public void showOverflowIconContent(String message, int id) {
		new BottomSheet.Builder((HomeActivity) context).title(message)
				.sheet(R.menu.posts_sliding_panel_menu)
				.listener(new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {

						}
					}
				}).show();
	}

	public void showSeenByPeople() {
		((HomeActivity) context).switchToSeenByPeopleFragment();
	}

	public void showCommentsFragment() {
		((HomeActivity) context).switchToCommentsFragment();
	}

	void openUserProfileFragment() {
		((HomeActivity) context).switchToUserProfileViewedByOtherFragment();
	}
}
package com.instirepo.app.adapters;

import java.util.List;

import serverApi.ImageRequestManager;

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
import android.widget.TextView;

import com.instirepo.app.R;
import com.instirepo.app.activities.HomeActivity;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.bottomsheet.BottomSheet;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.objects.PostListSinglePostObject;
import com.instirepo.app.widgets.CircularImageView;
import com.instirepo.app.widgets.PEWImageView;

public class PostsByTeachersListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

	Context context;
	List<PostListSinglePostObject> mData;
	MyClickListener clickListener;
	boolean isMoreAllowed;

	public PostsByTeachersListAdapter(Context context,
			List<PostListSinglePostObject> mData, boolean isMoreAllowed) {
		super();
		this.context = context;
		this.mData = mData;
		this.isMoreAllowed = isMoreAllowed;
		clickListener = new MyClickListener();
	}

	public void addData(List<PostListSinglePostObject> data, boolean isMore) {
		mData.addAll(data);
		this.isMoreAllowed = isMore;
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		if (isMoreAllowed == true)
			return mData.size() + 1;
		return mData.size();
	}

	@Override
	public int getItemViewType(int position) {
		if (position > mData.size() - 1)
			return Z_RECYCLER_VIEW_ITEM_LOADING;
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

			PostListSinglePostObject obj = mData.get(pos);
			holder.userName.setText(obj.getUser_name());
			holder.heading.setText(obj.getHeading());
			holder.description.setText(obj.getDescription());
			if (obj.getImage() == null) {
				holder.imagePost.setVisibility(View.GONE);
			} else {
				holder.imagePost.setVisibility(View.VISIBLE);
				ImageRequestManager.get(context).requestImage(context,
						holder.imagePost,
						ZApplication.getImageUrl(obj.getImage()), -1);
			}
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int type) {
		if (type == Z_RECYCLER_VIEW_ITEM_NORMAL) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.post_by_teacher_list_item_layout, arg0, false);
			PostsHolderNormal holder = new PostsHolderNormal(v);
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

	class PostsHolderNormal extends RecyclerView.ViewHolder {

		LinearLayout overflowIcon;
		FrameLayout seenByContainerLayout, openUserProfile;
		ImageView commentsLayout;
		CircularImageView userImage;
		TextView userName, time, heading, description;
		PEWImageView imagePost;

		public PostsHolderNormal(View v) {
			super(v);
			overflowIcon = (LinearLayout) v.findViewById(R.id.overflowiconpost);
			seenByContainerLayout = (FrameLayout) v
					.findViewById(R.id.seenbycontainer);
			commentsLayout = (ImageView) v
					.findViewById(R.id.commentsviewconatiner);
			openUserProfile = (FrameLayout) v
					.findViewById(R.id.openuserprofilepost);
			userImage = (CircularImageView) v.findViewById(R.id.circularimage);
			userName = (TextView) v.findViewById(R.id.uploadrname);
			time = (TextView) v.findViewById(R.id.utime);
			heading = (TextView) v.findViewById(R.id.uptheading);
			description = (TextView) v.findViewById(R.id.uptdesc);
			imagePost = (PEWImageView) v.findViewById(R.id.pewimage);
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

package com.instirepo.app.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.instirepo.app.R;
import com.instirepo.app.activities.ZHomeActivity;
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

		public PostsHolderNormal(View v) {
			super(v);
			overflowIcon = (LinearLayout) v.findViewById(R.id.overflowiconpost);
		}
	}

	class MyClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.overflowiconpost:
				showOverflowIconContent("Post Heading", 0);
				break;

			default:
				break;
			}
		}

	}

	public void showOverflowIconContent(String message, int id) {
		new BottomSheet.Builder((ZHomeActivity) context).title(message)
				.sheet(R.menu.posts_sliding_panel_menu)
				.listener(new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						}
					}
				}).grid().show();
	}
}

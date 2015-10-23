package com.instirepo.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.instirepo.app.R;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.objects.PostsListObject;

public class PostsByStudentsListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

	Context context;
	PostsListObject mData;

	public PostsByStudentsListAdapter(Context context, PostsListObject mData) {
		super();
		this.context = context;
		this.mData = mData;
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

		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int type) {
		if (type == Z_RECYCLER_VIEW_ITEM_NORMAL) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.post_by_student_list_item_layout, arg0, false);
			PostsHolderNormal holder = new PostsHolderNormal(v);
			return holder;
		}
		return null;
	}

	class PostsHolderNormal extends RecyclerView.ViewHolder {

		public PostsHolderNormal(View v) {
			super(v);
		}

	}
}

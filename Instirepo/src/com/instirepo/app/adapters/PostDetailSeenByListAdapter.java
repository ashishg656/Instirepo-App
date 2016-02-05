package com.instirepo.app.adapters;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.instirepo.app.R;
import com.instirepo.app.objects.SeenByPeopleObject.PeopleSeenPost;
import com.instirepo.app.serverApi.ImageRequestManager;
import com.instirepo.app.widgets.CircularImageView;

public class PostDetailSeenByListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {

	List<PeopleSeenPost> mData;
	Context context;

	public PostDetailSeenByListAdapter(List<PeopleSeenPost> mData,
			Context context) {
		super();
		this.mData = mData;
		this.context = context;
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holderCom, int pos) {
		SeenByHolder holder = (SeenByHolder) holderCom;

		ImageRequestManager.get(context).requestImage(context, holder.image,
				mData.get(pos).getImage(), pos);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
		View v = LayoutInflater.from(context).inflate(
				R.layout.post_detail_seen_by_people_list_item_layout, parent,
				false);
		SeenByHolder holder = new SeenByHolder(v);
		return holder;
	}

	class SeenByHolder extends RecyclerView.ViewHolder {
		CircularImageView image;

		public SeenByHolder(View v) {
			super(v);
			image = (CircularImageView) v.findViewById(R.id.seenbyimage);
		}
	}

}

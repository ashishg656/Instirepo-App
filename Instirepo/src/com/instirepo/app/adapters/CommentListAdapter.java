package com.instirepo.app.adapters;

import java.util.List;

import serverApi.ImageRequestManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.instirepo.app.R;
import com.instirepo.app.extras.TimeUtils;
import com.instirepo.app.objects.CommentsListObject;
import com.instirepo.app.objects.CommentsListObject.CommentObject;
import com.instirepo.app.widgets.CircularImageView;

public class CommentListAdapter extends BaseAdapter {

	Context context;
	List<CommentsListObject.CommentObject> mData;

	public CommentListAdapter(Context context, List<CommentObject> mData) {
		super();
		this.context = context;
		this.mData = mData;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	public void addData(List<CommentsListObject.CommentObject> objs) {
		this.mData.addAll(objs);
	}

	public void addSingleComment(CommentObject obj) {
		this.mData.add(0, obj);
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommentHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.comments_list_item_layout, parent, false);
			holder = new CommentHolder(convertView);
			convertView.setTag(holder);
		} else
			holder = (CommentHolder) convertView.getTag();

		CommentObject obj = mData.get(position);

		holder.comment.setText(obj.getComment());
		holder.time.setText(TimeUtils.getPostTime(obj.getTime()));
		holder.userName.setText(obj.getUser_name());
		ImageRequestManager.get(context).requestImage(context,
				holder.userImage, obj.getUser_image(), -1);
		if (obj.isIs_different_color()) {
			holder.userName.setTextColor(context.getResources().getColor(
					R.color.z_green_color_primary));
		} else {
			holder.userName.setTextColor(context.getResources().getColor(
					R.color.z_text_color_dark));
		}

		return convertView;
	}

	class CommentHolder {

		CircularImageView userImage;
		TextView userName, comment, time;

		public CommentHolder(View v) {
			userImage = (CircularImageView) v.findViewById(R.id.circularimage);
			comment = (TextView) v.findViewById(R.id.comment);
			time = (TextView) v.findViewById(R.id.time);
			userName = (TextView) v.findViewById(R.id.uploadrname);
		}
	}

}

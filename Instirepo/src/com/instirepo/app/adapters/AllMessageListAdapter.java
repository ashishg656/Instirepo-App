package com.instirepo.app.adapters;

import java.util.List;

import serverApi.ImageRequestManager;

import com.instirepo.app.R;
import com.instirepo.app.extras.TimeUtils;
import com.instirepo.app.objects.AllMessagesListObject;
import com.instirepo.app.widgets.CircularImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AllMessageListAdapter extends BaseAdapter {

	List<AllMessagesListObject.SingleMessageListObj> mData;
	Context context;

	public AllMessageListAdapter(
			List<AllMessagesListObject.SingleMessageListObj> mData,
			Context context) {
		super();
		this.mData = mData;
		this.context = context;
	}

	@Override
	public int getCount() {
		return mData.size();
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
		MessageListHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.all_messages_list_list_item_layout, parent, false);
			holder = new MessageListHolder(convertView);
			convertView.setTag(holder);
		} else
			holder = (MessageListHolder) convertView.getTag();

		AllMessagesListObject.SingleMessageListObj obj = mData.get(position);

		holder.lastMessage.setText(obj.getLastmessage());
		holder.time.setText(TimeUtils.getPostTime(obj.getTime()));
		holder.userName.setText(obj.getName());
		ImageRequestManager.get(context).requestImage(context,
				holder.userImage, obj.getImage(), -1);

		return convertView;
	}

	class MessageListHolder {

		CircularImageView userImage;
		TextView userName, lastMessage, time;

		public MessageListHolder(View v) {
			userImage = (CircularImageView) v.findViewById(R.id.circularimage);
			lastMessage = (TextView) v.findViewById(R.id.comment);
			time = (TextView) v.findViewById(R.id.time);
			userName = (TextView) v.findViewById(R.id.uploadrname);
		}
	}

}

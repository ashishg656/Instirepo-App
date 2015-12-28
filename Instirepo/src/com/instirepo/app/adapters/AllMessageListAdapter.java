package com.instirepo.app.adapters;

import java.util.List;

import serverApi.ImageRequestManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.instirepo.app.R;
import com.instirepo.app.activities.AllMessagesActivity;
import com.instirepo.app.activities.MessageListActivity;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.TimeUtils;
import com.instirepo.app.objects.AllMessagesListObject;
import com.instirepo.app.objects.AllMessagesListObject.SingleMessageListObj;
import com.instirepo.app.widgets.CircularImageView;

public class AllMessageListAdapter extends BaseAdapter {

	List<AllMessagesListObject.SingleMessageListObj> mData;
	Context context;
	MyClickListener clickListener;

	public AllMessageListAdapter(
			List<AllMessagesListObject.SingleMessageListObj> mData,
			Context context) {
		super();
		this.mData = mData;
		this.context = context;
		clickListener = new MyClickListener();
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
		holder.containerLayout.setTag(R.integer.z_select_post_tag_position,
				position);
		holder.containerLayout.setOnClickListener(clickListener);

		return convertView;
	}

	class MessageListHolder {

		CircularImageView userImage;
		TextView userName, lastMessage, time;
		FrameLayout containerLayout;

		public MessageListHolder(View v) {
			userImage = (CircularImageView) v.findViewById(R.id.circularimage);
			lastMessage = (TextView) v.findViewById(R.id.comment);
			time = (TextView) v.findViewById(R.id.time);
			userName = (TextView) v.findViewById(R.id.uploadrname);
			containerLayout = (FrameLayout) v
					.findViewById(R.id.messagelistriplebg);
		}
	}

	class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int pos = (int) v.getTag(R.integer.z_select_post_tag_position);
			Intent i = new Intent(context, MessageListActivity.class);
			Log.w("as", mData.get(pos).getPersonid() + " id");
			i.putExtra("person_id", mData.get(pos).getPersonid());
			i.putExtra("person_name", mData.get(pos).getName());
			i.putExtra("person_image", mData.get(pos).getImage());
			((AllMessagesActivity) context).startActivityForResult(i,
					AppConstants.Z_REQUEST_CODE_MESSAGES_ACTIVITY);
		}
	}

	public void updateEntryAfterBackPress(String personID, String message,
			String time) {
		int id = Integer.parseInt(personID);
		for (SingleMessageListObj msg : mData) {
			if (msg.getPersonid() == id) {
				if (!msg.getLastmessage().equals(message)) {
					msg.setLastmessage(message);
					msg.setTime(time);
					notifyDataSetChanged();
				}
				break;
			}
		}
	}

}

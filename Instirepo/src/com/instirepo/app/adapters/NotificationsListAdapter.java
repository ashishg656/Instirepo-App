package com.instirepo.app.adapters;

import java.util.List;


import com.instirepo.app.R;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.TimeUtils;
import com.instirepo.app.objects.NotificationsListObject.NotificationsSingleObj;
import com.instirepo.app.serverApi.ImageRequestManager;
import com.instirepo.app.widgets.RoundedImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationsListAdapter extends BaseAdapter {

	List<NotificationsSingleObj> mData;
	Context context;

	public NotificationsListAdapter(List<NotificationsSingleObj> mData,
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
		NotificationHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.notifications_list_item_with_image, parent, false);
			holder = new NotificationHolder(convertView);
			convertView.setTag(holder);
		} else
			holder = (NotificationHolder) convertView.getTag();

		NotificationsSingleObj obj = mData.get(position);
		if (obj.isGeneral_notification() || obj.isIs_author_of_post()) {
			holder.leftImage.setVisibility(View.GONE);
			holder.topImage.setVisibility(View.VISIBLE);

			if (obj.getImage() != null) {
				ImageRequestManager.get(context).requestImage(context,
						holder.topImage,
						ZApplication.getImageUrl(obj.getImage()), -1);
			} else if (obj.getImage_url() != null) {
				ImageRequestManager.get(context).requestImage(context,
						holder.topImage, obj.getImage_url(), -1);
			} else if (obj.getPost_image() != null) {
				ImageRequestManager.get(context).requestImage(context,
						holder.topImage,
						ZApplication.getImageUrl(obj.getPost_image()), -1);
			} else {
				holder.topImage.setVisibility(View.GONE);
			}
		} else {
			holder.leftImage.setVisibility(View.VISIBLE);
			holder.topImage.setVisibility(View.GONE);

			if (obj.getPost_image() != null) {
				ImageRequestManager.get(context).requestImage(context,
						holder.leftImage,
						ZApplication.getImageUrl(obj.getPost_image()), -1);
			} else {
				holder.leftImage.setVisibility(View.GONE);
			}
		}

		if (obj.isGeneral_notification()) {
			holder.text.setText(obj.getText());
		} else if (obj.isIs_author_of_post()) {
			holder.text.setText("New comments received on your post, '"
					+ obj.getPost_name() + "'");
		} else {
			holder.text.setText("The post you are following '"
					+ obj.getPost_name() + "', authored by '"
					+ obj.getUploader_name() + "', has new comments");
		}
		
		holder.time.setText(TimeUtils.getPostTime(obj.getTime()));

		return convertView;
	}

	class NotificationHolder {

		RoundedImageView topImage;
		ImageView leftImage;
		TextView text,time;

		public NotificationHolder(View v) {
			topImage = (RoundedImageView) v.findViewById(R.id.notiftopimage);
			leftImage = (ImageView) v.findViewById(R.id.notifsideimage);
			text = (TextView) v.findViewById(R.id.notiftext);
			time = (TextView) v.findViewById(R.id.notiftexttime);
		}

	}

}

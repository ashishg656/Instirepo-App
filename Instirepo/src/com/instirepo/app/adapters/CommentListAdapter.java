package com.instirepo.app.adapters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import serverApi.ImageRequestManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.instirepo.app.R;
import com.instirepo.app.activities.BaseActivity;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.TimeUtils;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.fragments.UserProfileViewedByOtherFragment;
import com.instirepo.app.objects.CommentsListObject;
import com.instirepo.app.objects.CommentsListObject.CommentObject;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.widgets.CircularImageView;

public class CommentListAdapter extends BaseAdapter implements ZUrls,
		AppConstants {

	Context context;
	List<CommentsListObject.CommentObject> mData;
	MyClickListener clickListener;

	int flagCommentPosition;
	CommentHolder flagCommentHolder;

	public CommentListAdapter(Context context, List<CommentObject> mData) {
		super();
		this.context = context;
		this.mData = mData;
		clickListener = new MyClickListener();
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

		holder.commentFlagLayout.setTag(R.integer.z_select_post_tag_holder,
				holder);
		holder.commentFlagLayout.setTag(R.integer.z_select_post_tag_position,
				position);
		holder.commentFlagLayout.setOnClickListener(clickListener);

		holder.containerLayout.setTag(position);
		holder.containerLayout.setOnClickListener(clickListener);

		if (obj.isIs_flagged()) {
			holder.commentFlagImage.setSelected(true);
		} else {
			holder.commentFlagImage.setSelected(false);
		}

		return convertView;
	}

	class CommentHolder {

		CircularImageView userImage;
		TextView userName, comment, time;
		LinearLayout commentFlagLayout;
		ImageView commentFlagImage;
		FrameLayout containerLayout;

		public CommentHolder(View v) {
			userImage = (CircularImageView) v.findViewById(R.id.circularimage);
			comment = (TextView) v.findViewById(R.id.comment);
			time = (TextView) v.findViewById(R.id.time);
			userName = (TextView) v.findViewById(R.id.uploadrname);
			commentFlagImage = (ImageView) v.findViewById(R.id.commentflgimage);
			commentFlagLayout = (LinearLayout) v
					.findViewById(R.id.commentflaglayou);
			containerLayout = (FrameLayout) v
					.findViewById(R.id.commentlayouttoopenuserprofile);
		}
	}

	class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.commentflaglayou:
				flagCommentHolder = (CommentHolder) v
						.getTag(R.integer.z_select_post_tag_holder);
				flagCommentPosition = (int) v
						.getTag(R.integer.z_select_post_tag_position);
				mData.get(flagCommentPosition).setIs_flagged(
						!mData.get(flagCommentPosition).isIs_flagged());

				if (flagCommentHolder != null) {
					if (mData.get(flagCommentPosition).isIs_flagged())
						flagCommentHolder.commentFlagImage.setSelected(true);
					else
						flagCommentHolder.commentFlagImage.setSelected(false);
				}

				sendFlagCommentRequestToServer();
				break;
			case R.id.commentlayouttoopenuserprofile:
				int pos = (int) v.getTag();
				openUserProfileViewedByOtherFragment(pos);
				break;
			default:
				break;
			}
		}
	}

	public void sendFlagCommentRequestToServer() {
		StringRequest req = new StringRequest(Method.POST, flagCommentOnPost,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						errorResponseForFlaggingAComment();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("user_id", ZPreferences.getUserProfileID(context));
				p.put("comment_id", mData.get(flagCommentPosition).getId() + "");
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, flagCommentOnPost);
	}

	public void openUserProfileViewedByOtherFragment(int pos) {
		Bundle bundle = new Bundle();
		bundle.putString("name", mData.get(pos).getUser_name());
		bundle.putInt("userid", mData.get(pos).getUser_id());
		bundle.putString("image", mData.get(pos).getUser_image());

		((BaseActivity) context)
				.getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.fragmentcontainer,
						UserProfileViewedByOtherFragment.newInstance(bundle),
						Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG_FROM_COMMENT_LIST_ADAPTER)
				.addToBackStack(
						Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG_FROM_COMMENT_LIST_ADAPTER)
				.commit();
	}

	protected void errorResponseForFlaggingAComment() {
		((BaseActivity) context)
				.makeToast("Error in flagging post. Check internet");

		mData.get(flagCommentPosition).setIs_flagged(
				!mData.get(flagCommentPosition).isIs_flagged());

		if (flagCommentHolder != null) {
			if (mData.get(flagCommentPosition).isIs_flagged())
				flagCommentHolder.commentFlagImage.setSelected(true);
			else
				flagCommentHolder.commentFlagImage.setSelected(false);
		}
	}

}

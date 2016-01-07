package com.instirepo.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.instirepo.app.R;
import com.instirepo.app.activities.UserProfileActivity;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.UserProfileEditProfileObject;

public class UserProfileEditProfileListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants,
		ZUrls {

	Context context;
	UserProfileEditProfileObject mData;
	MyClickListener clickListener;

	public UserProfileEditProfileListAdapter(Context context,
			UserProfileEditProfileObject mData) {
		super();
		this.context = context;
		this.mData = mData;
		clickListener = new MyClickListener();
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return Z_USER_PROFILE_ITEM_HEADER;
		} else {
			return Z_RECYCLER_VIEW_ITEM_NORMAL;
		}
	}

	@Override
	public int getItemCount() {
		return 2;
	}

	@Override
	public void onBindViewHolder(ViewHolder holderCom, int pos) {
		pos = holderCom.getAdapterPosition();
		if (getItemViewType(pos) == Z_USER_PROFILE_ITEM_HEADER) {
			FakeHeaderHolder holder = (FakeHeaderHolder) holderCom;
			ViewGroup.LayoutParams params = holder.header.getLayoutParams();
			params.height = ((UserProfileActivity) context).headerHeight;
			holder.header.setLayoutParams(params);
		} else {
			EditProfileHolder holder = (EditProfileHolder) holderCom;
			holder.name.setText("Name : " + mData.getName());
			holder.branch.setText("Branch : " + mData.getBranch());
			holder.batch.setText("Batch : " + mData.getBatch());
			holder.year.setText("Year : " + mData.getYear());
			holder.enrollmentNumber.setText("Enrollment Number : "
					+ mData.getEnrollment_number());
			holder.email.setText("Email : " + mData.getEmail());
			holder.aboutText.setText(mData.getAbout());
			holder.numberOfPosts.setText("Number of Posts Posted : "
					+ mData.getNumber_of_posts());
			holder.numberOfUpvotes.setText("Number of Upvotes Received : "
					+ mData.getUpvotes());
			holder.numberOfDownvotes.setText("Number of Downvotes Received : "
					+ mData.getDownvotes());
			if (mData.getPhone() == null || mData.getPhone().isEmpty()) {
				holder.mobile.setText("Phone : -");
			} else
				holder.mobile.setText("Phone : " + mData.getPhone());

			holder.checkBoxEmail.setChecked(mData.isIs_email_shown_to_others());
			holder.checkBoxPhone
					.setChecked(mData.isIs_mobile_shown_to_others());
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int type) {
		if (type == Z_RECYCLER_VIEW_ITEM_NORMAL) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.user_profile_edit_profile_list_item_layout, arg0,
					false);
			EditProfileHolder holder = new EditProfileHolder(v);
			return holder;
		} else if (type == Z_USER_PROFILE_ITEM_HEADER) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.fake_header_user_profile, arg0, false);
			FakeHeaderHolder holder = new FakeHeaderHolder(v);
			return holder;
		}
		return null;
	}

	class FakeHeaderHolder extends RecyclerView.ViewHolder {

		LinearLayout header;

		public FakeHeaderHolder(View v) {
			super(v);
			header = (LinearLayout) v.findViewById(R.id.fakehader);
		}
	}

	class EditProfileHolder extends RecyclerView.ViewHolder {

		TextView name, enrollmentNumber, branch, batch, year, numberOfPosts,
				numberOfUpvotes, numberOfDownvotes, aboutText, email, mobile;
		CheckBox checkBoxEmail, checkBoxPhone;

		public EditProfileHolder(View v) {
			super(v);
			name = (TextView) v.findViewById(R.id.name);
			enrollmentNumber = (TextView) v.findViewById(R.id.enrollment);
			branch = (TextView) v.findViewById(R.id.branch);
			batch = (TextView) v.findViewById(R.id.batch);
			year = (TextView) v.findViewById(R.id.year);
			numberOfPosts = (TextView) v.findViewById(R.id.posts);
			numberOfUpvotes = (TextView) v.findViewById(R.id.upvotes);
			numberOfDownvotes = (TextView) v.findViewById(R.id.downvotes);
			aboutText = (TextView) v.findViewById(R.id.about);
			email = (TextView) v.findViewById(R.id.emailuser);
			mobile = (TextView) v.findViewById(R.id.calluser);
			checkBoxEmail = (CheckBox) v.findViewById(R.id.emailvisible);
			checkBoxPhone = (CheckBox) v.findViewById(R.id.phonevisible);
		}
	}

	class MyClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
		}
	}

}

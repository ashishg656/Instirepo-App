package com.instirepo.app.adapters;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.instirepo.app.R;
import com.instirepo.app.activities.BaseActivity;
import com.instirepo.app.activities.UserProfileActivity;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.UserProfileEditProfileObject;
import com.instirepo.app.preferences.ZPreferences;

public class UserProfileEditProfileListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants,
		ZUrls {

	Context context;
	UserProfileEditProfileObject mData;

	EditProfileHolder holderChangeEmailVisibility;
	EditProfileHolder holderChangePhoneVisibility;

	public UserProfileEditProfileListAdapter(Context context,
			UserProfileEditProfileObject mData) {
		super();
		this.context = context;
		this.mData = mData;
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
			final EditProfileHolder holder = (EditProfileHolder) holderCom;
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

			holder.checkBoxPhone.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					setPhoneVisibilityChangeRequestToServer(
							holder.checkBoxPhone.isChecked(), holder);
				}
			});

			holder.checkBoxEmail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					setEmailVisibilityChangeRequestToServer(
							holder.checkBoxEmail.isChecked(), holder);
				}
			});

			if (mData.getResume() == null || mData.getResume().isEmpty()) {
				holder.downloadResume.setVisibility(View.GONE);
				holder.deleteResume.setVisibility(View.GONE);
				holder.editResume.setText("Upload Resume");
			} else {
				holder.downloadResume.setVisibility(View.VISIBLE);
				holder.deleteResume.setVisibility(View.VISIBLE);
				holder.editResume.setText("Edit Resume");
			}

			holder.downloadResume.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(mData.getResume()));
					context.startActivity(i);
				}
			});

			holder.deleteResume.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					deleteResumeRequest();
				}
			});
		}
	}

	protected void deleteResumeRequest() {
		StringRequest req = new StringRequest(Method.POST, deleteResumeRequest,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
			}
		};
	}

	protected void setPhoneVisibilityChangeRequestToServer(
			final boolean isChecked, EditProfileHolder holder) {
		holderChangePhoneVisibility = holder;
		StringRequest request = new StringRequest(Method.POST,
				changePhoneVisibility, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						((BaseActivity) context)
								.makeToast("Unable to change phone visibility. Check internet");
						if (holderChangePhoneVisibility != null) {
							holderChangePhoneVisibility.checkBoxPhone.toggle();
						}
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("user_id", ZPreferences.getUserProfileID(context));
				p.put("is_visible", Boolean.toString(isChecked));
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(request,
				changePhoneVisibility);
	}

	protected void setEmailVisibilityChangeRequestToServer(
			final boolean isChecked, EditProfileHolder holder) {
		holderChangeEmailVisibility = holder;
		StringRequest request = new StringRequest(Method.POST,
				changeEmailVisibility, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						((BaseActivity) context)
								.makeToast("Unable to change email visibility. Check internet");
						if (holderChangeEmailVisibility != null) {
							holderChangeEmailVisibility.checkBoxEmail.toggle();
						}
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> p = new HashMap<>();
				p.put("user_id", ZPreferences.getUserProfileID(context));
				p.put("is_visible", Boolean.toString(isChecked));
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(request,
				changeEmailVisibility);
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
				numberOfUpvotes, numberOfDownvotes, aboutText, email, mobile,
				downloadResume, deleteResume, editResume;
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
			downloadResume = (TextView) v.findViewById(R.id.downloadresuem);
			editResume = (TextView) v.findViewById(R.id.editresume);
			deleteResume = (TextView) v.findViewById(R.id.deleteresume);
			checkBoxEmail = (CheckBox) v.findViewById(R.id.emailvisible);
			checkBoxPhone = (CheckBox) v.findViewById(R.id.phonevisible);
		}
	}

}

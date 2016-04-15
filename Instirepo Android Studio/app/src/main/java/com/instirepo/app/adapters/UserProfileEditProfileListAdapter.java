package com.instirepo.app.adapters;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DropboxLink;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AppKeyPair;
import com.instirepo.app.R;
import com.instirepo.app.activities.BaseActivity;
import com.instirepo.app.activities.UserProfileActivity;
import com.instirepo.app.afilechooser.utils.FileUtils;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.DropboxFilesObject;
import com.instirepo.app.objects.UserProfileEditProfileObject;
import com.instirepo.app.preferences.ZPreferences;

public class UserProfileEditProfileListAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants,
		ZUrls {

	Context context;
	UserProfileEditProfileObject mData;

	EditProfileHolder holderChangeEmailVisibility;
	EditProfileHolder holderChangePhoneVisibility;

	ProgressDialog progressDialog, dropboxProgressDialog;
	AlertDialog alertDialog;

	final static private String APP_KEY = "ku1kknp8f14k7a6";
	final static private String APP_SECRET = "INSERT_APP_SECRET";
	private DropboxAPI<AndroidAuthSession> mDBApi;
	private static final int SELECT_FILE_FROM_AFILECHOOSER_CODE = 455;

	public UserProfileEditProfileListAdapter(Context context,
			UserProfileEditProfileObject mData) {
		super();
		this.context = context;
		this.mData = mData;
		AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
		AndroidAuthSession session = new AndroidAuthSession(appKeys);
		mDBApi = new DropboxAPI<AndroidAuthSession>(session);
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return Z_USER_PROFILE_ITEM_HEADER;
		} else {
			return Z_RECYCLER_VIEW_ITEM_NORMAL;
		}
	}

	private void checkIfUserAuthenticatedDropbox() {
		if (ZPreferences.getDropboxToken(context) == null) {
			mDBApi.getSession().startOAuth2Authentication(
					(UserProfileActivity) context);
		} else {
			intentForRequestingFileFromBrowser();
		}
	}

	private void intentForRequestingFileFromBrowser() {
		Intent getContentIntent = FileUtils.createGetContentIntent();
		Intent intent = Intent.createChooser(getContentIntent, "Select a file");
		((UserProfileActivity) context).startActivityForResult(intent,
				SELECT_FILE_FROM_AFILECHOOSER_CODE);
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

			holder.editResume.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					checkIfUserAuthenticatedDropbox();
				}
			});

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

			holder.editAbout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showEditAboutDialog();
				}
			});

			holder.editPhone.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showEditPhoneDialog();
				}
			});
		}
	}

	protected void showEditPhoneDialog() {
		View content = LayoutInflater.from(context).inflate(
				R.layout.edit_phone_dialog_layout, null, false);
		final EditText editText = (EditText) content.findViewById(R.id.about);
		editText.setText(mData.getPhone());
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Edit Contact Number");
		builder.setView(content);
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		builder.setPositiveButton("Save", null);
		alertDialog = builder.create();
		alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {

				Button b = ((AlertDialog) dialog)
						.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						if (editText.getText().toString().trim().length() > 0) {
							savePhoneOnServer(editText.getText().toString()
									.trim());
						}
					}
				});
			}
		});
		alertDialog.show();
	}

	protected void showEditAboutDialog() {
		View content = LayoutInflater.from(context).inflate(
				R.layout.edit_about_dialog_layout, null, false);
		final EditText editText = (EditText) content.findViewById(R.id.about);
		editText.setText(mData.getAbout());
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Edit About");
		builder.setView(content);
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		builder.setPositiveButton("Save", null);
		alertDialog = builder.create();
		alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {

				Button b = ((AlertDialog) dialog)
						.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						if (editText.getText().toString().trim().length() > 0) {
							saveAboutOnServer(editText.getText().toString()
									.trim());
						}
					}
				});
			}
		});
		alertDialog.show();
	}

	protected void saveAboutOnServer(final String about) {
		progressDialog = ProgressDialog.show(context, "Editing About",
				"Please Wait", true, false);

		StringRequest req = new StringRequest(Method.POST, editAboutUrl,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						if (progressDialog != null)
							progressDialog.dismiss();

						((BaseActivity) context)
								.makeToast("Edited About successfully");
						mData.setAbout(about);
						notifyItemChanged(1);

						if (alertDialog != null)
							alertDialog.dismiss();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						if (progressDialog != null)
							progressDialog.dismiss();

						((BaseActivity) context)
								.makeToast("Error. Check internet");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> p = new HashMap<>();
				p.put("user_id", ZPreferences.getUserProfileID(context));
				p.put("about", about);
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, editAboutUrl);
	}

	protected void savePhoneOnServer(final String about) {
		progressDialog = ProgressDialog.show(context, "Editing Number",
				"Please Wait", true, false);

		StringRequest req = new StringRequest(Method.POST, editPhoneUrl,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						if (progressDialog != null)
							progressDialog.dismiss();

						((BaseActivity) context)
								.makeToast("Edited Number successfully");
						mData.setPhone(about);
						notifyItemChanged(1);

						if (alertDialog != null)
							alertDialog.dismiss();
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						if (progressDialog != null)
							progressDialog.dismiss();

						((BaseActivity) context)
								.makeToast("Error. Check internet");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> p = new HashMap<>();
				p.put("user_id", ZPreferences.getUserProfileID(context));
				p.put("number", about);
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, editPhoneUrl);
	}

	protected void deleteResumeRequest() {
		progressDialog = ProgressDialog.show(context, "Deleting Resume",
				"Please Wait", true, false);

		StringRequest req = new StringRequest(Method.POST, deleteResumeRequest,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						if (progressDialog != null)
							progressDialog.dismiss();

						((BaseActivity) context)
								.makeToast("Deleted resume successfully");
						mData.setResume(null);
						notifyItemChanged(1);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						((BaseActivity) context)
								.makeToast("Unable to delete resume. Check internet");
						if (progressDialog != null)
							progressDialog.dismiss();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> p = new HashMap<>();
				p.put("user_id", ZPreferences.getUserProfileID(context));
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, deleteResumeRequest);
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
		LinearLayout editAbout, editPhone;

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
			editAbout = (LinearLayout) v.findViewById(R.id.editabout);
			editPhone = (LinearLayout) v.findViewById(R.id.editphone);
		}
	}

	public void notifyOnActivityResultFromActivity(int requestCode,
			int resultCode, Intent data) {
		if (requestCode == SELECT_FILE_FROM_AFILECHOOSER_CODE
				&& resultCode == UserProfileActivity.RESULT_OK) {
			if (data == null || data.getData() == null)
				return;
			final Uri uri = data.getData();

			String path = FileUtils.getPath(context, uri);
			if (path != null && FileUtils.isLocal(path)) {
				saveFileToDropbox(path, uri);
			}
		}
	}

	private void saveFileToDropbox(String path, Uri uri) {
		new UploadFileAsyncTask().execute(path);
	}

	public class UploadFileAsyncTask extends
			AsyncTask<String, Long, DropboxFilesObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (dropboxProgressDialog != null)
				dropboxProgressDialog.dismiss();
			dropboxProgressDialog = new ProgressDialog(context);
			dropboxProgressDialog.setMessage("Uplaoding file to dropbox");
			dropboxProgressDialog
					.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dropboxProgressDialog.setIndeterminate(false);
			dropboxProgressDialog.setProgress(0);
			dropboxProgressDialog.setCancelable(false);
			dropboxProgressDialog.setCanceledOnTouchOutside(false);
			dropboxProgressDialog.show();
		}

		@Override
		protected void onPostExecute(DropboxFilesObject obj) {
			super.onPostExecute(obj);
			dropboxProgressDialog.dismiss();
			if (obj == null) {
				((BaseActivity) context)
						.makeToast("Error occured.Please login to Dropbox");
			} else {
				((BaseActivity) context)
						.makeToast("Success uploading dropbox file");

				addResumeToList(obj);
			}
		}

		@Override
		protected DropboxFilesObject doInBackground(String... params) {
			try {
				String path = params[0];
				final File file = new File(path);
				FileInputStream inputStream = new FileInputStream(file);
				Entry response = mDBApi.putFile(file.getName(), inputStream,
						file.length(), null, new ProgressListener() {

							@Override
							public void onProgress(long arg0, long arg1) {
								float progess = (float) arg0 / arg1 * 100.0f;
								publishProgress((long) progess);
							}
						});
				DropboxLink link = mDBApi.share(response.path);

				DropboxFilesObject obj = new DropboxFilesObject(
						response.fileName(), response.parentPath(),
						response.path, response.mimeType, response.modified,
						response.rev, response.size, link.url, response.bytes,
						link.expires);

				return obj;
			} catch (DropboxUnlinkedException e) {
				e.printStackTrace();
				ZPreferences.setDropboxToken(context, null);
				checkIfUserAuthenticatedDropbox();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Long... values) {
			super.onProgressUpdate(values);
			int value = (int) (long) values[0];
			dropboxProgressDialog.setProgress(value);
		}
	}

	public void addResumeToList(final DropboxFilesObject obj) {
		progressDialog = ProgressDialog.show(context, "Uploading Resume",
				"Sending data to server", true, false);
		StringRequest req = new StringRequest(Method.POST, uploadResumeRequest,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						progressDialog.dismiss();
						mData.setResume(obj.getFileLink());
						((BaseActivity) context)
								.makeToast("Successfully uploaded resume");
						notifyItemChanged(1);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						((BaseActivity) context)
								.makeToast("Unable to upload resume. Check net and try again");
						progressDialog.dismiss();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> p = new HashMap<>();
				p.put("user_id", ZPreferences.getUserProfileID(context));
				p.put("fileName", obj.getFileName());
				p.put("parentPath", obj.getParentPath());
				p.put("path", obj.getPath());
				p.put("mimeType", obj.getMimeType());
				p.put("modified", obj.getModified());
				p.put("rev", obj.getRev());
				p.put("size", obj.getSize());
				p.put("fileLink", obj.getFileLink());
				p.put("bytes", obj.getBytes() + "");
				p.put("expires", obj.getExpires());
				return p;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, uploadResumeRequest);
	}

	public void notifyOnResumeCalled() {
		if (mDBApi.getSession().authenticationSuccessful()) {
			try {
				mDBApi.getSession().finishAuthentication();
				String accessToken = mDBApi.getSession().getOAuth2AccessToken();
				ZPreferences.setDropboxToken(context, accessToken);
			} catch (IllegalStateException e) {
				Log.i("DbAuthLog", "Error authenticating", e);
			}
		}
	}

}

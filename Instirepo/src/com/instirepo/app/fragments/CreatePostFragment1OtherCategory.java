package com.instirepo.app.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DropboxLink;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AppKeyPair;
import com.instirepo.app.R;
import com.instirepo.app.activities.BaseActivity;
import com.instirepo.app.activities.CreatePostActivity;
import com.instirepo.app.afilechooser.utils.FileUtils;
import com.instirepo.app.extras.TimeUtils;
import com.instirepo.app.objects.AllPostCategoriesObject;
import com.instirepo.app.objects.CreatePostDataToSendToServer;
import com.instirepo.app.objects.DropboxFilesObject;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.widgets.CustomGoogleFloatingActionButton;
import com.instirepo.app.widgets.RoundedImageView;

public class CreatePostFragment1OtherCategory extends BaseFragment implements
		OnClickListener {

	final static private String APP_KEY = "ku1kknp8f14k7a6";
	final static private String APP_SECRET = "INSERT_APP_SECRET";
	private DropboxAPI<AndroidAuthSession> mDBApi;

	LinearLayout uploadPicLayout, addAttachmentLayout, removeImageLayout,
			addedAttachments, companyNameLayout;
	View addedAttachmentsView;
	TextView uploadPicText;
	FrameLayout imageViewHolder;
	public RoundedImageView roundedImageView;

	public static int SELECT_POST_COVER_PIC = 355;
	private static final int SELECT_FILE_FROM_AFILECHOOSER_CODE = 455;

	ProgressDialog progressDialog, dropboxProgressDialog;
	CustomGoogleFloatingActionButton floatingActionButton;
	public EditText postHeading, postDescription, postCompanyName;

	String imageToSend;

	public ArrayList<DropboxFilesObject> dropboxFilesList;

	// event
	LinearLayout eventDetailsLayout;
	EditText eventLocation, eventFee, eventContact;
	TextView startDate, startTime, endDate, endTime;
	TextView changeStartDate, changeStartTime, changeEndDate, changeEndTime;
	boolean startDateSelected, startTimeSelected, endDateSelected,
			endTimeSelected;

	// poll
	LinearLayout pollDetailsLayout;

	public static CreatePostFragment1OtherCategory newInstance(Bundle b) {
		CreatePostFragment1OtherCategory frg = new CreatePostFragment1OtherCategory();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.create_post_fragment_1_other_category, container,
				false);

		uploadPicLayout = (LinearLayout) v.findViewById(R.id.uploadpiclayout);
		uploadPicText = (TextView) v.findViewById(R.id.uploadpictext);
		roundedImageView = (RoundedImageView) v
				.findViewById(R.id.rounderimagecreapost);
		addAttachmentLayout = (LinearLayout) v
				.findViewById(R.id.googledrivebutton);
		removeImageLayout = (LinearLayout) v
				.findViewById(R.id.crossbuttonimage);
		imageViewHolder = (FrameLayout) v
				.findViewById(R.id.createpostimagelayout);
		addedAttachments = (LinearLayout) v
				.findViewById(R.id.addadeadttachments);
		addedAttachmentsView = (View) v.findViewById(R.id.vieforatatchdcdnckl);
		companyNameLayout = (LinearLayout) v.findViewById(R.id.compaynamlayout);
		floatingActionButton = (CustomGoogleFloatingActionButton) v
				.findViewById(R.id.createpostfab);
		postHeading = (EditText) v.findViewById(R.id.postHeading);
		postDescription = (EditText) v.findViewById(R.id.postdesciption);
		postCompanyName = (EditText) v.findViewById(R.id.postcomapnyname);
		eventDetailsLayout = (LinearLayout) v
				.findViewById(R.id.wvwntdetailslayout);
		startDate = (TextView) v.findViewById(R.id.eventstartdate);
		startTime = (TextView) v.findViewById(R.id.eventstarttime);
		endDate = (TextView) v.findViewById(R.id.eventenddate);
		endTime = (TextView) v.findViewById(R.id.eventendtime);
		changeStartDate = (TextView) v.findViewById(R.id.eventstartdatechange);
		changeStartTime = (TextView) v.findViewById(R.id.eventstarttimechange);
		changeEndDate = (TextView) v.findViewById(R.id.eventenddatechange);
		changeEndTime = (TextView) v.findViewById(R.id.eventendtimechange);
		eventLocation = (EditText) v.findViewById(R.id.eventlocation);
		eventFee = (EditText) v.findViewById(R.id.registrationfee);
		eventContact = (EditText) v
				.findViewById(R.id.contactnumberformoredetails);
		pollDetailsLayout = (LinearLayout) v
				.findViewById(R.id.polldetaillayout);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dropboxFilesList = new ArrayList<>();
		AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
		AndroidAuthSession session = new AndroidAuthSession(appKeys);
		mDBApi = new DropboxAPI<AndroidAuthSession>(session);

		uploadPicLayout.setOnClickListener(this);
		addAttachmentLayout.setOnClickListener(this);
		removeImageLayout.setOnClickListener(this);
		floatingActionButton.setOnClickListener(this);
		changeEndDate.setOnClickListener(this);
		changeEndTime.setOnClickListener(this);
		changeStartDate.setOnClickListener(this);
		changeStartTime.setOnClickListener(this);

		if (((CreatePostActivity) getActivity()).categoryType
				.equals(AllPostCategoriesObject.categoryPlacement)) {
			companyNameLayout.setVisibility(View.VISIBLE);
		} else if (((CreatePostActivity) getActivity()).categoryType
				.equals(AllPostCategoriesObject.categoryEvent)) {
			eventDetailsLayout.setVisibility(View.VISIBLE);
		} else if (((CreatePostActivity) getActivity()).categoryType
				.equals(AllPostCategoriesObject.categoryPoll)) {
			pollDetailsLayout.setVisibility(View.VISIBLE);
		}
	}

	public void setImageForPost(Bitmap bitmap) {
		imageToSend = getStringImage(bitmap);
		imageViewHolder.setVisibility(View.VISIBLE);
		roundedImageView.setImageBitmap(bitmap);
		uploadPicText.setText("Change Post Cover Pic");
	}

	void removeImageForPost() {
		imageToSend = null;
		uploadPicText.setText("Upload Post Cover Pic (optional)");
		roundedImageView.setImageBitmap(null);
		imageViewHolder.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.uploadpiclayout:
			Intent intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*");
			startActivityForResult(intent, SELECT_POST_COVER_PIC);
			break;
		case R.id.googledrivebutton:
			checkIfUserAuthenticatedDropbox();
			break;
		case R.id.crossbuttonimage:
			removeImageForPost();
			break;
		case R.id.createpostfab:
			if (checkIfCanGoNext()) {
				moveToSecondFragmentForSelectingPostVisibility();
			}
			break;
		case R.id.eventenddatechange:
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);

			DatePickerDialog datePickerDialog = new DatePickerDialog(
					getActivity(), new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							endDateSelected = true;
							endDate.setText(TimeUtils
									.getNormalisedDateFromDatePicker(year,
											monthOfYear, dayOfMonth));
							endDate.setTextColor(getActivity().getResources()
									.getColor(R.color.z_text_color_medium_dark));
						}
					}, year, month, day);
			datePickerDialog.show();
			break;
		case R.id.eventendtimechange:
			calendar = Calendar.getInstance();
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);

			TimePickerDialog timePickerDialog = new TimePickerDialog(
					getActivity(), new OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							endTimeSelected = true;
							endTime.setText(TimeUtils
									.getNormalisedTimeFromTimePicker(hourOfDay,
											minute));
							endTime.setTextColor(getActivity().getResources()
									.getColor(R.color.z_text_color_medium_dark));
						}
					}, hour, minute, false);
			timePickerDialog.show();
			break;
		case R.id.eventstartdatechange:
			calendar = Calendar.getInstance();
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			day = calendar.get(Calendar.DAY_OF_MONTH);

			datePickerDialog = new DatePickerDialog(getActivity(),
					new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							startDateSelected = true;
							startDate.setText(TimeUtils
									.getNormalisedDateFromDatePicker(year,
											monthOfYear, dayOfMonth));
							startDate
									.setTextColor(getActivity()
											.getResources()
											.getColor(
													R.color.z_text_color_medium_dark));
						}
					}, year, month, day);
			datePickerDialog.show();
			break;
		case R.id.eventstarttimechange:
			calendar = Calendar.getInstance();
			hour = calendar.get(Calendar.HOUR_OF_DAY);
			minute = calendar.get(Calendar.MINUTE);

			timePickerDialog = new TimePickerDialog(getActivity(),
					new OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							startTimeSelected = true;
							startTime.setText(TimeUtils
									.getNormalisedTimeFromTimePicker(hourOfDay,
											minute));
							startTime
									.setTextColor(getActivity()
											.getResources()
											.getColor(
													R.color.z_text_color_medium_dark));
						}
					}, hour, minute, false);
			timePickerDialog.show();
			break;
		default:
			break;
		}
	}

	public void onResume() {
		super.onResume();
		((CreatePostActivity) getActivity()).isFirstFragmentVisible = true;
		if (mDBApi.getSession().authenticationSuccessful()) {
			try {
				mDBApi.getSession().finishAuthentication();
				String accessToken = mDBApi.getSession().getOAuth2AccessToken();
				ZPreferences.setDropboxToken(getActivity(), accessToken);
			} catch (IllegalStateException e) {
				Log.i("DbAuthLog", "Error authenticating", e);
			}
		}
	}

	private void checkIfUserAuthenticatedDropbox() {
		if (ZPreferences.getDropboxToken(getActivity()) == null) {
			mDBApi.getSession().startOAuth2Authentication(
					(CreatePostActivity) getActivity());
		} else {
			intentForRequestingFileFromBrowser();
		}
	}

	private void moveToSecondFragmentForSelectingPostVisibility() {
		CreatePostDataToSendToServer data = new CreatePostDataToSendToServer(
				postHeading.getText().toString().trim(), postDescription
						.getText().toString().trim(), postCompanyName.getText()
						.toString().trim(), imageToSend, -1, -1,
				dropboxFilesList);

		((CreatePostActivity) getActivity())
				.setSecondFragmentForPostVisibility(data);
	}

	boolean checkIfCanGoNext() {
		if (postHeading.getText().toString().trim().length() == 0) {
			makeToast("Please enter post heading");
			return false;
		} else if (postDescription.getText().toString().trim().length() == 0) {
			makeToast("Please enter post description");
			return false;
		} else if (((CreatePostActivity) getActivity()).categoryType
				.equalsIgnoreCase(AllPostCategoriesObject.categoryEvent)) {
			if (eventLocation.getText().toString().trim().length() < 1) {
				makeToast("Please enter location of event");
				return false;
			} else if (eventFee.getText().toString().trim().length() < 1) {
				makeToast("Please enter registration fee for event. Enter Rs.0 for no registration fee.");
				return false;
			} else if (eventContact.getText().toString().trim().length() < 1) {
				makeToast("Please enter contact number for event");
				return false;
			} else if (!startDateSelected || !startTimeSelected
					|| !endDateSelected || !endTimeSelected) {
				makeToast("Please enter starting and ending dates and times for event");
				return false;
			}
		}
		return true;
	}

	private void intentForRequestingFileFromBrowser() {
		Intent getContentIntent = FileUtils.createGetContentIntent();
		Intent intent = Intent.createChooser(getContentIntent, "Select a file");
		startActivityForResult(intent, SELECT_FILE_FROM_AFILECHOOSER_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == getActivity().RESULT_OK) {
			if (requestCode == CreatePostFragment1OtherCategory.SELECT_POST_COVER_PIC) {
				Uri selectedImageUri = data.getData();
				String[] projection = { MediaColumns.DATA };
				Cursor cursor = getActivity().managedQuery(selectedImageUri,
						projection, null, null, null);
				int column_index = cursor
						.getColumnIndexOrThrow(MediaColumns.DATA);
				cursor.moveToFirst();
				String selectedImagePath = cursor.getString(column_index);
				Bitmap bm;
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(selectedImagePath, options);
				final int REQUIRED_SIZE = 400;
				int scale = 1;
				while (options.outWidth / scale / 2 >= REQUIRED_SIZE
						&& options.outHeight / scale / 2 >= REQUIRED_SIZE)
					scale *= 2;
				options.inSampleSize = scale;
				options.inJustDecodeBounds = false;
				bm = BitmapFactory.decodeFile(selectedImagePath, options);
				setImageForPost(bm);
			} else if (requestCode == CreatePostFragment1OtherCategory.SELECT_FILE_FROM_AFILECHOOSER_CODE) {
				final Uri uri = data.getData();

				String path = FileUtils.getPath(getActivity(), uri);
				if (path != null && FileUtils.isLocal(path)) {
					saveFileToDropbox(path, uri);
				}
			}
		}
	}

	private void saveFileToDropbox(final String path, Uri uri) {
		new UploadFileAsyncTask().execute(path);
	}

	public String getStringImage(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] imageBytes = baos.toByteArray();
		String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
		return encodedImage;
	}

	private void addFileToFilesList(DropboxFilesObject obj) {
		dropboxFilesList.add(obj);

		if (addedAttachments.getVisibility() == View.GONE) {
			addedAttachments.setVisibility(View.VISIBLE);
			addedAttachmentsView.setVisibility(View.VISIBLE);
		}
		TextView textView = new TextView(getActivity());
		textView.setText(obj.getFileName());
		textView.setTextColor(getActivity().getResources().getColor(
				R.color.z_text_color_dark));
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.bottomMargin = getActivity().getResources()
				.getDimensionPixelSize(R.dimen.z_margin_small);
		textView.setLayoutParams(params);

		addedAttachments.addView(textView);
	}

	public String getPathFromUri(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getActivity().getContentResolver().query(uri,
				projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public class UploadFileAsyncTask extends
			AsyncTask<String, Long, DropboxFilesObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (dropboxProgressDialog != null)
				dropboxProgressDialog.dismiss();
			dropboxProgressDialog = new ProgressDialog(getActivity());
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
				((BaseActivity) getActivity())
						.makeToast("Error occured.Please login to Dropbox");
			} else {
				((BaseActivity) getActivity())
						.makeToast("Success uploading dropbox file");

				addFileToFilesList(obj);
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
								Log.w("As", "arg0 " + arg0 + "  arg1  " + arg1);
								float progess = (float) arg0 / arg1 * 100.0f;
								publishProgress((long) progess);
							}
						});
				Log.i("DbExampleLog", "The uploaded file's rev is: "
						+ response.rev);

				DropboxLink link = mDBApi.share(response.path);

				DropboxFilesObject obj = new DropboxFilesObject(
						response.fileName(), response.parentPath(),
						response.path, response.mimeType, response.modified,
						response.rev, response.size, link.url, response.bytes,
						link.expires);

				return obj;
			} catch (DropboxUnlinkedException e) {
				e.printStackTrace();
				ZPreferences.setDropboxToken(getActivity(), null);
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
}

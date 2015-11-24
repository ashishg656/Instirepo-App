package com.instirepo.app.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.instirepo.app.R;
import com.instirepo.app.activities.CreatePostActivity;
import com.instirepo.app.afilechooser.utils.FileUtils;
import com.instirepo.app.objects.AllPostCategoriesObject;
import com.instirepo.app.widgets.RoundedImageView;

public class CreatePostFragment1OtherCategory extends BaseFragment implements
		OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {

	LinearLayout uploadPicLayout, addAttachmentLayout, removeImageLayout,
			addedAttachments, companyNameLayout;
	View addedAttachmentsView;
	TextView uploadPicText;
	FrameLayout imageViewHolder;
	public RoundedImageView roundedImageView;

	static final int REQUEST_CODE_RESOLUTION = 155;
	public static final int REQUEST_CODE_CREATOR = 255;
	public static int SELECT_POST_COVER_PIC = 355;
	private static final int SELECT_FILE_FROM_AFILECHOOSER_CODE = 455;

	private GoogleApiClient mGoogleApiClient;

	ArrayList<String> fileNames;
	public ArrayList<String> fileUrls;
	String fileName;

	ProgressDialog progressDialog;
	FloatingActionButton floatingActionButton;
	public EditText postHeading, postDescription, postCompanyName;

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
		floatingActionButton = (FloatingActionButton) v
				.findViewById(R.id.createpostfab);
		postHeading = (EditText) v.findViewById(R.id.postHeading);
		postDescription = (EditText) v.findViewById(R.id.postdesciption);
		postCompanyName = (EditText) v.findViewById(R.id.postcomapnyname);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fileNames = new ArrayList<>();
		fileUrls = new ArrayList<>();

		uploadPicLayout.setOnClickListener(this);
		addAttachmentLayout.setOnClickListener(this);
		removeImageLayout.setOnClickListener(this);
		floatingActionButton.setOnClickListener(this);

		if (((CreatePostActivity) getActivity()).categoryType
				.equals(AllPostCategoriesObject.categoryPlacement)) {
			companyNameLayout.setVisibility(View.VISIBLE);
		}
	}

	private void connectGoogleApiClient() {
		if (mGoogleApiClient == null) {
			mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
					.addApi(Drive.API).addScope(Drive.SCOPE_FILE)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this).build();
		}
		mGoogleApiClient.connect();
	}

	public void setImageForPost(Bitmap bitmap) {
		imageViewHolder.setVisibility(View.VISIBLE);
		roundedImageView.setImageBitmap(bitmap);
		uploadPicText.setText("Change Post Cover Pic");
	}

	void removeImageForPost() {
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
			if (mGoogleApiClient.isConnected()) {
				intentForRequestingFileFromBrowser();
			} else {
				connectGoogleApiClient();
			}
			break;
		case R.id.crossbuttonimage:
			removeImageForPost();
			break;
		case R.id.createpostfab:
			if (checkIfCanGoNext()) {
				((CreatePostActivity) getActivity())
						.setSecondFragmentForPostVisibility();
			}
			break;
		default:
			break;
		}
	}

	boolean checkIfCanGoNext() {
		if (postHeading.getText().toString().trim().length() == 0) {
			makeToast("Please enter post heading");
			return false;
		} else if (postDescription.getText().toString().trim().length() == 0) {
			makeToast("Please enter post description");
			return false;
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
					saveFileToDrive(path, uri);
				}
			} else if (requestCode == CreatePostFragment1OtherCategory.REQUEST_CODE_CREATOR) {
				if (progressDialog != null)
					progressDialog.dismiss();

				Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT)
						.show();

				DriveId driveId = (DriveId) data
						.getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
				Log.w("a", "File created with ID: " + driveId);

				fileNames.add(fileName);
				fileUrls.add(driveId.toString());

				addFileToFilesList(fileName, driveId.toString());
			}
		} else if (requestCode == CreatePostFragment1OtherCategory.REQUEST_CODE_CREATOR) {
			if (progressDialog != null)
				progressDialog.dismiss();
		}
	}

	private void addFileToFilesList(String fileName, String driveId) {
		if (addedAttachments.getVisibility() == View.GONE) {
			addedAttachments.setVisibility(View.VISIBLE);
			addedAttachmentsView.setVisibility(View.VISIBLE);
		}
		TextView textView = new TextView(getActivity());
		textView.setText(fileName);
		textView.setTextColor(getActivity().getResources().getColor(
				R.color.z_text_color_dark));
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.bottomMargin = getActivity().getResources()
				.getDimensionPixelSize(R.dimen.z_margin_mini);
		textView.setLayoutParams(params);

		addedAttachments.addView(textView);
	}

	private void saveFileToDrive(final String path, final Uri uri) {
		Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(
				new ResultCallback<DriveContentsResult>() {

					@Override
					public void onResult(DriveContentsResult result) {
						if (!result.getStatus().isSuccess()) {
							Log.i("tag", "Failed to create new contents.");
							return;
						}
						Log.i("tag", "New contents created.");
						OutputStream outputStream = result.getDriveContents()
								.getOutputStream();

						File file = new File(path);

						try {
							byte[] byteArray = new byte[(int) file.length()];
							FileInputStream fileInputStream = new FileInputStream(
									file);
							fileInputStream.read(byteArray);
							fileInputStream.close();

							outputStream.write(byteArray);
						} catch (IOException e1) {
							e1.printStackTrace();
						} finally {

						}

						fileName = file.getName();

						MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
								.setMimeType(
										getActivity().getContentResolver()
												.getType(uri))
								.setTitle(file.getName()).build();
						IntentSender intentSender = Drive.DriveApi
								.newCreateFileActivityBuilder()
								.setInitialMetadata(metadataChangeSet)
								.setInitialDriveContents(
										result.getDriveContents())
								.build(mGoogleApiClient);

						if (progressDialog != null
								&& progressDialog.isShowing())
							progressDialog.dismiss();
						progressDialog = ProgressDialog
								.show(getActivity(), "Processing",
										"Processing selected file for Google Drive upload");

						try {
							getActivity().startIntentSenderForResult(
									intentSender, REQUEST_CODE_CREATOR, null,
									0, 0, 0);
						} catch (SendIntentException e) {
							Log.i("tag", "Failed to launch file chooser.");
						}
					}
				});
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

	@Override
	public void onPause() {
		if (mGoogleApiClient != null)
			mGoogleApiClient.disconnect();
		super.onPause();
	}

	@Override
	public void onResume() {
		((CreatePostActivity) getActivity()).isFirstFragmentVisible = true;
		connectGoogleApiClient();
		super.onResume();
	}

	@Override
	public void onConnected(Bundle arg0) {
		Log.i("as", "API client connected.");
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		Log.i("TAG", "GoogleApiClient connection suspended");
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GoogleApiAvailability.getInstance()
					.getErrorDialog(getActivity(), result.getErrorCode(), 0)
					.show();
			return;
		}
		try {
			result.startResolutionForResult(getActivity(),
					REQUEST_CODE_RESOLUTION);
		} catch (SendIntentException e) {
			Log.e("tag", "Exception while starting resolution activity", e);
		}
	}

}

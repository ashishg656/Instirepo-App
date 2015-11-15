package com.instirepo.app.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.google.android.gms.drive.MetadataChangeSet;
import com.instirepo.app.R;
import com.instirepo.app.widgets.RoundedImageView;

public class CreatePostFragment1OtherCategory extends BaseFragment implements
		OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {

	LinearLayout uploadPicLayout, addAttachmentLayout, removeImageLayout;
	TextView uploadPicText;
	FrameLayout imageViewHolder;
	RoundedImageView roundedImageView;
	Uri filePickURI;
	private static Drive mService;

	static final int REQUEST_CODE_RESOLUTION = 155;
	static final int REQUEST_CODE_CREATOR = 255;
	public static int SELECT_POST_COVER_PIC = 355;
	private static final int SELECT_FILE_FROM_BROWSER_CODE = 455;

	private GoogleApiClient mGoogleApiClient;

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

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		uploadPicLayout.setOnClickListener(this);
		addAttachmentLayout.setOnClickListener(this);
		removeImageLayout.setOnClickListener(this);
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

		default:
			break;
		}
	}

	private void intentForRequestingFileFromBrowser() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		// intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("*/*");
		startActivityForResult(intent, SELECT_FILE_FROM_BROWSER_CODE);
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
			} else if (requestCode == CreatePostFragment1OtherCategory.SELECT_FILE_FROM_BROWSER_CODE) {
				filePickURI = data.getData();

				// String FilePath = data.getData().getPath();
				// String FileName = data.getData().getLastPathSegment();
				// int lastPos = FilePath.length() - FileName.length();
				// String Folder = FilePath.substring(0, lastPos);

				saveFileToDrive();
			} else if (requestCode == CreatePostFragment1OtherCategory.REQUEST_CODE_CREATOR) {
				Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	private void saveFileToDrive() {
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

						String path = getPathFromUri(filePickURI);
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

						MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
								.setMimeType(
										getActivity().getContentResolver()
												.getType(filePickURI))
								.setTitle(file.getName()).build();
						IntentSender intentSender = Drive.DriveApi
								.newCreateFileActivityBuilder()
								.setInitialMetadata(metadataChangeSet)
								.setInitialDriveContents(
										result.getDriveContents())
								.build(mGoogleApiClient);
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

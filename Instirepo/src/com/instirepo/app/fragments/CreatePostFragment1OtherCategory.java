package com.instirepo.app.fragments;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.Files;
import com.instirepo.app.R;
import com.instirepo.app.dropboxtasks.DropboxClient;
import com.instirepo.app.dropboxtasks.UploadFileTask;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.widgets.RoundedImageView;

public class CreatePostFragment1OtherCategory extends BaseFragment implements
		OnClickListener {

	LinearLayout uploadPicLayout, addAttachmentLayout, removeImageLayout;
	TextView uploadPicText;
	FrameLayout imageViewHolder;
	public static int SELECT_POST_COVER_PIC = 50;
	RoundedImageView roundedImageView;
	private static final int SELECT_FILE_FROM_BROWSER_CODE = 173;

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
			checkIfUserAuthenticatedDropbox();
			break;
		case R.id.crossbuttonimage:
			removeImageForPost();
			break;

		default:
			break;
		}
	}

	private void checkIfUserAuthenticatedDropbox() {
		if (ZPreferences.getDropboxToken(getActivity()) == null) {
			String accessToken = Auth.getOAuth2Token();
			if (accessToken != null) {
				ZPreferences.setDropboxToken(getActivity(), accessToken);
				intentForRequestingFileFromBrowser();
			} else {
				Auth.startOAuth2Authentication(getActivity(), getActivity()
						.getResources().getString(R.string.dropbox_app_key));
			}
		} else {
			intentForRequestingFileFromBrowser();
		}
	}

	private void intentForRequestingFileFromBrowser() {
		// boolean isKitKat = Build.VERSION.SDK_INT >=
		// Build.VERSION_CODES.KITKAT;
		// if (isKitKat) {
		// Intent intent = new Intent();
		// intent.setType("*/*");
		// intent.setAction(Intent.ACTION_GET_CONTENT);
		// startActivityForResult(intent, SELECT_FILE_FROM_BROWSER_CODE);
		//
		// } else {
		// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		// intent.setType("*/*");
		// startActivityForResult(intent, SELECT_FILE_FROM_BROWSER_CODE);
		// }

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
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
				// Uri selectedFile = data.getData();
				//
				// // String FilePath = data.getData().getPath();
				// // String FileName = data.getData().getLastPathSegment();
				// // int lastPos = FilePath.length() - FileName.length();
				// // String Folder = FilePath.substring(0, lastPos);
				//
				// uploadFile(data.getData().toString());

				newCodeToGetFileFromUri(data);
			}
		}
	}

	public byte[] getBytes(InputStream is) throws IOException {

		int len;
		int size = 1024;
		byte[] buf;

		if (is instanceof ByteArrayInputStream) {
			size = is.available();
			buf = new byte[size];
			len = is.read(buf, 0, size);
		} else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];
			while ((len = is.read(buf, 0, size)) != -1)
				bos.write(buf, 0, len);
			buf = bos.toByteArray();
		}
		return buf;
	}

	private void newCodeToGetFileFromUri(Intent data) {
		Uri uri = data.getData();
		byte[] fileContent;
		InputStream inputStream = null;

		try {
			inputStream = getActivity().getContentResolver().openInputStream(
					uri);
			if (inputStream != null) {
				fileContent = new byte[1];
				inputStream.read(fileContent);
				fileContent = new byte[1024];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int read;
				while ((read = inputStream.read(fileContent)) > -1)
					baos.write(fileContent, 0, read);
				fileContent = baos.toByteArray();
				baos.close();
				Log.v("as", "-----> Input Stream: " + inputStream);
				Log.v("as", "-----> Byte Array: " + fileContent.length);
			} else {
				Log.e("AS", "-----> Input Stream is null");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void uploadFile(String fileUri) {
		final ProgressDialog dialog = new ProgressDialog(getActivity());
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setCancelable(false);
		dialog.setMessage("Uploading");
		dialog.show();

		new UploadFileTask(getActivity(), DropboxClient.files(),
				new UploadFileTask.Callback() {
					@Override
					public void onUploadComplete(Files.FileMetadata result) {
						dialog.dismiss();

						Toast.makeText(
								getActivity(),
								result.name + " size " + result.size
										+ " modified "
										+ result.clientModified.toGMTString(),
								Toast.LENGTH_SHORT).show();

					}

					@Override
					public void onError(Exception e) {
						dialog.dismiss();
						Toast.makeText(getActivity(), "An error has occurred",
								Toast.LENGTH_SHORT).show();
					}
				}).execute(fileUri, "POSTS");
	}
}

package com.instirepo.app.fragments;

import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.instirepo.app.R;
import com.instirepo.app.activities.CreatePostActivity;
import com.instirepo.app.afilechooser.utils.FileUtils;
import com.instirepo.app.objects.AllPostCategoriesObject;
import com.instirepo.app.objects.CreatePostDataToSendToServer;
import com.instirepo.app.widgets.CustomGoogleFloatingActionButton;
import com.instirepo.app.widgets.RoundedImageView;

public class CreatePostFragment1OtherCategory extends BaseFragment implements
		OnClickListener {

	LinearLayout uploadPicLayout, addAttachmentLayout, removeImageLayout,
			addedAttachments, companyNameLayout;
	View addedAttachmentsView;
	TextView uploadPicText;
	FrameLayout imageViewHolder;
	public RoundedImageView roundedImageView;

	public static int SELECT_POST_COVER_PIC = 355;
	private static final int SELECT_FILE_FROM_AFILECHOOSER_CODE = 455;

	ArrayList<String> fileNames;
	public ArrayList<String> fileUrls;
	String fileName;

	ProgressDialog progressDialog;
	CustomGoogleFloatingActionButton floatingActionButton;
	public EditText postHeading, postDescription, postCompanyName;

	String imageToSend;

	private static FileDataStoreFactory dataStoreFactory;
	private static HttpTransport httpTransport;
	private static final JsonFactory JSON_FACTORY = JacksonFactory
			.getDefaultInstance();
	private static Drive drive;
	private static final String APPLICATION_NAME = "Instirepo";
	private static final String UPLOAD_FILE_PATH = "Enter File Path";
	private static final String DIR_FOR_DOWNLOADS = "Enter Download Directory";
	private static final java.io.File UPLOAD_FILE = new java.io.File(
			UPLOAD_FILE_PATH);
	private static final java.io.File DATA_STORE_DIR = new java.io.File(
			System.getProperty("user.home"), ".store/drive_sample");

	private static Credential authorize() throws Exception {
		Reader r = new StringReader(
				"{\"installed\":{\"client_id\":\"860159170646-epn9dli0ipo9oo1qv1572ng1jhpk8s0p.apps.googleusercontent.com\",\"project_id\":\"instirepo-1089\",\"auth_uri\":\"https://accounts.google.com/o/oauth2/auth\",\"token_uri\":\"https://accounts.google.com/o/oauth2/token\",\"auth_provider_x509_cert_url\":\"https://www.googleapis.com/oauth2/v1/certs\",\"redirect_uris\":[\"urn:ietf:wg:oauth:2.0:oob\",\"http://localhost\"]}}");

		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
				JSON_FACTORY, r);
		if (clientSecrets.getDetails().getClientId().startsWith("Enter")
				|| clientSecrets.getDetails().getClientSecret()
						.startsWith("Enter ")) {
			System.out
					.println("Enter Client ID and Secret from https://code.google.com/apis/console/?api=drive "
							+ "into drive-cmdline-sample/src/main/resources/client_secrets.json");
			System.exit(1);
		}
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, JSON_FACTORY, clientSecrets,
				Collections.singleton(DriveScopes.DRIVE_FILE))
				.setDataStoreFactory(dataStoreFactory).build();
		return new AuthorizationCodeInstalledApp(flow,
				new LocalServerReceiver()).authorize("user");
	}

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

		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
			Credential credential = authorize();
			drive = new Drive.Builder(httpTransport, JSON_FACTORY, credential)
					.setApplicationName(APPLICATION_NAME).build();
		} catch (Exception e) {

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
			// if (mGoogleApiClient.isConnected()) {
			// intentForRequestingFileFromBrowser();
			// } else {
			// connectGoogleApiClient();
			// }
			break;
		case R.id.crossbuttonimage:
			removeImageForPost();
			break;
		case R.id.createpostfab:
			if (checkIfCanGoNext()) {
				moveToSecondFragmentForSelectingPostVisibility();
			}
			break;
		default:
			break;
		}
	}

	private void moveToSecondFragmentForSelectingPostVisibility() {
		CreatePostDataToSendToServer data = new CreatePostDataToSendToServer(
				postHeading.getText().toString().trim(), postDescription
						.getText().toString().trim(), postCompanyName.getText()
						.toString().trim(), imageToSend, -1, -1);

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
			}
		}
	}

	private void saveFileToDrive(String path, Uri uri) {

	}

	public String getStringImage(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] imageBytes = baos.toByteArray();
		String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
		return encodedImage;
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

	public String getPathFromUri(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getActivity().getContentResolver().query(uri,
				projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

}

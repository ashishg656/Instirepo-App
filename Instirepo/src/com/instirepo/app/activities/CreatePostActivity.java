package com.instirepo.app.activities;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder.DriveFileResult;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.instirepo.app.R;
import com.instirepo.app.circularreveal.SupportAnimator;
import com.instirepo.app.circularreveal.ViewAnimationUtils;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.ZAnimatorListener;
import com.instirepo.app.extras.ZCircularAnimatorListener;
import com.instirepo.app.fragments.CreatePostFragment1OtherCategory;

@SuppressLint("NewApi")
public class CreatePostActivity extends BaseActivity implements AppConstants,
		ConnectionCallbacks, OnConnectionFailedListener {

	CreatePostFragment1OtherCategory createPostFragment1OtherCategory;
	int touchX, touchY;
	View circularRevealView;
	int deviceHeight;
	LinearLayout mainLayoutForFragment;
	int toolbarHeight;
	AppBarLayout appBarLayout;

	private static final String TAG = "PickFileWithOpenerActivity";
	private static final int REQUEST_CODE_OPENER = 2;
	protected static final int REQUEST_CODE_RESOLUTION = 1;
	File file;

	private GoogleApiClient mGoogleApiClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_post_activity_layout);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		circularRevealView = (View) findViewById(R.id.circularereavelaveiw);
		mainLayoutForFragment = (LinearLayout) findViewById(R.id.linearlayoutcreatepost);
		appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		toolbarHeight = getResources().getDimensionPixelSize(
				R.dimen.z_toolbar_height);
		appBarLayout.setTranslationY(-toolbarHeight);

		touchX = getIntent().getExtras().getInt("touchx");
		touchY = getIntent().getExtras().getInt("touchy");
		deviceHeight = getResources().getDisplayMetrics().heightPixels;

		setFirstFragmentForOthersCategory();

		circularRevealView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						try {
							circularRevealView.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						} catch (Exception e) {
							circularRevealView.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						}
						showAndHideCircularRevealView();
					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mGoogleApiClient == null) {
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					.addApi(Drive.API).addScope(Drive.SCOPE_FILE)
					.addScope(Drive.SCOPE_APPFOLDER)
					// required for App Folder sample
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this).build();
		}
		mGoogleApiClient.connect();
	}

	@Override
	protected void onPause() {
		if (mGoogleApiClient != null) {
			mGoogleApiClient.disconnect();
		}
		super.onPause();
	}

	private void showAndHideCircularRevealView() {
		SupportAnimator anim = ViewAnimationUtils.createCircularReveal(
				circularRevealView, touchX, touchY, 0, deviceHeight);
		anim.setDuration(500);
		anim.setInterpolator(new AccelerateDecelerateInterpolator());
		anim.addListener(new ZCircularAnimatorListener() {
			@Override
			public void onAnimationEnd() {
				mainLayoutForFragment.setVisibility(View.VISIBLE);
				circularRevealView.animate().alpha(0).setDuration(300)
						.setListener(new ZAnimatorListener() {
							@Override
							public void onAnimationEnd(Animator animation) {
								circularRevealView.setVisibility(View.GONE);
								appBarLayout.animate().translationY(0)
										.setDuration(250).start();
							}
						}).start();
			}
		});
		anim.start();
	}

	void setFirstFragmentForOthersCategory() {
		createPostFragment1OtherCategory = CreatePostFragment1OtherCategory
				.newInstance(new Bundle());
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragmtnholder, createPostFragment1OtherCategory)
				.commit();
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to discrad this post?");
		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				CreatePostActivity.this.finish();
			}
		});
		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_RESOLUTION && resultCode == RESULT_OK) {
			mGoogleApiClient.connect();
		} else if (requestCode == REQUEST_CODE_OPENER) {
			if (resultCode == RESULT_OK) {
				DriveId driveId = (DriveId) data
						.getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
				Log.w("as", "Selected file's ID: " + driveId);
			}
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			// show the localized error dialog.
			GoogleApiAvailability.getInstance()
					.getErrorDialog(this, result.getErrorCode(), 0).show();
			return;
		}
		try {
			result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
		} catch (SendIntentException e) {
			Log.e(TAG, "Exception while starting resolution activity", e);
		}
	}

	public void requestFileOpen() {
		IntentSender intentSender = Drive.DriveApi.newOpenFileActivityBuilder()
				.setMimeType(new String[] { "text/plain", "text/html" })
				.build(mGoogleApiClient);
		try {
			startIntentSenderForResult(intentSender, REQUEST_CODE_OPENER, null,
					0, 0, 0);
		} catch (SendIntentException e) {
			Log.w(TAG, "Unable to send intent", e);
		}
	}

	public void createFile(File file) {
		this.file = file;
		Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(
				driveContentsCallback);
	}

	public void showMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	final private ResultCallback<DriveContentsResult> driveContentsCallback = new ResultCallback<DriveContentsResult>() {
		@Override
		public void onResult(DriveContentsResult result) {
			if (!result.getStatus().isSuccess()) {
				showMessage("Error while trying to create new file contents");
				return;
			}
			final DriveContents driveContents = result.getDriveContents();

			// Perform I/O off the UI thread.
			new Thread() {
				@Override
				public void run() {
					// write content to DriveContents
					OutputStream outputStream = driveContents.getOutputStream();
					Writer writer = new OutputStreamWriter(outputStream);
					try {
						writer.write("Hello World!");
						writer.close();
					} catch (IOException e) {
						Log.e(TAG, e.getMessage());
					}

					MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
							.setTitle("New file").setMimeType("text/plain")
							.setStarred(true).build();

					// create a file on root folder
					Drive.DriveApi
							.getRootFolder(mGoogleApiClient)
							.createFile(mGoogleApiClient, changeSet,
									driveContents)
							.setResultCallback(fileCallback);
				}
			}.start();
		}
	};

	final private ResultCallback<DriveFileResult> fileCallback = new ResultCallback<DriveFileResult>() {
		@Override
		public void onResult(DriveFileResult result) {
			if (!result.getStatus().isSuccess()) {
				showMessage("Error while trying to create the file");
				return;
			}
			showMessage("Created a file with content: "
					+ result.getDriveFile().getDriveId());
		}
	};
}

package com.instirepo.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.instirepo.app.R;
import com.instirepo.app.objects.PostListSinglePostObject;

public abstract class BaseActivity extends AppCompatActivity {

	public Toolbar toolbar;
	int toolbarHeight;

	ProgressBar progressBar;
	LinearLayout progressBarContainer;

	LinearLayout connectionErrorLayout;
	LinearLayout retryDataConnectionLayout;
	LinearLayout connectionFailedCloudImage;
	Toast toast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	void setProgressLayoutVariablesAndErrorVariables() {
		progressBar = (ProgressBar) findViewById(R.id.progressbarloading);
		progressBarContainer = (LinearLayout) findViewById(R.id.progressbarcontainer);

		connectionErrorLayout = (LinearLayout) findViewById(R.id.connection_error_layout);
		retryDataConnectionLayout = (LinearLayout) findViewById(R.id.retrylayoutconnectionerror);
		connectionFailedCloudImage = (LinearLayout) findViewById(R.id.connectionfailedimagelayout);
	}

	void showLoadingLayout() {
		progressBar.setVisibility(View.VISIBLE);
		progressBarContainer.setVisibility(View.VISIBLE);
	}

	void hideLoadingLayout() {
		progressBar.setVisibility(View.GONE);
		progressBarContainer.setVisibility(View.GONE);
	}

	void showErrorLayout() {
		connectionErrorLayout.setVisibility(View.VISIBLE);

		retryDataConnectionLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		connectionFailedCloudImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				retryDataConnectionLayout.performClick();
			}
		});
		findViewById(R.id.opensettingslayout).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(
								Settings.ACTION_WIFI_SETTINGS);
						if (intent.resolveActivity(getPackageManager()) != null) {
							startActivity(intent);
						}
					}
				});
	}

	void hideErrorLayout() {
		if (connectionErrorLayout != null)
			connectionErrorLayout.setVisibility(View.GONE);
	}

	public void makeToast(String text) {
		if (toast != null)
			toast.cancel();
		if (this != null) {
			toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	public void showSnackBar(String text) {
		if (findViewById(R.id.coordinatorlayout) != null)
			Snackbar.make(findViewById(R.id.coordinatorlayout), text,
					Snackbar.LENGTH_SHORT).show();
	}

	void openMessagesListActivity() {
		Intent i = new Intent(this, AllMessagesActivity.class);
		startActivity(i);
	}

	public void openUserProfileActivity() {
		Intent i = new Intent(this, UserProfileActivity.class);
		startActivity(i);
	}

	public void openNotificationsActivity() {
		Intent i = new Intent(this, NotificationsActivity.class);
		startActivity(i);
	}

	public void openUserChatWithPersonUserActivity(int id, String name,
			String image) {
		Intent i = new Intent(this, MessageListActivity.class);
		i.putExtra("person_id", id);
		i.putExtra("person_name", name);
		i.putExtra("person_image", image);
		startActivity(i);
	}

	public void openPostDetailActivity(
			PostListSinglePostObject postListSinglePostObject) {
		Intent i = new Intent(this, PostDetailActivity.class);
		i.putExtra("postobj", postListSinglePostObject);
		startActivity(i);
	}

	public void showEmptyListView(String nullCaseText, boolean fullHeight) {
		LinearLayout emptyLayout = (LinearLayout) findViewById(R.id.nullcaselayoutF);
		TextView textView = (TextView) findViewById(R.id.textnullcaseF);

		if (nullCaseText != null)
			textView.setText(nullCaseText);

		if (fullHeight) {
			ViewGroup.LayoutParams p = emptyLayout.getLayoutParams();
			p.height = getResources().getDisplayMetrics().heightPixels;
			emptyLayout.setLayoutParams(p);
		}
	}

	public void hideEmptyListCase() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.nullcaselayoutF);
		layout.setVisibility(View.GONE);
	}
}

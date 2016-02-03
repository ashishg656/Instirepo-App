package com.instirepo.app.fragments;

import com.instirepo.app.R;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BaseFragment extends Fragment {

	ProgressBar progressBar;
	LinearLayout progressBarContainer;

	LinearLayout connectionErrorLayout;
	LinearLayout retryDataConnectionLayout;
	LinearLayout connectionFailedCloudImage;
	LinearLayout openSettingsLayout;

	Toast toast;

	View rootView;

	void makeToast(String s) {
		if (toast != null)
			toast.cancel();
		if (getActivity() != null) {
			toast = Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	void setProgressLayoutVariablesAndErrorVariables(View v) {
		progressBar = (ProgressBar) v.findViewById(R.id.progressbarloading);
		progressBarContainer = (LinearLayout) v
				.findViewById(R.id.progressbarcontainer);

		connectionErrorLayout = (LinearLayout) v
				.findViewById(R.id.connection_error_layout);
		retryDataConnectionLayout = (LinearLayout) v
				.findViewById(R.id.retrylayoutconnectionerror);
		connectionFailedCloudImage = (LinearLayout) v
				.findViewById(R.id.connectionfailedimagelayout);
		openSettingsLayout = (LinearLayout) v
				.findViewById(R.id.opensettingslayout);
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

		connectionFailedCloudImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				retryDataConnectionLayout.performClick();
			}
		});
		openSettingsLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
				if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
					startActivity(intent);
				}
			}
		});
	}

	void hideErrorLayout() {
		if (connectionErrorLayout != null)
			connectionErrorLayout.setVisibility(View.GONE);
	}

	public void showEmptyListView(String nullCaseText, boolean fullHeight,
			View view) {
		LinearLayout emptyLayout = (LinearLayout) view
				.findViewById(R.id.nullcaselayoutF);
		TextView textView = (TextView) view.findViewById(R.id.textnullcaseF);

		emptyLayout.setVisibility(View.VISIBLE);

		if (nullCaseText != null)
			textView.setText(nullCaseText);

		if (fullHeight && getActivity() != null) {
			ViewGroup.LayoutParams p = emptyLayout.getLayoutParams();
			p.height = getActivity().getResources().getDisplayMetrics().heightPixels;
			emptyLayout.setLayoutParams(p);
		}
	}

	public void hideEmptyListCase(View view) {
		LinearLayout layout = (LinearLayout) view
				.findViewById(R.id.nullcaselayoutF);
		layout.setVisibility(View.GONE);
	}
}

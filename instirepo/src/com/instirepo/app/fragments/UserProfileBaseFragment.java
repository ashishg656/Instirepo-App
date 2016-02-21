package com.instirepo.app.fragments;

import com.instirepo.app.R;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class UserProfileBaseFragment extends BaseFragment {

	public RecyclerView recyclerView;
	public LinearLayoutManager layoutManager;
	LinearLayout progressSuperContainer;

	@Override
	void setProgressLayoutVariablesAndErrorVariables(View v) {
		progressBar = (ProgressBar) v.findViewById(R.id.progressbarloading);
		progressBarContainer = (LinearLayout) v
				.findViewById(R.id.progressbarcontainer);

		connectionErrorLayout = (LinearLayout) v
				.findViewById(R.id.connection_error_layout);
		retryDataConnectionLayout = (LinearLayout) v
				.findViewById(R.id.retrylayoutconnectionerror);
	}

	void showLoadingLayout() {
		progressBar.setVisibility(View.VISIBLE);
		progressBarContainer.setVisibility(View.VISIBLE);
		progressSuperContainer.setVisibility(View.VISIBLE);
	}

	void hideLoadingLayout() {
		progressSuperContainer.setVisibility(View.GONE);
		progressBar.setVisibility(View.GONE);
		progressBarContainer.setVisibility(View.GONE);
	}

	void showErrorLayout() {
		connectionErrorLayout.setVisibility(View.VISIBLE);
	}

	void hideErrorLayout() {
		if (connectionErrorLayout != null)
			connectionErrorLayout.setVisibility(View.GONE);
	}

}

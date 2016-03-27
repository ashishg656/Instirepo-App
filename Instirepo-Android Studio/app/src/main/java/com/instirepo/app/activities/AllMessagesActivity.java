package com.instirepo.app.activities;

import com.instirepo.app.R;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.fragments.AllMessagesFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class AllMessagesActivity extends BaseActivity {

	AllMessagesFragment allMessagesFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_messages_activity_layout);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("All Messages");

		setMessagesFragment();
	}

	private void setMessagesFragment() {
		allMessagesFragment = AllMessagesFragment.newInstance(new Bundle());

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragmentcontainer, allMessagesFragment).commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AppConstants.Z_REQUEST_CODE_MESSAGES_ACTIVITY
				&& resultCode == RESULT_OK) {
			String personID = data.getStringExtra("person_id");
			String message = data.getStringExtra("last_message");
			String time = data.getStringExtra("time");

			if (allMessagesFragment != null
					&& allMessagesFragment.adapter != null)
				allMessagesFragment.adapter.updateEntryAfterBackPress(personID,
						message, time);
		}
	}
}

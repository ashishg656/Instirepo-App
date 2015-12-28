package com.instirepo.app.activities;

import com.instirepo.app.R;
import com.instirepo.app.fragments.AllMessagesFragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class AllMessagesActivity extends BaseActivity {

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
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragmentcontainer,
						AllMessagesFragment.newInstance(new Bundle())).commit();
	}

}

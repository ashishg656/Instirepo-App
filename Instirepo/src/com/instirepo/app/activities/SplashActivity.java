package com.instirepo.app.activities;

import android.content.Intent;
import android.os.Bundle;

import com.instirepo.app.R;
import com.instirepo.app.preferences.ZPreferences;

public class SplashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity_layout);

		switchToLoginActivityOrHomeActivity();
	}

	private void switchToLoginActivityOrHomeActivity() {
		Intent i = new Intent(this, ZLaunchActivity.class);
		if (ZPreferences.isUserLogIn(this)) {
			i = new Intent(this, ZHomeActivity.class);
		}
		startActivity(i);
		this.finish();
	}
}

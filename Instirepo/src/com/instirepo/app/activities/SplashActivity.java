package com.instirepo.app.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;

import com.instirepo.app.R;
import com.instirepo.app.preferences.ZPreferences;

public class SplashActivity extends BaseActivity {

	int splashDuration = 800;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity_layout);

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				switchToLoginActivityOrHomeActivity();
			}
		}, splashDuration);
	}

	private void switchToLoginActivityOrHomeActivity() {
		Intent i = new Intent(this, LaunchActivity.class);
		if (ZPreferences.isUserLogIn(this)) {
			i = new Intent(this, HomeActivity.class);
		}
		startActivity(i);
		this.finish();
	}
}

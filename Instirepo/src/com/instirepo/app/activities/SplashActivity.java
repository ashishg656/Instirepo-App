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

		if (ZPreferences.isUserLogIn(this)) {
			switchToHomeActivity();
		} else {
			new Timer().schedule(new TimerTask() {

				@Override
				public void run() {
					switchToLoginActivity();
				}
			}, splashDuration);
		}
	}

	private void switchToLoginActivity() {
		Intent i = new Intent(this, LaunchActivity.class);
		startActivity(i);
		this.finish();
	}

	public void switchToHomeActivity() {
		Intent i = new Intent(this, HomeActivity.class);
		i.putExtra("showsplash", true);
		startActivity(i);
		overridePendingTransition(0, 0);
		this.finish();
	}
}

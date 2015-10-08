package com.example.faceless.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;

import com.example.faceless.R;
import com.example.faceless.preferences.ZPreferences;

public class SplashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spash_activity_layout);

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						switchToHomeOrLoginActivity();
					}
				});
			}
		}, 1000);
	}

	private void switchToHomeOrLoginActivity() {
		Intent i = new Intent(this, LaunchActivity.class);
		if (ZPreferences.isUserLogIn(this)) {
			i = new Intent(this, HomeActivity.class);
		}
		startActivity(i);
		this.finish();
	}
}
